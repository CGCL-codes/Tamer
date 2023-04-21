package ma.chora.framework.mapping;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * --------- Mise à jour 1 ---------
 * @author rabbah
 * @Motif création de la classe Person
 * @Date 21 déc. 2008
 * @Description : Class ...
 * ---------------------------------
 */
@Entity
@Table(name = "person")
@NamedQueries({ @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"), @NamedQuery(name = "Person.findById", query = "SELECT p FROM Person p WHERE p.id = :id"), @NamedQuery(name = "Person.findByName", query = "SELECT p FROM Person p WHERE p.name = :name"), @NamedQuery(name = "Person.findBySurname", query = "SELECT p FROM Person p WHERE p.surname = :surname") })
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    public Person() {
    }

    public Person(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ma.chora.framework.mapping.Person[id=" + id + "]";
    }
}
