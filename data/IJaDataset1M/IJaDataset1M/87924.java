package javango.forms.widgets;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javango.forms.fields.ChoiceField;
import javango.forms.widgets.RadioWidget.RadioInput;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RadioHorizontalWidget<K, V> extends AbstractChoicesWidget<K, V> {

    public static class RadioInput extends AbstractWidget {

        Object choice_value;

        String choice_label;

        private int index;

        public RadioInput(Map<String, Object> attrs, Object choice_value, String choice_label, int index) {
            super(attrs);
            this.choice_label = choice_label;
            this.choice_value = choice_value == null ? "" : choice_value;
            this.index = index;
        }

        public String getInputType() {
            return "radio";
        }

        @Override
        public String render(String name, Object value, Map<String, Object> attrs) {
            Map<String, Object> finalAttrs = buildAttrs(attrs);
            finalAttrs.put("type", getInputType());
            finalAttrs.put("name", name);
            String labelFor = "";
            if (finalAttrs.containsKey("id")) {
                String id = String.format("%s_%s", (String) finalAttrs.get("id"), index);
                finalAttrs.put("id", id);
                labelFor = String.format(" for='%s'", id);
            }
            finalAttrs.put("value", choice_value);
            if (StringUtils.equals(choice_value.toString(), value == null ? "" : value.toString())) {
                finalAttrs.put("checked", "checked");
            }
            return String.format("<label%s><input%s /> %s</label>", labelFor, Util.flatatt(finalAttrs), choice_label);
        }
    }

    private static final Log log = LogFactory.getLog(RadioHorizontalWidget.class);

    public RadioHorizontalWidget(Map<K, V> choices) {
        super(null);
        setChoices(choices);
    }

    public RadioHorizontalWidget() {
        super(null);
    }

    public RadioHorizontalWidget(Map<String, Object> attrs, Map<K, V> choices) {
        super(attrs);
        setChoices(choices);
    }

    public String render(String name, Object value, Map<String, Object> attrs) {
        if (value instanceof String[]) {
            String[] myvalue = (String[]) value;
            if (myvalue.length > 0) {
                value = myvalue[0];
            } else {
                value = "";
            }
        }
        Map<String, Object> finalAttrs = buildAttrs(attrs);
        finalAttrs.put("name", name);
        if (finalAttrs.containsKey("id")) {
            finalAttrs.put("id", String.format((String) finalAttrs.get("id"), name));
        }
        StringBuilder b = new StringBuilder("");
        int index = 0;
        for (Entry<K, V> e : getChoices().entrySet()) {
            b.append(new RadioInput(attrs, e.getKey(), e.getValue().toString(), index++).render(name, value, finalAttrs));
        }
        return b.toString();
    }
}
