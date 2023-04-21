package vavi.net.xmpp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import vavi.util.event.GenericEvent;
import vavi.util.event.GenericListener;
import vavi.util.event.GenericSupport;

/**
 * Pinger.
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 040117 nsano initial version <br>
 *          0.10 040122 nsano be generic <br>
 */
class Pinger {

    /** */
    private OutputStream os;

    /** */
    public Pinger(Socket socket) throws IOException {
        this.os = socket.getOutputStream();
    }

    /** */
    private int interval = 5000;

    /** */
    private Thread thread;

    /** */
    public void start() {
        thread = new Thread(new Runnable() {

            /** */
            public void run() {
                while (true) {
                    try {
                        try {
                            Thread.sleep(interval);
                        } catch (Exception e) {
                        }
                        os.write('\n');
                    } catch (IOException e) {
                        fireEvent(this, "connectionLost");
                    }
                }
            }
        });
    }

    /** */
    private GenericSupport gs = new GenericSupport();

    /** GenericListener ��ǉ����܂��D */
    public void addGenericListener(GenericListener l) {
        gs.addGenericListener(l);
    }

    /** GenericListener ���폜���܂��D */
    public void removeGenericListener(GenericListener l) {
        gs.removeGenericListener(l);
    }

    /** �ėp�C�x���g�𔭍s���܂��D */
    private void fireEvent(Object source, String name) {
        gs.fireEventHappened(new GenericEvent(source, name));
    }
}
