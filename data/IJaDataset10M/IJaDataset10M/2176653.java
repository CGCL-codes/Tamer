package game.entities;

import game.core.Sequencer;
import game.resourceObjects.BlipResource;
import game.util.Conf;
import game.util.TickCounter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import net.phys2d.math.ROVector2f;
import net.phys2d.raw.Body;
import com.jme.scene.Node;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;

public class Blip {

    public static int _blipCount = 0;

    private Blip parent;

    private List<BlipImage> images = new ArrayList<BlipImage>();

    private TickCounter counter = new TickCounter();

    protected BlipResource resource;

    private Node node = new Node();

    private Body body;

    private int pulseCount = 0;

    private AudioTrack audioTrack;

    protected List<Blip> children = new Vector<Blip>();

    private boolean connected = false;

    private boolean isDying = false;

    private boolean dead = false;

    public Blip(final BlipResource resource) {
        _blipCount++;
        this.resource = resource;
        getNode().setName("Blip ['" + resource.handle + "'] ");
    }

    public BlipResource getResource() {
        return resource;
    }

    public void setAudioTrack(final AudioTrack track) {
        audioTrack = track;
        AudioSystem.getSystem().getEnvironmentalPool().addTrack(track);
    }

    public Node getNode() {
        return node;
    }

    public List<Blip> getChildren() {
        return children;
    }

    public void addChild(final Blip b) {
        if (!children.contains(b)) {
            children.add(b);
            b.setParent(this);
        }
    }

    public void removeChild(final Blip b) {
        children.remove(b);
        b.destroy();
        b.setParent(null);
    }

    /**
	 * Prepare to die, not immediately, show that we're dying, then destroy()
	 * later
	 */
    public void die() {
        for (final BlipImage im : images) {
            im.die();
        }
        isDying = true;
    }

    /**
	 * Immediately destroy this object
	 */
    public void destroy() {
        if (audioTrack != null) {
            AudioSystem.getSystem().getEnvironmentalPool().removeTrack(audioTrack);
            audioTrack = null;
        }
        for (final BlipImage im : images) {
            im.destroy();
        }
        images = null;
        node.detachAllChildren();
        node.removeFromParent();
        node = null;
        parent = null;
        children = null;
        counter = null;
        body = null;
        resource = null;
        dead = true;
    }

    public Blip getParent() {
        return parent;
    }

    public void setParent(final Blip parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return null != parent;
    }

    /**
	 * Evaluates the {@link BlipResource} and makes sure everything is displayed
	 * correctly. Use only if the blip has structurally changed during runtime.
	 */
    public void updateProperties() {
        for (final BlipImage im : images) {
            im.updateProperties();
        }
    }

    public void setBody(final Body b) {
        body = b;
    }

    public Body getBody() {
        return body;
    }

    public void addImage(final BlipImage image) {
        images.add(image);
        getNode().attachChild(image);
    }

    public void tick() {
        if (isDying) return;
        if (counter.hasCounters()) {
            if (counter.tick()) {
                for (final Blip child : children) {
                    child.pulse();
                }
            }
        }
    }

    /**
	 * Queues this blip for a pulse on the next tick update.
	 */
    public void pulse() {
        if (isDying) return;
        counter.addCounter(resource.sustain.value);
        for (final BlipImage im : images) {
            im.pulse(resource.sustain.value * Sequencer.getSequencer().getTickLength());
        }
        if (Conf.SOUND_ENABLED) {
            try {
                audioTrack.play();
            } catch (Exception e) {
            }
        }
        if (resource.life.value > 0) {
            pulseCount++;
            if (pulseCount > resource.life.value) {
                die();
            }
        }
    }

    public boolean isDying() {
        return isDying;
    }

    @Override
    public String toString() {
        return getNode().getName();
    }

    public void setConnected(final boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isDead() {
        return dead;
    }

    private final float moveToSmooth = .2f;

    public void moveTo(final float x, final float y) {
        final ROVector2f v = getBody().getPosition();
        final float xp = v.getX() + ((x - v.getX()) * moveToSmooth);
        final float yp = v.getY() + ((y - v.getY()) * moveToSmooth);
        getBody().setPosition(xp, yp);
    }

    @Override
    protected void finalize() throws Throwable {
        _blipCount--;
    }
}
