package net.zero.smarttrace.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

@Entity
@NamedQuery(name = "getFieldByName", query = "SELECT f FROM EField f " + "WHERE f.name = :name AND f.declaringType.name = :declaringTypeName")
public class EField {

    private Long id;

    private EReferenceType declaringType;

    private String signature;

    private String name;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public EReferenceType getDeclaringType() {
        return declaringType;
    }

    public void setDeclaringType(EReferenceType declaringType) {
        this.declaringType = declaringType;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
