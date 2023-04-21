package fb.rt.visual.model;

import fb.datatype.*;
import fb.rt.*;
import fb.rt.events.*;

/** FUNCTION_BLOCK Copy_data_2_8
  * @author JHC
  * @version 20070703/JHC  */
public class Copy_data_2_8 extends FBInstance {

    public UINT IN1_1 = new UINT();

    public UINT IN1_2 = new UINT();

    public BOOL IN1_3 = new BOOL();

    public BOOL IN1_4 = new BOOL();

    public UINT OUT1_1 = new UINT();

    public UINT OUT1_2 = new UINT();

    public UINT OUT2_1 = new UINT();

    public UINT OUT2_2 = new UINT();

    public BOOL OUT3_1 = new BOOL();

    public BOOL OUT3_2 = new BOOL();

    public BOOL OUT4_1 = new BOOL();

    public BOOL OUT4_2 = new BOOL();

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
        if (s.equals("IN1_1")) return IN1_1;
        if (s.equals("IN1_2")) return IN1_2;
        if (s.equals("IN1_3")) return IN1_3;
        if (s.equals("IN1_4")) return IN1_4;
        return super.ivNamed(s);
    }

    public ANY ovNamed(String s) throws FBRManagementException {
        if (s.equals("OUT1_1")) return OUT1_1;
        if (s.equals("OUT1_2")) return OUT1_2;
        if (s.equals("OUT2_1")) return OUT2_1;
        if (s.equals("OUT2_2")) return OUT2_2;
        if (s.equals("OUT3_1")) return OUT3_1;
        if (s.equals("OUT3_2")) return OUT3_2;
        if (s.equals("OUT4_1")) return OUT4_1;
        if (s.equals("OUT4_2")) return OUT4_2;
        return super.ovNamed(s);
    }

    public void connectIV(String ivName, ANY newIV) throws FBRManagementException {
        if (ivName.equals("IN1_1")) {
            connect_IN1_1((UINT) newIV);
            return;
        }
        if (ivName.equals("IN1_2")) {
            connect_IN1_2((UINT) newIV);
            return;
        }
        if (ivName.equals("IN1_3")) {
            connect_IN1_3((BOOL) newIV);
            return;
        }
        if (ivName.equals("IN1_4")) {
            connect_IN1_4((BOOL) newIV);
            return;
        }
        super.connectIV(ivName, newIV);
    }

    public void connect_IN1_1(UINT newIV) throws FBRManagementException {
        IN1_1 = newIV;
    }

    public void connect_IN1_2(UINT newIV) throws FBRManagementException {
        IN1_2 = newIV;
    }

    public void connect_IN1_3(BOOL newIV) throws FBRManagementException {
        IN1_3 = newIV;
    }

    public void connect_IN1_4(BOOL newIV) throws FBRManagementException {
        IN1_4 = newIV;
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

    public Copy_data_2_8() {
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

    /** ALGORITHM INIT IN Java*/
    public void alg_INIT() {
        OUT1_1.value = IN1_1.value;
        OUT1_2.value = IN1_1.value;
        OUT2_1.value = IN1_2.value;
        OUT2_2.value = IN1_2.value;
        OUT3_1.value = IN1_3.value;
        OUT3_2.value = IN1_3.value;
        OUT4_1.value = IN1_4.value;
        OUT4_2.value = IN1_4.value;
    }

    /** ALGORITHM REQ IN Java*/
    public void alg_REQ() {
        OUT1_1.value = IN1_1.value;
        OUT1_2.value = IN1_1.value;
        OUT2_1.value = IN1_2.value;
        OUT2_2.value = IN1_2.value;
        OUT3_1.value = IN1_3.value;
        OUT3_2.value = IN1_3.value;
        OUT4_1.value = IN1_4.value;
        OUT4_2.value = IN1_4.value;
    }
}
