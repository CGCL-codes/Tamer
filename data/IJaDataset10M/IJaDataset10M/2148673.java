package org.nexopenframework.ide.eclipse.ui.model;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Custom implementation of the {@link IContinuumServer}</p>
 * 
 * @see IContinuumServer
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class ContinuumServerResource implements IContinuumServer, Comparable<IContinuumServer> {

    private String location;

    private String webLocation;

    private List<IProject> projects;

    /**
	 * 
	 * @see org.nexopenframework.ide.eclipse.ui.model.IContinuumServer#getLocation()
	 */
    public String getLocation() {
        return location;
    }

    /**
	 * @see org.nexopenframework.ide.eclipse.ui.model.IContinuumServer#setLocation(java.lang.String)
	 */
    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebLocation() {
        return webLocation;
    }

    public void setWebLocation(String webLocation) {
        this.webLocation = webLocation;
    }

    /**
	 * @see org.nexopenframework.ide.eclipse.ui.model.IContinuumServer#addProject(org.eclipse.core.resources.IProject)
	 */
    public void addProject(final IProject project) {
        if (getProjects() == null) {
            return;
        }
        this.getProjects().add(project);
    }

    public List<IProject> getProjects() {
        if (this.projects == null) {
            this.projects = new ArrayList<IProject>();
        }
        return projects;
    }

    public void removeProject(final IProject project) {
        if (getProjects() == null) {
            this.projects = new ArrayList<IProject>();
        }
        this.getProjects().remove(project);
    }

    public boolean isProjectPresent(final IProject project) {
        if (getProjects() == null) {
            this.projects = new ArrayList<IProject>();
        }
        return this.projects.contains(project);
    }

    /**
	 * 
	 * @see org.nexopenframework.ide.eclipse.ui.model.IContinuumServer#setProjects(java.util.List)
	 */
    public void setProjects(final List<IProject> projects) {
        this.projects = projects;
    }

    /**
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
    public Object getAdapter(Class adapter) {
        return Platform.getAdapterManager().getAdapter(this, adapter);
    }

    /**
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof IContinuumServer) && ((IContinuumServer) obj).getLocation().equals(location);
    }

    @Override
    public int hashCode() {
        if (this.location != null) {
            return this.location.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{location :: ");
        sb.append(this.location).append("}");
        return sb.toString();
    }

    public int compareTo(final IContinuumServer o) {
        if (o != null) {
            o.getLocation().compareTo(location);
        }
        return 0;
    }
}
