package objects.items;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import objects.base.DraggableObject;
import objects.base.Particle;
import objects.particles.DirtParticle;
import resource.GetResourceLoaderException;
import resource.LoadResourceException;
import resource.ResourceLoader;
import scenes.base.PhysicsScene;

/**
 * A cheese ball. Whenever Spheres jumps on top of this draggable object, he is
 * launched into the air.
 * 
 * @author tom
 * 
 */
public class CheeseBall extends DraggableObject {

    /**
	 * How directly Spheres must be on top of the cheese ball to be launched.
	 * The cosine of the angle Spheres makes with the cheese ball.
	 */
    public static final float CHEESEBALL_BUMP_COEFFICIENT = .7f;

    /**
	 * The velocity at which Spheres is launched into the air after touching the
	 * cheese ball.
	 */
    public static final float CHEESEBALL_BUMP_VELOCITY = 44;

    /**
	 * Create a new cheese ball object.
	 * 
	 * @param scene
	 *            The scene to which to add the cheese ball.
	 * @param gl
	 *            The OpenGL object used for loading textures.
	 * @throws LoadResourceException
	 *             if one of the required resources could not be loaded.
	 * @throws GetResourceLoaderException
	 *             if the resource loader could not be retrieved.
	 */
    public CheeseBall(PhysicsScene scene, GL gl) throws LoadResourceException, GetResourceLoaderException {
        super(0, scene);
        setSprite(ResourceLoader.getInstance().getSprite("sprites/items/cheeseball", gl));
    }

    @Override
    protected Shape getShape() {
        CircleShape cir = new CircleShape();
        cir.m_radius = 8f * PIXEL_TO_PHYSICS;
        return cir;
    }

    @Override
    public Particle createParticle() {
        DirtParticle p = new DirtParticle(getScene());
        p.setPhysicsScene(getPhysicsScene());
        p.setColors(new float[] { 1f, 1f, 0 }, new float[] { 1f, .5f, 0 }, .1f);
        p.setLifeTime(4);
        return p;
    }

    @Override
    public void draw(GL2 gl) {
        sprite.drawRotated(gl, getAngle());
    }
}
