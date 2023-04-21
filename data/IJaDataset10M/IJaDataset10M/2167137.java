package interfaces.options;

import interfaces.GUISource;
import interfaces.menu.options.ControlOptionsWindow;
import org.fenggui.Button;
import org.fenggui.Display;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;

public class KeyOptionWidget extends OptionWidget {

    private Button keyButton;

    private Display display;

    private ControlOptionsWindow optionsWindow;

    public KeyOptionWidget(String initValue, String description, String label, Display display, ControlOptionsWindow optionsWindow) {
        super(description, label);
        this.display = display;
        this.optionsWindow = optionsWindow;
        keyButton = new Button(initValue);
        keyButton.getAppearance().setFont(GUISource.labelFont);
        keyButton.addButtonPressedListener(getButtonListener());
        addWidget(keyButton);
    }

    @Override
    public String getValue() {
        return keyButton.getText();
    }

    public void changeKey(String key) {
        keyButton.setText(key);
    }

    private IButtonPressedListener getButtonListener() {
        return new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                OptionKeyWindow keyWindow = new OptionKeyWindow(display, getThis());
                display.addWidget(keyWindow);
                keyWindow.changeSettings();
            }
        };
    }

    private KeyOptionWidget getThis() {
        return this;
    }

    public ControlOptionsWindow getOptionsWindow() {
        return optionsWindow;
    }
}
