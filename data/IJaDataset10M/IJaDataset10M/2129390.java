package game.packets.server;

import game.GameEngine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SystemMessagePack extends ServerBasePack {

    private static final int TYPE_SKILL_NAME = 4;

    private static final int TYPE_ITEM_NAME = 3;

    private static final int TYPE_NPC_NAME = 2;

    private static final int TYPE_NUMBER = 1;

    private static final int TYPE_TEXT = 0;

    private SystemMessagePack() {
    }

    public static void runImplementatiton(GameEngine gameEngine, byte data[]) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.position(1);
        int messageId = buf.getInt();
        int typesSize = buf.getInt();
        gameEngine.getVisualInterface().putMessage("SYSTEM MSG_ID=" + messageId, GameEngine.MSG_SYSTEM_NORMAL, false);
        String msg;
        int itemId;
        int skillId;
        for (int i = 0; i < typesSize; i++) {
            int t = buf.getInt();
            switch(t) {
                case TYPE_TEXT:
                    {
                        msg = readS(buf);
                        break;
                    }
                case TYPE_NUMBER:
                case TYPE_NPC_NAME:
                case TYPE_ITEM_NAME:
                    {
                        itemId = buf.getInt();
                        break;
                    }
                case TYPE_SKILL_NAME:
                    {
                        skillId = buf.getInt();
                        break;
                    }
            }
        }
    }
}
