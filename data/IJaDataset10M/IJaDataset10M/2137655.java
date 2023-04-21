package components;

import java.util.HashMap;
import components.IOBuffer.IOBuffer;

/***
 * This is a device list, pare the device id with corresponding input buffer or
 * output buffer
 * 
 * @author Chz
 * 
 */
public class DevLst {

    public HashMap<Integer, IOBuffer> list = new HashMap<Integer, IOBuffer>();

    private static DevLst self;

    private DevLst() {
        list.put(0, gui.MyInput.keyboardInputBuffer);
        list.put(1, gui.MyOutput.ConsoleOutputBuffer);
    }

    public static DevLst getInstance() {
        if (self == null) self = new DevLst();
        return self;
    }
}
