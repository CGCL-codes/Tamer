package org.internna.ossmoney.model;

import java.util.Set;
import java.util.HashSet;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import org.internna.ossmoney.model.security.UserDetails;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooSerializable
@RooJpaActiveRecord
public class FinancialInstitution extends AbstractEntity implements Comparable<FinancialInstitution> {

    private static final long serialVersionUID = 7222421682406951594L;

    @NotNull
    @Size(min = 3)
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "heldAt")
    private Set<Account> accounts = new HashSet<Account>();

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private UserDetails owner;

    private String web, icon;

    public static FinancialInstitution findByName(String name) {
        UserDetails owner = UserDetails.findCurrentUser();
        return entityManager().createQuery("select f from FinancialInstitution f where name = :name AND owner = :owner", FinancialInstitution.class).setParameter("name", name).setParameter("owner", owner).getSingleResult();
    }

    @Override
    public int compareTo(FinancialInstitution other) {
        return other == null ? -1 : name.compareTo(other.name);
    }

    public void addAccount(Account account) {
        if (account != null) {
            if (accounts == null) {
                accounts = new HashSet<Account>();
            }
            accounts.add(account);
            account.setHeldAt(this);
        }
    }
}
