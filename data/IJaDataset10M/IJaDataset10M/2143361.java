package vavi.net.im.protocol.ymsg.command;

import java.io.IOException;
import vavi.net.im.event.IMEvent;
import vavi.net.im.protocol.ymsg.YmsgEventName;
import vavi.net.im.protocol.ymsg.YmsgPacket;
import vavi.util.Debug;

/**
 * �F�؂̃��X�|���X�Ή�.
 *
 * @event 87
 * 
 * @see YmsgEventName#responseAuth
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 041010 nsano initial version <br>
 */
public class AuthCommand extends Command {

    /** */
    public void exec(YmsgPacket yp) throws IOException {
        String seed = yp.getDataValueByKey(94);
        Debug.println("seed: " + seed);
        String sn = yp.getDataValueByKey(1);
        Debug.println("sn: " + sn);
        int m = yp.getDataValueByKey(13) == null || Integer.parseInt(yp.getDataValueByKey(13)) == 0 ? 0 : 1;
        Debug.println("m: " + m + ", (" + yp.getDataValueByKey(13) + ")");
        listeners.eventHappened(new IMEvent(this, YmsgEventName.responseAuth, seed, sn, m));
    }
}
