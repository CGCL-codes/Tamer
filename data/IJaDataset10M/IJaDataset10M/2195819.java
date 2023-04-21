package org.sepp.channels;

import iaik.me.security.CryptoBag;
import java.util.List;
import java.util.LinkedList;
import org.sepp.messages.BaseMessage;
import org.sepp.states.ProtocolStatesHandler;
import org.sepp.utils.xml.XMLTags;

/**
 * This class represents all datas which are needed to join a group.
 * 
 * @author david_monichi - david_monichi@sourceforge.net
 * 
 */
public class Channel {

    private String id;

    private String name;

    private String contact;

    private String description;

    private String connectTo;

    private CryptoBag sessionKey;

    private int authenticationMethod;

    private List members = new LinkedList();

    /**
	 * 
	 */
    public Channel(String id, String name, String contact, String description, String connectTo, int authMethod) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.connectTo = connectTo;
        this.description = description;
        this.authenticationMethod = authMethod;
    }

    /**
	 * @return group name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return group id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @return authentication method from type {@code}AuthenticationProcess
	 */
    public int getAuthenticationMethod() {
        return authenticationMethod;
    }

    /**
	 * A set description is not useful since users join a group for a specific
	 * topic, if the topic changes most of the user are not interested anymore
	 * and therefore its useful to create a new group for a new topic.
	 * 
	 * @return group description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * For every group there should be a valid contact so that every user which
	 * would like to enter a group can contact the owner to obtain credentials
	 */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
	 * @return group contact
	 */
    public String getContact() {
        return contact;
    }

    /**
	 * 
	 * 
	 * @param
	 */
    public void setSessionKey(CryptoBag sessionKey) {
        this.sessionKey = sessionKey;
    }

    public void setSessionKey(byte[] sessionKey) {
        this.sessionKey = CryptoBag.makeSecretKey(sessionKey);
    }

    /**
	 * @return
	 */
    public CryptoBag getSessionKey() {
        return sessionKey;
    }

    /**
	 * @return
	 */
    public byte[] getSessionKeyBytes() {
        return sessionKey.getByteArray(CryptoBag.V_KEY);
    }

    /**
	 * 
	 */
    public void setConnectTo(String connectTo) {
        this.connectTo = connectTo;
    }

    /**
	 * 
	 */
    public String getConnectTo() {
        return connectTo;
    }

    /**
	 * 
	 */
    public void removeMember(String peerId) {
        members.remove(peerId);
    }

    /**
	 * 
	 */
    public void clearMembersList() {
        members.clear();
    }

    /**
	 * 
	 */
    public void addMember(String peerId) {
        if (!members.contains(peerId)) members.add(peerId);
    }

    /**
	 * 
	 */
    public List getMembers() {
        return members;
    }

    /**
	 * 
	 * @param members
	 */
    public void setMembers(List members) {
        this.members = members;
    }

    /**
	 * 
	 */
    public void addChannelToMessage(BaseMessage msg) {
        msg.setString(XMLTags.ID, id);
        msg.setString(XMLTags.NAME, name);
        msg.setString(XMLTags.DESCRIPTION, description);
        msg.setString(XMLTags.CONTACT, contact);
        msg.setString(XMLTags.CONNECT_TO, connectTo);
        msg.setString(XMLTags.AUTHENTICATION_METHOD, String.valueOf(authenticationMethod));
    }

    public boolean isValid() {
        if ((id == null) || (name == null) || (description == null) || (contact == null) || (connectTo == null)) return false;
        if ((authenticationMethod == ProtocolStatesHandler.NO_AUTHENTICATION) || (authenticationMethod == ProtocolStatesHandler.SHARED_SECRET_KEY_AUTHENTICATION) || (authenticationMethod == ProtocolStatesHandler.PUBLIC_PRIVATE_KEY_AUTHENTICATION)) return true;
        return false;
    }

    /**
	 * 
	 */
    public void addMembersToMessage(BaseMessage msg) {
        msg.setList(XMLTags.MEMBERS, members);
    }
}
