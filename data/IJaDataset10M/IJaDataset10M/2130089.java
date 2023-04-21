package koppa.widget.comboBox;

import koppa.widget.AbstractMetaData;

public class ComboBoxMetaData extends AbstractMetaData {

    public static final class Behavior {
    }

    public static final class BindingExp {

        private String value;

        private String options;

        private String labelKey;

        private String valueKey;

        public String getValue() {
            return value;
        }

        public void setValue(final String value) {
            this.value = value;
        }

        protected String getLabelKey() {
            return labelKey;
        }

        protected void setLabelKey(final String labelKey) {
            this.labelKey = labelKey;
        }

        protected String getOptions() {
            return options;
        }

        protected void setOptions(final String options) {
            this.options = options;
        }

        protected String getValueKey() {
            return valueKey;
        }

        protected void setValueKey(final String valueKey) {
            this.valueKey = valueKey;
        }
    }

    public static final class HtmlAttribute {

        private String cssClass;

        private String alt;

        public String getCssClass() {
            return cssClass;
        }

        public void setCssClass(final String cssClass) {
            this.cssClass = cssClass;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(final String alt) {
            this.alt = alt;
        }
    }

    public static final class JsConfig {
    }

    private HtmlAttribute attribute;

    private JsConfig config;

    private BindingExp bindingExp;

    private Behavior behavior;

    public HtmlAttribute getAttribute() {
        if (this.attribute == null) {
            this.attribute = new HtmlAttribute();
        }
        return attribute;
    }

    public Behavior getBehavior() {
        if (this.behavior == null) {
            this.behavior = new Behavior();
        }
        return behavior;
    }

    public BindingExp getBindingExp() {
        if (this.bindingExp == null) {
            this.bindingExp = new BindingExp();
        }
        return bindingExp;
    }

    public JsConfig getConfig() {
        if (this.config == null) {
            this.config = new JsConfig();
        }
        return config;
    }
}
