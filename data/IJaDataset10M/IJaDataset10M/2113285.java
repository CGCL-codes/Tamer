package fb.rt.visual.model;

import fb.datatype.*;
import fb.rt.*;
import fb.rt.events.*;

/** FUNCTION_BLOCK testtest
  * @author JHC
  * @version 20070422/JHC  */
public class testtest extends FBInstance {

    public BOOL Enable = new BOOL();

    public UINT Init_Object_X = new UINT();

    public UINT Init_Object_Y = new UINT();

    public BOOL Move_Right = new BOOL();

    public BOOL Move_Left = new BOOL();

    public BOOL Move_Up = new BOOL();

    public BOOL Move_Down = new BOOL();

    public UINT Movement_Rate = new UINT();

    public BOOL Out_Enable = new BOOL();

    public UINT Out_Object_X = new UINT();

    public UINT Out_Object_Y = new UINT();

    public UINT Object_X_C = new UINT();

    public UINT Object_Y_C = new UINT();

    public UINT Temp_X = new UINT();

    public UINT Temp_Y = new UINT();

    public UINT internal_rate = new UINT();

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
        if (s.equals("Enable")) return Enable;
        if (s.equals("Init_Object_X")) return Init_Object_X;
        if (s.equals("Init_Object_Y")) return Init_Object_Y;
        if (s.equals("Move_Right")) return Move_Right;
        if (s.equals("Move_Left")) return Move_Left;
        if (s.equals("Move_Up")) return Move_Up;
        if (s.equals("Move_Down")) return Move_Down;
        if (s.equals("Movement_Rate")) return Movement_Rate;
        return super.ivNamed(s);
    }

    public ANY ovNamed(String s) throws FBRManagementException {
        if (s.equals("Out_Enable")) return Out_Enable;
        if (s.equals("Out_Object_X")) return Out_Object_X;
        if (s.equals("Out_Object_Y")) return Out_Object_Y;
        if (s.equals("Object_X_C")) return Object_X_C;
        if (s.equals("Object_Y_C")) return Object_Y_C;
        return super.ovNamed(s);
    }

    public void connectIV(String ivName, ANY newIV) throws FBRManagementException {
        if (ivName.equals("Enable")) {
            connect_Enable((BOOL) newIV);
            return;
        }
        if (ivName.equals("Init_Object_X")) {
            connect_Init_Object_X((UINT) newIV);
            return;
        }
        if (ivName.equals("Init_Object_Y")) {
            connect_Init_Object_Y((UINT) newIV);
            return;
        }
        if (ivName.equals("Move_Right")) {
            connect_Move_Right((BOOL) newIV);
            return;
        }
        if (ivName.equals("Move_Left")) {
            connect_Move_Left((BOOL) newIV);
            return;
        }
        if (ivName.equals("Move_Up")) {
            connect_Move_Up((BOOL) newIV);
            return;
        }
        if (ivName.equals("Move_Down")) {
            connect_Move_Down((BOOL) newIV);
            return;
        }
        if (ivName.equals("Movement_Rate")) {
            connect_Movement_Rate((UINT) newIV);
            return;
        }
        super.connectIV(ivName, newIV);
    }

    public void connect_Enable(BOOL newIV) throws FBRManagementException {
        Enable = newIV;
    }

    public void connect_Init_Object_X(UINT newIV) throws FBRManagementException {
        Init_Object_X = newIV;
    }

    public void connect_Init_Object_Y(UINT newIV) throws FBRManagementException {
        Init_Object_Y = newIV;
    }

    public void connect_Move_Right(BOOL newIV) throws FBRManagementException {
        Move_Right = newIV;
    }

    public void connect_Move_Left(BOOL newIV) throws FBRManagementException {
        Move_Left = newIV;
    }

    public void connect_Move_Up(BOOL newIV) throws FBRManagementException {
        Move_Up = newIV;
    }

    public void connect_Move_Down(BOOL newIV) throws FBRManagementException {
        Move_Down = newIV;
    }

    public void connect_Movement_Rate(UINT newIV) throws FBRManagementException {
        Movement_Rate = newIV;
    }

    private static final int index_START = 0;

    private void state_START() {
        eccState = index_START;
    }

    private static final int index_INIT_Object = 1;

    private void state_INIT_Object() {
        eccState = index_INIT_Object;
        alg_Initialisation_POS();
        INITO.serviceEvent(this);
        state_START();
    }

    public testtest() {
        super();
    }

    public void serviceEvent(EventServer e) {
        if (e == INIT) service_INIT(); else if (e == REQ) service_REQ();
    }

    public void service_INIT() {
        if ((eccState == index_START)) state_INIT_Object();
    }

    public void service_REQ() {
    }

    /** ALGORITHM Initialisation_POS IN Java*/
    public void alg_Initialisation_POS() {
        if (Enable.value == false) {
            Out_Enable.value = false;
        } else {
            Out_Object_X.value = Init_Object_X.value;
            Out_Object_Y.value = Init_Object_Y.value;
            Out_Enable.value = true;
        }
    }
}
