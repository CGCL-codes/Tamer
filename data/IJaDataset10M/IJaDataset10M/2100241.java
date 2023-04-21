package com.google.gwt.dev.javac;

import com.google.gwt.dev.util.Util;
import junit.framework.TestCase;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class CompiledClassTest extends TestCase {

    static byte[] dummyByteCode = { (byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF };

    static final String DUMMY_NAME = "com/example/DeadBeef";

    /**
   * Test for {@link CompiledClass#getSourceName()}.
   */
    public void testSourceName() throws Exception {
        CompiledClass compiledClass = new CompiledClass(dummyByteCode, null, false, DUMMY_NAME);
        assertEquals("com.example.DeadBeef", compiledClass.getSourceName());
    }

    public void testCompiledClassSerialization() throws Exception {
        CompiledClass writeObject = new CompiledClass(dummyByteCode, null, false, DUMMY_NAME);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Util.writeObjectToStream(outputStream, writeObject);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CompiledClass readObject = Util.readStreamAsObject(inputStream, CompiledClass.class);
        assertEquals(4, readObject.getBytes().length);
        byte[] readBytes = readObject.getBytes();
        for (int i = 0; i < 4; ++i) {
            assertEquals(dummyByteCode[i], readBytes[i]);
        }
    }
}
