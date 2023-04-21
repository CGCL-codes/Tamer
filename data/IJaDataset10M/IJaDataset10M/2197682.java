package org.dicom4j.apps.commons.dicomdevice;

/**
 * remote device
 *
 * @since 0.0
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public class DicomDevice {

    /**
	 * the name
	 */
    private String fName = "DicomDevice";

    /**
	 * the AET to use
	 */
    private String fAET = "ANY-AET";

    public void setName(String name) {
        this.fName = name;
    }

    public String getAET() {
        return fAET;
    }

    public void setAET(String aet) {
        fAET = aet;
    }

    private String fHostName;

    private int fPort;

    public DicomDevice() {
        this("", "localhost", 104, "ANY-AET");
    }

    public DicomDevice(String aName, String aHostName) {
        this(aName, aHostName, 104, "ANY-AET");
    }

    public DicomDevice(String aName, String aHostName, int aPort) {
        this(aName, aHostName, aPort, "ANY-AET");
    }

    public DicomDevice(String aName, String aHostName, int aPort, String aET) {
        super();
        setName(aName);
        setHostName(aHostName);
        setAET(aET);
        setPort(aPort);
    }

    public String getName() {
        return this.fName;
    }

    /**
	 * @return Returns the fHostName.
	 */
    public String getHostName() {
        return fHostName;
    }

    /**
	 * @param hostName The fHostName to set.
	 */
    public void setHostName(String hostName) {
        fHostName = hostName;
    }

    /**
	 * @return Returns the fPort.
	 */
    public int getPort() {
        return fPort;
    }

    /**
	 * @param port The fPort to set.
	 */
    public void setPort(int port) {
        fPort = port;
    }
}
