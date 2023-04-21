package com.bluebrim.layout.impl.client.tools;

import java.awt.event.*;
import com.bluebrim.layout.impl.client.*;
import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.client.editor.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Implementation of "selection mode" (the default tool)
 * 
 * @author: Dennis Malmstr�m
 */
public class CoContentTool extends CoSelectionTool {

    CoContentWrapperPageItemView m_contentView = null;

    public CoContentTool(CoLayoutEditor pageItemEditor) {
        super(pageItemEditor);
    }

    public CoContentWrapperPageItemView getContentWrapperView() {
        return m_contentView;
    }

    public CoTool keyPressed(KeyEvent e) {
        if ((e.getModifiers() & MouseEvent.CTRL_MASK) != 0) {
            return super.keyPressed(e);
        }
        if (m_contentView != null) {
            CoPageItemContentView c = m_contentView.getContentView();
            if (c instanceof CoPageItemBoundedContentView) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        twitch((CoPageItemImageContentView) c, 0, ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) ? -1 : -10);
                        return this;
                    case KeyEvent.VK_DOWN:
                        twitch((CoPageItemImageContentView) c, 0, ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) ? 1 : 10);
                        return this;
                    case KeyEvent.VK_LEFT:
                        twitch((CoPageItemImageContentView) c, ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) ? -1 : -10, 0);
                        return this;
                    case KeyEvent.VK_RIGHT:
                        twitch((CoPageItemImageContentView) c, ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) ? 1 : 10, 0);
                        return this;
                    default:
                }
            }
        }
        return super.keyPressed(e);
    }

    public CoTool mouseMoved(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.CTRL_MASK) != 0) {
            return super.mouseMoved(e);
        }
        CoTool t = super.mouseMoved(e);
        CoPageItemView tmp = m_viewPanel.getRootView().findTopMostViewContaining(getLocation(e), null, false, true, -1);
        if (tmp instanceof CoContentWrapperPageItemView) {
            m_contentView = (CoContentWrapperPageItemView) tmp;
            m_viewPanel.setCursor(m_contentView.getContentCursor());
            return this;
        } else {
            m_contentView = null;
            return t;
        }
    }

    public CoTool mousePressed(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.CTRL_MASK) != 0) {
            return super.mousePressed(e);
        }
        if (m_contentView != null) {
            CoPageItemContentView c = m_contentView.getContentView();
            if (c instanceof CoPageItemAbstractTextContentView) {
                CoPageItemAbstractTextContentIF pi = (CoPageItemAbstractTextContentIF) c.getPageItem();
                if ((e.getModifiers() & InputEvent.SHIFT_MASK) == 0) {
                    return m_editor.activateTextEditTool(e);
                } else {
                    return this;
                }
            } else if (c instanceof CoPageItemNoContentView) {
                return this;
            } else if (c instanceof CoPageItemBoundedContentView) {
                CoPageItemBoundedContentView iv = (CoPageItemBoundedContentView) c;
                if (iv.hasContent()) {
                    return new CoMoveBoundedContentTool(this, m_editor, iv);
                } else {
                    return this;
                }
            }
        }
        return super.mousePressed(e);
    }

    private void twitch(final CoPageItemBoundedContentView v, final double dx, final double dy) {
        CoPageItemCommands.SET_BOUNDED_CONTENT_POSITION.prepare(v.getOwner(), v.getX() + dx, v.getY() + dy);
        m_editor.getCommandExecutor().doit(CoPageItemCommands.SET_BOUNDED_CONTENT_POSITION, null);
    }

    public String getName() {
        return CoPageItemUIStringResources.getName("CONTENT_TOOL");
    }
}
