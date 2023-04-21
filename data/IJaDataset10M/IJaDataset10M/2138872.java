package org.plazmaforge.studio.reportdesigner.actions;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;
import org.plazmaforge.studio.reportdesigner.ReportDesignerPlugin;
import org.plazmaforge.studio.reportdesigner.ReportDesignerResources;
import org.plazmaforge.studio.reportdesigner.model.IBorderElement;
import org.plazmaforge.studio.reportdesigner.model.TextAlignment;
import org.plazmaforge.studio.reportdesigner.model.TextElement;

public class BorderBottomAction extends AbstractBorderAction {

    public static final String ID = BorderBottomAction.class.getName();

    public static final ImageDescriptor IMAGE_DESCRIPTOR = ReportDesignerPlugin.getImageDescriptor("icons/toolbar/enabled/border-bottom.gif");

    public static final ImageDescriptor DISABLED_IMAGE_DESCRIPTOR = ReportDesignerPlugin.getImageDescriptor("icons/toolbar/disabled/border-bottom.gif");

    public static final String NAME = ReportDesignerResources.Toggle_border_bottom;

    public BorderBottomAction(IWorkbenchPart part, int style) {
        super(part, style);
        initialize();
    }

    public BorderBottomAction(IWorkbenchPart part) {
        super(part);
        initialize();
    }

    private void initialize() {
        setImageDescriptor(IMAGE_DESCRIPTOR);
        setDisabledImageDescriptor(DISABLED_IMAGE_DESCRIPTOR);
        setText(NAME);
        setId(ID);
    }

    protected void performCheck(IBorderElement element) {
        setChecked(element.isBorderBottom());
    }

    public void run() {
        IBorderElement element = getSelectedBorderElement();
        if (element == null) {
            return;
        }
        boolean oldValue = element.isBorderBottom();
        boolean newValue = !element.isBorderBottom();
        executeSetValueCommand(element, "borderBottom", oldValue, newValue);
        updateBorder(element, newValue);
    }
}
