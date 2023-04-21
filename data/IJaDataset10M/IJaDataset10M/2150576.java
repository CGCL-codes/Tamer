package net.sourceforge.jmodbus;

/**
 * Class to implement a Modbus RTU Master device.  This class only 
 * defines what type of transport if to be used, all the work in 
 * generating and sending requests is performed by the methods in the 
 * ModbusMaster class.
 *
 * @author Kelvin Proctor
 */
public class ModbusRTUMaster extends ModbusMaster {

    private static ModbusRTUTransport rtuTransport;

    static {
        rtuTransport = new ModbusRTUTransport();
    }

    /**
     * Constructor that uses the RTU transport created with 
     * default parameters.  
     */
    public ModbusRTUMaster() {
        super(rtuTransport);
    }
}
