package test.node;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import env3d.Env;
import env3d.EnvObject;
import env3d.advanced.EnvNode;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author jmadar
 */
public class Game {

    public void play() {
        Env env = new Env();
        env.enableLighting();
        EnvNode d = new EnvNode();
        d.setX(5);
        d.setY(0.3);
        d.setZ(5);
        d.setScale(3);
        d.setTexture(null);
        d.setModel("/Users/jmadar/Documents/models/spider/spider.blend");
        AnimControl c = d.getJme_node().getControl(AnimControl.class);
        for (String n : c.getAnimationNames()) {
            System.out.println(n);
        }
        env.addObject(d);
        while (true) {
            Object o = env.getPick(env.getMouseX(), env.getMouseY());
            if (o instanceof EnvNode) {
            } else {
            }
            if (env.getKeyDown(Keyboard.KEY_ESCAPE)) {
                env.exit();
            }
            if (env.getKey() == Keyboard.KEY_G) {
                env.setMouseGrab(!env.isMouseGrabbed());
                env.setDefaultControl(env.isMouseGrabbed());
                if (env.isMouseGrabbed()) {
                    env.enableLighting();
                } else {
                    env.disableLighting();
                }
            }
            if (env.getKey() == Keyboard.KEY_F) {
                env.setFullscreen(!env.isFullScreen());
            }
            if (env.getKey() == Keyboard.KEY_Q) {
                env.setFullscreen(false);
            }
            env.advanceOneFrame();
        }
    }

    public static void main(String[] args) {
        (new Game()).play();
    }
}
