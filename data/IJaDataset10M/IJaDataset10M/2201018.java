package be.lassi.lanbox.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests class <code>CommandFactory</code>.
 */
public class CommandFactoryTestCase {

    @Test
    public void testCommand() {
        Command command = new CommandFactory().get("*6500#".getBytes());
        assertEquals(Common16BitMode.class, command.getClass());
    }

    @Test
    public void testUnknownCommand() {
        Command command = new CommandFactory().get("*@@#".getBytes());
        assertEquals(UnknownCommand.class, command.getClass());
        assertEquals("*@@#", command.getRequestString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestTooShort() {
        new CommandFactory().get("*#".getBytes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStartCharacter() {
        new CommandFactory().get("?00#".getBytes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEndCharacter() {
        new CommandFactory().get("*00?".getBytes());
    }
}
