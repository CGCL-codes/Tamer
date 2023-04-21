package net.sf.swinglib.field;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.PlainDocument;
import net.sf.swinglib.field.FieldValidator;
import junit.framework.TestCase;

public class TestFieldValidator extends TestCase {

    private JTextField _theField;

    private FieldValidator _validator;

    private volatile boolean _isValid;

    private volatile Color _currentColor;

    public void testReadOnlyValidator() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                _theField = new JTextField("blah");
                _validator = new FieldValidator(_theField, "bla*h");
                _isValid = _validator.isValid();
            }
        });
        assertTrue(_isValid);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                _theField.setText("argle");
                _isValid = _validator.isValid();
            }
        });
        assertFalse(_isValid);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                _theField.setText("blaaaaaaaah");
                _isValid = _validator.isValid();
            }
        });
        assertTrue(_isValid);
    }

    public void testHighlighting() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                _theField = new JTextField("blah");
                _validator = new FieldValidator(_theField, "bla*h", Color.red, Color.blue);
                _isValid = _validator.isValid();
                _currentColor = _theField.getBackground();
            }
        });
        assertTrue(_isValid);
        assertEquals(Color.blue, _currentColor);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                _theField.setText("argle");
                _isValid = _validator.isValid();
                _currentColor = _theField.getBackground();
            }
        });
        assertFalse(_isValid);
        assertEquals(Color.red, _currentColor);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                _theField.setText("blaaaaaaaah");
                _isValid = _validator.isValid();
                _currentColor = _theField.getBackground();
            }
        });
        assertTrue(_isValid);
        assertEquals(Color.blue, _currentColor);
    }

    public void testReset() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                _theField = new JTextField("blah");
                _validator = new FieldValidator(_theField, "bla*h");
                _isValid = _validator.isValid();
            }
        });
        assertTrue(_isValid);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                _theField.setDocument(new PlainDocument());
                _theField.setText("argle");
                _isValid = _validator.isValid();
            }
        });
        assertTrue(_isValid);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                _validator.reset();
                _isValid = _validator.isValid();
            }
        });
        assertFalse(_isValid);
    }
}
