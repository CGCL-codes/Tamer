package mikera.tyrant;

import java.util.HashMap;
import mikera.engine.Map;
import mikera.engine.RPG;
import mikera.engine.Thing;
import mikera.util.Maths;
import mikera.util.Rand;

/**
 * @author Mike
 *
 * Implements static methods for handling level maps
 */
public class LevelMap implements java.io.Serializable {

    private static final long serialVersionUID = 3545517309273125684L;

    private int[] pixels = null;

    private int[] currentMemory = null;

    private Map lastMap = null;

    private HashMap mapMemory = new HashMap();

    public static LevelMap instance() {
        LevelMap l = (LevelMap) Game.instance().get("MapMemory");
        if (l == null) {
            l = new LevelMap();
            Game.instance().set("MapMemory", l);
        }
        return l;
    }

    private HashMap getMapMemory() {
        return mapMemory;
    }

    private int[] getMapMemory(Map m) {
        HashMap h = getMapMemory();
        int[] memory = (int[]) h.get(m);
        if ((memory == null) && (m == lastMap)) {
            memory = currentMemory;
        }
        if (memory == null) {
            memory = new int[m.width * m.height];
            if (!m.getFlag("ForgetMap")) {
                h.put(m, memory);
            }
        }
        currentMemory = memory;
        lastMap = m;
        return memory;
    }

    public static void reveal(Map map) {
        int w = map.width;
        int h = map.height;
        int[] mem = instance().getMapMemory(map);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                setMapColor(map, mem, x, y);
            }
        }
    }

    public static void forget(Map map, int chance) {
        if (map == null) return;
        int[] mem = instance().getMapMemory(map);
        for (int i = 0; i < mem.length; i++) {
            if (Rand.r(100) < chance) mem[i] = 0;
        }
    }

    public int[] getMapView(Map map) {
        int w = map.width;
        int h = map.height;
        int[] mem = getMapMemory(map);
        Thing he = Game.hero();
        int r = Being.calcViewRange(he);
        for (int y = Maths.max(0, he.y - r); y < Maths.min(he.y + r + 1, h); y++) {
            for (int x = Maths.max(0, he.x - r); x < Maths.min(he.x + r + 1, w); x++) {
                if (map.isVisibleChecked(x + y * w)) {
                    setMapColor(map, mem, x, y);
                }
            }
        }
        if ((pixels == null) || (pixels.length != mem.length)) {
            pixels = new int[mem.length];
        }
        System.arraycopy(mem, 0, pixels, 0, mem.length);
        for (int y = Maths.max(0, he.y - r); y < Maths.min(he.y + r + 1, h); y++) {
            for (int x = Maths.max(0, he.x - r); x < Maths.min(he.x + r + 1, w); x++) {
                updateMapColor(map, pixels, x, y);
            }
        }
        return pixels;
    }

    /**
	 * This method modifiers map memory colour 
	 * Use this for temporary map colours
	 * e.g. Beings, Hero on radar
	 * 
	 * @param map
	 * @param pixels
	 * @param x
	 * @param y
	 */
    private static void updateMapColor(Map map, int[] pixels, int x, int y) {
        int i = x + map.width * y;
        if (map.isVisibleChecked(i)) {
            int c = pixels[i];
            c = c + 0x00101010;
            Thing t = map.getObjectsChecked(i);
            while (t != null) {
                if (t.getFlag("IsMobile")) {
                    if (t.isHero()) {
                        int cc = 0x00FF8000;
                        pixels[i] = cc;
                        return;
                    } else if (AI.isHostile(Game.hero(), t)) {
                        c = 0x00D00000;
                    } else {
                        c = 0x0000D020;
                    }
                }
                t = t.next;
            }
            pixels[i] = c;
        }
    }

    /**
	 * This method sets map memory colour according to map contents
	 * 
	 * @param map
	 * @param pixels
	 * @param x
	 * @param y
	 */
    private static void setMapColor(Map map, int[] pixels, int x, int y) {
        int tile = map.getTile(x, y);
        int c = Tile.getMapColour(tile);
        Thing t = map.getObjects(x, y);
        while (t != null) {
            int mc = t.getStat("MapColour");
            if (mc > 0) {
                c = mc;
            }
            t = t.next;
        }
        pixels[x + map.width * y] = c;
    }
}
