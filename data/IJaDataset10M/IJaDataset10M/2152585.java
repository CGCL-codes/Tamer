package org.jscsi.scsi.tasks.buffered;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.nio.ByteBuffer;
import java.util.Random;
import org.apache.log4j.Logger;
import org.jscsi.scsi.protocol.cdb.CDB;
import org.jscsi.scsi.protocol.cdb.Write10;
import org.jscsi.scsi.protocol.cdb.Write12;
import org.jscsi.scsi.protocol.cdb.Write16;
import org.jscsi.scsi.protocol.cdb.Write6;
import org.junit.Test;

public class BufferedWriteTaskTest extends BufferTestTask {

    private static Logger _logger = Logger.getLogger(BufferedWriteTaskTest.class);

    private static final int WRITE_BLOCKS = 10;

    private static int cmdRef = 0;

    private Random rnd = new Random();

    @Test
    public void testWrite6inMemory() {
        _logger.debug("********** WRITE6 MEMORY **********");
        int lba = generateRandomLBA();
        CDB cdb = new Write6(false, true, lba, WRITE_BLOCKS);
        ByteBuffer data = this.createReadData(WRITE_BLOCKS * STORE_BLOCK_SIZE, cmdRef);
        this.submitMemoryTask(cdb, cmdRef);
        verifyDeviceBuffer(data, this.getMemoryBuffer(), lba);
        this.purgeReadData(cmdRef);
        this.purgeDeviceData();
        cmdRef++;
    }

    @Test
    public void testWrite6inFile() {
        _logger.debug("********** WRITE6 FILE **********");
        int lba = generateRandomLBA();
        CDB cdb = new Write6(false, true, lba, WRITE_BLOCKS);
        ByteBuffer data = this.createReadData(WRITE_BLOCKS * STORE_BLOCK_SIZE, cmdRef);
        this.submitFileTask(cdb, cmdRef);
        verifyDeviceBuffer(data, this.getFileBuffer(), lba);
        this.purgeReadData(cmdRef);
        this.purgeDeviceData();
        cmdRef++;
    }

    @Test
    public void testWrite10inMemory() {
        _logger.debug("********** WRITE10 MEMORY **********");
        int lba = generateRandomLBA();
        CDB cdb = new Write10(0, false, false, false, false, false, lba, WRITE_BLOCKS);
        ByteBuffer data = this.createReadData(WRITE_BLOCKS * STORE_BLOCK_SIZE, cmdRef);
        this.submitMemoryTask(cdb, cmdRef);
        verifyDeviceBuffer(data, this.getMemoryBuffer(), lba);
        this.purgeReadData(cmdRef);
        this.purgeDeviceData();
        cmdRef++;
    }

    @Test
    public void testWrite10inFile() {
        _logger.debug("********** WRITE10 FILE **********");
        int lba = generateRandomLBA();
        CDB cdb = new Write10(0, false, false, false, false, false, lba, WRITE_BLOCKS);
        ByteBuffer data = this.createReadData(WRITE_BLOCKS * STORE_BLOCK_SIZE, cmdRef);
        this.submitFileTask(cdb, cmdRef);
        verifyDeviceBuffer(data, this.getFileBuffer(), lba);
        this.purgeReadData(cmdRef);
        this.purgeDeviceData();
        cmdRef++;
    }

    @Test
    public void testWrite12InMemory() {
        _logger.debug("********** WRITE12 MEMORY **********");
        int lba = generateRandomLBA();
        CDB cdb = new Write12(0, false, false, false, false, false, lba, WRITE_BLOCKS);
        ByteBuffer data = this.createReadData(WRITE_BLOCKS * STORE_BLOCK_SIZE, cmdRef);
        this.submitMemoryTask(cdb, cmdRef);
        verifyDeviceBuffer(data, this.getMemoryBuffer(), lba);
        this.purgeReadData(cmdRef);
        this.purgeDeviceData();
        cmdRef++;
    }

    @Test
    public void testWrite12InFile() {
        _logger.debug("********** WRITE12 FILE **********");
        int lba = generateRandomLBA();
        CDB cdb = new Write12(0, false, false, false, false, false, lba, WRITE_BLOCKS);
        ByteBuffer data = this.createReadData(WRITE_BLOCKS * STORE_BLOCK_SIZE, cmdRef);
        this.submitFileTask(cdb, cmdRef);
        verifyDeviceBuffer(data, this.getFileBuffer(), lba);
        this.purgeReadData(cmdRef);
        this.purgeDeviceData();
        cmdRef++;
    }

    @Test
    public void testWrite16inMemory() {
        _logger.debug("********** WRITE16 MEMORY **********");
        int lba = generateRandomLBA();
        CDB cdb = new Write16(0, false, false, false, false, false, lba, WRITE_BLOCKS);
        ByteBuffer data = this.createReadData(WRITE_BLOCKS * STORE_BLOCK_SIZE, cmdRef);
        this.submitMemoryTask(cdb, cmdRef);
        verifyDeviceBuffer(data, this.getMemoryBuffer(), lba);
        this.purgeReadData(cmdRef);
        this.purgeDeviceData();
        cmdRef++;
    }

    @Test
    public void testWrite16inFile() {
        _logger.debug("********** WRITE16 FILE **********");
        int lba = generateRandomLBA();
        CDB cdb = new Write16(0, false, false, false, false, false, lba, WRITE_BLOCKS);
        ByteBuffer data = this.createReadData(WRITE_BLOCKS * STORE_BLOCK_SIZE, cmdRef);
        this.submitFileTask(cdb, cmdRef);
        verifyDeviceBuffer(data, this.getFileBuffer(), lba);
        this.purgeReadData(cmdRef);
        this.purgeDeviceData();
        cmdRef++;
    }

    /**
    * Verify that the data was properly placed into the device's buffer
    * 
    * @param data The data written to the device
    */
    private void verifyDeviceBuffer(ByteBuffer writtenData, ByteBuffer deviceData, int lba) {
        final int bytesWritten = WRITE_BLOCKS * STORE_BLOCK_SIZE;
        assertNotNull(deviceData);
        assertNotNull(writtenData);
        deviceData.rewind();
        writtenData.rewind();
        for (int i = 0; i < STORE_CAPACITY; i++) {
            byte d = deviceData.get();
            if (i < (lba * STORE_BLOCK_SIZE)) {
                assertEquals(0, d);
            } else if (i < (lba * STORE_BLOCK_SIZE) + bytesWritten) {
                byte w = writtenData.get();
                assertEquals(w, d);
            } else {
                assertEquals(0, d);
            }
        }
    }

    private int generateRandomLBA() {
        return Math.abs(rnd.nextInt()) % ((STORE_CAPACITY / STORE_BLOCK_SIZE) - WRITE_BLOCKS);
    }
}
