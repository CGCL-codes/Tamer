package edu.mit.lcs.haystack.ozone.core.utils;

import edu.mit.lcs.haystack.rdf.*;
import edu.mit.lcs.haystack.rdf.Resource;
import edu.mit.lcs.haystack.ozone.core.IGUIHandler;
import edu.mit.lcs.haystack.ozone.core.IPart;
import edu.mit.lcs.haystack.ozone.core.IVisualPart;
import edu.mit.lcs.haystack.ozone.core.ParentChildDropHandler;
import edu.mit.lcs.haystack.ozone.core.ParentChildFocusHandler;
import edu.mit.lcs.haystack.ozone.core.ParentChildMouseHandler;
import edu.mit.lcs.haystack.ozone.core.PartUtilities;
import edu.mit.lcs.haystack.ozone.core.VisualPartBase;
import edu.mit.lcs.haystack.ozone.standard.widgets.parts.PartConstants;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import java.util.*;

/**
 * This class is mostly useful for handling mouse event on
 * several child parts.
 * 
 * @version 	1.0
 * @author		David Huynh
 */
public abstract class ContainerPartBase extends VisualPartBase {

    protected List m_otherChildParts = new ArrayList();

    protected List m_childParts = new ArrayList();

    protected List m_childData = new ArrayList();

    protected IVisualPart m_previousVisualChildPart = null;

    protected Rectangle m_rect = new Rectangle(0, 0, 0, 0);

    protected ParentChildMouseHandler m_parentChildMouseHandler = new ParentChildMouseHandler() {

        protected IVisualPart getPreviousChildPart() {
            return m_previousVisualChildPart;
        }

        protected void setPreviousChildPart(IVisualPart vp) {
            m_previousVisualChildPart = vp;
        }

        protected IVisualPart hittest(int x, int y) {
            return ContainerPartBase.this.hittest(x, y);
        }
    };

    protected ParentChildDropHandler m_parentChildDropHandler = new ParentChildDropHandler() {

        protected IVisualPart getPreviousChildPart() {
            return m_previousVisualChildPart;
        }

        protected void setPreviousChildPart(IVisualPart vp) {
            m_previousVisualChildPart = vp;
        }

        protected IVisualPart hittest(int x, int y) {
            return ContainerPartBase.this.hittest(x, y);
        }
    };

    protected ParentChildFocusHandler m_parentChildFocusHandler = new ParentChildFocusHandler() {

        protected List getChildParts() {
            return m_childParts;
        }
    };

    /**
	 * @see IPart#dispose()
	 */
    public void dispose() {
        Iterator i;
        i = m_childParts.iterator();
        while (i.hasNext()) {
            IVisualPart childPart = (IVisualPart) i.next();
            childPart.dispose();
        }
        m_childParts.clear();
        m_childParts = null;
        i = m_otherChildParts.iterator();
        while (i.hasNext()) {
            IPart childPart = (IPart) i.next();
            childPart.dispose();
        }
        m_otherChildParts.clear();
        m_otherChildParts = null;
        m_childData.clear();
        m_childData = null;
        m_rect = null;
        super.dispose();
    }

    /**
	 * @see IGUIHandler@setVisible(boolean)
	 */
    public void setVisible(boolean visible) {
        for (int i = 0; i < m_childParts.size(); i++) {
            IVisualPart vp = (IVisualPart) m_childParts.get(i);
            IGUIHandler guiHandler = vp.getGUIHandler(null);
            if (guiHandler != null) {
                guiHandler.setVisible(visible);
            }
        }
    }

    /**
	 * @see VisualPartBase#onMouseEvent(Resource, MouseEvent)
	 */
    protected boolean onMouseEvent(Resource eventType, MouseEvent event) {
        if (eventType.equals(PartConstants.s_eventMouseUp) && event.button == 3) {
            prepareContextMenu(event);
        }
        if (PartUtilities.filterFakeDropEvents(eventType, event, m_context, m_source, this)) {
            return true;
        }
        boolean r = m_parentChildMouseHandler.letChildrenHandleEvent(eventType, event);
        if (!r) {
            r = super.onMouseEvent(eventType, event);
        } else {
            onChildHasHandledMouseEvent(event);
        }
        return r;
    }

    protected void onChildHasHandledMouseEvent(MouseEvent event) {
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.VisualPartBase#onContentHittest(org.eclipse.swt.events.MouseEvent)
	 */
    protected boolean onContentHittest(ContentHittestEvent e) {
        IVisualPart vp = hittest(e.m_x, e.m_y);
        boolean r = false;
        if (vp != null) {
            r = vp.handleEvent(PartConstants.s_eventContentHittest, e);
        }
        if (!r) {
            r = handleContentHittest(e);
        }
        return r;
    }

    protected boolean handleContentHittest(ContentHittestEvent e) {
        return false;
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.VisualPartBase#onDrag(DragSourceEvent)
	 */
    protected boolean onDrag(DragSourceEvent e) {
        Point p = (Point) e.data;
        IVisualPart childPart = hittest(p.x, p.y);
        boolean r = false;
        if (childPart != null) {
            r = childPart.handleEvent(PartConstants.s_eventDrag, e);
        }
        if (!r) {
            r = super.onDrag(e);
        }
        return r;
    }

    /**
	 * @see VisualPartBase#onDropTargetEvent(Resource, DropTargetEvent)
	 */
    protected boolean onDropTargetEvent(Resource eventType, edu.mit.lcs.haystack.ozone.core.OzoneDropTargetEvent event) {
        boolean r = m_parentChildDropHandler.letChildrenHandleEvent(eventType, event);
        if (!r) {
            r = super.onDropTargetEvent(eventType, event);
        } else {
            onChildHasHandledDragAndDropEvent(eventType, event);
        }
        return r;
    }

    protected void onChildHasHandledDragAndDropEvent(Resource eventType, edu.mit.lcs.haystack.ozone.core.OzoneDropTargetEvent event) {
        if (m_dropOperations != DND.DROP_NONE) {
            handleEvent(PartConstants.s_eventContentHighlight, new ContentHighlightEvent(this, false));
            m_dropOperations = DND.DROP_NONE;
        }
    }

    protected abstract IVisualPart hittest(int x, int y);

    protected void cacheBounds(Rectangle r) {
        m_rect.x = r.x;
        m_rect.y = r.y;
        m_rect.width = r.width;
        m_rect.height = r.height;
    }

    protected boolean onGotInputFocus(FocusEvent e) {
        return m_parentChildFocusHandler.letChildHandleGotFocusEvent(e);
    }

    protected boolean onLostInputFocus(FocusEvent e) {
        return m_parentChildFocusHandler.letChildHandleLostFocusEvent(e);
    }

    public void initializeFromDeserialization(IRDFContainer source) {
        super.initializeFromDeserialization(source);
        Iterator i = m_childParts.iterator();
        while (i.hasNext()) {
            IPart part = (IPart) i.next();
            part.initializeFromDeserialization(source);
        }
        i = m_otherChildParts.iterator();
        while (i.hasNext()) {
            IPart part = (IPart) i.next();
            part.initializeFromDeserialization(source);
        }
    }
}
