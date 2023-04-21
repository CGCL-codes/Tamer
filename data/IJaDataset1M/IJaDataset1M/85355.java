package org.activision.io.scripts.objects;

import org.activision.Engine;
import org.activision.task.Task;
import org.activision.model.Hits.HitType;
import org.activision.model.player.Player;
import org.activision.io.scripts.objectScript;
import org.activision.util.RSTile;

public class o42938 extends objectScript {

    @Override
    public void examine(Player p) {
    }

    @Override
    public void option1(final Player p, final int coordX, final int coordY, final int height) {
        if (p.getWalk().getWalkDir() != -1 || p.getWalk().getRunDir() != -1) return;
        if (Math.round(p.getLocation().getDistance(RSTile.createRSTile(coordX, coordY, height))) > 1) return;
        int firstX = coordX - (p.getLocation().getRegionX() - 6) * 8;
        int firstY = coordY - (p.getLocation().getRegionY() - 6) * 8;
        p.getWalk().reset();
        p.getWalk().addStepToWalkingQueue(firstX, firstY);
        if (p.getIntermanager().containsTab(16)) p.getFrames().closeInterface(16);
        if (!p.getIntermanager().containsInterface(8, 137)) p.getDialogue().finishDialogue();
        if (p.getCombat().hasTarget()) p.getCombat().removeTarget();
        if (!p.getMask().isTurnToReset()) p.getMask().setTurnToReset(true);
        sendDeath(p);
    }

    private void sendDeath(final Player p) {
        p.getCombatDefinitions().doEmote(-1, 2400, 1800);
        Engine.getEntityExecutor().schedule(new Task() {

            @Override
            public void run() {
                p.hit(p.getSkills().getHitPoints(), HitType.DUNGEON_DAMAGE);
            }
        }, 1800);
    }

    @Override
    public void option2(Player p, int coordX, int coordY, int height) {
    }
}
