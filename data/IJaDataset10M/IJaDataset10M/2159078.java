package test.unit.purview.string;

import java.lang.reflect.Method;
import junit.framework.TestCase;
import com.pureperfect.purview.GetterMethodFilter;
import com.pureperfect.purview.MakeAccessibleFieldFilter;
import com.pureperfect.purview.Purview;
import com.pureperfect.purview.string.MaxLength;

/**
 * 
 * @author J. Chris Folsom
 * @version 1.0
 * @since 1.0
 */
public class MaxLengthTest extends TestCase {

    public class Mock {

        @MaxLength(5)
        public String field;

        private String value;

        @MaxLength(4)
        public String getValue() {
            return this.value;
        }

        public void setValue(final String value) {
            this.value = value;
        }

        public void paramTest(@MaxLength(5) String string) {
        }
    }

    public void testParameters() throws Exception {
        Mock mock = new Mock();
        Method method = mock.getClass().getMethod("paramTest", new Class[] { String.class });
        assertEquals(1, Purview.validateParameters(mock, method, new Object[] { "asdfff" }).getProblems().size());
        assertEquals(0, Purview.validateParameters(mock, method, new Object[] { "asff" }).getProblems().size());
    }

    public void testField() throws Exception {
        Mock mock = new Mock();
        mock.field = "adffff";
        assertEquals(1, Purview.validateFields(mock, new MakeAccessibleFieldFilter()).getProblems().size());
    }

    public void testMaxBoundsEqual() throws Exception {
        final Mock mock = new Mock();
        mock.setValue("asdf");
        assertEquals(0, Purview.validateMethods(mock, GetterMethodFilter.defaultInstance()).getProblems().size());
    }

    public void testMaxBoundsOver() throws Exception {
        final Mock mock = new Mock();
        mock.setValue("asdfa");
        assertEquals(1, Purview.validateMethods(mock, GetterMethodFilter.defaultInstance()).getProblems().size());
    }

    public void testMaxBoundsUnder() throws Exception {
        final Mock mock = new Mock();
        mock.setValue("asdf");
        assertEquals(0, Purview.validateMethods(mock, GetterMethodFilter.defaultInstance()).getProblems().size());
    }
}
