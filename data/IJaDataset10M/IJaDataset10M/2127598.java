package org.openmim.wrapper;

import java.util.*;
import org.openmim.*;
import org.openmim.mn.MessagingNetwork;
import org.openmim.mn.MessagingNetworkListener;
import org.openmim.mn.MessagingNetworkException;
import org.openmim.icq.util.joe.*;
import org.openmim.icq2k.*;
import org.apache.log4j.Logger;

/**
  MR: rarify changestatus(src, dst) events for dst != src

  ,,,.messaging.wrapper.MessagingNetworkStatusFilter:
  //implements MessagingNetwork
  �᫨ ����� src X dst Y (X != Y) ������� ��, 祬 ࠧ � ������,
  � 䨫��஢��� ��०����� ����� ���䨪�樨 �� ࠧ� � ������.
  �� ���뢠�� from-core statuses & from-icq-server statuses.
*/
public class StatusFilterWrapper implements MessagingNetwork {

    private static final Logger CAT = Logger.getLogger(StatusFilterWrapper.class.getName());

    /**
StatusFilterWrapper.properties format:

# this will instantiate the mess networks specified.
#
# REQPARAM_WRAPPED_CLASS_NAME = \
#   <messagingNetwork.class.getName()>
#
# StatusFilterWrapper properties for wrapped class X
# are taken from the /X.properties resource.

REQPARAM_WRAPPED_CLASS_NAME = \
  org.openmim.icq2k.ICQ2KMessagingNetwork
*/
    public static String REQPARAM_WRAPPED_CLASS_NAME = null;

    /** Enables/disables the status filter */
    public static boolean REQPARAM_STATUS_FILTER_USED;

    /** wrapped mn class instance */
    private static MessagingNetworkWrappable wrapped = null;

    /** property for wrapped mn class instance */
    private static long statusFilterPeriodMillis = -1;

    private static Hashtable extListener2myListener = new Hashtable(1);

    static {
        try {
            Properties p = PropertyUtil.loadResourceProperties(PropertyUtil.getResourceFilePathName(StatusFilterWrapper.class));
            if (Defines.DEBUG && CAT.isInfoEnabled()) CAT.info("StatusFilter properties: " + p);
            AutoConfig.fetchFromClassLocalResourceProperties_propertyResourceRequired(StatusFilterWrapper.class);
            if ((REQPARAM_WRAPPED_CLASS_NAME) == null) Lang.ASSERT_NOT_NULL(REQPARAM_WRAPPED_CLASS_NAME, "REQPARAM_WRAPPED_CLASS_NAME field");
            REQPARAM_WRAPPED_CLASS_NAME = REQPARAM_WRAPPED_CLASS_NAME.trim();
            if (StringUtil.isNullOrEmpty(REQPARAM_WRAPPED_CLASS_NAME)) Lang.EXPECT_NOT_NULL_NOR_EMPTY(REQPARAM_WRAPPED_CLASS_NAME, "REQPARAM_WRAPPED_CLASS_NAME property value");
            Class clazz = Class.forName(REQPARAM_WRAPPED_CLASS_NAME);
            wrapped = (MessagingNetworkWrappable) clazz.newInstance();
            int statusFilterPeriodSeconds = PropertyUtil.getRequiredPropertyInt(p, StatusFilterWrapper.class + " properties resource", "statusFilterPeriodSeconds");
            if ((statusFilterPeriodSeconds) <= 0) Lang.EXPECT_POSITIVE(statusFilterPeriodSeconds, "statusFilterPeriodSeconds in " + StatusFilterWrapper.class + " properties resource");
            statusFilterPeriodMillis = 1000 * (long) statusFilterPeriodSeconds;
        } catch (RuntimeException ex) {
            if (Defines.DEBUG && CAT.isEnabledFor(org.apache.log4j.Level.ERROR)) CAT.error("ex in static init", ex);
            throw ex;
        } catch (Throwable tr) {
            if (Defines.DEBUG && CAT.isEnabledFor(org.apache.log4j.Level.ERROR)) CAT.error("ex in static init", tr);
            throw new RuntimeException("" + tr);
        }
    }

    public void addMessagingNetworkListener(final MessagingNetworkListener l) {
        if ((l) == null) Lang.ASSERT_NOT_NULL(l, "l");
        final MessagingNetworkWrappable wrapped = StatusFilterWrapper.wrapped;
        if ((wrapped) == null) Lang.ASSERT_NOT_NULL(wrapped, "wrapped");
        if (!REQPARAM_STATUS_FILTER_USED) {
            wrapped.addMessagingNetworkListener(l);
        } else {
            final MessagingNetworkListener myL = new MessagingNetworkListener() {

                public void authorizationResponse(byte networkId, String srcLoginId, String dstLoginId, boolean grant) {
                    l.authorizationResponse(networkId, srcLoginId, dstLoginId, grant);
                }

                public void authorizationRequest(byte networkId, String srcLoginId, String dstLoginId, String reason) {
                    l.authorizationRequest(networkId, srcLoginId, dstLoginId, reason);
                }

                public void messageReceived(byte networkId, String srcLoginId, String dstLoginId, String text) {
                    l.messageReceived(networkId, srcLoginId, dstLoginId, text);
                }

                public void contactsReceived(byte networkId, String from, String to, String[] contactsUins, String[] contactsNicks) {
                    l.contactsReceived(networkId, from, to, contactsUins, contactsNicks);
                }

                public void sendMessageFailed(byte networkId, long operationId, String originalMessageSrcLoginId, String originalMessageDstLoginId, String originalMessageText, MessagingNetworkException ex) {
                    l.sendMessageFailed(networkId, operationId, originalMessageSrcLoginId, originalMessageDstLoginId, originalMessageText, ex);
                }

                public void sendMessageSuccess(byte networkId, long operationId, String originalMessageSrcLoginId, String originalMessageDstLoginId, String originalMessageText) {
                    l.sendMessageSuccess(networkId, operationId, originalMessageSrcLoginId, originalMessageDstLoginId, originalMessageText);
                }

                public void getUserDetailsFailed(byte networkId, long operationId, String originalSrcLoginId, String originalDstLoginId, MessagingNetworkException ex) {
                    l.getUserDetailsFailed(networkId, operationId, originalSrcLoginId, originalDstLoginId, ex);
                }

                public void getUserDetailsSuccess(byte networkId, long operationId, String originalSrcLoginId, String originalDstLoginId, UserDetails userDetails) {
                    l.getUserDetailsSuccess(networkId, operationId, originalSrcLoginId, originalDstLoginId, userDetails);
                }

                public void setStatusFailed(byte networkId, long operationId, String originalSrcLoginId, MessagingNetworkException ex) {
                    l.setStatusFailed(networkId, operationId, originalSrcLoginId, ex);
                }

                public void statusChanged(byte networkId, String srcLoginId, String dstLoginId, int status, int reasonLogger, String reasonMessage, int endUserReasonCode) {
                    if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("statusChanged(): srcLoginId=" + srcLoginId);
                    if ((wrapped) == null) Lang.ASSERT_NOT_NULL(wrapped, "wrapped");
                    if (srcLoginId.equals(dstLoginId)) {
                        l.statusChanged(networkId, srcLoginId, dstLoginId, status, reasonLogger, reasonMessage, endUserReasonCode);
                        return;
                    }
                    Session ses = (Session) wrapped.getSession(srcLoginId);
                    if (ses == null) {
                        if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("session for " + srcLoginId + " is null, ignoring statusChanged (...) event.");
                        return;
                    }
                    synchronized (ses.getFireStatusLock()) {
                        MessagingNetworkContactListItem cli = ses.getContactListItem(dstLoginId);
                        if (cli == null) {
                            if (Defines.DEBUG && CAT.isEnabledFor(org.apache.log4j.Level.ERROR)) CAT.error("dst " + dstLoginId + " deleted from the [" + srcLoginId + "]'s contact list, statusChange event ignored");
                            return;
                        }
                        long scheduledTimeMillis = cli.getScheduledStatusChangeSendTimeMillis();
                        long lastChangeTimeMillis = cli.getContactStatusLastChangeTimeMillis();
                        long now = System.currentTimeMillis();
                        if (now >= scheduledTimeMillis || now >= lastChangeTimeMillis + statusFilterPeriodMillis) {
                            cli.setScheduledStatusChangeSendTimeMillis(Long.MAX_VALUE);
                            cli.setContactStatusLastChangeTimeMillis(now);
                            l.statusChanged(networkId, srcLoginId, dstLoginId, status, reasonLogger, reasonMessage, endUserReasonCode);
                            if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("statusChanged () sent by statusfilter: srcLoginId=" + srcLoginId + " dst=" + dstLoginId + " status=" + StatusUtilMim.translateStatusMimToString(status));
                        } else {
                            if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("statusChanged () delayed by statusfilter (until " + new Date(lastChangeTimeMillis + statusFilterPeriodMillis) + "): srcLoginId=" + srcLoginId + " dst=" + dstLoginId + " status=" + StatusUtilMim.translateStatusMimToString(status));
                            cli.setScheduledStatusChangeSendTimeMillis(lastChangeTimeMillis + statusFilterPeriodMillis);
                        }
                    }
                }
            };
            try {
                removeMessagingNetworkListener0(l, false);
            } catch (MessagingNetworkException ex) {
                if (Defines.DEBUG && CAT.isEnabledFor(org.apache.log4j.Level.ERROR)) CAT.error("removeMessagingNetworkListener() exception ignored", ex);
            }
            extListener2myListener.put(l, myL);
            wrapped.addMessagingNetworkListener(myL);
        }
    }

    public void addToContactList(String srcLoginId, String dstLoginId) throws MessagingNetworkException {
        wrapped.addToContactList(srcLoginId, dstLoginId);
    }

    public int getClientStatus(String srcLoginId) throws MessagingNetworkException {
        return wrapped.getClientStatus(srcLoginId);
    }

    public String getComment() {
        return wrapped.getComment();
    }

    public String getName() {
        return wrapped.getName();
    }

    public int getStatus(String srcLoginId, String dstLoginId) throws MessagingNetworkException {
        return wrapped.getStatus(srcLoginId, dstLoginId);
    }

    public void login(String srcLoginId, String password, String[] contactList, int status) throws MessagingNetworkException {
        wrapped.login(srcLoginId, password, contactList, status);
    }

    public void logout(String srcLoginId) throws MessagingNetworkException {
        wrapped.logout(srcLoginId);
    }

    public void logout(String srcLoginId, int endUserReason) throws MessagingNetworkException {
        wrapped.logout(srcLoginId, endUserReason);
    }

    public void removeFromContactList(String srcLoginId, String dstLoginId) throws MessagingNetworkException {
        wrapped.removeFromContactList(srcLoginId, dstLoginId);
    }

    public void removeMessagingNetworkListener(MessagingNetworkListener l) throws MessagingNetworkException {
        if (!REQPARAM_STATUS_FILTER_USED) {
            wrapped.removeMessagingNetworkListener(l);
        } else {
            removeMessagingNetworkListener0(l, true);
        }
    }

    private void removeMessagingNetworkListener0(MessagingNetworkListener l, boolean reportNonExistingLs) throws MessagingNetworkException {
        if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("removeMessagingNetworkListener(): " + l);
        if ((l) == null) Lang.ASSERT_NOT_NULL(l, "l");
        MessagingNetworkListener myL = (MessagingNetworkListener) extListener2myListener.remove(l);
        if (myL == null) {
            if (reportNonExistingLs) if (Defines.DEBUG && CAT.isDebugEnabled()) CAT.debug("IMListener " + l + " was not registered, removeMNL() request ignored.");
            return;
        }
        MessagingNetwork wrapped = StatusFilterWrapper.wrapped;
        if ((wrapped) == null) Lang.ASSERT_NOT_NULL(wrapped, "wrapped");
        wrapped.removeMessagingNetworkListener(myL);
    }

    public void sendContacts(String srcLoginId, String dstLoginId, String[] nicks, String[] loginIds) throws MessagingNetworkException {
        wrapped.sendContacts(srcLoginId, dstLoginId, nicks, loginIds);
    }

    public boolean isAuthorizationRequired(String srcLoginId, String dstLoginId) throws MessagingNetworkException {
        return wrapped.isAuthorizationRequired(srcLoginId, dstLoginId);
    }

    public void authorizationRequest(String srcLoginId, String dstLoginId, String reason) throws MessagingNetworkException {
        wrapped.authorizationRequest(srcLoginId, dstLoginId, reason);
    }

    public void authorizationResponse(String srcLogin, String dstLogin, boolean grant) throws MessagingNetworkException {
        wrapped.authorizationResponse(srcLogin, dstLogin, grant);
    }

    /**
    src src status not filtered
  */
    public void setClientStatus(String srcLoginId, int status, int endUserReason) throws MessagingNetworkException {
        wrapped.setClientStatus(srcLoginId, status, endUserReason);
    }

    public void setClientStatus(String srcLoginId, int status) throws MessagingNetworkException {
        wrapped.setClientStatus(srcLoginId, status);
    }

    public UserDetails getUserDetails(String srcLoginId, String dstLoginId) throws MessagingNetworkException {
        return wrapped.getUserDetails(srcLoginId, dstLoginId);
    }

    public UserSearchResults searchUsers(String srcLoginId, String emailSearchPattern, String nickSearchPattern, String firstNameSearchPattern, String lastNameSearchPattern) throws MessagingNetworkException {
        return wrapped.searchUsers(srcLoginId, emailSearchPattern, nickSearchPattern, firstNameSearchPattern, lastNameSearchPattern);
    }

    public long startLogin(String srcLoginId, String password, String[] contactList, int status) throws MessagingNetworkException {
        return wrapped.startLogin(srcLoginId, password, contactList, status);
    }

    public long startSendMessage(String srcLoginId, String dstLoginId, String text) throws MessagingNetworkException {
        return wrapped.startSendMessage(srcLoginId, dstLoginId, text);
    }

    public long startGetUserDetails(String srcLoginId, String dstLoginId) throws MessagingNetworkException {
        return wrapped.startGetUserDetails(srcLoginId, dstLoginId);
    }

    public void init() {
        wrapped.init();
    }

    public void deinit() {
        extListener2myListener.clear();
        wrapped.deinit();
    }
}
