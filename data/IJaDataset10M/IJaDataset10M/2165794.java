package net.sourceforge.sfeclipse.model;

import java.util.HashMap;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

public class SymfonyModel {

    /**
	 * TODO: caching model
	 */
    private IProject project;

    private HashMap<IFolder, SymfonyApplication> applications;

    private HashMap<IFolder, SymfonyPlugin> plugins;

    private HashMap<IFolder, SymfonyModule> modules;

    private HashMap<IFile, SymfonyAction> actions;

    private HashMap<IFile, SymfonyComponent> components;

    private HashMap<IFile, SymfonyTask> tasks;

    private HashMap<IFile, SymfonyConfig> configs;

    public SymfonyModel(IProject project) {
        this.project = project;
    }

    public void addPlugin(SymfonyPlugin plugin) {
        getPlugins().put((IFolder) plugin.getResource(), plugin);
    }

    public void removePlugin(SymfonyPlugin plugin) {
        getPlugins().remove(plugin.getResource());
    }

    public void addModule(SymfonyModule module, SymfonyApplication app) {
        app.addModule(module);
        getModules().put((IFolder) module.getResource(), module);
    }

    public void removeModule(SymfonyModule module) {
        getModules().remove(module);
        module.getParent().removeModule(module);
    }

    public IProject getProject() {
        return project;
    }

    public HashMap<IFolder, SymfonyApplication> getApplications() {
        if (applications == null) {
            applications = new HashMap<IFolder, SymfonyApplication>();
        }
        return applications;
    }

    public HashMap<IFolder, SymfonyPlugin> getPlugins() {
        if (plugins == null) {
            plugins = new HashMap<IFolder, SymfonyPlugin>();
        }
        return plugins;
    }

    public HashMap<IFolder, SymfonyModule> getModules() {
        if (modules == null) {
            modules = new HashMap<IFolder, SymfonyModule>();
        }
        return modules;
    }

    public void addApplication(SymfonyApplication app) {
        getApplications().put((IFolder) app.getResource(), app);
    }

    public void removeApplication(SymfonyApplication app) {
        getApplications().remove((IFolder) app.getResource());
    }

    public SymfonyModule getModule(IFolder folder) {
        return getModules().get(folder);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SymfonyModel(" + getProject().getName() + ")\n");
        sb.append("applications:\n");
        for (SymfonyApplication app : getApplications().values()) {
            sb.append(app);
        }
        sb.append("plugins:\n");
        for (SymfonyPlugin plugin : getPlugins().values()) {
            sb.append(plugin);
        }
        return sb.toString();
    }

    public HashMap<IFile, SymfonyAction> getActions() {
        if (actions == null) {
            actions = new HashMap<IFile, SymfonyAction>();
        }
        return actions;
    }

    public void setActions(HashMap<IFile, SymfonyAction> actions) {
        this.actions = actions;
    }

    public HashMap<IFile, SymfonyComponent> getComponents() {
        if (components == null) {
            components = new HashMap<IFile, SymfonyComponent>();
        }
        return components;
    }

    public void setComponents(HashMap<IFile, SymfonyComponent> components) {
        this.components = components;
    }

    public HashMap<IFile, SymfonyTask> getTasks() {
        if (tasks == null) {
            tasks = new HashMap<IFile, SymfonyTask>();
        }
        return tasks;
    }

    public void setTasks(HashMap<IFile, SymfonyTask> tasks) {
        this.tasks = tasks;
    }

    public HashMap<IFile, SymfonyConfig> getConfigs() {
        if (configs == null) {
            configs = new HashMap<IFile, SymfonyConfig>();
        }
        return configs;
    }

    public void setConfigs(HashMap<IFile, SymfonyConfig> configs) {
        this.configs = configs;
    }
}
