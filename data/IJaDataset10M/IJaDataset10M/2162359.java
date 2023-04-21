package org.tapestrycomponents.tassel.components;

import org.apache.tapestry.BaseComponent;
import org.tapestrycomponents.tassel.Visit;
import org.tapestrycomponents.tassel.domain.User;

/**
 * @author robertz
 *TasselNavigation.java
 *Tassel
 */
public abstract class TasselNavigation extends BaseComponent {

    public abstract String getCurrentPage();

    public boolean getCanViewAdminLink() {
        Visit v = (Visit) getPage().getEngine().getVisit();
        if (v == null) {
            return false;
        }
        if (v.getUser() == null) {
            return false;
        }
        User u = v.getUser();
        return u.isAdmin();
    }

    public String getLinkMessage() {
        if (getCurrentPage() == null) {
            return "boo!";
        } else {
            return getMessage(getCurrentPage());
        }
    }

    public boolean getAuthed() {
        if (this.getPage().getEngine().getVisit() == null) {
            return false;
        }
        Visit v = (Visit) this.getPage().getEngine().getVisit();
        if (v.getUser() == null) {
            return false;
        }
        return v.getUser().isAuthenticated();
    }
}
