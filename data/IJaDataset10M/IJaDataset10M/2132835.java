package org.jd3lib.archoslib;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import junit.framework.TestCase;

/**
 * @author Grunewald
 */
public class HeaderTest extends TestCase {

    public void testToOutStream() {
        FileOutputStream tag;
        try {
            tag = new FileOutputStream("test_data/archos/jml.bin");
            tag.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
