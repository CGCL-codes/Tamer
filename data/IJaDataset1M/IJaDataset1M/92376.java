package com.epicsagaonline.bukkit.EpicZones.commands;

import com.epicsagaonline.bukkit.EpicZones.General;
import com.epicsagaonline.bukkit.EpicZones.Message;
import com.epicsagaonline.bukkit.EpicZones.Message.Message_ID;
import com.epicsagaonline.bukkit.EpicZones.commands.EZZoneHelp.ZoneCommand;
import com.epicsagaonline.bukkit.EpicZones.objects.EpicZone;
import com.epicsagaonline.bukkit.EpicZones.objects.EpicZonePlayer;
import com.epicsagaonline.bukkit.EpicZones.objects.EpicZonePlayer.EpicZoneMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EZZoneParent {

    public EZZoneParent(String[] data, CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            EpicZonePlayer ezp = General.getPlayer(player.getName());
            if (ezp.getAdmin()) {
                if (ezp.getMode() == EpicZoneMode.ZoneEdit) {
                    if (data.length > 2) {
                        String cmd = data[1];
                        for (int i = 2; i < data.length; i++) {
                            String tag = data[i].replaceAll("[^a-zA-Z0-9_]", "");
                            if (tag.length() > 0) {
                                EpicZone zone = General.myZones.get(tag);
                                if (zone != null) {
                                    if (cmd.equalsIgnoreCase("add")) {
                                        zone.addChildTag(tag);
                                    } else if (cmd.equalsIgnoreCase("remove")) {
                                        zone.removeChild(tag);
                                    }
                                }
                            }
                        }
                        Message.Send(sender, Message_ID.Info_00027_ParentsUpdated);
                    }
                }
            } else {
                new EZZoneHelp(ZoneCommand.CHILD, sender, ezp);
            }
        }
    }
}
