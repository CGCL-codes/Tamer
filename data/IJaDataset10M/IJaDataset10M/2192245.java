package br.ufmg.lcc.pcollecta.dto;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *Represents one profile with one set of roles
 */
@Entity
@Table(name = "PC_PROFILE")
public class Profile extends PCollectaDTO {

    /**
	 *Description of the profile
	 */
    private String description;

    private List<ProfileRole> profileRoles;

    private List<Role> allRoles;

    @Column(name = "NAME")
    public String getName() {
        if (super.getName() != null) return super.getName().trim();
        return null;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        if (description != null) return description.trim();
        return null;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "master", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    public List<ProfileRole> getProfileRoles() {
        return profileRoles;
    }

    public void setProfileRoles(List<ProfileRole> profileRoles) {
        this.profileRoles = profileRoles;
    }

    @Transient
    public List<Role> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(List<Role> allRoles) {
        this.allRoles = allRoles;
    }

    @Id
    @Column(name = "ID_PROFILE")
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "SE_PROFILE")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQUENCE")
    @Override
    public Long getId() {
        return super.getId();
    }
}
