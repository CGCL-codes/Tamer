package org.jprovocateur.basis.objectmodel.accessrights.impl;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import org.jprovocateur.basis.objectmodel.accessrights.BasisClassInt;

/**
 * <p/>
 *
 * @author Michael Pitsounis 
 */
@Entity
@Table(name = "SERVICE")
public class BasisClass implements java.io.Serializable, BasisClassInt {

    @Id
    @Column(name = "SERVICEID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "basis_gen")
    @SequenceGenerator(name = "basis_gen", sequenceName = "basis_seq")
    @Length(max = 38)
    private Long serviceId;

    @Column(name = "CLASSNAME")
    @NotNull
    @NotEmpty
    @Length(max = 300)
    private String classname;

    @Column(name = "DESCRIPTION")
    @Length(max = 1000)
    private String description;

    @Column(name = "DISABLED")
    @NotNull
    @NotEmpty
    @Length(max = 1)
    private Long disabled;

    @Column(name = "SHORTNAME")
    @NotNull
    @NotEmpty
    @Length(max = 50)
    private String shortname;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bc")
    private Set<AccessRights> accessRights = new HashSet<AccessRights>(0);

    /** default constructor */
    public BasisClass() {
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getClassname() {
        return this.classname;
    }

    public void setClassname(String classname) {
        this.classname = classname.trim();
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDisabled() {
        return this.disabled;
    }

    public void setDisabled(Long disabled) {
        this.disabled = disabled;
    }

    public Set getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(Set accessRights) {
        this.accessRights = accessRights;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname.trim();
    }
}
