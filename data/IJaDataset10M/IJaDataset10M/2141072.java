package edu.ucdavis.genomics.metabolomics.binbase.cluster.ant;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;

/**
 * a simple parameter for our tasks
 * 
 * @author wohlgemuth
 * @version Jul 28, 2006
 */
public class ClusterParameter extends ProjectComponent {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2L;

    private String name = "not set";

    private String value = "not set";

    public ClusterParameter(Project project) {
        super();
        this.setProject(project);
    }

    public ClusterParameter(Project project, String name, String value) {
        super();
        this.setProject(project);
        this.setName(name);
        this.setValue(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        log("setting name: " + name, Project.MSG_DEBUG);
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        log("setting value: " + value, Project.MSG_DEBUG);
        this.value = value;
    }
}
