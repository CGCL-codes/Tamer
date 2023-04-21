package up5.mi.visio.B2B;

import java.io.IOException;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletMessage;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.TimerService;
import javax.servlet.sip.TooManyHopsException;
import org.apache.log4j.Logger;
import up5.mi.visio.B2B.sipState.InitialState;

public class CallState {

    public static final String CALL_STATE = "CALL_STATE";

    public static final String SIP_FACTORY = "SIP_FACTORY";

    public static final String ORIGINAL_SESSION = "ORIGINAL_SESSION";

    public static final String OTHER_LEG_SESSION = "OTHER_LEG_SESSION";

    public static final String IDENTIFIER_BEAN = "IDENTIFIER_BEAN";

    public static final String B2B_CONTROL = "B2B_CONTROL";

    public final Logger logger = Logger.getLogger(CallState.class);

    protected SipApplicationSession appSession = null;

    /**
	 * 
	 * @param appSession
	 */
    public void setApplicationSession(SipApplicationSession appSession) {
        this.appSession = appSession;
    }

    /**
	 * retrieve the state for a sip message.
	 * if no state exists, create a initial state.
	 * 
	 * @param message the received sip message.
	 * @return the state.
	 */
    protected SipState getState(SipServletMessage message) {
        Object obj = message.getSession().getAttribute(SipState.STATE);
        if (obj != null) {
            return (SipState) obj;
        } else {
            SipFactory sf = (SipFactory) this.appSession.getAttribute(CallState.SIP_FACTORY);
            return new InitialState(sf);
        }
    }

    /**
	 * Get the other leg State for a SIP message 
	 *
	 * @param message 
	 * @return the other leg State.
	 * @throws IncompatibleStateException no other leg exists.
	 */
    protected SipState getOtherState(SipServletMessage message) throws IllegalStateException {
        Object obj = message.getSession().getAttribute(CallState.OTHER_LEG_SESSION);
        if (obj != null) {
            return (SipState) ((SipSession) obj).getAttribute(SipState.STATE);
        } else {
            throw new IllegalStateException();
        }
    }

    /**
	 * 
	 * @param newState
	 * @return
	 */
    protected CallState setNewCallState(CallState newState) {
        newState.setApplicationSession(this.appSession);
        this.appSession.setAttribute(CallState.CALL_STATE, newState);
        this.logger.debug("Changing call state to " + newState.getClass());
        return newState;
    }

    /**
	 * 
	 * @param callBean
	 */
    public void setCallBean(CallIdentifierBean callBean) {
        this.appSession.setAttribute(CallState.IDENTIFIER_BEAN, callBean);
    }

    /**
	 * 
	 * @return
	 */
    public CallIdentifierBean getCallBean() {
        return (CallIdentifierBean) this.appSession.getAttribute(CallState.IDENTIFIER_BEAN);
    }

    /**
	 * 
	 * @param control
	 */
    public void setB2BControl(B2BControl control) {
        this.appSession.setAttribute(CallState.B2B_CONTROL, control);
    }

    /**
	 * 
	 * @return
	 */
    public B2BControl getB2BControl() {
        return (B2BControl) this.appSession.getAttribute(CallState.B2B_CONTROL);
    }

    public CallState doInvite(SipServletRequest invite, B2BAgent b2bAgent, SipFactory sf) throws IOException, IllegalStateException, TooManyHopsException {
        throw new IllegalStateException();
    }

    public CallState doProvisionalResponse(SipServletResponse response) throws IOException, IllegalStateException {
        throw new IllegalStateException();
    }

    public CallState doSuccessResponse(SipServletResponse ok, B2BAgent b2bAgent) throws IOException, IllegalStateException {
        throw new IllegalStateException();
    }

    public CallState doAck(SipServletRequest ack, B2BAgent b2bAgent, TimerService ts) throws IOException, IllegalStateException {
        throw new IllegalStateException();
    }

    public CallState doBye(SipServletRequest bye, B2BAgent b2bAgent) throws IOException, IllegalStateException {
        throw new IllegalStateException();
    }

    public CallState closeCall(B2BAgent b2bAgent) throws IllegalStateException, IOException {
        throw new IllegalStateException();
    }

    public CallState doErrorResponse(SipServletResponse response, B2BAgent b2bAgent) throws IllegalStateException, IOException {
        throw new IllegalStateException();
    }

    public CallState doCancel(SipServletRequest cancel, B2BAgent b2bAgent) throws IllegalStateException, IOException {
        throw new IllegalStateException();
    }

    public CallState callFirstParty() {
        throw new IllegalStateException();
    }

    public CallState callSecondParty() {
        throw new IllegalStateException();
    }
}
