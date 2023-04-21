package model.project;

import java.util.List;
import model.an.ActivityNetwork;
import model.domain.ClassDiagram;

public class Project {

    public String name;

    public String location;

    public ActivityNetwork activityNetwork;

    public List<Resource> resources;

    public ClassDiagram classDiagram;
}
