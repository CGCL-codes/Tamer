package org.apache.isis.extensions.jpa.testapp.claims.dom.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import org.apache.isis.applib.AbstractDomainObject;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.extensions.jpa.testapp.claims.dom.claim.Approver;
import org.apache.isis.extensions.jpa.testapp.claims.dom.claim.Claimant;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

@Entity
public class Employee extends AbstractDomainObject implements Claimant, Approver {

    public String title() {
        return getName();
    }

    private Long id;

    @Hidden
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    @Any(metaColumn = @Column(name = "approver_type", length = 3), fetch = FetchType.LAZY)
    @AnyMetaDef(idType = "long", metaType = "string", metaValues = { @MetaValue(targetEntity = Employee.class, value = "EMP") })
    @JoinColumn(name = "approver_id")
    public Approver getApprover() {
        return approver;
    }

    public void setApprover(Approver approver) {
        this.approver = approver;
    }
}
