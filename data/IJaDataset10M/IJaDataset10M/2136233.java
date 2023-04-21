package org.tockit.swing.preferences;

public class ConfigurationType {

    public static class Enumeration extends ConfigurationType {

        private String[] values;

        private Enumeration(String[] values) {
            this.values = values;
        }

        public String[] getValues() {
            return this.values;
        }
    }

    public static final ConfigurationType INTEGER = new ConfigurationType();

    public static final ConfigurationType DOUBLE = new ConfigurationType();

    public static final ConfigurationType BOOLEAN = new ConfigurationType();

    public static final ConfigurationType STRING = new ConfigurationType();

    public static final ConfigurationType COLOR = new ConfigurationType();

    public static final ConfigurationType FONT_FAMILY = new ConfigurationType();

    private ConfigurationType() {
    }

    public static ConfigurationType createEnumType(String[] values) {
        return new Enumeration(values);
    }
}
