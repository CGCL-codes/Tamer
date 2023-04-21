package com.intrigueit.myc2i.message.service;

import java.util.List;
import com.intrigueit.myc2i.message.domain.Message;

/**
 * This is the service interface for message entity
 * 
 * @version 1.00 August 05, 2010
 * @author Shamim Ahmmed
 * 
 */
public interface MessageService {

    /** Save the message entity */
    public void save(Message entity);

    /** Delete the message entity */
    public void delete(Message entity);

    /** Update the existing message entity */
    public void update(Message entity);

    /** Find the message entity */
    public Message findById(Long messageId);

    /** Get the list of all conversation belongs to this sender */
    public List<Message> getConversation(Long senderId);

    /** Get the list of all conversation belongs to this sender */
    public List<Message> getConversationByOwner(Long ownerId, String status, int startIndex);

    /** Get the list of all conversation belongs to this sender */
    public List<Message> getUnReadConversationByOwner(Long ownerId, String status, int startIndex);

    /** Get the list of all conversion based on reference message */
    public List<Message> getConversationByReferenceMessage(Long ownerId, Long refMessageId);

    /** Remove message by message Id */
    public Boolean removeMessage(Long messageId);

    /** Return the no of unread message of user mail box */
    public int getUnReadMessageByOwner(Long ownerId);
}
