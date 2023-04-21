package nl.kbna.dioscuri.module.ata;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing a single ATA channel
 * 
 */
public class ATAChannel {

    private static Logger logger = Logger.getLogger("nl.kbna.dioscuri.module.ata");

    public static final int IDE_MASTER_INDEX = 0;

    public static final int IDE_SLAVE_INDEX = 1;

    private ATADrive[] drives = new ATADrive[ATAConstants.MAX_NUMBER_DRIVES_PER_CHANNEL];

    private int ioAddress1;

    private int ioAddress2;

    private int irqNumber;

    private int defaultIoAddress1;

    private int defaultIoAddress2;

    private ATA parent;

    private int selectedDriveIndex;

    /**
     * Class constructor.
     * 
     * @param parent        the parent IDE object
     * @param defaultIoAddr1
     * @param defaultIoAddr2
     */
    public ATAChannel(ATA parent, int defaultIoAddr1, int defaultIoAddr2) {
        this.parent = parent;
        for (int i = 0; i < this.drives.length; i++) {
            this.drives[i] = new ATADrive(ATADriveType.NONE, parent, false);
        }
        this.ioAddress1 = defaultIoAddr1;
        this.ioAddress2 = defaultIoAddr2;
        this.defaultIoAddress1 = defaultIoAddr1;
        this.defaultIoAddress2 = defaultIoAddr2;
        this.irqNumber = -1;
        this.selectedDriveIndex = 0;
    }

    /**
     * Get cur selected IDE Driver Controller
     * 
     * @return IDE Driver Controller
     */
    public ATADriveController getSelectedController() {
        ATADriveController curDriveController = this.getSelectedDrive().getControl();
        return curDriveController;
    }

    /**
     * Reset the channel.
     * 
     * @return true if reset successful
     */
    public boolean reset() {
        this.ioAddress1 = this.defaultIoAddress1;
        this.ioAddress2 = this.defaultIoAddress2;
        this.irqNumber = this.parent.getPic().requestIRQNumber(this.parent);
        if (this.irqNumber > -1) {
            logger.log(Level.CONFIG, this.parent.getType() + " -> IRQ number set to: " + irqNumber);
            this.parent.getPic().clearIRQ(irqNumber);
        } else {
            logger.log(Level.WARNING, this.parent.getType() + " -> Request of IRQ number failed.");
        }
        if (this.ioAddress1 != 0) {
            parent.getMotherboard().setIOPort(this.ioAddress1, this.parent);
            for (int address = 0x1; address <= 0x7; address++) {
                parent.getMotherboard().setIOPort(this.ioAddress1 + address, this.parent);
            }
        }
        if ((this.ioAddress2 != 0) && (this.ioAddress2 != 0x3f0)) {
            for (int address = 0x6; address <= 0x7; address++) {
                parent.getMotherboard().setIOPort(this.ioAddress2 + address, this.parent);
            }
        }
        selectedDriveIndex = IDE_MASTER_INDEX;
        for (int i = 0; i < this.drives.length; i++) {
            this.drives[i].reset();
        }
        return true;
    }

    public boolean isSelectedDrivePresent() {
        if (drives[selectedDriveIndex].getDriveType() == ATADriveType.NONE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Set a disk including disk image.
     * 
     * @param isMaster is the drive master?
     * @param drive the drive object to set
     */
    public void setDisk(ATADrive drive) {
        int driveIndex = this.getDriveIndex(drive);
        this.drives[driveIndex] = drive;
        return;
    }

    private int getDriveIndex(ATADrive drive) {
        int driveIndex;
        if (drive.isMaster) {
            driveIndex = 0;
        } else {
            driveIndex = 1;
        }
        return driveIndex;
    }

    /**
     * Get the drives.
     * 
     * @return the drives
     */
    public ATADrive[] getDrives() {
        return this.drives;
    }

    /**
     * Get the IO address 1.
     * 
     * @return the IO address 1
     */
    public int getIoAddress1() {
        return this.ioAddress1;
    }

    /**
     * Get the IO address 2
     * 
     * @return the IO address 2
     */
    public int getIoAddress2() {
        return ioAddress2;
    }

    public int getIrqNumber() {
        return this.irqNumber;
    }

    public ATADrive getSelectedDrive() {
        return this.drives[selectedDriveIndex];
    }

    public int getSelectedDriveIndex() {
        return this.selectedDriveIndex;
    }

    public void setSelectedDriveIndex(int theSelectedDriveIndex) {
        this.selectedDriveIndex = theSelectedDriveIndex;
    }

    public boolean isMasterDrivePresent() {
        boolean isMasterPresent = false;
        if (getDrives()[IDE_MASTER_INDEX] != null && getDrives()[IDE_MASTER_INDEX].getDriveType() != ATADriveType.NONE) {
            isMasterPresent = true;
        }
        return isMasterPresent;
    }

    public boolean isSlaveDrivePresent() {
        boolean isSlavePresent = false;
        if (getDrives()[IDE_SLAVE_INDEX] != null && getDrives()[IDE_SLAVE_INDEX].getDriveType() != ATADriveType.NONE) {
            isSlavePresent = true;
        }
        return isSlavePresent;
    }

    public boolean isAnyDrivePresent() {
        boolean isAnyDrivePresent = false;
        if (isMasterDrivePresent() || isSlaveDrivePresent()) {
            isAnyDrivePresent = true;
        }
        return isAnyDrivePresent;
    }

    public boolean isSlaveSelected() {
        return selectedDriveIndex == IDE_SLAVE_INDEX;
    }

    public boolean isMasterSelected() {
        return selectedDriveIndex == IDE_MASTER_INDEX;
    }
}
