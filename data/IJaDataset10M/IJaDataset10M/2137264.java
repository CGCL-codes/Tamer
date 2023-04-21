package org.hfbk.vis;

import org.lwjgl.util.vector.Vector3f;

/**
 * 
 * This viewpoint creates a soft transition between viewpoints.
 * 
 * Usually after finishing a transition the user controlled viewpoint 
 * should be updated as well, to continue navigation at the new position. 
 *  
 * 
 * @author Paul
 *
 */
public class TransitionViewpoint extends Viewpoint {

    Viewpoint fromView, toView;

    float speed = 1f, transition = 0;

    /**
	 * build a new transition from the viewpoint to 
	 * another
	 *  
	 * @param viewpoint
	 * @param viewpoint2
	 */
    public TransitionViewpoint(Viewpoint viewpoint, Viewpoint viewpoint2, float speed) {
        this.fromView = viewpoint;
        this.toView = viewpoint2;
        this.speed = speed;
    }

    /**
	 * check if we have arrived target position
	 * 
	 * @return if we arrived.
	 */
    public boolean checkArrived() {
        return transition >= 1;
    }

    float ipol(float a, float b, float t) {
        return a * (1 - t) + b * t;
    }

    float aipol(float a, float b, float t) {
        float pi = (float) Math.PI;
        if (Math.abs(a - b) > Math.PI) {
            a = (a + pi) % (2 * pi);
            b = (b + pi) % (2 * pi);
            return (a * (1 - t) + b * t + pi) % (2 * pi);
        } else return a * (1 - t) + b * t;
    }

    void render(float dt) {
        transition += dt * speed;
        float ftrans = (float) Math.sin((transition - .5f) * Math.PI) / 2 + .5f;
        angle = aipol(fromView.angle, toView.angle, ftrans);
        elevation = aipol(fromView.elevation, toView.elevation, ftrans);
        Vector3f.add((Vector3f) new Vector3f(fromView).scale(1 - ftrans), (Vector3f) new Vector3f(toView).scale(ftrans), this);
        super.render(dt);
    }
}
