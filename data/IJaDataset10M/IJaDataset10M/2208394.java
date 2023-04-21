package aurora.hwc;

import aurora.*;

/**
 * Highway Link.
 * <br>Allowed begin node (predecessor): NodeHighway, NodeHWCNetwork.
 * <br>Allowed end node (successor): NodeHighway, NodeHWCNetwork.
 * 
 * @see NodeHighway, NodeHWCNetwork
 * 
 * @author Alex Kurzhanskiy
 * @version $Id: LinkHw.java 42 2010-02-16 01:08:27Z akurzhan $
 */
public final class LinkHw extends AbstractLinkHWC {

    private static final long serialVersionUID = -2360723731084436658L;

    public LinkHw() {
    }

    public LinkHw(int id) {
        this.id = id;
    }

    public LinkHw(int id, DynamicsHWC dyn) {
        this.id = id;
        myDynamics = dyn;
    }

    /**
	 * Returns type.
	 */
    public final int getType() {
        return TypesHWC.LINK_HIGHWAY;
    }

    /**
	 * Returns letter code of the Link type.
	 */
    public String getTypeLetterCode() {
        return "HW";
    }

    /**
	 * Returns type description.
	 */
    public final String getTypeString() {
        return "Highway";
    }

    /**
	 * Validates Highway Link configuration.<br>
	 * Checks if begin and end nodes are of correct type.
	 * @return <code>true</code> if configuration is correct, <code>false</code> - otherwise.
	 * @throws ExceptionConfiguration
	 */
    public boolean validate() throws ExceptionConfiguration {
        boolean res = super.validate();
        String cnm;
        int type;
        if (predecessors.size() > 0) {
            type = predecessors.firstElement().getType();
            cnm = predecessors.firstElement().getClass().getName();
            if ((type != TypesHWC.NODE_HIGHWAY) && (type != TypesHWC.NODE_SIGNAL) && (type != TypesHWC.NODE_STOP) && (type != TypesHWC.NETWORK_HWC)) {
                myNetwork.addConfigurationError(new ErrorConfiguration(this, "Begin Node of wrong type (" + cnm + ")."));
                res = false;
            }
        }
        if (successors.size() > 0) {
            type = successors.firstElement().getType();
            cnm = successors.firstElement().getClass().getName();
            if ((type != TypesHWC.NODE_HIGHWAY) && (type != TypesHWC.NODE_SIGNAL) && (type != TypesHWC.NODE_STOP) && (type != TypesHWC.NETWORK_HWC)) {
                myNetwork.addConfigurationError(new ErrorConfiguration(this, "End Node of wrong type (" + cnm + ")."));
                res = false;
            }
        }
        return res;
    }
}
