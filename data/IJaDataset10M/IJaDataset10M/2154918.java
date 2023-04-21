package net.larsbehnke.petclinicplus.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

/**
 * Simple JavaBean domain object representing an owner.
 * <p>
 * Corresponding mapping:
 * </p>
 * 
 * <pre>
 *  &lt;entity class=&quot;Owner&quot;&gt;
 *        &lt;table name=&quot;OWNERS&quot; /&gt;
 *        &lt;attributes&gt;
 *            &lt;basic name=&quot;address&quot;&gt;
 *                &lt;column name=&quot;ADDRESS&quot; /&gt;
 *            &lt;/basic&gt;
 *            &lt;basic name=&quot;city&quot;&gt;
 *                &lt;column name=&quot;CITY&quot; /&gt;
 *            &lt;/basic&gt;
 *            &lt;basic name=&quot;telephone&quot;&gt;
 *                &lt;column name=&quot;TELEPHONE&quot; /&gt;
 *            &lt;/basic&gt;
 *            &lt;one-to-many name=&quot;petsInternal&quot; target-entity=&quot;Pet&quot; mapped-by=&quot;owner&quot; fetch=&quot;EAGER&quot;&gt;
 *                &lt;cascade&gt;
 *                    &lt;cascade-all /&gt;
 *                &lt;/cascade&gt;
 *            &lt;/one-to-many&gt;
 *            &lt;transient name=&quot;pets&quot; /&gt;
 *        &lt;/attributes&gt;
 *    &lt;/entity&gt;
 * </pre>
 * 
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Lars Behnke
 */
@Entity
@Table(name = "OWNERS")
public class Owner extends UserDataHolder implements Serializable {

    private static final long serialVersionUID = 488926730692029206L;

    private String address;

    private String city;

    private String telephone;

    private Set<Pet> pets;

    @Column(name = "ADDRESS")
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "CITY")
    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "TELEPHONE")
    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    protected void setPetsInternal(Set<Pet> pets) {
        this.pets = pets;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "owner", targetEntity = Pet.class)
    protected Set<Pet> getPetsInternal() {
        if (this.pets == null) {
            this.pets = new HashSet<Pet>();
        }
        return this.pets;
    }

    @Transient
    public List<Pet> getPets() {
        List<Pet> sortedPets = new ArrayList<Pet>(getPetsInternal());
        PropertyComparator.sort(sortedPets, new MutableSortDefinition("name", true, true));
        return Collections.unmodifiableList(sortedPets);
    }

    public void addPet(Pet pet) {
        getPetsInternal().add(pet);
        pet.setOwner(this);
    }

    /**
     * Return the Pet with the given name, or null if none found for this Owner.
     * @param name to test
     * @return true if pet name is already in use
     */
    public Pet getPet(String name) {
        return getPet(name, false);
    }

    /**
     * Return the Pet with the given name, or null if none found for this Owner.
     * @param name to test
     * @return true if pet name is already in use
     */
    public Pet getPet(String name, boolean ignoreNew) {
        name = name.toLowerCase();
        for (Pet pet : getPetsInternal()) {
            if (!ignoreNew || !pet.isNew()) {
                String compName = pet.getName();
                compName = compName.toLowerCase();
                if (compName.equals(name)) {
                    return pet;
                }
            }
        }
        return null;
    }
}
