package au.edu.qut.yawl.editor.swing;

import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.JButton;

public class YAWLToolBarButton extends JButton {

    private static final Insets margin = new Insets(0, 0, 0, 0);

    public YAWLToolBarButton(Action a) {
        super(a);
        setText(null);
        setMnemonic(0);
        setMargin(margin);
        setMaximumSize(getPreferredSize());
    }

    public Point getToolTipLocation(MouseEvent e) {
        return new Point(0, getSize().height);
    }

    public void setEnabled(boolean enabled) {
        if (getAction() instanceof TooltipTogglingWidget) {
            TooltipTogglingWidget action = (TooltipTogglingWidget) this.getAction();
            if (enabled) {
                setToolTipText(action.getEnabledTooltipText());
            } else {
                setToolTipText(action.getDisabledTooltipText());
            }
        }
        super.setEnabled(enabled);
    }
}
