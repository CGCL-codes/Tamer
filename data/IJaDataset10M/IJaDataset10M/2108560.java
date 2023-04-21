package net.sourceforge.jwakeup;

import junit.framework.TestCase;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class WakeUpUtilTest extends TestCase {

    private static final String NIC = "01:02:03:04:05:06";

    private static final byte[] WAKEUP_FRAME = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06 };

    private class Lock {

        private boolean locked = false;

        public boolean isLocked() {
            return locked;
        }

        public void setLocked(boolean b) {
            this.locked = b;
        }
    }

    public void testCreateWakeupFrame() {
        EthernetAddress nic = new EthernetAddress(NIC);
        byte[] wakeupFrame = WakeUpUtil.createWakeupFrame(nic);
        assertNotNull(wakeupFrame);
        assertTrue(Arrays.equals(wakeupFrame, WAKEUP_FRAME));
    }

    public void testWakeupEthernetAddressArrayInetAddressint() throws IOException, InterruptedException {
        int port = 2710;
        final ServerSocket serverSocket = new ServerSocket(port);
        final Lock lock = new Lock();
        serverSocket.setSoTimeout(5000);
        new Thread(new Runnable() {

            public void run() {
                try {
                    Socket socket = serverSocket.accept();
                    InputStream in = socket.getInputStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    for (int b = in.read(); -1 != b; b = in.read()) {
                        out.write(b);
                    }
                    in.close();
                    byte[] wakeupFrame = out.toByteArray();
                    out.close();
                    assertTrue("Wakeup frames must equal", Arrays.equals(wakeupFrame, WAKEUP_FRAME));
                } catch (SocketTimeoutException e) {
                    fail("Timeout. No wakeup frame recieved");
                } catch (IOException e) {
                    fail("IO excepmvn tion");
                } catch (Throwable t) {
                    fail(t.toString());
                } finally {
                    lock.setLocked(false);
                }
            }
        }).start();
        Thread.sleep(2000);
        WakeUpUtil.wakeup(new EthernetAddress(NIC), port);
        for (; lock.isLocked(); ) {
        }
    }
}
