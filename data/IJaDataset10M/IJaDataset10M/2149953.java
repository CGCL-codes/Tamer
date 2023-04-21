package jp.ekasi.pms.ui.ganttchart.gef.handles;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Locator;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.RelativeHandleLocator;
import org.eclipse.gef.handles.SquareHandle;
import org.eclipse.gef.tools.ResizeTracker;
import org.eclipse.swt.graphics.Cursor;

/**
 * @author Yuusuke Hikime
 */
public class ResizeHandle extends org.eclipse.gef.handles.ResizeHandle {

    /**
	 * �R���X�g���N�^.<br>
	 * @param owner
	 * @param direction
	 */
    public ResizeHandle(GraphicalEditPart owner, int direction) {
        super(owner, direction);
    }

    /**
	 * �R���X�g���N�^.<br>
	 * @param owner
	 * @param loc
	 * @param c
	 */
    public ResizeHandle(GraphicalEditPart owner, Locator loc, Cursor c) {
        super(owner, loc, c);
    }

    @Override
    public void paintFigure(Graphics g) {
    }
}
