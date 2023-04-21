package org.parallelj.tools.maven.ui.wizard.project;

import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.IProgressConstants;
import org.parallelj.tools.maven.ui.Messages;
import org.parallelj.tools.maven.ui.archetype.ArchetypeApplication;
import org.parallelj.tools.maven.ui.wizard.tools.Maven2ProjectCreator;

/**
 * Job dedicated to creation of Projects from Archetypes
 * 
 * @author Atos Worldline
 * 
 */
public class Maven2ArchetypesCreationJob extends Job {

    private List<ArchetypeApplication> archetypeApplication;

    private Map<String, String> properties;

    /**
	 * Constructor
	 * 
	 */
    public Maven2ArchetypesCreationJob(List<ArchetypeApplication> archetypeApplication) {
        super(Messages.archetypeInstallTaskTitle);
        this.setUser(true);
        this.archetypeApplication = archetypeApplication;
        this.setRule(ResourcesPlugin.getWorkspace().getRoot());
        this.setPriority(Job.BUILD);
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    protected IStatus run(IProgressMonitor arg0) {
        this.setProperty(IProgressConstants.KEEP_PROPERTY, Boolean.TRUE);
        Maven2ProjectCreator creator = new Maven2ProjectCreator(this.archetypeApplication);
        creator.setProperties(this.properties);
        return creator.run(arg0);
    }
}
