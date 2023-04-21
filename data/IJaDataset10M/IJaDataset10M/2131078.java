package org.iwidget.model.javascript;

import org.iwidget.desktop.model.ElementPreferences;
import org.iwidget.desktop.model.WidgetElement;
import java.util.ArrayList;
import org.mozilla.javascript.ScriptableObject;

/**
 *
 * @author Muhammad Hakim A
 */
public class PreferencesJS extends ScriptableObject {

    public String getClassName() {
        return elementData.getName();
    }

    public PreferencesJS() {
        elementData = new ElementPreferences();
        elementData.setName("preferences");
    }

    public PreferencesJS(String name) {
        elementData = new ElementPreferences();
        elementData.setName(name);
        setDefaults();
    }

    public PreferencesJS(WidgetElement widget, String name) {
        elementData = new ElementPreferences(widget);
        elementData.setName(name);
        setDefaults();
    }

    public synchronized void jsConstructor() {
    }

    public ElementPreferences getElementData() {
        return elementData;
    }

    public void setElementData(ElementPreferences d) {
        elementData = d;
    }

    private void setDefaults() {
        try {
            defineProperty("defaultValue", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("description", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("directory", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("file", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("lines", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("kind", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("name", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("notSaved", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("options", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("minLength", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("maxLength", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("select", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("style", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("ticks", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("title", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("type", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("validator", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("validatorMessage", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("value", org.iwidget.model.javascript.PreferencesJS.class, 0);
            defineProperty("visible", org.iwidget.model.javascript.PreferencesJS.class, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getOptions() {
        return elementData.getOptions();
    }

    public void addOption(String option) {
        elementData.options.add(option);
    }

    public ArrayList getOptionsArray() {
        return elementData.options;
    }

    public void setOptions(String s) {
        elementData.setOptions(s);
    }

    public void addTickLabel(String option) {
        elementData.tickLabels.add(option);
    }

    public ArrayList getTickLabels() {
        return elementData.tickLabels;
    }

    public void addExtension(String option) {
        elementData.extentions.add(option);
    }

    public ArrayList getExtensions() {
        return elementData.extentions;
    }

    public String getName() {
        return elementData.getName();
    }

    public void setName(String s) {
        elementData.setName(s);
    }

    public String getSelect() {
        return elementData.getSelect();
    }

    public void setSelect(String s) {
        elementData.setSelect(s);
    }

    public String getType() {
        return elementData.getType();
    }

    public void setType(String s) {
        elementData.setType(s);
    }

    public String getStyle() {
        return elementData.getStyle();
    }

    public void setStyle(String s) {
        elementData.setStyle(s);
    }

    public String getKind() {
        return elementData.getKind();
    }

    public void setKind(String s) {
        elementData.setKind(s);
    }

    public String getDirectory() {
        return elementData.getDirectory();
    }

    public void setDirectory(String s) {
        elementData.setDirectory(s);
    }

    public String getFile() {
        return elementData.getFile();
    }

    public void setFile(String s) {
        elementData.setFile(s);
    }

    public String getDescription() {
        return elementData.getDescription();
    }

    public void setDescription(String s) {
        elementData.setDescription(s);
    }

    public void setLines(int i) {
        elementData.setLines(i);
    }

    public int getLines() {
        return elementData.getLines();
    }

    public String getTitle() {
        return elementData.getTitle();
    }

    public void setTitle(String s) {
        elementData.setTitle(s);
    }

    public String getDefaultValue() {
        return elementData.getDefaultValue();
    }

    public void setDefaultValue(String s) {
        elementData.setDefaultValue(s);
    }

    public String getValue() {
        return elementData.getValue();
    }

    public void setValue(String s) {
        elementData.setValue(s);
    }

    public String getValidator() {
        return elementData.getValidator();
    }

    public void setValidator(String s) {
        elementData.setValidator(s);
    }

    public String getValidatorMessage() {
        return elementData.getValidatorMessage();
    }

    public void setValidatorMessage(String s) {
        elementData.setValidatorMessage(s);
    }

    public void setVisible(boolean b) {
        elementData.setVisible(b);
    }

    public boolean getVisible() {
        return elementData.getVisible();
    }

    public void setNotSaved(boolean b) {
        elementData.setNotSaved(b);
    }

    public boolean getNotSaved() {
        return elementData.getNotSaved();
    }

    public void setMinLength(int i) {
        elementData.setMinLength(i);
    }

    public int getMinLength() {
        return elementData.getMinLength();
    }

    public void setMaxLength(int i) {
        elementData.setMaxLength(i);
    }

    public int getMaxLength() {
        return elementData.getMaxLength();
    }

    public void setTicks(int i) {
        elementData.setTicks(i);
    }

    public int getTicks() {
        return elementData.getTicks();
    }

    private static final long serialVersionUID = 0x2d31393639393431L;

    ElementPreferences elementData;
}
