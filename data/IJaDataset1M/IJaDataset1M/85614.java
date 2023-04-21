package net.sf.dict4j.command;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AbstractCommandTest {

    /**
	 * Test of stripQuotes method, of class AbstractCommand.
	 */
    @Test
    public void testStripQuotes() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        assertEquals("test", AbstractCommand.stripQuotes("\"test\""));
        assertEquals("\"test\"", AbstractCommand.stripQuotes("\"\"test\"\""));
    }
}
