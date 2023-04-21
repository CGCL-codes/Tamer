package com.aurecon.kwb.ui.view;

import java.util.Comparator;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import com.aurecon.kwb.ldap.LdapUser;
import com.aurecon.kwb.ui.view.ViewStatusBar.ActiveUserListbox.UserFirstNameComparator;
import com.aurecon.kwb.ui.view.ViewStatusBar.ActiveUserListbox.UserLastNameComparator;
import com.aurecon.kwb.ui.view.ViewStatusBar.ActiveUserListbox.UserListItemRenderer;
import com.aurecon.kwb.ui.view.ViewStatusBar.ActiveUserListbox.UserRoleComparator;
import data.ListModelListSharer.ProxyModel;

final class ActiveUserListbox extends Listbox {

    private static final long serialVersionUID = 1L;

    private UserFirstNameComparator ascendingFirstNameComparator;

    private UserFirstNameComparator descendingFirstNameComparator;

    private UserLastNameComparator ascendingLastNameComparator;

    private UserLastNameComparator descendingLastNameComparator;

    private UserRoleComparator ascendingRoleComparator;

    private UserRoleComparator descendingRoleComparator;

    private ListModel listModel;

    /**
		 * @param bar TODO
		 * 
		 */
    protected ActiveUserListbox() {
        this.setHeight("214px");
        Listhead head = new Listhead();
        head.setParent(this);
        Listheader header1 = new Listheader("First Name");
        header1.setParent(head);
        Listheader header2 = new Listheader("Last Name");
        header2.setParent(head);
        Listheader header3 = new Listheader("Permissions");
        header3.setParent(head);
        header1.setWidth("28%");
        header2.setWidth("39%");
        header3.setWidth("22%");
        header1.setStyle("padding: 2px;");
        header2.setStyle("padding: 2px;");
        header3.setStyle("padding: 2px;");
        ascendingFirstNameComparator = new UserFirstNameComparator(true);
        descendingFirstNameComparator = new UserFirstNameComparator(false);
        ascendingLastNameComparator = new UserLastNameComparator(true);
        descendingLastNameComparator = new UserLastNameComparator(false);
        ascendingRoleComparator = new UserRoleComparator(true);
        descendingRoleComparator = new UserRoleComparator(false);
        header1.setSortAscending(ascendingFirstNameComparator);
        header1.setSortDescending(descendingFirstNameComparator);
        header2.setSortAscending(ascendingLastNameComparator);
        header2.setSortDescending(descendingLastNameComparator);
        header3.setSortAscending(ascendingRoleComparator);
        header3.setSortDescending(descendingRoleComparator);
        UserListItemRenderer listItemRenderer = new UserListItemRenderer();
        Desktop desktop = getUsersDesktop();
        listModel = workspaceviewer.getUserListModel(desktop);
        this.setItemRenderer(listItemRenderer);
        ((ProxyModel) listModel).sort(ascendingFirstNameComparator, true);
        header1.setSortDirection("ascending");
    }

    /**
		 * Add the model when the component is in the desktop
		 *
		 */
    public void addModel() {
        this.setModel(listModel);
    }

    /**
		 * User List item renderer
		 * 
		 * @author williamsrc
		 *
		 */
    private class UserListItemRenderer implements ListitemRenderer {

        private static final String admin = "/img/silk/user_suit.png";

        private static final String write = "/img/silk/user_edit.png";

        private static final String readonly = "/img/silk/user.png";

        @SuppressWarnings("unchecked")
        public void render(Listitem item, Object entry) {
            LdapUser user = (LdapUser) entry;
            Listcell givenName = new Listcell(user.getGivenName());
            Listcell familyName = new Listcell(user.getSurname());
            String permissions = user.getRole().toUpperCase();
            if (!permissions.equals("ADMIN")) {
            }
            givenName.setImage(this.getIcon(permissions));
            Listcell role = new Listcell(permissions);
            item.setAttribute("DN", user.getDN());
            givenName.setParent(item);
            familyName.setParent(item);
            role.setParent(item);
            givenName.setStyle("border-bottom: 1px solid #DDD; border-left: 1px solid white; border-right: 1px solid #CCC; border-top: none;");
            familyName.setStyle("border-bottom: 1px solid #DDD; border-left: 1px solid white; border-right: 1px solid #CCC; border-top: none;");
            role.setStyle("border-bottom: 1px solid #DDD; border-left: 1px solid white; border-right: 1px solid #CCC; border-top: none;");
        }

        private String getIcon(String role) {
            String result;
            if (role.equals("WRITE")) {
                result = write;
            } else if (role.equals("ADMIN")) {
                result = admin;
            } else {
                result = readonly;
            }
            return result;
        }
    }

    /**
		 * First name comparator for users
		 * 
		 * @author morey_surfer
		 *
		 */
    private class UserFirstNameComparator implements Comparator<LdapUser> {

        private boolean _ascending;

        /**
			 * Sole-Constructor
			 * 
			 * @param ascending	{@link Boolean} true if comparator is for ascending sorts (else it is for descending sorts)
			 */
        public UserFirstNameComparator(boolean ascending) {
            _ascending = ascending;
        }

        public int compare(LdapUser lu1, LdapUser lu2) {
            String comp1;
            String comp2;
            comp1 = lu1.getGivenName();
            comp2 = lu2.getGivenName();
            int compare = comp1.compareTo(comp2);
            if (!_ascending) {
                compare = -compare;
            }
            return compare;
        }
    }

    /**
		 * Last name comparator for users
		 * 
		 * @author morey_surfer
		 *
		 */
    private class UserLastNameComparator implements Comparator<LdapUser> {

        private boolean _ascending;

        /**
			 * Sole-Constructor
			 * 
			 * @param ascending	{@link Boolean} true if comparator is for ascending sorts (else it is for descending sorts)
			 */
        public UserLastNameComparator(boolean ascending) {
            _ascending = ascending;
        }

        public int compare(LdapUser lu1, LdapUser lu2) {
            String comp1;
            String comp2;
            comp1 = lu1.getSurname();
            comp2 = lu2.getSurname();
            int compare = comp1.compareTo(comp2);
            if (!_ascending) {
                compare = -compare;
            }
            return compare;
        }
    }

    /**
		 * Role comparator for users
		 * 
		 * @author morey_surfer
		 *
		 */
    private class UserRoleComparator implements Comparator<LdapUser> {

        private boolean _ascending;

        /**
			 * Sole-Constructor
			 * 
			 * @param ascending	{@link Boolean} true if comparator is for ascending sorts (else it is for descending sorts)
			 */
        public UserRoleComparator(boolean ascending) {
            _ascending = ascending;
        }

        public int compare(LdapUser lu1, LdapUser lu2) {
            String comp1;
            String comp2;
            comp1 = lu1.getRole();
            comp2 = lu2.getRole();
            int compare = 0;
            if (comp1.equals(comp2)) {
                compare = lu1.getGivenName().compareTo(lu2.getGivenName());
            } else {
                if (comp1.equals("admin") || comp2.equals("disabled")) {
                    compare = -1;
                } else if (comp1.equals("disabled") || comp2.equals("admin")) {
                    compare = 1;
                }
            }
            if (!_ascending) {
                compare = -compare;
            }
            return compare;
        }
    }
}
