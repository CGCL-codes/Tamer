package springclient.panel;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import springclient.DefaultSwingComponent;

public abstract class DefaultPanel<T> extends DefaultSwingComponent<T, JPanel> {

    public DefaultPanel(JComponent jComponent) {
        super(jComponent);
    }

    public void setBorder(Border border) {
        super.toSwing().setBorder(border);
    }

    public void setResizable(Boolean resizable) {
        if (!resizable) {
            super.toSwing().setMaximumSize(super.toSwing().getPreferredSize());
            super.toSwing().setMinimumSize(super.toSwing().getPreferredSize());
        }
    }
}
