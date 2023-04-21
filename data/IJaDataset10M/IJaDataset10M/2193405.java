package growl.delegate.growltalk;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NotificationPacket implements GrowlPacket {

    private GrowlAuthentication auth;

    private String applicationName;

    private GrowlTalkPriority priority;

    private boolean sticky;

    private String notificationType;

    private String title;

    private String description;

    public NotificationPacket(GrowlAuthentication auth, String applicationName, GrowlTalkPriority priority, boolean sticky, String notificationType, String title, String description) {
        this.auth = auth;
        this.applicationName = applicationName;
        this.priority = priority;
        this.sticky = sticky;
        this.notificationType = notificationType;
        this.title = title;
        this.description = description;
    }

    public GrowlTalkVersion getVersion() {
        return auth.getProtocolVersion();
    }

    public GrowlTalkPacketType getType() {
        return auth.getNotificationType();
    }

    public String getApplicationName() {
        return applicationName;
    }

    public GrowlTalkPriority getPriority() {
        return priority;
    }

    public boolean isSticky() {
        return sticky;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public byte[] asMessageBytes() throws IOException {
        byte[] applicationNameBytes = Util.toUTF8(applicationName);
        byte[] notificationTypeBytes = Util.toUTF8(notificationType);
        byte[] titleBytes = Util.toUTF8(title);
        byte[] descriptionBytes = Util.toUTF8(description);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream daos = new DataOutputStream(baos);
        daos.writeByte(auth.getProtocolVersion().getVersionId());
        daos.writeByte(auth.getNotificationType().getTypeId());
        daos.writeShort(deriveFlags());
        daos.writeShort(notificationTypeBytes.length);
        daos.writeShort(titleBytes.length);
        daos.writeShort(descriptionBytes.length);
        daos.writeShort(applicationNameBytes.length);
        daos.write(notificationTypeBytes);
        daos.write(titleBytes);
        daos.write(descriptionBytes);
        daos.write(applicationNameBytes);
        daos.flush();
        byte[] message = baos.toByteArray();
        daos.write(auth.generateChecksum(message));
        daos.flush();
        return baos.toByteArray();
    }

    private int deriveFlags() {
        int flags = (priority.getPriorityCode() & 0x7) << 9;
        if (sticky) {
            flags |= 0x100;
        }
        return flags;
    }
}
