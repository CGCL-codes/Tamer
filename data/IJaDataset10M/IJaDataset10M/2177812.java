package org.vo.urp.test;

import org.apache.commons.logging.Log;
import org.ivoa.env.ApplicationAdapter;
import org.ivoa.util.SystemLogUtil;
import org.ivoa.util.timer.TimerFactory;
import org.vo.urp.test.jaxb.XMLTests;

/**
 * Unit Tests : JPA
 *
 * @author Laurent Bourges (voparis) / Gerard Lemson (mpe)
 */
public final class Main {

    /**
   * Creates a new instance of Main
   */
    private Main() {
    }

    /**
   * Main Entry point for test classes
   *
   * @param args the command line arguments
   */
    public static void main(final String[] args) {
        final Log log = initLogs();
        if (log != null) {
            ApplicationAdapter.start();
            final long start = System.nanoTime();
            try {
                new XMLTests().run(args);
                new DBTests().run(args);
            } finally {
                TimerFactory.getTimer("Test.main").addMilliSeconds(start, System.nanoTime());
                ApplicationAdapter.stop();
            }
        }
    }

    /**
   * Initializes Log4J
   *
   * @return Log instance
   */
    private static Log initLogs() {
        Log l = null;
        try {
            l = org.ivoa.util.LogUtil.getLogger();
        } catch (final Throwable th) {
            if (SystemLogUtil.isErrorEnabled()) {
                SystemLogUtil.error("Unable to initialize logging system :", th);
            }
        }
        return l;
    }
}
