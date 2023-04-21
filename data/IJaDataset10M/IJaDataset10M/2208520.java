package org.apache.fop.fo;

import org.apache.fop.apps.FOPException;
import org.apache.fop.layout.Area;
import org.apache.fop.layout.AreaClass;
import org.apache.fop.layout.LinkSet;
import org.apache.avalon.framework.logger.Logger;
import java.util.ArrayList;

/**
 * base class for nodes in the formatting object tree
 *
 * Modified by Mark Lillywhite mark-fop@inomial.com. Made
 * ArrayList a protected member. (/me things this should be
 * a private member with an API for adding children;
 * this woudl save a lot of memory because the ArrayList
 * would not have to be instantiated unless the node had
 * children).
 */
public abstract class FONode {

    protected FObj parent;

    protected String areaClass = AreaClass.UNASSIGNED;

    protected ArrayList children = new ArrayList();

    /**
     * value of marker before layout begins
     */
    public static final int START = -1000;

    /**
     * value of marker after break-after
     */
    public static final int BREAK_AFTER = -1001;

    /**
     * where the layout was up to.
     * for FObjs it is the child number
     * for FOText it is the character number
     */
    protected int marker = START;

    protected boolean isInTableCell = false;

    protected int forcedStartOffset = 0;

    protected int forcedWidth = 0;

    protected LinkSet linkSet;

    public int areasGenerated = 0;

    protected Logger log;

    protected FONode(FObj parent) {
        this.parent = parent;
        if (parent != null) {
            this.areaClass = parent.areaClass;
            log = parent.log;
        }
    }

    public void setLogger(Logger logger) {
        log = logger;
    }

    public void setIsInTableCell() {
        this.isInTableCell = true;
        for (int i = 0; i < this.children.size(); i++) {
            FONode child = (FONode) this.children.get(i);
            child.setIsInTableCell();
        }
    }

    public void forceStartOffset(int offset) {
        this.forcedStartOffset = offset;
        for (int i = 0; i < this.children.size(); i++) {
            FONode child = (FONode) this.children.get(i);
            child.forceStartOffset(offset);
        }
    }

    public void forceWidth(int width) {
        this.forcedWidth = width;
        for (int i = 0; i < this.children.size(); i++) {
            FONode child = (FONode) this.children.get(i);
            child.forceWidth(width);
        }
    }

    public void resetMarker() {
        this.marker = START;
        this.areasGenerated = 0;
        int numChildren = this.children.size();
        for (int i = 0; i < numChildren; i++) {
            ((FONode) children.get(i)).resetMarker();
        }
    }

    public void removeAreas() {
    }

    protected void addChild(FONode child) {
        children.add(child);
    }

    public FObj getParent() {
        return this.parent;
    }

    public void setLinkSet(LinkSet linkSet) {
        this.linkSet = linkSet;
        for (int i = 0; i < this.children.size(); i++) {
            FONode child = (FONode) this.children.get(i);
            child.setLinkSet(linkSet);
        }
    }

    public LinkSet getLinkSet() {
        return this.linkSet;
    }

    public abstract int layout(Area area) throws FOPException;

    /**
     * lets outside sources access the property list
     * first used by PageNumberCitation to find the "id" property
     * returns null by default, overide this function when there is a property list
     * @param name - the name of the desired property to obtain
     * @return the property
     */
    public Property getProperty(String name) {
        return (null);
    }

    /**
     * At the start of a new span area layout may be partway through a
     * nested FO, and balancing requires rollback to this known point.
     * The snapshot records exactly where layout is at.
     * @param snapshot a ArrayList of markers (Integer)
     * @return the updated ArrayList of markers (Integers)
     */
    public ArrayList getMarkerSnapshot(ArrayList snapshot) {
        snapshot.add(new Integer(this.marker));
        if (this.marker < 0) return snapshot; else if (children.isEmpty()) return snapshot; else return ((FONode) children.get(this.marker)).getMarkerSnapshot(snapshot);
    }

    /**
     * When balancing occurs, the flow layout() method restarts at the
     * point specified by the current marker snapshot, which is retrieved
     * and restored using this method.
     * @param snapshot the ArrayList of saved markers (Integers)
     */
    public void rollback(ArrayList snapshot) {
        this.marker = ((Integer) snapshot.get(0)).intValue();
        snapshot.remove(0);
        if (this.marker == START) {
            resetMarker();
            return;
        } else if ((this.marker == -1) || children.isEmpty()) return;
        int numChildren = this.children.size();
        if (this.marker <= START) {
            return;
        }
        for (int i = this.marker + 1; i < numChildren; i++) {
            FONode fo = (FONode) children.get(i);
            fo.resetMarker();
        }
        ((FONode) children.get(this.marker)).rollback(snapshot);
    }

    public boolean mayPrecedeMarker() {
        return false;
    }
}
