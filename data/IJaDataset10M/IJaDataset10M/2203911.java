package org.openuss.chat;

import org.openuss.foundation.DomainObject;

/**
 * Id of the chatuser is equal to the user id
 * 
 * @author Ingo D�ppe
 */
public interface ChatUser extends DomainObject {

    public Long getId();

    public void setId(Long id);

    public String getDisplayName();

    public void setDisplayName(String displayName);

    public String getEmail();

    public void setEmail(String email);
}
