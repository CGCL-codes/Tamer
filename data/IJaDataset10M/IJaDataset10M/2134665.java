package packets;

import java.io.Serializable;
import mupedk.User;

/**
 *
 * @author jan-philipp
 */
public class AdminAddUserPacket implements Serializable {

    User user;

    /**
     *
     * @param user
     */
    public AdminAddUserPacket(User user) {
        this.user = user;
    }

    /**
     *
     * @return
     */
    public User getUser() {
        return this.user;
    }
}
