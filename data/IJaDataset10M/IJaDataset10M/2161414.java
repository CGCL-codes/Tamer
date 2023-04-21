package au.edu.uq.itee.maenad.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;
import static org.junit.Assert.*;

public class WrappingReaderTest {

    @Test
    public void testRead() throws Exception {
        runTestCase("prologue", "CONTENT", "epilogue");
        runTestCase("prologue1", "CONTENT", "epilogue");
        runTestCase("prologue12", "CONTENT", "epilogue");
        runTestCase("", "CONTENT", "epilogue");
        runTestCase("prologue12", "", "epilogue");
        runTestCase("prologue12", "CONTENT", "");
    }

    private void runTestCase(String prologue, String content, String epilogue) throws IOException {
        String expected = prologue + content + epilogue;
        Reader reader = new WrappingReader(new StringReader(content), prologue, epilogue);
        int pos = 0;
        char[] target = new char[expected.length()];
        while (reader.read(target, pos, Math.min(3, target.length - pos)) == 3) {
            pos += 3;
        }
        assertEquals(-1, reader.read());
        assertEquals(expected, new String(target));
    }
}
