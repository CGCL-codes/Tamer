package entity.subscription;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import entity.EntitySuperclass;
import entity.protocol.Protocol;
import entity.user.User;

/**
 * <p>
 * Describes an email account.
 * </p>
 * 
 * <p>
 * JPA entity class for the <code>APP_SUBSCRIPTION</code> table. This class is
 * kept simple to allow for easier regeneration.
 * </p>
 */
@Entity(name = "APP_SUBSCRIPTION")
@NamedQueries({ @NamedQuery(name = Subscription.FIND_ALL, query = "SELECT s FROM APP_SUBSCRIPTION s"), @NamedQuery(name = Subscription.FIND_BY_NAME, query = "SELECT s FROM APP_SUBSCRIPTION s WHERE s.host = :host"), @NamedQuery(name = Subscription.FIND_BY_USER_ID, query = "SELECT s FROM APP_SUBSCRIPTION s WHERE s.user = :id") })
public class Subscription extends EntitySuperclass implements Serializable {

    /**
     * <p>
     * Named query for finding a <code>User</code> by username.
     * </p>
     */
    public static final String FIND_ALL = "Subscription.FIND_ALL";

    /**
     * <p>
     * Named query for finding a <code>Subscription</code> by host.
     * </p>
     */
    public static final String FIND_BY_HOST = "Subscription.FIND_BY_HOST";

    /**
     * <p>
     * Named query for finding a <code>User</code> by username.
     * </p>
     */
    public static final String FIND_BY_USER_ID = "Subscription.FIND_BY_USER_ID";

    /**
     * <p>
     * Named query for finding a <code>User</code> by username.
     * </p>
     */
    public static final String FIND_BY_NAME = "Subscription.FIND_BY_HOST";

    /**
     * <p>
     * Token represnting the "host" attribute.
     * </p>
     */
    public static final String HOST = "host";

    @Column
    private boolean auto_connect;

    @Column(nullable = false)
    private String host;

    @Column(nullable = false)
    private String password;

    @JoinColumn(name = "protocol_id")
    @OneToOne
    private Protocol protocol_id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String username;

    public boolean isAutoConnect() {
        return auto_connect;
    }

    public void setAutoConnect(boolean value) {
        auto_connect = value;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String value) {
        host = value;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String value) {
        password = value;
    }

    public Protocol getProtocol() {
        return protocol_id;
    }

    public void setProtocol(Protocol value) {
        protocol_id = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User value) {
        user = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String value) {
        username = value;
    }

    /**
     * <p>
     * Instantiate a default <code>Subscription</code> object.
     * </p>
     */
    public Subscription() {
    }
}
