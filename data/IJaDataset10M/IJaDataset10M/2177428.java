package junittests;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.sourceforge.webcompmath.draw.TangentLine;
import junit.framework.TestCase;

/**
 * @author PKHG
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewLineWidth extends TestCase {

    FrameForDrawTests frame;

    private TangentLine tangent;

    protected void setUp() throws Exception {
        super.setUp();
        frame = new FrameForDrawTests();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        int a;
        String line = " ";
        BufferedReader br;
        br = new java.io.BufferedReader(new InputStreamReader(System.in));
        while (line.equals(" ")) {
            System.out.println("Type something here to stop this test");
            line = br.readLine();
        }
        System.out.println("This test is finished!");
    }

    /**
	 * Constructor for NewLineWidth.
	 * 
	 * @param arg0
	 */
    public NewLineWidth(String arg0) {
        super(arg0);
    }

    /**
	 * always the last setting is visible! pkhg 0504025
	 */
    public void testSetLineWidth() {
        assertNotNull(frame);
        tangent = frame.getTangent();
        tangent.setColor(Color.GREEN);
        assertEquals(Color.GREEN, tangent.getColor());
        tangent.setLineWidth(52);
        assertEquals(10, tangent.getLineWidth());
        tangent.setLineWidth(-652);
        assertEquals(0, tangent.getLineWidth());
        tangent.setLineWidth(5);
        assertEquals(5, tangent.getLineWidth());
        assertEquals(5, tangent.getShape());
        tangent.setShape(1);
        assertEquals(1, tangent.getShape());
    }
}
