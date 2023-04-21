package net.sf.mxlosgi.filetransfer.impl;

import net.sf.mxlosgi.filetransfer.FileTransferRequest;
import net.sf.mxlosgi.filetransfer.listener.FileTransferListener;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class FileTransferListenerServiceTracker extends ServiceTracker {

    public FileTransferListenerServiceTracker(BundleContext context) {
        super(context, FileTransferListener.class.getName(), null);
    }

    public void fireFileTransferRequest(FileTransferRequest request) {
        Object[] services = getServices();
        if (services == null) {
            return;
        }
        for (Object obj : services) {
            FileTransferListener listener = (FileTransferListener) obj;
            listener.fileTransferRequest(request);
        }
    }
}
