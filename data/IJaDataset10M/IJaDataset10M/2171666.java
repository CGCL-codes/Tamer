package com.steadystate.css.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.Locator;

/**
 * Testcases.
 */
public class LocatorImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void serializeTest() throws Exception {
        final Locator locator = new LocatorImpl("uri", 1, 2);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(locator);
        oos.flush();
        oos.close();
        final byte[] bytes = baos.toByteArray();
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        final Object o = ois.readObject();
        Assert.assertEquals(locator, o);
    }
}
