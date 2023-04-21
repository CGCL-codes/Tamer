package scenes.base;

import java.util.concurrent.ConcurrentLinkedQueue;
import event.EventHandler;
import gui.Window;
import objects.base.DraggableObject;
import objects.base.PhysicsObject;
import objects.base.PhysicsObject.SolidCollisionData;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import scene.RObject;
import scene.Scene;

/**
 * A scene containing features for adding physics-powered objects (using
 * JBox2D).
 * 
 * @author tom
 * 
 */
public class PhysicsScene extends Scene {

    /**
	 * The time step used for physics. (Basically the length of a frame.)
	 */
    public static final float PHYSICS_TIMESTEP = (float) 1 / Window.FRAMERATE;

    /**
	 * The priority for the evaluation of the physics.
	 */
    public static final double PRIORITY_PHYSICS = -200;

    /**
	 * The priority for the passing of the messages.
	 */
    public static final double PRIORITY_MESSAGES = -199;

    /**
	 * The number of velocity iterations for JBox2D.
	 */
    static final int PHYSICS_VELOCITY_ITERATIONS = 5;

    /**
	 * The number of position iterations for JBox2D.
	 */
    static final int PHYSICS_POSITION_ITERATIONS = 5;

    /**
	 * The default gravity vector.
	 */
    static final Vec2 DEFAULT_GRAVITY_VECTOR = new Vec2(0, 40f);

    /**
	 * The JBox2D physics world.
	 */
    private World world;

    /**
	 * The gravity vector.
	 */
    private Vec2 gravity;

    /**
	 * The normal vector pointing in the direction of gravity.
	 */
    private Vec2 normalizedGravity;

    /**
	 * The vector pointing to the left (taking gravity into account).
	 */
    private Vec2 leftDirection;

    /**
	 * The queue of messages to be passed in the next step.
	 */
    private ConcurrentLinkedQueue<String> messages;

    /**
	 * @param window
	 *            The window containing this scene.
	 */
    public PhysicsScene(Window window) {
        super(window);
        gravity = DEFAULT_GRAVITY_VECTOR;
        world = new World(gravity, true);
        world.setAutoClearForces(true);
        messages = new ConcurrentLinkedQueue<String>();
        setGravity(DEFAULT_GRAVITY_VECTOR);
        onStep().addHandler(new EventHandler<Void>() {

            @Override
            public void handle(Void data) {
                physicsStep();
            }
        }, PRIORITY_PHYSICS);
        onStep().addHandler(new EventHandler<Void>() {

            @Override
            public void handle(Void data) {
                flushMessages();
            }
        }, PRIORITY_MESSAGES);
        world.setContactListener(new RContactListener());
    }

    /**
	 * Get the JBox2D world.
	 * 
	 * @return The JBox2D world.
	 */
    public World getPhysicsWorld() {
        return world;
    }

    /**
	 * Execute a step in the physics engine.
	 */
    protected void physicsStep() {
        world.step(PHYSICS_TIMESTEP, PHYSICS_VELOCITY_ITERATIONS, PHYSICS_POSITION_ITERATIONS);
    }

    /**
	 * Set the direction of the gravity.
	 * 
	 * @param gravity
	 *            The new gravity vector.
	 */
    public void setGravity(Vec2 gravity) {
        this.gravity = gravity;
        this.normalizedGravity = new Vec2(gravity);
        this.normalizedGravity.normalize();
        this.leftDirection = new Vec2(-normalizedGravity.y, normalizedGravity.x);
        world.setGravity(gravity);
    }

    /**
	 * Get the gravity vector.
	 * 
	 * @return The gravity vector.
	 */
    public Vec2 getGravity() {
        return gravity;
    }

    /**
	 * Get a normalized version of the gravity vector.
	 * 
	 * @return A normalized version of the gravity vector.
	 */
    public Vec2 getNormalizedGravity() {
        return normalizedGravity;
    }

    /**
	 * Get a normal vector pointing to the left (from the gravity vector).
	 * 
	 * @return A normal vector pointing to the left.
	 */
    public Vec2 getLeftDirection() {
        return leftDirection;
    }

    /**
	 * Add a new message to the message event.
	 * 
	 * @param message
	 *            The message to add.
	 */
    public void message(String message) {
        messages.add(message);
    }

    /**
	 * Flush the messages, causing the message event to fire once for each
	 * message and clearing the queue.
	 */
    void flushMessages() {
        String m;
        while ((m = messages.poll()) != null) onMessage().fire(m, null);
        messages.clear();
    }

    /**
	 * The contact listener for the physics scene.
	 * 
	 * @author tom
	 * 
	 */
    class RContactListener implements ContactListener {

        @Override
        public void preSolve(Contact contact, Manifold manifold) {
            PhysicsObject o1 = (PhysicsObject) contact.m_fixtureA.getBody().getUserData();
            PhysicsObject o2 = (PhysicsObject) contact.m_fixtureB.getBody().getUserData();
            if (o1.isSolid()) {
                o2.onUpdateCollisionWithSolid().fire(o1, manifold.localNormal);
            }
            if (o2.isSolid()) {
                o1.onUpdateCollisionWithSolid().fire(o2, manifold.localNormal);
            }
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
        }

        @Override
        public void endContact(Contact contact) {
            PhysicsObject o1 = (PhysicsObject) contact.m_fixtureA.getBody().getUserData();
            PhysicsObject o2 = (PhysicsObject) contact.m_fixtureB.getBody().getUserData();
            if (o1.isSolid()) {
                o2.onStopCollisionWithSolid().fire(o1);
            }
            if (o2.isSolid()) {
                o1.onStopCollisionWithSolid().fire(o2);
            }
            if (o1 instanceof DraggableObject) o2.onStopCollisionWithDraggable().fire((DraggableObject) o1);
            if (o2 instanceof DraggableObject) o1.onStopCollisionWithDraggable().fire((DraggableObject) o2);
            o1.onStopCollisionWith().fire(o2.getClass(), o2);
            o2.onStopCollisionWith().fire(o1.getClass(), o1);
        }

        @Override
        public void beginContact(Contact contact) {
            PhysicsObject o1 = (PhysicsObject) contact.m_fixtureA.getBody().getUserData();
            PhysicsObject o2 = (PhysicsObject) contact.m_fixtureB.getBody().getUserData();
            if (o1.isSolid()) {
                SolidCollisionData data = o1.new SolidCollisionData();
                data.other = o1;
                data.normal = contact.getManifold().localNormal;
                o2.onStartCollisionWithSolid().fire(data);
            }
            if (o2.isSolid()) {
                SolidCollisionData data = o2.new SolidCollisionData();
                data.other = o2;
                data.normal = contact.getManifold().localNormal;
                o1.onStartCollisionWithSolid().fire(data);
            }
            if (o1 instanceof DraggableObject) o2.onStartCollisionWithDraggable().fire((DraggableObject) o1);
            if (o2 instanceof DraggableObject) o1.onStartCollisionWithDraggable().fire((DraggableObject) o2);
            o1.onStartCollisionWith().fire(o2.getClass(), o2);
            o2.onStartCollisionWith().fire(o1.getClass(), o1);
        }
    }

    /**
	 * Wake up the complete scene, making sure that each object will respond to
	 * manual movements of objects.
	 */
    public void wakeUp() {
        for (RObject o : objects) {
            if (o instanceof PhysicsObject) {
                ((PhysicsObject) o).getBody().setAwake(true);
            }
        }
    }
}
