package test.unit.purview;

import junit.framework.TestCase;
import com.pureperfect.purview.MakeAccessibleFieldFilter;
import com.pureperfect.purview.MatchField;
import com.pureperfect.purview.Purview;

/**
 * 
 * @author J. Chris Folsom
 * @version 1.0
 * @since 1.0
 */
public class MatchFieldTest extends TestCase {

    public class Mock {

        public String expected;

        @MatchField("expected")
        public String actual;
    }

    public void testExpectedNull() {
        Mock mock = new Mock();
        assertEquals(0, Purview.validateFields(mock, new MakeAccessibleFieldFilter()).getProblems().size());
        mock.actual = "adsf";
        assertEquals(1, Purview.validateFields(mock, new MakeAccessibleFieldFilter()).getProblems().size());
    }

    public void testActualNull() {
        Mock mock = new Mock();
        assertEquals(0, Purview.validateFields(mock, new MakeAccessibleFieldFilter()).getProblems().size());
        mock.expected = "adsf";
        assertEquals(1, Purview.validateFields(mock, new MakeAccessibleFieldFilter()).getProblems().size());
    }

    public void testBoth() {
        Mock mock = new Mock();
        mock.actual = "adsf";
        mock.expected = "adsf";
        assertEquals(0, Purview.validateFields(mock, new MakeAccessibleFieldFilter()).getProblems().size());
        mock.expected = "aaa";
        assertEquals(1, Purview.validateFields(mock, new MakeAccessibleFieldFilter()).getProblems().size());
    }
}
