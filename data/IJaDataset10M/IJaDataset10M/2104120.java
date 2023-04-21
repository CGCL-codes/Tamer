package org.xnap.commons.gui.settings;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ComboBoxModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.JTextComponent;
import org.xnap.commons.settings.BooleanSetting;
import org.xnap.commons.settings.IntSetting;
import org.xnap.commons.settings.Setting;
import org.xnap.commons.settings.StringSetting;

/**
 * Provides a mediator between {@link org.xnap.commons.settings.Setting} objects
 * and gui components. This class can be used to easily implement dialogs that
 * configure application settings. 
 * 
 * @author Steffen Pingel
 */
public class SettingComponentMediator {

    private List<SettingMapper> mappers = new LinkedList<SettingMapper>();

    public void add(BooleanSetting setting, AbstractButton button) {
        mappers.add(new BooleanSettingMapper(setting, button));
    }

    public void add(IntSetting setting, SpinnerNumberModel spinnerModel) {
        mappers.add(new IntSettingMapper(setting, spinnerModel));
    }

    public void add(StringSetting setting, JTextComponent textComponent) {
        mappers.add(new StringSettingMapper(setting, textComponent));
    }

    public <T> void add(Setting<T> setting, ComboBoxModel model) {
        mappers.add(new SelectionMapper<T>(setting, model));
    }

    public void add(SettingMapper mapper) {
        mappers.add(mapper);
    }

    public void remove(SettingMapper mapper) {
        mappers.remove(mapper);
    }

    public Iterator<SettingMapper> iterator() {
        return mappers.iterator();
    }

    public void apply() {
        for (SettingMapper mapper : mappers) {
            mapper.apply();
        }
    }

    public void revert() {
        for (SettingMapper mapper : mappers) {
            mapper.revert();
        }
    }

    public void revertToDefaults() {
        for (SettingMapper mapper : mappers) {
            mapper.revertToDefaults();
        }
    }

    public static interface SettingMapper {

        void apply();

        void revert();

        void revertToDefaults();
    }

    public static class BooleanSettingMapper implements SettingMapper {

        private AbstractButton button;

        private BooleanSetting setting;

        public BooleanSettingMapper(BooleanSetting setting, AbstractButton button) {
            this.setting = setting;
            this.button = button;
        }

        public void apply() {
            setting.setValue(button.isSelected());
        }

        public void revert() {
            button.setSelected(setting.getValue());
        }

        public void revertToDefaults() {
            button.setSelected(setting.getDefaultValue());
        }
    }

    public static class StringSettingMapper implements SettingMapper {

        private StringSetting setting;

        private JTextComponent textComponent;

        public StringSettingMapper(StringSetting setting, JTextComponent textComponent) {
            this.setting = setting;
            this.textComponent = textComponent;
        }

        public void apply() {
            setting.setValue(textComponent.getText());
        }

        public void revert() {
            textComponent.setText(setting.getValue());
        }

        public void revertToDefaults() {
            textComponent.setText(setting.getDefaultValue());
        }
    }

    public static class IntSettingMapper implements SettingMapper {

        private IntSetting setting;

        private SpinnerNumberModel spinnerModel;

        public IntSettingMapper(IntSetting setting, SpinnerNumberModel spinnerModel) {
            this.setting = setting;
            this.spinnerModel = spinnerModel;
        }

        public void apply() {
            setting.setValue(spinnerModel.getNumber().intValue());
        }

        public void revert() {
            spinnerModel.setValue(setting.getValue());
        }

        public void revertToDefaults() {
            spinnerModel.setValue(setting.getDefaultValue());
        }
    }

    public static class SelectionMapper<T> implements SettingMapper {

        private Setting<T> setting;

        private ComboBoxModel model;

        public SelectionMapper(Setting<T> setting, ComboBoxModel model) {
            this.setting = setting;
            this.model = model;
        }

        public void apply() {
            setting.setValue((T) model.getSelectedItem());
        }

        public void revert() {
            model.setSelectedItem(setting.getValue());
        }

        public void revertToDefaults() {
            model.setSelectedItem(setting.getDefaultValue());
        }
    }
}
