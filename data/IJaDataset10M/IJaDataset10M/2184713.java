package fr.gestionimmoejb.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Utilisateur implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    private String nom;

    private String prenom;

    private String email;

    private String passwd;

    @OneToMany(mappedBy = "user")
    private Set<Role> roleCollection;

    private static final long serialVersionUID = 1L;

    public Utilisateur() {
        super();
    }

    public Utilisateur(Integer id, String nom, String prenom, String email, String passwd, Set<Role> roleCollection) {
        super();
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.passwd = passwd;
        this.roleCollection = roleCollection;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Set<Role> getRoleCollection() {
        return this.roleCollection;
    }

    public void setRoleCollection(Set<Role> roleCollection) {
        this.roleCollection = roleCollection;
    }
}
