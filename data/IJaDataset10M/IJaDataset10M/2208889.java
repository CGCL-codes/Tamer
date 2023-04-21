package jtinymediav1.jtinymedia;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import java.util.logging.Level;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import jtinymediav1.config.ConfigManager;
import jtinymediav1.jtinymedia.core.JtmController;
import jtinymediav1.jtinymedia.gui.player.MainFrameExt;
import jtinymediav1.jtinymedia.structure.library.Artist;
import jtinymediav1.jtinymedia.structure.library.MusicAlbum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Administrator
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOGGER.debug("Starting JTinyMedia ... ");
        LOGGER.debug("Start initializing core componenets ...");
        try {
            ConfigManager.init();
            try {
                JtmController.init();
            } catch (UnsupportedAudioFileException ex) {
                java.util.logging.Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            LOGGER.debug("Finished initializing core componenets");
            NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
            NimRODLookAndFeel.setCurrentTheme(ConfigManager.getJtmconfig().getJtmTheme());
            try {
                UIManager.setLookAndFeel(NimRODLF);
            } catch (UnsupportedLookAndFeelException ex) {
                LOGGER.error("Exception setting look and feel", ex);
            }
            LOGGER.debug("Opening main frame ... ");
            java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    new MainFrameExt().setVisible(true);
                }
            });
        } catch (Exception e) {
            LOGGER.error("An exception occured", e);
        }
    }
}
