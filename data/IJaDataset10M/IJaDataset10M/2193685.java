package srcp.server;

import java.io.*;

/** transport object for SRCP instructions from DeviceGroup instances to the
  * processor instance and info distributor instance
  *
  * @author  osc
  * @version $Revision: 163 $
  */
public class Operand implements Serializable {

    protected String strBus = null;

    protected String strGroup = null;

    protected String strAddr = null;

    protected String strPort = null;

    protected String strValue = null;

    protected String strDelay = null;

    protected String strFunc = null;

    protected String strTimestamp = null;

    protected boolean blnInit = false;

    protected boolean blnTerm = false;

    /** default constructor */
    public Operand() {
    }

    /** constructor for setting all fields as strings */
    public Operand(String strABus, String strAGroup, String strAAddr, String strAPort, String strAValue, String strADelay, String strATimestamp) {
        strBus = strABus;
        strGroup = strAGroup;
        strAddr = strAAddr;
        strPort = strAPort;
        strValue = strAValue;
        strDelay = strADelay;
        strTimestamp = strATimestamp;
    }

    /** constructor for setting all fields as strings */
    public Operand(String strABus, String strAGroup, String strAAddr, String strAPort, String strAValue, String strADelay, String strAFunc, String strATimestamp) {
        strBus = strABus;
        strGroup = strAGroup;
        strAddr = strAAddr;
        strPort = strAPort;
        strValue = strAValue;
        strDelay = strADelay;
        strFunc = strAFunc;
        strTimestamp = strATimestamp;
    }

    /** constructor for setting fields with integer values */
    public Operand(String strABus, String strAGroup, int intAddr, int intPort, int intValue, int intDelay, String strATimestamp) {
        strBus = strABus;
        strGroup = strAGroup;
        strAddr = Integer.toString(intAddr);
        strPort = Integer.toString(intPort);
        strValue = Integer.toString(intValue);
        strDelay = Integer.toString(intDelay);
        strTimestamp = strATimestamp;
    }

    /** copy constructor */
    public Operand(Operand objOperand) {
        strBus = objOperand.strBus;
        strGroup = objOperand.strGroup;
        strAddr = objOperand.strAddr;
        strPort = objOperand.strPort;
        strValue = objOperand.strValue;
        strDelay = objOperand.strDelay;
        strTimestamp = objOperand.strTimestamp;
        strFunc = objOperand.strFunc;
        blnInit = objOperand.blnInit;
        blnTerm = objOperand.blnTerm;
    }

    /** return all data in one string */
    public String getMessage() {
        return strGroup + " " + strAddr + " " + strPort + " " + strValue + " " + strDelay;
    }

    /** return this field */
    public String getAddr() {
        return strAddr;
    }

    /** return this field as int*/
    public int getIntAddr() {
        try {
            return Integer.parseInt(strAddr);
        } catch (Exception e) {
        }
        return 0;
    }

    /** return this field */
    public String getPort() {
        return strPort;
    }

    /** return this field */
    public String getGroup() {
        return strGroup;
    }

    /** return this field */
    public String getValue() {
        return strValue;
    }

    /** return this field as int*/
    public int getIntValue() {
        try {
            return Integer.parseInt(strValue);
        } catch (Exception e) {
        }
        return 0;
    }

    /** return this field */
    public String getDelay() {
        return strDelay;
    }

    /** return this field */
    public String getTimestamp() {
        return strTimestamp;
    }

    public boolean getInit() {
        return blnInit;
    }

    public boolean getTerm() {
        return blnTerm;
    }

    public void setInit() {
        blnInit = true;
    }

    public void setTerm() {
        blnTerm = true;
    }

    public void setDelay(String strADelay) {
        strDelay = strADelay;
    }

    /** return this field */
    public String getBus() {
        return strBus;
    }

    public void setTimestamp(String strATimestamp) {
        strTimestamp = strATimestamp;
    }

    public void setValue(int intValue) {
        strValue = Integer.toString(intValue);
    }

    /** return this field */
    public String getFunc() {
        return strFunc;
    }

    public void setPort(String strAPort) {
        strPort = strAPort;
    }

    public void setValue(String strAValue) {
        strValue = strAValue;
    }

    public void setAddr(String strAAddr) {
        strAddr = strAAddr;
    }
}
