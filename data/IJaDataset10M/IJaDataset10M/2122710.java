package com.tt.bnct.domain;

import java.util.List;

public class BiologicalProcess {

    private String id;

    private String name;

    private Organism organism;

    private List<SubLevelProcess> subLevelProcesses;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return organism.getId() + id;
    }

    public Organism getOrganism() {
        return organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
    }

    public List<SubLevelProcess> getSubLevelProcesses() {
        return subLevelProcesses;
    }

    public void setSubLevelProcesses(List<SubLevelProcess> subLevelProcesses) {
        this.subLevelProcesses = subLevelProcesses;
    }

    public boolean equals(Object obj) {
        if (obj instanceof BiologicalProcess) {
            BiologicalProcess process = (BiologicalProcess) obj;
            if (id.equals(process.id) && organism.equals(process.organism)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return id.hashCode() ^ organism.hashCode();
    }

    public String toString() {
        return id;
    }
}
