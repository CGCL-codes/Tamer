package org.posper.graphics;

import java.awt.SplashScreen;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.AlphaComposite;
import java.net.URL;
import org.posper.datautils.AppLocal;

public class SplashManager {

    private static SplashManager m_instance = null;

    private int currentPos;

    private String[] frameMessages;

    private Graphics2D g;

    private Font fontToUse;

    private int splashWidth;

    private int splashHeight;

    private int textXStart;

    private int textYStart;

    private SplashScreen splash;

    /** renders the splash screen **/
    private void renderSplashFrame(String message) {
        g.setComposite(AlphaComposite.Clear);
        if (currentPos > 0) {
            g.fillRect(0, 0, splashWidth, splashHeight);
        }
        g.setPaintMode();
        g.setColor(Color.BLACK);
        g.setFont(fontToUse);
        g.drawString(message + "...", textXStart, textYStart);
    }

    public void renderNextFrame() {
        if (currentPos < frameMessages.length - 1) {
            currentPos++;
            renderSplashFrame(frameMessages[currentPos]);
            splash.update();
        }
    }

    protected SplashManager() {
    }

    protected SplashManager(SplashScreen splashSource, String[] frameMessages, int textXStart, int textYStart, Font fontToUse, URL imageToUse) {
        AppLocal appLocal = AppLocal.getInstance();
        splash = splashSource;
        if (splash == null) {
            System.out.println(appLocal.getIntString("message.nullsplash"));
            return;
        }
        try {
            splash.setImageURL(imageToUse);
        } catch (Exception e) {
            System.out.println(appLocal.getIntString("message.miscsplashproblem") + ":" + e);
            return;
        }
        splashWidth = (int) splash.getBounds().getWidth();
        splashHeight = (int) splash.getBounds().getHeight();
        this.frameMessages = frameMessages;
        this.g = splashSource.createGraphics();
        this.currentPos = -1;
        this.textXStart = textXStart;
        this.textYStart = textYStart;
        this.fontToUse = fontToUse;
    }

    public static SplashManager getInstance() {
        return m_instance;
    }

    public static SplashManager getInstance(SplashScreen splashSource, String[] frameMessages, int textXStart, int textYStart, Font fontToUse, URL imageToUse) {
        if (m_instance == null) {
            m_instance = new SplashManager(splashSource, frameMessages, textXStart, textYStart, fontToUse, imageToUse);
        }
        return m_instance;
    }
}
