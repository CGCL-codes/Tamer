package eyetrackercalibrator.gui.util;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * This verifyer check if the field is empty or has a positive number in it
 * @author SQ
 */
public class TextFieldEmptyPositiveDoubleInputVerifier extends InputVerifier {

    @Override
    public boolean verify(JComponent input) {
        JTextField tf = (JTextField) input;
        String text = tf.getText().trim();
        if (text.length() == 0) {
            return true;
        }
        double i = 0;
        try {
            i = Double.parseDouble(text);
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
        return i > 0;
    }
}
