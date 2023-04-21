package kanjitori.map.tile;

import com.jme.scene.TriMesh;
import java.util.HashMap;
import java.util.Map;
import kanjitori.graphics.FloorQuad;

/**
 *
 * @author Pirx
 */
public class FloorTile extends AbstractTile {

    private static final String RENDERSTATE = "renderstate";

    private static final String FLIPPED = "flipped";

    private String renderstate;

    boolean flipped = false;

    private FloorQuad floor;

    /** Creates a new instance of FloorTile 
     * @param name 
     * @param ref 
     * @param renderstate 
     */
    public FloorTile(String name, char ref, String renderstate) {
        super(name, ref);
        this.renderstate = renderstate;
    }

    public FloorTile(String name, char ref, String renderstate, boolean flipped) {
        super(name, ref);
        this.renderstate = renderstate;
        this.flipped = flipped;
    }

    public FloorTile() {
    }

    public void init() {
        floor = new FloorQuad("floor", 2, 2, flipped ? FloorQuad.DOWN : FloorQuad.UP);
    }

    public void setParams(Map<String, String> map) {
        renderstate = map.get(RENDERSTATE);
        String fl = map.get(FLIPPED);
        if (fl != null) {
            flipped = Boolean.parseBoolean(fl);
        }
    }

    public Map<String, String> getParams() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(RENDERSTATE, renderstate);
        map.put(FLIPPED, "" + flipped);
        return map;
    }

    public TriMesh getMesh(int index) {
        if (floor == null) {
            init();
        }
        return floor;
    }

    public int getSize() {
        return 1;
    }

    public String getRenderStateName(int index) {
        return renderstate;
    }
}
