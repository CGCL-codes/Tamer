package mya_dc.compilation_server;

import java.io.File;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import mya_dc.compilation_server.database.DatabaseManager;
import mya_dc.compilation_server.gui.CompilationServerGUI;

/**
 * Main class of the Compilation Server entity
 * 
 * @author      Marina Skarbovsky
 * <br>			MYA
 */
public class CompilationServerMain {

    /**
 	 * main function that initializes the application
	 */
    public static void main(String[] args) {
        String sWorkingDirectory = System.getProperty("user.dir") + "\\server\\";
        File fPathCreator = new File(sWorkingDirectory + "projects\\compilation");
        fPathCreator.mkdirs();
        fPathCreator = new File(sWorkingDirectory + "projects\\sources");
        fPathCreator.mkdirs();
        JFrame frame = new JFrame();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e1) {
        }
        String gcc = System.getProperty("user.dir") + "\\MinGW\\bin\\g++.exe";
        String h2 = sWorkingDirectory + "database\\H2\\bin\\h2.jar";
        if (!new File(gcc).exists()) {
            JOptionPane.showMessageDialog(frame, "File not found:\n" + gcc + "\nMake sure file exists before running MYA-DC Compilation Server.", "Error", 0);
            System.exit(1);
        }
        if (!new File(h2).exists()) {
            JOptionPane.showMessageDialog(frame, "File not found:\n" + h2 + "\nMake sure file exists before running MYA-DC Compilation Server.", "Error", 0);
            System.exit(1);
        }
        try {
            DatabaseManager.initializeDatabase(sWorkingDirectory + "database\\");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "Database driver cannot be loaded. Attempt reinstalling the application.", "Error", 0);
            System.exit(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database initialization failed. Make sure that an instance of the application is not already running, if not attempt to reinstall the application.", "Error", 0);
            System.exit(1);
        }
        CompilationServer Server = new CompilationServer(sWorkingDirectory);
        new CompilationServerGUI(Server);
        DatabaseManager.shutDownDatabase();
    }
}
