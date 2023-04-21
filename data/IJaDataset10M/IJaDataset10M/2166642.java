package jfigure.util;

import java.awt.Color;
import java.awt.Font;
import java.util.*;

/**
 * Propri�t�s pour les de type displayable
 */
public class ConfigurationProperties {

    /**
     * La classe des propri�t�s pour les objets displayable
     */
    public class CProperty {

        private String name;

        private String description;

        private String label;

        private Object value;

        private int type = STRING_INPUT;

        public static final int DOUBLE_INPUT = 0;

        public static final int STRING_INPUT = 1;

        public static final int BOOLEAN_INPUT = 2;

        public static final int COLOR_INPUT = 3;

        public static final int FONT_INPUT = 4;

        public static final int CHOICE_INPUT = 5;

        public static final int STRING = 99;

        /**
         * @return Returns the type.
         */
        public final int getType() {
            return type;
        }

        /**
         * @param type The type to set.
         */
        public final void setType(int type) {
            this.type = type;
        }

        /**
         * @return Returns the description.
         */
        public final String getDescription() {
            return description;
        }

        /**
         * @param description The description to set.
         */
        public final void setDescription(String description) {
            this.description = description;
        }

        /**
         * @return Returns the label.
         */
        public final String getLabel() {
            return label;
        }

        /**
         * @param label The label to set.
         */
        public final void setLabel(String label) {
            this.label = label;
        }

        /**
         * @return Returns the name.
         */
        public final String getName() {
            return name;
        }

        /**
         * @param name The name to set.
         */
        public final void setName(String name) {
            this.name = name;
        }

        /**
         * @return Returns the value.
         */
        public final Object getValue() {
            return value;
        }

        /**
         * @param value The value to set.
         */
        public final void setValue(Object value) {
            this.value = value;
        }
    }

    private final Hashtable propertiesH = new Hashtable();

    private final ArrayList properties = new ArrayList();

    /**
     * Ajoute une propri�t�
     */
    public final ConfigurationProperties.CProperty addProperty(ConfigurationProperties.CProperty prop) {
        if (prop != null && prop.getName() != null && prop.getName().length() != 0) {
            this.propertiesH.put(prop.getName(), prop);
            this.properties.add(prop);
        }
        return prop;
    }

    /**
     * Ajoute une liste de propri�t�s
     */
    public final void addAll(ConfigurationProperties props) {
        for (int i = 0; i < props.properties.size(); i++) {
            this.addProperty((ConfigurationProperties.CProperty) props.properties.get(i));
        }
    }

    /**
     * Ajoute une propri�t�
     */
    public final ConfigurationProperties.CProperty addProperty(String name, String label, String description, Object value) {
        int type = ConfigurationProperties.CProperty.STRING_INPUT;
        if (value instanceof Double) type = ConfigurationProperties.CProperty.DOUBLE_INPUT;
        if (value instanceof Color) type = ConfigurationProperties.CProperty.COLOR_INPUT;
        if (value instanceof Font) type = ConfigurationProperties.CProperty.FONT_INPUT;
        if (value instanceof Boolean) type = ConfigurationProperties.CProperty.BOOLEAN_INPUT;
        if (value instanceof CPList) type = ConfigurationProperties.CProperty.CHOICE_INPUT;
        return this.addProperty(name, label, description, type, value);
    }

    /**
     * Ajoute une propri�t� de type d'information
     */
    public final ConfigurationProperties.CProperty addInformationProperty(String name, String label, String description, String value) {
        return this.addProperty(name, label, description, ConfigurationProperties.CProperty.STRING, value);
    }

    /**
     * Ajoute une propri�t�
     */
    public final ConfigurationProperties.CProperty addProperty(String name, String label, String description, int type, Object value) {
        ConfigurationProperties.CProperty p = new ConfigurationProperties.CProperty();
        p.setName(name);
        p.setDescription(description);
        p.setLabel(label);
        p.setValue(value);
        p.setType(type);
        return this.addProperty(p);
    }

    /**
     * Retourne la liste des propri�t�s
     */
    public final ConfigurationProperties.CProperty[] getAllProperties() {
        ConfigurationProperties.CProperty[] ps = new ConfigurationProperties.CProperty[this.properties.size()];
        for (int i = 0; i < ps.length; i++) {
            ps[i] = (ConfigurationProperties.CProperty) this.properties.get(i);
        }
        return ps;
    }

    /**
     * Retourne une propri�t�s d'un nom donn�e
     */
    public final ConfigurationProperties.CProperty getProperty(String name) {
        return (ConfigurationProperties.CProperty) this.propertiesH.get(name);
    }

    /**
     * Le nombre de propri�t�
     * 
     */
    public final int size() {
        return this.properties.size();
    }
}
