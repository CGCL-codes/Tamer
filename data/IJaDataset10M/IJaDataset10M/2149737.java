package javango.forms.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javango.forms.fields.ChoiceField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.inject.Inject;

public class SelectMultipleWidget<K, V> extends SelectWidget<K, V> {

    private static final Log log = LogFactory.getLog(SelectMultipleWidget.class);

    @Inject
    public SelectMultipleWidget() {
        super();
    }

    public String render(String name, Object value, Map<String, Object> attrs) {
        Map<String, Object> finalAttrs = buildAttrs(attrs);
        finalAttrs.put("name", name);
        if (finalAttrs.containsKey("id")) {
            finalAttrs.put("id", String.format((String) finalAttrs.get("id"), name));
        }
        StringBuilder b = new StringBuilder("<select multiple=\"multiple\"");
        Util.flatatt(finalAttrs, b);
        b.append(">");
        String optionHtml = "<option value=\"%s\" %s>%s</option>";
        List<String> values = new ArrayList<String>();
        if (value != null) {
            if (value instanceof Object[]) {
                for (Object o : (Object[]) value) {
                    values.add(o.toString());
                }
            } else if (value instanceof Iterable) {
                for (Object o : (Iterable) value) {
                    values.add(o.toString());
                }
            } else {
                values.add(value.toString());
            }
        }
        for (Map.Entry<K, V> entry : getChoices().entrySet()) {
            Object optionValue = entry.getKey();
            String selected = value == null ? "" : values.contains(optionValue) ? "selected=\"selected\"" : "";
            log.debug("optionValue : '" + optionValue + "'; value : '" + value + "'");
            b.append(String.format(optionHtml, optionValue.toString(), selected, entry.getValue()));
        }
        b.append("</select>");
        return b.toString();
    }

    @Override
    public String asText(Object value) {
        if (value == null) return "";
        if (value instanceof Iterable<?>) {
            StringBuilder b = new StringBuilder("<ul>");
            for (Object o : (Iterable<?>) value) {
                b.append(String.format("<li>%s</li>", super.asText(o)));
            }
            b.append("</ul>");
            return b.toString();
        } else {
            return super.asText(value);
        }
    }
}
