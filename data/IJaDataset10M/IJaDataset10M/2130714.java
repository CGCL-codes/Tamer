package flex.messaging.io.amfx.traits_tag;

import flex.messaging.io.amfx.DeserializationConfirmation;
import flex.messaging.io.amf.ActionMessage;
import flex.messaging.io.amf.MessageBody;
import flex.messaging.io.amf.ASObject;
import flex.messaging.MessageException;

public class Confirm12a extends DeserializationConfirmation {

    private ActionMessage EXPECTED_VALUE;

    public Confirm12a() {
        ActionMessage m = new ActionMessage();
        MessageBody body = new MessageBody();
        m.addBody(body);
        ASObject aso = new ASObject();
        aso.put("prop0", Boolean.TRUE);
        aso.put("prop1", Boolean.FALSE);
        aso.put("prop2", Boolean.TRUE);
        aso.put("prop3", Boolean.FALSE);
        body.setData(aso);
        EXPECTED_VALUE = m;
    }

    public ActionMessage getExpectedMessage() {
        return EXPECTED_VALUE;
    }

    public MessageException getExpectedException() {
        return null;
    }
}
