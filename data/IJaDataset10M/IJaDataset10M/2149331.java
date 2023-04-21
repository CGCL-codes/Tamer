package net.kano.joustsim.oscar.oscar.service.chatrooms;

import net.kano.joscar.OscarTools;
import net.kano.joscar.rv.RvSession;
import net.kano.joscar.snaccmd.FullRoomInfo;
import net.kano.joustsim.Screenname;
import javax.crypto.SecretKey;
import java.security.cert.X509Certificate;

class ChatInvitationImpl implements ChatInvitation {

    private final ChatRoomManager chatRoomManager;

    private final RvSession session;

    private final Screenname screenname;

    private final FullRoomInfo roomInfo;

    private final String message;

    private final boolean secure;

    private SecretKey roomKey = null;

    private X509Certificate buddySignature = null;

    private InvalidInvitationReason invalidReason = null;

    private boolean rejected = false;

    public ChatInvitationImpl(ChatRoomManager chatRoomManager, RvSession session, Screenname screenname, FullRoomInfo roomInfo, String message) {
        this(chatRoomManager, session, screenname, roomInfo, message, false);
    }

    private ChatInvitationImpl(ChatRoomManager chatRoomManager, RvSession session, Screenname screenname, FullRoomInfo roomInfo, String message, boolean secure) {
        this.chatRoomManager = chatRoomManager;
        this.screenname = screenname;
        this.roomInfo = roomInfo;
        this.message = message;
        this.secure = secure;
        this.session = session;
    }

    public ChatInvitationImpl(ChatRoomManager chatRoomManager, RvSession session, Screenname screenname, FullRoomInfo roomInfo, X509Certificate buddySignature, SecretKey roomKey, String message) {
        this(chatRoomManager, session, screenname, roomInfo, message, true);
        this.roomKey = roomKey;
        this.buddySignature = buddySignature;
    }

    public ChatInvitationImpl(ChatRoomManager chatRoomManager, RvSession session, Screenname screenname, FullRoomInfo roomInfo, InvalidInvitationReason reason, String message) {
        this(chatRoomManager, session, screenname, roomInfo, message, true);
        this.invalidReason = reason;
    }

    public RvSession getSession() {
        return session;
    }

    public Screenname getScreenname() {
        return screenname;
    }

    public FullRoomInfo getRoomInfo() {
        return roomInfo;
    }

    public int getRoomExchange() {
        return getRoomInfo().getExchange();
    }

    public String getRoomName() {
        return OscarTools.getRoomNameFromCookie(getRoomInfo().getCookie());
    }

    public X509Certificate getBuddySignature() {
        return buddySignature;
    }

    public SecretKey getRoomKey() {
        return roomKey;
    }

    public String getMessage() {
        return message;
    }

    public InvalidInvitationReason getInvalidReason() {
        return invalidReason;
    }

    public ChatRoomManager getChatRoomManager() {
        return chatRoomManager;
    }

    public boolean isValid() {
        return !isForSecureChatRoom() || roomKey != null;
    }

    public boolean isForSecureChatRoom() {
        return secure;
    }

    public ChatRoomSession accept() {
        return chatRoomManager.acceptInvitation(this);
    }

    public void reject() {
        chatRoomManager.rejectInvitation(this);
    }

    public String toString() {
        return "ChatInvitationImpl{" + "screenname=" + screenname + ", roomInfo=" + roomInfo + ", message='" + message + "'" + ", secure=" + secure + ", invalidReason=" + invalidReason + "}";
    }

    public synchronized boolean setRejected() {
        if (rejected) return false;
        rejected = true;
        return true;
    }

    public synchronized boolean isRejected() {
        return rejected;
    }

    public boolean isAcceptable() {
        return isValid() && !isRejected();
    }
}
