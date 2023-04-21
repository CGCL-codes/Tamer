package seventhsense.gui;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

public class MainWindowTest {

    private static final Logger LOGGER = Logger.getLogger(MainWindowTest.class.getName());

    /**
	 * Launch the application.
	 */
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, e.toString(), e);
                    }
                    final MainWindow window = new MainWindow();
                    window.setVisible(true);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
            }
        });
    }
}
