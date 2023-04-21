package dve.object;

import com.jme.bounding.BoundingBox;
import dve.core.DveEvent;
import dve.core.DveState;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Sphere;
import com.jmex.physics.DynamicPhysicsNode;

/**Sfera che rappresenta un giocatore locale*/
public class SphereLocalPlayer extends LocalActiveObject {

    private Vector3f friction;

    /**Costruttore
     * 
     * @param name Nome del giocatore
     * @param gamestate stato del Dve
     */
    public SphereLocalPlayer(String name, DveState gamestate) {
        super(name, gamestate, new Sphere(name, 9, 11, 1));
    }

    @Override
    public void update(float interpolation) {
        DynamicPhysicsNode physicsActiveObject = (DynamicPhysicsNode) physicsObject;
        if (currentEvent != null) {
            switch(currentEvent.getEventType()) {
                case DveEvent.FORCE:
                    Vector3f appliedForce = new Vector3f(currentEvent.x, currentEvent.y, currentEvent.z);
                    physicsActiveObject.addForce(appliedForce);
                    break;
                case DveEvent.POSITION:
                    Vector3f Position = new Vector3f(currentEvent.x, currentEvent.y, currentEvent.z);
                    physicsActiveObject.setLocalTranslation(Position);
                    break;
            }
            currentEvent = null;
        }
        friction = physicsActiveObject.getLinearVelocity(null).mult((float) -50 * interpolation);
        physicsActiveObject.addTorque(physicsActiveObject.getAngularVelocity(null).mult((float) -50 * interpolation));
        friction.y = 0;
        physicsActiveObject.addForce(friction);
    }

    @Override
    protected void initPhysics() {
        physicsObject.createSphere(name);
        ((DynamicPhysicsNode) physicsObject).computeMass();
        ((DynamicPhysicsNode) physicsObject).getForce(null).getX();
        mesh.setModelBound(new BoundingBox());
        mesh.updateModelBound();
    }
}
