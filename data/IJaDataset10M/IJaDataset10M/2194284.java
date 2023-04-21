package br.unb.bioagents.drools.lang;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Cascade;
import br.unb.bioagents.agents.AgentGroup;

/**
 * @author hugowschneider
 * 
 */
@Entity
public class PackageGroupDescriptor implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6842781257650677844L;

    private boolean active;

    private AgentGroup agentGroup;

    private long id;

    private List<PackageDescriptor> packages;

    /**
	 * @param packageDescriptor
	 */
    public void addPackageDescriptor(PackageDescriptor packageDescriptor) {
        getPackages().add(packageDescriptor);
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public AgentGroup getAgentGroup() {
        return agentGroup;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public List<PackageDescriptor> getPackages() {
        if (packages == null) {
            setPackages(new Vector<PackageDescriptor>());
        }
        return packages;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setAgentGroup(AgentGroup agentGroup) {
        this.agentGroup = agentGroup;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPackages(List<PackageDescriptor> packages) {
        this.packages = packages;
    }
}
