package org.mobicents.ss7.management.console;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.testng.*;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.mobicents.ss7.management.console.Console;
import org.mobicents.ss7.management.console.ConsoleListener;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ConsoleTest {

    ByteArrayInputStream in = null;

    ByteArrayOutputStream out = null;

    Console console = null;

    ConsoleListener listener = null;

    public ConsoleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testConsole() throws IOException {
        String message = "This is some message";
        in = new ByteArrayInputStream(message.getBytes());
        out = new ByteArrayOutputStream();
        listener = new TestConsoleListener(out);
        console = new Console(in, out, listener, null);
        console.start();
        assertEquals(message, new String(out.toByteArray()));
    }

    private class TestConsoleListener implements ConsoleListener {

        ByteArrayOutputStream out;

        Console console;

        TestConsoleListener(ByteArrayOutputStream out) {
            this.out = out;
        }

        public void commandEntered(String consoleInput) {
            System.out.println(consoleInput);
            this.console.write(consoleInput);
        }

        public void setConsole(Console console) {
            this.console = console;
        }
    }
}
