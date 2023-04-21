package neon.core.handlers;

import java.awt.Point;
import java.util.Collection;
import neon.core.Configuration;
import neon.core.Engine;
import neon.maps.*;
import neon.objects.entities.Creature;
import neon.objects.entities.Player;
import neon.objects.property.Condition;
import neon.systems.timing.TimerListener;
import neon.ui.GamePanel;

public class TurnHandler implements TimerListener {

    private GamePanel panel;

    private Generator generator;

    public TurnHandler(GamePanel panel) {
        this.panel = panel;
    }

    public void tick(int time) {
        if (Configuration.gThread) {
            if (generator == null || !generator.isAlive()) {
                generator = new Generator();
                generator.start();
            }
        } else {
            checkRegions();
        }
        Player player = Engine.getPlayer();
        int range = (panel.getVisibleRectangle().width + panel.getVisibleRectangle().width) / 4;
        for (Creature creature : Atlas.getCurrentZone().getCreatures()) {
            if (!creature.hasCondition(Condition.DEAD)) {
                creature.heal(creature.species.con / 100f);
                creature.addMana(creature.species.wis / 100f);
                if (Point.distance(player.x, player.y, creature.x, creature.y) < range) {
                    creature.brain.act();
                }
            }
        }
        player.heal(player.species.con / 100f);
        player.addMana(player.species.wis / 100f);
        panel.repaint();
        Engine.getPhysicsEngine().update(time);
    }

    private class Generator extends Thread {

        public void run() {
            checkRegions();
            panel.repaint();
        }
    }

    private void checkRegions() {
        Zone zone = Atlas.getCurrentZone();
        boolean fixed = true;
        do {
            Collection<Region> buffer = zone.getRegions(panel.getVisibleRectangle());
            fixed = true;
            for (Region r : buffer) {
                if (!r.isFixed()) {
                    fixed = false;
                    r.setFixed(true);
                    String type = Themes.getRegionThemes().get(r.getTheme()).getAttributeValue("random");
                    if (type.startsWith("town")) {
                        new TownGenerator(zone).generate(r.x, r.y, r.width, r.height, type, r.z);
                    } else {
                        new WildernessGenerator(zone).generate(r);
                    }
                }
            }
        } while (fixed == false);
    }
}
