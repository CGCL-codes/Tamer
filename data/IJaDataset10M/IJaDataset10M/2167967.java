package org.springframework.richclient.samples.showcase.view;

import java.awt.FlowLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.springframework.richclient.application.support.AbstractView;
import org.springframework.util.ObjectUtils;

/**
 * @author Peter De Bruycker
 */
public class ViewWithInput extends AbstractView {

    private JTextField inputField;

    @Override
    protected JComponent createControl() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel label = new JLabel("The input: ");
        inputField = new JTextField(25);
        inputField.setEnabled(false);
        panel.add(label);
        panel.add(inputField);
        return panel;
    }

    @Override
    public void setInput(Object input) {
        inputField.setText(ObjectUtils.nullSafeToString(input));
    }
}
