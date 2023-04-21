package basys.client.eib;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import basys.client.ListEntry;
import basys.client.Project;
import basys.client.ui.dialogs.NewEndDeviceDialog;
import basys.datamodels.installation.InstallationModel;
import basys.datamodels.architectural.ArchitecturalDataModel;
import basys.datamodels.eib.EIBDevicesDataModel;

/**
 * EIBActorConfigurator.java
 * 
 * 
 * @author	oalt
 * @version $Id: EIBDeviceConfigurator.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class EIBDeviceConfigurator {

    private int deviceType;

    private Project p;

    private String installationLocationID;

    private String location;

    /**
	 * 
	 */
    public EIBDeviceConfigurator(int deviceType, String installationLocationID, String location, Project p) {
        super();
        this.deviceType = deviceType;
        this.installationLocationID = installationLocationID;
        this.location = location;
        this.p = p;
    }

    /**
	 * Search for already installed actuators with matching functions.
	 * 
	 * @return id list of found actuators.
	 */
    public Vector findActuatorsInProject() {
        Vector actuators = new Vector();
        InstallationModel imodel = this.p.getInstallationModel();
        ArchitecturalDataModel amodel = this.p.getArchitecturalDataModel();
        actuators = imodel.findMatchingDevices(this.location, this.getRequiredDeviceFunctions(this.deviceType), "actuator", (Vector) amodel.getBusDeviceIDs(this.installationLocationID));
        return actuators;
    }

    /**
	 * Search for new matching actuators
	 * 
	 * @return id list of found actuators
	 */
    public Vector findNewActuators() {
        Vector actuators = new Vector();
        EIBDevicesDataModel model = (EIBDevicesDataModel) this.p.getApplication().getBusDeviceDataModel("EIB");
        actuators = model.findMatchingDevices(this.location, this.getRequiredDeviceFunctions(this.deviceType), "actuator");
        return actuators;
    }

    /**
	 * 
	 * @param requiredFunctions
	 * @return
	 */
    public Vector findSensorsInProject(String[] requiredFunctions) {
        Vector sensors = new Vector();
        InstallationModel imodel = this.p.getInstallationModel();
        ArchitecturalDataModel amodel = this.p.getArchitecturalDataModel();
        sensors = imodel.findMatchingDevices(this.location, requiredFunctions, "sensor", (Vector) amodel.getBusDeviceIDs(this.installationLocationID));
        return sensors;
    }

    /**
	 * 
	 * @param requiredFunctions
	 * @return
	 */
    public Vector findNewSensors(String enddeviceID) {
        String requiredFunctions[] = this.getRequiredFinctionsForSensor(enddeviceID);
        Vector sensors = new Vector();
        EIBDevicesDataModel model = (EIBDevicesDataModel) this.p.getApplication().getBusDeviceDataModel("EIB");
        sensors = model.findMatchingDevices(this.location, requiredFunctions, "sensor");
        return sensors;
    }

    private String[] getRequiredFinctionsForSensor(String enddeviceID) {
        InstallationModel imodel = this.p.getInstallationModel();
        return imodel.getFunctionsForEnddevice(enddeviceID);
    }

    public Vector getFunctionGroupList(String busDevID) {
        Vector list = new Vector();
        InstallationModel imodel = this.p.getInstallationModel();
        Vector ids = this.getFunctionGroupIDs(busDevID);
        for (Enumeration e = ids.elements(); e.hasMoreElements(); ) {
            String id = (String) e.nextElement();
            String name = imodel.readDOMNodeValue(imodel.getDataRootNode(id), new StringTokenizer("function/name", "/"));
            ListEntry le = new ListEntry(name, id);
            list.addElement(le);
        }
        return list;
    }

    public void setEIBGroupAddresses(String funcGroupID) {
        InstallationModel imodel = this.p.getInstallationModel();
        imodel.setGroupAddresses(this.getRequiredDeviceFunctions(this.deviceType), funcGroupID);
    }

    public Vector getFunctionGroupIDs(String busDevID) {
        Vector actuators = new Vector();
        InstallationModel imodel = this.p.getInstallationModel();
        actuators = imodel.getMatchingFunctionGroups(busDevID, this.getRequiredDeviceFunctions(this.deviceType));
        return actuators;
    }

    public String getInstallationLocationID() {
        return this.installationLocationID;
    }

    /**
	 * Returns all required EIB device functions for control the device (lamp, valve,...) with the given id.
	 * @param deviceType the type of device to control (lamp, valve, ...)
	 * @return array of required functions
	 */
    private String[] getRequiredDeviceFunctions(int deviceType) {
        String[] type = new String[1];
        type[0] = "";
        switch(deviceType) {
            case NewEndDeviceDialog.LAMP:
                type = new String[1];
                type[0] = "switching";
                break;
            case NewEndDeviceDialog.DIMMABLE_LAMP:
                type = new String[3];
                type[0] = "switching";
                type[1] = "";
                type[2] = "";
                break;
            case NewEndDeviceDialog.VALVE:
                type = new String[1];
                type[0] = "value";
                break;
        }
        return type;
    }
}
