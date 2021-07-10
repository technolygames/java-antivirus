package clase;

import java.io.File;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class antivirus extends JFrame implements Runnable{
    File[] myFile;
        
    JTextField textField=new JTextField("",50);//for file name
    JTextArea textArea=new JTextArea();//for displaying directories
    JLabel fileLabel=new JLabel("Nombre del archivo :");//for file name label
    JLabel dirLabel=new JLabel("Directorio :");//for directory label
    JLabel status=new JLabel("Estado: listo");//current status label
    JScrollPane srollPane=new JScrollPane(textArea);//scroll bars in text area
    JComboBox comboBox=new JComboBox();//for combo box
    JButton cleanVirus=new JButton("Limpiar virus");//for search button
    
    String fileName;//for storing file name
    String dir;//for storing directory
    
    int directoriesCount=0;
    
    boolean deleted=false;//for changing the status
    
    antivirus(){
        directoriesCount = File.listRoots().length;//counting total disk drives
        myFile = File.listRoots();//a file object for storing root directoris
        
        for(int i=0;i<directoriesCount;i++){
            comboBox.addItem(myFile[i]);//adding directories in comboBox
        }
        
        JPanel up = new JPanel();//making a new panel to add north
        
        add(up,"North");//adding panel in north position
        up.add(fileLabel);//first adding File Name label
        up.add(textField);//then text field to enter file name
        up.add(dirLabel);//directory label
        up.add(comboBox);//then comboBox
        up.add(status);//status label
        up.add(cleanVirus);//search button
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Antivirus");
        setSize(1024,700);//frame size
        setVisible(true);//frame visibility
        
        add(srollPane);//adding scroll pane
        cleanVirus.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent me){//overriding mouseClicked of anonymouse MouseAdapter class
                fileName=textField.getText();//set the file name by getting the text from text field
                dir=comboBox.getSelectedItem().toString();//directory seleted from combo box
                dir+="\\";//appending double slashes to dir e.g C:\\
                status.setText("Encontrando virus");//setting the status
                startThread();//creating a new thread using this function
            }
        });//end anonymous class and mouselistener
    }//end constructor
    
    public void startThread(){
        Thread t=new Thread(this);//making a thread object
        t.start();//starting thread
    }
    
    @Override
    public void run(){//overriding run because of implementing runnable interface
        try{
            DeleteVirus(fileName,new File(dir));//calling delete virus function
            if(deleted==true){
                status.setText("Virus eliminado");//setting the label if file is deleted
            }else{
                status.setText("Virus no encontrado");//otherwise no file is found is displayed
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error:\n"+e);
        }//handling the exception
    }
    
    public void DeleteVirus(String vDir,File v){
        File[] list=v.listFiles();//listing files
        if(list!=null){ //checking so that nullpointerException cannot occur
            for(File subfile:list){//listing every subdirectory
                textArea.append(""+v.getAbsolutePath());//showing in text area
                textArea.append("\n");
                File v_file=new File(v.getAbsolutePath(), fileName);//getting the path of file
                if(v_file.isFile()){//v_file points to a file
                    v_file.delete();//delete that file which has specified name
                    deleted = true;//turn boolean to true to set the label later
                }
                if(subfile.isDirectory()){
                    DeleteVirus(vDir,subfile);//if more directories then search through them
                }
            }//end for
        }//end if
    }//end DeleteVirus
    
    public static void main(String[] args){
        new antivirus();//creating the antivirus object
    }//end main
}// end JFrame