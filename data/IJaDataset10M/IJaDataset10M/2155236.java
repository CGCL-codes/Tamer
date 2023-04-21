package sipinspector.Utils;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

/**
 *
 * @author Zarko Coklin
 */
public class PortValidator extends InputVerifier {

    public PortValidator() {
    }

    public boolean verify(JComponent input) {
        int value;
        JTextField txtField = (JTextField) input;
        if (txtField.isEnabled() == true) {
            try {
                value = Integer.parseInt(txtField.getText());
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(input, "Port must be a numeric value", "Port validation", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (value <= 0 || value > 65535) {
                JOptionPane.showMessageDialog(input, "Port must be within range [1..65535]", "Port range validation", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
}
