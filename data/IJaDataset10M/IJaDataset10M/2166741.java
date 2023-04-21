package org.jazzteam.edu.junit4;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SaveTxtFileTest {

    SaveTxtFile savefile = null;

    @Before
    public void setUp() throws Exception {
        savefile = new SaveTxtFile();
    }

    @After
    public void tearDown() {
        File tempFile = new File(System.getProperty("java.io.tmpdir") + "test.txt");
        tempFile.delete();
    }

    @Test
    public void testSaveFile() throws IOException {
        String lineToSave = "�������� ����������.";
        String expectedResult = "�������� ����������.";
        String readResult = "";
        savefile.entryStringAndWriteFile(lineToSave);
        readResult = savefile.readingStringInFile();
        assertEquals(lineToSave, readResult);
        assertEquals(expectedResult, readResult);
    }
}
