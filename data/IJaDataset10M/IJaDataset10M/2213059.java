package com.azirar.dna.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * The Class ProfilRoleRole.
 */
@Entity
@NamedQueries({ @NamedQuery(name = "selectProfilRoleById", query = "select profil from ProfilRole profilRole where profilRole.idProfilRole = :idProfilRole"), @NamedQuery(name = "selectProfilRoleByIdProfil", query = "select profil from ProfilRole profilRole where profilRole.idProfilRole = :idProfil"), @NamedQuery(name = "selectProfilRoleByIdRole", query = "select profil from ProfilRole profilRole where profilRole.idProfilRole = :idRole"), @NamedQuery(name = "selectAllProfilRoles", query = "select profil from ProfilRole profilRole") })
@Table(name = "dna_profil_role")
public class ProfilRole {

    /** id application contributeur. */
    @Id
    @Column(nullable = false, columnDefinition = "INTEGER", length = 9)
    @GeneratedValue(generator = "inc-gen")
    @GenericGenerator(name = "inc-gen", strategy = "increment")
    private int idProfilRole;

    /** The profil.*/
    @ManyToOne
    @JoinColumn(name = "idProfil")
    private Profil profil;

    /** The role. */
    @ManyToOne
    @JoinColumn(name = "idRole")
    private Role role;

    /**
	 * Gets the id profil role.
	 *
	 * @return the id profil role
	 */
    public int getIdProfilRole() {
        return idProfilRole;
    }

    /**
	 * Sets the id profil role.
	 *
	 * @param idProfilRoleRole the new id profil role
	 */
    public void setIdProfilRole(int idProfilRole) {
        this.idProfilRole = idProfilRole;
    }

    /**
	 * Gets the profil.
	 *
	 * @return the profil
	 */
    public Profil getProfil() {
        return profil;
    }

    /**
	 * Sets the profil.
	 *
	 * @param profil the new profil
	 */
    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    /**
	 * Gets the role.
	 *
	 * @return the role
	 */
    public Role getRole() {
        return role;
    }

    /**
	 * Sets the role.
	 *
	 * @param role the new role
	 */
    public void setRole(Role role) {
        this.role = role;
    }
}
