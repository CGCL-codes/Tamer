package org.localstorm.mcc.ejb.people.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.localstorm.mcc.ejb.AbstractEntity;
import org.localstorm.mcc.ejb.Identifiable;
import org.localstorm.mcc.ejb.users.User;

/**
 *
 * @author localstorm
 */
@Entity
@Table(name = "PERSON_GROUPS")
@NamedQueries({ @NamedQuery(name = PersonGroup.Queries.FIND_BY_OWNER, query = "SELECT o FROM PersonGroup o WHERE o.owner=:owner and o.archived=false ORDER BY o.name"), @NamedQuery(name = PersonGroup.Queries.FIND_ARCHIVED_BY_OWNER, query = "SELECT o FROM PersonGroup o WHERE o.owner=:owner and o.archived=true ORDER BY o.name"), @NamedQuery(name = PersonGroup.Queries.DELETE_ORPHAN_PERSONS, query = "DELETE Person p WHERE p IN (SELECT o.person FROM PersonToGroup o WHERE o.group=:group) AND p NOT IN (SELECT o.person FROM PersonToGroup o WHERE o.group<>:group)") })
public class PersonGroup extends AbstractEntity implements Serializable, Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", unique = false, updatable = true, nullable = false)
    private String name;

    @Column(name = "is_archived", unique = false, updatable = true, nullable = false)
    private boolean archived;

    @JoinColumn(name = "owner", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    private static final long serialVersionUID = -4914596367651790259L;

    public PersonGroup() {
    }

    public PersonGroup(String name) {
        this.name = name;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isArchived() {
        return archived;
    }

    public static interface Queries {

        public static final String FIND_BY_OWNER = "findPGroupsByOwner";

        public static final String FIND_ARCHIVED_BY_OWNER = "findPGArchivedByOwner";

        public static final String DELETE_ORPHAN_PERSONS = "deleteOrphanPersons";
    }

    public static interface Properties {

        public static final String OWNER = "owner";

        public static final String GROUP = "group";
    }
}
