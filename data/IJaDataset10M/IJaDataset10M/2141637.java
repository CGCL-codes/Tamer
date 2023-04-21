package admincommands;

import nakayo.gameserver.configs.administration.AdminConfig;
import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.model.Race;
import nakayo.gameserver.network.aion.serverpackets.SM_PLAYER_SPAWN;
import nakayo.gameserver.services.TeleportService;
import nakayo.gameserver.utils.PacketSendUtility;
import nakayo.gameserver.world.World;
import nakayo.gameserver.utils.chathandlers.AdminCommand;
import nakayo.gameserver.utils.i18n.CustomMessageId;
import nakayo.gameserver.utils.i18n.LanguageHandler;

/**
 * @author Alfa
 * 
 */
public class Startevent extends AdminCommand {

    /**
    * Constructor.
    */
    public Startevent() {
        super("startevent");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_STARTEVENT) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }
        if (params.length < 3) {
            PacketSendUtility.sendMessage(admin, "syntax //startevent <Check isLookingforevent 0:False|1:True> <0:Elyos|1:Asmos|3:All> <0:Start|1:End>");
            return;
        }
        int playerscount = 0;
        World world = admin.getActiveRegion().getWorld();
        switch(Integer.parseInt(params[1])) {
            case 3:
                for (final Player p : World.getInstance().getPlayers()) {
                    if (p.equals(admin)) continue;
                    if (Integer.parseInt(params[2]) == 0) {
                        TeleportService.teleportTo(p, admin.getWorldId(), admin.getInstanceId(), admin.getX(), admin.getY(), admin.getZ(), admin.getHeading(), 5);
                        playerscount++;
                        PacketSendUtility.sendPacket(p, new SM_PLAYER_SPAWN(p));
                        PacketSendUtility.sendMessage(p, "Teleported for the event by " + admin.getName() + ".");
                    } else {
                        if (p.isLookingForEvent() && p.getCommonData().getRace().getRaceId() == Integer.parseInt(params[1])) {
                            TeleportService.moveToBindLocation(p, true);
                            playerscount++;
                            PacketSendUtility.sendMessage(p, "Teleported to bind point by " + admin.getName() + ".");
                            p.setLookingForEvent(false);
                            PacketSendUtility.sendMessage(p, "You are no longer waiting for event.");
                        }
                    }
                }
            default:
                switch(Integer.parseInt(params[0])) {
                    case 0:
                        for (final Player p : World.getInstance().getPlayers()) {
                            if (p.equals(admin)) continue;
                            if (Integer.parseInt(params[2]) == 0) {
                                if (p.getCommonData().getRace().getRaceId() == Integer.parseInt(params[1])) {
                                    TeleportService.teleportTo(p, admin.getWorldId(), admin.getInstanceId(), admin.getX(), admin.getY(), admin.getZ(), admin.getHeading(), 5);
                                    playerscount++;
                                    PacketSendUtility.sendPacket(p, new SM_PLAYER_SPAWN(p));
                                    PacketSendUtility.sendMessage(p, "Teleported for the event by " + admin.getName() + ".");
                                }
                            } else {
                                if (p.isLookingForEvent() && p.getCommonData().getRace().getRaceId() == Integer.parseInt(params[1])) {
                                    TeleportService.moveToBindLocation(p, true);
                                    playerscount++;
                                    PacketSendUtility.sendMessage(p, "Teleported to bind point by " + admin.getName() + ".");
                                    p.setLookingForEvent(false);
                                    PacketSendUtility.sendMessage(p, "You are no longer waiting for event.");
                                }
                            }
                        }
                    case 1:
                        for (final Player p : World.getInstance().getPlayers()) {
                            if (p.equals(admin)) continue;
                            if (Integer.parseInt(params[2]) == 0) {
                                if (p.isLookingForEvent() && p.getCommonData().getRace().getRaceId() == Integer.parseInt(params[1])) {
                                    TeleportService.teleportTo(p, admin.getWorldId(), admin.getInstanceId(), admin.getX(), admin.getY(), admin.getZ(), admin.getHeading(), 5);
                                    playerscount++;
                                    PacketSendUtility.sendPacket(p, new SM_PLAYER_SPAWN(p));
                                    PacketSendUtility.sendMessage(p, "Teleported for the event by " + admin.getName() + ".");
                                }
                            } else {
                                if (p.isLookingForEvent() && p.getCommonData().getRace().getRaceId() == Integer.parseInt(params[0])) {
                                    TeleportService.moveToBindLocation(p, true);
                                    playerscount++;
                                    PacketSendUtility.sendMessage(p, "Teleported to bind point by " + admin.getName() + ".");
                                    p.setLookingForEvent(false);
                                    PacketSendUtility.sendMessage(p, "You are no longer waiting for event.");
                                }
                            }
                        }
                }
        }
        PacketSendUtility.sendMessage(admin, playerscount + " players teleported.");
    }
}
