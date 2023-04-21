package org.openremote.modeler.server.lutron.importmodel;

import java.util.HashSet;
import java.util.Set;

public class Project {

    private String name;

    private Set<Area> areas;

    public Project(String name) {
        super();
        this.name = name;
        this.areas = new HashSet<Area>();
    }

    public String getName() {
        return name;
    }

    public Set<Area> getAreas() {
        return areas;
    }

    public void addArea(Area area) {
        areas.add(area);
    }
}
