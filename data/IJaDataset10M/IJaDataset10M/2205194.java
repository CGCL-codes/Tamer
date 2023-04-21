package vavi.net.im;

import java.io.Serializable;
import vavi.net.im.protocol.Protocol;

/**
 * A buddy is a person who is participating in instant messaging.
 * A buddy is usually identified by a unique name in the underlying
 * protocol's namespace.
 *
 * @author Raghu
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 */
public class Buddy implements Serializable {

    /**
     * The username of this buddy.
     */
    private String username;

    /**
     * Status of this buddy.
     */
    private String status;

    /**
     * Custom status of this buddy.
     */
    private String customStatus;

    /**
     * Alias name of this buddy.
     */
    private String alias;

    /**
     * Construct a buddy object for a buddy with a specified
     * protocol and username. This constructor is used when the
     * group of the buddy is not relevant.
     *
     * @param username the username of this buddy.
     */
    public Buddy(String username) {
        this.username = username;
    }

    /**
     * Get the status message for this buddy. Hamsam does not always keep
     * track of buddy statuses. The only place where a client is notified
     * about status change is the
     * {@link vavi.net.im.event.IMEvent.IMEventName#buddyStatusChanged} method.
     *
     * @return the status message, or <code>null</code> if this buddy is offline.
     * @see vavi.net.im.protocol.Protocol#changeStatus(String) Protocol.changeStatus(String status)
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status message for this buddy.
     *
     * @param status the status message.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return Returns the customStatus.
     */
    public String getCustomStatus() {
        return customStatus;
    }

    /**
     * @param customStatus The customStatus to set.
     */
    public void setCustomStatus(String customStatus) {
        this.customStatus = customStatus;
    }

    /**
     * Returns the alias name of this buddy. Alias is also known as nick name.
     * Alias is not unique in the namespace of a protocol. Not all protocols
     * support aliases. If this buddy's protocol does not support aliases, this
     * method returns <code>null</code>.
     *
     * @see Protocol.Feature#BuddyNameAliasSupported
     * @return the alias name of this buddy.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Set the alias name of this buddy. This method is internally used by Hamsam API
     * and not meant for users of the API.
     *
     * <p>
     * Alias is also known as nick name. Alias is not unique in the namespace of a
     * protocol. Not all protocols support aliases.
     *
     * @see Protocol.Feature#BuddyNameAliasSupported
     * @param alias the alias name of this buddy.
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Returns the username of this buddy.
     *
     * @return the username of this buddy.
     */
    public String getUsername() {
        return username;
    }
}
