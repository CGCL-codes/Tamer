package fb.rt.vhmi;

import fb.datatype.*;
import fb.rt.*;
import fb.rt.events.*;

/** FUNCTION_BLOCK SEL_SENS
  * @author JHC
  * @version 20071011/JHC  */
public class SEL_SENS extends FBInstance {

    public BOOL VALUE = new BOOL();

    public WSTRING NAME = new WSTRING();

    public EventServer INIT = new EventInput(this);

    public EventServer REQ = new EventInput(this);

    public EventOutput INITO = new EventOutput();

    public EventOutput CNF = new EventOutput();

    public EventServer eiNamed(String s) {
        if (s.equals("INIT")) return INIT;
        if (s.equals("REQ")) return REQ;
        return super.eiNamed(s);
    }

    public EventOutput eoNamed(String s) {
        if (s.equals("INITO")) return INITO;
        if (s.equals("CNF")) return CNF;
        return super.eoNamed(s);
    }

    public ANY ivNamed(String s) throws FBRManagementException {
        if (s.equals("VALUE")) return VALUE;
        return super.ivNamed(s);
    }

    public ANY ovNamed(String s) throws FBRManagementException {
        if (s.equals("NAME")) return NAME;
        return super.ovNamed(s);
    }

    public void connectIV(String ivName, ANY newIV) throws FBRManagementException {
        if (ivName.equals("VALUE")) {
            connect_VALUE((BOOL) newIV);
            return;
        }
        super.connectIV(ivName, newIV);
    }

    public void connect_VALUE(BOOL newIV) throws FBRManagementException {
        VALUE = newIV;
    }

    private static final int index_START = 0;

    private void state_START() {
        eccState = index_START;
    }

    private static final int index_INIT = 1;

    private void state_INIT() {
        eccState = index_INIT;
        alg_INIT();
        INITO.serviceEvent(this);
        state_START();
    }

    private static final int index_REQ = 2;

    private void state_REQ() {
        eccState = index_REQ;
        alg_REQ();
        CNF.serviceEvent(this);
        state_START();
    }

    public SEL_SENS() {
        super();
    }

    public void serviceEvent(EventServer e) {
        if (e == INIT) service_INIT(); else if (e == REQ) service_REQ();
    }

    public void service_INIT() {
        if ((eccState == index_START)) state_INIT();
    }

    public void service_REQ() {
        if ((eccState == index_START)) state_REQ();
    }

    /** ALGORITHM INIT IN ST*/
    public void alg_INIT() {
        NAME.value = "/fb/rt/vhmi/sens0.gif";
    }

    /** ALGORITHM REQ IN ST*/
    public void alg_REQ() {
        if ((VALUE.value == false)) {
            NAME.value = "/fb/rt/vhmi/sens0.gif";
        }
        if ((VALUE.value == true)) {
            NAME.value = "/fb/rt/vhmi/sens1.gif";
        }
    }
}
