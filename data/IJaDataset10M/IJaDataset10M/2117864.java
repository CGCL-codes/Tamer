package org.primordion.user.app.Bestiary;

import org.primordion.xholon.base.AbstractGrid;
import org.primordion.xholon.base.IGrid;
import org.primordion.xholon.base.IQueue;
import org.primordion.xholon.base.IXholon;

/**
 * Graph a changing value.
 * An instance of this class should initially be a child of the node whose value is to be graphed.
 * Paste the following as a last child:
<pre>&lt;GraphingBeastbehavior/></pre>
or:
<pre>&lt;GraphingBeastbehavior howChoose="1" xInc="0.1" xMax="100.0" yMult="1000.0"/></pre>
 */
public class GraphingBeastbehavior extends BeastBehavior {

    /** The node whose value(s) will be graphed. */
    protected IXholon graphedNode = null;

    /** Cached value of the graphedNode value. */
    protected double graphedNodeVal = 0.0;

    /**
	 * How to choose a volunteer beast:
	 * (1) use Licorice
	 * (2) use first beast in the Beasts Q
	 */
    protected int howChoose = 1;

    /**
	 * Amount of movement in the X direction each time step.
	 */
    protected double xInc = 0.1;

    /**
	 * Maximum movement in the X direction.
	 */
    protected double xMax = 100.0;

    /**
	 * The grid is typically 100*100 in size.
	 * The graph needs to be scaled to fit within this space.
	 * Multiply the graphedNode value by yMult to determine which gridCell to move to.
	 */
    protected double yMult = 1000.0;

    /**
	 * Current count of movement in the X direction.
	 */
    protected double xCounter = 0.0;

    /**
	 * (xCounter % 1.0) has a roundoff error
	 */
    protected double remainderErr = 0.01;

    public void postConfigure() {
        if (graphedNode != null) {
            if (this.hasNextSibling()) {
                this.getNextSibling().postConfigure();
            }
            return;
        }
        super.postConfigure();
        graphedNode = this.getParentNode();
        IXholon aBeast = findAVolunteer();
        IXholon habitatCell = this.getXPath().evaluate("ancestor::HabitatCell", this);
        aBeast.removeChild();
        aBeast.appendChild(habitatCell);
        this.removeChild();
        this.insertFirstChild(aBeast);
        this.setBeast(aBeast);
    }

    public void act() {
        xCounter += xInc;
        if (xCounter >= xMax) {
            this.removeChild();
            return;
        }
        double remainder = xCounter % 1.0;
        if (remainder < 0.99 && remainder > remainderErr) {
            return;
        }
        IXholon myBeast = getBeast();
        AbstractGrid destination = (AbstractGrid) myBeast.getParentNode();
        destination = (AbstractGrid) destination.port[IGrid.P_EAST];
        double oldGraphedNodeVal = graphedNodeVal;
        double newGraphedNodeVal = graphedNode.getVal();
        int yInc = (int) ((newGraphedNodeVal - oldGraphedNodeVal) * yMult);
        if (yInc > 0) {
            graphedNodeVal = newGraphedNodeVal;
        }
        for (int i = 0; i < yInc; i++) {
            if ((destination == null)) {
                break;
            }
            destination = (AbstractGrid) destination.port[IGrid.P_NORTH];
        }
        if (destination == null) {
            return;
        }
        destination.appendChild("WallSection", null);
        myBeast.removeChild();
        myBeast.appendChild(destination);
    }

    public int setAttributeVal(String attrName, String attrVal) {
        if ("howChoose".equals(attrName)) {
            howChoose = Integer.parseInt(attrVal);
        } else if ("xInc".equals(attrName)) {
            xInc = Double.parseDouble(attrVal);
        } else if ("xMax".equals(attrName)) {
            xMax = Double.parseDouble(attrVal);
        } else if ("yMult".equals(attrName)) {
            yMult = Double.parseDouble(attrVal);
        } else {
            return super.setAttributeVal(attrName, attrVal);
        }
        return 0;
    }

    /**
	 * Find a beast who's willing to help with graphing.
	 * @return
	 */
    protected IXholon findAVolunteer() {
        IXholon aBeast = null;
        switch(howChoose) {
            case 1:
                aBeast = this.getXPath().evaluate("/descendant-or-self::*[@name='cat_2']", this);
                if (aBeast == null) {
                    howChoose = 2;
                    return findAVolunteer();
                }
                break;
            case 2:
                IQueue q = (IQueue) this.getXPath().evaluate("ancestor::Bestiary/Beasts", this);
                aBeast = (IXholon) q.dequeue();
                q.enqueue(aBeast);
                break;
            default:
                break;
        }
        return aBeast;
    }
}
