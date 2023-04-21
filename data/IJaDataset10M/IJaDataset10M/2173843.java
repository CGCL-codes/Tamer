package br.com.jro.developer.tools.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import br.com.jro.developer.tools.MainCommand;
import static org.junit.Assert.*;

/**
 *
 * @author jairodealmeida
 */
public class MainTest {

    private static ArrayList<String[]> commands = new ArrayList<String[]>();

    public MainTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        commands.add("-h".split(" "));
        commands.add("-wkt file:/G:/workspace1/jro-shapefile-tools/src/test/java/br/com/jro/developer/tools/shapefile/calc_cont.shp".split(" "));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class Main.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        try {
            for (Iterator<String[]> it = commands.iterator(); it.hasNext(); ) {
                String[] args = it.next();
                MainCommand.main(args);
            }
        } catch (Exception e) {
            fail("woldn't pass here");
        }
    }
}
