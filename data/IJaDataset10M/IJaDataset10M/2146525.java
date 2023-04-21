package de.engelhardt.jdso.control;

import org.eclipse.swt.widgets.Text;
import de.engelhardt.jdso.comm.TwoWaySerialComm;
import de.engelhardt.jdso.visual.Scope;

/**
 * @author Dieter
 *
 */
public class DataControl {

    private Text output;

    private TwoWaySerialComm serialCom;

    private Scope scope;

    public DataControl(Text msg) {
        output = msg;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void addMessage(String strMsg) {
        output.append(strMsg + output.getLineDelimiter());
    }

    public void init() {
        try {
            serialCom = new TwoWaySerialComm(this);
            serialCom.connect("COM1");
            reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendACDC(boolean dc) {
        try {
            serialCom.write(dc ? "DC\r\n" : "AC\r\n");
        } catch (Exception e) {
            addMessage(e.getMessage());
        }
    }

    public void sendStart() {
        try {
            serialCom.readScopeData();
        } catch (Exception e) {
            addMessage(e.getMessage());
        }
    }

    public void sendStop() {
        try {
            serialCom.stopScopeData();
        } catch (Exception e) {
            addMessage(e.getMessage());
        }
    }

    public void sendTiming(int time) {
        String sTimeCmd;
        switch(time) {
            case 1:
                sTimeCmd = "DIV 0,0";
                scope.setTimeFactor(60);
                break;
            case 2:
                sTimeCmd = "DIV 0,0";
                scope.setTimeFactor(150);
                break;
            case 3:
                sTimeCmd = "DIV 0,1";
                scope.setTimeFactor(300);
                break;
            case 4:
                sTimeCmd = "DIV 0,4";
                scope.setTimeFactor(300);
                break;
            case 5:
                sTimeCmd = "DIV 0,9";
                scope.setTimeFactor(300);
                break;
            case 6:
                sTimeCmd = "DIV 0,19";
                scope.setTimeFactor(300);
                break;
            case 7:
                sTimeCmd = "DIV 0,38";
                scope.setTimeFactor(385);
                break;
            case 8:
                sTimeCmd = "DIV 1,4";
                scope.setTimeFactor(188);
                break;
            case 9:
                sTimeCmd = "DIV 1,4";
                scope.setTimeFactor(375);
                break;
            case 10:
                sTimeCmd = "DIV 1,14";
                scope.setTimeFactor(261);
                break;
            case 11:
                sTimeCmd = "DIV 1,21";
                scope.setTimeFactor(355);
                break;
            case 12:
                sTimeCmd = "DIV 2,32";
                scope.setTimeFactor(71);
                break;
            case 13:
                sTimeCmd = "DIV 2,64";
                scope.setTimeFactor(90);
                break;
            case 14:
                sTimeCmd = "DIV 2,64";
                scope.setTimeFactor(180);
                break;
            case 15:
                sTimeCmd = "DIV 3, 0";
                scope.setTimeFactor(60);
                break;
            default:
                sTimeCmd = "ERR";
                scope.setTimeFactor(0);
                break;
        }
        try {
            serialCom.write(sTimeCmd + "\r\n");
        } catch (Exception e) {
            addMessage(e.getMessage());
        }
    }

    public void sendLevel(int level) {
        try {
            serialCom.write("OFFSET " + level + "\r\n");
        } catch (Exception e) {
            addMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendTriggerLevel(boolean bHi) {
        try {
            serialCom.write((bHi ? "TRIGFH" : "TRIGFL") + "\r\n");
        } catch (Exception e) {
            addMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendTrigger(int level) {
        try {
            serialCom.write("TRIGOFF " + (byte) level + "\r\n");
            scope.setTriggerLevel(level);
        } catch (Exception e) {
            addMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendOffeset(int level) {
        try {
            serialCom.write("OFFSET " + (byte) level + "\r\n");
        } catch (Exception e) {
            addMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    public TwoWaySerialComm getSerialCom() {
        return serialCom;
    }

    public void setScopeData(int[] buffer) {
        scope.setScopeData(buffer);
    }

    public Scope getScope() {
        return scope;
    }

    public void dispose() {
        getSerialCom().dispose();
    }

    public void reset() {
        try {
            serialCom.write("RESET " + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
