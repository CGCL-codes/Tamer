package alx.library;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            log.info("Started");
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
            }
            Settings.init();
            IndexResources.setSettings(Settings.getInstance());
            Engine.setSettings(Settings.getInstance());
            Engine.unlockIndex();
            MainWindow mw = new MainWindow();
            mw.validate();
            mw.setVisible(true);
        } catch (Throwable e) {
            log.log(Level.SEVERE, "Error", e);
            JOptionPane.showMessageDialog(null, "Fatal error has occured:\n" + e.toString() + ": " + e.getMessage() + "\nProgram will quit now.", "Fatal error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
