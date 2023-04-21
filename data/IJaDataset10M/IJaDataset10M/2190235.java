package net.sf.doolin.gui.core.view.desktop;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import net.sf.doolin.gui.core.ViewContainer;
import net.sf.doolin.gui.core.validation.ValidationReport;
import net.sf.doolin.gui.core.view.AbstractView;
import net.sf.doolin.gui.core.view.WindowConstraint;

public class DesktopView extends AbstractView {

    private JDesktopPane desktopPane;

    public void init() {
        super.init();
        desktopPane = new JDesktopPane();
    }

    public JComponent getComponent() {
        return desktopPane;
    }

    public ViewContainer createViewContainer() {
        return new DesktopViewContainer();
    }

    public void addChild(ViewContainer viewContainer) {
        JInternalFrame frame = (JInternalFrame) viewContainer.getComponent();
        WindowConstraint constraint = (WindowConstraint) viewContainer.getViewConstraint();
        if (constraint == null) {
            constraint = new WindowConstraint();
        }
        frame.setClosable(constraint.isCloseable());
        frame.setIconifiable(constraint.isIconifiable());
        frame.setMaximizable(constraint.isMaximizable());
        frame.setResizable(constraint.isResizable());
        desktopPane.add(frame);
        super.addChild(viewContainer);
    }

    public void removeChild(ViewContainer viewContainer) {
        JInternalFrame frame = (JInternalFrame) viewContainer.getComponent();
        desktopPane.remove(frame);
        super.removeChild(viewContainer);
    }

    public void validate(ValidationReport validationReport) {
    }

    /**
	 * Delegates to the desktop.
	 * 
	 * @see JDesktopPane#setEnabled(boolean)
	 * @see net.sf.doolin.gui.core.View#setEnabled(boolean)
	 */
    public void setEnabled(boolean enabled) {
        desktopPane.setEnabled(enabled);
    }
}
