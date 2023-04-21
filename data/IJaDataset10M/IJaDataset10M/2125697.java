package fr.umlv.jee.hibou.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import fr.umlv.jee.hibou.bdd.table.Project;

/**
 * Bean class user's projects.
 * @author micka, alex, nak, matt
 *
 */
@XmlRootElement(name = "userProjectBean", namespace = "http://javax.hibou/jaxws")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserProjectBean {

    @XmlElement(name = "project", namespace = "")
    private ProjectBean project;

    @XmlElement(name = "username", namespace = "")
    private String username;

    @XmlElement(name = "isProjectLeacer", namespace = "")
    private boolean isProjectLeader;

    /**
	 * Constructor.
	 * @param project the project object.
	 * @param username the user's username.
	 * @param isProjectLeader a boolean value that indicates if the user is a leader or not.
	 */
    public UserProjectBean(Project project, String username, boolean isProjectLeader) {
        this.project = new ProjectBean(project);
        this.username = username;
        this.isProjectLeader = isProjectLeader;
    }

    /**
	 * @return the isProjectLeader
	 */
    public boolean isProjectLeader() {
        return isProjectLeader;
    }

    /**
	 * @param isProjectLeader the isProjectLeader to set
	 */
    public void setProjectLeader(boolean isProjectLeader) {
        this.isProjectLeader = isProjectLeader;
    }

    /**
	 * @return the project
	 */
    public ProjectBean getProject() {
        return project;
    }

    /**
	 * @param project the project to set
	 */
    public void setProject(ProjectBean project) {
        this.project = project;
    }

    /**
	 * @return the username
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * @param username the username to set
	 */
    public void setUsername(String username) {
        this.username = username;
    }
}
