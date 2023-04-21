package de.dgrid.wisent.gridftp.gui.actions;

import java.net.URL;
import de.dgrid.wisent.gridftp.Activator;
import de.dgrid.wisent.gridftp.xfer.Transfer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public class ResumeTransferAction extends Action {

    private Transfer xfer;

    public ResumeTransferAction(Transfer xfer) {
        this.xfer = xfer;
        setText("Resume");
        setToolTipText("Resume");
        setEnabled(false);
    }

    public ResumeTransferAction() {
        setText("Resume");
        setToolTipText("Resume");
        setImageDescriptor(getImageDescriptor("/icons/resume.gif"));
        setEnabled(false);
    }

    private ImageDescriptor getImageDescriptor(String path) {
        URL url = getClass().getResource(path);
        return ImageDescriptor.createFromURL(url);
    }

    public void run() {
        int state = xfer.getState();
        if (state == Transfer.STATE_HELD || state == Transfer.STATE_WILLHOLD) {
            Activator.getDefault().getTransferManager().rescheduleTransfer(xfer);
            setEnabled(false);
        }
    }

    public Transfer getXfer() {
        return xfer;
    }

    public void setXfer(Transfer xfer) {
        this.xfer = xfer;
    }
}
