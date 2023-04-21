package nl.knaw.dans.common.dataperfect;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.io.File;
import java.io.IOException;

/**
 * @author Jan van Mansum
 */
public class TestDataFile {

    @Test
    public void shouldReadPackedValue() throws IOException {
        final DataFile dataFile = new DataFile(new File("src/test/resources/DP26G/PACKED/PACPAN"), 0, DefaultTestDatabaseSettings.INSTANCE);
        try {
            dataFile.open();
            dataFile.skipBytes(32);
            assertEquals(12345678901L, dataFile.readPackedNumber());
            assertEquals(123456789012L, dataFile.readPackedNumber());
        } finally {
            dataFile.close();
        }
    }

    @Test
    public void shouldReadAllTypes() throws IOException {
        final DataFile dataFile = new DataFile(new File("src/test/resources/DP26G/TYPES/PANEL"), 0, DefaultTestDatabaseSettings.INSTANCE);
        try {
            dataFile.open();
            dataFile.skipBytes(32);
            assertEquals("String value             ", dataFile.readString(25));
            assertEquals(3, dataFile.readBlockNumber());
            assertEquals(1234, dataFile.readInteger());
            assertEquals(123456789012L, dataFile.readPackedNumber());
            assertEquals(-1234, dataFile.readInteger());
            assertEquals(-123456789012L, dataFile.readPackedNumber());
        } finally {
            dataFile.close();
        }
    }
}
