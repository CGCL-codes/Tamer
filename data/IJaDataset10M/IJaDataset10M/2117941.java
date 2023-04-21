package game.objects;

import game.entities.Blip;
import game.resourceObjects.LevelResource;
import java.util.ArrayList;
import java.util.List;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import com.jme.scene.Node;

/**
 * The actual level as it is used in-game, uses a {@link LevelResource} to be
 * constructed.
 * 
 * @author B. Korsmit
 */
public class Level extends Node {

    private final LevelResource resource;

    private List<Body> bodies = new ArrayList<Body>();

    private List<Blip> blips = new ArrayList<Blip>();

    private Blip player;

    public Level(final LevelResource resource) {
        super("Level ['" + resource.title + "'] ");
        this.resource = resource;
    }

    public void addBody(final StaticBody body2) {
        bodies.add(body2);
    }

    public void addBlip(final Blip b) {
        blips.add(b);
        attachChild(b.getNode());
    }

    public List<Body> getBodies() {
        return bodies;
    }

    public LevelResource getResource() {
        return resource;
    }

    public void setPlayer(final Blip b) {
        player = b;
    }

    public Blip getPlayer() {
        return player;
    }

    public List<Blip> getBlips() {
        return blips;
    }

    public void destroy() {
        blips.clear();
        bodies.clear();
        detachAllChildren();
        blips = null;
        bodies = null;
        player = null;
    }
}
