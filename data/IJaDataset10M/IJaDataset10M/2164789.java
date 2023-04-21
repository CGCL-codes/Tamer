package failure.wm.business;

import failure.common.*;
import failure.wm.business.MetaWorldName;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Set of worlds. This class allow to connect worlds of same sizes
 * they is tow layers in a meta world ( see metaworld doc for details )
 * @author Higurashi
 * @version WM 1.0
 * @since WM 1.0
 */
public class MetaWorld implements Serializable {

    public static final String WORLD_EXT = ".world";

    public static final String WORLD_PATH = "/tmp/";

    private Hashtable<BlockPosition, String> worldsLayer0;

    private Hashtable<BlockPosition, String> worldsLayer1;

    private Hashtable<String, Calendar> updates;

    private MetaWorldName name;

    /**
     * Constructor
     */
    public MetaWorld(MetaWorldName name) {
        this.name = name;
        worldsLayer0 = new Hashtable<BlockPosition, String>();
        worldsLayer1 = new Hashtable<BlockPosition, String>();
        updates = new Hashtable<String, Calendar>();
    }

    /**
     * Add a world ( to layer 0 ) , the world is serialized.
     * @param world the world to add
     * @param position the NO position of the world ( in the meta-world )
     * @throws failure.wm.business.WorldException
     */
    public void addWorldLayer0(World world, BlockPosition position) throws WorldException {
        if (Constains.debugWorld) System.out.println("[mw] add world / layer 0 " + world.getName() + "@" + position);
        if (!world.getSurface().equals(Constains.worldsSurface)) throw new WorldException("worlds must be " + Constains.worldsSurface + " to be add");
        worldsLayer0.put(position, world.getName());
        world.move(position);
        saveWorldLayer0(world);
        updates.put(world.getName() + ".0", world.getCreation());
    }

    /**
     * Add a world ( to layer 1 ) , the world is serialized.
     * @param world the world to add
     * @param position the NO position of the world ( in the meta-world )
     * @throws failure.wm.business.WorldException
     */
    public void addWorldLayer1(World world, BlockPosition position) throws WorldException {
        if (Constains.debugWorld) System.out.println("[mw] add world / layer 1 " + world.getName() + "@" + position);
        if (!world.getSurface().equals(Constains.worldsSurface)) throw new WorldException("worlds must be " + Constains.worldsSurface + " to be add");
        worldsLayer1.put(position, world.getName());
        world.move(position);
        saveWorldLayer1(world);
        updates.put(world.getName() + ".1", world.getCreation());
    }

    /**
     * Return the name(s) of world(s) for a given position in the meta-world,
     * for layer 0
     * @param position the position in the meta-world (NO of the world)
     * @return the name(s) of world(s)
     * @throws failure.wm.business.WorldException
     */
    public String[] getWorldLayer0ForPosition(BlockPosition position) throws WorldException {
        return getWorldForPosition(position, worldsLayer0);
    }

    /**
     * Return the name(s) of world(s) for a given position in the meta-world,
     * for layer 1
     * @param position the position in the meta-world (NO of the world)
     * @return the name(s) of world(s)
     * @throws failure.wm.business.WorldException
     */
    public String[] getWorldLayer1ForPosition(BlockPosition position) throws WorldException {
        return getWorldForPosition(position, worldsLayer1);
    }

    /**
     * Return the file of a serialzed world (layer 0)
     * @param name the name of the world
     * @return the file name
     */
    public static String getWorldFileLayer0ByName(String name) {
        return WORLD_PATH + name + ".0" + WORLD_EXT;
    }

    /**
     * Return the file of a serialzed world (layer 1)
     * @param name the name of the world
     * @return the file name
     */
    public static String getWorldFileLayer1ByName(String name) {
        return WORLD_PATH + name + ".1" + WORLD_EXT;
    }

    /**
     * Tell if there is a update needed for a world ( for layer 0 )
     * @param  world the world to test
     * @return True if need a update
     * @throws failure.wm.business.WorldException
     */
    public boolean needUpdateLayer0(World world) throws WorldException {
        return needUpdate(world, world.getName() + ".0");
    }

    /**
     * Tell if there is a update needed for a world ( for layer 1 )
     * @param  world the world to test
     * @return True if need a update
     * @throws failure.wm.business.WorldException
     */
    public boolean needUpdateLayer1(World world) throws WorldException {
        return needUpdate(world, world.getName() + ".1");
    }

    /**
     * Used to test worlds (private)
     * @param World the world
     * @param name the world name
     * @return True if need a update
     * @throws failure.wm.business.WorldException
     */
    private boolean needUpdate(World world, String name) throws WorldException {
        if (updates.get(name) == null) {
            if (Constains.debugWorld) System.out.println("[mw] unknown world " + name);
            return true;
        }
        if ((world.getCreation().compareTo(updates.get(name))) < 0) return true; else return false;
    }

    /**
     * Return the name(s) of world(s) for a given position in a list of worlds
     * @param position the position
     * @param worlds the list of worlds
     * @return the worlds names
     * @throws failure.wm.business.WorldException
     */
    private static String[] getWorldForPosition(BlockPosition position, Hashtable<BlockPosition, String> worlds) throws WorldException {
        int margue = 8;
        if (Constains.debugWorld) System.out.println("[mw] get worlds for position " + position);
        int x = ((position.getBlockX() - (position.getBlockX() % Constains.worldsSurface.getBlockX())) / Constains.worldsSurface.getBlockX()) * Constains.worldsSurface.getBlockX();
        int y = ((position.getBlockY() - (position.getBlockY() % Constains.worldsSurface.getBlockY())) / Constains.worldsSurface.getBlockY()) * Constains.worldsSurface.getBlockY();
        Vector<String> world = new Vector<String>();
        String w = worlds.get(new BlockPosition(x, y));
        if (w != null) world.add(w);
        boolean H = false;
        boolean B = false;
        boolean G = false;
        boolean D = false;
        if (position.getBlockX() > x + Constains.worldsSurface.getBlockX() - margue) {
            w = worlds.get(new BlockPosition(x + Constains.worldsSurface.getBlockX(), y));
            if (w != null) world.add(w);
            D = true;
        }
        if (position.getBlockX() < x + margue) {
            w = worlds.get(new BlockPosition(x - Constains.worldsSurface.getBlockX(), y));
            if (w != null) world.add(w);
            G = true;
        }
        if (position.getBlockY() > y + Constains.worldsSurface.getBlockY() - margue) {
            w = worlds.get(new BlockPosition(x, y + Constains.worldsSurface.getBlockY()));
            if (w != null) world.add(w);
            B = true;
        }
        if (position.getBlockY() < y + margue) {
            w = worlds.get(new BlockPosition(x, y - Constains.worldsSurface.getBlockY()));
            if (w != null) world.add(w);
            H = true;
        }
        if (D && B) {
            w = worlds.get(new BlockPosition(x + Constains.worldsSurface.getBlockX(), y + Constains.worldsSurface.getBlockY()));
            if (w != null) world.add(w);
        }
        if (D && H) {
            w = worlds.get(new BlockPosition(x + Constains.worldsSurface.getBlockX(), y - Constains.worldsSurface.getBlockY()));
            if (w != null) world.add(w);
        }
        if (G && H) {
            w = worlds.get(new BlockPosition(x - Constains.worldsSurface.getBlockX(), y - Constains.worldsSurface.getBlockY()));
            if (w != null) world.add(w);
        }
        if (G && B) {
            w = worlds.get(new BlockPosition(x - Constains.worldsSurface.getBlockX(), y + Constains.worldsSurface.getBlockY()));
            if (w != null) world.add(w);
        }
        return world.toArray(new String[world.size()]);
    }

    /**
     * Serialize a world (layer 0)
     * @param world the world to save
     */
    public void saveWorldLayer0(World world) {
        WorldSerializer.save(world, getWorldFileLayer0ByName(world.getName()));
    }

    /**
     * Serialize a world (layer 1)
     * @param world the world to save
     */
    public void saveWorldLayer1(World world) {
        WorldSerializer.save(world, getWorldFileLayer1ByName(world.getName()));
    }

    public void save() {
        WorldSerializer.save(this, WORLD_PATH + this.name.toString() + ".meta" + WORLD_EXT);
    }

    /**
     * Deserialize a world (layer 0)
     * @param name the world name
     * @return the world
     */
    public static World loadWorldLayer0(String name) {
        if (Constains.debugWorld) System.out.println("[mw] load world " + name + " / layer 0");
        return WorldSerializer.loadWorld(getWorldFileLayer0ByName(name));
    }

    /**
     * Deserialize a world (layer 1)
     * @param name the world name
     * @return the world
     */
    public static World loadWorldLayer1(String name) {
        if (Constains.debugWorld) System.out.println("[mw] load world " + name + " / layer 1");
        return WorldSerializer.loadWorld(getWorldFileLayer1ByName(name));
    }

    /**
     * Return the names of worlds (layer 0)
     * @return the names
     */
    public String[] getWorldsNamesLayer0() {
        String[] names = new String[this.worldsLayer0.values().size()];
        return this.worldsLayer0.values().toArray(names);
    }

    /**
     * Return the names of worlds (layer 1)
     * @return the names
     */
    public String[] getWorldsNamesLayer1() {
        String[] names = new String[this.worldsLayer1.values().size()];
        return this.worldsLayer1.values().toArray(names);
    }
}
