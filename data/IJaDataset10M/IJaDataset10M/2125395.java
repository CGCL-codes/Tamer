package gameserver.model.instances;

import gameserver.configs.EventConfig;
import gameserver.controllers.instances.TvtController;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.WorldMapInstance;
import javolution.util.FastMap;
import java.util.Map;

public class Tvt {

    private int elyos = 0;

    private int asmos = 0;

    private Race winnerRace = null;

    private Map<Player, Integer> participants = new FastMap<Player, Integer>();

    public synchronized boolean addPlayer(Player player) {
        if (player == null) {
            return false;
        }
        Race playerRace = player.getCommonData().getRace();
        if (playerRace == Race.ASMODIANS) {
            if (asmos == EventConfig.ASMO_MAX_PLAYERS_TVT) return false; else asmos++;
        } else if (playerRace == Race.ELYOS) {
            if (elyos == EventConfig.ELYOS_MAX_PLAYERS_TVT) return false; else elyos++;
        }
        participants.put(player, 0);
        return true;
    }

    public void setWinnerRace(Race race) {
        this.winnerRace = race;
    }

    public Race getWinnerRace() {
        if (winnerRace != null) return this.winnerRace; else {
            int asmosPoints = 0;
            int elyosPoints = 0;
            for (Player p : participants.keySet()) {
                if (p.getCommonData().getRace() == Race.ASMODIANS) asmosPoints += participants.get(p); else elyosPoints += participants.get(p);
            }
            if (asmosPoints == elyosPoints) return Race.NONE; else if (asmosPoints > elyosPoints) return Race.ASMODIANS; else return Race.ELYOS;
        }
    }

    public boolean isAlreadyRegister(Player player) {
        return participants.containsKey(player);
    }

    public void teleportIn(WorldMapInstance newinstance, TvtController tvt) {
        for (Player p : participants.keySet()) {
            if (p == null) {
                return;
            } else if (p.getCommonData().getRace() == Race.ELYOS) TeleportService.teleportTo(p, 320090000, newinstance.getInstanceId(), 276, 183, 162, 3000); else TeleportService.teleportTo(p, 320090000, newinstance.getInstanceId(), 276, 289, 162, 3000);
            p.setTvt(tvt);
        }
    }

    public void teleportOut() {
        for (Player p : participants.keySet()) {
            if (p == null) {
                return;
            }
            p.setTvt(null);
            if (p.getWorldId() == 320090000) TeleportService.moveToBindLocation(p, true, 15000);
        }
        this.sendEndMessage();
        doReward();
        for (Player p : participants.keySet()) {
            p.setTvt(null);
        }
        participants.clear();
    }

    public boolean canStart() {
        return (participants.size() == 12);
    }

    public void sendSorryMessage() {
        for (Player p : participants.keySet()) PacketSendUtility.sendMessage(p, "Not enough players to start.");
    }

    public void sendBeginMessage() {
        for (Player p : participants.keySet()) {
            PacketSendUtility.sendSysMessage(p, "Welcome to Team vs Team. Bleeding begins!");
        }
    }

    private void doReward() {
        for (Player p : participants.keySet()) {
            if (p.getCommonData().getRace() == getWinnerRace()) p.getCommonData().addAp(30000); else p.getCommonData().addAp(15000);
            if (participants.get(p) > 0) p.getCommonData().addAp(participants.get(p));
        }
    }

    public synchronized void addScore(Player p, int value) {
        int newValue = participants.get(p) + value;
        participants.remove(p);
        participants.put(p, newValue);
    }

    public void sendEndMessage() {
        String message = "Team vs Team: \n\n";
        message += "Congratulations " + getWinnerRace().toString() + " ! \n\n";
        for (Player p : participants.keySet()) {
            int score = participants.get(p);
            if (score < 0) score = 0;
            if (p.getCommonData().getRace() == getWinnerRace()) score += 30000; else score += 15000;
            message += ("Player : " + p.getName() + " to win : " + String.valueOf(score) + " Points.\n\n");
        }
        message += "You will be teleported outside of the event in 15 seconds.";
        for (Player p : participants.keySet()) {
            PacketSendUtility.sendSysMessage(p, message);
        }
    }

    public void removePlayer(Player p) {
        participants.remove(p);
    }
}
