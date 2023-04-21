package peterhi.platform.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.FillLayout;
import peterhi.platform.panels.WhiteboardPanel;

public class WhiteboardView extends ViewPart {

    public static final String ID = "peterhi.platform.views.WhiteboardView";

    private Composite top = null;

    @SuppressWarnings("unused")
    private WhiteboardPanel content = null;

    @Override
    public void createPartControl(Composite parent) {
        top = new Composite(parent, SWT.NONE);
        top.setLayout(new FillLayout());
        createContent();
    }

    @Override
    public void setFocus() {
    }

    /**
	 * This method initializes content	
	 *
	 */
    private void createContent() {
        content = new WhiteboardPanel(top, SWT.NONE);
    }
}
