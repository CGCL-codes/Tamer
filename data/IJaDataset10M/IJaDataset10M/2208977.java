package org.fudaa.fudaa.crue.options.node;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.MissingResourceException;
import javax.swing.Action;
import org.apache.commons.lang.ObjectUtils;
import org.fudaa.dodico.crue.projet.conf.Option;
import org.fudaa.dodico.crue.projet.conf.OptionsEnum;
import org.fudaa.dodico.crue.projet.conf.UserOption;
import org.fudaa.fudaa.crue.common.property.PropertyCrueUtils;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.PropertySupport.ReadOnly;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author deniger ( genesis)
 */
public class OptionNode extends AbstractNode {

    public static final String PROP_ID = "id";

    public static final String PROP_VALUE = "value";

    public static final String PROP_DEFAULT = "default";

    private final String defaultValue;

    public static String getPropIdDisplayName() throws MissingResourceException {
        return NbBundle.getMessage(OptionNode.class, "ProprieteOption.Id.Name");
    }

    public static String getPropIdDescription() throws MissingResourceException {
        return NbBundle.getMessage(OptionNode.class, "ProprieteOption.Id.Desc");
    }

    public static String getPropDefaultDisplayName() throws MissingResourceException {
        return NbBundle.getMessage(OptionNode.class, "ProprieteOption.Default.Name");
    }

    public static String getPropDefaultDescription() throws MissingResourceException {
        return NbBundle.getMessage(OptionNode.class, "ProprieteOption.Default.Desc");
    }

    public static String getPropValueDisplayName() throws MissingResourceException {
        return NbBundle.getMessage(OptionNode.class, "ProprieteOption.Value.Name");
    }

    public static String getPropValueDescription() throws MissingResourceException {
        return NbBundle.getMessage(OptionNode.class, "ProprieteOption.Value.Desc");
    }

    private final OptionsEnum optionEnum;

    public OptionNode(Option option, String defaultValue, OptionsEnum optionEnum) {
        super(Children.LEAF, Lookups.singleton(option));
        this.optionEnum = optionEnum;
        setDisplayName(option.getDisplayName());
        setShortDescription(option.getCommentaire());
        this.defaultValue = defaultValue;
    }

    public OptionsEnum getOptionEnum() {
        return optionEnum;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[] { SystemAction.get(OptionResetAction.class) };
    }

    public void setOptionValue(String newValue) {
        if (getOption().isEditable()) {
            String old = getOption().getValeur();
            ((UserOption) getOption()).setValeur(newValue);
            firePropertyChange(PROP_VALUE, old, newValue);
        }
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        Option option = getOption();
        try {
            set.put(new PropertyId(option));
            if (option.isEditable()) {
                set.put(create((UserOption) option));
                set.put(new PropertyDefault(defaultValue));
            } else {
                set.put(new PropertyValue(option));
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        sheet.put(set);
        return sheet;
    }

    private Property<?> create(UserOption option) {
        final Class expectedType = optionEnum.getExpectedValue();
        if (File.class.equals(expectedType)) {
            return new PropertyValueFileEditable(option);
        } else if (Boolean.class.equals(expectedType)) {
            return new PropertyValueBooleanEditable(option);
        } else if (Double.class.equals(expectedType)) {
            return new PropertyValueDoubleEditable(option);
        }
        return new PropertyValueStringEditable((UserOption) option);
    }

    public Option getOption() {
        return getLookup().lookup(Option.class);
    }

    private static class PropertyId extends ReadOnly {

        private final Option option;

        public PropertyId(final Option option) {
            super(PROP_ID, String.class, getPropIdDisplayName(), getPropIdDescription());
            this.option = option;
            PropertyCrueUtils.configureNoCustomEditor(this);
        }

        @Override
        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return option.getId();
        }
    }

    private static class PropertyValue extends ReadOnly {

        private final Option option;

        public PropertyValue(final Option option) {
            super(PROP_VALUE, String.class, getPropValueDisplayName(), getPropValueDescription());
            this.option = option;
            PropertyCrueUtils.configureNoCustomEditor(this);
        }

        @Override
        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return option.getValeur();
        }
    }

    private static class PropertyDefault extends ReadOnly {

        private final String value;

        public PropertyDefault(final String option) {
            super(PROP_DEFAULT, String.class, getPropDefaultDisplayName(), getPropDefaultDescription());
            this.value = option;
            PropertyCrueUtils.configureNoCustomEditor(this);
        }

        @Override
        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            return value;
        }
    }

    private static class PropertyValueStringEditable extends PropertySupport.ReadWrite<String> {

        private final UserOption option;

        public PropertyValueStringEditable(final UserOption option) {
            super(PROP_VALUE, String.class, getPropValueDisplayName(), getPropValueDescription());
            this.option = option;
            PropertyCrueUtils.configureNoCustomEditor(this);
        }

        @Override
        public String getValue() throws IllegalAccessException, InvocationTargetException {
            return option.getValeur();
        }

        @Override
        public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            option.setValeur(val);
        }
    }

    private class PropertyValueDoubleEditable extends PropertySupport.ReadWrite<Double> {

        private final UserOption option;

        public PropertyValueDoubleEditable(final UserOption option) {
            super(PROP_VALUE, Double.class, getPropValueDisplayName(), getPropValueDescription());
            this.option = option;
            PropertyCrueUtils.configureNoCustomEditor(this);
        }

        @Override
        public Double getValue() throws IllegalAccessException, InvocationTargetException {
            return Double.valueOf(option.getValeur());
        }

        @Override
        public void setValue(Double val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            setOptionValue(ObjectUtils.toString(val));
        }
    }

    private class PropertyValueBooleanEditable extends PropertySupport.ReadWrite<Boolean> {

        private final UserOption option;

        public PropertyValueBooleanEditable(final UserOption option) {
            super(PROP_VALUE, boolean.class, getPropValueDisplayName(), getPropValueDescription());
            this.option = option;
            PropertyCrueUtils.configureNoCustomEditor(this);
        }

        @Override
        public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
            return Boolean.valueOf(option.getValeur());
        }

        @Override
        public void setValue(Boolean val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            setOptionValue(val == null ? "false" : val.toString());
        }
    }

    private class PropertyValueFileEditable extends PropertySupport.ReadWrite<File> {

        private final UserOption option;

        public PropertyValueFileEditable(final UserOption option) {
            super(PROP_VALUE, File.class, getPropValueDisplayName(), getPropValueDescription());
            this.option = option;
        }

        @Override
        public File getValue() throws IllegalAccessException, InvocationTargetException {
            return new File(option.getValeur());
        }

        @Override
        public void setValue(File val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (val == null) {
                setOptionValue(null);
            } else if (val.exists()) {
                setOptionValue(val.getAbsolutePath());
            } else {
                setOptionValue(val.getPath());
            }
        }
    }
}
