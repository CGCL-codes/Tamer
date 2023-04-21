package org.starobjects.restful.testapp.dom.employee;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.starobjects.restful.testapp.dom.claim.Approver;
import org.starobjects.restful.testapp.dom.claim.Claimant;

public class Employee extends AbstractDomainObject implements Claimant, Approver {

    public String title() {
        return getName();
    }

    private String name;

    @MemberOrder(sequence = "1")
    public String getName() {
        return name;
    }

    public void setName(String lastName) {
        this.name = lastName;
    }

    private Approver approver;

    @MemberOrder(sequence = "2")
    public Approver getApprover() {
        return approver;
    }

    public void setApprover(Approver approver) {
        this.approver = approver;
    }
}
