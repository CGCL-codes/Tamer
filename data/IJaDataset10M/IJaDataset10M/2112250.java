package test.unit.purview.net;

import java.lang.reflect.Method;
import junit.framework.TestCase;
import com.pureperfect.purview.GetterMethodFilter;
import com.pureperfect.purview.MakeAccessibleFieldFilter;
import com.pureperfect.purview.Purview;
import com.pureperfect.purview.net.DomainName;
import com.pureperfect.purview.net.HostName;

/**
 * 
 * @author J. Chris Folsom
 * @version 1.0
 * @since 1.0
 */
public class DomainNameTest extends TestCase {

    public class Mock {

        @DomainName
        public String field = "myhost.com";

        @DomainName(required = true)
        public String required = "myhost.com";

        private String value = "myhost.com";

        @DomainName
        public String getValue() {
            return this.value;
        }

        public void setValue(final String value) {
            this.value = value;
        }

        public void paramTest(@HostName String address) {
        }
    }

    public void testParameters() throws Exception {
        Mock mock = new Mock();
        Method method = mock.getClass().getMethod("paramTest", new Class[] { String.class });
        assertEquals(1, Purview.validateParameters(mock, method, new Object[] { "my@host" }).getProblems().size());
        assertEquals(0, Purview.validateParameters(mock, method, new Object[] { "myhost" }).getProblems().size());
    }

    public void testField() throws Exception {
        Mock mock = new Mock();
        mock.field = "123...123.123.123";
        assertEquals(1, Purview.validateFields(mock, new MakeAccessibleFieldFilter()).getProblems().size());
        mock.field = "123.com";
        assertEquals(0, Purview.validateFields(mock, new MakeAccessibleFieldFilter()).getProblems().size());
    }

    public void testRequired() throws Exception {
        Mock mock = new Mock();
        mock.required = "asdf.foo.com";
        assertEquals(0, Purview.validateFields(mock, new MakeAccessibleFieldFilter()).getProblems().size());
        mock.required = null;
        assertEquals(1, Purview.validateFields(mock, new MakeAccessibleFieldFilter()).getProblems().size());
    }

    public void testValid() throws Exception {
        Mock mock = new Mock();
        mock.setValue(".asdf.foo.com");
        assertEquals(1, Purview.validateMethods(mock, GetterMethodFilter.defaultInstance()).getProblems().size());
        mock.setValue("asdf.foo.com.");
        assertEquals(1, Purview.validateMethods(mock, GetterMethodFilter.defaultInstance()).getProblems().size());
        mock.setValue("asdf..foo.com");
        assertEquals(1, Purview.validateMethods(mock, GetterMethodFilter.defaultInstance()).getProblems().size());
        mock.setValue("asdf.foo.com");
        assertEquals(0, Purview.validateMethods(mock, GetterMethodFilter.defaultInstance()).getProblems().size());
        mock.setValue("as-df.foo.com");
        assertEquals(0, Purview.validateMethods(mock, GetterMethodFilter.defaultInstance()).getProblems().size());
        mock.setValue("as_df.foo.com");
        assertEquals(0, Purview.validateMethods(mock, GetterMethodFilter.defaultInstance()).getProblems().size());
        mock.setValue("123.foo.com");
        assertEquals(0, Purview.validateMethods(mock, GetterMethodFilter.defaultInstance()).getProblems().size());
        mock.setValue("123.com");
        assertEquals(0, Purview.validateMethods(mock, GetterMethodFilter.defaultInstance()).getProblems().size());
        mock.setValue("123.com.badtld");
        assertEquals(1, Purview.validateMethods(mock, GetterMethodFilter.defaultInstance()).getProblems().size());
    }
}
