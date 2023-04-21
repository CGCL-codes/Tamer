package j8086emu.model.hardware.ram;

import j8086emu.model.hardware.CPU;
import j8086emu.model.utils.RamVizualizer;
import org.junit.*;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author sone
 */
public class StackTest {

    public StackTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
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
     * Test of setRamDataInSegment method, of class Ram.
     */
    @Test
    public void testStack() throws Exception {
        System.out.println("Stack");
        CPU cpu = new CPU();
        cpu.setRegData("CS", 512);
        cpu.setRegData("SP", 0xfff0);
        cpu.setRamData(cpu.getRegData("CS") * 16 + 0xfff3, 24);
        RamVizualizer rv = new RamVizualizer(cpu);
        String stack = rv.getStack();
        System.out.println("--" + stack);
        assertTrue(stack.equals("0000 0180 0000 0000 0000 0000 0000 "));
    }
}
