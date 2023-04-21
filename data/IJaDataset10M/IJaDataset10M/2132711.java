package fr.umlv.jee.hibou.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean class for Maven list object.
 * @author micka, alex, nak, matt
 *
 */
@XmlRootElement(name = "mavenListBean", namespace = "http://javax.hibou/jaxws")
public class MavenListBean {

    @XmlElement(name = "base", namespace = "")
    @XmlList
    private String[] base;

    @XmlElement(name = "dependencies", namespace = "")
    @XmlList
    private String[] dependencies;

    @XmlElement(name = "dependencyGroupId", namespace = "")
    private String dependencyGroupId;

    @XmlElement(name = "dependencyArtifactId", namespace = "")
    private String dependencyArtifactId;

    @XmlElement(name = "dependencyVersion", namespace = "")
    private String dependencyVersion;

    @XmlElement(name = "dependencyScope", namespace = "")
    private String dependencyScope;

    @XmlElement(name = "plugins", namespace = "")
    @XmlList
    private String[] plugins;

    @XmlElement(name = "pluginGroupId", namespace = "")
    private String pluginGroupId;

    @XmlElement(name = "pluginArtifactId", namespace = "")
    private String pluginArtifactId;

    @XmlElement(name = "pluginVersion", namespace = "")
    private String pluginVersion;

    @XmlElement(name = "modules", namespace = "")
    @XmlList
    private String[] modules;

    @XmlElement(name = "nameModule", namespace = "")
    private String nameModule;

    /**
	 * @return the base
	 */
    public String[] getBase() {
        return base;
    }

    /**
	 * @param base the base to set
	 */
    public void setBase(String[] base) {
        this.base = base;
    }

    /**
	 * @return the dependencies
	 */
    public String[] getDependencies() {
        return dependencies;
    }

    /**
	 * @param dependencies the dependencies to set
	 */
    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }

    /**
	 * @return the dependencyArtifactId
	 */
    public String getDependencyArtifactId() {
        return dependencyArtifactId;
    }

    /**
	 * @param dependencyArtifactId the dependencyArtifactId to set
	 */
    public void setDependencyArtifactId(String dependencyArtifactId) {
        this.dependencyArtifactId = dependencyArtifactId;
    }

    /**
	 * @return the dependencyGroupId
	 */
    public String getDependencyGroupId() {
        return dependencyGroupId;
    }

    /**
	 * @param dependencyGroupId the dependencyGroupId to set
	 */
    public void setDependencyGroupId(String dependencyGroupId) {
        this.dependencyGroupId = dependencyGroupId;
    }

    /**
	 * @return the dependencyScope
	 */
    public String getDependencyScope() {
        return dependencyScope;
    }

    /**
	 * @param dependencyScope the dependencyScope to set
	 */
    public void setDependencyScope(String dependencyScope) {
        this.dependencyScope = dependencyScope;
    }

    /**
	 * @return the dependencyVersion
	 */
    public String getDependencyVersion() {
        return dependencyVersion;
    }

    /**
	 * @param dependencyVersion the dependencyVersion to set
	 */
    public void setDependencyVersion(String dependencyVersion) {
        this.dependencyVersion = dependencyVersion;
    }

    /**
	 * @return the modules
	 */
    public String[] getModules() {
        return modules;
    }

    /**
	 * @param modules the modules to set
	 */
    public void setModules(String[] modules) {
        this.modules = modules;
    }

    /**
	 * @return the nameModule
	 */
    public String getNameModule() {
        return nameModule;
    }

    /**
	 * @param nameModule the nameModule to set
	 */
    public void setNameModule(String nameModule) {
        this.nameModule = nameModule;
    }

    /**
	 * @return the pluginArtifactId
	 */
    public String getPluginArtifactId() {
        return pluginArtifactId;
    }

    /**
	 * @param pluginArtifactId the pluginArtifactId to set
	 */
    public void setPluginArtifactId(String pluginArtifactId) {
        this.pluginArtifactId = pluginArtifactId;
    }

    /**
	 * @return the pluginGroupId
	 */
    public String getPluginGroupId() {
        return pluginGroupId;
    }

    /**
	 * @param pluginGroupId the pluginGroupId to set
	 */
    public void setPluginGroupId(String pluginGroupId) {
        this.pluginGroupId = pluginGroupId;
    }

    /**
	 * @return the plugins
	 */
    public String[] getPlugins() {
        return plugins;
    }

    /**
	 * @param plugins the plugins to set
	 */
    public void setPlugins(String[] plugins) {
        this.plugins = plugins;
    }

    /**
	 * @return the pluginVersion
	 */
    public String getPluginVersion() {
        return pluginVersion;
    }

    /**
	 * @param pluginVersion the pluginVersion to set
	 */
    public void setPluginVersion(String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }
}
