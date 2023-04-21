package net.sf.mailsomething.mail;

/**
 *@author     Stig Tanggaard.
 *@created    October 4, 2001
 *@version    0.1 Interface describing a MessageHolder, eg. a mailbox containing
 *      messages or similar.
 */
public interface MessageHolder extends Hierachy {

    /**
	 *  Returns an array of all messages in the mailbox.
	 *
	 *@return    The messages value
	 */
    public Message[] getMessages();

    /**
	 *  Gets the message (if exists) with the corresponding messageid.
	 *
	 *@param  messageid  Description of Parameter
	 *@return            The message value
	 */
    public Message getMessage(String messageid);

    /**
	 *  Removes/deletes the message from the mailbox. If the mailbox
	 * is 'remote' ie equevalent to a mailbox on a server (imap) the
	 * message will also be removed at the server.
	 *
	 *@param  message  Description of Parameter
	 */
    public void removeMessage(Message message);

    /**
	 *  For adding a message to the mailbox.
	 *
	 *@param  message  the message to add.
	 */
    public void addMessage(Message message);

    /**
	 *  No need for the old version where inputargument 
	 * was message and not messageid. One
	 * must asume that a message with an equal messageid 
	 * is the same. This should be removed.
	 * or changed with existsUid. If one wants to 
	 * find/search for a message with a specific field
	 * one should use a search method (which isnt done yet).
	 *
	 *@param  messageid  Description of Parameter
	 *@return          true if a message with the messagid exists.
	 */
    public boolean exists(String messageid);

    /**
	 * if a message with the UID exists. The uid is a message attribute
	 * appointed by the server to keep track of the mailbox. Normally
	 * a class dont need to use this. 
	 * 
	 * @param uid
	 * @return
	 */
    public boolean existsUid(String uid);

    /**
	 * Method updateMessages. For updating the mailbox. Calling this only
	 * makes sence on a 'remote' mailbox. The update type is a custom
	 * type which is implemented by the MailAccount subtypes. Ie, different
	 * mailaccounts has different available updatetypes. 
	 * 
	 * 
	 * @see net.sf.mailsomething.mail.ImapAccount
	 * @param type
	 */
    public void updateMessages(int type);

    /**
	 * To add a messagelistener.
	 * 
	 * @param listener
	 * @see net.sf.mailsomething.mail.MessageListener
	 */
    public void addMessageListener(MessageListener listener);

    /**
	 * Method removeMessageListener.
	 * @param listener
	 * @see net.sf.mailsomething.mail.MessageListener
	 */
    public void removeMessageListener(MessageListener listener);

    /**
	 * Get the messagecount of the mailbox
	 * @return int
	 */
    public int getMessageCount();

    /**
	 * Gets the message at the specified index. The index starts at 0,
	 * which mean u cant use getMessage(getMessageCount()) to get the last
	 * message, the last message will be getMessageCount()-1
	 * @param index
	 * @return Message
	 */
    public Message getMessage(int index);

    /**
	 * Ehm... how did this method end up here?
	 * 
	 * @return
	 */
    public MailboxHolder getParentMailbox();

    /** Both this and movemessage exists because of synchronization. One should be able to 
	* just remove a message from the original mailbox, and add to the new mailbox. But theese
	* methods should allow for better implementations. 
	* To be specific: in Imap its a question of
	* using the copy command rather than the append command, which is ofcourse better if its possible.
	* 
	*/
    public void copyMessage(Message message, MessageHolder newMailbox);

    /**
	 * Method for moving a message from the mailbox its called on to
	 * the argument mailbox. This should be used instead of directly
	 * calling removeMessage and addMessage on 2 different mailboxes, 
	 * since if the mailboxes are remote it might be more efficient to
	 * call a move-method on the server instead of deleting the message
	 * and appending it in another mailbox (which requires we send the
	 * message to the server) which again isnt necessary if the server
	 * has a move-message command. 
	 * 
	 * @param message
	 * @param newMailbox
	 */
    public void moveMessage(Message message, MessageHolder newMailbox);
}
