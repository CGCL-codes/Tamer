package uk.ac.aber.Blockmation;

import java.io.IOException;

/**
 * @author Alex
 *
 */
public class MainAppDriver {

    /**
         * @param args
         * @throws IOException 
         */
    public static void main(String[] args) throws IOException {
        SplashScreen splashScreen = new SplashScreen();
        MainFrame mainFrame = new MainFrame();
        while (splashScreen.isRunning()) {
        }
        splashScreen.hide();
        mainFrame.setVisible(true);
    }
}
