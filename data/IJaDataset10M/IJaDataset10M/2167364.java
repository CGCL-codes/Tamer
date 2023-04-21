package edu.hawaii.halealohacli.test;

import static org.junit.Assert.assertSame;
import org.junit.Test;
import edu.hawaii.halealohacli.command.Quit;

/**
 * Tests the Quit class.
 * 
 * @author Team Pichu
 */
public class TestQuit {

    /**
   * Tests if the quit command returns "quit".
   * 
   * @throws Exception Doesn't really throw an exception.
   */
    @Test
    public void testQuit() throws Exception {
        Quit quit = new Quit();
        quit.run();
        assertSame("Testing quit", quit.getOutput(), "quit");
    }
}
