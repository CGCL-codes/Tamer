package flex.messaging.io.amfx.null_tag;

import flex.messaging.io.amfx.DeserializationConfirmation;
import flex.messaging.io.amf.ActionMessage;
import flex.messaging.io.amf.MessageBody;
import flex.messaging.MessageException;

public class Confirm9a extends DeserializationConfirmation {

    private ActionMessage EXPECTED_VALUE;

    public Confirm9a() {
        ActionMessage m = new ActionMessage();
        MessageBody body = new MessageBody();
        m.addBody(body);
        body.setData(null);
        EXPECTED_VALUE = m;
    }

    public ActionMessage getExpectedMessage() {
        return EXPECTED_VALUE;
    }

    public MessageException getExpectedException() {
        return null;
    }
}
