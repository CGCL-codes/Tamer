package net.java.dev.joode.examples;

import org.xith3d.loop.*;
import org.xith3d.scenegraph.*;
import org.xith3d.base.*;
import org.xith3d.render.*;
import net.java.dev.joode.*;
import net.java.dev.joode.collision.CollisionManager;
import net.java.dev.joode.collision.SurfaceParameters;
import net.java.dev.joode.force.*;
import net.java.dev.joode.space.*;
import net.java.dev.joode.geom.*;
import net.java.dev.joode.util.*;
import net.java.dev.joode.joint.*;
import net.java.dev.joode.test.xith.*;
import org.openmali.vecmath2.*;
import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.devices.components.MouseButton;
import org.jagatoo.input.devices.components.MouseButtons;
import org.jagatoo.input.events.KeyPressedEvent;
import org.jagatoo.input.events.MouseButtonReleasedEvent;

/**
 * <p>
 * This is a simple example, that should be able to enable most JOODE-Newbies a first impression
 * of how to use JOODE and how to setup a simple scene.
 * </p>
 * <p>
 * It contains some static objects, like walls and a ball, which can be moved by pressing the arrow-keys.
 * There is also a Box, which lies on one wall to enable interaction between to moveable bodies.
 * </p>
 * @author Arne Müller
 *
 */
public class Example2 extends InputAdapterRenderLoop {

    public static final float MIN_STEPSIZE = .01f;

    private Canvas3D canvas;

    private World w;

    @SuppressWarnings("unused")
    private XithManager binder;

    private Body ballBody;

    @Override
    protected void prepareNextFrame(long gameTime, long frameTime, TimingMode timingMode) {
        super.prepareNextFrame(gameTime, frameTime, timingMode);
        float stepsize = Math.max(frameTime / 2000f, MIN_STEPSIZE);
        w.step(stepsize);
    }

    private void addGeometry(Space s, BranchGroup bg) {
        new Plane(s, new Vector3(0.0f, 1.0f, 0.0f), new Vector3(0.0f, 0.0f, 0.0f));
        Box wall1 = new Box(s, 20.0f, 5.0f, 0.1f);
        wall1.setPosition(-5.0f, 0.0f, -15.0f);
        Box wall2 = new Box(s, 20.0f, 5.0f, 0.1f);
        wall2.setPosition(-5.0f, 0.0f, 5.0f);
        Box wall3 = new Box(s, 0.1f, 5.0f, 20.0f);
        wall3.setPosition(-15.0f, 0.0f, -5.0f);
        Box wall4 = new Box(s, 0.1f, 5.0f, 20.0f);
        wall4.setPosition(5.0f, 0.0f, -5.0f);
        Sphere ball = new Sphere(s, 1.0f);
        ballBody = new Body(w, Mass.createSphereTotal(2.0f, 1.0f));
        ball.setBody(ballBody);
        ballBody.setPosition(0, 5.2f, 0);
        makeTransporter(s, bg);
    }

    private Quaternion makeRotation(double d) {
        Quaternion q = new Quaternion();
        q.setEuler((float) d, 0, 0);
        return (q);
    }

    private Body makeShovel(Space s, BranchGroup bg, float pos) {
        Capsule c = new Capsule(s, 0.1f, 3);
        Body b = new Body(w, Mass.createCapsuleTotal(0.4f, 2, 0.1f, 2));
        c.setBody(b);
        Quaternion q = new Quaternion();
        q.setEuler(0, (float) Math.PI / 2, 0);
        b.setPosition(0, 4.0f, pos);
        b.setRotation(q);
        return (b);
    }

    private void makeTransporter(Space s, BranchGroup bg) {
        Box ramp1 = new Box(s, 3.0f, 0.5f, 2.5f);
        ramp1.setPosition(0.0f, 0.0f, -1.0f);
        ramp1.setQuaternion(makeRotation(Math.PI / 12));
        Box ramp2 = new Box(s, 3.0f, 0.5f, 2.5f);
        ramp2.setPosition(0.0f, 1.0f, -3.0f);
        ramp2.setQuaternion(makeRotation(Math.PI / 4));
        Box ramp3 = new Box(s, 3.0f, 0.5f, 2.5f);
        ramp3.setPosition(0.0f, 3.0f, -4.0f);
        ramp3.setQuaternion(makeRotation(5 * Math.PI / 12));
        Box ramp4 = new Box(s, 3.0f, 0.5f, 2.5f);
        ramp4.setPosition(0.0f, 5.0f, -4.0f);
        ramp4.setQuaternion(makeRotation(7 * Math.PI / 12));
        Box ramp5 = new Box(s, 3.0f, 0.5f, 2.5f);
        ramp5.setPosition(0.0f, 7.0f, -3.0f);
        ramp5.setQuaternion(makeRotation(9 * Math.PI / 12));
        Box ramp6 = new Box(s, 3.0f, 0.5f, 2.5f);
        ramp6.setPosition(0.0f, 8.0f, -1.0f);
        ramp6.setQuaternion(makeRotation(11 * Math.PI / 12));
        Box ramp7 = new Box(s, 3.0f, 0.5f, 2.4f);
        ramp7.setPosition(0.0f, 7.7f, 1.0f);
        ramp7.setQuaternion(makeRotation(15 * Math.PI / 12));
        Body shovBody = new Body(w, Mass.createCapsuleTotal(1.0f, 2, 0.1f, 6.0f));
        Capsule handle = new Capsule(s, 0.1f, 6.0f);
        handle.setBody(shovBody);
        shovBody.setPosition(2.3f, 4.0f, 0f);
        JointHinge shovelJoint = new JointHinge(w);
        shovelJoint.attach(shovBody, null);
        shovelJoint.setAnchor(0, 4.0f, 0);
        shovelJoint.setAxis(1, 0, 0);
        shovelJoint.getLimitMotor().setVelocity(10.0f);
        shovelJoint.getLimitMotor().setMaxForce(100.0f);
        Body shov1 = makeShovel(s, bg, -3.0f);
        JointHinge shov1J = new JointHinge(w);
        shov1J.attach(shov1, shovBody);
        shov1J.setAnchor(1.5f, 4.0f, -3.0f);
        shov1J.setAxis(1, 0, 0);
        Body shov2 = makeShovel(s, bg, 3.0f);
        JointHinge shov2J = new JointHinge(w);
        shov2J.attach(shov2, shovBody);
        shov2J.setAnchor(1.5f, 4.0f, 3.0f);
        shov2J.setAxis(1, 0, 0);
        Box above1 = new Box(s, 8.0f, 0.2f, 3.0f);
        above1.setPosition(-2.0f, 3.0f, 0f);
        Quaternion q = new Quaternion();
        q.setEuler(0f, 0f, 0.2f);
        above1.setQuaternion(q);
        Box above2 = new Box(s, 8.0f, 0.4f, 0.2f);
        above2.setPosition(-2.0f, 3.3f, -1.4f);
        above2.setQuaternion(q);
        Box above3 = new Box(s, 8.0f, 2.4f, 0.2f);
        above3.setPosition(-2.0f, 4.2f, 1.4f);
        above3.setQuaternion(q);
        Box above4 = new Box(s, 6.0f, 0.2f, 3.0f);
        above4.setPosition(-8.0f, 1.0f, 0f);
        Quaternion q2 = new Quaternion();
        q2.setEuler(0f, 0f, -0.8f);
        above4.setQuaternion(q2);
        Box rille1 = new Box(s, 8.0f, 0.6f, 0.2f);
        rille1.setPosition(-5.5f, 0.3f, -1.0f);
        Box rille2 = new Box(s, 8.0f, 0.6f, 0.2f);
        rille2.setPosition(-5.5f, 0.3f, 1.0f);
        Box rille3 = new Box(s, 0.2f, 2.0f, 6.0f);
        rille3.setPosition(2.0f, 1.0f, 0.0f);
    }

    @Override
    public void onKeyPressed(KeyPressedEvent e, Key key) {
        switch(key.getKeyID()) {
            case RIGHT:
                ballBody.addForce(0.0f, 0.0f, -200.0f);
                break;
            case LEFT:
                ballBody.addForce(0.0f, 0.0f, 200.0f);
                break;
            case UP:
                ballBody.addForce(-200.0f, 0.0f, 0.0f);
                break;
            case DOWN:
                ballBody.addForce(200.0f, 0.0f, 0.0f);
                break;
            case ESCAPE:
                this.end();
                break;
        }
    }

    @Override
    public void onMouseButtonReleased(MouseButtonReleasedEvent e, MouseButton button) {
        if (button == MouseButtons.LEFT_BUTTON) {
            this.end();
        }
    }

    public Example2() throws Exception {
        super(120f);
        Tuple3f eyePosition = new Vector3f(10.0f, 10.0f, 5.0f);
        Tuple3f viewFocus = new Vector3f(0.0f, 0.0f, 0.0f);
        Tuple3f vecUp = new Vector3f(0.0f, 1.0f, 0.0f);
        Xith3DEnvironment env = new Xith3DEnvironment(eyePosition, viewFocus, vecUp, this);
        canvas = Canvas3DFactory.createWindowed(800, 600, "Example 2");
        this.addFPSListener(new CanvasFPSListener(canvas));
        InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
        BranchGroup bg = new BranchGroup();
        env.addPerspectiveBranch(bg);
        env.addCanvas(canvas);
        w = new World();
        new NewtonGravity(w, new Vector3(0, -9.81f, 0));
        Space s = new SimpleSpace(null);
        SurfaceParameters surfaceParams = new SurfaceParameters(40f);
        new CollisionManager(w, s, surfaceParams, true, 10000);
        addGeometry(s, bg);
        binder = new XithManager(w, s, bg);
        this.begin();
    }

    /**
     * Starts the example
     * @param args
     */
    public static void main(String[] args) throws Exception {
        new Example2();
    }
}
