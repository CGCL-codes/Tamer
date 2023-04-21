package fb.rt.hmi;

import java.awt.*;
import java.awt.event.*;
import fb.datatype.*;
import fb.rt.*;
import fb.rt.net.*;
import fb.rt.events.*;

public class IN_EVENT extends FBInstance {

    public EventServer INIT = new EventInput(this);

    public EventServer eiNamed(String s) {
        if (s.equals("INIT")) return INIT;
        return super.eiNamed(s);
    }

    public EventOutput INITO = new EventOutput();

    public EventOutput IND = new EventOutput();

    public EventOutput eoNamed(String s) {
        if (s.equals("INITO")) return INITO;
        if (s.equals("IND")) return IND;
        return super.eoNamed(s);
    }

    public BOOL QI = new BOOL();

    public WSTRING LABEL = new WSTRING();

    public ANY ivNamed(String s) throws FBRManagementException {
        if (s.equals("QI")) return QI;
        if (s.equals("LABEL")) return LABEL;
        return super.ivNamed(s);
    }

    public void connectIV(String ivName, ANY newIV) throws FBRManagementException {
        if (ivName.equals("QI")) {
            connect_QI((BOOL) newIV);
            return;
        }
        if (ivName.equals("LABEL")) {
            connect_LABEL((WSTRING) newIV);
            return;
        }
        super.connectIV(ivName, newIV);
    }

    public void connect_QI(BOOL newIV) throws FBRManagementException {
        QI = newIV;
    }

    public void connect_LABEL(WSTRING newIV) throws FBRManagementException {
        LABEL = newIV;
    }

    public BOOL QO = new BOOL();

    public ANY ovNamed(String s) throws FBRManagementException {
        if (s.equals("QO")) return QO;
        return super.ovNamed(s);
    }
}
