package net.sourceforge.xml1wire;

import java.util.Vector;

/** A cluster is described a group of devices.
 */
public class OWCluster extends Vector {

    /**
   * Get the description of this cluster.
   *
   * @return Its description.
   */
    public String getDescription() {
        return Description;
    }

    /**
   * Set the description of this cluster.
   *
   * @param description The new description.
   */
    public void setDescription(String description) {
        this.Description = description;
    }

    /**
   * sets Descrition of cluster containing this cluster
   */
    public void setContainerCluster(String clusterName) {
        this.Containingcluster = clusterName;
    }

    /**
    * Retrieves name of cluster containing this cluster
    */
    public String getContainerCluster() {
        return this.Containingcluster;
    }

    /** Description of this cluster. */
    private String Description;

    private String Containingcluster = null;
}
