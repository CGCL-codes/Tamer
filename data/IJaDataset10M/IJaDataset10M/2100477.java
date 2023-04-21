package org.ogce.schemas.gfac.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ye Fan
 */
public class HostBean implements Serializable {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 7173720848242950441L;

    private String hostName = null;

    private String gFacPath = "";

    private String jdkName = "jdk1.5";

    private String jdkPath = "";

    private int portRangeStart = 12000;

    private int portRangeEnd = 12500;

    private String hostEnv = "";

    private String tmpDir = "/tmp";

    private String gateKeeperJobManager = "";

    private String gateKeeperName = "";

    private String gateKeeperendPointReference = "";

    private String gateKeeperendPointReferenceItems = "";

    private boolean wsGram = false;

    private String gridFtpName = "";

    private String gridFtpendPointReference = "";

    private boolean sshEnabled = false;

    private String gridFtpEndpointItems = "";

    private String serviceTypeItems = "";

    private List<String> serviceType = new ArrayList<String>();

    private String hostNameItems;

    /**
	 * Constructs a HostBean.
	 *
	 */
    public HostBean() {
    }

    /**
	 * @return the gFacPath
	 */
    public String getgFacPath() {
        return gFacPath;
    }

    /**
	 * @param gFacPath the gFacPath to set
	 */
    public void setgFacPath(String gFacPath) {
        this.gFacPath = gFacPath;
    }

    /**
	 * @return the hostName
	 */
    public String getHostName() {
        return hostName;
    }

    /**
	 * @param hostName the hostName to set
	 */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
	 * @return the gFacPath
	 */
    public String getGFacPath() {
        return gFacPath;
    }

    /**
	 * @param facPath the gFacPath to set
	 */
    public void setGFacPath(String facPath) {
        gFacPath = facPath;
    }

    /**
	 * @return the jdkName
	 */
    public String getJdkName() {
        return jdkName;
    }

    /**
	 * @param jdkName the jdkName to set
	 */
    public void setJdkName(String jdkName) {
        this.jdkName = jdkName;
    }

    /**
	 * @return the jdkPath
	 */
    public String getJdkPath() {
        return jdkPath;
    }

    /**
	 * @param jdkPath the jdkPath to set
	 */
    public void setJdkPath(String jdkPath) {
        this.jdkPath = jdkPath;
    }

    /**
	 * @return the portRangeStart
	 */
    public int getPortRangeStart() {
        return portRangeStart;
    }

    /**
	 * @param portRangeStart the portRangeStart to set
	 */
    public void setPortRangeStart(int portRangeStart) {
        this.portRangeStart = portRangeStart;
    }

    /**
	 * @return the portRangeEnd
	 */
    public int getPortRangeEnd() {
        return portRangeEnd;
    }

    /**
	 * @param portRangeEnd the portRangeEnd to set
	 */
    public void setPortRangeEnd(int portRangeEnd) {
        this.portRangeEnd = portRangeEnd;
    }

    /**
	 * @return the hostEnv
	 */
    public String getHostEnv() {
        return hostEnv;
    }

    /**
	 * @param hostEnv the hostEnv to set
	 */
    public void setHostEnv(String hostEnv) {
        this.hostEnv = hostEnv;
    }

    /**
	 * @return the tmpDir
	 */
    public String getTmpDir() {
        return tmpDir;
    }

    /**
	 * @param tmpDir the tmpDir to set
	 */
    public void setTmpDir(String tmpDir) {
        this.tmpDir = tmpDir;
    }

    /**
	 * @return the gateKeeperName
	 */
    public String getGateKeeperName() {
        return gateKeeperName;
    }

    /**
	 * @param gateKeeperName the gateKeeperName to set
	 */
    public void setGateKeeperName(String gateKeeperName) {
        this.gateKeeperName = gateKeeperName;
    }

    /**
	 * @return the gateKeeperendPointReference
	 */
    public String getGateKeeperendPointReference() {
        return gateKeeperendPointReference;
    }

    /**
	 * @param gateKeeperendPointReference the gateKeeperendPointReference to set
	 */
    public void setGateKeeperendPointReference(String gateKeeperendPointReference) {
        this.gateKeeperendPointReference = gateKeeperendPointReference;
    }

    /**
	 * @return the wsGram
	 */
    public boolean isWsGram() {
        return wsGram;
    }

    /**
	 * @param wsGram the wsGram to set
	 */
    public void setWsGram(boolean wsGram) {
        this.wsGram = wsGram;
    }

    /**
	 * @return the gridFtpName
	 */
    public String getGridFtpName() {
        return gridFtpName;
    }

    /**
	 * @param gridFtpName the gridFtpName to set
	 */
    public void setGridFtpName(String gridFtpName) {
        this.gridFtpName = gridFtpName;
    }

    /**
	 * @return the gridFtpendPointReference
	 */
    public String getGridFtpendPointReference() {
        return gridFtpendPointReference;
    }

    /**
	 * @param gridFtpendPointReference the gridFtpendPointReference to set
	 */
    public void setGridFtpendPointReference(String gridFtpendPointReference) {
        this.gridFtpendPointReference = gridFtpendPointReference;
    }

    /**
	 * @return the sshEnabled
	 */
    public boolean isSshEnabled() {
        return sshEnabled;
    }

    /**
	 * @param sshEnabled the sshEnabled to set
	 */
    public void setSshEnabled(boolean sshEnabled) {
        this.sshEnabled = sshEnabled;
    }

    /**
	 * @return the gateKeeperJobManager
	 */
    public String getGateKeeperJobManager() {
        return gateKeeperJobManager;
    }

    /**
	 * @param gateKeeperJobManager the gateKeeperJobManager to set
	 */
    public void setGateKeeperJobManager(String gateKeeperJobManager) {
        this.gateKeeperJobManager = gateKeeperJobManager;
    }

    /**
	 * @param gridFtpEndpointItems
	 */
    public void setGridFtpEndpointItems(String gridFtpEndpointItems) {
        this.gridFtpEndpointItems = gridFtpEndpointItems;
    }

    /**
	 * @return the gridFtpEndpointItems
	 */
    public String getGridFtpEndpointItems() {
        return gridFtpEndpointItems;
    }

    /**
	 * @param serviceTypeItems
	 */
    public void setServiceTypeItems(String serviceTypeItems) {
        this.serviceTypeItems = serviceTypeItems;
    }

    /**
	 * @return the serviceTypeItems
	 */
    public String getServiceTypeItems() {
        return this.serviceTypeItems;
    }

    /**
	 * @param serviceType
	 */
    public void setServiceType(List<String> serviceType) {
        this.serviceType = serviceType;
    }

    /**
	 * @return the serviceType
	 */
    public List<String> getServiceType() {
        return this.serviceType;
    }

    /**
	 * @param gateKeeperendPointReferenceItems
	 */
    public void setGateKeeperendPointReferenceItems(String gateKeeperendPointReferenceItems) {
        this.gateKeeperendPointReferenceItems = gateKeeperendPointReferenceItems;
    }

    /**
	 * @return the gateKeeperendPointReferenceItems
	 */
    public String getGateKeeperendPointReferenceItems() {
        return gateKeeperendPointReferenceItems;
    }

    public void setHostNameItems(String hostNameItems) {
        this.hostNameItems = hostNameItems;
    }

    public String getHostNameItems() {
        return hostNameItems;
    }
}
