package net.suberic.pooka.gui.filter;

import net.suberic.pooka.Pooka;
import javax.swing.*;
import java.util.Vector;
import java.util.Properties;

/**
 * This is a class that lets you choose your filter actions.
 */
public class FlagFilterEditor extends FilterEditor {

    JComboBox flagBox;

    JComboBox trueFalseBox;

    public static String FILTER_CLASS = "net.suberic.pooka.filter.FlagFilterAction";

    /**
   * Configures the given FilterEditor from the given VariableBundle and
   * property.
   */
    public void configureEditor(net.suberic.util.gui.propedit.PropertyEditorManager newManager, String propertyName) {
        property = propertyName;
        manager = newManager;
        Vector flagNames = Pooka.getSearchManager().getFlagLabels();
        flagBox = new JComboBox(flagNames);
        this.add(flagBox);
        Vector trueFalse = new Vector();
        trueFalse.add(Pooka.getProperty("label.true", "True"));
        trueFalse.add(Pooka.getProperty("label.false", "False"));
        trueFalseBox = new JComboBox(trueFalse);
        this.add(trueFalseBox);
    }

    /**
   * Gets the values that would be set by this FilterEditor.
   */
    public java.util.Properties getValue() {
        Properties props = new Properties();
        String oldFlag = manager.getProperty(property + ".flag", "");
        if (!oldFlag.equals((String) flagBox.getSelectedItem())) props.setProperty(property + ".flag", (String) flagBox.getSelectedItem());
        String oldValue = manager.getProperty(property + ".value", "");
        if (!oldFlag.equals((String) trueFalseBox.getSelectedItem())) props.setProperty(property + ".value", (String) trueFalseBox.getSelectedItem());
        String oldClassName = manager.getProperty(property + ".class", "");
        if (!oldClassName.equals(FILTER_CLASS)) props.setProperty(property + ".class", FILTER_CLASS);
        return props;
    }

    /**
   * Sets the values represented by this FilterEditor in the manager.
   */
    public void setValue() {
        String oldFlag = manager.getProperty(property + ".flag", "");
        if (!oldFlag.equals((String) flagBox.getSelectedItem())) manager.setProperty(property + ".flag", (String) flagBox.getSelectedItem());
        String oldValue = manager.getProperty(property + ".value", "");
        if (!oldFlag.equals((String) trueFalseBox.getSelectedItem())) manager.setProperty(property + ".value", (String) trueFalseBox.getSelectedItem());
        String oldClassName = manager.getProperty(property + ".class", "");
        if (!oldClassName.equals(FILTER_CLASS)) manager.setProperty(property + ".class", FILTER_CLASS);
    }

    /**
   * Returns the class that will be set for this FilterEditor.
   */
    public String getFilterClassValue() {
        return FILTER_CLASS;
    }
}
