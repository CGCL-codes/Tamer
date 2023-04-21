package javango.forms.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.google.inject.Inject;
import javango.forms.AbstractForm;
import javango.forms.Form;
import javango.forms.fields.BooleanField;
import javango.forms.fields.BoundField;
import javango.forms.fields.CharField;
import javango.forms.fields.EmailField;
import javango.forms.fields.Field;
import javango.forms.fields.FieldFactory;
import javango.forms.fields.LongField;
import javango.forms.tests.PrefixTest.ContactForm;
import junit.framework.TestCase;

public class FieldNameTest extends InjectedTestCaseBase {

    public static class ContactForm extends AbstractForm {

        @Inject
        public ContactForm(FieldFactory factory) {
            super(factory);
            getFields().put("value", factory.newField(LongField.class));
        }
    }

    public void testNullName() throws Exception {
        Form f = injector.getInstance(ContactForm.class).setId(null);
        String expected = "<input type=\"text\" name=\"value\" />";
        assertEquals(expected, f.get("value").toString());
        expected = "<tr><th>Value</th><td><input type=\"text\" name=\"value\" /></td></tr>\n";
        String html = f.asTable();
        assertEquals(expected, html);
    }

    public void testChanged() throws Exception {
        Map<String, String[]> m = new HashMap<String, String[]>();
        m.put("prefix-newvalue", new String[] { "1234" });
        Form f = injector.getInstance(ContactForm.class);
        f.getFields().get("value").setName("newvalue");
        f.bind(m).setPrefix("prefix").setId(null);
        f.isValid();
        String expected = "<input type=\"text\" name=\"prefix-newvalue\" value=\"1234\" />";
        assertEquals(expected, f.get("value").toString());
        expected = "<tr><th>Newvalue</th><td><input type=\"text\" name=\"prefix-newvalue\" value=\"1234\" /></td></tr>\n";
        String html = f.asTable();
        assertEquals(expected, html);
    }

    public void testFieldIteration() throws Exception {
        Form f = injector.getInstance(ContactForm.class).setPrefix("prefix").setId(null);
        f.getFields().get("value").setName("newvalue");
        List<String> expected = new ArrayList<String>();
        expected.add("<input type=\"text\" name=\"prefix-newvalue\" />");
        Iterator<String> i = expected.iterator();
        for (BoundField field : f) {
            assertEquals(i.next(), field.toString());
        }
    }
}
