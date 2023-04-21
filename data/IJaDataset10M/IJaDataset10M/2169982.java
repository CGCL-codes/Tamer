package org.openuss.messaging;

import org.openuss.foundation.DomainObject;

/**
 * @author Ingo Dueppe
 */
public interface TextMessage extends Message, DomainObject {

    public String getText();

    public void setText(String text);
}
