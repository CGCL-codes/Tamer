package clientrequest;

import java.util.Vector;

/**
 *
 * @author
 */
public class RequestChatPublic extends RequestToServer {

    @Override
    public void send(short requestCode, Vector args) {
        short sizeofrequest = (short) (4);
        super.writeShort(sizeofrequest);
        super.writeShort(requestCode);
    }
}
