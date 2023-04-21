package flex.messaging.io.amfx.string_tag;

import flex.messaging.io.amfx.DeserializationConfirmation;
import flex.messaging.io.amf.ActionMessage;
import flex.messaging.MessageException;

public class Confirm11h extends DeserializationConfirmation {

    public Confirm11h() {
    }

    public boolean isNegativeTest() {
        return true;
    }

    public ActionMessage getExpectedMessage() {
        return null;
    }

    public MessageException getExpectedException() {
        return new MessageException("Unknown string reference: 5");
    }
}
