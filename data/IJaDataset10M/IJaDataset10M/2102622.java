package packets;

import java.io.Serializable;

/**
 *
 * @author jan-philipp
 */
public class NotifyUserInvitedToProjectPacket implements Serializable {

    private String inviter, projectname, description;

    /**
     *
     * @param projectname
     * @param description
     * @param inviter
     */
    public NotifyUserInvitedToProjectPacket(String projectname, String description, String inviter) {
        this.inviter = inviter;
        this.description = description;
        this.projectname = projectname;
    }

    /**
     *
     * @return
     */
    public String getInviter() {
        return this.inviter;
    }

    /**
     * 
     * @return
     */
    public String getProjectname() {
        return this.projectname;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return this.description;
    }
}
