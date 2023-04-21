package org.clico.swing.impl.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import org.clico.swing.view.EasyFormLayout;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class EasyButtonBarImpl implements EasyFormLayout.EasyButtonBar {

    private final EasyFormLayoutImpl layout;

    private JPanel buttonPanel;

    public EasyButtonBarImpl(EasyFormLayoutImpl layout) {
        this.layout = layout;
    }

    /**
	 * Fills the button bar with the help, ok and cancel buttons
	 * 
	 * @param help
	 * @param ok
	 * @param cancel
	 * @return
	 */
    public EasyButtonBarImpl helpOKCancel(JButton help, JButton ok, JButton cancel) {
        buttonPanel = ButtonBarFactory.buildHelpOKCancelBar(help, ok, cancel);
        return this;
    }

    /**
     * Fills the button bar with the ok button
     * 
     * @param ok
     * @return
     */
    public EasyButtonBarImpl ok(JButton ok) {
        buttonPanel = ButtonBarFactory.buildOKBar(ok);
        return this;
    }

    public void println() {
        if (buttonPanel == null) {
            throw new IllegalStateException("Button bar has not been filled with buttons.");
        }
        DefaultFormBuilder formBuilder = layout.getFormBuilder();
        formBuilder.append(buttonPanel, formBuilder.getColumnCount());
    }
}
