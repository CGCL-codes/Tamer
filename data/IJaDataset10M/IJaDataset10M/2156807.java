package org.jSyncManager.SJS.Adapters.SMTPServer.contracts;

import net.sourceforge.c4j.ContractBase;
import org.jSyncManager.SJS.Adapters.SMTPServer.AbstractSMTPServer.CommandNoop;

/** 
 * This class lists the contracts and invariants for the CommandNoop class
 *
 * @author Matt Campbell
 * @version 1.0
 *
 * @see CommandNoop
 */
public class CommandNoopContract extends ContractBase<CommandNoop> {

    /** 
    * A general constructor that sets the instance variable<br/>
    * This is of the form required by C4J
    *
    * @param t the reference to the base class
    */
    public CommandNoopContract(CommandNoop t) {
        super(t);
    }

    /** 
    * The invariant for this class. Each of these conditions must be
    * satisfied before and after each method call
    */
    public void classInvariant() {
        assert "NOOP".equalsIgnoreCase(((String) super.getTargetField("cmd")));
    }
}
