package lamao.soh.console;

import static org.junit.Assert.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import com.jme.input.KeyInput;
import com.jme.input.dummy.DummyKeyInput;
import com.jme.system.DisplaySystem;
import com.jme.system.dummy.DummySystemProvider;

/**
 * @author lamao
 *
 */
public class SHConsoleStateTest {

    static {
        Logger.getLogger("").setLevel(Level.OFF);
        DisplaySystem.setSystemProvider(new DummySystemProvider());
        KeyInput.setProvider(DummyKeyInput.class);
    }

    class SHDummyCommand extends SHBasicCommand {

        public String[] command;

        @Override
        protected void processCommand(String[] args) {
            command = args;
        }
    }

    private SHDummyCommand command = new SHDummyCommand();

    private SHConsoleState console;

    @Before
    public void setUp() {
        console = new SHConsoleState("console");
    }

    @Test
    public void testConstructors() {
        assertEquals(SHConsoleState.PROMT, console.getContents()[0]);
        assertTrue(console.getSupportedCommands().contains("exit"));
        assertTrue(console.getSupportedCommands().contains("echo"));
    }

    @Test
    public void testAddRemoveCommand() {
        console.add("a", command);
        assertEquals(3, console.getSupportedCommands().size());
        assertTrue(console.getSupportedCommands().contains("a"));
    }

    @Test
    public void testExitCommand() {
        console.setActive(true);
        console.execute("exit");
        assertFalse(console.isActive());
    }

    @Test
    public void testExecute() {
        console.execute("non-existent-command");
        assertEquals(SHConsoleState.PROMT, console.getContents()[0]);
        assertFalse(console.getContents()[1].isEmpty());
        assertTrue(console.getContents()[2].isEmpty());
        console.add("a", command);
        console.execute("a arg1 arg2");
        assertEquals("a", command.command[0]);
        assertEquals("arg1", command.command[1]);
        assertEquals("arg2", command.command[2]);
        command.command = null;
        console.add("aaaa", command);
        console.execute("aaaa arg1 arg2");
        assertEquals("aaaa", command.command[0]);
        assertEquals("arg1", command.command[1]);
        assertEquals("arg2", command.command[2]);
    }

    @Test
    public void testPrint() {
        String INFO = "info";
        String WARNING = "warning";
        String ERROR = "error";
        String MESSAGE = "message";
        console.info(INFO);
        assertEquals(SHConsoleState.PROMT, console.getContents()[0]);
        assertEquals(INFO, console.getContents()[1]);
        console.warning(WARNING);
        assertEquals(SHConsoleState.PROMT, console.getContents()[0]);
        assertEquals(WARNING, console.getContents()[1]);
        assertEquals(INFO, console.getContents()[2]);
        console.error(ERROR);
        assertEquals(SHConsoleState.PROMT, console.getContents()[0]);
        assertEquals(ERROR, console.getContents()[1]);
        assertEquals(WARNING, console.getContents()[2]);
        assertEquals(INFO, console.getContents()[3]);
        console.print(MESSAGE, SHConsoleState.DEFAULT_COLOR);
        assertEquals(SHConsoleState.PROMT, console.getContents()[0]);
        assertEquals(MESSAGE, console.getContents()[1]);
        assertEquals(ERROR, console.getContents()[2]);
        assertEquals(WARNING, console.getContents()[3]);
        assertEquals(INFO, console.getContents()[4]);
        for (int i = 0; i < 8; i++) {
            console.info("a");
        }
        String[] contents = console.getContents();
        for (int i = 1; i < 9; i++) {
            assertEquals("a", contents[i]);
        }
        assertEquals(MESSAGE, contents[9]);
    }

    @Test
    public void testSetActive() {
        console.onKey('a', KeyInput.KEY_A, true);
        assertEquals(SHConsoleState.PROMT, console.getContents()[0]);
        console.setActive(true);
        console.onKey('a', KeyInput.KEY_A, true);
        assertEquals(SHConsoleState.PROMT + "a", console.getContents()[0]);
        console.setActive(false);
        assertEquals(SHConsoleState.PROMT + "a", console.getContents()[0]);
        console.setActive(true);
        assertEquals(SHConsoleState.PROMT, console.getContents()[0]);
    }

    @Test
    public void testWalkingThroughHistory() {
        console.setActive(true);
        console.onKey('a', KeyInput.KEY_UP, true);
        console.onKey('a', KeyInput.KEY_DOWN, true);
    }
}
