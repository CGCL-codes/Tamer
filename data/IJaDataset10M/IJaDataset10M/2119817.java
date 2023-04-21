package be.ac.fundp.infonet.econf.daemon;

import java.util.*;
import java.io.*;
import java.net.*;
import be.ac.fundp.infonet.econf.*;
import be.ac.fundp.infonet.econf.util.*;
import be.ac.fundp.infonet.econf.resource.*;
import be.ac.fundp.infonet.econf.producer.*;
import be.ac.fundp.infonet.econf.audio.*;
import be.ac.fundp.infonet.econf.history.*;

/**
* A console daemon for eConf.
* @author Stephane Nicoll - Infonet FUNDP
* @version 0.1
*/
public class ConsoleDaemon implements Runnable, WorkStatusListener {

    /**
     * Logging object.
     */
    private static org.apache.log4j.Category m_logCat = org.apache.log4j.Category.getInstance(ConsoleDaemon.class.getName());

    private BufferedReader in = null;

    private PrintStream out = null;

    private SessionManager manager = null;

    private Vector roots = null;

    private File audioFile = null;

    /**
    * Creates a new ConsoleDaemon.
    */
    public ConsoleDaemon() throws ConfigurationException {
        in = new BufferedReader(new InputStreamReader(System.in));
        out = System.out;
        manager = SessionManager.getInstance();
        manager.setup(Context.getInstance().getProperty(SessionManager.SESSION_NAME));
        WorkStatus.addWorkStatusListener(this);
    }

    /**
    * Run method.
    */
    public void run() {
        try {
            String line = new String();
            StringTokenizer st = null;
            out.println("eConf daemon has started");
            printMenu();
            out.print("> ");
            while (!(line = in.readLine()).equalsIgnoreCase("-x")) {
                if (line.equalsIgnoreCase("-start")) {
                    start();
                } else if (line.equalsIgnoreCase("-stop")) {
                    stop();
                } else if (line.equalsIgnoreCase("-pause")) {
                    pause();
                } else if (line.equalsIgnoreCase("-restart")) {
                    restart();
                } else if (line.equalsIgnoreCase("-stats")) {
                    showStats();
                } else if ((line.equalsIgnoreCase("-?")) || (line.equalsIgnoreCase("-h")) || (line.equalsIgnoreCase("-help"))) {
                    printMenu();
                } else {
                    out.println("Command not supported");
                }
                out.print("> ");
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        exit();
    }

    /**
    * Prints the console menu.
    */
    private void printMenu() {
        out.println("syntax: -option param");
        out.println("where option include:");
        out.println("-start              : start this session.");
        out.println("-stop               : stop this session.");
        out.println("-pause              : pause this session.");
        out.println("-restart            : restart this session.");
        out.println("-stats              : show the statistics of this session (debug).");
        out.println("-x                  : exit eConf and produce content if requested.");
        out.println("-?                  : print this menu");
    }

    /**
	 * Called when -stop is requested
	 */
    private void stop() {
        manager.stop();
        roots = manager.getRoot();
        audioFile = manager.getAudioFile();
    }

    /**
	 * Called when -start is requested
	 */
    private void start() {
        manager.start();
    }

    /**
	 * Called when -pause is requested
	 */
    private void pause() {
        manager.pause();
    }

    /**
	 * Called when -restart is requested
	 */
    private void restart() {
        manager.restart();
    }

    /**
	 * Called when -x is requested
	 */
    private void exit() {
        if (manager.getMode() == SessionManager.RECORD) stop();
        production();
        MainDaemon.exit();
    }

    /**
    * Start production
    */
    private void production() {
        if (roots != null) {
            ContentManager cm = new ContentManager(roots, audioFile);
            ArrayList v = MainDaemon.getSessions();
            if (v != null) {
                for (int i = 0; i < v.size(); i++) {
                    SessionConfig sc = (SessionConfig) v.get(i);
                    cm.produceSession(sc);
                }
            } else out.println("No sessions defined");
        } else out.println("Session is empty");
    }

    /**
	 * Called when -status is requested
	 */
    private void showStats() {
        out.println(manager.getStatistics());
    }

    /**
    * Invoked when a new Step is engaged.
    */
    public void startStep(WorkStatusEvent e) {
        out.println("Step " + e.getStepID() + ": " + e.getMessage());
    }

    /**
    * Invoked when the production has finished a step.
    */
    public void progression(WorkStatusEvent e) {
        out.println("\t" + e.getMessage());
    }

    /**
    * Invoked when the current step finishes.
    */
    public void endStep(WorkStatusEvent e) {
        String msg = "------------- ended - ";
        if (e.getStatus() == WorkStatusEvent.STEP_OK) msg += "OK"; else if (e.getStatus() == WorkStatusEvent.STEP_FAILED) msg += "FAILED!";
        out.println(msg);
    }
}
