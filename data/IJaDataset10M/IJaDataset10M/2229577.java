package edu.mit.lcs.haystack.ozone.standard.layout;

import java.io.IOException;
import edu.mit.lcs.haystack.ozone.standard.widgets.parts.PartConstants;
import edu.mit.lcs.haystack.ozone.standard.widgets.parts.ViewContainerPart;
import edu.mit.lcs.haystack.ozone.standard.widgets.slide.BlockElement;
import edu.mit.lcs.haystack.ozone.standard.widgets.slide.SlideUtilities;
import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.rdf.Resource;
import edu.mit.lcs.haystack.ozone.core.BlockScreenspace;
import edu.mit.lcs.haystack.ozone.core.HTMLengine;
import edu.mit.lcs.haystack.ozone.core.IBlockGUIHandler;
import edu.mit.lcs.haystack.ozone.core.IGUIHandler;
import edu.mit.lcs.haystack.ozone.core.utils.graphics.GraphicsManager;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;

/**
 * @author David Huynh
 */
public abstract class BlockViewContainerPart extends ViewContainerPart implements IBlockGUIHandler {

    public IBlockGUIHandler m_blockGUIHandler;

    Rectangle m_rect = new Rectangle(0, 0, 0, 0);

    boolean m_focused = false;

    boolean m_selected = false;

    transient Color m_normalBackground;

    transient Color m_highlightBackground;

    transient Color m_foreground;

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        BlockElement.writeColor(out, m_normalBackground);
        BlockElement.writeColor(out, m_highlightBackground);
        BlockElement.writeColor(out, m_foreground);
    }

    private transient RGB m_normalBackgroundRGB;

    private transient RGB m_highlightBackgroundRGB;

    private transient RGB m_foregroundRGB;

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        m_normalBackgroundRGB = (RGB) in.readObject();
        m_highlightBackgroundRGB = (RGB) in.readObject();
        m_foregroundRGB = (RGB) in.readObject();
    }

    public void initializeFromDeserialization(IRDFContainer source) {
        super.initializeFromDeserialization(source);
        if (m_normalBackgroundRGB != null) {
            m_normalBackground = GraphicsManager.acquireColorByRGB(m_normalBackgroundRGB);
        }
        if (m_highlightBackgroundRGB != null) {
            m_highlightBackground = GraphicsManager.acquireColorByRGB(m_highlightBackgroundRGB);
        }
        if (m_foregroundRGB != null) {
            m_foreground = GraphicsManager.acquireColorByRGB(m_foregroundRGB);
        }
    }

    public void setFocused(boolean focused) {
        m_focused = focused;
    }

    public boolean getFocused() {
        return m_focused;
    }

    public void setSelected(boolean selected) {
        m_selected = selected;
    }

    public boolean getSelected() {
        return m_selected;
    }

    public void update() {
        m_blockGUIHandler = m_child == null ? null : (IBlockGUIHandler) m_child.getGUIHandler(IBlockGUIHandler.class);
    }

    protected void internalInitialize() {
        super.internalInitialize();
        Color bgcolor = SlideUtilities.getAmbientBgcolor(m_context);
        m_foreground = GraphicsManager.acquireColor("80%", SlideUtilities.getAmbientColor(m_context));
        m_normalBackground = GraphicsManager.acquireColorBySample(bgcolor);
        m_highlightBackground = GraphicsManager.acquireColor("92%", bgcolor);
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.IPart#dispose()
	 */
    public void dispose() {
        GraphicsManager.releaseColor(m_foreground);
        GraphicsManager.releaseColor(m_normalBackground);
        GraphicsManager.releaseColor(m_highlightBackground);
        m_foreground = null;
        m_normalBackground = null;
        m_highlightBackground = null;
        super.dispose();
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.IPart#handleEvent(Resource, Object)
	 */
    public boolean handleEvent(Resource eventType, Object event) {
        if (eventType.equals(PartConstants.s_eventChildResize)) {
            update();
        }
        boolean result = super.handleEvent(eventType, event);
        if (eventType.equals(PartConstants.s_eventMouseUp) && !result) {
            result = onMouseUp((MouseEvent) event);
        }
        return result;
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.IVisualPart#getGUIHandler(Class)
	 */
    public IGUIHandler getGUIHandler(Class cls) {
        if (cls == null || cls.equals(IBlockGUIHandler.class)) {
            m_blockGUIHandler = m_child == null ? null : (IBlockGUIHandler) m_child.getGUIHandler(IBlockGUIHandler.class);
            return this;
        } else {
            m_blockGUIHandler = null;
            return null;
        }
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.IBlockGUIHandler#calculateSize(int, int)
	 */
    public BlockScreenspace calculateSize(int hintedWidth, int hintedHeight) {
        if (m_blockGUIHandler != null) {
            return m_blockGUIHandler.calculateSize(hintedWidth, hintedHeight);
        }
        return null;
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.IBlockGUIHandler#draw(GC, Rectangle)
	 */
    public void draw(GC gc, Rectangle r) {
        Color background = gc.getBackground();
        Color foreground = gc.getForeground();
        int lineStyle = gc.getLineStyle();
        int lineWidth = gc.getLineWidth();
        gc.setLineStyle(SWT.LINE_SOLID);
        gc.setLineWidth(1);
        if (m_selected) {
            gc.setBackground(m_highlightBackground);
        } else {
            gc.setBackground(m_normalBackground);
        }
        gc.fillRectangle(r);
        if (m_blockGUIHandler != null) {
            m_blockGUIHandler.draw(gc, r);
        } else {
            gc.setForeground(m_highlightBackground);
            int x = r.x - r.height;
            while (x < r.x + r.width) {
                gc.drawLine(x, r.y, x + r.height, r.y + r.height);
                gc.drawLine(x + 1, r.y, x + 1 + r.height, r.y + r.height);
                x += 4;
            }
        }
        if (m_focused) {
            gc.setForeground(m_foreground);
            gc.drawRectangle(r.x, r.y, r.width - 1, r.height - 1);
        }
        gc.setBackground(background);
        gc.setForeground(foreground);
        gc.setLineStyle(lineStyle);
        gc.setLineWidth(lineWidth);
    }

    /**
	 * @see IBlockGUIHandler#renderHTML(HTMLengine he)
	 */
    public void renderHTML(HTMLengine he) {
        he.enter("BlockViewContainerPart");
        if (m_blockGUIHandler != null) m_blockGUIHandler.renderHTML(he);
        he.exit("BlockViewContainerPart");
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.IBlockGUIHandler#getFixedSize()
	 */
    public BlockScreenspace getFixedSize() {
        if (m_blockGUIHandler != null) {
            return m_blockGUIHandler.getFixedSize();
        }
        return null;
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.IBlockGUIHandler#getHintedDimensions()
	 */
    public int getHintedDimensions() {
        if (m_blockGUIHandler != null) {
            return m_blockGUIHandler.getHintedDimensions();
        }
        return IBlockGUIHandler.BOTH;
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.IBlockGUIHandler#getTextAlign()
	 */
    public int getTextAlign() {
        if (m_blockGUIHandler != null) {
            return m_blockGUIHandler.getTextAlign();
        }
        return BlockScreenspace.ALIGN_TEXT_CLEAR;
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.IBlockGUIHandler#setBounds(Rectangle)
	 */
    public void setBounds(Rectangle r) {
        m_rect.x = r.x;
        m_rect.y = r.y;
        m_rect.width = r.width;
        m_rect.height = r.height;
        if (m_blockGUIHandler != null) {
            m_blockGUIHandler.setBounds(r);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(m_rect.x, m_rect.y, m_rect.width, m_rect.height);
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.standard.widgets.parts.ViewContainerPart#initializeChild()
	 */
    protected synchronized void initializeChild() {
        super.initializeChild();
        onNavigationComplete();
    }

    protected abstract void onNavigationComplete();

    protected abstract boolean onMouseUp(MouseEvent e);

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.IGUIHandler#setVisible(boolean)
	 */
    public void setVisible(boolean visible) {
        if (m_blockGUIHandler != null) {
            m_blockGUIHandler.setVisible(visible);
        }
    }
}
