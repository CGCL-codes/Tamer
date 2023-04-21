package org.openliberty.arisid.policy.neethi;

import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.neethi.Assertion;
import org.apache.neethi.AssertionBuilderFactory;
import org.apache.neethi.builders.AssertionBuilder;
import org.openliberty.arisid.policy.IContractOrLegalConstraint;
import org.openliberty.arisid.stack.PolicyException;

/**
 * 
 */
public class ContractOrLegalConstraintBuilder implements AssertionBuilder {

    public Assertion build(OMElement arg0, AssertionBuilderFactory arg1) throws IllegalArgumentException {
        try {
            return new ContractOrLegalConstraint(arg0);
        } catch (PolicyException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid policy definition: " + e.getMessage());
        }
    }

    public QName[] getKnownElements() {
        QName[] names = new QName[1];
        names[0] = IContractOrLegalConstraint.qelement;
        return names;
    }
}
