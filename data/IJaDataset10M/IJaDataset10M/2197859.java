package systemStates;

import systemStates.exceptions.NotAllowedInHostException;
import systemStates.types.OCLExpression;
import systemStates.types.Variable;

/**
 * @author hwaters
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UnlinkProcessNode extends ProcessNode {

    protected Object assocName;

    public UnlinkProcessNode(SystemState systemState) {
        super(systemState, systemState.systemStateGraGra.ntUnlinkProcess);
    }

    /**
	 * @param string
	 */
    public void setAssociationName(String string) {
        assocName = string;
        setAGGAttribute("assocName", string);
    }

    public void setAssociationName(Variable var) throws NotAllowedInHostException {
        throw new NotAllowedInHostException();
    }

    public void setStatus(OCLExpression expr) throws NotAllowedInHostException {
        throw new NotAllowedInHostException();
    }
}
