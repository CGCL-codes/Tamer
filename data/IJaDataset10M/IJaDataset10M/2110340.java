package org.vikamine.gui.attribute.workspace;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.vikamine.app.VIKAMINE;
import de.d3web.kernel.psMethods.shared.AbnormalityNum;

/**
 * The Panel, where the User can edit an interval.
 * 
 * @author Tobias Vogele
 */
public class NumQuestionEditPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * The PropertyName of the Property-value, which is fired by the
     * FormattedTexfField, when the Validity is changed.
     */
    private static final String PROPERTY_VALIDITY = "editValid";

    /**
     * listens for the validity of the user-input.
     */
    class ValidListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            if (!evt.getPropertyName().equals(PROPERTY_VALIDITY)) {
                return;
            }
            Boolean bool = (Boolean) evt.getNewValue();
            updateErrorLabel(bool.booleanValue());
        }
    }

    /**
     * The errormessage, if the input is invalid, appears here.
     */
    private JLabel errorLabel = new JLabel();

    /**
     * The Input-Textfield.
     */
    private JFormattedTextField textfield;

    /**
     * 
     */
    public NumQuestionEditPanel() {
        super();
        initialize();
    }

    /**
     * 
     */
    private void initialize() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        errorLabel.setForeground(Color.red);
        add(errorLabel);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(VIKAMINE.I18N.getString("vikamine.selectedTree.editor.abnormality.insertInterval")), BorderLayout.WEST);
        panel.add(getTextfield(), BorderLayout.CENTER);
        add(panel);
    }

    public void setQuestion(AbnormalityNum abnormal) {
        getTextfield().setValue(abnormal);
    }

    public JFormattedTextField getTextfield() {
        if (textfield == null) {
            textfield = new JFormattedTextField(new IntervalFormatterFactory());
            textfield.setFocusLostBehavior(JFormattedTextField.PERSIST);
            textfield.addPropertyChangeListener(PROPERTY_VALIDITY, new ValidListener());
        }
        return textfield;
    }

    @Override
    public void requestFocus() {
        getTextfield().requestFocus();
    }

    /**
     * @param object
     */
    private void updateErrorLabel(boolean valid) {
        if (valid) {
            errorLabel.setText(" ");
        } else {
            String error = VIKAMINE.I18N.getString("vikamine.selectedTree.editor.abnormality.invalidInterval");
            errorLabel.setText(error);
        }
    }
}
