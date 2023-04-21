package openfarmmanager.beans.ccu;

import openfarmmanager.beans.IBean;
import openfarmmanager.memory.monitor.MemoryCalculator;

public class CCUInformationBean implements IBean {

    private static final long serialVersionUID = 8788766194895778264L;

    private String ip;

    private int port;

    private String name;

    private String machineName;

    private String wsdlUrl;

    private String basedOS;

    private String root;

    public CCUInformationBean() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getWsdlUrl() {
        return wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    public String getBasedOS() {
        return basedOS;
    }

    public void setBasedOS(String basedOS) {
        this.basedOS = basedOS;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    /**
	 * Expressed in Kb
	 * */
    public double getJvmMaxMemory() {
        return MemoryCalculator.convertFromBytesToKilobytes(MemoryCalculator.getCalc().getJvmMaxMemory());
    }

    /**
	 * Expressed in Kb
	 * */
    public double getJvmFreeMemory() {
        return MemoryCalculator.convertFromBytesToKilobytes(MemoryCalculator.getCalc().getJvmFreeMemory());
    }

    public double getJvmUsedMemory() {
        return MemoryCalculator.convertFromBytesToKilobytes(MemoryCalculator.getCalc().getJvmUsedMemory());
    }

    public int getNumberOfProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }
}
