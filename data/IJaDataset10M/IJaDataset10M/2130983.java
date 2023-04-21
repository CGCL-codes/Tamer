package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.message.AddMessage;

/**
 * Duplicates the transport
 * 
 * @author b.thek
 */
public class CopyTransportAction extends Action implements IProgramStatus {

    private TableViewer viewer;

    /**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
    public CopyTransportAction(TableViewer viewer) {
        this.viewer = viewer;
        setText("Kopie erstellen");
        setToolTipText("Dupliziert den ausgew�hlten Transport");
    }

    @Override
    public void run() {
        ISelection selection = viewer.getSelection();
        Transport sourceTransport = (Transport) ((IStructuredSelection) selection).getFirstElement();
        Transport newTransport = Transport.createTransport(sourceTransport, NetWrapper.getSession().getUsername());
        AddMessage<Transport> addMessage = new AddMessage<Transport>(newTransport);
        addMessage.asnchronRequest(NetWrapper.getSession());
    }
}
