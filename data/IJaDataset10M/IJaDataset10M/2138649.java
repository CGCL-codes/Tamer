package net.sf.hibernate4gwt.sample.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * User Domain class for stateful server
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
	 * @gwt.typeArgs <net.sf.hibernate4gwt.sample.domain.Message>
	 */
    private Set messageList;

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
	 * @gwt.typeArgs <net.sf.hibernate4gwt.sample.domain.Message>
	 * @return the message List
	 */
    public Set getMessageList() {
        return messageList;
    }

    /**
	 * @gwt.typeArgs messageList <net.sf.hibernate4gwt.sample.domain.Message>
	 * @param messageList the message List to set
	 */
    public void setMessageList(Set messageList) {
        this.messageList = messageList;
    }

    public void addMessage(Message message) {
        message.setAuthor(this);
        if (messageList == null) {
            messageList = new HashSet();
        }
        messageList.add(message);
    }

    public void removeMessage(Message message) {
        messageList.remove(message);
    }
}
