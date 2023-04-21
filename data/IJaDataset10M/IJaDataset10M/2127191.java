package xsearch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

public class ButtonGroupHide extends ButtonGroup {

    private AbstractButton defaultButton;

    private AbstractButton actualButton;

    public ButtonGroupHide() {
        super();
    }

    public void add(AbstractButton b) {
        super.add(b);
        if (this.getButtonCount() == 1) {
            defaultButton = b;
            actualButton = b;
        }
        if (b.isSelected()) actualButton = b;
        b.addActionListener(new ButtonGroupHideActionListener());
    }

    /**
	 * when a button inside the group shall be set selected, actualButton must be set
	 */
    public void setSelected(AbstractButton b, boolean value) {
        b.setSelected(value);
        if (value) actualButton = b; else {
            if (actualButton == b) {
                defaultButton.setSelected(true);
                actualButton = defaultButton;
            }
        }
    }

    class ButtonGroupHideActionListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            Object source = evt.getSource();
            if (source == actualButton) {
                defaultButton.setSelected(true);
                actualButton = defaultButton;
            } else {
                actualButton = (AbstractButton) source;
            }
        }
    }
}
