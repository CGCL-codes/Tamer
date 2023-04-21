package wicketrocks.model;

import org.apache.wicket.model.LoadableDetachableModel;
import entity.contact.Contact;
import entity.contact.data.ContactsData;

/**
 * detachable model for an instance of contact
 * 
 * @author manuelbarzi
 * @version 20111201180314 
 */
public class DetachableContactModel extends LoadableDetachableModel<Contact> {

    private final long id;

    protected ContactsData getContactsDB() {
        return ContactsData.getInstance();
    }

    /**
	 * @param c
	 */
    public DetachableContactModel(Contact c) {
        this((Long) c.getId());
    }

    /**
	 * @param id
	 */
    public DetachableContactModel(Long id) {
        if (id == 0) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }

    /**
	 * used for dataview with ReuseIfModelsEqualStrategy item reuse strategy
	 * 
	 * @see org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (obj instanceof DetachableContactModel) {
            DetachableContactModel other = (DetachableContactModel) obj;
            return other.id == id;
        }
        return false;
    }

    /**
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
    protected Contact load() {
        return getContactsDB().get(id);
    }
}
