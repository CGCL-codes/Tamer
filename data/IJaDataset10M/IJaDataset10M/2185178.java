package org.hip.vif.admin.member.tasks;

import java.io.IOException;
import java.util.Random;
import org.hip.kernel.bom.GettingException;
import org.hip.kernel.exc.VException;
import org.hip.vif.admin.member.Activator;
import org.hip.vif.admin.member.Constants;
import org.hip.vif.admin.member.data.RoleContainer;
import org.hip.vif.core.bom.Member;
import org.hip.vif.core.bom.MemberHome;
import org.hip.vif.core.exc.BOMChangeValueException;
import org.hip.vif.core.exc.ExternIDNotUniqueException;
import org.hip.vif.core.service.PreferencesHandler;
import org.hip.vif.web.tasks.AbstractVIFTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vaadin.ui.Window.Notification;

/**
 * Abstract base class for member tasks.
 * 
 * @author Luthiger
 * Created: 22.10.2011
 */
public abstract class AbstractMemberTask extends AbstractVIFTask {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractMemberTask.class);

    private static final char[] saltChars = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789./".toCharArray());

    @Override
    protected String needsPermission() {
        return Constants.PERMISSION_SEARCH;
    }

    /**
	 * Callback method, saves the changed member data.
	 * 
	 * @param inMember {@link Member}
	 * @param inRoles {@link RoleContainer}
	 * @return boolean <code>true</code> if successful
	 * @throws ExternIDNotUniqueException
	 */
    public abstract boolean saveMember(Member inMember, RoleContainer inRoles) throws ExternIDNotUniqueException;

    /**
	 * Notify the user and forward to the next task.
	 * 
	 * @param inChangedRoles boolean
	 * @param inFeedback String
	 * @throws BOMChangeValueException
	 * @throws GettingException
	 */
    protected void refreshAndNotify(boolean inChangedRoles, String inFeedback) throws BOMChangeValueException, VException {
        if (isActorChanged() && inChangedRoles) {
            getActor().refreshAuthorization();
        }
        showNotification(inFeedback, Notification.TYPE_TRAY_NOTIFICATION);
        sendEvent(MemberSearchTask.class);
    }

    /**
	 * Subclasses may override.
	 * 
	 * @param inMember {@link Member}
	 * @return String the notification message to signal successful data procession 
	 * @throws VException
	 */
    protected String getNotificationMessage(Member inMember) throws VException {
        return Activator.getMessages().getFormattedMessage("msg.member.data.saved", inMember.get(MemberHome.KEY_USER_ID));
    }

    /**
	 * @return boolean <code>true</code> if the actor (i.e. the actual user) is changing his own member entry
	 */
    private boolean isActorChanged() {
        return getActor().getActorID().equals(getMemberID());
    }

    protected abstract Long getMemberID();

    /**
	 * Creates a new password.
	 * 
	 * @return java.lang.String The new password.
	 */
    protected String createPassword() {
        Random randomGenerator = new Random();
        int numSaltChars = saltChars.length;
        StringBuffer outPassword = new StringBuffer();
        for (int i = 0; i < 6; i++) outPassword.append(saltChars[Math.abs(randomGenerator.nextInt()) % numSaltChars]);
        return new String(outPassword);
    }

    /**
	 * Checks whether the password should be displayed on the administration screen.
	 * 
	 * @return boolean <code>true</true> if setting for password display is activated
	 */
    protected boolean displayPassword() {
        try {
            return Boolean.parseBoolean(PreferencesHandler.INSTANCE.get(PreferencesHandler.KEY_PW_DISPLAY));
        } catch (IOException exc) {
            LOG.error("Error encountered while retrieving the preferences!", exc);
            return false;
        }
    }

    /**
	 * Subclasses may override.
	 * 
	 * @return boolean <code>true</code> if the password have been reseted successfully
	 */
    public boolean resetPW() {
        return false;
    }
}
