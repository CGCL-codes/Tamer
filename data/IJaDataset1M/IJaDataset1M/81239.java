package net.sourceforge.smarthomephone.x10;

import com.jpeterson.x10.GatewayException;
import com.jpeterson.x10.GatewayStateError;
import com.jpeterson.x10.X10Util;
import com.jpeterson.x10.event.AddressEvent;
import com.jpeterson.x10.event.AddressListener;
import com.jpeterson.x10.event.FunctionEvent;
import com.jpeterson.x10.event.FunctionListener;
import com.jpeterson.x10.module.CM11A;

public class X10ExecutionSingleton implements AddressListener, FunctionListener {

    private static Object lock = new Object();

    private static X10ExecutionSingleton instance = null;

    private CM11A cm11a;

    private X10ExecutionSingleton() throws GatewayException, GatewayStateError {
        System.setProperty("DEBUG", "TRUE");
        cm11a = new CM11A();
        cm11a.setPortName("/dev/ttyS0");
        cm11a.addAddressListener(this);
        cm11a.addFunctionListener(this);
        cm11a.allocate();
    }

    public static X10ExecutionSingleton getInstance() throws GatewayException, GatewayStateError {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new X10ExecutionSingleton();
                }
            }
        }
        return instance;
    }

    public void executeCommand(byte x10Action, byte housecode, int elementcode) {
        AddressEvent adressEvent = new AddressEvent(this, housecode, elementcode);
        FunctionEvent functionEvent;
        if ((x10Action == X10Util.X10_FUNCTION_DIM) || (x10Action == X10Util.X10_FUNCTION_BRIGHT)) {
            functionEvent = new FunctionEvent(this, 5, housecode, x10Action, 22);
        } else {
            functionEvent = new FunctionEvent(this, 0, housecode, x10Action, 22);
        }
        synchronized (cm11a) {
            cm11a.transmit(adressEvent);
            cm11a.transmit(functionEvent);
        }
    }

    public void address(AddressEvent e) {
    }

    public void functionAllLightsOff(FunctionEvent e) {
    }

    public void functionAllLightsOn(FunctionEvent e) {
    }

    public void functionAllUnitsOff(FunctionEvent e) {
    }

    public void functionBright(FunctionEvent e) {
    }

    public void functionDim(FunctionEvent e) {
    }

    public void functionHailAcknowledge(FunctionEvent e) {
    }

    public void functionHailRequest(FunctionEvent e) {
    }

    public void functionOff(FunctionEvent e) {
    }

    public void functionOn(FunctionEvent e) {
    }

    public void functionPresetDim1(FunctionEvent e) {
    }

    public void functionPresetDim2(FunctionEvent e) {
    }

    public void functionStatusOff(FunctionEvent e) {
    }

    public void functionStatusOn(FunctionEvent e) {
    }

    public void functionStatusRequest(FunctionEvent e) {
    }
}
