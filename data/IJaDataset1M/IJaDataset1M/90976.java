package br.com.mcampos.ejb.cloudsystem.system.loginproperty.entity;

import br.com.mcampos.ejb.cloudsystem.system.systemuserproperty.entity.SystemUserProperty;
import br.com.mcampos.ejb.cloudsystem.user.collaborator.entity.Collaborator;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = LoginProperty.getAll, query = "select o from LoginProperty o where o.collaborator = ?1") })
@Table(name = "login_property")
@IdClass(LoginPropertyPK.class)
public class LoginProperty implements Serializable {

    public static final String getAll = "LoginProperty.findAll";

    @Id
    @Column(name = "sup_id_in", nullable = false, insertable = false, updatable = false)
    private Integer propertyId;

    @Id
    @Column(name = "usr_id_in", nullable = false, insertable = false, updatable = false)
    private Integer userId;

    @Id
    @Column(name = "col_seq_in", nullable = false, insertable = false, updatable = false)
    private Integer sequence;

    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "sup_id_in")
    private SystemUserProperty systemUserProperty;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumns({ @JoinColumn(name = "col_seq_in", referencedColumnName = "col_seq_in", nullable = false, insertable = true, updatable = true), @JoinColumn(name = "usr_id_in", referencedColumnName = "usr_id_in", nullable = false, insertable = true, updatable = true) })
    private Collaborator collaborator;

    public LoginProperty() {
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer sup_id_in) {
        this.propertyId = sup_id_in;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer usr_id_in) {
        this.userId = usr_id_in;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SystemUserProperty getSystemUserProperty() {
        return systemUserProperty;
    }

    public void setSystemUserProperty(SystemUserProperty systemUserProperty) {
        this.systemUserProperty = systemUserProperty;
        if (systemUserProperty != null) {
            this.propertyId = systemUserProperty.getId();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return getCollaborator().equals(((LoginProperty) obj).getCollaborator()) && getSystemUserProperty().equals(((LoginProperty) obj).getSystemUserProperty());
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
        setSequence(collaborator != null ? collaborator.getSequence() : null);
        setUserId(collaborator != null ? collaborator.getCompanyId() : null);
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getSequence() {
        return sequence;
    }
}
