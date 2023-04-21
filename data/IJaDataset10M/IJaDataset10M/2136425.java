package org.mftech.dawn.server.projects;

import java.util.Comparator;

public class ProjectComparator implements Comparator<Project> {

    @Override
    public int compare(Project p1, Project p2) {
        System.out.println("HALLOOOOOO");
        return p1.getName().compareTo(p2.getName());
    }
}
