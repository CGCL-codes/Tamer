package eu.isas.searchgui.processbuilders;

import eu.isas.searchgui.gui.WaitingDialog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A simple ancestor class to reduce code duplication in formatdb and omssacl
 * process builders.
 *
 * @author Lennart Martens
 * @author Marc Vaudel
 */
public abstract class SearchGUIProcessBuilder {

    /**
     * The process to be executed as array
     */
    ArrayList process_name_array = new ArrayList();

    /**
     * The process builder
     */
    ProcessBuilder pb;

    /**
     * The process
     */
    Process p;

    /**
     * The Waiting dialog to display the feedback
     */
    protected WaitingDialog waitingDialog;

    /**
     * Trivial constructor.
     */
    public SearchGUIProcessBuilder() {
    }

    /**
     * Starts the process of a process builder, gets the inputstream from the
     * process and shows it in a JTextArea. Does not close until the process
     * is completed.
     */
    public void startProcess() {
        p = null;
        try {
            p = pb.start();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
        }
        Scanner scan = new Scanner(p.getInputStream());
        String temp;
        while (scan.hasNext()) {
            temp = scan.next();
            if (temp.startsWith("writing")) {
                System.out.print("writing results ..... ");
                waitingDialog.appendReport("writing results ..... ");
                while (scan.hasNext() && !temp.equals("done.")) {
                    temp = scan.next();
                }
                System.out.print("done.\n");
                waitingDialog.appendReport("done.\n");
            } else {
                if (temp.endsWith("done.") || temp.endsWith("loaded.") || temp.endsWith("started.") || temp.endsWith("loaded.")) {
                    temp += "\n";
                } else {
                    temp += " ";
                }
            }
            System.out.print(temp);
            waitingDialog.appendReport(temp);
        }
        waitingDialog.appendReport("\n" + getType() + " Finished.\n\n");
        System.out.print("\n" + getType() + " Finished.\n\n");
        scan.close();
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            if (p != null) {
                p.destroy();
            }
        }
    }

    /**
     * Ends the process.
     */
    public void endProcess() {
        if (p != null) {
            p.destroy();
        }
    }

    /**
     * Returns the type of the process.
     *
     * @return the type of the process
     */
    public abstract String getType();

    /**
     * Returns the file name of the currently processed file.
     *
     * @return the file name of the currently processed file
     */
    public abstract String getCurrentlyProcessedFileName();
}
