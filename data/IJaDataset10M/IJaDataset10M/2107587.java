package org.tripcom.components;

import java.rmi.RMISecurityManager;
import net.jini.space.JavaSpace;
import org.apache.log4j.Logger;
import org.tripcom.ComponentsMain;
import org.tripcom.Constants;
import org.tripcom.integration.entry.DataResultExternal;
import org.tripcom.integration.entry.ErrorResultExternal;
import org.tripcom.integration.entry.SetCookieEntry;
import org.tripcom.integration.javaspace.JavaSpacesUtil;

public class TSAPI extends Thread {

    private JavaSpace javaSpace;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.setProperty("java.security.policy", ComponentsMain.class.getResource("policy.all").getPath());
        System.setSecurityManager(new RMISecurityManager());
        new TSAPI().start();
    }

    public void run() {
        try {
            javaSpace = JavaSpacesUtil.lookupJavaSpace(Constants.JAVASPACESHOST, Constants.JAVASPACESPORT);
            new TSAPIThread1().start();
            new TSAPIThread2().start();
            new TSAPIThread3().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class TSAPIThread1 extends Thread {

        private Logger logger = Logger.getLogger(TSAPIThread1.class);

        public void run() {
            try {
                while (true) {
                    logger.debug("Wait for: SetCookieEntry");
                    SetCookieEntry entry = (SetCookieEntry) javaSpace.take(new SetCookieEntry(), null, Constants.TSAPI1WAIT);
                    if (entry == null) {
                        logger.debug("Timeout");
                        continue;
                    }
                    logger.debug("Took:" + entry);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TSAPIThread2 extends Thread {

        private Logger logger = Logger.getLogger(TSAPIThread2.class);

        public void run() {
            try {
                while (true) {
                    logger.debug("Wait for: DataResultExternal");
                    DataResultExternal entry = (DataResultExternal) javaSpace.take(new DataResultExternal(), null, Constants.TSAPI2WAIT);
                    if (entry == null) {
                        logger.debug("Timeout");
                        continue;
                    }
                    logger.debug("Took:" + entry);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TSAPIThread3 extends Thread {

        private Logger logger = Logger.getLogger(TSAPIThread3.class);

        public void run() {
            try {
                while (true) {
                    logger.debug("Wait for: ErrorResultExternal");
                    ErrorResultExternal entry = (ErrorResultExternal) javaSpace.take(new ErrorResultExternal(), null, Constants.TSAPI2WAIT);
                    if (entry == null) {
                        logger.debug("Timeout");
                        continue;
                    }
                    logger.debug("Took:" + entry);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
