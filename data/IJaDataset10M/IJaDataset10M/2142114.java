package au.com.bytecode.opencsv;

import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static org.junit.Assert.assertTrue;

public class OpencsvTest {

    private File tempFile = null;

    private CSVWriter writer = null;

    private CSVReader reader = null;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("csvWriterTest", ".csv");
        tempFile.deleteOnExit();
    }

    /**
	 * Test the full cycle of write-read
	 * 
	 */
    @Test
    public void testWriteRead() throws IOException {
        final String[][] data = new String[][] { { "hello, a test", "one nested \" test" }, { "\"\"", "test", null, "8" } };
        writer = new CSVWriter(new FileWriter(tempFile));
        for (String[] aData : data) {
            writer.writeNext(aData);
        }
        writer.close();
        reader = new CSVReader(new FileReader(tempFile));
        String[] line;
        for (int row = 0; (line = reader.readNext()) != null; row++) {
            assertTrue(line.length == data[row].length);
            for (int col = 0; col < line.length; col++) {
                if (data[row][col] == null) {
                    assertTrue(line[col].equals(""));
                } else {
                    assertTrue(line[col].equals(data[row][col]));
                }
            }
        }
        reader.close();
    }
}
