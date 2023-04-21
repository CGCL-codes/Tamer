package cz.zcu.fav.os;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import cz.zcu.fav.os.command.init;
import cz.zcu.fav.os.exception.VMMRuntimeException;
import cz.zcu.fav.os.vm.ProcessDesc;
import cz.zcu.fav.os.vm.VMRunnable;
import cz.zcu.fav.os.vm.ProcessDesc.ProcessState;

/**
 * Main class for start up.
 * @author Tomáš Hofhans
 * @since 28.10.2009
 */
public class Main {

    private static final String LOG_DIR = "logs";

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    /**
   * Main method for start up.
   * @param args arguments
   */
    public static void main(String[] args) {
        File logs = new File(LOG_DIR);
        if (!logs.exists() || !logs.isDirectory()) {
            if (!logs.mkdir()) {
                throw new VMMRuntimeException("Couldn't create '" + LOG_DIR + "' directory.");
            }
        }
        if (LOG.isLoggable(Level.INFO)) {
            LOG.info("Application starting up.");
        }
        VMRunnable init = new init();
        ProcessDesc pd = new ProcessDesc(null, null, args, null, null, null, ProcessState.RUNNING, init, new File("."), "init");
        init.setProcessDesc(pd);
        init.start();
    }
}
