package org.t2framework.commons.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.t2framework.commons.util.using;
import junit.framework.TestCase;

public class UsingTest extends TestCase {

    public void test1() throws Exception {
        try {
            new using<Closeable, Throwable>() {

                @Override
                public Closeable connect() throws Throwable {
                    return new FileInputStream(new File("hogehoge"));
                }

                @Override
                public void handle(Closeable stream) throws Throwable {
                    fail();
                }

                @Override
                public void handleError(Throwable throwable) {
                    assertEquals(FileNotFoundException.class, throwable.getClass());
                    throw new NullPointerException();
                }
            };
            fail();
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
}
