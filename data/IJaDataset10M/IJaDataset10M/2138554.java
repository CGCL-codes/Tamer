package opt.base;

import javax.swing.JTextField;

public class SingleIntOption extends SingleBaseOption {

    int b_optionDefault;

    int b_optionValue;

    public SingleIntOption(String valueKey, String displayLabel, int defaultValue, int optionValue) {
        super(valueKey, displayLabel);
        initialize(defaultValue, optionValue, 10);
    }

    public SingleIntOption(String valueKey, String displayLabel, int defaultValue) {
        super(valueKey, displayLabel);
        initialize(defaultValue, defaultValue, 10);
    }

    private void initialize(int defaultValue, int optionValue, int maxLength) {
        setOptionDefault(defaultValue);
        setOptionValue(optionValue);
        setMaxEntryLength(maxLength);
        setGUIObjectData();
        setIsChanged(false);
    }

    @Override
    public Integer getOptionDefault() {
        return (b_optionDefault);
    }

    @Override
    public Integer getOptionValue() {
        return (b_optionValue);
    }

    @Override
    public void setOptionDefault(int defaultValue) {
        b_optionDefault = defaultValue;
    }

    @Override
    public void setOptionValue(int optionValue) {
        if (b_optionValue == optionValue) return;
        this.setIsChanged(true);
        b_optionValue = optionValue;
        setGUIObjectData();
    }

    @Override
    public String toString() {
        return (Integer.toString(b_optionValue));
    }

    public String getDatatype() {
        return ("int");
    }

    public boolean applyChanges() {
        setOptionValue(Integer.parseInt(((JTextField) b_guiObject).getText()));
        return (b_isChanged);
    }
}
