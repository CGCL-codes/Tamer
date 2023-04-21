package com.limegroup.gnutella.gui;

import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;

public class Main {

    private static URL CHOSEN_SPLASH_URL = null;

    public static CountDownLatch MAC_EVENT_REGISTER_LATCH = null;

    public static String argFilePath = null;

    /** 
	 * Creates an <tt>Initializer</tt> instance that constructs the 
	 * necessary classes for the application.
	 *
	 * @param args the array of command line arguments
	 */
    @SuppressWarnings("unchecked")
    public static void main(String args[]) {
        Frame splash = null;
        try {
            if (isMacOSX()) {
                MAC_EVENT_REGISTER_LATCH = new CountDownLatch(1);
                Class clazz = Class.forName("com.limegroup.gnutella.gui.GURLHandler");
                Method getInstance = clazz.getMethod("getInstance", new Class[0]);
                Object gurl = getInstance.invoke(null, new Object[0]);
                Method register = gurl.getClass().getMethod("register", new Class[0]);
                register.invoke(gurl, new Object[0]);
                MAC_EVENT_REGISTER_LATCH.await();
                System.out.print("preferIPV6Addresses: ");
                System.out.println(System.getProperty("java.net.preferIPV6Addresses"));
                System.out.print("preferIPv4stack: ");
                System.out.println(System.getProperty("java.net.preferIPv4stack"));
                if (isOlderThanLeopard()) {
                    System.setProperty("java.nio.preferSelect", String.valueOf(System.getProperty("java.version").startsWith("1.5")));
                } else {
                    System.setProperty("java.nio.preferSelect", "false");
                }
                System.out.print("java.nio.preferSelect: ");
                System.out.println(System.getProperty("java.nio.preferSelect"));
                if (argFilePath != null) {
                    System.out.println("Main.main() - received args from GURLHandler " + argFilePath);
                    args = new String[] { argFilePath };
                }
            }
            if (args == null || args.length == 0) splash = showInitialSplash();
            Class.forName("com.limegroup.gnutella.gui.GUILoader").getMethod("load", new Class[] { String[].class, Frame.class }).invoke(null, new Object[] { args, splash });
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
	 * Shows the initial splash window.
	 */
    private static Frame showInitialSplash() {
        Frame splashFrame = null;
        Image image = null;
        URL imageURL = getChosenSplashURL();
        if (imageURL != null) {
            image = Toolkit.getDefaultToolkit().createImage(imageURL);
            if (image != null) {
                splashFrame = AWTSplashWindow.splash(image);
            }
        }
        return splashFrame;
    }

    /**
	 * Tries to get a random splash every time. It keeps track of the 
	 * last 2 shown splashes to avoid recent collisions.
	 * @return
	 */
    public static final URL getChosenSplashURL() {
        if (CHOSEN_SPLASH_URL != null) return CHOSEN_SPLASH_URL;
        final String splashPath = "com/frostwire/splash/";
        final int max_splashes = 20;
        final int randomSplash = 1 + (Calendar.getInstance().get(Calendar.MINUTE) % max_splashes);
        CHOSEN_SPLASH_URL = ClassLoader.getSystemResource(splashPath + randomSplash + ".jpg");
        return CHOSEN_SPLASH_URL;
    }

    /** Determines if this is running on OS X. */
    private static boolean isMacOSX() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.startsWith("mac os") && os.endsWith("x");
    }

    /** Determines if this is running a Mac OSX lower than Leopard */
    private static boolean isOlderThanLeopard() {
        String version = System.getProperty("os.version");
        StringTokenizer tk = new StringTokenizer(version, ".");
        int major = Integer.parseInt(tk.nextToken());
        int minor = Integer.parseInt(tk.nextToken());
        return major == 10 && minor < 6;
    }
}
