package net.sf.fallfair.committee;

import net.sf.fallfair.contact.Contactable;
import net.sf.fallfair.CRUD.Persistent;
import net.sf.fallfair.contact.Contact;

public interface Committee extends Persistent, Contactable {

    @Override
    Contact getContact();

    void setContact(Contact contact);
}
