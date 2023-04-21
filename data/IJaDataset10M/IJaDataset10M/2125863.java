package fi.cleancode.jfileoperator.fileoperator;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import fi.cleancode.jfileoperator.fileoperator.BasicFileOperator;

public class BasicFileOperatorCreationTests {

    private static final String REAL_FILE = "src/test/resources/REAL_FILE";

    private BasicFileOperator operator;

    @Before
    public void setup() throws IOException {
        File fileToOperate = new File(REAL_FILE);
        if (!fileToOperate.exists()) fileToOperate.createNewFile();
    }

    @After
    public void after() {
        File fileToOperate = new File(REAL_FILE);
        if (fileToOperate.exists()) fileToOperate.delete();
    }

    @Test
    public void isCreatedWithRealFile() {
        File realFile = new File(REAL_FILE);
        operator = new BasicFileOperator(realFile);
        assertNotNull(operator);
    }
}
