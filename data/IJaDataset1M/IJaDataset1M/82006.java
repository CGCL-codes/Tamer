package org.translationcomponent.api.impl.response.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.translationcomponent.api.impl.response.ResponseStateOk;

public class StorageStringWriterTest extends TestCase {

    public void test() throws Exception {
        StorageStringWriter s = new StorageStringWriter(2048, "UTF-8");
        s.addText("Test");
        try {
            s.getOutputStream();
            fail("Should throw IOException as method not supported.");
        } catch (IOException e) {
        }
        s.getWriter().write("ing is important");
        s.close(ResponseStateOk.getInstance());
        assertEquals("Testing is important", s.getText());
        InputStream input = s.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(input, writer, "UTF-8");
        assertEquals("Testing is important", writer.toString());
        try {
            s.getWriter();
            fail("Should throw IOException as storage is closed.");
        } catch (IOException e) {
        }
    }
}
