package org.primordion.xholon.samples;

import org.primordion.xholon.base.AbstractGrid;
import org.primordion.xholon.base.IGrid;
import org.primordion.xholon.base.IXholon;
import org.primordion.xholon.util.MiscRandom;

/**
 * Combo Grid.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.5 (Created on February 14, 2007)
 */
public class XhCombo extends AbstractGrid implements CeCombo {

    private int numNeighborsAlive = 0;

    public boolean alive = false;

    public String roleName = null;

    protected boolean hasMoved;

    /**
	 * Constructor.
	 */
    public XhCombo() {
    }

    public void initialize() {
        super.initialize();
    }

    public void setVal(boolean val) {
        alive = val;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    /**
	 * Is this grid cell alive?
	 * @return true or false
	 */
    public boolean isAlive() {
        return alive;
    }

    public void preAct() {
        switch(xhc.getId()) {
            case GolGridCellCE:
                numNeighborsAlive = 0;
                break;
            default:
                break;
        }
        super.preAct();
    }

    public void act() {
        switch(xhc.getId()) {
            case CreatureCE:
                if (hasMoved == false) {
                    int r = MiscRandom.getRandomInt(0, 6);
                    moveAdjacent(r);
                }
                break;
            case HexGridCellCE:
                break;
            case GolGridCellCE:
                if (port[IGrid.P_NORTH] != null) {
                    if (((XhCombo) port[IGrid.P_NORTH]).alive) {
                        numNeighborsAlive++;
                    }
                }
                if (port[IGrid.P_NORTHEAST] != null) {
                    if (((XhCombo) port[IGrid.P_NORTHEAST]).alive) {
                        numNeighborsAlive++;
                    }
                }
                if (port[IGrid.P_EAST] != null) {
                    if (((XhCombo) port[IGrid.P_EAST]).alive) {
                        numNeighborsAlive++;
                    }
                }
                if (port[IGrid.P_SOUTHEAST] != null) {
                    if (((XhCombo) port[IGrid.P_SOUTHEAST]).alive) {
                        numNeighborsAlive++;
                    }
                }
                if (port[IGrid.P_SOUTH] != null) {
                    if (((XhCombo) port[IGrid.P_SOUTH]).alive) {
                        numNeighborsAlive++;
                    }
                }
                if (port[IGrid.P_SOUTHWEST] != null) {
                    if (((XhCombo) port[IGrid.P_SOUTHWEST]).alive) {
                        numNeighborsAlive++;
                    }
                }
                if (port[IGrid.P_WEST] != null) {
                    if (((XhCombo) port[IGrid.P_WEST]).alive) {
                        numNeighborsAlive++;
                    }
                }
                if (port[IGrid.P_NORTHWEST] != null) {
                    if (((XhCombo) port[IGrid.P_NORTHWEST]).alive) {
                        numNeighborsAlive++;
                    }
                }
                break;
            default:
                break;
        }
        super.act();
    }

    public void postAct() {
        switch(xhc.getId()) {
            case GolGridCellCE:
                if (numNeighborsAlive == 2) {
                } else if (numNeighborsAlive == 3) {
                    alive = true;
                } else {
                    alive = false;
                }
                break;
            default:
                hasMoved = false;
                break;
        }
        super.postAct();
    }

    /**
	 * Used by Creature to move to an adjacent grid cell.
	 * @param direction The direction it would like to move.
	 */
    protected void moveAdjacent(int direction) {
        IXholon newParentNode = getParentNode();
        newParentNode = ((AbstractGrid) newParentNode).port[direction];
        if ((newParentNode != null) && (((XhCombo) newParentNode).isAlive())) {
            removeChild();
            appendChild(newParentNode);
            hasMoved = true;
        }
    }

    public String toString() {
        String outStr = getName();
        if ((port != null) && (port.length > 0)) {
            outStr += " [";
            for (int i = 0; i < port.length; i++) {
                if (port[i] != null) {
                    outStr += " port:" + port[i].getName();
                }
            }
            outStr += "]";
        }
        if (getVal() != 0.0) {
            outStr += " val:" + getVal();
        }
        return outStr;
    }
}
