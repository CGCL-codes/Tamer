package org.schalm.mailcheck;

import java.io.File;
import javax.swing.UIManager;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.schalm.mailcheck.gui.MailCheckerFrame;

/**
 * Main class for starting the application.
 * 
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: Starter.java 34 2009-10-16 23:46:37Z cschalm $
 */
public final class Starter {

    private static Logger log = Logger.getLogger(Starter.class);

    private Starter() {
    }

    public static void main(String[] args) {
        if (new File("log4j.properties").canRead()) {
            PropertyConfigurator.configure("log4j.properties");
            if (log.isDebugEnabled()) {
                log.debug("Starting...");
            }
        } else {
            BasicConfigurator.configure();
        }
        try {
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
            MailCheckerFrame.getInstance();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
