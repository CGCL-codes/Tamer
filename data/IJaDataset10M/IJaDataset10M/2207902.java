package be.castanea.client.model;

import be.castanea.client.CastaneaClient;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.state.CullState;
import com.jme.system.DisplaySystem;
import com.jmex.terrain.TerrainPage;
import java.util.HashMap;
import java.util.Map;

/**
 * Castanea
 * 2009
 * @author Geert van Leemputten, Steven Rymenans, Bart Van Hooydonck
 */
public class World extends TerrainPage {

    private static World instance;

    private Map<String, Mob> mobs;

    public static World getInstance() {
        return instance;
    }

    public static void load(int blockSize, int size, Vector3f stepScale, float[] heightMap) {
        instance = new World("world", blockSize, size, stepScale, heightMap);
    }

    private World(String name, int blockSize, int size, Vector3f stepScale, float[] heightMap) {
        super(name, blockSize, size, stepScale, heightMap);
        CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
        cs.setCullFace(CullState.Face.Back);
        cs.setEnabled(true);
        setRenderState(cs);
        mobs = new HashMap<String, Mob>();
    }

    public void addMob(Mob mob) {
        mobs.put(mob.getName(), mob);
        mob.registerWorld(this);
    }

    public void removeMob(String mobName) {
        mobs.remove(mobName);
        Node rootNode = CastaneaClient.getIngameState().getRootNode();
        rootNode.detachChildNamed(mobName);
    }

    public Mob getMob(String mobName) {
        Mob mob = mobs.get(mobName);
        if (mob == null) {
            throw new RuntimeException("Player could not be found");
        }
        return mob;
    }

    public void update(float interpolation) {
        for (Mob mob : mobs.values()) {
            mob.update(interpolation);
        }
    }
}
