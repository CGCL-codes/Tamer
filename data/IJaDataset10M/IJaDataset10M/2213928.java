package org.jagent.acr;

import java.util.ArrayList;
import java.util.List;

/**
 * The AclMessage class is a special type of <code>AcrNode</code> that
 * conforms to the FIPA specifications.
 * It maintains the mapping between the <code>AclConstants</code> and
 * the corresponding <code>AcrNodes</code> by hiding the information
 * access behind convience methods.  The <code>AclMessage</code> has
 * the following mandatory well-know attributes:
 * <ul>
 *    <li> performative
 *    <li> content
 *    <li> sender
 *    <li> receiver
 * </ul>
 * and the following optional well-known properties:
 * <ul>
 *    <li> reply-to
 *    <li> language
 *    <li> ontology
 *    <li> encoding
 *    <li> conversation-id
 *    <li> reply-with
 *    <li> in-reply-to
 *    <li> protocol
 *    <li> reply-by
 * </ul>
 *
 * @author A. Spydell
 * @since 1.0
 */
public final class AclMessage extends AcrNode {

    /**
    * Create a new empty AclMessage.
    */
    public AclMessage() {
    }

    /**
    * Create a new AclMessage based upon the given performative.
    * @see #setPerformative(String)
    */
    public AclMessage(String perform) {
        setPerformative(perform);
    }

    /**
    * Sets the well-known performative attribute of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_PERFORMATIVE key.
    * @param actionType the performative of the AclMessage.
    * @exception IllegalArgumentException if actionType is null or
    *            does not conform to FIPA specifications.
    */
    public void setPerformative(String actionType) {
        if (!FipaAclPerformative.isValidPerformative(actionType)) throw new IllegalArgumentException("Non conforment action type.");
        set(AclConstants.ACL_PERFORMATIVE, actionType);
    }

    /**
    * Returns the well-known performative attribute of the
    * <code>AclMessage</code>.
    * @return the performative of the AclMessage
    */
    public String getPerformative() {
        return (String) get(AclConstants.ACL_PERFORMATIVE);
    }

    /**
    * Sets the well-known sender attribute of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_SENDER key.
    * @param name the sender of the AclMessage.
    * @exception IllegalArgumentException if name is null
    * @see AcrAgentName
    */
    public void setSender(AcrAgentName name) {
        set(AclConstants.ACL_SENDER, name);
    }

    /**
    * Returns the well-known sender attribute of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_SENDER key.
    * @return AcrAgentName the sender of the AclMessage
    * @see AcrAgentName
    */
    public AcrAgentName getSender() {
        return (AcrAgentName) get(AclConstants.ACL_SENDER);
    }

    /**
    * Sets the well-known receiver attribute of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_RECEIVER key.
    * @param name the receiver of the AclMessage.
    * @exception IllegalArgumentException if name is null
    * @see AcrAgentName
    */
    public void addReceiver(AcrAgentName name) {
        check(name);
        if (!containsKey(AclConstants.ACL_RECEIVER)) set(AclConstants.ACL_RECEIVER, new ArrayList());
        List list = (List) get(AclConstants.ACL_RECEIVER);
        list.add(name);
    }

    public void setReceivers(AcrAgentName[] names) {
        check(names);
        if (!containsKey(AclConstants.ACL_RECEIVER)) set(AclConstants.ACL_RECEIVER, new ArrayList());
        List list = (List) get(AclConstants.ACL_RECEIVER);
        list.clear();
        for (int i = 0, n = names.length; i < n; i++) list.add(check(names[i]));
    }

    /**
    * Returns the well-known receiver attribute of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_RECEIVER key.
    * @return AcrAgentName the receiver of the AclMessage
    * @see AcrAgentName
    */
    public AcrAgentName[] getReceivers() {
        if (!containsKey(AclConstants.ACL_RECEIVER)) return new AcrAgentName[] {};
        List list = (List) get(AclConstants.ACL_RECEIVER);
        return (AcrAgentName[]) list.toArray(new AcrAgentName[] {});
    }

    /**
    * Sets the well-known content attribute of the
    * <code>AclMessage</code>.  The content String is first
    * encapsulated in a <code>String</code> Object and then
    * stored.  This attribute value corresponds to the
    * AclConstants.ACL_CONTENT key.
    * @param content the content of the AclMessage.
    * @exception IllegalArgumentException if content is null
    * @see #setContent( AcrNode )
    */
    public void setContent(String content) {
        setContent(new AcrString(content));
    }

    /**
    * Sets the well-known content attribute of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_CONTENT key.
    * @param content the content of the AclMessage.
    * @exception IllegalArgumentException if content is null
    * @see AcrNode
    */
    public void setContent(AcrNode content) {
        set(AclConstants.ACL_CONTENT, content);
    }

    /**
    * Returns the well-known content attribute of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_CONTENT key.
    * @return AcrNode the content of the AclMessage
    * @see AcrNode
    */
    public AcrNode getContent() {
        return (AcrNode) get(AclConstants.ACL_CONTENT);
    }

    /**
    * Sets the well-known reply-to property of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_REPLY_TO key.
    * @param id the reply-to delegate of the AclMessage.
    * @exception IllegalArgumentException if content is null
    * @see AcrAgentName
    */
    public void setReplyTo(AcrAgentName id) {
        check(id);
        addProperty(AclConstants.ACL_REPLY_TO, id);
    }

    /**
    * Returns the well-known reply-to property of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_REPLY_TO key.
    * @return AcrAgentName the reply-to delegate property of the
    *         AclMessage
    * @see AcrAgentName
    */
    public AcrAgentName getReplyTo() {
        return (AcrAgentName) getPropertyByName(AclConstants.ACL_REPLY_TO);
    }

    /**
    * Sets the well-known language property of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_LANGUAGE key.
    * @param o the language of the AclMessage.
    * @exception IllegalArgumentException if o is null
    * @see AcrNode
    */
    public void setLanguage(AcrNode o) {
        check(o);
        addProperty(AclConstants.ACL_LANGUAGE, o);
    }

    /**
    * Returns the well-known language property of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_LANGUAGE key.
    * @return AcrNode the language property of the AclMessage
    * @see AcrNode
    */
    public AcrNode getLanguage() {
        return getPropertyByName(AclConstants.ACL_LANGUAGE);
    }

    /**
    * Sets the well-known ontology property of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_ONTOLOGY key.
    * @param o the ontology of the AclMessage.
    * @exception IllegalArgumentException if o is null
    * @see AcrNode
    */
    public void setOntology(AcrNode o) {
        check(o);
        addProperty(AclConstants.ACL_ONTOLOGY, o);
    }

    /**
    * Returns the well-known ontology property of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_ONTOLOGY key.
    * @return AcrNode the ontology property of the AclMessage
    * @see AcrNode
    */
    public AcrNode getOntology() {
        return getPropertyByName(AclConstants.ACL_ONTOLOGY);
    }

    /**
    * Sets the well-known encoding property of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_ENCODING key.
    * @param o the encoding of the AclMessage.
    * @exception IllegalArgumentException if o is null
    * @see AcrNode
    */
    public void setEncoding(AcrNode o) {
        check(o);
        addProperty(AclConstants.ACL_ENCODING, o);
    }

    /**
    * Returns the well-known encoding property of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_ENCODING key.
    * @return AcrNode the encoding property of the AclMessage
    * @see AcrNode
    */
    public AcrNode getEncoding() {
        return getPropertyByName(AclConstants.ACL_ENCODING);
    }

    /**
    * Sets the well-known conversation-id property of the
    * <code>AclMessage</code>.  This attribute value corresponds to
    * the AclConstants.ACL_CONVERSATION_ID key.
    * @param o the conversation-id of the AclMessage.
    * @exception IllegalArgumentException if o is null
    * @see AcrNode
    */
    public void setConversationID(AcrNode o) {
        check(o);
        addProperty(AclConstants.ACL_CONVERSATION_ID, o);
    }

    /**
    * Returns the well-known conversation-id property of the
    * <code>AclMessage</code>.  This attribute value corresponds to
    * the AclConstants.ACL_CONVERSATION_ID key.
    * @return AcrNode the encoding property of the AclMessage
    * @see AcrNode
    */
    public AcrNode getConversationID() {
        return getPropertyByName(AclConstants.ACL_CONVERSATION_ID);
    }

    /**
    * Sets the well-known reply-with property of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_REPLY_WITH key.
    * @param o the reply-with of the AclMessage.
    * @exception IllegalArgumentException if o is null
    * @see AcrNode
    */
    public void setReplyWith(AcrNode o) {
        check(o);
        addProperty(AclConstants.ACL_REPLY_WITH, o);
    }

    /**
    * Returns the well-known reply-with property of the
    * <code>AclMessage</code>.  This attribute value corresponds to
    * the AclConstants.ACL_REPLY_WITH key.
    * @return AcrNode the reply-with property of the AclMessage
    * @see AcrNode
    */
    public AcrNode getReplyWith() {
        return getPropertyByName(AclConstants.ACL_REPLY_WITH);
    }

    /**
    * Sets the well-known in-reply-to property of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_IN_REPLY_TO key.
    * @param o the in-reply-to of the AclMessage.
    * @exception IllegalArgumentException if o is null
    * @see AcrNode
    */
    public void setInReplyTo(AcrNode o) {
        check(o);
        addProperty(AclConstants.ACL_IN_REPLY_TO, o);
    }

    /**
    * Returns the well-known in-reply-to property of the
    * <code>AclMessage</code>.  This attribute value corresponds to
    * the AclConstants.ACL_IN_REPLY_TO key.
    * @return AcrNode the in-reply-to property of the AclMessage
    * @see AcrNode
    */
    public AcrNode getInReplyTo() {
        return getPropertyByName(AclConstants.ACL_IN_REPLY_TO);
    }

    /**
    * Sets the well-known protocol property of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_PROTOCOL key.
    * @param o the protocol of the AclMessage.
    * @exception IllegalArgumentException if o is null
    * @see AcrNode
    */
    public void setProtocol(AcrNode o) {
        check(o);
        addProperty(AclConstants.ACL_PROTOCOL, o);
    }

    /**
    * Returns the well-known protocol property of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_PROTOCOL key.
    * @return AcrNode the protocol property of the AclMessage
    * @see AcrNode
    */
    public AcrNode getProtocol() {
        return getPropertyByName(AclConstants.ACL_PROTOCOL);
    }

    /**
    * Sets the well-known reply-by property of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_REPLY_BY key.
    * @param dt the reply by time of the AclMessage.
    * @exception IllegalArgumentException if dt is null
    * @see AcrDateTime
    */
    public void setReplyBy(AcrDateTime dt) {
        check(dt);
        addProperty(AclConstants.ACL_REPLY_BY, dt);
    }

    /**
    * Returns the well-known reply-by property of the
    * <code>AclMessage</code>.  This attribute value corresponds
    * to the AclConstants.ACL_REPLY_BY key.
    * @return AcrDateTime the reply-by property of the AclMessage
    * @see AcrDateTime
    */
    public AcrDateTime getReplyBy() {
        return (AcrDateTime) getPropertyByName(AclConstants.ACL_REPLY_BY);
    }

    /**
    * Allows a <code>AcrSerializer</code> to serialize this node.
    * @see AcrSerializer
    */
    public void acceptSerializer(AcrSerializer serializer) {
        serializer.serializeAclMessage(this);
    }
}
