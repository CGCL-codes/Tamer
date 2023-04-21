package edu.mit.lcs.haystack.ozone.standard.layout;

import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.ozone.core.BlockScreenspace;
import edu.mit.lcs.haystack.ozone.core.Context;
import edu.mit.lcs.haystack.ozone.core.HTMLengine;
import edu.mit.lcs.haystack.ozone.core.IBlockGUIHandler;
import edu.mit.lcs.haystack.ozone.core.IVisualPart;
import edu.mit.lcs.haystack.ozone.core.utils.graphics.GraphicsManager;
import org.eclipse.swt.graphics.*;

/**
 * @author David Huynh
 */
public class RowSplitterLayoutManager extends SplitterLayoutManagerBase {

    static org.apache.log4j.Logger s_logger = org.apache.log4j.Logger.getLogger(RowSplitterLayoutManager.class);

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.IPart#initialize(IRDFContainer, Context)
	 */
    public void initialize(IRDFContainer source, Context context) {
        super.initialize(source, context);
        m_resizeCursor = GraphicsManager.s_vertResizeCursor;
    }

    public void initializeFromDeserialization(IRDFContainer source) {
        super.initializeFromDeserialization(source);
        m_resizeCursor = GraphicsManager.s_vertResizeCursor;
    }

    /**
	 * @see IBlockGUIHandler#getHintedDimensions()
	 */
    public int getHintedDimensions() {
        return IBlockGUIHandler.HEIGHT;
    }

    /**
	 * @see IBlockGUIHandler#getTextAlign()
	 */
    public int getTextAlign() {
        return m_calculatedBS.m_align;
    }

    /**
	 * @see IBlockGUIHandler#setBounds(Rectangle)
	 */
    public void setBounds(Rectangle r) {
        internalSetBounds(r, null);
    }

    protected void internalSetBounds(Rectangle r, Region region) {
        boolean sizeChanged = r.height != m_bounds.height;
        m_bounds.x = r.x;
        m_bounds.y = r.y;
        m_bounds.width = r.width;
        m_bounds.height = r.height;
        if (r.height != m_calculatedBS.m_size.y) {
            internalCalculateSize(m_bounds.width, m_bounds.height, true);
        }
        int[] heights = new int[m_elements.size()];
        int accumulatedHeight = 0;
        for (int i = 0; i < m_elements.size(); i++) {
            Element elmt = (Element) m_elements.get(i);
            if (elmt.m_initializedSize && elementResizable(elmt) && !sizeChanged) {
                heights[i] = elmt.m_bounds.height;
            } else if (elmt.m_blockScreenspace != null) {
                heights[i] = elmt.m_blockScreenspace.m_size.y;
            } else {
                heights[i] = 0;
            }
            accumulatedHeight += heights[i];
        }
        if (m_fillElement > -1) {
            heights[m_fillElement] = Math.max(heights[m_fillElement] - (accumulatedHeight - r.height), 0);
        }
        Rectangle bounds = new Rectangle(0, 0, 0, 0);
        accumulatedHeight = 0;
        for (int i = 0; i < m_elements.size(); i++) {
            Element elmt = (Element) m_elements.get(i);
            bounds.y = r.y + Math.min(accumulatedHeight, r.height);
            bounds.x = r.x;
            bounds.width = r.width;
            bounds.height = Math.min(heights[i], Math.max((r.height - accumulatedHeight), 0));
            IVisualPart vp = (IVisualPart) m_visualChildParts.get(i);
            IBlockGUIHandler blockGUIHandler = (IBlockGUIHandler) vp.getGUIHandler(IBlockGUIHandler.class);
            boolean childResized = m_childrenToResize.contains(vp);
            if (blockGUIHandler != null && (!elmt.m_initializedSize || !elmt.m_bounds.equals(bounds) || childResized)) {
                elmt.m_bounds.x = bounds.x;
                elmt.m_bounds.y = bounds.y;
                elmt.m_bounds.width = bounds.width;
                elmt.m_bounds.height = bounds.height;
                elmt.m_initializedSize = (elmt.m_blockScreenspace != null);
                blockGUIHandler.setBounds(elmt.m_bounds);
                if (region != null) {
                    region.add(bounds);
                }
            }
            accumulatedHeight += elmt.m_bounds.height;
        }
    }

    /**
	 * @see IBlockGUIHandler#calculateSize(int, int)
	 */
    public BlockScreenspace calculateSize(int hintedWidth, int hintedHeight) {
        if (hintedHeight < 0) {
            return null;
        }
        if (hintedHeight != m_calculatedBS.m_size.y || (hintedWidth != -1 && hintedWidth != m_calculatedBS.m_size.x)) {
            internalCalculateSize(hintedWidth, hintedHeight, true);
        }
        return new BlockScreenspace(m_calculatedBS);
    }

    /**
	 * @see SplitterLayoutManagerBase#redistributeElements()
	 */
    protected void redistributeElements() {
        internalCalculateSize(m_bounds.width, m_bounds.height, false);
    }

    protected BlockScreenspace internalCalculateSize(int hintedWidth, int hintedHeight, boolean forceRedistribute) {
        int i;
        int accumulatedHeight = 0;
        for (i = 0; i < m_elements.size(); i++) {
            if (i == m_fillElement) {
                continue;
            }
            Element elmt = (Element) m_elements.get(i);
            IVisualPart vp = (IVisualPart) m_visualChildParts.get(i);
            IBlockGUIHandler blockGUIHandler = (IBlockGUIHandler) vp.getGUIHandler(IBlockGUIHandler.class);
            if (blockGUIHandler == null) {
                continue;
            }
            int height = hintedHeight / m_elements.size();
            if (elmt.m_constraint != null) {
                if (elmt.m_constraint.m_ratio < 0) {
                    height = elmt.m_constraint.m_pixels;
                } else {
                    height = (int) (hintedHeight * elmt.m_constraint.m_ratio);
                }
            }
            if (forceRedistribute || elmt.m_needsRecalculation || !elmt.m_initializedSize || !elementResizable(elmt)) {
                elmt.m_blockScreenspace = calculateElementSize(blockGUIHandler, hintedWidth, height);
                elmt.m_needsRecalculation = false;
            }
            if (elmt.m_initializedSize && elementResizable(elmt)) {
                if (forceRedistribute) {
                    accumulatedHeight += elmt.m_blockScreenspace.m_size.y;
                } else {
                    accumulatedHeight += elmt.m_bounds.height;
                }
            } else if (elmt.m_blockScreenspace != null) {
                accumulatedHeight += elmt.m_blockScreenspace.m_size.y;
            }
        }
        if (m_fillElement >= 0) {
            Element elmt = (Element) m_elements.get(m_fillElement);
            IVisualPart vp = (IVisualPart) m_visualChildParts.get(m_fillElement);
            IBlockGUIHandler blockGUIHandler = (IBlockGUIHandler) vp.getGUIHandler(IBlockGUIHandler.class);
            if (blockGUIHandler != null) {
                int height = hintedHeight - accumulatedHeight;
                elmt.m_blockScreenspace = calculateElementSize(blockGUIHandler, hintedWidth, height);
                elmt.m_needsRecalculation = false;
            }
        }
        m_calculatedBS.m_size.x = 0;
        m_calculatedBS.m_size.y = 0;
        for (i = 0; i < m_elements.size(); i++) {
            Element elmt = (Element) m_elements.get(i);
            if (elmt.m_initializedSize || elementResizable(elmt)) {
                if (forceRedistribute) {
                    BlockScreenspace bs = elmt.m_blockScreenspace;
                    if (bs != null) {
                        m_calculatedBS.m_size.y += bs.m_size.y;
                        m_calculatedBS.m_size.x = Math.max(m_calculatedBS.m_size.x, bs.m_size.x);
                    }
                } else {
                    m_calculatedBS.m_size.y += elmt.m_bounds.height;
                    m_calculatedBS.m_size.x = Math.max(m_calculatedBS.m_size.x, elmt.m_bounds.width);
                }
            } else {
                BlockScreenspace bs = elmt.m_blockScreenspace;
                if (bs != null) {
                    m_calculatedBS.m_size.y += bs.m_size.y;
                    m_calculatedBS.m_size.x = Math.max(m_calculatedBS.m_size.x, bs.m_size.x);
                }
            }
        }
        m_calculatedBS.m_align = BlockScreenspace.ALIGN_TEXT_CLEAR;
        return m_calculatedBS;
    }

    protected BlockScreenspace calculateElementSize(IBlockGUIHandler blockGUIHandler, int hintedWidth, int hintedHeight) {
        switch(blockGUIHandler.getHintedDimensions()) {
            case IBlockGUIHandler.FIXED_SIZE:
                return blockGUIHandler.getFixedSize();
            case IBlockGUIHandler.WIDTH:
                return blockGUIHandler.calculateSize(hintedWidth < 0 ? hintedHeight : hintedWidth, hintedHeight);
            default:
                return blockGUIHandler.calculateSize(hintedWidth, hintedHeight);
        }
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.standard.layout.SplitterLayoutManagerBase#hittestResizeElement(int, int)
	 */
    protected int hittestResizeElement(int x, int y) {
        int result = -1;
        int resizables = 0;
        boolean nearBorder = false;
        for (int i = 0; i < m_elements.size(); i++) {
            Element elmt = (Element) m_elements.get(i);
            if (elementResizable(elmt)) {
                if (nearBorder) {
                    resizables++;
                } else {
                    result = i;
                }
            }
            if (Math.abs(elmt.m_bounds.y + elmt.m_bounds.height - y) < s_resizeHotspotWidth) {
                nearBorder = true;
            }
        }
        return nearBorder && resizables > 0 ? result : -1;
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.standard.layout.SplitterLayoutManagerBase#onStartResizing()
	 */
    protected int m_heightBeforeResizing;

    protected int m_resizeElement2;

    protected void onStartResizing() {
        Element elmt = (Element) m_elements.get(m_resizeElement);
        m_heightBeforeResizing = elmt.m_bounds.height;
        for (int i = m_resizeElement + 1; i < m_elements.size(); i++) {
            elmt = (Element) m_elements.get(i);
            if (elementResizable(elmt)) {
                m_resizeElement2 = i;
                break;
            }
        }
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.standard.layout.SplitterLayoutManagerBase#onResizing(int, int)
	 */
    protected void onResizing(int x, int y) {
        if (m_resizeElement < 0) {
            return;
        }
        Element elmt = (Element) m_elements.get(m_resizeElement);
        Element elmt2 = (Element) m_elements.get(m_resizeElement2);
        int originalDiff = y - m_resizeInitialPoint.y;
        int newHeight = Math.max(2 * s_resizeHotspotWidth, m_heightBeforeResizing + originalDiff);
        int currentDiff = newHeight - elmt.m_bounds.height;
        elmt2.m_bounds.height -= currentDiff;
        if (elmt2.m_blockScreenspace != null) {
            elmt2.m_blockScreenspace.m_size.y = elmt2.m_bounds.height;
        }
        elmt.m_bounds.height = newHeight;
        if (elmt.m_blockScreenspace != null) {
            elmt.m_blockScreenspace.m_size.y = elmt.m_bounds.height;
        }
        for (int i = m_resizeElement + 1; i <= m_resizeElement2; i++) {
            Element elmt3 = (Element) m_elements.get(i);
            elmt3.m_bounds.y += currentDiff;
        }
        for (int i = m_resizeElement; i <= m_resizeElement2; i++) {
            IVisualPart vp = (IVisualPart) m_visualChildParts.get(i);
            IBlockGUIHandler blockGUIHandler = (IBlockGUIHandler) vp.getGUIHandler(IBlockGUIHandler.class);
            if (blockGUIHandler != null) {
                Element elmt3 = (Element) m_elements.get(i);
                blockGUIHandler.setBounds(elmt3.m_bounds);
            }
        }
        Rectangle r = elmt.m_bounds.union(elmt2.m_bounds);
        m_parent.redraw(r.x, r.y, r.width, r.height, true);
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.standard.layout.SplitterLayoutManagerBase#onEndResizing()
	 */
    protected void onEndResizing() {
        for (int i = 0; i < m_elements.size(); i++) {
            Element element = (Element) m_elements.get(i);
            Constraint constraint = element.m_constraint;
            if (constraint != null) {
                if (constraint.m_ratio < 0) {
                    constraint.m_pixels = element.m_bounds.height;
                } else {
                    constraint.m_ratio = ((float) element.m_bounds.height) / m_bounds.height;
                }
            }
        }
    }

    /**
	 * @see IBlockGUIHandler#renderHTML(HTMLengine he)
	 * 
	 * SJG: Need to use frames for scrolling
	 */
    public void renderHTML(HTMLengine he) {
        he.rowSetStart("RowSplitterLayoutManager");
        for (int i = 0; i < m_visualChildParts.size(); i++) {
            IVisualPart vp = (IVisualPart) m_visualChildParts.get(i);
            Element element = (Element) m_elements.get(i);
            IBlockGUIHandler blockGUIHandler = (IBlockGUIHandler) vp.getGUIHandler(IBlockGUIHandler.class);
            if (blockGUIHandler != null) {
                he.rowStart("RowSplitterLayoutManager");
                blockGUIHandler.renderHTML(he);
                he.rowEnd("RowSplitterLayoutManager");
            }
        }
        he.rowSetEnd("RowSplitterLayoutManager");
    }
}
