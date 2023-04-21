package clear.messaging.io.amf;

import java.io.InputStream;
import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf0Input;
import flex.messaging.io.amf.AmfMessageDeserializer;
import flex.messaging.io.amf.AmfTrace;

public class ClearAmfMessageDeserializer extends AmfMessageDeserializer {

    public void initialize(final SerializationContext context, final InputStream in, final AmfTrace trace) {
        amfIn = createInput(context);
        amfIn.setInputStream(in);
        debugTrace = trace;
        isDebug = debugTrace != null;
        amfIn.setDebugTrace(debugTrace);
    }

    protected Amf0Input createInput(final SerializationContext context) {
        return new ClearAmf0Input(context);
    }
}
