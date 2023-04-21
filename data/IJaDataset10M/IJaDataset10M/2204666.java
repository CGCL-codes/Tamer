package com.captiveimagination.game.control.physics;

import com.captiveimagination.game.control.FloatSpring;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jmex.physics.DynamicPhysicsNode;

/**
 * @author Matthew D. Hicks
 */
public class PhysicsFollowingController extends Controller {

    private static final long serialVersionUID = 1L;

    private DynamicPhysicsNode leader;

    private DynamicPhysicsNode follower;

    private FloatSpring x;

    private FloatSpring y;

    private FloatSpring z;

    private FloatSpring w;

    private Quaternion rotation;

    private Vector3f up;

    private Vector3f location;

    private Vector3f speed;

    public PhysicsFollowingController(DynamicPhysicsNode leader, DynamicPhysicsNode follower, float spring, float damping) {
        this.leader = leader;
        this.follower = follower;
        x = new FloatSpring(spring, damping);
        y = new FloatSpring(spring, damping);
        z = new FloatSpring(spring, damping);
        w = new FloatSpring(spring, damping);
        rotation = new Quaternion();
        rotation.set(follower.getLocalRotation());
        up = new Vector3f(0.0f, 1.0f, 0.0f);
        location = new Vector3f();
        speed = new Vector3f();
        x.setPosition(rotation.x);
        y.setPosition(rotation.y);
        w.setPosition(rotation.z);
        z.setPosition(rotation.w);
    }

    private void updateDestination() {
        location.set(leader.getLocalTranslation());
        location.subtractLocal(follower.getLocalTranslation());
        rotation.lookAt(location, up);
        speed.set(follower.getLocalRotation().getRotationColumn(2));
        speed.multLocal(250.0f);
        follower.setLinearVelocity(speed);
    }

    public void update(float time) {
        updateDestination();
        x.update(rotation.x, time);
        y.update(rotation.y, time);
        z.update(rotation.z, time);
        w.update(rotation.w, time);
        follower.getLocalRotation().set(x.getPosition(), y.getPosition(), z.getPosition(), w.getPosition());
    }
}
