package clubmixer.client.splashscreen;

import com.slychief.clubmixer.logging.ClubmixerLogger;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SplashScreen;

/**
 *
 * @author Alexander Schindler
 */
public class ClientSplashScreen {

    public static final int SCREEN_HEIGHT = 334;

    public static final int SCREEN_WIDTH = 500;

    private static ClientSplashScreen instance;

    public static ClientSplashScreen getInstance() {
        if (instance == null) {
            instance = new ClientSplashScreen();
        }
        return instance;
    }

    private SplashScreen splash;

    private Graphics2D g;

    public ClientSplashScreen() {
        splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            ClubmixerLogger.debug(this, "Splashscreen not found or not supported");
            return;
        }
        g = splash.createGraphics();
        if (g == null) {
            ClubmixerLogger.debug(this, "Splashscreen cannot create graphics");
            splash = null;
            return;
        }
        ClubmixerLogger.debug(this, "Splashscreen initiated");
    }

    public void updateSplashscreen(String msg, float percentage) {
        if (splash != null) {
            int progessBarWidth = (int) (SCREEN_WIDTH * percentage);
            renderSplashFrame(g, msg, progessBarWidth);
            splash.update();
        }
    }

    static void renderSplashFrame(Graphics2D g, String msg, int progSize) {
        g.setColor(Color.ORANGE);
        g.fillRect(0, 216, progSize, 4);
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(20, 220, 300, 40);
        g.setPaintMode();
        g.setColor(Color.ORANGE);
        g.drawString(msg, 20, 235);
    }

    public void close() {
        splash.close();
        splash = null;
    }
}
