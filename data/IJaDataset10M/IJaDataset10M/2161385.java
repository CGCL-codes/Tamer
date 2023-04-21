package org.syrup;

/**
 * The surrounding Context of a PTask. All inputs and outputs linked to a single
 * PTask can be accessed via this Context.
 * 
 * @author Robbert van Dalen
 */
public interface Context {

    static final String COPYRIGHT = "Copyright 2005 Robbert van Dalen." + "At your option, you may copy, distribute, or make derivative works under " + "the terms of The Artistic License. This License may be found at " + "http://www.opensource.org/licenses/artistic-license.php. " + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

    /**
     * The PTask this Context represents.
     * 
     * @return The PTask.
     */
    public PTask task();

    /**
     * The first input Link of this Context's PTask.
     * 
     * @return The first input Link.
     */
    public Link in_1_link();

    /**
     * The second input Link of this Context's PTask.
     * 
     * @return The second input Link.
     */
    public Link in_2_link();

    /**
     * The first output Link of this Context's PTask.
     * 
     * @return The first output Link.
     */
    public Link out_1_link();

    /**
     * The second output Link of this Context's PTask.
     * 
     * @return The second output Link.
     */
    public Link out_2_link();
}
