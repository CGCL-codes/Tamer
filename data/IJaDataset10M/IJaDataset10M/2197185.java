package org.hyperion.rs2.task.impl;

import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.task.Task;

/**
 * Performs restoring of a player's energy.
 *
 * @author Korsakoff
 */
public class RestoreEnergyTask implements Task {

    @Override
    public void execute(GameEngine context) {
        for (final Player p : World.getWorld().getPlayers()) {
            if ((p.getWalkingQueue().isRunningToggled() || p.getWalkingQueue().isRunning()) && p.getSprites().getSecondarySprite() != -1) {
                continue;
            } else {
                if (p.getRunningEnergy() < 100) {
                    p.setRunningEnergy(p.getRunningEnergy() + 1);
                }
            }
        }
    }
}
