package fb.rt.visual.model;

import fb.datatype.*;
import fb.rt.*;
import fb.rt.events.*;

/** FUNCTION_BLOCK Initialiser_S2_FV
  * @author JHC
  * @version 20070702/JHC  */
public class Initialiser_S2_FV extends FBInstance {

    public BOOL QI = new BOOL();

    public UINT Main_Stand_XI = new UINT();

    public UINT Main_Stand_YI = new UINT();

    public UINT Main_Arm_XI = new UINT();

    public UINT Main_Arm_YI = new UINT();

    public UINT Piston_XI = new UINT();

    public UINT Piston_YI = new UINT();

    public UINT Shell_XI = new UINT();

    public UINT Shell_YI = new UINT();

    public UINT Pincer1_XI = new UINT();

    public UINT Pincer1_YI = new UINT();

    public UINT Pincer2_XI = new UINT();

    public UINT Pincer2_YI = new UINT();

    public UINT Pallet1_XI = new UINT();

    public UINT Pallet1_YI = new UINT();

    public UINT Pallet2_XI = new UINT();

    public UINT Pallet2_YI = new UINT();

    public UINT Pallet3_XI = new UINT();

    public UINT Pallet3_YI = new UINT();

    public UINT Pallet4_XI = new UINT();

    public UINT Pallet4_YI = new UINT();

    public UINT Pallet5_XI = new UINT();

    public UINT Pallet5_YI = new UINT();

    public UINT Pallet6_XI = new UINT();

    public UINT Pallet6_YI = new UINT();

    public UINT Collector_XI = new UINT();

    public UINT Collector_YI = new UINT();

    public UINT Collector2_XI = new UINT();

    public UINT Collector2_YI = new UINT();

    public UINT Docker_XI = new UINT();

    public UINT Docker_YI = new UINT();

    public UINT Init_Pos_X = new UINT();

    public UINT Init_Pos_Y = new UINT();

    public BOOL QO = new BOOL();

    public UINT Main_Stand_XO = new UINT();

    public UINT Main_Stand_YO = new UINT();

    public UINT Main_Arm_XO = new UINT();

    public UINT Main_Arm_YO = new UINT();

    public UINT Piston_XO = new UINT();

    public UINT Piston_YO = new UINT();

    public UINT Shell_XO = new UINT();

    public UINT Shell_YO = new UINT();

    public UINT Pincer1_XO = new UINT();

    public UINT Pincer1_YO = new UINT();

    public UINT Pincer2_XO = new UINT();

    public UINT Pincer2_YO = new UINT();

    public UINT Pallet1_XO = new UINT();

    public UINT Pallet1_YO = new UINT();

    public UINT Pallet2_XO = new UINT();

    public UINT Pallet2_YO = new UINT();

    public UINT Pallet3_XO = new UINT();

    public UINT Pallet3_YO = new UINT();

    public UINT Pallet4_XO = new UINT();

    public UINT Pallet4_YO = new UINT();

    public UINT Pallet5_XO = new UINT();

    public UINT Pallet5_YO = new UINT();

    public UINT Pallet6_XO = new UINT();

    public UINT Pallet6_YO = new UINT();

    public UINT Collector_XO = new UINT();

    public UINT Collector_YO = new UINT();

    public UINT Collector2_XO = new UINT();

    public UINT Collector2_YO = new UINT();

    public UINT Docker_XO = new UINT();

    public UINT Docker_YO = new UINT();

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
        if (s.equals("QI")) return QI;
        if (s.equals("Main_Stand_XI")) return Main_Stand_XI;
        if (s.equals("Main_Stand_YI")) return Main_Stand_YI;
        if (s.equals("Main_Arm_XI")) return Main_Arm_XI;
        if (s.equals("Main_Arm_YI")) return Main_Arm_YI;
        if (s.equals("Piston_XI")) return Piston_XI;
        if (s.equals("Piston_YI")) return Piston_YI;
        if (s.equals("Shell_XI")) return Shell_XI;
        if (s.equals("Shell_YI")) return Shell_YI;
        if (s.equals("Pincer1_XI")) return Pincer1_XI;
        if (s.equals("Pincer1_YI")) return Pincer1_YI;
        if (s.equals("Pincer2_XI")) return Pincer2_XI;
        if (s.equals("Pincer2_YI")) return Pincer2_YI;
        if (s.equals("Pallet1_XI")) return Pallet1_XI;
        if (s.equals("Pallet1_YI")) return Pallet1_YI;
        if (s.equals("Pallet2_XI")) return Pallet2_XI;
        if (s.equals("Pallet2_YI")) return Pallet2_YI;
        if (s.equals("Pallet3_XI")) return Pallet3_XI;
        if (s.equals("Pallet3_YI")) return Pallet3_YI;
        if (s.equals("Pallet4_XI")) return Pallet4_XI;
        if (s.equals("Pallet4_YI")) return Pallet4_YI;
        if (s.equals("Pallet5_XI")) return Pallet5_XI;
        if (s.equals("Pallet5_YI")) return Pallet5_YI;
        if (s.equals("Pallet6_XI")) return Pallet6_XI;
        if (s.equals("Pallet6_YI")) return Pallet6_YI;
        if (s.equals("Collector_XI")) return Collector_XI;
        if (s.equals("Collector_YI")) return Collector_YI;
        if (s.equals("Collector2_XI")) return Collector2_XI;
        if (s.equals("Collector2_YI")) return Collector2_YI;
        if (s.equals("Docker_XI")) return Docker_XI;
        if (s.equals("Docker_YI")) return Docker_YI;
        if (s.equals("Init_Pos_X")) return Init_Pos_X;
        if (s.equals("Init_Pos_Y")) return Init_Pos_Y;
        return super.ivNamed(s);
    }

    public ANY ovNamed(String s) throws FBRManagementException {
        if (s.equals("QO")) return QO;
        if (s.equals("Main_Stand_XO")) return Main_Stand_XO;
        if (s.equals("Main_Stand_YO")) return Main_Stand_YO;
        if (s.equals("Main_Arm_XO")) return Main_Arm_XO;
        if (s.equals("Main_Arm_YO")) return Main_Arm_YO;
        if (s.equals("Piston_XO")) return Piston_XO;
        if (s.equals("Piston_YO")) return Piston_YO;
        if (s.equals("Shell_XO")) return Shell_XO;
        if (s.equals("Shell_YO")) return Shell_YO;
        if (s.equals("Pincer1_XO")) return Pincer1_XO;
        if (s.equals("Pincer1_YO")) return Pincer1_YO;
        if (s.equals("Pincer2_XO")) return Pincer2_XO;
        if (s.equals("Pincer2_YO")) return Pincer2_YO;
        if (s.equals("Pallet1_XO")) return Pallet1_XO;
        if (s.equals("Pallet1_YO")) return Pallet1_YO;
        if (s.equals("Pallet2_XO")) return Pallet2_XO;
        if (s.equals("Pallet2_YO")) return Pallet2_YO;
        if (s.equals("Pallet3_XO")) return Pallet3_XO;
        if (s.equals("Pallet3_YO")) return Pallet3_YO;
        if (s.equals("Pallet4_XO")) return Pallet4_XO;
        if (s.equals("Pallet4_YO")) return Pallet4_YO;
        if (s.equals("Pallet5_XO")) return Pallet5_XO;
        if (s.equals("Pallet5_YO")) return Pallet5_YO;
        if (s.equals("Pallet6_XO")) return Pallet6_XO;
        if (s.equals("Pallet6_YO")) return Pallet6_YO;
        if (s.equals("Collector_XO")) return Collector_XO;
        if (s.equals("Collector_YO")) return Collector_YO;
        if (s.equals("Collector2_XO")) return Collector2_XO;
        if (s.equals("Collector2_YO")) return Collector2_YO;
        if (s.equals("Docker_XO")) return Docker_XO;
        if (s.equals("Docker_YO")) return Docker_YO;
        return super.ovNamed(s);
    }

    public void connectIV(String ivName, ANY newIV) throws FBRManagementException {
        if (ivName.equals("QI")) {
            connect_QI((BOOL) newIV);
            return;
        }
        if (ivName.equals("Main_Stand_XI")) {
            connect_Main_Stand_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Main_Stand_YI")) {
            connect_Main_Stand_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Main_Arm_XI")) {
            connect_Main_Arm_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Main_Arm_YI")) {
            connect_Main_Arm_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Piston_XI")) {
            connect_Piston_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Piston_YI")) {
            connect_Piston_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Shell_XI")) {
            connect_Shell_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Shell_YI")) {
            connect_Shell_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pincer1_XI")) {
            connect_Pincer1_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pincer1_YI")) {
            connect_Pincer1_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pincer2_XI")) {
            connect_Pincer2_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pincer2_YI")) {
            connect_Pincer2_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pallet1_XI")) {
            connect_Pallet1_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pallet1_YI")) {
            connect_Pallet1_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pallet2_XI")) {
            connect_Pallet2_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pallet2_YI")) {
            connect_Pallet2_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pallet3_XI")) {
            connect_Pallet3_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pallet3_YI")) {
            connect_Pallet3_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pallet4_XI")) {
            connect_Pallet4_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pallet4_YI")) {
            connect_Pallet4_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pallet5_XI")) {
            connect_Pallet5_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pallet5_YI")) {
            connect_Pallet5_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pallet6_XI")) {
            connect_Pallet6_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Pallet6_YI")) {
            connect_Pallet6_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Collector_XI")) {
            connect_Collector_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Collector_YI")) {
            connect_Collector_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Collector2_XI")) {
            connect_Collector2_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Collector2_YI")) {
            connect_Collector2_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Docker_XI")) {
            connect_Docker_XI((UINT) newIV);
            return;
        }
        if (ivName.equals("Docker_YI")) {
            connect_Docker_YI((UINT) newIV);
            return;
        }
        if (ivName.equals("Init_Pos_X")) {
            connect_Init_Pos_X((UINT) newIV);
            return;
        }
        if (ivName.equals("Init_Pos_Y")) {
            connect_Init_Pos_Y((UINT) newIV);
            return;
        }
        super.connectIV(ivName, newIV);
    }

    public void connect_QI(BOOL newIV) throws FBRManagementException {
        QI = newIV;
    }

    public void connect_Main_Stand_XI(UINT newIV) throws FBRManagementException {
        Main_Stand_XI = newIV;
    }

    public void connect_Main_Stand_YI(UINT newIV) throws FBRManagementException {
        Main_Stand_YI = newIV;
    }

    public void connect_Main_Arm_XI(UINT newIV) throws FBRManagementException {
        Main_Arm_XI = newIV;
    }

    public void connect_Main_Arm_YI(UINT newIV) throws FBRManagementException {
        Main_Arm_YI = newIV;
    }

    public void connect_Piston_XI(UINT newIV) throws FBRManagementException {
        Piston_XI = newIV;
    }

    public void connect_Piston_YI(UINT newIV) throws FBRManagementException {
        Piston_YI = newIV;
    }

    public void connect_Shell_XI(UINT newIV) throws FBRManagementException {
        Shell_XI = newIV;
    }

    public void connect_Shell_YI(UINT newIV) throws FBRManagementException {
        Shell_YI = newIV;
    }

    public void connect_Pincer1_XI(UINT newIV) throws FBRManagementException {
        Pincer1_XI = newIV;
    }

    public void connect_Pincer1_YI(UINT newIV) throws FBRManagementException {
        Pincer1_YI = newIV;
    }

    public void connect_Pincer2_XI(UINT newIV) throws FBRManagementException {
        Pincer2_XI = newIV;
    }

    public void connect_Pincer2_YI(UINT newIV) throws FBRManagementException {
        Pincer2_YI = newIV;
    }

    public void connect_Pallet1_XI(UINT newIV) throws FBRManagementException {
        Pallet1_XI = newIV;
    }

    public void connect_Pallet1_YI(UINT newIV) throws FBRManagementException {
        Pallet1_YI = newIV;
    }

    public void connect_Pallet2_XI(UINT newIV) throws FBRManagementException {
        Pallet2_XI = newIV;
    }

    public void connect_Pallet2_YI(UINT newIV) throws FBRManagementException {
        Pallet2_YI = newIV;
    }

    public void connect_Pallet3_XI(UINT newIV) throws FBRManagementException {
        Pallet3_XI = newIV;
    }

    public void connect_Pallet3_YI(UINT newIV) throws FBRManagementException {
        Pallet3_YI = newIV;
    }

    public void connect_Pallet4_XI(UINT newIV) throws FBRManagementException {
        Pallet4_XI = newIV;
    }

    public void connect_Pallet4_YI(UINT newIV) throws FBRManagementException {
        Pallet4_YI = newIV;
    }

    public void connect_Pallet5_XI(UINT newIV) throws FBRManagementException {
        Pallet5_XI = newIV;
    }

    public void connect_Pallet5_YI(UINT newIV) throws FBRManagementException {
        Pallet5_YI = newIV;
    }

    public void connect_Pallet6_XI(UINT newIV) throws FBRManagementException {
        Pallet6_XI = newIV;
    }

    public void connect_Pallet6_YI(UINT newIV) throws FBRManagementException {
        Pallet6_YI = newIV;
    }

    public void connect_Collector_XI(UINT newIV) throws FBRManagementException {
        Collector_XI = newIV;
    }

    public void connect_Collector_YI(UINT newIV) throws FBRManagementException {
        Collector_YI = newIV;
    }

    public void connect_Collector2_XI(UINT newIV) throws FBRManagementException {
        Collector2_XI = newIV;
    }

    public void connect_Collector2_YI(UINT newIV) throws FBRManagementException {
        Collector2_YI = newIV;
    }

    public void connect_Docker_XI(UINT newIV) throws FBRManagementException {
        Docker_XI = newIV;
    }

    public void connect_Docker_YI(UINT newIV) throws FBRManagementException {
        Docker_YI = newIV;
    }

    public void connect_Init_Pos_X(UINT newIV) throws FBRManagementException {
        Init_Pos_X = newIV;
    }

    public void connect_Init_Pos_Y(UINT newIV) throws FBRManagementException {
        Init_Pos_Y = newIV;
    }

    private static final int index_START = 0;

    private void state_START() {
        eccState = index_START;
    }

    private static final int index_Initialisation = 1;

    private void state_Initialisation() {
        eccState = index_Initialisation;
        alg_Initialisation();
        INITO.serviceEvent(this);
        state_START();
    }

    public Initialiser_S2_FV() {
        super();
    }

    public void serviceEvent(EventServer e) {
        if (e == INIT) service_INIT(); else if (e == REQ) service_REQ();
    }

    public void service_INIT() {
        if ((eccState == index_START)) state_Initialisation();
    }

    public void service_REQ() {
    }

    /** ALGORITHM REQ IN ST*/
    public void alg_REQ() {
    }

    /** ALGORITHM Initialisation IN Java*/
    public void alg_Initialisation() {
        Main_Stand_XO.value = Main_Stand_XI.value + Init_Pos_X.value;
        Main_Stand_YO.value = Main_Stand_YI.value + Init_Pos_Y.value;
        Main_Arm_XO.value = Main_Arm_XI.value + Init_Pos_X.value;
        Main_Arm_YO.value = Main_Arm_YI.value + Init_Pos_Y.value;
        Piston_XO.value = Piston_XI.value + Init_Pos_X.value;
        Piston_YO.value = Piston_YI.value + Init_Pos_Y.value;
        Shell_XO.value = Shell_XI.value + Init_Pos_X.value;
        Shell_YO.value = Shell_YI.value + Init_Pos_Y.value;
        Pincer1_XO.value = Pincer1_XI.value + Init_Pos_X.value;
        Pincer1_YO.value = Pincer1_YI.value + Init_Pos_Y.value;
        Pincer2_XO.value = Pincer2_XI.value + Init_Pos_X.value;
        Pincer2_YO.value = Pincer2_YI.value + Init_Pos_Y.value;
        Pallet1_XO.value = Pallet1_XI.value + Init_Pos_X.value;
        Pallet1_YO.value = Pallet1_YI.value + Init_Pos_Y.value;
        Pallet2_XO.value = Pallet2_XI.value + Init_Pos_X.value;
        Pallet2_YO.value = Pallet2_YI.value + Init_Pos_Y.value;
        Pallet3_XO.value = Pallet3_XI.value + Init_Pos_X.value;
        Pallet3_YO.value = Pallet3_YI.value + Init_Pos_Y.value;
        Pallet4_XO.value = Pallet4_XI.value + Init_Pos_X.value;
        Pallet4_YO.value = Pallet4_YI.value + Init_Pos_Y.value;
        Pallet5_XO.value = Pallet5_XI.value + Init_Pos_X.value;
        Pallet5_YO.value = Pallet5_YI.value + Init_Pos_Y.value;
        Pallet6_XO.value = Pallet6_XI.value + Init_Pos_X.value;
        Pallet6_YO.value = Pallet6_YI.value + Init_Pos_Y.value;
        Collector_XO.value = Collector_XI.value + Init_Pos_X.value;
        Collector_YO.value = Collector_YI.value + Init_Pos_Y.value;
        Collector2_XO.value = Collector2_XI.value + Init_Pos_X.value;
        Collector2_YO.value = Collector2_YI.value + Init_Pos_Y.value;
        Docker_XO.value = Docker_XI.value + Init_Pos_X.value;
        Docker_YO.value = Docker_YI.value + Init_Pos_Y.value;
    }
}
