package GUI.GUIChairMaintenance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import Repository.RepositoryChairMaintenance.Mysql;

/**
 * Class that checks if mySQL and the database are running
 * @author G09
 * @version 0.5.1
 * @since 0.5.1
 */
public class SQLchecker {

    /**
	 * Constructor
	 */
    SQLchecker() {
    }

    /**
	 * Checks if a given process is running on your System
	 * works for Windows
	 * @param process
	 * @return true if the process is running, false if it is not
	 */
    public static boolean isRunning(String process) {
        boolean found = false;
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);
            String vbs = "Set WshShell = WScript.CreateObject(\"WScript.Shell\")\n" + "Set locator = CreateObject(\"WbemScripting.SWbemLocator\")\n" + "Set service = locator.ConnectServer()\n" + "Set processes = service.ExecQuery _\n" + " (\"select name from Win32_Process where name='" + process + "'\")\n" + "For Each process in processes\n" + "wscript.echo process.Name \n" + "Next\n" + "Set WSHShell = Nothing\n";
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            line = input.readLine();
            if (line != null) {
                if (line.equals(process)) {
                    found = true;
                }
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return found;
    }

    /**
	 * Loads the file into the program
	 * @param fileName
	 * @return 
	 */
    public static boolean Load(String fileName) {
        BufferedReader r;
        try {
            r = new BufferedReader(new FileReader(fileName));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
	 * Checks if first time running PAPERS
	 * @return true if first time running, false otherwise
	 */
    public static boolean isFirstTime() {
        return (!Load("options.ini"));
    }

    /**
	 * Main function checks the mysql process,
	 * then if the options file exists and then runs the database
	 * @param args
	 */
    public static void main(String[] args) {
        boolean result = SQLchecker.isRunning("mysqld-nt.exe") || SQLchecker.isRunning("mysqld.exe");
        if (!result) {
            System.out.println("im here");
            msgBox(" PAPERS could not connect to mySQL\n Please check if it is running ");
        } else {
            System.out.println("im on else");
            if (isFirstTime()) {
                SQLCheckerGUI prompt = new SQLCheckerGUI();
                prompt.setVisible(true);
                while (!prompt.isDone()) {
                }
                System.out.println("wrote file");
                Mysql db = new Mysql();
                db.createTables("createTables.sql");
                db.createTables("inserts.sql");
                new InitBD();
            } else new LoginClass();
        }
    }

    /**
	 * Displays a message box in case of error
	 * @param msg
	 */
    public static void msgBox(String msg) {
        javax.swing.JOptionPane.showConfirmDialog((java.awt.Component) null, msg, "mySQL error", javax.swing.JOptionPane.DEFAULT_OPTION);
    }
}
