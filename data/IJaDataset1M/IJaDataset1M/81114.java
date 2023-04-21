package uk.ac.essex.common.gui.panel;

import uk.ac.essex.common.text.FloatDocument;
import javax.swing.*;

/**
 * A panel to allow the users to enter a growable array of floats
 * <br>
 * Date: May 6, 2002 <br>
 *
 * @author Laurence Smith
 *
 * You should have received a copy of GNU public license with this code.
 * If not please visit <a href="www.gnu.org/copyleft/gpl.html">this site </a>
 */
public class FloatListPanel extends NumberListPanel {

    /**
     *
     * @param label
     */
    public FloatListPanel(String label) {
        super(FloatDocument.class, label);
    }

    public Object getValue() {
        return getFloats();
    }

    public void setValue(Object value) {
        if (value instanceof float[]) setFloats((float[]) value);
    }

    /**
     *
     * @return
     */
    public float[] getFloats() {
        float[] floats = new float[jTextField.size()];
        for (int i = 0; i < jTextField.size(); i++) {
            JTextField tf = (JTextField) jTextField.get(i);
            try {
                floats[i] = Float.parseFloat(tf.getText());
            } catch (NumberFormatException e) {
                floats[i] = 0f;
            }
        }
        return floats;
    }

    /**
     *
     * @param floats
     */
    public void setFloats(float[] floats) {
        int floatLength = floats.length;
        int jTextFieldLength = jTextField.size();
        if (floatLength > jTextFieldLength) {
            int diff = floatLength - jTextFieldLength;
            for (int i = 0; i < diff; i++) {
                addRow();
            }
        }
        if (floatLength < jTextFieldLength) {
            int diff = jTextFieldLength - floatLength;
            for (int i = 0; i < diff; i++) {
                removeRow();
            }
        }
        for (int i = 0; i < floatLength; i++) {
            JTextField tf = (JTextField) jTextField.get(i);
            tf.setText(Float.toString(floats[i]));
        }
    }

    /** Main for testing */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        FloatListPanel panel = new FloatListPanel("Band");
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }
}
