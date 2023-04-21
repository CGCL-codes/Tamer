package GUI.main;

import java.awt.Font;
import javax.swing.JFrame;
import GUI.network.OfficeConnection;
import OfficeServer.users.Doctor;
import OfficeServer.users.Patient;

/**
 * @author Chris Bayruns
 * 
 */
public class main {

    /**
	 * @author Chris Bayruns
	 * @param args
	 */
    public static final String FRAME_TITLE = new String("The Office 2.0");

    public static final Font LOGO_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);

    public static final String LOGO_STRING = new String("the office 2.0");

    public static OfficeConnection officeConnection;

    public static void main(String[] args) {
        MainFrame mf = new MainFrame();
        mf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mf.pack();
        mf.setVisible(true);
    }
}
