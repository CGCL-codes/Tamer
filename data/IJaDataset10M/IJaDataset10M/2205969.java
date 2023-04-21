package de.rentoudu.chat.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Id;

/**
 * The persistence channel model.
 * 
 * @author Florian Sauter
 */
public class Channel implements Serializable {

    private static final long serialVersionUID = 5908401699881446757L;

    @Id
    private String id;

    private String password;

    private List<String> permittedUsers;

    private int maxParticipants;

    public Channel() {
    }

    public Channel(String id, String password, List<String> permittedUsers, int maxParticipants) {
        this.id = id;
        this.password = password;
        this.permittedUsers = permittedUsers;
        this.maxParticipants = maxParticipants;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getPermittedUsers() {
        return permittedUsers;
    }

    public void setPermittedUsers(List<String> permittedUsers) {
        this.permittedUsers = permittedUsers;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
}
