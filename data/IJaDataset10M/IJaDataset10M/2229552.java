package eu.funcnet.funcnet_spindlep.utils;

import java.io.File;
import java.io.FileReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StringListFromFileTest {

    private final File _file = new File(this.getClass().getClassLoader().getResource("test.txt").getFile());

    @Test
    public void testStringListReadsRightNumberOfLinesFromFile() throws Exception {
        final StringListFromFile slff = new StringListFromFile();
        slff.populate(_file);
        assertEquals(3, slff.size());
    }

    @Test
    public void testStringListReadsRightNumberOfLinesFromReader() throws Exception {
        final StringListFromFile slff = new StringListFromFile();
        slff.populate(new FileReader(_file));
        assertEquals(3, slff.size());
    }
}
