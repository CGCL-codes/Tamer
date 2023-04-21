package up5.mi.visio.B2B.callState;

import java.io.IOException;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.TimerService;
import up5.mi.visio.B2B.B2BAgent;
import up5.mi.visio.B2B.CallState;

public class ThirdPartyCallAccepted extends CallState {

    @Override
    public CallState doAck(SipServletRequest ack, B2BAgent agent, TimerService ts) throws IOException, IllegalStateException {
        return this.setNewCallState(new CallConfirmed());
    }
}
