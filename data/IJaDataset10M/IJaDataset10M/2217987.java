package org.matsim.interfaces.basic.v01.network;

import java.util.Map;
import org.matsim.interfaces.basic.v01.BasicLocation;
import org.matsim.interfaces.basic.v01.Id;

/**
 * A topological representation of an network node.
 */
public interface BasicNode extends BasicLocation {

    /**
     * Adds a non-<code>null</code> link to this node's set of ingoing links.
     *
     * @param link
     *            the <code>BasicLinkI</code> to be added
     *
     * @return <code>true</code>> if <code>link</code> has been added and
     *         <code>false</code> otherwise
     *
     * @throws IllegalArgumentException
     *             if <code>link</code> is <code>null</code>
     */
    public boolean addInLink(BasicLink link);

    /**
     * Adds a non-<code>null</code> link to this node's set of outgoing
     * links.
     *
     * @param link
     *            the <code>BasicLinkI</code> to be added
     *
     * @return <code>true</code> if <code>link</code> has been added and
     *         <code>false</code> otherwise
     *
     * @throws IllegalArgumentException
     *             if <code>link</code> is <code>null</code>
     */
    public boolean addOutLink(BasicLink link);

    /**
     * Returns this node's set of ingoing links. This set might be empty, but it
     * must not be <code>null</code>.
     *
     * @return this node's ingoing links
     */
    public Map<Id, ? extends BasicLink> getInLinks();

    /**
     * Returns this node's set of outgoing links. This set might be empty, but
     * it must not be <code>null</code>.
     *
     * @return this node's outgoing links
     */
    public Map<Id, ? extends BasicLink> getOutLinks();
}
