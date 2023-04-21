package ggc.pump.device.animas;

import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.manager.PumpDevicesIds;

public class AnimasIR1250 extends AnimasPump {

    /**
     * Constructor 
     */
    public AnimasIR1250() {
        super();
    }

    /**
     * Constructor 
     * 
     * @param conn_parameter 
     * @param writer 
     */
    public AnimasIR1250(String conn_parameter, OutputWriter writer) {
        super(conn_parameter, writer);
    }

    /**
     * Constructor
     * 
     * @param conn_parameter
     * @param writer
     * @param da 
     */
    public AnimasIR1250(String conn_parameter, OutputWriter writer, DataAccessPlugInBase da) {
        super(conn_parameter, writer, da);
    }

    /**
     * Constructor
     * 
     * @param cmp
     */
    public AnimasIR1250(AbstractDeviceCompany cmp) {
        super(cmp);
    }

    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName() {
        return "IR 1250";
    }

    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName() {
        return "an_ir1250.jpg";
    }

    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId() {
        return PumpDevicesIds.PUMP_ANIMAS_IR_1250;
    }

    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    public String getInstructions() {
        return "INSTRUCTIONS_ANIMAS_IR1250";
    }

    /**
     * getComment - Get Comment for device 
     * 
     * @return comment or null
     */
    public String getComment() {
        return null;
    }

    /**
     * getImplementationStatus - Get Implementation Status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public int getImplementationStatus() {
        return DeviceImplementationStatus.IMPLEMENTATION_NOT_AVAILABLE;
    }

    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    public String getDeviceClassName() {
        return "ggc.pump.device.animas.AnimasIR1200";
    }

    /** 
     * Get Max Memory Records
     */
    public int getMaxMemoryRecords() {
        return 0;
    }

    /**
     * Get Download Support Type
     * 
     * @return
     */
    public int getDownloadSupportType() {
        return DownloadSupportType.DOWNLOAD_SUPPORT_NO;
    }

    /**
     * How Many Months Of Data Stored
     * 
     * @return
     */
    public int howManyMonthsOfDataStored() {
        return -1;
    }

    /**
     * Get Temporary Basal Type Definition
     * "TYPE=Unit;STEP=0.1"
     * "TYPE=Procent;STEP=10;MIN=0;MAX=200"
     * "TYPE=Both;STEP_UNIT=0.1;STEP=10;MIN=0;MAX=200"
     * 
     * @return
     */
    public String getTemporaryBasalTypeDefinition() {
        return null;
    }

    /**
     * Get Bolus Step (precission)
     * 
     * @return
     */
    public float getBolusStep() {
        return 0.1f;
    }

    /**
     * Get Basal Step (precission)
     * 
     * @return
     */
    public float getBasalStep() {
        return 0.1f;
    }

    /**
     * Are Pump Settings Set (Bolus step, Basal step and TBR settings)
     * 
     * @return
     */
    public boolean arePumpSettingsSet() {
        return false;
    }
}
