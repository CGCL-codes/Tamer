package carassius.GUI;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 *
 * @author siebz0r
 */
public class IntegerTextField extends JTextField {

    private boolean _isValid;

    private final Pattern _textPattern = Pattern.compile("\\A(-)?\\d+\\z");

    public IntegerTextField() {
        _isValid = true;
    }

    public boolean textIsValid() {
        return _isValid;
    }

    @Override
    public void setText(String t) {
        super.setText(t);
        validateText();
    }

    @Override
    public String getText() {
        if (_isValid) {
            return super.getText();
        } else {
            return null;
        }
    }

    public Integer getValue() {
        String text = super.getText();
        validateText();
        if (_isValid && text != null && !text.isEmpty()) {
            return Integer.parseInt(super.getText());
        } else {
            return null;
        }
    }

    public void setValue(Integer i) {
        setText((i != null) ? i.toString() : null);
    }

    private void validateText() {
        if (super.getText() == null || super.getText().isEmpty() || _textPattern.matcher(super.getText()).matches()) {
            _isValid = true;
            Object border = UIManager.get("TextField.border");
            if (border instanceof Border) {
                setBorder((Border) border);
            }
        } else {
            _isValid = false;
            setBorder(new LineBorder(Color.RED, 1));
        }
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        validateText();
        super.processKeyEvent(e);
    }
}
