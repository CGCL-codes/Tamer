package org.vramework.mvc;

import java.util.List;

/**
 * Defines the next step after an action has been performed. <br />
 * Implementors define what should happen exactly, e.g. render a view or call another action.
 * 
 * @author thomas.mahringer
 * @see IAction#action(IConversation, java.util.Map, ActionDef, ActionDef, ActionDef, Object)
 */
public interface INextStep {

    /**
   * @return The (optional) user messages, e.g. hints and warnings to be displayed on the next step's view.
   */
    List<UserMessage> getMessages();

    /**
   * Adds a message, see {@link #getMessages()}.
   * @param message
   */
    void addMessage(UserMessage message);

    /**
   * Adds messages, see {@link #getMessages()}.
   * @param messages
   */
    void addMessages(List<UserMessage> messages);
}
