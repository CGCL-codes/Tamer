package net.frontlinesms.ui.handler.contacts;

import net.frontlinesms.data.domain.Contact;

/**
 * Implemented by classes which spawn {@link ContactEditor}s so they can be notified when editing is
 * completed.
 * @author Alex alex@frontlinesms.com
 */
public interface ContactEditorOwner {

    /** @param contact the contact which has been created */
    void contactCreationComplete(Contact contact);

    /** @param contact the contact which has been edited */
    void contactEditingComplete(Contact contact);
}
