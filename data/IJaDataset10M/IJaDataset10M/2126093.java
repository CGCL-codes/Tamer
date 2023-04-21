package net.timeslicer.jabber;

import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.Occupant;

public class RoomUtil {

    private static class LazyHolder {

        private static final RoomUtil roomUtil = new RoomUtil();
    }

    public static RoomUtil getInstance() {
        return LazyHolder.roomUtil;
    }

    public Occupant getSender(Packet packet) {
        return ModThread.room.getOccupant(packet.getFrom());
    }

    public void kickSpammer(String sender) {
        try {
            ModThread.room.kickParticipant(sender, "Spamming the room.");
        } catch (Exception ignored) {
        }
    }

    public void registerBackwardsText(final Occupant occ) {
        try {
            ModThread.room.sendMessage("‭ Fix applied for backwards text sent by " + occ.getNick());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String nick = occ.getNick();
            ModThread.room.revokeVoice(nick);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
