package net.solosky.maplefetion.client.dispatcher;

import net.solosky.maplefetion.ExceptionHandler;
import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.client.notify.AckNotifyHandler;
import net.solosky.maplefetion.client.notify.BuddyPresenceNotifyHandler;
import net.solosky.maplefetion.client.notify.ByeNotifyHandler;
import net.solosky.maplefetion.client.notify.ContactNotifyHandler;
import net.solosky.maplefetion.client.notify.ConversationNotifyHandler;
import net.solosky.maplefetion.client.notify.FetionShowNotifyHandler;
import net.solosky.maplefetion.client.notify.GroupNotifyHandler;
import net.solosky.maplefetion.client.notify.GroupPresenceNotifyHandler;
import net.solosky.maplefetion.client.notify.InviteBuddyNotifyHandler;
import net.solosky.maplefetion.client.notify.MessageNotifyHandler;
import net.solosky.maplefetion.client.notify.OptionNotifyHandler;
import net.solosky.maplefetion.client.notify.PermissionNotifyHandler;
import net.solosky.maplefetion.client.notify.RegistrationNotifyHandler;
import net.solosky.maplefetion.client.notify.SyncUserInfoNotifyHandler;
import net.solosky.maplefetion.client.notify.SystemNotifyHandler;
import net.solosky.maplefetion.sipc.SipcHeader;
import net.solosky.maplefetion.sipc.SipcMethod;
import net.solosky.maplefetion.sipc.SipcNotify;
import org.apache.log4j.Logger;

/**
 *
 * 这个通知处理器处理飞信服务器发回的通知
 *
 * @author solosky <solosky772@qq.com>
 */
public class ServerMessageDispatcher extends AbstractMessageDispatcher {

    private static Logger logger = Logger.getLogger(ServerMessageDispatcher.class);

    /**
     * @param client
     * @param dialog
     * @param exceptionHandler
     */
    public ServerMessageDispatcher(FetionContext client, Dialog dialog, ExceptionHandler exceptionHandler) {
        super(client, dialog, exceptionHandler);
    }

    @Override
    protected Class findNotifyHandlerClass(SipcNotify notify) {
        if (notify == null) return null;
        String method = notify.getMethod();
        if (method == null) {
            logger.warn("Unknown Notify method:[" + notify + "]");
        }
        Class clazz = null;
        if (method.equals(SipcMethod.BENOTIFY)) {
            SipcHeader eventHeader = notify.getHeader(SipcHeader.EVENT);
            if (eventHeader == null || eventHeader.getValue() == null) {
                logger.warn("Unknown Notify event:[" + notify + "]");
                return null;
            }
            String event = notify.getHeader(SipcHeader.EVENT).getValue();
            if (event.equals("PresenceV4")) {
                clazz = BuddyPresenceNotifyHandler.class;
            } else if (event.equals("PGPresence")) {
                clazz = GroupPresenceNotifyHandler.class;
            } else if (event.equals("PGGroup")) {
                clazz = GroupNotifyHandler.class;
            } else if (event.equals("contact")) {
                clazz = ContactNotifyHandler.class;
            } else if (event.equals("Conversation")) {
                clazz = ConversationNotifyHandler.class;
            } else if (event.equals("registration")) {
                clazz = RegistrationNotifyHandler.class;
            } else if (event.equals("compact")) {
            } else if (event.equals("permission")) {
                clazz = PermissionNotifyHandler.class;
            } else if (event.equals("SystemNotifyV4")) {
                clazz = SystemNotifyHandler.class;
            } else if (event.equals("SyncUserInfoV4")) {
                clazz = SyncUserInfoNotifyHandler.class;
            } else {
            }
        } else if (method.equals(SipcMethod.MESSAGE)) {
            clazz = MessageNotifyHandler.class;
        } else if (method.equals(SipcMethod.INVATE)) {
            clazz = InviteBuddyNotifyHandler.class;
        } else if (method.equals(SipcMethod.INFO)) {
            clazz = FetionShowNotifyHandler.class;
        } else if (method.equals(SipcMethod.ACK)) {
            clazz = AckNotifyHandler.class;
        } else if (method.equals(SipcMethod.OPTION)) {
            clazz = OptionNotifyHandler.class;
        } else if (method.equals(SipcMethod.BYE)) {
            clazz = ByeNotifyHandler.class;
        }
        return clazz;
    }
}
