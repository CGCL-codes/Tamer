package org.amityregion5.projectx.client.main;

import javax.swing.UIManager;
import org.amityregion5.projectx.client.communication.MulticastCommunicationHandler;
import org.amityregion5.projectx.client.gui.ServerChooserWindow;
import org.amityregion5.projectx.client.gui.UsernameWindow;
import org.amityregion5.projectx.client.preferences.PreferenceManager;
import org.amityregion5.projectx.client.sound.SoundManager;

/**
 * Main class for loading the Client
 * 
 * @author Daniel Centore
 * @author Joe Stein
 */
public class Main {

    public static final int SPLASH_TIME = 2000;

    public static void load() {
        main(new String[0]);
    }

    public static void main(String[] args) {
        SoundManager.preload();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        if (PreferenceManager.getUsername() == null) {
            new UsernameWindow(null, true, true);
        }
        final ServerChooserWindow chooser = new ServerChooserWindow();
        MulticastCommunicationHandler mch = new MulticastCommunicationHandler();
        mch.registerListener(chooser);
        mch.start();
        chooser.setVisible(true);
    }
}
