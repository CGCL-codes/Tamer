package jcontrol.conect.eibinterf;

/**
 * A listener-class for the busmonitor-service. If the BCU is in
 * busmonitor-mode, an object that implements this interface should
 * be applied to the EIBTransceiver. The <code>indication</code>-method
 * is called when ever a frame is received.
 *
 * @version $Revision: 1.1.1.1 $
 * @author T. F�rster
 *
 * @see EIBTransceiver#addBusmonitorLister
 * @see EIBTransceiver#removeBusmonitorLister
 */
public interface BusmonitorListener {

    /**
     * The busmonitor has received a frame.
     * @param event an busmonitor-event that describes the received frame.
     */
    public void indication(BusmonitorEvent event);

    /**
     * The busmonitor received an acknowledgement. This means, that a device
     * has sent a confirm to a frame that was  received by it.
     * @param event an busmonitor-event that describes the received acknowledge type.
     */
    public void acknowledge(BusmonitorEvent event);
}
