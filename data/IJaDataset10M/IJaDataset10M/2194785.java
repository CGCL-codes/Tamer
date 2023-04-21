package dioscuri.module.fdc;

import dioscuri.exception.StorageDeviceException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Bram Lohman
 * @author Bart Kiers
 */
public class Drive {

    private int driveType;

    private boolean motorRunning;

    protected int eot;

    protected int hds;

    protected int cylinder;

    protected int sector;

    private Floppy floppy;

    protected int floppyType;

    protected int tracks;

    protected int heads;

    protected int cylinders;

    protected int sectorsPerTrack;

    protected int sectors;

    protected boolean writeProtected;

    protected boolean multiTrack;

    protected byte dir;

    private static enum FloppyType {

        TYPE_NONE((byte) 0x00, 0, 0, 0, 0, 0x00), TYPE_360K((byte) 0x01, 40, 2, 9, 720, 0x05), TYPE_1_2((byte) 0x02, 80, 2, 15, 2400, 0x04), TYPE_720K((byte) 0x03, 80, 2, 9, 1440, 0x1f), TYPE_1_44((byte) 0x04, 80, 2, 18, 2880, 0x18), TYPE_2_88((byte) 0x05, 80, 2, 36, 5760, 0x10), TYPE_160K((byte) 0x06, 40, 1, 8, 320, 0x05), TYPE_180K((byte) 0x07, 40, 1, 9, 360, 0x05), TYPE_320K((byte) 0x08, 40, 2, 8, 640, 0x05);

        private final byte id;

        private final int tracks;

        private final int heads;

        private final int sectorsPerTrack;

        private final int sectors;

        private final int value;

        private FloppyType(byte id, int tracks, int heads, int sectorsPerTrack, int sectors, int value) {
            this.id = id;
            this.tracks = tracks;
            this.heads = heads;
            this.sectorsPerTrack = sectorsPerTrack;
            this.sectors = sectors;
            this.value = value;
        }

        public byte getId() {
            return id;
        }

        public int getTracks() {
            return tracks;
        }

        public int getHeads() {
            return heads;
        }

        public int getSectorsPerTrack() {
            return sectorsPerTrack;
        }

        public int getSectors() {
            return sectors;
        }

        /**
         * @param floppyType
         * @return -
         */
        public static FloppyType fromId(byte id) {
            FloppyType result = TYPE_NONE;
            for (FloppyType type : values()) {
                if (type.getId() == id) {
                    result = type;
                    break;
                }
            }
            return result;
        }
    }

    ;

    /**
     * Drive
     */
    public Drive() {
        driveType = 0;
        eot = 0;
        hds = 0;
        cylinder = 0;
        sector = 0;
        floppy = null;
        floppyType = 0;
        tracks = 0;
        heads = 0;
        cylinders = 0;
        sectorsPerTrack = 0;
        sectors = 0;
        writeProtected = false;
        multiTrack = false;
        dir = (byte) 0x80;
    }

    /**
     * Reset drive All geometry parameters for sector selection are reset.
     *
     * @return -
     */
    protected boolean reset() {
        dir |= 0x80;
        eot = 0;
        hds = 0;
        cylinder = 0;
        sector = 0;
        return true;
    }

    /**
     * Checks the existence of a floppy disk in drive
     *
     * @return boolean true if drive contains a floppy, false otherwise
     */
    protected boolean containsFloppy() {
        return (floppy != null);
    }

    /**
     * Get type of drive
     *
     * @return int drive type
     */
    protected int getDriveType() {
        return driveType;
    }

    /**
     * Set type of drive
     *
     * @param type
     */
    protected void setDriveType(byte type) {
        driveType = type;
    }

    /**
     * Set motor status
     *
     * @param state
     */
    protected void setMotor(boolean state) {
        motorRunning = state;
    }

    /**
     * Returns the state of the drive motor
     *
     * @return boolean true if motor is running, false otherwise
     */
    protected boolean isMotorRunning() {
        return motorRunning;
    }

    /**
     * Get type of floppy inserted
     *
     * @return int type of floppy
     */
    protected int getFloppyType() {
        return floppyType;
    }

    /**
     * Get size in bytes of floppy Returns -1 if drive is empty.
     *
     * @return int size of floppy, or -1 if no floppy available
     */
    protected int getFloppySize() {
        if (this.containsFloppy()) {
            return floppy.getSize();
        }
        return -1;
    }

    /**
     * Inserts a floppy into the drive
     *
     * @param floppyType
     * @param imageFile
     * @param writeProtected
     * @throws StorageDeviceException
     */
    protected void insertFloppy(byte floppyType, File imageFile, boolean writeProtected) throws StorageDeviceException {
        try {
            floppy = new Floppy(floppyType, imageFile);
            this.floppyType = floppyType;
            if (floppy.getSize() <= 1474560) {
                FloppyType type = FloppyType.fromId(floppyType);
                tracks = type.getTracks();
                heads = type.getHeads();
                sectorsPerTrack = type.getSectorsPerTrack();
            } else if (floppy.getSize() == 1720320) {
                tracks = 80;
                heads = 2;
                sectorsPerTrack = 21;
            } else if (floppy.getSize() == 1763328) {
                tracks = 82;
                heads = 2;
                sectorsPerTrack = 21;
            } else if (floppy.getSize() == 1884160) {
                tracks = 80;
                heads = 2;
                sectorsPerTrack = 23;
            }
            sectors = heads * tracks * sectorsPerTrack;
            if (floppy.getSize() > (sectors * 512)) {
                throw new StorageDeviceException("Error: size of file too large for selected type");
            }
            this.writeProtected = writeProtected;
        } catch (IOException e) {
            throw new StorageDeviceException("Floppy could not be inserted.");
        }
    }

    /**
     * Ejects a floppy from the drive
     *
     * @throws StorageDeviceException
     */
    protected void ejectFloppy() throws StorageDeviceException {
        try {
            floppy.storeImageToFile();
        } catch (IOException e) {
            throw new StorageDeviceException("Floppy data could not be stored to disk image. Data may be lost.");
        } finally {
            floppy = null;
            driveType = 0;
            floppyType = 0;
            tracks = 0;
            heads = 0;
            sectorsPerTrack = 0;
            sectors = 0;
            writeProtected = false;
            this.resetChangeline();
        }
    }

    /**
     * Read data from floppy into buffer
     *
     * @param offset
     * @param totalBytes
     * @param floppyBuffer
     * @throws StorageDeviceException
     */
    protected void readData(int offset, int totalBytes, byte[] floppyBuffer) throws StorageDeviceException {
        if (this.containsFloppy()) {
            try {
                System.arraycopy(floppy.bytes, offset, floppyBuffer, 0, totalBytes);
            } catch (ArrayIndexOutOfBoundsException e1) {
                if (offset < floppy.bytes.length) {
                    int partialBytes = floppy.bytes.length - offset;
                    System.arraycopy(floppy.bytes, offset, floppyBuffer, 0, partialBytes);
                    Arrays.fill(floppyBuffer, partialBytes, totalBytes, (byte) 0);
                } else {
                    Arrays.fill(floppyBuffer, 0, totalBytes, (byte) 0);
                }
            }
        } else {
            throw new StorageDeviceException("Error: drive does not contain a floppy");
        }
    }

    /**
     * Write data to floppy from buffer
     *
     * @param offset
     * @param totalBytes
     * @param floppyBuffer
     * @throws StorageDeviceException
     */
    protected void writeData(int offset, int totalBytes, byte[] floppyBuffer) throws StorageDeviceException {
        if (this.containsFloppy() && !writeProtected) {
            System.arraycopy(floppyBuffer, 0, floppy.bytes, offset, totalBytes);
        } else {
            throw new StorageDeviceException("Error: drive does not contain a floppy or is write protected");
        }
    }

    /**
     * Increment current sector Note: also takes care of multitrack disks and
     * cylinder position
     */
    protected void incrementSector() {
        sector++;
        if ((sector > eot) || (sector > sectorsPerTrack)) {
            sector = 1;
            if (multiTrack) {
                hds++;
                if (hds > 1) {
                    hds = 0;
                    cylinder++;
                    this.resetChangeline();
                }
            } else {
                cylinder++;
                this.resetChangeline();
            }
            if (cylinder >= tracks) {
                cylinder = tracks;
            }
        }
    }

    /**
     * Reset change line Updates DIR on bit 7
     */
    protected void resetChangeline() {
        if (this.containsFloppy()) {
            dir = (byte) (dir & ~0x80);
        }
    }

    /**
     * Get String representation of this class
     */
    @Override
    public String toString() {
        String driveInfo, floppyInfo, ret, tab;
        ret = "\r\n";
        tab = "\t";
        driveInfo = "drivetype=" + driveType + ", motorRunning=" + motorRunning + ", eot=" + eot + ", hds=" + hds + ", cylinder=" + cylinder + ", sector=" + sector;
        floppyInfo = "floppy=";
        if (this.containsFloppy()) {
            floppyInfo += "inserted" + ", floppytype=" + floppyType + ", floppysize=" + floppy.getSize() + ", tracks=" + tracks + ", heads=" + heads + ", cylinders=" + cylinders + ", sectorsPerTrack=" + sectorsPerTrack + ", sectors=" + sectors + ", writeProtected=" + writeProtected + ", multiTrack=" + multiTrack;
        } else {
            floppyInfo += "none";
        }
        return driveInfo + ret + tab + tab + floppyInfo;
    }
}
