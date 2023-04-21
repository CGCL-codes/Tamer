package fb.rt.fst1;

import fb.rt.net.*;
import fb.rt.service.*;
import fb.datatype.*;
import fb.rt.*;
import fb.rt.events.*;

/** FUNCTION_BLOCK PANEL_PUBL7
  * @author JHC
  * @version 20071011/JHC  */
public class PANEL_PUBL7 extends FBInstance {

    public BOOL SEMIAUTO = new BOOL();

    public WSTRING IDREAD = new WSTRING();

    public WSTRING IDWRITE = new WSTRING();

    public BOOL STLD = new BOOL();

    public BOOL IPLD = new BOOL();

    public BOOL MGZLD = new BOOL();

    public EventOutput INIT = new EventOutput();

    public EventOutput REQ = new EventOutput();

    public EventOutput START = new EventOutput();

    public EventOutput RESET = new EventOutput();

    public EventOutput STOP = new EventOutput();

    public EventOutput ACK = new EventOutput();

    public EventOutput ADD_WP = new EventOutput();

    public EventOutput REMOVE_WP = new EventOutput();

    public EventOutput INITO = new EventOutput();

    public EventOutput CHANGE = new EventOutput();

    public EventServer eiNamed(String s) {
        if (s.equals("INIT")) return INIT;
        if (s.equals("REQ")) return REQ;
        if (s.equals("START")) return START;
        if (s.equals("RESET")) return RESET;
        if (s.equals("STOP")) return STOP;
        if (s.equals("ACK")) return ACK;
        if (s.equals("ADD_WP")) return ADD_WP;
        if (s.equals("REMOVE_WP")) return REMOVE_WP;
        return super.eiNamed(s);
    }

    public EventOutput eoNamed(String s) {
        if (s.equals("INITO")) return INITO;
        if (s.equals("CHANGE")) return CHANGE;
        return super.eoNamed(s);
    }

    public ANY ivNamed(String s) throws FBRManagementException {
        if (s.equals("SEMIAUTO")) return SEMIAUTO;
        if (s.equals("IDREAD")) return IDREAD;
        if (s.equals("IDWRITE")) return IDWRITE;
        return super.ivNamed(s);
    }

    public ANY ovNamed(String s) throws FBRManagementException {
        if (s.equals("STLD")) return STLD;
        if (s.equals("IPLD")) return IPLD;
        if (s.equals("MGZLD")) return MGZLD;
        return super.ovNamed(s);
    }

    public void connectIV(String ivName, ANY newIV) throws FBRManagementException {
        if (ivName.equals("SEMIAUTO")) {
            connect_SEMIAUTO((BOOL) newIV);
            return;
        }
        if (ivName.equals("IDREAD")) {
            connect_IDREAD((WSTRING) newIV);
            return;
        }
        if (ivName.equals("IDWRITE")) {
            connect_IDWRITE((WSTRING) newIV);
            return;
        }
        super.connectIV(ivName, newIV);
    }

    public void connect_SEMIAUTO(BOOL newIV) throws FBRManagementException {
        SEMIAUTO = newIV;
        SUB.connectIVNoException("SD_4", SEMIAUTO);
    }

    public void connect_IDREAD(WSTRING newIV) throws FBRManagementException {
        IDREAD = newIV;
        PUB_PAN.connectIVNoException("ID", IDREAD);
    }

    public void connect_IDWRITE(WSTRING newIV) throws FBRManagementException {
        IDWRITE = newIV;
        SUB.connectIVNoException("ID", IDWRITE);
    }

    public SUBL PUB_PAN = new SUBL(3);

    public PUBL SUB = new PUBL(8);

    protected E_SR T1 = new E_SR();

    protected E_SR T2 = new E_SR();

    protected E_SR T3 = new E_SR();

    protected E_SR T4 = new E_SR();

    protected E_MERGE7 MRG = new E_MERGE7();

    protected E_MERGE MRG1 = new E_MERGE();

    protected E_DELAY DLY = new E_DELAY();

    protected E_SR T5 = new E_SR();

    protected E_SR T6 = new E_SR();

    public PANEL_PUBL7() {
        super();
        START.connectTo(T1.S);
        T1.EO.connectTo(MRG.EI1);
        MRG.EO.connectTo(SUB.REQ);
        RESET.connectTo(T2.S);
        T2.EO.connectTo(MRG.EI2);
        STOP.connectTo(T3.S);
        T3.EO.connectTo(MRG.EI3);
        ACK.connectTo(T4.S);
        T4.EO.connectTo(MRG.EI4);
        SUB.CNF.connectTo(DLY.START);
        DLY.EO.connectTo(T1.R);
        DLY.EO.connectTo(T2.R);
        DLY.EO.connectTo(T3.R);
        DLY.EO.connectTo(T4.R);
        INIT.connectTo(PUB_PAN.INIT);
        PUB_PAN.INITO.connectTo(SUB.INIT);
        SUB.INITO.connectTo(INITO);
        REQ.connectTo(MRG.EI5);
        SUB.CNF.connectTo(MRG1.EI1);
        MRG1.EO.connectTo(CHANGE);
        ADD_WP.connectTo(T5.S);
        T5.EO.connectTo(MRG.EI6);
        REMOVE_WP.connectTo(T6.S);
        T6.EO.connectTo(MRG.EI7);
        SUB.connectIVNoException("SD_1", T1.ovNamedNoException("Q"));
        SUB.connectIVNoException("SD_2", T2.ovNamedNoException("Q"));
        SUB.connectIVNoException("SD_5", T3.ovNamedNoException("Q"));
        SUB.connectIVNoException("SD_3", T4.ovNamedNoException("Q"));
        SUB.connectIVNoException("SD_4", SEMIAUTO);
        PUB_PAN.connectIVNoException("ID", IDREAD);
        SUB.connectIVNoException("ID", IDWRITE);
        PUB_PAN.connectOVNoException("RD_1", STLD);
        PUB_PAN.connectOVNoException("RD_2", IPLD);
        PUB_PAN.connectOVNoException("RD_3", MGZLD);
        SUB.connectIVNoException("SD_6", T5.ovNamedNoException("Q"));
        SUB.connectIVNoException("SD_7", T6.ovNamedNoException("Q"));
        DLY.DT.initializeNoException("t#1000ms");
    }

    public void initialize(String fbName, Resource r) throws FBRManagementException {
        super.initialize(fbName, r);
        PUB_PAN.initialize("PUB_PAN", r);
        SUB.initialize("SUB", r);
        T1.initialize("T1", r);
        T2.initialize("T2", r);
        T3.initialize("T3", r);
        T4.initialize("T4", r);
        MRG.initialize("MRG", r);
        MRG1.initialize("MRG1", r);
        DLY.initialize("DLY", r);
        T5.initialize("T5", r);
        T6.initialize("T6", r);
    }

    /** Start the FB instances. */
    public void start() {
        PUB_PAN.start();
        SUB.start();
        T1.start();
        T2.start();
        T3.start();
        T4.start();
        MRG.start();
        MRG1.start();
        DLY.start();
        T5.start();
        T6.start();
    }

    /** Stop the FB instances. */
    public void stop() {
        PUB_PAN.stop();
        SUB.stop();
        T1.stop();
        T2.stop();
        T3.stop();
        T4.stop();
        MRG.stop();
        MRG1.stop();
        DLY.stop();
        T5.stop();
        T6.stop();
    }
}
