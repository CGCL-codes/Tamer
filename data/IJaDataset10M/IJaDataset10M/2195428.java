package visitpc.lib.gui;

import java.io.*;
import visitpc.lib.gui.splash.*;
import java.util.*;
import javax.swing.ImageIcon;
import visitpc.lib.io.*;

/**
 * Responsible for displaying the VisitPC splash Screen on startup.
 */
public class VisitPCSplash {

    private static Splash splash;

    private static int MilliSecondsDelay = 0;

    private static long StartTimeMilliSeconds;

    private static Timer ProgressTimer;

    public static void ShowVisitPCSplash() {
        File userDir = new File(System.getProperty("user.dir"));
        String fileList[] = userDir.list();
        File splashImageFile = new File(userDir, "logo.png");
        SimpleConfigHelper simpleConfigHelper = new SimpleConfigHelper();
        ImageIcon splashImageIconFromJar = simpleConfigHelper.getLogoImageIcon();
        if (splashImageIconFromJar != null) {
            String logoFileName = simpleConfigHelper.getLogoFileName();
            if (logoFileName.startsWith("logo") && logoFileName.endsWith(".png")) {
                StringTokenizer strTok = new StringTokenizer(logoFileName, "_");
                if (strTok.countTokens() == 3) {
                    strTok.nextToken();
                    String secStr = strTok.nextToken();
                    try {
                        VisitPCSplash.MilliSecondsDelay = Integer.parseInt(secStr) * 1000;
                    } catch (NumberFormatException e) {
                    }
                }
            }
        } else {
            for (String filename : fileList) {
                if (filename.startsWith("logo") && filename.endsWith(".png")) {
                    if (filename.endsWith("_sec.png")) {
                        StringTokenizer strTok = new StringTokenizer(filename, "_");
                        if (strTok.countTokens() == 3) {
                            strTok.nextToken();
                            String secStr = strTok.nextToken();
                            try {
                                VisitPCSplash.MilliSecondsDelay = Integer.parseInt(secStr) * 1000;
                                splashImageFile = new File(userDir, filename);
                            } catch (NumberFormatException e) {
                            }
                        }
                    }
                }
            }
        }
        if (splashImageFile.isFile() || splashImageIconFromJar != null) {
            if (splashImageIconFromJar != null) {
                splash = new Splash(splashImageIconFromJar);
            } else {
                splash = new Splash(splashImageFile.getAbsolutePath());
            }
            splash.setVisible(true);
            VisitPCSplash.StartTimeMilliSeconds = System.currentTimeMillis();
            ProgressTimer = new Timer();
            ProgressTimer.scheduleAtFixedRate(new TimerTask() {

                public void run() {
                    splash.updateProgress((int) (System.currentTimeMillis() - VisitPCSplash.StartTimeMilliSeconds), VisitPCSplash.MilliSecondsDelay);
                }
            }, new Date(), 10);
        }
    }

    public static void HideVisitPCSplash() {
        if (splash != null) {
            if (VisitPCSplash.MilliSecondsDelay > 0) {
                long msPassed = System.currentTimeMillis() - VisitPCSplash.StartTimeMilliSeconds;
                try {
                    Thread.sleep(VisitPCSplash.MilliSecondsDelay - msPassed);
                } catch (InterruptedException e) {
                }
                ProgressTimer.cancel();
                ProgressTimer = null;
            }
            splash.setVisible(false);
        }
    }
}
