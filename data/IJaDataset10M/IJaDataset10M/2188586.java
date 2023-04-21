package flex.messaging.io.amfx.double_tag;

import flex.messaging.io.amfx.DeserializationConfirmation;
import flex.messaging.io.amf.ActionMessage;
import flex.messaging.io.amf.MessageBody;
import flex.messaging.io.amf.ASObject;
import flex.messaging.MessageException;

public class Confirm5c extends DeserializationConfirmation {

    private ActionMessage EXPECTED_VALUE;

    public Confirm5c() {
        ActionMessage m = new ActionMessage();
        MessageBody body = new MessageBody();
        m.addBody(body);
        ASObject aso = new ASObject();
        aso.put("prop0", new Double(-7654321.251));
        aso.put("prop1", new Double(-1.2345));
        aso.put("prop2", new Double(5.6E-10));
        aso.put("prop3", new Double(19.756893));
        aso.put("prop4", new Double(7000000.22));
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
