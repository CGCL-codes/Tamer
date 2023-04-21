package galacticthrone.empire;

import galacticthrone.map.data.DataMap;
import galacticthrone.map.data.obs.SolarSystem;
import galacticthrone.map.render.SolarSystemIcon;
import galacticthrone.map.render.VisionMap;

/**
 * <br>
 *
 * @author Jaco van der Westhuizen
 */
public class Empire {

    public final EmpireColor color;

    Species leaderRace;

    VisionMap explored;

    SolarSystem home;

    public Empire(EmpireColor color, Species race) {
        this.color = color;
        this.leaderRace = race;
    }

    /**
     * @return
     */
    public VisionMap getMap() {
        return explored;
    }

    /**
     * @param posX
     * @param posY
     */
    public SolarSystem makeHomeWorld(DataMap map, int posX, int posY) {
        explored = new VisionMap(map);
        home = leaderRace.makeHomeWorld(this, posX, posY);
        explored.add(new SolarSystemIcon(home, true));
        home.setOwner(this);
        return home;
    }

    public SolarSystem getHomeWorld() {
        return home;
    }
}
