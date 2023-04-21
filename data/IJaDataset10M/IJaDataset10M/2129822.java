package org.thole.phiirc.client.view.interfaces;

import org.thole.phiirc.client.view.swing.ChatInput;

/**
 * Interface to represent the chat input fild as
 * GUI Element
 * 
 * @author ch.claus
 *
 */
public interface IChatInput {

    /**
	 * @return the message, send by the user
	 */
    public String getMessage();

    /**
	 * clears the chat input
	 */
    public void clearInput();

    /**
	 * @return itself
	 */
    public ChatInput getThis();
}
