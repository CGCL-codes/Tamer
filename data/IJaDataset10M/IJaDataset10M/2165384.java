package net.sourceforge.jepesi.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Cluster {

    private int id = -1;

    private String name;

    private HashSet<Host> hosts = new HashSet<Host>();

    private int status = 0;

    /**
	 * @return the id
	 */
    public int getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the hosts
	 */
    public Set<Host> getHosts() {
        return Collections.unmodifiableSet(hosts);
    }

    /**
	 * @param host the hosts to add
	 */
    public void addHost(Host host) {
        if (host != null) this.hosts.add(host);
    }

    public boolean equals(Object obj) {
        Cluster cluster = ((Cluster) obj);
        return (this.getId() == cluster.getId() && this.getName().equals(cluster.getName()) && this.getHosts().equals(cluster.getHosts()));
    }

    /**
	 * @return the cluster as XML string
	 */
    public String toXml() {
        String xml;
        xml = "\t<cluster>\n\t\t<id>" + getId() + "</id>\n\t\t<name>" + getName() + "</name>\n\t\t<hosts>";
        for (Host host : hosts) {
            xml += "\n\t\t\t<host>" + host.getId() + "</host>";
        }
        xml += "\n\t\t</hosts>\n\t</cluster>";
        return xml;
    }

    public String toString() {
        return this.getName();
    }

    /**
	 * @param status the status to set
	 */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
	 * @return the status
	 */
    public int getStatus() {
        return status;
    }
}
