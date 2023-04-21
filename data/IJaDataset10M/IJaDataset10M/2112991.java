package com.epicsagaonline.bukkit.EpicZones.commands;

import com.epicsagaonline.bukkit.EpicZones.General;
import com.epicsagaonline.bukkit.EpicZones.Message;
import com.epicsagaonline.bukkit.EpicZones.Message.Message_ID;
import com.epicsagaonline.bukkit.EpicZones.commands.EZZoneHelp.ZoneCommand;
import com.epicsagaonline.bukkit.EpicZones.objects.EpicZonePlayer;
import com.epicsagaonline.bukkit.EpicZones.objects.EpicZonePlayer.EpicZoneMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EZZoneDelete {

    public EZZoneDelete(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            EpicZonePlayer ezp = General.getPlayer(player.getName());
            if (ezp.getAdmin()) {
                if (ezp.getMode() == EpicZoneMode.ZoneEdit) {
                    ezp.setMode(EpicZoneMode.ZoneDeleteConfirm);
                    Message.Send(sender, Message_ID.Info_00104_Mode_ZoneDelete, new String[] { ezp.getEditZone().getTag() });
                }
            } else {
                new EZZoneHelp(ZoneCommand.DELETE, sender, ezp);
            }
        }
    }
}
