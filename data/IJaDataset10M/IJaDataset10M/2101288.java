package org.jmove.core;

/** Instances of classes which implement this interface can be used as
 * filters within different search and traversion methods which the framework
 * provides. <br/>
 * It defines a single method {@link #accept} which decides whether the link
 * can be used within the traversion or not.
 *
 * @author Axel Terfloth
 */
public interface LinkFilter {

    /** This method will be called to decide whether a link should be used
	 * within a traversion or not. The decision may depend on the properties
	 * of the link itself and the {@link Linkable} from which the link will
	 * be traversed or any other model element which is accessible from here. The Linkable which is passed as a parameter defines the
     * direction of the traversion.
	 *
	 * @param linkable The object from which is linked by the link.
	 * @param link The link object.
	 */
    boolean accept(Linkable linkable, Link link);
}
