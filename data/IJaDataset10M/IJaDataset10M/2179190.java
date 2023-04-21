package Beans.Requests.MachineManagement;

import Beans.Requests.MachineManagement.Components.AudioSettings;
import Beans.Requests.MachineManagement.Components.BIOSSettings;
import Beans.Requests.MachineManagement.Components.BootSettings;
import Beans.Requests.MachineManagement.Components.CPUSettings;
import Beans.Requests.MachineManagement.Components.IOSettings;
import Beans.Requests.MachineManagement.Components.MemorySettings;
import Beans.Requests.MachineManagement.Components.NetworkAdapterSettings;
import Beans.Requests.MachineManagement.Components.VRDPSettings;
import Beans.Requests.MachineManagement.Components.VideoSettings;
import Utilities.Constants;
import Utilities.Functions;
import Utilities.PermissionsConstants;

/**
 * Instances of this class are used by the user interface when a request to
 * modify the offline machine attributes is sent to the machines management service.
 *
 * @author Angel Sanadinov
 */
public class ModifyMachineAttributesRequest extends MachineManagementRequest {

    private String machineName;

    private String machineDescription;

    private String permissions;

    private AudioSettings audioSettings;

    private BIOSSettings biosSettings;

    private BootSettings bootSettings;

    private CPUSettings cpuSettings;

    private IOSettings ioSettings;

    private MemorySettings memorySettings;

    private NetworkAdapterSettings[] networksSettings;

    private VRDPSettings vrdpSettings;

    private VideoSettings videoSettings;

    private boolean arePermissionsSet;

    private int newOwnerId;

    private boolean isNewOwnerIdSet;

    private boolean isMachineDescriptionSet;

    private boolean isMachineNameSet;

    /**
     * No-argument constructor. <br><br>
     * All fields are set to their invalid values.
     *
     * @see Constants#INVALID_USER_ID
     */
    public ModifyMachineAttributesRequest() {
        super();
        this.machineName = null;
        this.isMachineNameSet = false;
        this.machineDescription = null;
        this.isMachineDescriptionSet = false;
        this.audioSettings = null;
        this.biosSettings = null;
        this.bootSettings = null;
        this.cpuSettings = null;
        this.ioSettings = null;
        this.memorySettings = null;
        this.networksSettings = null;
        this.vrdpSettings = null;
        this.videoSettings = null;
        this.arePermissionsSet = false;
        this.newOwnerId = Constants.INVALID_USER_ID;
        this.isNewOwnerIdSet = false;
        setRequestedAction(PermissionsConstants.ACTION_MACHINE_MODIFY_ATTRIBUTES);
    }

    /**
     * Constructs an incomplete request for modifying the offline attributes of a
     * virtual machine. <br><br>
     *
     * <b>Note:</b> <i>All machine data needs to be explicitly set!</i>
     *
     * @param requestorId the id of the user that made the request
     * @param serverId the id of the server on which the machine resides
     * @param machineId the id of the machine
     */
    public ModifyMachineAttributesRequest(int requestorId, int serverId, String machineId) {
        this();
        setRequestorId(requestorId);
        setServerId(serverId);
        setMachineId(machineId);
    }

    /**
     * Constructs a complete request for creating a new virtual machine. <br><br>
     *
     * <b>Note:</b> <i>No additional request setup is needed.</i>
     *
     * @param requestorId the id of the user that made the request
     * @param serverId the id of the server on which the machine resides
     * @param machineId the id of the machine
     * @param machineName the name of the machine
     * @param machineDescription the machine description
     * @param audioSettings audio settings
     * @param biosSettings BIOS settings for the machine
     * @param bootSettings Boot settings for the machine
     * @param cpuSettings CPU settings for the machine
     * @param ioSettings I/O settings for the machine
     * @param memorySettings Memory settings for the machine
     * @param networksSettings Networking settings for the machine (for each network adapter)
     * @param vrdpSettings VRDP server settings for the machine
     * @param videoSettings Video settings for the machine
     */
    public ModifyMachineAttributesRequest(int requestorId, int serverId, String machineId, String machineName, String machineDescription, AudioSettings audioSettings, BIOSSettings biosSettings, BootSettings bootSettings, CPUSettings cpuSettings, IOSettings ioSettings, MemorySettings memorySettings, NetworkAdapterSettings[] networksSettings, VRDPSettings vrdpSettings, VideoSettings videoSettings) {
        this(requestorId, serverId, machineId);
        this.machineName = machineName;
        this.isMachineNameSet = true;
        this.machineDescription = machineDescription;
        this.isMachineDescriptionSet = true;
        this.audioSettings = audioSettings;
        this.biosSettings = biosSettings;
        this.bootSettings = bootSettings;
        this.cpuSettings = cpuSettings;
        this.ioSettings = ioSettings;
        this.memorySettings = memorySettings;
        this.networksSettings = networksSettings;
        this.vrdpSettings = vrdpSettings;
        this.videoSettings = videoSettings;
    }

    /**
     * Retrieves the name of the machine.
     *
     * @return the machine name
     */
    public String getMachineName() {
        return machineName;
    }

    /**
     * Retrieves the machine description.
     *
     * @return
     */
    public String getMachineDescription() {
        return machineDescription;
    }

    /**
     * Retrieves the permissions of the machine.
     *
     * @return the machine's permissions
     */
    public String getPermissions() {
        return permissions;
    }

    /**
     * Retrieves the audio settings of the machine.
     *
     * @return the audio settings, if available or <code>null</code> otherwise
     */
    public AudioSettings getAudioSettings() {
        return audioSettings;
    }

    /**
     * Retrieves the BIOS settings of the machine.
     *
     * @return the BIOS settings, if available or <code>null</code> otherwise
     */
    public BIOSSettings getBiosSettings() {
        return biosSettings;
    }

    /**
     * Retrieves the boot settings of the machine.
     *
     * @return the boot settings, if available or <code>null</code> otherwise
     */
    public BootSettings getBootSettings() {
        return bootSettings;
    }

    /**
     * Retrieves the CPU settings of the machine.
     *
     * @return the CPU settings, if available or <code>null</code> otherwise
     */
    public CPUSettings getCpuSettings() {
        return cpuSettings;
    }

    /**
     * Retrieves the I/O settings of the machine.
     *
     * @return the I/O settings, if available or <code>null</code> otherwise
     */
    public IOSettings getIoSettings() {
        return ioSettings;
    }

    /**
     * Retrieves the memory settings of the machine.
     *
     * @return the memory settings, if available or <code>null</code> otherwise
     */
    public MemorySettings getMemorySettings() {
        return memorySettings;
    }

    /**
     * Retrieves the networking settings of the machine (for each network adapter).
     *
     * @return the networking settings, if available or <code>null</code> otherwise
     */
    public NetworkAdapterSettings[] getNetworksSettings() {
        return networksSettings;
    }

    /**
     * Retrieves the VRDP settings of the machine.
     *
     * @return the VRDP settings, if available or <code>null</code> otherwise
     */
    public VRDPSettings getVrdpSettings() {
        return vrdpSettings;
    }

    /**
     * Retrieves the video settings of the machine.
     *
     * @return the video settings, if available or <code>null</code> otherwise
     */
    public VideoSettings getVideoSettings() {
        return videoSettings;
    }

    /**
     * Retrieves the new permissions status.
     *
     * @return <code>true</code> if new permissions have been set or <code>false</code>
     *         if not
     */
    public boolean arePermissionsSet() {
        return arePermissionsSet;
    }

    /**
     * Retrieves the new owner status.
     *
     * @return <code>true</code> if a new owner has been set or <code>false</code> if not
     */
    public boolean isNewOwnerIdSet() {
        return isNewOwnerIdSet;
    }

    /**
     * Retrieves the new name status.
     *
     * @return <code>true</code> if a new name has been set or <code>false</code> if not
     */
    public boolean isMachineNameSet() {
        return isMachineNameSet;
    }

    /**
     * Retrieves the new description status.
     *
     * @return <code>true</code> if a new description has been set or <code>false</code> if not
     */
    public boolean isMachineDescriptionSet() {
        return isMachineDescriptionSet;
    }

    /**
     * Retrieves the id of the new owner of the machine.
     *
     * @return the new owner id, if available or <code>INVALID_USER_ID</code> otherwise
     *
     * @see Constants#INVALID_USER_ID
     */
    public int getNewOwnerId() {
        return newOwnerId;
    }

    /**
     * Sets the permissions of the machine.
     *
     * @param permissions the machine's permissions
     */
    public void setPermissions(String permissions) {
        this.permissions = permissions;
        this.arePermissionsSet = true;
    }

    /**
     * Sets the id of the new owner of the machine.
     *
     * @param newOwnerId the new owner id
     */
    public void setNewOwnerId(int newOwnerId) {
        this.newOwnerId = newOwnerId;
        this.isNewOwnerIdSet = true;
    }

    /**
     * Sets the name of the virtual machine.
     *
     * @param machineName the machine name
     */
    public void setMachineName(String machineName) {
        this.machineName = machineName;
        this.isMachineNameSet = true;
    }

    /**
     * Sets the description of the machine.
     *
     * @param machineDescription the machine description
     */
    public void setMachineDescription(String machineDescription) {
        this.machineDescription = machineDescription;
        this.isMachineDescriptionSet = true;
    }

    /**
     * Sets the audio settings for the machine.
     *
     * @param audioSettings the audio settings
     */
    public void setAudioSettings(AudioSettings audioSettings) {
        this.audioSettings = audioSettings;
    }

    /**
     * Sets the BIOS settings for the machine.
     *
     * @param biosSettings the BIOS settings
     */
    public void setBiosSettings(BIOSSettings biosSettings) {
        this.biosSettings = biosSettings;
    }

    /**
     * Sets the boot settings for the machine.
     *
     * @param bootSettings the boot settings
     */
    public void setBootSettings(BootSettings bootSettings) {
        this.bootSettings = bootSettings;
    }

    /**
     * Sets the CPU settings for the machine.
     *
     * @param cpuSettings the CPU settings
     */
    public void setCpuSettings(CPUSettings cpuSettings) {
        this.cpuSettings = cpuSettings;
    }

    /**
     * Sets the I/O settings for the machine.
     *
     * @param ioSettings the I/O settings
     */
    public void setIoSettings(IOSettings ioSettings) {
        this.ioSettings = ioSettings;
    }

    /**
     * Sets the memory settings for the machine.
     *
     * @param memorySettings the memory settings
     */
    public void setMemorySettings(MemorySettings memorySettings) {
        this.memorySettings = memorySettings;
    }

    /**
     * Sets the networking settings for the machine (for each network adapter).
     *
     * @param networksSettings the networking settings
     */
    public void setNetworksSettings(NetworkAdapterSettings[] networksSettings) {
        this.networksSettings = networksSettings;
    }

    /**
     * Sets the VRDP settings for the machine.
     *
     * @param vrdpSettings the VRDP settings
     */
    public void setVrdpSettings(VRDPSettings vrdpSettings) {
        this.vrdpSettings = vrdpSettings;
    }

    /**
     * Sets the video settings for the machine.
     *
     * @param videoSettings the video settings
     */
    public void setVideoSettings(VideoSettings videoSettings) {
        this.videoSettings = videoSettings;
    }

    /**
     * Checks if the networking settings are valid.
     *
     * @return <code>true</code> if the settings are valid or <code>false</code> otherwise
     */
    private boolean areNetworksSettingsValid() {
        if (networksSettings != null) {
            for (NetworkAdapterSettings adapterSettings : networksSettings) {
                if (adapterSettings != null && !adapterSettings.isValid()) return false; else ;
            }
        } else ;
        return true;
    }

    @Override
    public boolean isValid() {
        if (super.isValid() && isMachineIdValid() && (!arePermissionsSet || Functions.arePermissionsValid(permissions)) && (!isNewOwnerIdSet || Functions.isUserIdValid(newOwnerId)) && (machineName == null || Functions.isLocationValid(machineName)) && (audioSettings == null || audioSettings.isValid()) && (biosSettings == null || biosSettings.isValid() && (bootSettings == null || bootSettings.isValid()) && (cpuSettings == null || cpuSettings.isValid()) && (ioSettings == null || ioSettings.isValid()) && (memorySettings == null || memorySettings.isValid()) && areNetworksSettingsValid() && (vrdpSettings == null || vrdpSettings.isValid()) && (videoSettings == null || videoSettings.isValid()))) {
            return true;
        } else return false;
    }

    /**
     * Returns a textual representation of the object. <br>
     *
     * It is in the form: "ClassName: (machineId,machineName,machineDescription,permissions,newOwnerId)".
     *
     * @return the object represented as a String
     */
    @Override
    public String toString() {
        return ModifyMachineAttributesRequest.class.getSimpleName() + ": (" + getMachineId() + "," + machineName + "," + machineDescription + "," + permissions + "," + newOwnerId + ")";
    }
}
