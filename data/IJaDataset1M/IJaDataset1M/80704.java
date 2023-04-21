package client;

import java.awt.Dimension;
import java.awt.Toolkit;
import client.tabs.GUIFrame;
import client.update.Config;
import common.ConfigParser;
import common.Logger;
import common.Variables;

public class GUI {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    boolean packFrame = false;

    /**
   * Create the application and show it.
   */
    public GUI() {
        GUIFrame frame;
        frame = new GUIFrame();
        if (packFrame) {
        } else {
            frame.validate();
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
        frame.allgemein.setSelectedIndex(0);
        frame.allgemein.setSelectedIndex(2);
        frame.setSize(1280, 1024);
    }

    /**
   * Entrypoint of the application
   *
   * @param args
   *          String[]
   */
    public static void main(String[] args) {
        Logger log;
        log = new Logger();
        try {
            Variables.init();
            ConfigParser configparser = new ConfigParser("cfggui.properties");
            configparser.changeProperties();
            log.addLogDefault();
            log.addLogSystem();
            Config.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String str = "b.jpg";
        (new Thread(new SplashScreen(str, new Dimension(640, 480)))).start();
        new GUI();
    }
}
