package net.sourceforge.mandalore.hibernate.hsqldb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Manfred Geiler (latest modification by $Author: gem $)
 * @version $Revision: 679 $ $Date: 13.07.2006 09:15:01$
 */
public class HsqldbManagerStarter {

    private static final Log log = LogFactory.getLog(HsqldbManagerStarter.class);

    public void init() {
        Thread t = new Thread(new DatabaseManagerRunner());
        t.run();
        log.info("HSQL Database Manager was started");
    }

    private static class DatabaseManagerRunner implements Runnable {

        public void run() {
            org.hsqldb.util.DatabaseManager.main(new String[] { "-url", "jdbc:hsqldb:mem:test" });
        }
    }
}
