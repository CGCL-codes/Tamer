package cz.zcu.fav.os.console;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import cz.zcu.fav.os.vm.ProcessDesc;
import cz.zcu.fav.os.vm.VMM;
import cz.zcu.fav.os.vm.ProcessDesc.ProcessState;

/**
 * Input stream for reading from console.
 * @author Tomáš Hofhans
 * @since 18.11.2009
 */
public class ConsoleInputStream extends InputStream {

    /** Log. */
    private static final Logger LOG = Logger.getLogger(ConsoleInputStream.class.getName());

    /**
   * Data from console to process. Processed data is sored to the end of process. This is place to
   * potential optimalization. But there is no problem with synchronization because StringBuffer is
   * synchronized.
   */
    private StringBuffer data;

    /** Console for input. */
    private final Console console;

    /** Number of readed bytes. */
    private int readed = 0;

    /**
   * Constructor for making input stram clone.
   * @param cis input stream for clone
   */
    public ConsoleInputStream(ConsoleInputStream cis) {
        this(cis.console);
    }

    /**
   * Creating console input stream.
   * @param console console which write to this input
   */
    public ConsoleInputStream(Console console) {
        super();
        this.console = console;
        data = new StringBuffer();
    }

    @Override
    public int read() throws IOException {
        VMM vmm = VMM.getInstance();
        ProcessDesc pd = vmm.getProcessDesc(Thread.currentThread().getId());
        while (available() == 0) {
            try {
                if (pd.getId() != console.getInputPid()) {
                    if (pd.getState() == ProcessState.RUNNING || pd.getState() == ProcessState.INIT) {
                        pd.setState(ProcessState.SUSPENDED);
                    }
                    while (pd.getState() == ProcessState.SUSPENDED) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            if (pd.getState() == ProcessState.TERMINATED) {
                                Thread.currentThread().stop();
                            }
                        }
                    }
                }
                if (pd.getState() == ProcessState.DONE || pd.getState() == ProcessState.TERMINATED) {
                    return -1;
                }
                Thread.sleep(10);
            } catch (InterruptedException e) {
                if (pd.getState() == ProcessState.TERMINATED) {
                    Thread.currentThread().stop();
                }
                LOG.log(Level.INFO, "Interrupted.", e);
            }
        }
        int out = -1;
        synchronized (data) {
            out = data.charAt(readed);
            if (out == 4) {
                out = -1;
            } else {
                readed++;
            }
        }
        return out;
    }

    /** {@inheritDoc} */
    public int available() {
        synchronized (data) {
            return data.length() - readed;
        }
    }

    /**
   * Write data from console.
   * @param data data from console
   */
    public void write(String data) {
        this.data.append(data);
    }
}
