package com.agileprojectassistant.shared;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

/**
 * A proxy object containing a message destined for a particular
 * recipient, identified by email address.
 */
@ProxyForName(value = "com.agileprojectassistant.server.Message", locator = "com.agileprojectassistant.server.MessageLocator")
public interface MessageProxy extends ValueProxy {

    String getMessage();

    String getRecipient();

    void setRecipient(String recipient);

    void setMessage(String message);
}
