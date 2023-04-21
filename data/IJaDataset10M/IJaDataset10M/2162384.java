package com.tt.bnct.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Enzyme implements MetabolicVertex {

    private String id;

    private List<String> names = new ArrayList<String>();

    private List<String> reactions;

    private int weight;

    private Date createDate;

    public Enzyme(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<String> getNames() {
        return names;
    }

    public void addName(String nameToAdd) {
        boolean exist = false;
        for (String name : names) {
            if (nameToAdd.equalsIgnoreCase(name)) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            names.add(nameToAdd);
        }
    }

    public void addNames(List<String> namesToAdd) {
        for (String nameToAdd : namesToAdd) {
            addName(nameToAdd);
        }
    }

    public List<String> getReactions() {
        return reactions;
    }

    public void setReactions(List<String> reactions) {
        this.reactions = reactions;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getVertexName() {
        return id;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Enzyme) {
            Enzyme enzyme = (Enzyme) obj;
            if (id.equals(enzyme.id)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return id.hashCode();
    }

    public String toString() {
        return "Enzyme (id: '" + id + "', weight: '" + weight + "')";
    }
}
