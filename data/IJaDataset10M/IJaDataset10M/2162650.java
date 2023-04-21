package org.junithelper.core.util;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

public class UniversalDetectorUtilTest {

    @Test
    public void type() throws Exception {
        assertNotNull(UniversalDetectorUtil.class);
    }

    @Test
    public void getDetectedEncoding_A$InputStream_UTF8() throws Exception {
        String name = "UTF-8.txt";
        InputStream is = IOUtil.getResourceAsStream(name);
        String actual = UniversalDetectorUtil.getDetectedEncoding(is);
        String expected = "UTF-8";
        assertEquals(expected, actual);
    }

    @Test
    public void getDetectedEncoding_A$InputStream_Shift_JIS() throws Exception {
        String name = "Shift_JIS.txt";
        InputStream is = IOUtil.getResourceAsStream(name);
        String actual = UniversalDetectorUtil.getDetectedEncoding(is);
        String expected = "SHIFT_JIS";
        assertEquals(expected, actual);
    }

    @Test
    public void getDetectedEncoding_A$InputStream_T$IOException() throws Exception {
        InputStream is = mock(InputStream.class);
        when(is.read(any(byte[].class))).thenThrow(new IOException());
        try {
            UniversalDetectorUtil.getDetectedEncoding(is);
            fail("Expected exception was not thrown!");
        } catch (IOException e) {
        }
    }

    @Test
    public void getDetectedEncoding_A$File() throws Exception {
        File file = new File("release/junithelper-config.properties");
        String actual = UniversalDetectorUtil.getDetectedEncoding(file);
        String expected = null;
        assertEquals(expected, actual);
    }

    @Test
    public void getDetectedEncoding_A$File_T$IOException() throws Exception {
        File file = mock(File.class);
        try {
            UniversalDetectorUtil.getDetectedEncoding(file);
            fail("Expected exception was not thrown!");
        } catch (IOException e) {
        }
    }
}
