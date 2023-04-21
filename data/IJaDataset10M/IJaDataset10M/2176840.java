package org.jSyncManager.SJS.Adapters.SMTPServer.contracts;

import net.sourceforge.c4j.ContractBase;
import org.jSyncManager.SJS.Adapters.SMTPServer.AbstractSMTPServer.CommandRcpt;

/** 
 * This class lists the contracts and invariants for the CommandRcpt class
 *
 * @author Matt Campbell
 * @version 1.0
 *
 * @see CommandRcpt
 */
public class CommandRcptContract extends ContractBase<CommandRcpt> {

    /** 
    * A general constructor that sets the instance variable<br/>
    * This is of the form required by C4J
    *
    * @param t the reference to the base class
    */
    public CommandRcptContract(CommandRcpt t) {
        super(t);
    }

    /** 
    * The invariant for this class. Each of these conditions must be
    * satisfied before and after each method call
    */
    public void classInvariant() {
        assert "RCPT".equalsIgnoreCase(((String) super.getTargetField("cmd")));
    }
}
