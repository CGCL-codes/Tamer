package prealpha.weapon;

import com.jme.scene.Controller;
import com.jmex.physics.DynamicPhysicsNode;

public class LifeTimeController extends Controller {

    private static final long serialVersionUID = -3137918177458083199L;

    public float lifetime = 9f;

    DynamicPhysicsNode target;

    public LifeTimeController(DynamicPhysicsNode target) {
        this.target = target;
    }

    @Override
    public void update(float time) {
        lifetime -= time;
        if (lifetime < 0) target.delete();
    }
}
