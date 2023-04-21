package eu.slasoi.infrastructure.model.infrastructure;

import java.util.Hashtable;
import java.util.UUID;
import eu.slasoi.infrastructure.model.Kind;

/**
 * This class represents A Compute kind Once created it contains a unique Identifier
 * 
 * @author Patrick Cheevers
 * 
 */
public class Compute extends Kind {

    public Compute() {
        super();
        uniqueid = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "Compute [boot_vol_type=" + bootVoltType + ", cpu_arch=" + cpuArch + ", cpu_cores=" + cpuCores + ", cpu_speed=" + cpuSpeed + ", extras=" + extras + ", macAddress=" + macAddress + ", memory_reliability=" + memReliability + ", memory_size=" + memSize + ", memory_speed=" + memSpeed + ", status=" + status + ", uniqueid=" + uniqueid + ", version=" + version + "]";
    }

    private String bootVoltType;

    private String cpuArch;

    private int cpuCores;

    private float cpuSpeed;

    private UUID uniqueid;

    private String memReliability;

    private float memSize;

    private float memSpeed;

    private long version;

    private String status;

    private String macAddress;

    private Hashtable<String, String> extras;

    /**
     * @return the extras
     */
    public Hashtable<String, String> getExtras() {
        return extras;
    }

    /**
     * @param extras
     *            the extras to set
     */
    public void setExtras(Hashtable<String, String> extras) {
        this.extras = extras;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     * @return the bootVoltType
     */
    public String getBootVoltType() {
        return bootVoltType;
    }

    /**
     * @param bootVoltType
     *            the bootVoltType to set
     */
    public void setBootVoltType(final String bootVoltType) {
        this.bootVoltType = bootVoltType;
    }

    /**
     * @return the cpuArch
     */
    public String getCpuArch() {
        return cpuArch;
    }

    /**
     * @param cpuArch
     *            the cpuArch to set
     */
    public void setCpuArch(final String cpuArch) {
        this.cpuArch = cpuArch;
    }

    /**
     * @return the cpuCores
     */
    public int getCpuCores() {
        return cpuCores;
    }

    /**
     * @param cpuCores
     *            the cpuCores to set
     */
    public void setCpuCores(final int cpuCores) {
        this.cpuCores = cpuCores;
    }

    /**
     * @return the uniqueid
     */
    public UUID getUniqueid() {
        return uniqueid;
    }

    /**
     * @param uniqueid
     *            the uniqueid to set
     */
    public void setUniqueid(final UUID uniqueid) {
        this.uniqueid = uniqueid;
    }

    /**
     * @return the memReliability
     */
    public String getMemReliability() {
        return memReliability;
    }

    /**
     * @param memReliability
     *            the memReliability to set
     */
    public void setMemReliability(final String memReliability) {
        this.memReliability = memReliability;
    }

    /**
     * @return the memSize
     */
    public float getMemSize() {
        return memSize;
    }

    /**
     * @param memSize
     *            the memSize to set
     */
    public void setMemSize(final float memSize) {
        this.memSize = memSize;
    }

    /**
     * @return the memSpeed
     */
    public float getMemSpeed() {
        return memSpeed;
    }

    /**
     * @param memSpeed
     *            the memSpeed to set
     */
    public void setMemSpeed(final float memSpeed) {
        this.memSpeed = memSpeed;
    }

    public UUID getUniqueId() {
        return uniqueid;
    }

    public void setUniqueId(UUID id) {
        this.uniqueid = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }

    public void setMacAddress(final String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }
}
