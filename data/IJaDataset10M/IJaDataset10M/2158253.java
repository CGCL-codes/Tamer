package com.bluebrim.layout.impl.server;

import java.awt.*;
import java.awt.geom.*;
import java.lang.reflect.*;
import java.util.*;
import org.w3c.dom.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.server.geom.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.page.shared.*;
import com.bluebrim.system.shared.*;
import com.bluebrim.text.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * A page item representing a page.
 * This page item can be connected to a page layer and should be so when owned by one.
 * 
 * @author: Dennis Malmstr�m
 */
public class CoPageLayoutArea extends CoCompositePageItem implements CoPageLayoutAreaIF {

    private CoPage m_page;

    private static CoPageItemVisitor m_addToInsertionRequestVisitor;

    protected CoCustomGrid m_customGrid = new CoCustomGrid();

    private RemoteCustomGrid m_mutableCustomGridProxy = new RemoteCustomGrid();

    private static CoPageItemVisitor m_removeFromInsertionRequestVisitor;

    private CoPageSizeIF m_size;

    private boolean m_isSpread = false;

    public static final String XML_PAGE_SIZE = "page-size";

    public static final String XML_TAG = "page-layer-layout-area";

    public static final String XML_X_GRID_LINES = "x-grid-lines";

    public static final String XML_Y_GRID_LINES = "y-grid-lines";

    public static final String XML_IS_SPREAD = "is-spread";

    private class RemoteCustomGrid implements CoRemoteCustomGridIF {

        public void addPropertyChangeListener(CoPropertyChangeListener l) {
            m_customGrid.addPropertyChangeListener(l);
        }

        public void removePropertyChangeListener(CoPropertyChangeListener l) {
            m_customGrid.removePropertyChangeListener(l);
        }

        public CoImmutableSnapGridIF deepClone() {
            return m_customGrid.deepClone();
        }

        public void xmlVisit(CoXmlVisitorIF visitor) {
        }

        ;

        public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) {
        }

        public void xmlImportFinished(Node node, CoXmlContext context) {
        }

        public int getXPositionCount() {
            return m_customGrid.getXPositionCount();
        }

        public int getYPositionCount() {
            return m_customGrid.getYPositionCount();
        }

        public double getXPosition(int i) {
            return m_customGrid.getXPosition(i);
        }

        public double getYPosition(int i) {
            return m_customGrid.getYPosition(i);
        }

        public double findXPosition(double pos, double range) {
            return m_customGrid.findXPosition(pos, range);
        }

        public double findYPosition(double pos, double range) {
            return m_customGrid.findYPosition(pos, range);
        }

        public Collection getGridLines() {
            return m_customGrid.getGridLines();
        }

        public Point2D snap(double d0, double d1, double d2, double d3, double d4, Point2D p) {
            return m_customGrid.snap(d0, d1, d2, d3, d4, p);
        }

        public Point2D snap(double d0, double d1, double d2, int i0, int i1, boolean b, Point2D p) {
            return m_customGrid.snap(d0, d1, d2, i0, i1, b, p);
        }

        public Shape getShape(int i) {
            return m_customGrid.getShape(i);
        }

        public void addXPosition(double x) {
            m_customGrid.addXPosition(x);
            doAfterCustomGridChanged();
        }

        public void addYPosition(double y) {
            m_customGrid.addYPosition(y);
            doAfterCustomGridChanged();
        }

        public void removeXPosition(double x) {
            m_customGrid.removeXPosition(x);
            doAfterCustomGridChanged();
        }

        public void removeYPosition(double y) {
            m_customGrid.removeYPosition(y);
            doAfterCustomGridChanged();
        }

        public void removeXPosition(int i) {
            m_customGrid.removeXPosition(i);
            doAfterCustomGridChanged();
        }

        public void removeYPosition(int i) {
            m_customGrid.removeYPosition(i);
            doAfterCustomGridChanged();
        }

        public void removeAllXPositions() {
            m_customGrid.removeAllXPositions();
            doAfterCustomGridChanged();
        }

        public void removeAllYPositions() {
            m_customGrid.removeAllYPositions();
            doAfterCustomGridChanged();
        }
    }

    public CoPageLayoutArea(CoPage pl) {
        super();
        m_page = pl;
    }

    public CoPageItemIF.State createState() {
        return new CoPageLayoutAreaIF.State();
    }

    protected void deepCopy(CoPageItem copy) {
        super.deepCopy(copy);
        CoPageLayoutArea c = (CoPageLayoutArea) copy;
        c.m_customGrid = (CoCustomGrid) m_customGrid.deepClone();
        c.m_mutableCustomGridProxy = c.new RemoteCustomGrid();
    }

    protected final void doAfterCustomGridChanged() {
        postCustomGridChanged();
        markDirty("CustomGridChanged");
    }

    protected final void doAfterPageSizeChanged() {
        postPageSizeChanged();
        markDirty("PageSizeChanged");
    }

    protected CoTextStyleApplier doGetTextStyleApplier() {
        CoTextStyleApplier textStyleApplier;
        CoPageItemPreferencesIF pip = getPreferences();
        textStyleApplier = (pip == null) ? null : pip.getTextStyleApplier();
        return textStyleApplier;
    }

    public Object getAttributeValue(Field d) throws IllegalAccessException {
        try {
            return d.get(this);
        } catch (IllegalAccessException ex) {
            return super.getAttributeValue(d);
        }
    }

    public CoImmutableCustomGridIF getCustomGrid() {
        return m_customGrid;
    }

    public CoDesktopLayoutAreaIF getDesktop() {
        return (m_page == null) ? null : (CoDesktopLayoutAreaIF) m_page.getDesktop();
    }

    public CoCustomGridIF getMutableCustomGrid() {
        return m_mutableCustomGridProxy;
    }

    public String getName() {
        StringBuffer b = new StringBuffer();
        b.append(getType());
        if (m_page != null) {
            b.append(" ");
            b.append(m_page.getName());
        }
        return b.toString();
    }

    public String getType() {
        return CoPageItemStringResources.getName(PAGE_LAYER_LAYOUT_AREA);
    }

    public CoPage getPage() {
        return m_page;
    }

    public CoPageSizeIF getPageSize() {
        return m_size;
    }

    public CoPageItemPreferencesIF getPreferences() {
        return (m_page == null) ? super.getPreferences() : (CoPageItemPreferencesIF) m_page.getLayoutParameters();
    }

    public boolean isLeftSide() {
        return (m_page == null) || m_page.isLeftSide();
    }

    protected void postCustomGridChanged() {
    }

    protected void postPageSizeChanged() {
        setSizeFromPageSize();
    }

    protected void postShapeChanged() {
        super.postShapeChanged();
        if (m_size != null) {
            if ((getWidth() != m_size.getWidth() * (m_isSpread ? 2 : 1)) || (getHeight() != m_size.getHeight())) {
                m_size = null;
            }
        }
        if (m_customGrid != null) m_customGrid.setSize(getWidth(), getHeight());
    }

    public void postSubTreeStructureChange(boolean added, CoShapePageItemIF pageItem, CoCompositePageItemIF otherParent) {
        super.postSubTreeStructureChange(added, pageItem, otherParent);
        if (added) {
            boolean doTell = (otherParent == null);
            if (!doTell) {
                CoCompositePageItemIF parent = otherParent;
                while (parent != this) {
                    if (parent == null) {
                        doTell = true;
                        break;
                    }
                    parent = parent.getParent();
                }
            }
        } else {
            boolean doTell = (otherParent == null);
            if (!doTell) {
                CoCompositePageItemIF parent = otherParent;
                while (parent != this) {
                    if (parent == null) {
                        doTell = true;
                        break;
                    }
                    parent = parent.getParent();
                }
            }
        }
    }

    protected void bindTextVariableValues(Map values) {
        super.bindTextVariableValues(values);
        m_page.bindTextVariableValues(values);
    }

    public void setPageSize(CoPageSizeIF s) {
        if (m_size == s) return;
        m_size = s;
        doAfterPageSizeChanged();
    }

    public void setPage(CoPage page) {
        if (m_page != page) {
            boolean old = isLeftSide();
            m_page = page;
            if (old != isLeftSide()) {
                requestLocalLayout();
                updateColumnGrid();
            }
            performLocalLayout();
        }
    }

    public void visit(CoPageItemVisitor visitor, Object anything, boolean goDown) {
        visitor.doToPageLayoutArea(this, anything, goDown);
    }

    public void xmlVisit(CoXmlVisitorIF visitor) {
        super.xmlVisit(visitor);
        if (m_size != null) visitor.exportAsGOIorObject(XML_PAGE_SIZE, m_size);
        StringBuffer s = new StringBuffer();
        int I = m_customGrid.getXPositionCount();
        for (int i = 0; i < I; i++) {
            if (i > 0) s.append(' ');
            s.append(m_customGrid.getXPosition(i));
        }
        if (I > 0) visitor.exportAttribute(XML_X_GRID_LINES, s.toString());
        s.setLength(0);
        I = m_customGrid.getYPositionCount();
        for (int i = 0; i < I; i++) {
            if (i > 0) s.append(' ');
            s.append(m_customGrid.getYPosition(i));
        }
        if (I > 0) visitor.exportAttribute(XML_Y_GRID_LINES, s.toString());
        visitor.exportAttribute(XML_IS_SPREAD, (m_isSpread ? Boolean.TRUE : Boolean.FALSE).toString());
    }

    protected void collectState(CoPageItemIF.State s, CoPageItemIF.ViewState viewState) {
        super.collectState(s, viewState);
        CoPageLayoutAreaIF.State S = (CoPageLayoutAreaIF.State) s;
        S.m_pageSize = m_size;
        S.m_pageSizeName = (m_size == null) ? null : m_size.getName();
        S.m_isSpread = m_isSpread;
        S.m_isLeftSideOfSpread = false;
        S.m_isRightSideOfSpread = false;
        S.m_customGrid = m_customGrid;
    }

    /**
	 * This method may only be called for CoPrintalbePage.addPageLayer
	 * Creation date: (2001-08-14 16:12:43)
	 * @param printablePage CoPrintablePage
	 */
    public final CoPageLayoutArea deepCloneFor(CoPage page) {
        CoPageLayoutArea clone = (CoPageLayoutArea) deepClone();
        clone.setPage(page);
        return clone;
    }

    public boolean isSpread() {
        return m_isSpread;
    }

    protected CoShapePageItemView newView(CoCompositePageItemView parent, int detailMode) {
        return new CoPageLayoutAreaView(this, parent, (CoPageLayoutAreaIF.State) getState(null), detailMode);
    }

    private void setSizeFromPageSize() {
        if (m_size != null) {
            double w = m_size.getWidth();
            double h = m_size.getHeight();
            if (m_isSpread) w *= 2;
            getMutableCoShape().setSize(w, h);
        }
    }

    /**
	 * Let the column grid have the same spread state as we have.
	 */
    public void setSpread(boolean s) {
        if (m_isSpread == s) return;
        m_isSpread = s;
        CoColumnGridIF columnGrid = getMutableColumnGrid();
        if (columnGrid != null) columnGrid.setSpread(s);
        doAfterPageSizeChanged();
    }

    /**
	 *	The object in m_page is not a subModel so why is this done? /G�ran S 
	 */
    public void xmlAddSubModel(String name, Object subModel, CoXmlContext context) {
        boolean didHandle = false;
        if (name == null) {
            if (subModel instanceof CoPage) {
                didHandle = true;
            }
        } else if (name.equals(XML_PAGE_SIZE)) {
            setPageSize((CoPageSizeIF) subModel);
            didHandle = true;
        }
        if (!didHandle) super.xmlAddSubModel(name, subModel, context);
    }

    /**
	 * Supermodel don't have to be a page. It can for example be a CoPageItemPrototype
	 */
    public static CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, CoXmlContext context) {
        CoPageLayoutArea area;
        if (superModel instanceof CoPage) area = new CoPageLayoutArea((CoPage) superModel); else area = new CoPageLayoutArea(null);
        area.xmlInit(node.getAttributes(), context);
        return area;
    }

    public void xmlInit(NamedNodeMap map, CoXmlContext context) {
        super.xmlInit(map, context);
        if (context.useGOI()) {
            String goiStr = CoModelBuilder.getAttrVal(map, XML_PAGE_SIZE, null);
            if (goiStr != null) {
                CoPageSizeIF s = ((CoPageSizeRegistry) context.getValue(CoPageSizeRegistry.class)).lookupPageSize(new CoGOI(goiStr));
                if (s == null) {
                    System.err.println("Warning: could not find page size: \"" + goiStr + " \"");
                } else {
                    setPageSize(s);
                }
            }
        }
        m_customGrid.removeAllXPositions();
        m_customGrid.removeAllYPositions();
        String tmp = CoModelBuilder.getAttrVal(map, XML_X_GRID_LINES, null);
        if (tmp != null) {
            StringTokenizer t = new StringTokenizer(tmp, " ");
            int i = 0;
            while (t.hasMoreTokens()) {
                double x = CoXmlUtilities.parseDouble(t.nextToken(), Double.NaN);
                if (Double.isNaN(x)) {
                    throw new IllegalArgumentException("invalid x custom grid line");
                }
                m_customGrid.addXPosition(x);
            }
        }
        tmp = CoModelBuilder.getAttrVal(map, XML_Y_GRID_LINES, null);
        if (tmp != null) {
            StringTokenizer t = new StringTokenizer(tmp, " ");
            int i = 0;
            while (t.hasMoreTokens()) {
                double y = CoXmlUtilities.parseDouble(t.nextToken(), Double.NaN);
                if (Double.isNaN(y)) {
                    throw new IllegalArgumentException("invalid y custom grid line");
                }
                m_customGrid.addYPosition(y);
            }
        }
        m_isSpread = CoModelBuilder.getBoolAttrVal(map, XML_IS_SPREAD, m_isSpread);
    }

    public String getFactoryKey() {
        return PAGE_LAYER_LAYOUT_AREA;
    }
}
