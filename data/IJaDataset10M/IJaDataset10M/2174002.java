package org.in4ama.editor.projectdesigner;

import javax.swing.JPanel;
import org.in4ama.documentengine.exception.ProjectException;
import org.in4ama.documentengine.project.Project;
import org.in4ama.documentengine.project.ProjectMgr;
import org.in4ama.documentengine.project.ProjectMgrConfiguration;

/** Provides access to UI components allowing to set up a new 
 * project of a certain type. */
public interface ProjectDesigner {

    /** Gets the type of the associated project manager. */
    String getType();

    /** Gets the name that will appear in the studio. */
    String getName();

    /** Returns the panel that allows to configure a new
	 * project of this type.   
	 * The returned object should be a singleton. */
    JPanel getPanelNew();

    /** Returns the panel that allows to open an existing project. */
    JPanel getPanelLoad();

    /** Get the 24x24 icon associated with this project type */
    String getIcon24();

    /** Gets the project mgr configuration object that is currently
	 * 'stored' in the configuration panel. */
    ProjectMgrConfiguration getConfiguration();

    /** Returns the name of the project that is to be created/loaded. */
    String getProjectName();

    /** Stores the values 'kept' in the panel's fields into the underlying
	 * document mgr configuration object. When this method returns true a new project
	 * is created, false nothing happens. */
    boolean saveConfigurationNew() throws Exception;

    /** Stores the values 'kept' in the panel's fields into the underlying
	 * document mgr configuration object. When this method returns true a project is opened
	 * false nothing happens. */
    boolean saveConfigurationLoad() throws Exception;

    /** Creates a new not initialised document mgr configuration object */
    void newConfiguration();

    /** Updates the UI of the new project panel. */
    void updatePanelNew();

    /** Updates the UI of the load project panel. */
    void updatePanelLoad();

    /** Sets the new project mgr configuration object with which the
     * configuration panel will be updated. */
    void setConfiguration(ProjectMgrConfiguration configuration);

    /** Indicates whether this designer provides UI components for
     * configuring a new project. */
    boolean hasNewUI();

    /** Indicates whether this designer provides UI components for
     * selecting/configuring an existing project. */
    boolean hasLoadUI();

    /** Callback method. Allows to amend a project that has just been created by, for example,
     * adding a default document manager to it. After this method is called the project will
     * is saved using project manager returned by this object. */
    void projectCreated(ProjectMgr projectMgr, Project project) throws ProjectException;

    /** Callback method. Allows to amend a project that has just been created by, for example,
     * adding a default document manager to it. */
    void projectLoaded(ProjectMgr projectMgr, Project project) throws ProjectException;

    String getMRUDescription();
}
