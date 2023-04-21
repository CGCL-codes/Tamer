package l1j.server.server.model;

import java.util.logging.Logger;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1CastleDoorInstance;
import l1j.server.server.ActionCodes;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_Door;

public class L1CDA {

    private static Logger _log = Logger.getLogger(L1CDA.class.getName());

    public static void CDA(L1CastleDoorInstance door) {
        int att = 0;
        if (!door.isDead() || door.getOpenStatus() == ActionCodes.ACTION_Close) {
            att = 65;
        }
        if (door.isDead() || door.getOpenStatus() == ActionCodes.ACTION_Open) {
            att = 0;
        }
        switch(door.getDirection()) {
            case 0:
                for (int i = 0; i < 4; i++) {
                    L1World.getInstance().broadcastPacketToAll(new S_Door(door, door.getEntranceX(), door.getEntranceY() - i, 1, att));
                    L1World.getInstance().broadcastPacketToAll(new S_Door(door, door.getEntranceX(), door.getEntranceY() + i, 1, att));
                }
                break;
            case 1:
                for (int i = 0; i < 4; i++) {
                    L1World.getInstance().broadcastPacketToAll(new S_Door(door, door.getEntranceX() - i, door.getEntranceY(), 0, att));
                    L1World.getInstance().broadcastPacketToAll(new S_Door(door, door.getEntranceX() + i, door.getEntranceY(), 0, att));
                }
                break;
        }
    }
}
