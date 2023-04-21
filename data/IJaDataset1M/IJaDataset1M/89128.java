package fr.macymed.modulo.platform.archive;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import fr.macymed.commons.lang.HashCodeUtilities;
import fr.macymed.modulo.framework.module.VersionNumber;
import fr.macymed.modulo.platform.Constants;
import fr.macymed.modulo.platform.loader.ModuleDependency;

/** 
 * <p>
 * Contains the definition of a module, including id, version, dependencies, ...
 * </p>
 * @author <a href="mailto:alexandre.cartapanis@macymed.fr">Cartapanis Alexandre</a>
 * @version 2.1.8
 * @since Modulo Platform 2.0
 */
public class ModuleDefinition implements Serializable {

    /** The serial verion UID. */
    private static final long serialVersionUID = 3409657326876301875L;

    /** The version of the framework used for the definition. */
    private String frameworkVersion;

    /** The id of the module. */
    private String id;

    /** The version of the module. */
    private VersionNumber version;

    /** The name of the module. */
    private String metaName;

    /** The description of the module. */
    private String metaDescription;

    /** The copyright of the module. */
    private String metaCopyright;

    /** The name of the provider of the module. */
    private String metaProviderName;

    /** The address of the provider of the module. */
    private String metaProviderAddress;

    /** The phone of the provider of the module. */
    private String metaProviderPhone;

    /** The web of the provider of the module. */
    private String metaProviderWeb;

    /** The support of the provider of the module. */
    private String metaProviderSupport;

    /** The name of the module class. */
    private String moduleClassName;

    /** The name of the configuration class. */
    private String configurationClassName;

    /** The name of the statistics class. */
    private String statisticsClassName;

    /** The dependency of the modules. */
    private Set<ModuleDependency> dependencies;

    /**
     * <p>
     * Creates a new ModuleDefinition.
     * </p>
     */
    public ModuleDefinition() {
        super();
        this.frameworkVersion = "null";
        this.id = "null";
        this.version = new VersionNumber(0, 0, 0);
        this.metaName = "null";
        this.metaDescription = "null";
        this.metaCopyright = "null";
        this.metaProviderName = "null";
        this.metaProviderAddress = "null";
        this.metaProviderPhone = "null";
        this.metaProviderWeb = "null";
        this.metaProviderSupport = "null";
        this.moduleClassName = "null";
        this.configurationClassName = "null";
        this.statisticsClassName = "null";
        this.dependencies = new HashSet<ModuleDependency>();
    }

    /**
     * <p>
     * Returns the framework version used by this definition.
     * </p>
     * @return <code>String</code> - The framework version.
     */
    public String getFrameworkVersion() {
        return this.frameworkVersion;
    }

    /**
     * <p>
     * Changes the framework version used by this definition.
     * </p>
     * @param _frameworkVersion The new framework version.
     */
    public void setFrameworkVersion(String _frameworkVersion) {
        this.frameworkVersion = _frameworkVersion;
    }

    /**
     * <p>
     * Returns the id.
     * </p>
     * @return <code>String</code> - The id.
     */
    public String getId() {
        return this.id;
    }

    /**
     * <p>
     * Changes the id.
     * </p>
     * @param _id The new id.
     */
    public void setId(String _id) {
        this.id = _id;
    }

    /**
     * <p>
     * Returns the version.
     * </p>
     * @return <code>VersionNumber</code> - The version.
     */
    public VersionNumber getVersion() {
        return this.version;
    }

    /**
     * <p>
     * Changes the version.
     * </p>
     * @param _version The new version.
     */
    public void setVersion(VersionNumber _version) {
        this.version = _version;
    }

    /**
     * <p>
     * Returns the registering ID, {@link #getId()}{@value fr.macymed.modulo.platform.Constants#MODULE_ID_SEPARATOR}{@link #getVersion()}.
     * </p>
     * @return <code>String</code> - The registering ID.
     */
    public String getRegisteringId() {
        return this.id + Constants.MODULE_ID_SEPARATOR + this.version.toString();
    }

    /**
     * <p>
     * Returns the metaName.
     * </p>
     * @return <code>String</code> - The metaName.
     */
    public String getMetaName() {
        return this.metaName;
    }

    /**
     * <p>
     * Changes the metaName.
     * </p>
     * @param _metaName The new metaName.
     */
    public void setMetaName(String _metaName) {
        this.metaName = _metaName;
    }

    /**
     * <p>
     * Returns the metaDescription.
     * </p>
     * @return <code>String</code> - The metaDescription.
     */
    public String getMetaDescription() {
        return this.metaDescription;
    }

    /**
     * <p>
     * Changes the metaDescription.
     * </p>
     * @param _metaDescription The new metaDescription.
     */
    public void setMetaDescription(String _metaDescription) {
        this.metaDescription = _metaDescription;
    }

    /**
     * <p>
     * Returns the metaCopyright.
     * </p>
     * @return <code>String</code> - The metaCopyright.
     */
    public String getMetaCopyright() {
        return this.metaCopyright;
    }

    /**
     * <p>
     * Changes the metaCopyright.
     * </p>
     * @param _metaCopyright The new metaCopyright.
     */
    public void setMetaCopyright(String _metaCopyright) {
        this.metaCopyright = _metaCopyright;
    }

    /**
     * <p>
     * Returns the metaProviderName.
     * </p>
     * @return <code>String</code> - The metaProviderName.
     */
    public String getMetaProviderName() {
        return this.metaProviderName;
    }

    /**
     * <p>
     * Changes the metaProviderName.
     * </p>
     * @param _metaProviderName The new metaProviderName.
     */
    public void setMetaProviderName(String _metaProviderName) {
        this.metaProviderName = _metaProviderName;
    }

    /**
     * <p>
     * Returns the metaProviderAddress.
     * </p>
     * @return <code>String</code> - The metaProviderAddress.
     */
    public String getMetaProviderAddress() {
        return this.metaProviderAddress;
    }

    /**
     * <p>
     * Changes the metaProviderAddress.
     * </p>
     * @param _metaProviderAddress The new metaProviderAddress.
     */
    public void setMetaProviderAddress(String _metaProviderAddress) {
        this.metaProviderAddress = _metaProviderAddress;
    }

    /**
     * <p>
     * Returns the metaProviderPhone.
     * </p>
     * @return <code>String</code> - The metaProviderPhone.
     */
    public String getMetaProviderPhone() {
        return this.metaProviderPhone;
    }

    /**
     * <p>
     * Changes the metaProviderPhone.
     * </p>
     * @param _metaProviderPhone The new metaProviderPhone.
     */
    public void setMetaProviderPhone(String _metaProviderPhone) {
        this.metaProviderPhone = _metaProviderPhone;
    }

    /**
     * <p>
     * Returns the metaProviderWeb.
     * </p>
     * @return <code>String</code> - The metaProviderWeb.
     */
    public String getMetaProviderWeb() {
        return this.metaProviderWeb;
    }

    /**
     * <p>
     * Changes the metaProviderWeb.
     * </p>
     * @param _metaProviderWeb The new metaProviderWeb.
     */
    public void setMetaProviderWeb(String _metaProviderWeb) {
        this.metaProviderWeb = _metaProviderWeb;
    }

    /**
     * <p>
     * Returns the metaProviderSupport.
     * </p>
     * @return <code>String</code> - The metaProviderSupport.
     */
    public String getMetaProviderSupport() {
        return this.metaProviderSupport;
    }

    /**
     * <p>
     * Changes the metaProviderSupport.
     * </p>
     * @param _metaProviderSupport The new metaProviderSupport.
     */
    public void setMetaProviderSupport(String _metaProviderSupport) {
        this.metaProviderSupport = _metaProviderSupport;
    }

    /**
     * <p>
     * Returns the moduleClassName.
     * </p>
     * @return <code>String</code> - The moduleClassName.
     */
    public String getModuleClassName() {
        return this.moduleClassName;
    }

    /**
     * <p>
     * Changes the moduleClassName.
     * </p>
     * @param _moduleClassName The new moduleClassName.
     */
    public void setModuleClassName(String _moduleClassName) {
        this.moduleClassName = _moduleClassName;
    }

    /**
     * <p>
     * Returns the configurationClassName.
     * </p>
     * @return <code>String</code> - The configurationClassName.
     */
    public String getConfigurationClassName() {
        return this.configurationClassName;
    }

    /**
     * <p>
     * Changes the configurationClassName.
     * </p>
     * @param _configurationClassName The new configurationClassName.
     */
    public void setConfigurationClassName(String _configurationClassName) {
        this.configurationClassName = _configurationClassName;
    }

    /**
     * <p>
     * Returns the statisticsClassName.
     * </p>
     * @return <code>String</code> - The statisticsClassName.
     */
    public String getStatisticsClassName() {
        return this.statisticsClassName;
    }

    /**
     * <p>
     * Changes the statisticsClassName.
     * </p>
     * @param _statisticsClassName The new statisticsClassName.
     */
    public void setStatisticsClassName(String _statisticsClassName) {
        this.statisticsClassName = _statisticsClassName;
    }

    /**
     * <p>
     * Returns the dependencies.
     * </p>
     * @return <code>Set<ModuleDependency></code> - The dependencies.
     */
    public Set<ModuleDependency> getDependencies() {
        return this.dependencies;
    }

    /**
     * <p>
     * Changes the dependencies.
     * </p>
     * @param _dependencies The new dependencies.
     */
    public void setDependencies(Set<ModuleDependency> _dependencies) {
        this.dependencies = _dependencies;
    }

    /**
     * <p>
     * Adds a dependency to the dependency list.
     * </p>
     * @param _depend The dependendy to add.
     */
    public void addDependency(ModuleDependency _depend) {
        this.dependencies.add(_depend);
    }

    /**
     * <p>
     * Returns a hash code value for the object. This method is supported for the benefit of hashtables such as those provided by java.util.Hashtable. It only uses short and long form (as the equals method).
     * </p>
     * @return <code>int</code> - A hash code value for this object.
     */
    @Override
    public final int hashCode() {
        return HashCodeUtilities.combineHashCode(new int[] { this.getFrameworkVersion().hashCode(), this.getId().hashCode(), this.getVersion().hashCode(), this.getMetaName().hashCode(), this.getMetaCopyright().hashCode(), this.getMetaDescription().hashCode(), this.getMetaProviderName().hashCode(), this.getMetaProviderAddress().hashCode(), this.getMetaProviderPhone().hashCode(), this.getMetaProviderWeb().hashCode(), this.getMetaProviderSupport().hashCode(), this.getModuleClassName().hashCode(), this.getConfigurationClassName().hashCode(), this.getStatisticsClassName().hashCode(), this.getDependencies().hashCode() });
    }

    /**
     * <p>
     * Indicates whether some other object is "equal to" this one.
     * </p>
     * @param _obj The reference object with which to compare.
     * @return <code>boolean</code> - True if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public final boolean equals(Object _obj) {
        if (_obj == null) {
            return false;
        }
        if (!(_obj instanceof ModuleDefinition)) {
            return false;
        }
        ModuleDefinition def = (ModuleDefinition) _obj;
        if (!(def.getFrameworkVersion().equals(this.getFrameworkVersion()))) {
            return false;
        }
        if (!(def.getId().equals(this.getId()))) {
            return false;
        }
        if (!(def.getVersion().equals(this.getVersion()))) {
            return false;
        }
        if (!(def.getMetaName().equals(this.getMetaName()))) {
            return false;
        }
        if (!(def.getMetaCopyright().equals(this.getMetaCopyright()))) {
            return false;
        }
        if (!(def.getMetaDescription().equals(this.getMetaDescription()))) {
            return false;
        }
        if (!(def.getMetaProviderName().equals(this.getMetaProviderName()))) {
            return false;
        }
        if (!(def.getMetaProviderAddress().equals(this.getMetaProviderAddress()))) {
            return false;
        }
        if (!(def.getMetaProviderPhone().equals(this.getMetaProviderPhone()))) {
            return false;
        }
        if (!(def.getMetaProviderWeb().equals(this.getMetaProviderWeb()))) {
            return false;
        }
        if (!(def.getMetaProviderSupport().equals(this.getMetaProviderSupport()))) {
            return false;
        }
        if (!(def.getModuleClassName().equals(this.getModuleClassName()))) {
            return false;
        }
        if (!(def.getConfigurationClassName().equals(this.getConfigurationClassName()))) {
            return false;
        }
        if (!(def.getStatisticsClassName().equals(this.getStatisticsClassName()))) {
            return false;
        }
        if (!(def.getDependencies().equals(this.getDependencies()))) {
            return false;
        }
        return true;
    }

    /**
     * <p>
     * Returns a string representation of this object.
     * </p>
     * @return <code>String</code> - A string representation of this object.
     */
    @Override
    public final String toString() {
        return "ModuleDefinition[frameworkVersion=" + this.getFrameworkVersion() + ",id=" + this.getId() + ",version=" + this.getVersion() + ",name=" + this.getMetaName() + ",copyright=" + this.getMetaCopyright() + ",description=" + this.getMetaDescription() + ",provider-name=" + this.getMetaProviderName() + ",provider-address=" + this.getMetaProviderAddress() + ",provider-phone=" + this.getMetaProviderPhone() + ",provider-web=" + this.getMetaProviderWeb() + ",provider-support=" + this.getMetaProviderSupport() + ",module-class-name=" + this.getModuleClassName() + ",configuration-class-name=" + this.getConfigurationClassName() + ",statistics-class-name=" + this.getStatisticsClassName() + ",dependencies=" + this.getDependencies() + "]";
    }
}
