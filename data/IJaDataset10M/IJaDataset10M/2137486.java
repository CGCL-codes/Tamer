package com.ail.insurance.quotation.assessrisk;

import com.ail.core.command.CommandArgImp;
import com.ail.insurance.policy.Policy;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/19 20:20:58 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/assessrisk/AssessRiskArgImp.java,v $
 * @stereotype argimp
 */
public class AssessRiskArgImp extends CommandArgImp implements AssessRiskArg {

    static final long serialVersionUID = 1199346453402049909L;

    private Policy policyArgRet;

    /** Default constructor */
    public AssessRiskArgImp() {
    }

    /**
     * {@inheritDoc}
     * @see #setPolicyArgRet
     * @return value of policy
     */
    public Policy getPolicyArgRet() {
        return this.policyArgRet;
    }

    /**
     * {@inheritDoc}
     * @see #getPolicyArgRet
     * @param policyArgRet New value for policy argument.
     */
    public void setPolicyArgRet(Policy policyArgRet) {
        this.policyArgRet = policyArgRet;
    }
}
