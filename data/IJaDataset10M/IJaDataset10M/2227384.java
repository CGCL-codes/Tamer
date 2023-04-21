package com.migazzi.dm4j.ant;

import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import com.migazzi.dm4j.common.Type;
import com.migazzi.dm4j.repository.Repository;
import com.migazzi.dm4j.repository.RepositoryAccessException;

public class TypesTask extends Task implements TaskContainer {

    private String repositoryRef;

    private String listId;

    private String organisation;

    private String valueName;

    private String artifact;

    private String version;

    private List<Task> innerTasks = new ArrayList<Task>();

    @Override
    public void addTask(Task t) {
        innerTasks.add(t);
    }

    public String getRepositoryRef() {
        return repositoryRef;
    }

    public void setRepositoryRef(String repositoryRef) {
        this.repositoryRef = repositoryRef;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    @Override
    public void execute() throws BuildException {
        if (version == null) {
            throw new BuildException("version attribut must be set");
        }
        if (artifact == null) {
            throw new BuildException("artifact attribut must be set");
        }
        if (organisation == null) {
            throw new BuildException("organisation attribut must be set");
        }
        if (repositoryRef == null) {
            throw new BuildException("repositoryRef attribute must be set");
        }
        Repository repo = (Repository) getProject().getReference(repositoryRef);
        if (repo == null) {
            throw new BuildException("no repository with id " + repositoryRef);
        }
        List<Type> types;
        try {
            types = repo.getArtifactTypes(organisation, artifact, version);
        } catch (RepositoryAccessException e) {
            throw new BuildException("cannot access to repository", e);
        }
        if (listId != null) {
            getProject().addReference(listId, types);
        }
        if (innerTasks.size() > 0 && valueName != null) {
            for (Type type : types) {
                getProject().setProperty(valueName, type.name());
                for (Task task : innerTasks) {
                    task.perform();
                }
            }
        }
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getArtifact() {
        return artifact;
    }

    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
