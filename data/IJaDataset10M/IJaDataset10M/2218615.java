package uk.ac.leeds.comp.ui.bridge;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import uk.ac.leeds.comp.ui.base.UIComponent;

public class JScrollPaneAsUIComponent extends JScrollPane implements UIComponent, SwingUIComponent {

    private static final long serialVersionUID = 7068773341703090544L;

    public JScrollPaneAsUIComponent() {
        super();
    }

    public JScrollPaneAsUIComponent(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
    }

    public JScrollPaneAsUIComponent(Component view) {
        super(view);
    }

    public JScrollPaneAsUIComponent(int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
    }

    public Component getAsAWTComponent() {
        return this;
    }

    public JComponent getAsJComponent() {
        return this;
    }
}
