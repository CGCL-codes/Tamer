package com.daveoxley.cnery.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.jboss.seam.security.annotations.management.IdentityProperty;
import org.jboss.seam.security.annotations.management.PropertyType;
import org.jboss.solder.core.Veto;

/**
 *
 * @author Dave Oxley <dave@daveoxley.co.uk>
 */
@Entity
@Veto
public class Identity implements Serializable {

    private static final long serialVersionUID = -8136797293873610623L;

    private Long id;

    private Long version;

    private String name;

    private String credential;

    private String credentialType;

    private IdentityType type;

    @Id
    @GeneratedValue
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @NotNull
    @Size(min = 1, max = 32)
    @Column(unique = true, nullable = false)
    @IdentityProperty(PropertyType.NAME)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @IdentityProperty(PropertyType.CREDENTIAL)
    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    @IdentityProperty(PropertyType.CREDENTIAL_TYPE)
    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    @ManyToOne
    @IdentityProperty(PropertyType.TYPE)
    @JoinColumn(name = "IDENTITY_OBJECT_TYPE_ID")
    public IdentityType getType() {
        return type;
    }

    public void setType(IdentityType type) {
        this.type = type;
    }
}
