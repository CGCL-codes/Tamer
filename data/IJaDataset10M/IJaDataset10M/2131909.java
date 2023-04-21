package ggc.pump.manager.company;

import java.util.Hashtable;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.protocol.ConnectionProtocols;
import ggc.pump.device.AbstractPump;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:      Generic Pump Device  
 *  Description:   Generic Pump Device used for getting profile names for specified 
 *                 pump company.
 * 
 *  Author: Andy {andy@atech-software.com}
 */
public class GenericPumpDevice extends AbstractPump {

    /**
     * The apdc.
     */
    AbstractPumpDeviceCompany apdc;

    /**
     * Instantiates a new generic pump device.
     * 
     * @param apdc the apdc
     */
    public GenericPumpDevice(AbstractPumpDeviceCompany apdc) {
        this.apdc = apdc;
    }

    /**
     * getDeviceCompany - get Company for device
     * 
     * @return 
     */
    public AbstractDeviceCompany getDeviceCompany() {
        return apdc;
    }

    /**
     * getConnectionProtocol - returns id of connection protocol
     * 
     * @return id of connection protocol
     */
    public int getConnectionProtocol() {
        return ConnectionProtocols.PROTOCOL_NONE;
    }

    /**
     * getConnectionPort - connection port data
     * 
     * @return connection port as string
     */
    public String getConnectionPort() {
        return "NONE";
    }

    /**
     * Is Device Readable (there are some devices that are not actual devices, but are used to get some
     * sort of specific device data - in most cases we call them generics, and they don't have ability
     * to read data)
     * @return
     */
    public boolean isReadableDevice() {
        return false;
    }

    /**
     * getName - Get Name of device. 
     * Should be implemented by device class.
     * 
     * @return 
     */
    public String getName() {
        return "Generic";
    }

    /** 
     * getProfileNames
     */
    public String[] getProfileNames() {
        return this.apdc.getProfileNames();
    }

    /** 
     * close
     */
    public void close() throws PlugInBaseException {
    }

    /** 
     * getAlarmMappings
     */
    public Hashtable<String, Integer> getAlarmMappings() {
        return null;
    }

    /** 
     * getBolusMappings
     */
    public Hashtable<String, Integer> getBolusMappings() {
        return null;
    }

    /** 
     * getDeviceInfo
     */
    public DeviceIdentification getDeviceInfo() {
        return null;
    }

    /** 
     * getErrorMappings
     */
    public Hashtable<String, Integer> getErrorMappings() {
        return null;
    }

    /** 
     * getEventMappings
     */
    public Hashtable<String, Integer> getEventMappings() {
        return null;
    }

    /** 
     * getMaxMemoryRecords
     */
    public int getMaxMemoryRecords() {
        return 0;
    }

    /** 
     * getReportMappings
     */
    public Hashtable<String, Integer> getReportMappings() {
        return null;
    }

    /** 
     * loadPumpSpecificValues
     */
    public void loadPumpSpecificValues() {
    }

    /** 
     * open
     */
    public boolean open() throws PlugInBaseException {
        return false;
    }

    /** 
     * dispose
     */
    public void dispose() {
    }

    /** 
     * getComment
     */
    public String getComment() {
        return null;
    }

    /** 
     * getDeviceClassName
     */
    public String getDeviceClassName() {
        return null;
    }

    /** 
     * getDeviceId
     */
    public int getDeviceId() {
        return 0;
    }

    /** 
     * getDeviceSpecialComment
     */
    public String getDeviceSpecialComment() {
        return null;
    }

    /** 
     * getIconName
     */
    public String getIconName() {
        return null;
    }

    /** 
     * getImplementationStatus
     */
    public int getImplementationStatus() {
        return 0;
    }

    /** 
     * getInstructions
     */
    public String getInstructions() {
        return null;
    }

    /** 
     * hasSpecialProgressStatus
     */
    public boolean hasSpecialProgressStatus() {
        return false;
    }

    /** 
     * isDeviceCommunicating
     */
    public boolean isDeviceCommunicating() {
        return false;
    }

    /** 
     * readConfiguration
     */
    public void readConfiguration() throws PlugInBaseException {
    }

    /** 
     * readDeviceDataFull
     */
    public void readDeviceDataFull() throws PlugInBaseException {
    }

    /** 
     * readDeviceDataPartitial
     */
    public void readDeviceDataPartitial() throws PlugInBaseException {
    }

    /** 
     * readInfo
     */
    public void readInfo() throws PlugInBaseException {
    }

    /**
     * Get Download Support Type
     * 
     * @return
     */
    public int getDownloadSupportType() {
        return DownloadSupportType.DOWNLOAD_SUPPORT_NA_GENERIC_DEVICE;
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
