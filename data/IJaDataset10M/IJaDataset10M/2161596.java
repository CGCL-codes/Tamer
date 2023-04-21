package scap.check.exec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.apache.log4j.Logger;

public class IORedirect extends Thread {

    private static final Logger log = Logger.getLogger(IORedirect.class);

    public static final String DEFAULT_PROCESS_NAME = "checking engine";

    private final boolean outputVerbose;

    private final BufferedReader bufIn;

    private final BufferedWriter bufOut;

    private String processName = DEFAULT_PROCESS_NAME;

    public IORedirect(final InputStream in, final OutputStream out, boolean outputVerbose) {
        bufIn = new BufferedReader(new InputStreamReader(in));
        bufOut = new BufferedWriter(new OutputStreamWriter(out));
        this.outputVerbose = outputVerbose;
    }

    public IORedirect(final InputStream in, final BufferedWriter out, boolean outputVerbose) {
        bufIn = new BufferedReader(new InputStreamReader(in));
        bufOut = out;
        this.outputVerbose = outputVerbose;
    }

    public IORedirect(final InputStream in, final Writer out, boolean outputVerbose) {
        bufIn = new BufferedReader(new InputStreamReader(in));
        bufOut = new BufferedWriter(out);
        this.outputVerbose = outputVerbose;
    }

    public void setProcessName(String name) {
        this.processName = name;
    }

    @Override
    public void run() {
        log.info("Executing " + this.processName);
        char[] buf = new char[256];
        int len = 0;
        DefaultPrint dp = new DefaultPrint(this.processName);
        try {
            if (this.outputVerbose == false) {
                Thread t = new Thread(dp);
                t.start();
            }
            while ((len = bufIn.read(buf)) != -1) {
                if (this.outputVerbose && (len > 1)) bufOut.write(buf, 0, len); else ;
            }
        } catch (IOException e) {
            log.error(null, e);
        } finally {
            try {
                bufOut.flush();
                dp.run = false;
            } catch (Exception ex) {
                log.error(null, ex);
            }
            log.info("Finished executing " + this.processName);
        }
    }

    static class DefaultPrint implements Runnable {

        private static final String[] outputProgressTable = new String[] { ".", "..", "....", "......", "........", "............" };

        private final String processName;

        public volatile boolean run = true;

        public DefaultPrint(String processName) {
            this.processName = processName;
        }

        public void run() {
            int index = 0;
            while (run == true) {
                System.out.printf("** executing %s%s                            \r", this.processName, DefaultPrint.outputProgressTable[index]);
                System.out.flush();
                index = (++index % 6);
                try {
                    Thread.sleep(350);
                } catch (Exception ex) {
                    IORedirect.log.error(null, ex);
                    run = false;
                }
            }
        }
    }
}
