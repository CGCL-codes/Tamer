package org.apache.wicket.spring.common.web;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.common.Contact;
import org.apache.wicket.spring.common.ContactDao;

/**
 * Base class for contact detachable models. This class implements all necessary logic except
 * retrieval of the dao object, this way we can isolate that logic in our example implementations.
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public abstract class ContactDetachableModel extends LoadableDetachableModel {

    private long id;

    /**
	 * @param contact
	 */
    public ContactDetachableModel(Contact contact) {
        super(contact);
        this.id = contact.getId();
    }

    protected abstract ContactDao getContactDao();

    @Override
    protected Contact load() {
        return getContactDao().get(id);
    }
}
