package net.sf.gilead.sample.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * User Domain class for stateless server
 */
public class User implements Serializable {

    /**
	 * Serialisation ID
	 */
    private static final long serialVersionUID = 1058354709157710766L;

    private Integer id;

    private Integer version;

    private String login;

    private String firstName;

    private String lastName;

    private String password;

    /**
	 * Message list
	 */
    private Set<Message> messageList;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String surname) {
        this.login = surname;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * @return the message List
	 */
    public Set<Message> getMessageList() {
        return messageList;
    }

    /**
	 * @param messageList the message List to set
	 */
    public void setMessageList(Set<Message> messageList) {
        this.messageList = messageList;
    }

    public void addMessage(Message message) {
        message.setAuthor(this);
        if (messageList == null) {
            messageList = new HashSet<Message>();
        }
        messageList.add(message);
    }

    public void removeMessage(Message message) {
        messageList.remove(message);
    }
}
