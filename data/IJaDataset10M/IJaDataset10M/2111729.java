package org.qctools4j.model.metadata;

import java.util.Collection;
import java.util.TreeSet;

/**
 * A Domain.
 * 
 * @author tszadel
 */
public class Domain implements Comparable<Domain> {

    private String name;

    private Collection<Project> projects;

    /**
	 * Constructor.
	 * 
	 * @param pName Name of the domain.
	 */
    public Domain(final String pName) {
        name = pName;
    }

    public Domain(final String pName, final Collection<Project> projects) {
        name = pName;
        this.projects = projects;
    }

    /**
	 * Overrides compareTo. {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(final Domain pDomain) {
        return getName().compareToIgnoreCase(pDomain.getName());
    }

    /**
	 * Returns name.
	 * 
	 * @return The name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Returns projects.
	 * 
	 * @return The projects.
	 */
    public Collection<Project> getProjects() {
        if (projects == null) {
            projects = new TreeSet<Project>();
        }
        return projects;
    }

    /**
	 * Sets the name.
	 * 
	 * @param pName The name.
	 */
    public void setName(final String pName) {
        name = pName;
    }

    /**
	 * Sets the projects.
	 * 
	 * @param pProjects The projects.
	 */
    public void setProjects(final Collection<Project> pProjects) {
        projects = pProjects;
    }

    /**
	 * Overrides toString. {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        final StringBuilder lStr = new StringBuilder("Domain: " + getName());
        lStr.append(" - Projects: ");
        if (getProjects().isEmpty()) {
            lStr.append("None");
        } else {
            String lSep = "";
            for (final Project lPrj : projects) {
                lStr.append(lSep);
                lStr.append(lPrj);
                lSep = ",";
            }
        }
        return lStr.toString();
    }
}
