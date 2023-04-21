package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.JTextField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import riaf.controller.RiafMgr;
import riaf.facade.IInfo;

@RunWith(Parameterized.class)
public class RInfoTest extends RiafParameterizedTestCase {

    IInfo input;

    boolean isReadValuesTested = false;

    public RInfoTest(String value, String expected) {
        super(value, expected);
    }

    @Parameters
    public static Collection<String[]> data() {
        return Arrays.asList(new String[][] { { null, "" }, { "~1", "~1" }, { "'", "'" }, { "", "" }, { "translation", "test" }, { "test", "test" }, { String.valueOf(true), "true" }, { "m<br>n", "<html><tt>m<br>n</html>" } });
    }

    @Before
    public void init() {
        input = (IInfo) RiafMgr.global().searchPageHolder("main").getContainer().searchComponent("test_info");
        assertNotNull(input);
    }

    @Test
    public void testReadValues() throws Exception {
        if (!isReadValuesTested) {
            String implContent = ((JTextField) input.getImpl()).getText();
            assertEquals("my test info", implContent);
            isReadValuesTested = true;
        }
    }

    @Test
    public void testContent() throws Exception {
        input.getModel().setContent(value);
        String implContent = ((JTextField) input.getImpl()).getText();
        if (value != null && value.contains("<br>")) assertEquals(expected.replaceFirst("<tt>", ""), implContent); else assertEquals(expected, implContent);
    }

    @Test
    public void testContent2() throws Exception {
        ((JTextField) input.getImpl()).setText(value);
        assertEquals(value, input.getModel().getContent());
    }

    @Test
    public void testTooltip() throws Exception {
        input.getModel().setTooltip(value);
        String implTooltip = ((JTextField) input.getImpl()).getToolTipText();
        if (value == null || value.contains("~")) assertEquals(value, implTooltip); else assertEquals(expected, implTooltip);
    }

    @Test
    public void testTooltip2() throws Exception {
        ((JTextField) input.getImpl()).setToolTipText(value);
        assertEquals(value, input.getModel().getTooltip());
    }

    @Test
    public void testStyle() {
        JTextField jtf = (JTextField) input.getImpl();
        assertTrue(Color.WHITE.equals(jtf.getBackground()));
        assertTrue(Color.BLACK.equals(jtf.getForeground()));
        assertEquals(6, jtf.getFont().getSize());
        assertTrue(jtf.getFont().isItalic());
    }

    @Test
    public void testSizeAndLocation() throws Exception {
        Dimension size = new Dimension(61, 16);
        Point location = new Point(125, 65);
        assertEquals(location, ((JTextField) input.getImpl()).getLocation());
        assertEquals(size, ((JTextField) input.getImpl()).getSize());
    }
}
