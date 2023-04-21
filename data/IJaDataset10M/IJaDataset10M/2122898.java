package CH.ifa.draw.test.contrib;

import java.awt.Font;
import CH.ifa.draw.contrib.TextAreaFigure;
import junit.framework.TestCase;

public class TextAreaFigureTest extends TestCase {

    private TextAreaFigure textareafigure;

    /**
	 * Constructor TextAreaFigureTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
    public TextAreaFigureTest(String name) {
        super(name);
    }

    /**
	 * Factory method for instances of the class to be tested.
	 */
    public CH.ifa.draw.contrib.TextAreaFigure createInstance() throws Exception {
        return new CH.ifa.draw.contrib.TextAreaFigure();
    }

    /**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        textareafigure = createInstance();
    }

    /**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
    protected void tearDown() throws Exception {
        textareafigure = null;
        super.tearDown();
    }

    public void testSetGetText() throws Exception {
        java.lang.String[] tests = { "", " ", "a", "A", "?", "?", "0123456789", "012345678901234567890", "\n", null };
        for (int i = 0; i < tests.length; i++) {
            textareafigure.setText(tests[i]);
            assertEquals(tests[i], textareafigure.getText());
        }
    }

    public void testTextDisplayBox() throws Exception {
    }

    public void testCreateFont() throws Exception {
    }

    public void testSetIsReadOnly() throws Exception {
        boolean[] tests = { true, false };
        for (int i = 0; i < tests.length; i++) {
            textareafigure.setReadOnly(tests[i]);
            assertEquals(tests[i], textareafigure.isReadOnly());
        }
    }

    public void testAcceptsTyping() throws Exception {
    }

    public void testIsTextDirty() throws Exception {
    }

    public void testSetIsSizeDirty() throws Exception {
        boolean[] tests = { true, false };
        for (int i = 0; i < tests.length; i++) {
            textareafigure.setSizeDirty(tests[i]);
            assertEquals(tests[i], textareafigure.isSizeDirty());
        }
    }

    public void testSetGetFont() throws Exception {
        Font[] tests = { new Font("Helvetica", Font.PLAIN, 12) };
        for (int i = 0; i < tests.length; i++) {
            textareafigure.setFont(tests[i]);
            assertEquals(tests[i], textareafigure.getFont());
        }
    }

    /**
	 * Test a null argument to setFont.		Expect an IllegalArgumentException
	 * 
	 * @see CH.ifa.draw.contrib.TextAreaFigure#setFont(java.awt.Font)
	 */
    public void testSetNullFont() throws Exception {
        Font original = textareafigure.getFont();
        try {
            textareafigure.setFont(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ok) {
            assertEquals("setFont(null) altered property value", original, textareafigure.getFont());
        }
    }

    public void testOverlayColumns() throws Exception {
    }

    public void testBasicDisplayBox() throws Exception {
    }

    public void testHandles() throws Exception {
    }

    public void testDisplayBox() throws Exception {
    }

    public void testMoveBy() throws Exception {
    }

    public void testDrawBackground() throws Exception {
    }

    public void testDraw() throws Exception {
    }

    public void testDrawFrame() throws Exception {
    }

    public void testGetAttribute() throws Exception {
    }

    public void testSetAttribute() throws Exception {
    }

    public void testWrite() throws Exception {
    }

    public void testRead() throws Exception {
    }

    public void testConnect() throws Exception {
    }

    public void testDisconnect() throws Exception {
    }

    public void testFigureInvalidated() throws Exception {
    }

    public void testFigureChanged() throws Exception {
    }

    public void testFigureRemoved() throws Exception {
    }

    public void testFigureRequestRemove() throws Exception {
    }

    public void testFigureRequestUpdate() throws Exception {
    }

    public void testGetTextColor() throws Exception {
    }

    public void testIsEmpty() throws Exception {
    }

    public void testSetIsFontDirty() throws Exception {
        boolean[] tests = { true, false };
        for (int i = 0; i < tests.length; i++) {
            textareafigure.setFontDirty(tests[i]);
            assertEquals(tests[i], textareafigure.isFontDirty());
        }
    }

    public void testGetRepresentingFigure() throws Exception {
    }

    public void testVault() throws Exception {
    }
}
