package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

public class MoveItemPacketHandler implements PacketHandler {

    private final int MOVE_ITEMS = 214;

    @Override
    public void handle(Player player, Packet packet) {
        switch(packet.getOpcode()) {
            case MOVE_ITEMS:
                moveItems(player, packet);
                break;
        }
    }

    private void moveItems(Player player, Packet packet) {
        int interfaceId = packet.getLEShortA();
        switch(interfaceId) {
            case 3214:
                packet.getByteC();
                int itemFrom = packet.getLEShortA(), itemTo = packet.getLEShort();
                if (itemFrom < 0 || itemFrom > 27 || itemTo < 0 || itemTo > 27) {
                    break;
                }
                Item from = player.getInventory().get(itemFrom), to = player.getInventory().get(itemTo);
                if (from == null) {
                    break;
                }
                player.getInventory().set(itemFrom, to);
                player.getInventory().set(itemTo, from);
                break;
        }
    }
}
