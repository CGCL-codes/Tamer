package org.dicom4j.apps.pacsadmin.core;

import org.dicom4j.apps.commons.application.Configuration;
import org.dicom4j.toolkit.network.devices.LightRemoteDevice;

public class PacsAdminConfiguration extends Configuration {

    public PacsAdminConfiguration(String aConfigFileName) {
        super(aConfigFileName);
    }

    public String getDatafilepath() throws Exception {
        return getString("application.datafilepath");
    }

    public LightRemoteDevice getWorklistServer() throws Exception {
        LightRemoteDevice lNode = new LightRemoteDevice();
        lNode.setHostName(getString("worklistserver.hostname"));
        lNode.setPort(getint("worklistserver.port"));
        lNode.setAET(getString("worklistserver.AET"));
        return lNode;
    }
}
