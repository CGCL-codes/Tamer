package org.primordion.ealontro.app;

import org.primordion.xholon.base.IActivity;
import org.primordion.xholon.base.IXholon;
import org.primordion.xholon.base.XholonWithPorts;

/**
 * Cart Centering System with Genetic Programming.
 * <p>source: Koza, J. (1992). Genetic Programming. p.122-147</p>
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.3 (Created on May 11, 2006)
 */
public class XhCartCentering extends XholonWithPorts implements IActivity, CeCartCentering {

    public static final int P_XPOSITION = 0;

    public static final int P_VELOCITY = 1;

    public static final int P_ACCELERATION = 2;

    public static final int P_CART = 3;

    public static final int P_BEHAVIOR = 4;

    private static final double NEGATIVE_ONE = -1.0;

    public double val = 0.0;

    public String roleName = null;

    /**
	 * Constructor.
	 */
    public XhCartCentering() {
    }

    public void initialize() {
        super.initialize();
        val = 0.0;
        roleName = null;
    }

    public double getVal() {
        return this.val;
    }

    public void setVal(double val) {
        this.val = val;
    }

    public void incVal(double incAmount) {
        this.val += incAmount;
    }

    public void decVal(double decAmount) {
        this.val += decAmount;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void act() {
        super.act();
        switch(xhc.getId()) {
            case ForceCE:
                val = performDoubleActivity(port[P_BEHAVIOR].getFirstChild());
                port[P_ACCELERATION].setVal(val / ((XhCartCentering) port[P_CART]).getVal());
                break;
            case CartCE:
                port[P_VELOCITY].incVal(port[P_ACCELERATION].getVal());
                port[P_XPOSITION].incVal(port[P_VELOCITY].getVal());
                break;
            default:
                break;
        }
    }

    public double performDoubleActivity(IXholon activity) {
        switch(activity.getXhcId()) {
            case PfAddCE:
                return performDoubleActivity(activity.getFirstChild()) + performDoubleActivity(activity.getFirstChild().getNextSibling());
            case PfSubtractCE:
                return performDoubleActivity(activity.getFirstChild()) - performDoubleActivity(activity.getFirstChild().getNextSibling());
            case PfMultiplyCE:
                return performDoubleActivity(activity.getFirstChild()) * performDoubleActivity(activity.getFirstChild().getNextSibling());
            case PfDivideCE:
                double numerator = performDoubleActivity(activity.getFirstChild());
                double denominator = performDoubleActivity(activity.getFirstChild().getNextSibling());
                if (denominator == 0.0) {
                    return 1.0;
                } else {
                    return numerator / denominator;
                }
            case PfABSCE:
                return Math.abs(performDoubleActivity(activity.getFirstChild()));
            case PfGTCE:
                return performDoubleActivity(activity.getFirstChild()) > performDoubleActivity(activity.getFirstChild().getNextSibling()) ? +1.0 : -1.0;
            case PfXPositionCE:
                return port[P_XPOSITION].getVal();
            case PfVelocityCE:
                return port[P_VELOCITY].getVal();
            case PfNegOneCE:
                return NEGATIVE_ONE;
            case PfWrapperCE:
                return performDoubleActivity(activity.getFirstChild()) > 0.0 ? +1.0 : -1.0;
            default:
                System.out.println("XhCartCentering: behavior for activity " + activity.getXhcName() + " not yet implemented");
                return 0.0;
        }
    }

    public String toString() {
        String outStr = getName();
        IXholon node;
        switch(xhc.getId()) {
            case GeneticProgramCE:
                outStr += ", sizeOfPfTree:" + (treeSize() - 1) + "]";
                break;
            case CartCenteringSystemCE:
                node = getXPath().evaluate("descendant::XPosition", this);
                outStr += " [xPosition:" + node.getVal();
                node = node.getNextSibling();
                outStr += " velocity:" + node.getVal();
                node = node.getNextSibling();
                outStr += " acceleration:" + node.getVal() + "]";
                break;
            case ForceCE:
                break;
            case CartCE:
                outStr += " [mass:" + val + "]";
                break;
            case XPositionCE:
            case VelocityCE:
            case AccelerationCE:
                outStr += " [val:" + val + "]";
                break;
            default:
                break;
        }
        if ((port != null) && (port.length > 0)) {
            outStr += " [";
            for (int i = 0; i < port.length; i++) {
                if (port[i] != null) {
                    outStr += " port:" + port[i].getName();
                }
            }
            outStr += "]";
        }
        return outStr;
    }
}
