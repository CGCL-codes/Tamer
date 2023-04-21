package jcox.jplc.usb.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbIrp;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotOpenException;
import javax.usb.UsbPipe;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import jcox.jplc.thread.AbstractQueueProcessor;
import jcox.jplc.thread.UnrecoverableException;
import jcox.jplc.usb.ClearToSendToken;
import jcox.jplc.usb.system.USBEndpointAdapter;
import jcox.jplc.util.HexUtils;

@Singleton
public class OutPipeWriterThread extends AbstractQueueProcessor {

    private final USBEndpointAdapter usbEndpointAdapter;

    private final BlockingQueue<byte[]> hidPackets;

    private final BlockingQueue<ClearToSendToken> clearToSendQueue;

    private final long numberOfMilliSecondsToWaitForClearToSendToken;

    private final int numberOfRetryAttemptsToAcquireClearToSendToken;

    ;

    static final byte[] clearTheLinePacket = new byte[] { 0x02, 0x02, 0x48, 0x00, 0x00, 0x00, 0x00, 0x00 };

    private boolean clearToSend = false;

    static final Logger log = Logger.getLogger(OutPipeWriterThread.class);

    @Inject
    public OutPipeWriterThread(USBEndpointAdapter usbEndpointAdapter, @Named("requestHIDPacketQueue") BlockingQueue<byte[]> hidPackets, @Named("clearToSendQueue") BlockingQueue<ClearToSendToken> clearToSendQueue, @Named("numberOfMilliSecondsToWaitForClearToSendToken") long numberOfMilliSecondsToWaitForClearToSendToken, @Named("numberOfRetryAttemptsToAcquireClearToSendToken") int numberOfRetryAttemptsToAcquireClearToSendToken) {
        super();
        this.hidPackets = hidPackets;
        this.clearToSendQueue = clearToSendQueue;
        this.usbEndpointAdapter = usbEndpointAdapter;
        this.numberOfMilliSecondsToWaitForClearToSendToken = numberOfMilliSecondsToWaitForClearToSendToken;
        this.numberOfRetryAttemptsToAcquireClearToSendToken = numberOfRetryAttemptsToAcquireClearToSendToken;
    }

    private void getClearToSendToken() throws InterruptedException {
        log.debug("waiting for the clear to send token");
        for (int j = 0; j < numberOfRetryAttemptsToAcquireClearToSendToken; j++) {
            ClearToSendToken token = clearToSendQueue.poll(numberOfMilliSecondsToWaitForClearToSendToken, TimeUnit.MILLISECONDS);
            if (token != null) {
                log.debug("Clear to Send token acquired:" + token);
                clearToSend = true;
                break;
            } else {
                log.debug("Waited for: " + numberOfMilliSecondsToWaitForClearToSendToken + " millis, but the clear to send token was not acquired. Attempt: " + (j + 1));
                sendUsbIrp(clearTheLinePacket);
            }
        }
        if (!clearToSend) {
            throw new UnrecoverableException("Could not acquire the clear to send token after " + numberOfRetryAttemptsToAcquireClearToSendToken + " attempts.");
        }
    }

    @Override
    protected void processUntilInterrupted() throws InterruptedException {
        if (!clearToSend) {
            getClearToSendToken();
        }
        sendNextHIDPacket();
    }

    private void sendNextHIDPacket() throws InterruptedException {
        byte[] dataOut = hidPackets.take();
        sendUsbIrp(dataOut);
        clearToSend = false;
    }

    private void sendUsbIrp(byte[] dataOut) {
        try {
            log.debug("Sending data:" + HexUtils.unsignedBytesToHex(dataOut));
            UsbPipe outPipe = usbEndpointAdapter.getOutPipe();
            UsbIrp usbIrp = outPipe.createUsbIrp();
            usbIrp.setComplete(false);
            usbIrp.setData(dataOut);
            usbIrp.setUsbException(null);
            outPipe.asyncSubmit(usbIrp);
        } catch (UsbNotActiveException e) {
            unrecoverableError(e);
        } catch (UsbNotOpenException e) {
            unrecoverableError(e);
        } catch (IllegalArgumentException e) {
            recoverableError(e);
        } catch (UsbDisconnectedException e) {
            unrecoverableError(e);
        } catch (UsbException e) {
            recoverableError(e);
        }
    }

    private void recoverableError(Exception e) {
        log.error("Recoverable Error sending data on outPipe", e);
    }

    private void unrecoverableError(Exception e) {
        throw new UnrecoverableException("Unrecoverable error sending data on outPipe", e);
    }

    @Override
    protected void log(Level level, String message) {
        log.log(level, message);
    }
}
