package pelletQuest.map;

import java.util.ArrayList;
import org.newdawn.slick.util.Log;
import pelletQuest.database.World;
import pelletQuest.entities.Entity;

public class Zone {

    protected String[] maps;

    public Zone(String[] maps) {
        this.maps = maps;
    }

    public void update(int delta) {
        ArrayList<Entity> entities = new ArrayList<Entity>();
        for (String s : maps) {
            try {
                entities.addAll(World.getMap(s).getUpdatableEntities());
            } catch (Exception e) {
                Log.warn(" Could not find map to update: " + s);
            }
        }
        for (Entity e : entities) {
            e.update(delta);
        }
        for (int i = 0; i < maps.length; i++) {
            try {
                World.getMap(maps[i]).update(delta);
            } catch (Exception e) {
                Log.warn(" Could not find map to update: " + maps[i]);
            }
        }
    }
}
