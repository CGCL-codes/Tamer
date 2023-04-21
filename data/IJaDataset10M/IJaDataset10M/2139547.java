package gnu.io;

import java.util.EventObject;

/**
 * @author Trent Jarvi
 * @version %I%, %G%
 * @since JDK1.0
 */
public class SerialPortEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    public static final int DATA_AVAILABLE = 1;

    public static final int OUTPUT_BUFFER_EMPTY = 2;

    public static final int CTS = 3;

    public static final int DSR = 4;

    public static final int RI = 5;

    public static final int CD = 6;

    public static final int OE = 7;

    public static final int PE = 8;

    public static final int FE = 9;

    public static final int BI = 10;

    private boolean OldValue;

    private boolean NewValue;

    private int eventType;

    public SerialPortEvent(SerialPort srcport, int eventtype, boolean oldvalue, boolean newvalue) {
        super(srcport);
        OldValue = oldvalue;
        NewValue = newvalue;
        eventType = eventtype;
    }

    public int getEventType() {
        return (eventType);
    }

    public boolean getNewValue() {
        return (NewValue);
    }

    public boolean getOldValue() {
        return (OldValue);
    }
}
