package br.nic.connector.general;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Implements the SimpleProcess interface. It is used in order to perform system
 * calls in a safe and fast way in multithreaded environments.
 * 
 * OBS: This function, while made to PERFORM well is a multithreaded
 * environment, is NOT thread safe in absolutely ANY way. Indeed, it was
 * designed considering it will be used by a singleThread at all times.
 * 
 * @author Pedro Hadek
 */
public class SimpleProcess {

    /** Value returned by exec when the execution never responded. */
    public static final String NORESPONSE = "No Response";

    /** Value returned whenever the process couldn't be initialized or such. */
    public static final String PROCESSFAIL = "Process Failure";

    /** Value returned whenever the only output came from the ErrorStream. */
    public static final String ONLYERROR = "Only Error";

    private static final int _10MEGABYTES = 10485760;

    private static final int _1MEGABYTE = 1048576;

    private Process simpProc;

    private StringBuffer errorStreamOutput;

    private boolean verboseMode;

    /**
	 * Constructor; Starts the simpProc that will handle the requests.
	 * @throws IOException
	 * Happens whenever the runscript could not be initialized for whatever reason.
	 */
    public SimpleProcess() throws IOException {
        if (Utils.createRunscript()) {
            try {
                simpProc = Runtime.getRuntime().exec("./runscript.sh");
            } catch (Exception ex) {
                SimpleLog.getInstance().writeException(ex, 3);
                SimpleLog.getInstance().writeLog(3, (simpProc == null ? "Erro no processamento!" : simpProc.toString()));
            }
        } else throw new IOException();
    }

    /**
	 * Must call cleanup on finalize, otherwise simpProc may keep running on the
	 * background, and will consume system resources, even after Java has
	 * closed.
	 * 
	 * TODO: This method isn't perfect, and leaves some runscripts on the background sometimes. Should be improved.
	 */
    @Override
    protected void finalize() throws Throwable {
        cleanup();
        super.finalize();
    }

    /**
	 * Must call cleanup before exiting, otherwise simpProc may keep running on
	 * the background, and will consume system resources, even after Java has
	 * closed.
	 */
    public void cleanup() {
        try {
            BufferedOutputStream os = (BufferedOutputStream) simpProc.getOutputStream();
            os.write(("exit\n").getBytes());
            os.close();
            simpProc.destroy();
        } catch (Exception e) {
            SimpleLog.getInstance().writeException(e, 3);
        }
    }

    /**
	 * Executes a Linux Command, returning it's default stream output value in a
	 * String.
	 */
    public String exec(String command) {
        StringBuilder result = new StringBuilder();
        cleanExec();
        try {
            OutputStream os = simpProc.getOutputStream();
            os.write((command + "\n").getBytes());
            os.flush();
            ConsumeStream ces = new ConsumeStream(simpProc.getErrorStream());
            ces.start();
            BufferedReader bis = new BufferedReader(new InputStreamReader(simpProc.getInputStream()));
            String lastLine = "";
            while (!lastLine.equals(Constants.RUNSCRIPT_PROCESS_END)) {
                if (result.length() > _10MEGABYTES) result.substring(_1MEGABYTE);
                lastLine = bis.readLine();
                result.append(lastLine + "\n");
                if (verboseMode) System.out.println(lastLine);
            }
            if (errorStreamOutput.length() == 0 && result.length() == Constants.RUNSCRIPT_PROCESS_END.length()) {
                result.append(NORESPONSE);
            } else if (result.length() == Constants.RUNSCRIPT_PROCESS_END.length()) {
                result.append(ONLYERROR);
            }
            ces.end();
        } catch (IOException e) {
            result.append(PROCESSFAIL);
        }
        return result.toString();
    }

    /**
	 * Cleans up the process in case there are "rogue" streams in it from
	 * improperly finished previous execs.
	 */
    private void cleanExec() {
    }

    /**
	 * Thread responsible for consuming the error stream during the runscript execution. Must be
	 * done to prevent possible lockups.
	 */
    protected class ConsumeStream extends Thread {

        public ConsumeStream(InputStream stream) {
            bes = new BufferedReader(new InputStreamReader(stream));
            errorStreamOutput = new StringBuffer(256);
        }

        private BufferedReader bes = null;

        private boolean done = false;

        public void run() {
            while (!done) {
                try {
                    while (bes.ready()) errorStreamOutput.append(bes.readLine());
                    sleep(500);
                } catch (Exception e) {
                }
            }
        }

        public void end() {
            done = true;
        }
    }

    /**
	 * Returns the error stream obtained on the latest exec.
	 */
    public StringBuffer getErrorOutput() {
        return errorStreamOutput;
    }

    /**
	 * If in verbose mode, will print to the default output all the default output of the scripts being executed.
	 */
    public void setVerboseMode(boolean verboseMode) {
        this.verboseMode = verboseMode;
    }
}
