package com.ham.mud.quests;

import com.ham.mud.ServerConnection;
import com.ham.mud.characters.mobs.Mob;
import com.ham.mud.characters.player.Player;
import com.ham.mud.rooms.RoomService;

/**
 * Created by hlucas on Jul 19, 2011 at 12:00:49 PM
 */
public class KillQuest extends Quest {

    boolean isComplete = false;

    Mob mob;

    public KillQuest(int rewardPoints, int rewardGold) {
        super(rewardPoints, rewardGold);
    }

    @Override
    public void display(ServerConnection connection) {
        connection.println("Kill \"" + mob.getName() + "\" near \"" + RoomService.getZone(mob.getZone()).getName() + "\"!");
    }

    @Override
    public boolean isComplete(Player player) {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public void setMob(Mob mob) {
        this.mob = mob;
    }

    public Mob getMob() {
        return mob;
    }
}
