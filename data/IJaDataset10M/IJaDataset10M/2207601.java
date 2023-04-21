package com.izforge.izpack.panels;

import com.izforge.izpack.installer.*;
import com.izforge.izpack.util.AbstractUIProcessHandler;
import com.izforge.izpack.adaptator.IXMLElement;
import java.io.IOException;

/**
 * Functions to support automated usage of the CompilePanel
 *
 * @author Jonathan Halliday
 * @author Tino Schwarze
 */
public class ProcessPanelAutomationHelper extends PanelAutomationHelper implements PanelAutomation, AbstractUIProcessHandler {

    private int noOfJobs = 0;

    private int currentJob = 0;

    /**
     * Save data for running automated.
     *
     * @param installData installation parameters
     * @param panelRoot   unused.
     */
    public void makeXMLData(AutomatedInstallData installData, IXMLElement panelRoot) {
    }

    /**
     * Perform the installation actions.
     *
     * @param panelRoot The panel XML tree root.
     */
    public void runAutomated(AutomatedInstallData idata, IXMLElement panelRoot) throws InstallerException {
        try {
            ProcessPanelWorker worker = new ProcessPanelWorker(idata, this);
            worker.run();
            if (!worker.getResult()) {
                throw new InstallerException("The work done by the ProcessPanel (line " + panelRoot.getLineNr() + ") failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new InstallerException("The work done by the ProcessPanel (line " + panelRoot.getLineNr() + ") failed", e);
        }
    }

    public void logOutput(String message, boolean stderr) {
        if (stderr) {
            System.err.println(message);
        } else {
            System.out.println(message);
        }
    }

    /**
     * Reports progress on System.out
     *
     * @see com.izforge.izpack.util.AbstractUIProcessHandler#startProcessing(int)
     */
    public void startProcessing(int noOfJobs) {
        System.out.println("[ Starting processing ]");
        this.noOfJobs = noOfJobs;
    }

    /**
     * @see com.izforge.izpack.util.AbstractUIProcessHandler#finishProcessing
     */
    public void finishProcessing(boolean unlockPrev, boolean unlockNext) {
        System.out.println("[ Processing finished ]");
    }

    /**
     *
     */
    public void startProcess(String name) {
        this.currentJob++;
        System.out.println("Starting process " + name + " (" + Integer.toString(this.currentJob) + "/" + Integer.toString(this.noOfJobs) + ")");
    }

    public void finishProcess() {
    }
}
