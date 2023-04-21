package org.sunshine.mamadu;

import org.sunshine.mamadu.gui.MainFrame;
import org.sunshine.mamadu.Listeners.SendMailListener;
import java.net.URL;
import java.util.logging.Level;

/**
 * Base algorithm:
 * <blockquote><pre>
 * 1) open file if exists
 * 2) read into DOM and after into Objects
 * 3) read page and parse it into Objects
 * 4) if there is new ads add them to list, save history list to file and send to email recepients.
 * if this is first time than do not send email.
 * 5) do sleep
 * 6) go to step 3.
 * </pre></blockquote>
 */
public class Runner {

    private static URL mamaduUrl = null;

    private static final MamaduParser mamaduParser = new MamaduParser();

    public static void main(String[] args) {
        try {
            LocalLogger.LOGGER.info("Mamadu notifier started...");
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg != null && arg.equalsIgnoreCase("-url") && i + 1 < args.length) {
                    String url = args[i + 1];
                    if (url != null) {
                        mamaduUrl = new URL(url);
                    }
                }
            }
            final MainFrame mainFrame = new MainFrame();
            new Thread(mainFrame, "main_frame").start();
            Thread.sleep(500);
            MamaduCheck mamaduCheck = new MamaduCheck();
            mamaduCheck.addListener(mainFrame);
            SendMailListener sendMailListener = new SendMailListener();
            sendMailListener.init();
            mamaduCheck.addListener(sendMailListener);
            mamaduCheck.start();
        } catch (Exception e) {
            LocalLogger.LOGGER.log(Level.SEVERE, "Critical exception occured.", e);
        }
        LocalLogger.LOGGER.info("Mamadu notifier ended...");
    }
}
