package muse.external.program.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class which delivers Process console to JVM console.
 * This class executes as single Thread.
 *
 * @author Korchak
 */
public class DefaultProcessHandler implements CommandProcessHandler, Runnable {

    /**
	 * Receiver of Module command line.
	 */
    private CommandLineReceiver commandLineReceiver = null;

    /**
	 * Process of execution.
	 */
    private Process process = null;

    /**
	 * Flag to star delivering process console to JVM.
	 */
    private volatile boolean start = false;

    /**
	 * @see java.lang.Runnable#run()
	 */
    public void run() {
        while (!start && process == null) {
        }
        InputStreamReader isr = new InputStreamReader(process.getInputStream());
        BufferedReader consoleReader = new BufferedReader(isr);
        long ch = 0;
        do {
            try {
                ch = consoleReader.read();
                if (ch != -1) getCommandLineReceiver().put((char) ch);
            } catch (IOException e) {
                e.printStackTrace();
                ch = -1;
            }
        } while (ch != -1);
    }

    /**
	 * @see muse.external.console.DefaultCommandLineModuleBridge.CommandProcessHandler#processStarted(java.lang.Process)
	 */
    public void processStarted(Process process) {
        this.process = process;
        start = true;
    }

    /**
	 * @see muse.external.console.DefaultCommandLineModuleBridge.CommandProcessHandler#setCommandLineReceiver(muse.external.program.DefaultCommandLineModuleBridge.CommandLineReceiver)
	 */
    public void setCommandLineReceiver(CommandLineReceiver commandLineReceiver) {
        this.commandLineReceiver = commandLineReceiver;
    }

    /**
	 * @see muse.external.console.DefaultCommandLineModuleBridge.CommandProcessHandler#getCommandLineReceiver()
	 */
    public CommandLineReceiver getCommandLineReceiver() {
        if (commandLineReceiver == null) commandLineReceiver = new DefaultCommandLineReceiver();
        return commandLineReceiver;
    }
}
