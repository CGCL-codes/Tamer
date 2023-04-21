package javango.forms.widgets;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.inject.Inject;
import javango.forms.AbstractForm;
import javango.forms.Form;
import javango.forms.fields.BoundField;
import javango.forms.fields.ChoiceField;
import javango.forms.fields.Field;
import javango.forms.fields.FieldFactory;
import javango.forms.fields.annotations.CharFieldProperties;
import javango.forms.fields.annotations.Choice;
import javango.forms.fields.annotations.ChoiceFieldProperties;
import javango.forms.fields.annotations.FieldProperties;
import javango.forms.tests.InjectedTestCaseBase;
import javango.forms.widgets.CheckboxWidget;
import javango.forms.widgets.SelectMultipleWidget;
import javango.forms.widgets.SelectWidget;
import javango.forms.widgets.TextAreaWidget;
import javango.forms.widgets.Widget;
import javango.forms.widgets.WidgetFactory;
import junit.framework.TestCase;

public class TextAreaWidgetTest extends InjectedTestCaseBase {

    private Log log = LogFactory.getLog(TextAreaWidgetTest.class);

    public void testEmptyValueHtml() {
        Widget w = new TextAreaWidget();
        String value = "";
        String output = w.render("field", value, new HashMap<String, Object>());
        String html = "<textarea name=\"field\" rows=\"10\" cols=\"40\"></textarea>";
        assertEquals(html, output);
    }

    public void testNullValueHtml() {
        Widget w = new TextAreaWidget();
        String value = null;
        String output = w.render("field", value, new HashMap<String, Object>());
        String html = "<textarea name=\"field\" rows=\"10\" cols=\"40\"></textarea>";
        assertEquals(html, output);
    }

    public void testEmptyArrayValueHtml() {
        Widget w = new TextAreaWidget();
        String[] value = new String[] {};
        String output = w.render("field", value, new HashMap<String, Object>());
        String html = "<textarea name=\"field\" rows=\"10\" cols=\"40\"></textarea>";
        assertEquals(html, output);
    }

    public void testStringHtml() {
        Widget w = new TextAreaWidget();
        String value = "hello";
        String output = w.render("field", value, new HashMap<String, Object>());
        String html = "<textarea name=\"field\" rows=\"10\" cols=\"40\">hello</textarea>";
        assertEquals(html, output);
    }

    public void testStringArrayHtml() {
        Widget w = new TextAreaWidget();
        String[] value = new String[] { "hello" };
        String output = w.render("field", value, new HashMap<String, Object>());
        String html = "<textarea name=\"field\" rows=\"10\" cols=\"40\">hello</textarea>";
        assertEquals(html, output);
    }

    public void testIdAttrHtml() {
        Widget w = new TextAreaWidget();
        String[] value = new String[] { "hello" };
        Map<String, Object> attrs = new HashMap<String, Object>();
        attrs.put("id", "id_%s");
        String output = w.render("field", value, attrs);
        String html = "<textarea id=\"id_field\" name=\"field\" rows=\"10\" cols=\"40\">hello</textarea>";
        assertEquals(html, output);
    }

    public void testWithRowsColsAttrs() {
        Widget w = new TextAreaWidget();
        String[] value = new String[] { "hello" };
        Map<String, Object> attrs = new LinkedHashMap<String, Object>();
        attrs.put("id", "id_%s");
        attrs.put("rows", "5");
        attrs.put("cols", "20");
        String output = w.render("field", value, attrs);
        String html = "<textarea id=\"id_field\" rows=\"5\" cols=\"20\" name=\"field\">hello</textarea>";
        assertEquals(html, output);
    }
}
