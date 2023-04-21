package org.whatsitcalled.webflange.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Script implements Serializable {

    private String name;

    private Long id;

    private Set<LoadTest> loadTests = new HashSet<LoadTest>();

    public Script() {
    }

    public Script(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        return this.getName() + this.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<LoadTest> getLoadTests() {
        return loadTests;
    }

    public void setLoadTests(Set<LoadTest> loadTests) {
        this.loadTests = loadTests;
    }
}
