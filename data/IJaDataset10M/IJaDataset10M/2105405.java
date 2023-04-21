package interfaces.options;

import interfaces.GUISource;
import java.util.List;
import org.fenggui.ComboBox;

public class ComboBoxOptionWidget extends OptionWidget {

    private ComboBox<String> comboBox;

    public ComboBoxOptionWidget(String initValue, String description, String label, List<String> values) {
        super(description, label);
        comboBox = new ComboBox<String>();
        for (String value : values) {
            comboBox.addItem(value);
        }
        comboBox.setSelected(initValue);
        comboBox.getLabel().getAppearance().setFont(GUISource.labelFont);
        addWidget(comboBox);
    }

    @Override
    public String getValue() {
        return comboBox.getSelectedValue();
    }
}
