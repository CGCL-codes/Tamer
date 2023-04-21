package pushboy.protocol;

import org.apache.mina.common.*;
import org.apache.mina.filter.codec.*;

public class SearchJobEncoder extends ProtocolEncoderAdapter {

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        assert message instanceof SearchJob;
        SearchJob model = (SearchJob) message;
    }
}
