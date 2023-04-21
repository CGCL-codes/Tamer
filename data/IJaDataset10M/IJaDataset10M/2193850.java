package com.aurecon.kwb.ldap;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zul.ListModelMap;
import mgr.ListModelMapSharer;

/**
 * LdapRole is a wrapper for a client role in the LDAP database.
 * 
 * @author williamsrc
 *
 */
public class LdapRole {

    /**
	 * Role user list.
	 */
    private LdapListModelMap roleUsers = null;

    /**
	 * ListModelSharer for this groups ListModel.
	 */
    private ListModelMapSharer roleListSharer;

    /**
	 * Current instance of LdapController that is running on the server.
	 */
    private LdapController _controller = null;

    /**
	 * Organisational Unit.
	 */
    private String ou;

    /**
	 * Common Name.
	 */
    public String cn;

    /**
	 * Description.
	 */
    public String description;

    /**
	 * Sole-Constructor.
	 * 
	 * @param controller	{@link LdapController} current controller
	 * @param orgUnit		{@link String} organisational unit
	 * @param commonName	{@link String} common name
	 * @param desc			{@link String} description
	 * @param userList		{@link LdapListModelMap} map of the current users for this role provided to constructor (may be blank map for new role)
	 */
    LdapRole(final LdapController controller, final String orgUnit, final String commonName, final String desc, final LdapListModelMap userList) {
        roleUsers = userList;
        roleListSharer = new ListModelMapSharer(roleUsers);
        _controller = controller;
        ou = orgUnit;
        cn = commonName;
        description = desc;
    }

    /**
	 * Add a member to this role.
	 * 
	 * @param distName	{@link String} distinguished name of the user
	 */
    public final synchronized void addMember(final String distName) {
        LdapUser user = _controller.addMemberToRole(ou, distName);
        roleUsers.put(distName, user);
    }

    /**
	 * Remove a member from this role (method may be deprecated later).
	 * 
	 * @param distName	{@link String} distinguished name of the user
	 */
    public final synchronized void removeMember(final String distName) {
        _controller.removeMemberFromRole(ou, distName);
        roleUsers.remove(distName);
    }

    /**
	 * Returns whether or not this role contains a member based on the specified distinguished name.
	 * 
	 * @param distName	{@link String} distinguished name of the user to test for
	 * @return			{@link Boolean} true if the user is a member of this role
	 */
    public final synchronized boolean containsMember(final String distName) {
        return roleUsers.containsKey(distName);
    }

    /**
	 * Returns a ListModel of the users for this role on the system.
	 * 
	 * @param desktop	{@link Desktop} desktop of the user
	 * @return			{@link ListModelMap} returned
	 */
    public final synchronized ListModelMap getRoleListModel(final Desktop desktop) {
        return (ListModelMap) roleListSharer.getProxy(desktop);
    }

    /**
	 * Returns the Organisational Unit for this role.
	 * @return	{@link String} organisational unit
	 */
    public final synchronized String getOU() {
        return ou;
    }
}
