package net.sf.jml.message.invitation;

/**
 * Abstract accept message, which invitation command is ACCEPT.
 * 
 * @author Roger Chen
 */
public abstract class MsnAcceptMessage extends MsnInvitationMessage {

    private final MsnInviteMessage invite;

    public MsnAcceptMessage(MsnInviteMessage invite) {
        this.invite = invite;
        setInvitationCommand(InvitationConstants.COMMAND_ACCEPT);
        setInvitationCookie(invite.getInvitationCookie());
        setLaunchApplication(false);
    }

    public final boolean isLaunchApplication() {
        return Boolean.TRUE.toString().equalsIgnoreCase(properties.getProperty(InvitationConstants.KEY_LAUNCH_APP));
    }

    public final void setLaunchApplication(boolean b) {
        properties.setProperty(InvitationConstants.KEY_LAUNCH_APP, b ? Boolean.TRUE.toString().toUpperCase() : Boolean.FALSE.toString().toUpperCase());
    }

    public MsnInviteMessage getInviteMessage() {
        return invite;
    }
}
