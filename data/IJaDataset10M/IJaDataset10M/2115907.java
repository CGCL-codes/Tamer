package org.bcholmes.jmicro.cif.model;

import static javax.persistence.GenerationType.AUTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.apache.commons.lang.StringUtils;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Party {

    private Long id;

    private List<Identifier> identifiers = Collections.synchronizedList(new ArrayList<Identifier>());

    @Id
    @GeneratedValue(strategy = AUTO)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "party_id")
    public List<Identifier> getIdentifiers() {
        return this.identifiers;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public Identifier getIdentifier(String type) {
        Identifier result = null;
        for (Identifier identifier : this.identifiers) {
            if (StringUtils.equals(type, identifier.getType())) {
                result = identifier;
                break;
            }
        }
        return result;
    }
}
