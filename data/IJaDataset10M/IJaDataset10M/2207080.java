package javango.forms.tests;

import java.util.HashMap;
import java.util.Map;
import javango.forms.fields.CharField;
import javango.forms.fields.Field;
import javango.forms.widgets.WidgetFactory;
import junit.framework.TestCase;

public class NullFieldTest extends InjectedTestCaseBase {

    public void testNoAllowNull() throws Exception {
        Field field = new CharField(injector.getInstance(WidgetFactory.class)).setRequired(false).setAllowNull(false);
        field.setName("fieldName");
        Map<String, String> errors = new HashMap<String, String>();
        String value = (String) field.clean(new String[] { "" }, errors);
        assertTrue(errors.isEmpty());
        assertTrue("".equals(value));
    }

    public void testAllowNull() throws Exception {
        Field field = new CharField(injector.getInstance(WidgetFactory.class)).setRequired(false).setAllowNull(true);
        field.setName("fieldName");
        Map<String, String> errors = new HashMap<String, String>();
        String value = (String) field.clean(new String[] { "" }, errors);
        assertTrue(errors.isEmpty());
        assertTrue(value == null);
    }
}
