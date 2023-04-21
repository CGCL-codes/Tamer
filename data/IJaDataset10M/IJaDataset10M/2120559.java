package net.sf.dict4j.command;

import net.sf.dict4j.enumeration.ResponseEnum;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class QuitCommandTest {

    private QuitCommand command;

    @Before
    public void setUp() {
        command = new QuitCommand();
    }

    /**
	 * Test of getResponseHeaderCode method, of class StatusCommand.
	 */
    @Test
    public void testGetResponseHeaderCode() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        ResponseEnum[] responses = command.getResponseHeaderCodes();
        assertTrue(responses.length == 1);
        assertEquals(responses[0], ResponseEnum.CLOSING_CONNECTION);
    }

    /**
	 * Test of getString method, of class StatusCommand.
	 */
    @Test
    public void testGetString() {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        assertEquals("QUIT", command.getString());
    }
}
