package Beans.Requests.MachineManagement.Components;

import Utilities.Interfaces.SelfValidating;
import java.io.Serializable;

/**
 * This is the superclass for all machine management components.
 *
 * @author Angel Sanadinov
 */
public abstract class MachineManagementComponent implements Serializable, SelfValidating {

    /**
     * Checks the validity of the component. <br><br>
     *
     * A component is considered valid only if all its data is valid.
     *
     * @return <code>true</code> if the component is valid or <code>false</code> otherwise
     */
    @Override
    public abstract boolean isValid();
}
