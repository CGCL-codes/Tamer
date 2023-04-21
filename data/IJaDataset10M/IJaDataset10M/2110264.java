package be.fedict.eid.idp.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * General configuration entity used for various IdP configurations like the
 * identity, global pseudonym configuration, ... .
 */
@Entity
@Table(name = Constants.DATABASE_TABLE_PREFIX + "configuration")
@NamedQueries(@NamedQuery(name = ConfigPropertyEntity.LIST_INDEXES, query = "FROM ConfigPropertyEntity WHERE name LIKE :name"))
public class ConfigPropertyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String LIST_INDEXES = "idp.config.list.idx";

    private String name;

    private String value;

    public ConfigPropertyEntity() {
        super();
    }

    public ConfigPropertyEntity(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Id
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public static List<ConfigPropertyEntity> listConfigsWhereNameLike(EntityManager entityManager, String name) {
        return entityManager.createNamedQuery(ConfigPropertyEntity.LIST_INDEXES).setParameter("name", "%" + name + "%").getResultList();
    }
}
