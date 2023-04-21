package test;

import java.util.Arrays;
import java.util.Collection;
import javax.swing.JLabel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import riaf.facade.ILabel;
import riaf.facade.IStyle;
import riafswing.RLabel;

@RunWith(Parameterized.class)
public class RLabelStaticTest extends RiafParameterizedTestCase {

    RLabel label;

    public RLabelStaticTest(String value, String expected) {
        super(value, expected);
    }

    @Parameters
    public static Collection<String[]> data() {
        return Arrays.asList(new String[][] { { null, "" }, { "~1", "1" }, { "'", "'" }, { "", "" }, { "translation", "test" }, { "test", "test" }, { String.valueOf(true), "true" }, { "m<br>n", "<html><tt>m<br>n</html>" } });
    }

    @Before
    public void init() {
        label = new RLabel(value, null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(label);
    }

    @Test
    public void testContent() throws Exception {
        label.getModel().setContent(value);
        String implContent = ((JLabel) label.getImpl()).getText();
        if (value != null && value.contains("<br>")) assertEquals(expected.replaceFirst("<tt>", ""), implContent); else assertEquals(expected, implContent);
    }

    @Test
    public void testTooltip() throws Exception {
        label.getModel().setTooltip(value);
        String implTooltip = ((JLabel) label.getImpl()).getToolTipText();
        if (value == null || value.contains("~")) assertEquals(value, implTooltip); else assertEquals(expected, implTooltip);
    }

    @Test
    public void testName() {
        assertEquals(ILabel.name, label.getName());
    }

    @Test
    public void testIsDataValid() {
        assertTrue(label.isDataValid());
    }
}
