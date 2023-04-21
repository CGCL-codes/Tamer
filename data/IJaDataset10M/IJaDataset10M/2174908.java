package net.sf.sail.webapp.domain;

import java.util.Set;
import net.sf.sail.webapp.domain.group.Group;
import net.sf.sail.webapp.domain.sds.SdsWorkgroup;

/**
 * Workgroup is an aggregation of users that work on the same offering. It is
 * made up of one or more users.
 * 
 * @author Hiroki Terashima
 * @version $Id: User.java 231 2007-03-26 07:03:00Z hiroki $
 */
public interface Workgroup extends Persistable {

    /**
     * Sets the SdsWorkgroup object.
     * 
     * @param sdsWorkgroup
     *            the sdsWorkgroup to set
     */
    public void setSdsWorkgroup(SdsWorkgroup sdsWorkgroup);

    /**
     * Gets the SdsWorkgroup object.
     * 
     * @return SdsWorkgroup
     */
    public SdsWorkgroup getSdsWorkgroup();

    /**
     * @return the members
     */
    public Set<User> getMembers();

    /**
     * @param members
     *            the members to set
     */
    public void setMembers(Set<User> members);

    /**
     * @param member
     *            the member to add
     */
    public void addMember(User member);

    /**
     * @param member
     *            the member to remove
     */
    public void removeMember(User member);

    /**
     * @return the offering
     */
    public Offering getOffering();

    /**
     * @param offering
     *            the offering to set
     */
    public void setOffering(Offering offering);

    /**
     * @return the group
     */
    public Group getGroup();

    /**
     * @param group
     *           the group to set
     */
    public void setGroup(Group group);

    /**
	 * @return the id
	 */
    public Long getId();

    /**
	 * Generates a name for this workgroup. This name may or may not be the same as
	 * the value in this.sdsWorkgroup.name.
	 * 
	 * @return <code>String</code> a name for this workgroup
	 */
    public String generateWorkgroupName();
}
