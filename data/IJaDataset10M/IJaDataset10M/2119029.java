package org.apache.jetspeed.om.security;

/**
 * A Jetspeed basic Group.
 *
 * @author <a href="mailto:taylor@apache.org">David Sean Taylor</a>
 * @version $Id: Group.java,v 1.3 2004/02/23 03:14:12 jford Exp $
 */
public interface Group {

    /**
     * Get the name of the Group
     *
     * @return the name of the group.
     */
    public String getName();

    /**
     * Set the name of the Group
     *
     * @param groupName the name of the Group.
     */
    public void setName(String groupName);

    /**
     * Get the id of the Group
     *
     * @return the id of the group.
     */
    public String getId();

    /**
     * Set the id of the Group
     *
     * @param id the new id for the group
     */
    public void setId(String id);
}
