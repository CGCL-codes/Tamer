package com.ail.insurance.quotation.calculatecommission;

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.insurance.policy.Policy;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/19 20:20:58 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/calculatecommission/CalculateCommissionCommand.java,v $
 * @stereotype command
 */
public class CalculateCommissionCommand extends Command implements CalculateCommissionArg {

    private static final long serialVersionUID = -8491180625295230298L;

    private CalculateCommissionArg args = null;

    public CalculateCommissionCommand() {
        super();
        args = new CalculateCommissionArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (CalculateCommissionArg) arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Policy getPolicyArgRet() {
        return args.getPolicyArgRet();
    }

    /**
     * {@inheritDoc}
     * @param policyArgRet @{inheritDoc}
     */
    public void setPolicyArgRet(Policy policyArgRet) {
        args.setPolicyArgRet(policyArgRet);
    }
}
