package org.unicore.outcome;

import org.unicore.ajo.ChangePermissions;

/**
 * The status and logs of executing a {@link org.unicore.ajo.ChangePermissions}.
 *
 * @see org.unicore.ajo.ChangePermissions
 *
 * @author S. van den Berghe (fecit)
 *
 * @version $Id: ChangePermissions_Outcome.java,v 1.2 2004/05/26 16:31:44 svenvdb Exp $
 * 
 **/
public class ChangePermissions_Outcome extends AbstractTask_Outcome {

    static final long serialVersionUID = -8072528257248456478L;

    public ChangePermissions_Outcome() {
        this(null, null);
    }

    /**
     * Create a new ChangePermissions_Outcome.
     * <p>
     * This will hold the standard information from {@link Outcome}.
     *
     * @param source The ChangePermissions for which this Outcome will hold the results.
     * @param initial The status of the ChangePermissions at the time that the Outcome is created.
     *
     **/
    public ChangePermissions_Outcome(ChangePermissions source, AbstractActionStatus initial) {
        super(source, initial);
    }
}
