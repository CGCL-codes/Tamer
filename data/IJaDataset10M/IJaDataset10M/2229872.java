package com.ncs.portal.to;

import com.ncs.common.to.BaseEntity;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tbl_aa_role")
public class RoleTo extends BaseEntity {

    private static final long serialVersionUID = -6718238800112233445L;

    private String name;

    private String value;

    private Boolean isSystem;

    private String description;

    private String ouId;

    private String projectId;

    private Set<SubjectTo> subject;

    private Set<ResourceTo> resourceSet;

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(nullable = false, updatable = false)
    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    @Column(length = 5000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roleSet")
    public Set<SubjectTo> getSubject() {
        return subject;
    }

    public void setSubject(Set<SubjectTo> subject) {
        this.subject = subject;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public Set<ResourceTo> getResourceSet() {
        return resourceSet;
    }

    public void setResourceSet(Set<ResourceTo> resourceSet) {
        this.resourceSet = resourceSet;
    }

    @Column(length = 32)
    public String getOuId() {
        return ouId;
    }

    public void setOuId(String ouId) {
        this.ouId = ouId;
    }

    @Column(nullable = false, length = 32)
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
