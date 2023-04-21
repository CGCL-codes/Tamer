package org.dita2indesign.indesign.inx.model;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.dita2indesign.indesign.inx.visitors.InDesignDocumentVisitor;
import org.dita2indesign.util.DataUtil;
import org.w3c.dom.Element;

/**
 * Represents a single page.
 */
public class Page extends InDesignRectangleContainingObject {

    Logger logger = Logger.getLogger(this.getClass());

    PageSideOption pageSide = PageSideOption.SINGLE_SIDED;

    TransformationMatix transformMatrix = null;

    Box boundingBox;

    private String name;

    public void loadObject(Element dataSource) throws Exception {
        super.loadObject(dataSource);
        Iterator<Element> elemIter = DataUtil.getElementChildrenIterator(dataSource);
        this.name = getStringProperty(InDesignDocument.PROP_PNAM);
        PageSideOption tempPgSide = (PageSideOption) getEnumProperty(InDesignDocument.PROP_PGSD);
        if (tempPgSide != null) {
            pageSide = tempPgSide;
        }
        setBounds();
        while (elemIter.hasNext()) {
            Element child = elemIter.next();
            if (child.getNodeName().equals("imgp")) {
            } else if (child.getNodeName().equals("Jgda")) {
            } else {
                logger.debug(" - Unrecognized component type [" + child.getNodeName() + "]");
            }
        }
    }

    /**
	 *  Set the page bounds as a Box. This is the position of box in in the
	 *  pasteboard coordinate space.
	 * @throws Exception 
	 */
    protected void setBounds() throws Exception {
        List<InxValue> bounds = getValueListProperty(InDesignDocument.PROP_PBND);
        boundingBox = new Box();
        if (bounds == null) return;
        double y1 = ((InxUnit) bounds.get(0)).getValue();
        double x1 = ((InxUnit) bounds.get(1)).getValue();
        double y2 = ((InxUnit) bounds.get(2)).getValue();
        double x2 = ((InxUnit) bounds.get(3)).getValue();
        boundingBox.setCorners(x1, y1, x2, y2);
    }

    /**
	 * @return
	 */
    public double getWidth() {
        return this.getBoundingBox().getRectangle2D().getWidth();
    }

    /**
	 * @return
	 */
    public double getHeight() {
        return this.getBoundingBox().getRectangle2D().getHeight();
    }

    /**
	 * @return
	 */
    public PageSideOption getPageSide() {
        return pageSide;
    }

    /**
	 * @return The bounding box for the page.
	 */
    public Box getBoundingBox() {
        return this.boundingBox;
    }

    public String getName() {
        return this.name;
    }

    /**
	 * @param visitor
	 * @throws Exception 
	 */
    public void accept(InDesignDocumentVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    /**
	 * Override of abstract superclass. Pages are not the parents of the frames
	 * they contain, spreads are.
	 */
    public void addRectangle(Rectangle rect) {
        logger.debug("addRectang(): rect=" + rect);
        this.rectangles.put(rect.getId(), rect);
        if (rect instanceof TextFrame) this.frames.put(rect.getId(), (TextFrame) rect);
    }
}
