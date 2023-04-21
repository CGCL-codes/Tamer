package com.pentagaia.eclipse.sgs.prjconf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of project layout
 * 
 * @author mepeisen
 */
class ProjectLayout implements IProjectLayout {

    /** Layout id */
    private String layoutId;

    /** Layout type */
    private ProjectTypes type = ProjectTypes.TYPE_UNKNOWN;

    /** Displayable name */
    private String name;

    /** Maven activation */
    private boolean maven;

    /** Inheritance */
    private String inherit;

    /** The maven repositories */
    private final List<IMavenRepository> repositories = new ArrayList<IMavenRepository>();

    /** The source folders */
    private final List<String> sourceFolders = new ArrayList<String>();

    /** The target folder */
    private String targetFolder;

    /** The maven module dependencies */
    private final Map<String, List<IMavenModule>> mavenDependencies = new HashMap<String, List<IMavenModule>>();

    /** The maven module dependencies */
    private final List<IMavenModuleLayoutDependency> mavenModuleDependencies = new ArrayList<IMavenModuleLayoutDependency>();

    /** Maven inheritance */
    private IMavenModule mavenInherit;

    /** Additional properties */
    private final Map<String, Map<String, String>> properties = new HashMap<String, Map<String, String>>();

    /** Allowed runtimes */
    private final List<String> runtimes = new ArrayList<String>();

    /** The dynamic configuration */
    private String dynamicConfig;

    /** The static configuration */
    private String staticConfig;

    /**
     * Constructor
     * 
     * @param layoutId
     * @param name
     * @param type
     * @param maven
     * @param inherit
     */
    public ProjectLayout(String layoutId, String name, String type, String maven, String inherit) {
        this.layoutId = layoutId;
        this.name = name;
        if (type != null && type.length() > 0) {
            if (type.equals("client")) {
                this.type = ProjectTypes.TYPE_CLIENT;
            } else if (type.equals("server")) {
                this.type = ProjectTypes.TYPE_SERVER;
            }
        }
        this.maven = maven != null && maven.equals("active");
        this.inherit = inherit;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#allowesAllRuntimes()
     */
    @Override
    public boolean allowesAllRuntimes() {
        return this.runtimes.contains("*");
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#getAdditionalRepositories()
     */
    @Override
    public IMavenRepository[] getAdditionalRepositories() {
        return this.repositories.toArray(new IMavenRepository[this.repositories.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#getModuleDependencies()
     */
    @Override
    public Map<String, IMavenModule[]> getModuleDependencies() {
        final Map<String, IMavenModule[]> result = new HashMap<String, IMavenModule[]>();
        for (final Map.Entry<String, List<IMavenModule>> entry : this.mavenDependencies.entrySet()) {
            result.put(entry.getKey(), entry.getValue().toArray(new IMavenModule[entry.getValue().size()]));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#getDynamicAppConfig()
     */
    @Override
    public String getDynamicAppConfig() {
        return this.dynamicConfig;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#getId()
     */
    @Override
    public String getId() {
        return this.layoutId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#getInheritedMavenModule()
     */
    @Override
    public IMavenModule getInheritedMavenModule() {
        return this.mavenInherit;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#getMavenProperties()
     */
    @Override
    public Map<String, Map<String, String>> getMavenProperties() {
        return this.properties;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#getRuntimes()
     */
    @Override
    public String[] getRuntimes() {
        return this.runtimes.toArray(new String[this.runtimes.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#getSourceFolders()
     */
    @Override
    public String[] getSourceFolders() {
        return this.sourceFolders.toArray(new String[this.sourceFolders.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#getStaticAppConfig()
     */
    @Override
    public String getStaticAppConfig() {
        return this.staticConfig;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#getTargetFolder()
     */
    @Override
    public String getTargetFolder() {
        return this.targetFolder;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#isMavenActivated()
     */
    @Override
    public boolean isMavenActivated() {
        return this.maven;
    }

    /**
     * Adds a new source folder
     * 
     * @param folders
     */
    void addSourceFolders(Set<String> folders) {
        this.sourceFolders.addAll(folders);
    }

    /**
     * Sets the target folder
     * 
     * @param targetFolder
     */
    void setTargetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
    }

    /**
     * Adds target runtimes
     * 
     * @param targetRuntimes
     */
    void addTargetRuntimes(Set<String> targetRuntimes) {
        this.runtimes.addAll(targetRuntimes);
    }

    /**
     * Sets the app config
     * 
     * @param staticConfig
     * @param dynamicConfig
     */
    void setAppConfig(String staticConfig, String dynamicConfig) {
        this.staticConfig = staticConfig;
        this.dynamicConfig = dynamicConfig;
    }

    /**
     * Adds new maven properties
     * 
     * @param versionName
     * @param props
     */
    void addMavenProperties(String versionName, Map<String, String> props) {
        this.properties.put(versionName, props);
    }

    /**
     * Sets the maven inheritance
     * 
     * @param mavenModule
     */
    void setMavenInherit(IMavenModule mavenModule) {
        this.mavenInherit = mavenModule;
    }

    /**
     * Adds a new maven dependency
     * 
     * @param runtimeVersion
     * @param mavenModule
     */
    void addMavenDependency(final String runtimeVersion, IMavenModule mavenModule) {
        List<IMavenModule> list = this.mavenDependencies.get(runtimeVersion);
        if (list == null) {
            list = new ArrayList<IMavenModule>();
            this.mavenDependencies.put(runtimeVersion, list);
        }
        list.add(mavenModule);
    }

    /**
     * Adds a new maven dependency
     * 
     * @param dependencyModule
     */
    void addMavenDependency(IMavenModuleLayoutDependency dependencyModule) {
        this.mavenModuleDependencies.add(dependencyModule);
    }

    /**
     * Adds a new maven repository
     * 
     * @param dependency
     */
    void addMavenRepositoryDependency(IMavenRepository dependency) {
        this.repositories.add(dependency);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#getModuleLayoutDependencies()
     */
    @Override
    public IMavenModuleLayoutDependency[] getModuleLayoutDependencies() {
        return this.mavenModuleDependencies.toArray(new IMavenModuleLayoutDependency[this.mavenModuleDependencies.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#getProjectInheritance()
     */
    @Override
    public String getProjectInheritance() {
        return this.inherit;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.eclipse.sgs.prjconf.IProjectLayout#getType()
     */
    @Override
    public ProjectTypes getType() {
        return this.type;
    }
}
