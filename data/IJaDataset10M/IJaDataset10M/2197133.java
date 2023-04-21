package net.sf.odinms.client.messages.commands;

import java.rmi.RemoteException;
import static net.sf.odinms.client.messages.CommandProcessor.getNamedIntArg;
import static net.sf.odinms.client.messages.CommandProcessor.joinAfterString;
import java.text.DateFormat;
import java.util.Calendar;
import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.MapleCharacterUtil;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.client.messages.Command;
import net.sf.odinms.client.messages.CommandDefinition;
import net.sf.odinms.client.messages.IllegalCommandSyntaxException;
import net.sf.odinms.client.messages.MessageCallback;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.tools.StringUtil;

public class BanningCommands implements Command {

    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception {
        ChannelServer cserv = c.getChannelServer();
        if (splitted[0].equals("!ban")) {
            if (splitted.length < 3) {
                throw new IllegalCommandSyntaxException(3);
            }
            String originalReason = StringUtil.joinStringFrom(splitted, 2);
            String reason = c.getPlayer().getName() + " banned " + splitted[1] + ": " + originalReason;
            MapleCharacter target = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (target != null) {
                String readableTargetName = MapleCharacterUtil.makeMapleReadable(target.getName());
                String ip = target.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                reason += " (IP: " + ip + ")";
                target.ban(reason);
                cserv.broadcastPacket(MaplePacketCreator.serverNotice(6, readableTargetName + " has been banned for " + originalReason));
                mc.dropMessage(readableTargetName + "'s IP: " + ip + "!");
            } else {
                if (MapleCharacter.ban(splitted[1], reason, false)) {
                    @SuppressWarnings("unused") String readableTargetName = MapleCharacterUtil.makeMapleReadable(target.getName());
                    String ip = target.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                    reason += " (IP: " + ip + ")";
                    cserv.broadcastPacket(MaplePacketCreator.serverNotice(6, readableTargetName + " has been banned for " + originalReason));
                } else {
                    mc.dropMessage("Failed to ban " + splitted[1]);
                }
            }
        } else if (splitted[0].equals("!tempban")) {
            Calendar tempB = Calendar.getInstance();
            String originalReason = joinAfterString(splitted, ":");
            if (splitted.length < 4 || originalReason == null) {
                throw new IllegalCommandSyntaxException(4);
            }
            int yChange = getNamedIntArg(splitted, 1, "y", 0);
            int mChange = getNamedIntArg(splitted, 1, "m", 0);
            int wChange = getNamedIntArg(splitted, 1, "w", 0);
            int dChange = getNamedIntArg(splitted, 1, "d", 0);
            int hChange = getNamedIntArg(splitted, 1, "h", 0);
            int iChange = getNamedIntArg(splitted, 1, "i", 0);
            int gReason = getNamedIntArg(splitted, 1, "r", 7);
            String reason = c.getPlayer().getName() + " tempbanned " + splitted[1] + ": " + originalReason;
            if (gReason > 14) {
                mc.dropMessage("You have entered an incorrect ban reason ID, please try again.");
                return;
            }
            DateFormat df = DateFormat.getInstance();
            tempB.set(tempB.get(Calendar.YEAR) + yChange, tempB.get(Calendar.MONTH) + mChange, tempB.get(Calendar.DATE) + (wChange * 7) + dChange, tempB.get(Calendar.HOUR_OF_DAY) + hChange, tempB.get(Calendar.MINUTE) + iChange);
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim == null) {
                int accId = MapleClient.findAccIdForCharacterName(splitted[1]);
                if (accId >= 0 && MapleCharacter.tempban(reason, tempB, gReason, accId)) {
                    String readableTargetName = MapleCharacterUtil.makeMapleReadable(victim.getName());
                    cserv.broadcastPacket(MaplePacketCreator.serverNotice(6, readableTargetName + " has been banned for " + originalReason));
                } else {
                    mc.dropMessage("There was a problem offline banning character " + splitted[1] + ".");
                }
            } else {
                victim.tempban(reason, tempB, gReason);
                mc.dropMessage("The character " + splitted[1] + " has been successfully tempbanned till " + df.format(tempB.getTime()));
            }
        } else if (splitted[0].equals("!dc")) {
            int level = 0;
            MapleCharacter victim;
            if (splitted[1].charAt(0) == '-') {
                level = StringUtil.countCharacters(splitted[1], 'f');
                victim = cserv.getPlayerStorage().getCharacterByName(splitted[2]);
            } else {
                victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            }
            if (level < 2) {
                victim.getClient().getSession().close();
                if (level >= 1) {
                    victim.getClient().disconnect();
                }
            } else {
                mc.dropMessage("Please use dc -f instead.");
            }
        }
    }

    @Override
    public CommandDefinition[] getDefinition() {
        return new CommandDefinition[] { new CommandDefinition("ban", "charname reason", "Permanently ip, mac and accountbans the given character", 1), new CommandDefinition("tempban", "<name> [i / m / w / d / h] <amount> [r  [reason id] : Text Reason", "Tempbans the given account", 1), new CommandDefinition("dc", "[-f] name", "Disconnects player matching name provided. Use -f only if player is persistant!", 1) };
    }
}
