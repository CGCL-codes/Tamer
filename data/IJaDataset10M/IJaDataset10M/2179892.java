package softwarekompetenz.gui;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class IconContainer extends JPanel {

    public IconContainer() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    }

    public void addButton(JComponent button) {
        add(Box.createRigidArea(new Dimension(2, 0)));
        add(button);
    }

    public void addSpace() {
        add(Box.createRigidArea(new Dimension(7, 0)));
    }
}
