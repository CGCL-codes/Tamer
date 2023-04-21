package org.unicore.resources;

/**
 * Resource for main Memory. 
 * <p>
 * The units are "Megabytes per Node".
 *
 * @author D. Snelling (fecit)
 * @author S. van den Berghe (fecit)
 *
 * @since AJO 3
 *
 * @version $Id: Memory.java,v 1.2 2004/05/26 16:31:44 svenvdb Exp $
 *
 **/
public final class Memory extends CapacityResource {

    static final long serialVersionUID = -5848591242096238291L;

    public Memory() {
        this("", 0.0, 0.0, 0.0);
    }

    /**
     * Create a new Memory.
     * <p>
     * Sets the units to "Megabytes per node"
     *
     * @param description A description of the Memory
     * @param maxrequest Maximum amount of resource allowed
     * @param defaultrequest Amount of resource granted if no specific request received
     * @param minrequest Minimum amount of resource allowed
     *
     **/
    public Memory(String description, double maxrequest, double defaultrequest, double minrequest) {
        super(description, maxrequest, defaultrequest, minrequest);
        this.units = "Megabytes per node";
    }

    public String toString() {
        return "Memory";
    }
}
