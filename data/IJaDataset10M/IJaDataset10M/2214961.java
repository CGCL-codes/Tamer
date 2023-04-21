package org.hip.vif.admin.member.tasks;

import org.hip.kernel.exc.VException;
import org.hip.kernel.mail.MailGenerationException;
import org.hip.vif.admin.member.Activator;
import org.hip.vif.admin.member.Constants;
import org.hip.vif.admin.member.data.RoleContainer;
import org.hip.vif.admin.member.mail.PasswordResetMail;
import org.hip.vif.admin.member.ui.MemberEditView;
import org.hip.vif.admin.member.ui.MemberView;
import org.hip.vif.core.annotations.Partlet;
import org.hip.vif.core.bom.LinkMemberRoleHome;
import org.hip.vif.core.bom.Member;
import org.hip.vif.core.bom.MemberHome;
import org.hip.vif.core.bom.VIFMember;
import org.hip.vif.core.bom.impl.BOMHelper;
import org.hip.vif.core.exc.BOMChangeValueException;
import org.hip.vif.core.exc.ExternIDNotUniqueException;
import org.hip.vif.core.interfaces.IMessages;
import org.hip.vif.core.service.AuthenticationUtility;
import org.hip.vif.core.util.RatingsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window.Notification;

/**
 * Shows the member data for that it can be edited.
 * 
 * @author Luthiger
 * Created: 19.10.2011
 */
@Partlet
public class MemberShowTask extends AbstractMemberTask {

    private static final Logger LOG = LoggerFactory.getLogger(MemberShowTask.class);

    Long memberID;

    @Override
    protected Component runChecked() throws VException {
        try {
            loadContextMenu(Constants.MENU_SET_ID_DEFAULT);
            memberID = (Long) getParameters().get(Constants.KEY_PARAMETER_MEMBER);
            IMemberHelper lMember = retrieveMember(memberID);
            if (isExternal()) {
                return new MemberView(lMember.getMember(), RoleContainer.createData(BOMHelper.getLinkMemberRoleHome().getRolesOf(memberID), getAppLocale().getLanguage(), BOMHelper.getRoleHome().getGroupSpecificIDs()), new RatingsHelper(memberID), this);
            }
            return new MemberEditView(lMember.getMember(), RoleContainer.createData(BOMHelper.getLinkMemberRoleHome().getRolesOf(memberID), getAppLocale().getLanguage(), BOMHelper.getRoleHome().getGroupSpecificIDs()), new RatingsHelper(memberID), this);
        } catch (Exception exc) {
            throw createContactAdminException(exc);
        }
    }

    private boolean isExternal() throws VException {
        return AuthenticationUtility.INSTANCE.getActiveAuthenticator().isExternal();
    }

    @Override
    public boolean saveMember(Member inMember, RoleContainer inRoles) throws ExternIDNotUniqueException {
        try {
            boolean lChangedRoles = inMember.ucSave(inRoles.getSelectedIDs(), getActor().getActorID());
            refreshAndNotify(lChangedRoles, getNotificationMessage(inMember));
            return true;
        } catch (BOMChangeValueException exc) {
            LOG.error("Error while saving the member data.", exc);
        } catch (VException exc) {
            LOG.error("Error while saving the member data.", exc);
        }
        return false;
    }

    /**
	 * Callback method, saves the changed member's roles.
	 * 
	 * @param inMember {@link Member} 
	 * @param inRoles {@link RoleContainer}
	 * @return boolean <code>true</code> if the roles have successfully been saved
	 */
    public boolean saveRoles(Member inMember, RoleContainer inRoles) {
        try {
            LinkMemberRoleHome lLinkHome = (LinkMemberRoleHome) BOMHelper.getLinkMemberRoleHome();
            boolean lChangedRoles = lLinkHome.updateRoles(memberID, inRoles.getSelectedIDs());
            refreshAndNotify(lChangedRoles, getNotificationMessage(inMember));
            return true;
        } catch (BOMChangeValueException exc) {
            LOG.error("Error while saving the member data.", exc);
        } catch (VException exc) {
            LOG.error("Error while saving the member data.", exc);
        }
        return false;
    }

    protected IMemberHelper retrieveMember(Long inMemberID) throws Exception {
        return new MemberHelper(inMemberID);
    }

    @Override
    protected Long getMemberID() {
        return memberID;
    }

    @Override
    public boolean resetPW() {
        try {
            if (isExternal()) return false;
            Member lMember = BOMHelper.getMemberHome().getMember(getMemberID());
            String lPassword = createPassword();
            lMember.savePwrd(lPassword);
            PasswordResetMail lMail = new PasswordResetMail((VIFMember) lMember, lPassword);
            lMail.send();
            refreshAndNotify(false, getNotificationMessage(lMember, lPassword));
            return true;
        } catch (MailGenerationException exc) {
            showNotification(Activator.getMessages().getMessage("errmsg.member.pwrd.no.mail"), Notification.TYPE_WARNING_MESSAGE);
            sendEvent(MemberSearchTask.class);
            return true;
        } catch (Exception exc) {
            LOG.error("Error while reseting the member's password.", exc);
        }
        return false;
    }

    private String getNotificationMessage(Member inMember, String inPwrd) throws VException {
        IMessages lMessages = Activator.getMessages();
        String lUserID = inMember.get(MemberHome.KEY_USER_ID).toString();
        StringBuilder outNotification = new StringBuilder(lMessages.getFormattedMessage("msg.member.reset.pw", lUserID));
        if (displayPassword()) {
            outNotification.append(" ").append(lMessages.getFormattedMessage("msg.member.data.saved.add", lUserID, inPwrd));
        }
        return new String(outNotification);
    }

    protected interface IMemberHelper {

        Member getMember();

        Long getMemberID();
    }

    private class MemberHelper implements IMemberHelper {

        private Long memberID;

        private Member member;

        public MemberHelper(Long inMemberID) throws Exception {
            memberID = new Long(inMemberID);
            member = BOMHelper.getMemberCacheHome().getMember(inMemberID);
        }

        public Member getMember() {
            return member;
        }

        public Long getMemberID() {
            return memberID;
        }
    }
}
