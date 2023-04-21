package xxl.core.io.raw;

/**
 * Allows user defined partitioning of a raw access. An object
 * of this class represents a consecutive part of a (usually) larger 
 * raw access.
 */
public class PartitionRawAccess implements RawAccess {

    /**
	 * Internally used raw access.
	 */
    private RawAccess ra;

    /**
	 * First sector of the raw access inside the raw access it is based on.
	 */
    private long startSector;

    /**
	 * Size of the raw access.
	 */
    private long size;

    /**
	 * Constructs a partition (raw access) inside a raw access.
	 * The partition has the same sector size with the base raw
	 * access.
	 * 
	 * @param ra base raw access
	 * @param startSector number of the start sector
	 * @param size size of the created partition in sectors
	 */
    public PartitionRawAccess(RawAccess ra, long startSector, long size) {
        this.ra = ra;
        this.startSector = startSector;
        this.size = size;
    }

    /**
	 * Opens the raw access (the base raw access already has to be open).
	 *
	 * @param filename name of the file (is not needed here).
 	 * @throws RawAccessException
	 */
    public void open(String filename) throws RawAccessException {
    }

    /**
	 * Close the raw access.
 	 * @throws RawAccessException
	 */
    public void close() throws RawAccessException {
    }

    /**
	 * Writes a block into the partition.
	 *
	 * @param block byte array of which will be written to the sector
	 * @param sector number of the sector in the file/device from where the block will be read
	 * @exception RawAccessException a specialized RuntimeException
	 */
    public void write(byte[] block, long sector) {
        if ((sector < 0) || (sector >= size)) throw new RawAccessException("Attempt to write outside boundary (" + sector + ", size of the Partition:" + size + ", startSector of the Partition:" + startSector + ")");
        ra.write(block, startSector + sector);
    }

    /**
	 * Reads a block from the partition.
	 *
	 * @param block byte array of which will be written to the sector
	 * @param sector number of the sector in the file/device from where the block will be read
	 * @exception RawAccessException a specialized RuntimeException
	 */
    public void read(byte[] block, long sector) throws RawAccessException {
        if ((sector < 0) || (sector >= size)) throw new RawAccessException("Attempt to read outside boundary (" + sector + ", length:" + size + ", startSector=" + startSector + ")");
        ra.read(block, startSector + sector);
    }

    /**
	 * Returns the size of the partition in sectors.
	 *
	 * @return amount of sectors
	 */
    public long getNumSectors() {
        return size;
    }

    /**
	 * Returns the size of each sector (is the same with the sector size of the
	 * base raw access).
	 *
	 * @return size of sectors
	 */
    public int getSectorSize() {
        return ra.getSectorSize();
    }

    /**
	 * Outputs a String representation of the raw device.
	 * @return A String representation.
	 */
    public String toString() {
        return "Partition raw access of: " + ra;
    }
}
