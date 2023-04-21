package org.epoline.print.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import org.epoline.bsi.access.BPSPrintException;
import org.epoline.print.server.PrintJobManager;
import org.epoline.print.server.PrintManagementController;
import org.epoline.print.shared.BPSJob;
import org.epoline.print.shared.LabelSheetJob;
import org.epoline.print.shared.PrintException;

public class PrintInterfaceManager extends UnicastRemoteObject implements PrintInterface {

    /**
	 * PrintInterfaceManager constructor comment.
	 * @exception java.rmi.RemoteException The exception description.
	 */
    public PrintInterfaceManager() throws RemoteException {
        super();
    }

    /**
	 * print method comment.
	 */
    public void print(BPSJob aJob) throws RemoteException, PrintException {
        PrintJobManager pjM = new PrintJobManager();
        try {
            pjM.print(aJob);
            PrintManagementController.getInstance().incNumProcessedPrintJobs();
        } catch (BPSPrintException e) {
            PrintManagementController.getInstance().incNumFailedPrintJobs();
            throw new PrintException("Error processing PrintJob: " + e.getMessage());
        }
    }

    public String printLabelSheet(LabelSheetJob aJob) throws RemoteException, PrintException {
        PrintJobManager pjM = new PrintJobManager();
        try {
            String result = pjM.printLabelSheet(aJob);
            PrintManagementController.getInstance().incNumProcessedPrintJobs();
            return result;
        } catch (BPSPrintException e) {
            PrintManagementController.getInstance().incNumFailedPrintJobs();
            throw new PrintException("Error processing PrintJob: " + e.getMessage());
        }
    }

    public String getLabelSheetPrintOutput(BPSJob aJob, int format) throws RemoteException, PrintException {
        return null;
    }
}
