package org.objectstyle.cayenne;

import junit.framework.TestCase;
import org.apache.log4j.Logger;

/**
 * @author Andrei Adamchik
 */
public class CayenneExceptionTst extends TestCase {

    static final Logger logObj = Logger.getLogger(CayenneExceptionTst.class);

    public void testConstructor1() throws Exception {
        CayenneException ex = new CayenneException();
        assertNull(ex.getCause());
        assertTrue(ex.getMessage().startsWith(CayenneException.getExceptionLabel()));
    }

    public void testConstructor2() throws Exception {
        CayenneException ex = new CayenneException("abc");
        assertNull(ex.getCause());
        assertEquals(CayenneException.getExceptionLabel() + "abc", ex.getMessage());
    }

    public void testConstructor3() throws Exception {
        Throwable cause = new Throwable();
        CayenneException ex = new CayenneException(cause);
        assertSame(cause, ex.getCause());
        assertEquals(CayenneException.getExceptionLabel() + cause.toString(), ex.getMessage());
    }

    public void testConstructor4() throws Exception {
        Throwable cause = new Throwable();
        CayenneException ex = new CayenneException("abc", cause);
        assertSame(cause, ex.getCause());
        assertEquals(CayenneException.getExceptionLabel() + "abc", ex.getMessage());
    }
}
