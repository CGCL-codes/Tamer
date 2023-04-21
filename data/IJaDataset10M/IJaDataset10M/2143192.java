package com.definity.toolkit.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.definity.toolkit.domain.Domain;

@Entity
@Table
public class RoleDomain extends Domain<String> implements Role {

    private String name;

    public RoleDomain() {
    }

    public RoleDomain(String name) {
        this.name = name;
    }

    @Id
    @Override
    public String getId() {
        return name;
    }

    @Column(nullable = false)
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
