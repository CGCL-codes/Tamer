package Beans.Responses.DataRetrievalComponents;

import Beans.VirtualResourceBean;
import com.sun.xml.ws.commons.virtualbox_3_2.IMedium;
import com.sun.xml.ws.commons.virtualbox_3_2.IMediumFormat;
import org.virtualbox_3_2.DeviceType;
import org.virtualbox_3_2.MediumState;
import org.virtualbox_3_2.MediumType;

/**
 * This class represents medium data, when retrieved from the system.
 *
 * @author Angel Sanadinov
 */
public class MediumDataComponent extends DataRetrievalComponent {

    private VirtualResourceBean mediumData;

    private String description;

    private MediumState mediumState;

    private String mediumLocaton;

    private String mediumName;

    private DeviceType deviceType;

    private boolean isHostDrive;

    private long physicalSize;

    private String storageFormat;

    private IMediumFormat mediumFormat;

    private MediumType mediumType;

    private String parentId;

    private String[] childrenIds;

    private String baseMediumId;

    private boolean isReadOnly;

    private long logicalSize;

    private boolean isAutoResetEnabled;

    private String lastAccessError;

    private String[] attachedMachinesIds;

    private boolean isMediumDataPartial;

    /**
     * Constructs a medium data component using the specified data. <br><br>
     *
     * Used when the service retrieves multiple medium data objects.
     *
     * @param mediumData medium data from the database
     * @param mediumName medium name
     * @param mediumDescription medium description
     * @param mediumState medium state
     */
    public MediumDataComponent(VirtualResourceBean mediumData, String mediumName, String mediumDescription, MediumState mediumState) {
        this.mediumData = mediumData;
        this.mediumName = mediumName;
        this.description = mediumDescription;
        this.mediumState = mediumState;
        this.isMediumDataPartial = true;
    }

    /**
     * Constructs a medium data component using the specified data. <br><br>
     *
     * Used when the service retrieves a single medium data object.
     *
     * @param mediumData medium data from the database
     * @param medium medium data from VirtualBox
     */
    public MediumDataComponent(VirtualResourceBean mediumData, IMedium medium) {
        this.mediumData = mediumData;
        this.description = medium.getDescription();
        this.mediumState = medium.getState();
        this.mediumLocaton = medium.getLocation();
        this.mediumName = medium.getName();
        this.deviceType = medium.getDeviceType();
        this.isHostDrive = medium.getHostDrive();
        this.physicalSize = medium.getSize().longValue();
        this.storageFormat = medium.getFormat();
        this.mediumFormat = medium.getMediumFormat();
        this.mediumType = medium.getType();
        if (medium.getParent() != null) this.parentId = medium.getParent().getId(); else this.parentId = null;
        IMedium[] children = medium.getChildren().toArray(new IMedium[medium.getChildren().size()]);
        int numberOfChildren = children.length;
        this.childrenIds = new String[numberOfChildren];
        for (int i = 0; i < numberOfChildren; i++) this.childrenIds[i] = children[i].getId();
        if (medium.getBase() != null) this.baseMediumId = medium.getBase().getId(); else this.baseMediumId = null;
        this.isReadOnly = medium.getReadOnly();
        this.logicalSize = medium.getLogicalSize().longValue();
        this.isAutoResetEnabled = medium.getAutoReset();
        this.lastAccessError = medium.getLastAccessError();
        this.attachedMachinesIds = medium.getMachineIds().toArray(new String[medium.getMachineIds().size()]);
        this.isMediumDataPartial = false;
    }

    /**
     * Retrieves the medium data associated with this object.
     *
     * @return the medium data
     */
    public VirtualResourceBean getMediumData() {
        return mediumData;
    }

    /**
     * Retrieves the medium description.
     *
     * @return the medium description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the medium's state.
     *
     * @return the medium state
     */
    public MediumState getMediumState() {
        return mediumState;
    }

    /**
     * Retrieves the location of the medium on the host.
     *
     * @return the medium location, if available or <code>null</code> otherwise
     */
    public String getMediumLocation() {
        return mediumLocaton;
    }

    /**
     * Retrieves the name of the medium.
     *
     * @return the medium name
     */
    public String getMediumName() {
        return mediumName;
    }

    /**
     * Retrieves the medium device type.
     *
     * @return the device type, if available or <code>null</code> otherwise
     */
    public DeviceType getDeviceType() {
        return deviceType;
    }

    /**
     * Returns whether the medium is an actual drive on the host.
     *
     * @return <code>true</code> if the medium is a drive on the host or
     *         <code>false</code> otherwise
     */
    public boolean isHostDrive() {
        return isHostDrive;
    }

    /**
     * Retrieves the size of the medium, as space taken on the host.
     *
     * @return the physical medium size (in MBs), if available or <code>0</code> otherwise
     */
    public long getPhysicalSize() {
        return physicalSize;
    }

    /**
     * Retrieves the storage format used by the medium.
     *
     * @return the storage format, if available or <code>null</code> otherwise
     */
    public String getStorageFormat() {
        return storageFormat;
    }

    /**
     * Retrieves the medium format.
     *
     * @return the medium format, if available or <code>null</code> otherwise
     */
    public IMediumFormat getMediumFormat() {
        return mediumFormat;
    }

    /**
     * Retrieves the medium type.
     *
     * @return the medium type, if available or <code>null</code> otherwise
     */
    public MediumType getMediumType() {
        return mediumType;
    }

    /**
     * Retrieves the id of the medium's parent.
     *
     * @return the parent id, if available or <code>null</code> otherwise
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Retrieves the IDs of all children media.
     *
     * @return the children IDs, if available or <code>null</code> otherwise
     */
    public String[] getChildrenIds() {
        return childrenIds;
    }

    /**
     * Retrieves the id of the medium's base.
     *
     * @return the base medium's id, if available or <code>null</code> otherwise
     */
    public String getBaseMediumId() {
        return baseMediumId;
    }

    /**
     * Returns whether the medium is read-only.
     *
     * @return <code>true</code> if the medium is read-only or <code>false</code>
     *         otherwise
     */
    public boolean isReadOnly() {
        return isReadOnly;
    }

    /**
     * Retrieves the logical size of the medium, as reported to the guest.
     *
     * @return the logical size, if available or <code>0</code> otherwise
     */
    public long getLogicalSize() {
        return logicalSize;
    }

    /**
     * Returns whether auto-reset is enabled for the medium.
     *
     * @return <code>true</code> if auto-reset is available or <code>false</code>
     *         otherwise
     */
    public boolean isAutoResetEnabled() {
        return isAutoResetEnabled;
    }

    /**
     * Retrieves the last access error that was encountered.
     *
     * @return the last access error, if available or <code>null</code> otherwise
     */
    public String getLastAccessError() {
        return lastAccessError;
    }

    /**
     * Retrieves the IDs of all machines, to which the medium is attached.
     *
     * @return the machine IDs, if available or <code>null</code> otherwise
     */
    public String[] getAttachedMachinesIds() {
        return attachedMachinesIds;
    }

    /**
     * Returns whether the data in this object is full or partial.
     *
     * @return <code>ture</code> if the data is partial or <code>false</code>
     *         otherwise
     */
    public boolean isMediumDataPartial() {
        return isMediumDataPartial;
    }

    @Override
    public boolean isValid() {
        if (mediumData != null && mediumData.isValid() && mediumName != null && mediumState != null) return true; else return false;
    }

    @Override
    public String toString() {
        return description + "," + mediumState + "," + mediumLocaton + "," + mediumName + "," + deviceType + "," + isHostDrive + "," + physicalSize + "," + storageFormat + "," + mediumFormat + "," + mediumType + "," + parentId + "," + baseMediumId + "," + isReadOnly + "," + logicalSize + "," + isAutoResetEnabled + "," + lastAccessError + "," + isMediumDataPartial;
    }
}
