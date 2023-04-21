package org.unicore.resources;

import org.unicore.sets.ResourceSet;

/**
 * Describes a host within a Vsite.
 * <p>
 * The presence of a Host resource in a Resource Description
 * indicates that it is possible for 
 * clients to request that jobs be run on that host. The resources
 * in the set obtained by {@link org.unicore.resources.Host#getResources}
 * are the subset of the Vsite's overall resources that are available on the named Host.
 * <p>
 * The presence of a Host resource in a Resource Request 
 * indicates that the client wishes to run the job on that host.
 * <p>
 * The absence of a Host resource in a Resource Description indicates that the Vsite has
 * no distinct hosts or that the Vsite will select a host based on other
 * resource data. Similarly the absence of a Host
 * in a Resource Request indicates that the client has no preference and
 * that the NJS may select a host based on the other resources
 * in the request.
 *
 * @deprecated V4 NJSs will return a single level of resources for a Vsite so this class
 *             should never be required.
 *
 * @author D. Snelling (fecit)
 * @author S. van den Berghe (fecit)
 *
 * @since AJO 3
 *
 * @version $Id: Host.java,v 1.2 2004/05/26 16:31:43 svenvdb Exp $
 *
 **/
public final class Host extends CapabilityResource {

    static final long serialVersionUID = -2685193224784792950L;

    /**
     * Create a new Host.
     *
     * @param description  A description of the host
     * @param host_name The name of the host
     *
     **/
    public Host(String description, String host_name) {
        super(description);
        setName(host_name);
    }

    /**
     * Create a new Host
     *
     **/
    public Host() {
        this("undescribed host", "");
    }

    private String host_name = "";

    /**
     * Get the host name.
     * <p>
     * The string that identifies the the exact host a user wishes
     * the task to run on within the Vsite.
     *
     **/
    public String getName() {
        return host_name;
    }

    /**
     * Set the host name.
     *
     * host_name must be non-null and non-empty.
     *
     **/
    public void setName(String host_name) {
        if (host_name == null) {
            this.host_name = "";
        } else {
            this.host_name = host_name;
        }
    }

    private ResourceSet resources;

    /**
     * Get the resources offered by this Host.
     * <p>
     * This is used only in Resource Descriptions, it is 
     * expected to be null in Resource Requests (a Task can
     * execute on one Host only, a Host instance will name
     * the host, the requested resources will be in the same set
     * as the Host instance).
     *
     **/
    public ResourceSet getResources() {
        if (resources == null) resources = new ResourceSet();
        return resources;
    }

    /**
     * Set the resources offered by this Host.
     *
     **/
    public void setResources(ResourceSet resources) {
        this.resources = resources;
    }

    /**
     * Hosts are equal if their names are the same. 
     * <p>
     * <em> Equality testing ignores the resources. </em>
     *
     **/
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass() != getClass()) return false;
        return getName().equals(((Host) o).getName());
    }

    /**
     * Return a hash code for this Host. 
     * <p>
     * Returns the hash code
     * of the name (since the name determines equality).
     *
     **/
    public int hashCode() {
        return getName().hashCode();
    }

    public String toString() {
        return getName();
    }
}
