package com.ham.mud.commands;

import com.ham.mud.Colors;
import com.ham.mud.ServerConnection;
import com.ham.mud.items.EquipSlot;
import com.ham.mud.items.Item;
import java.util.Map;

/**
 * Created by hlucas on Jun 21, 2011 at 3:03:14 PM
 */
public class EquipmentSlotsCommand extends PlayerCommand {

    @Override
    public void execute(ServerConnection connection, String[] params) {
        connection.printStart("Your equipment:");
        Map<EquipSlot, Item> eq = connection.getPlayer().getEquipment();
        EquipSlot[] slots = EquipSlot.values();
        for (EquipSlot slot : slots) {
            Item item = eq.get(slot);
            if (item != null) {
                connection.println("    <" + slot.getName() + ">" + Colors.getSpaces(12, slot.getName()) + item.getName() + item.getAffects());
            } else {
                connection.println("    <" + slot.getName() + ">" + Colors.getSpaces(12, slot.getName()) + "<nothing>");
            }
        }
        connection.printEnd();
    }

    @Override
    protected String getCommandName() {
        return "equipslots";
    }
}
