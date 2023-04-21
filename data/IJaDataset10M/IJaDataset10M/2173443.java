package peterhi.platform.panels;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class WhiteboardPanel extends Composite {

    private Canvas whiteboard = null;

    public WhiteboardPanel(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        createWhiteboard();
        setSize(new Point(300, 200));
        setLayout(new FillLayout());
    }

    /**
	 * This method initializes whiteboard	
	 *
	 */
    private void createWhiteboard() {
        whiteboard = new Canvas(this, SWT.NONE);
        whiteboard.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
    }
}
