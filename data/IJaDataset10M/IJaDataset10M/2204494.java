package viewControllers;

import javax.media.opengl.GL;
import globals.GlobalObjects;
import graphics.GraphicalObject;
import graphics.Shapes.TexturedQuad;
import graphics.Shapes.TexturedQuadShader;
import physics.GameObject;

public class PortalViewController extends ViewController {

    public TexturedQuadShader tq;

    public PortalViewController(GameObject object) {
        super(object);
        tq = new TexturedQuadShader();
        tq.setScale(0.75);
        tq.getCenter().setValues(0, 1, 0);
        PortalBody body2 = new PortalBody(tq);
        addPart(body2);
        setAnimator(new Animator(parts));
    }

    public class PortalBody extends AnimationPart {

        public PortalBody(GraphicalObject object) {
            super(object);
        }
    }

    public class PortalFrame extends AnimationPart {

        public PortalFrame(GraphicalObject object) {
            super(object);
        }
    }
}
