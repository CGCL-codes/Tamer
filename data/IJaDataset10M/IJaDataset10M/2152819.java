package com.express.domain;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import javax.persistence.*;

/** * Models the level of access a User has to a Project. * * @author adam boas */
@Entity
@Table(name = "permissions")
public class Permissions implements Persistable {

    private static final long serialVersionUID = 2241450015771645982L;

    @Transient
    private PersistableEqualityStrategy equalityStrategy = new PersistableEqualityStrategy(this);

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "gen_permissions")
    @TableGenerator(name = "gen_permissions", table = "sequence_list", pkColumnName = "name", valueColumnName = "next_value", allocationSize = 1, initialValue = 3000, pkColumnValue = "permissions")
    @Column(name = "permissions_id")
    private Long id;

    @Version
    @Column(name = "version_no")
    private Long version;

    @Column(name = "project_admin")
    private Boolean projectAdmin = Boolean.FALSE;

    @Column(name = "iteration_admin")
    private Boolean iterationAdmin = Boolean.FALSE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIterationAdmin() {
        return iterationAdmin;
    }

    public void setIterationAdmin(Boolean iterationAdmin) {
        this.iterationAdmin = iterationAdmin;
    }

    public Boolean getProjectAdmin() {
        return projectAdmin;
    }

    public void setProjectAdmin(Boolean projectAdmin) {
        this.projectAdmin = projectAdmin;
    }

    @Override
    public boolean equals(Object obj) {
        return equalityStrategy.entityEquals(obj);
    }

    @Override
    public int hashCode() {
        return equalityStrategy.entityHashCode(super.hashCode());
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }
}
