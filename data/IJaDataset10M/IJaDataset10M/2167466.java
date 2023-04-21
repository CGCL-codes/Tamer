package jcox.jplc.usb.thread;

import java.util.concurrent.BlockingQueue;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbIrp;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotOpenException;
import javax.usb.UsbPipe;
import jcox.jplc.thread.AbstractQueueProcessor;
import jcox.jplc.thread.UnrecoverableException;
import jcox.jplc.usb.system.USBEndpointAdapter;
import jcox.jplc.util.HexUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * 
 * Thread that reads the bytes from the InPipe and
 * puts the HID Packets onto the HIDQueue.
 * 
 * @author jcox
 *
 */
@Singleton
public class InPipeReaderThread extends AbstractQueueProcessor {

    static final Logger log = Logger.getLogger(InPipeReaderThread.class);

    private final USBEndpointAdapter usbEndpointAdapter;

    private final BlockingQueue<UsbIrp> hidPackets;

    @Inject
    public InPipeReaderThread(USBEndpointAdapter usbEndpointAdapter, @Named("responseHIDPacketQueue") BlockingQueue<UsbIrp> hidPackets) {
        super();
        this.hidPackets = hidPackets;
        this.usbEndpointAdapter = usbEndpointAdapter;
    }

    private void recoverableError(Exception e) {
        log.error("Recoverable Error reading inPipe", e);
    }

    private void unrecoverableError(Exception e) {
        throw new UnrecoverableException("Unrecoverable Error reading inPipe", e);
    }

    @Override
    protected void log(Level level, String message) {
        log.log(level, message);
    }

    @Override
    protected void processUntilInterrupted() throws InterruptedException {
        byte[] buffer = new byte[8];
        UsbPipe inPipe = usbEndpointAdapter.getInPipe();
        UsbIrp irp = inPipe.createUsbIrp();
        irp.setData(buffer);
        irp.setAcceptShortPacket(true);
        irp.setUsbException(null);
        irp.setComplete(false);
        try {
            inPipe.asyncSubmit(irp);
            irp.waitUntilComplete();
            log.debug("irp.getData()" + HexUtils.unsignedBytesToHex(irp.getData()));
            hidPackets.put(irp);
        } catch (UsbException ex) {
            recoverableError(ex);
        } catch (UsbNotActiveException ex) {
            unrecoverableError(ex);
        } catch (UsbNotOpenException ex) {
            unrecoverableError(ex);
        } catch (IllegalArgumentException ex) {
            recoverableError(ex);
        } catch (UsbDisconnectedException ex) {
            unrecoverableError(ex);
        }
    }
}
