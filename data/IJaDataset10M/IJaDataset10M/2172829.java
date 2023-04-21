package lablog.lib.db.entity;

import static lablog.lib.util.TextType.STRING_NAME;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import lablog.lib.db.entity.base.EntityMetadata;
import lablog.lib.db.entity.base.RestrictedEntity;
import lablog.lib.util.TextType;

/**
 * Groups {@link ResourceConfig}s of {@link Resource}s and links to {@link Result}s. Allows to store
 * configurations of Resources.
 * 
 * @assoc 1 - n ResourceConfig
 * @assoc n - m Result
 * @assoc n - 1 ResourceGroup
 *
 */
@Entity
@Table(name = "configuration_group", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "resource_group_id" }) })
@EntityMetadata(name = "Configuration Group")
public class ConfigurationGroup extends RestrictedEntity {

    public static final short CLASSID = 39;

    public static final long serialVersionUID = createSerialUID(DB_VERSION, MODULE, CLASSID);

    private String name;

    private String description;

    private ResourceGroup resourceGroup;

    private Set<Result> results;

    private Set<ResourceConfig<?>> resourceConfigs;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    @Column(name = "name", nullable = false, length = 64)
    @EntityMetadata(name = "Name", order = 1, title = true, texttype = STRING_NAME)
    public String getName() {
        return name;
    }

    public void setResourceGroup(ResourceGroup resourceGroup) {
        this.resourceGroup = resourceGroup;
    }

    @ManyToOne(targetEntity = ResourceGroup.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_group_id", nullable = false)
    @EntityMetadata(name = "Resource group", order = 2, display = true)
    public ResourceGroup getResourceGroup() {
        return resourceGroup;
    }

    public void setResourceConfigs(Set<ResourceConfig<?>> resourceConfigs) {
        this.resourceConfigs = resourceConfigs;
    }

    @OneToMany(targetEntity = ResourceConfig.class, fetch = FetchType.LAZY, mappedBy = "configGroup", cascade = CascadeType.REMOVE)
    @EntityMetadata(name = "Resource configurations", order = 99)
    @OrderBy("name ASC")
    public Set<ResourceConfig<?>> getResourceConfigs() {
        return resourceConfigs;
    }

    public void setResults(Set<Result> results) {
        this.results = results;
    }

    @ManyToMany(targetEntity = Result.class, fetch = FetchType.LAZY)
    @JoinTable(name = "result_config_group", joinColumns = { @JoinColumn(name = "config_group_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "result_id", nullable = false) }, uniqueConstraints = { @UniqueConstraint(columnNames = { "result_id", "config_group_id" }) })
    @OrderBy("type, id ASC")
    @EntityMetadata(name = "results", order = 99)
    public Set<Result> getResults() {
        return this.results;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "description", nullable = true)
    @EntityMetadata(name = "Description", order = 99, title = false, texttype = TextType.TEXT_MEDIUM)
    public String getDescription() {
        return description;
    }

    /**
    * Add a ResourceConfiguration to this {@link ConfigurationGroup}
    * 
    * @param c {@link ResourceConfig}.
    * 
    */
    @Transient
    public void addResourceConfig(ResourceConfig<?> c) {
        if (this.getResourceConfigs() != null) this.getResourceConfigs().add(c); else {
            resourceConfigs = new TreeSet<ResourceConfig<?>>();
            resourceConfigs.add(c);
        }
    }

    /**
    * Remove a {@link ResourceConfig} from this {@link ConfigurationGroup}
    * 
    * @param c {@link ResourceConfig}.
    */
    @Transient
    public void removeResourceConfig(ResourceConfig<?> c) {
        Set<ResourceConfig<?>> set = this.getResourceConfigs();
        if (set.contains(c)) set.remove(c);
        this.setResourceConfigs(set);
    }

    /**
    * Add a Result to this {@link ConfigurationGroup}
    *
    * @param result {@link Result}, the new result.
    * 
    */
    @Transient
    public void addResult(Result result) {
        if (this.getResults() != null) this.getResults().add(result); else {
            results = new TreeSet<Result>();
            results.add(result);
        }
    }

    /**
    * Remove a Result from this {@link ConfigurationGroup}
    * 
    * @param result {@link Result}, the new result.
    */
    @Transient
    public void removeResult(Result r) {
        Set<Result> set = this.getResults();
        if (set.contains(r)) set.remove(r);
        this.setResults(set);
    }

    @Override
    public String toString() {
        return name;
    }
}
