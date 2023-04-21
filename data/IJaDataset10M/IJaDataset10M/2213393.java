package snucse.teamd;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import snucse.teamd.math.Vec2;

public class World {

    private Vec2 gravity;

    private Vector<Body> bodies;

    public Vector<Arbiter> arbiterList = new Vector<Arbiter>();

    private int iterations;

    private boolean isPlaying;

    public World() {
        setPlaying(true);
        gravity = new Vec2(0, -10f);
        bodies = new Vector<Body>();
        iterations = 20;
    }

    public void reset() {
        setPlaying(true);
        gravity = new Vec2(0, -10f);
        bodies = new Vector<Body>();
        iterations = 20;
    }

    public Vector<Body> getBodies() {
        return bodies;
    }

    public void setBodies(Vector<Body> bodies) {
        this.bodies = bodies;
    }

    public void addBody(Body body) {
        bodies.addElement(body);
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public void setGravityX(double x) {
        gravity.setX(x);
    }

    public void setGravityY(double y) {
        gravity.setY(y);
    }

    public boolean isExist(Arbiter arb, float dt) {
        boolean isTrue = false;
        Arbiter existArb;
        for (int i = 0; i < arbiterList.size(); i++) {
            existArb = arbiterList.get(i);
            existArb.timeout += dt;
            if ((existArb.b1.equals(arb.b1) && existArb.b2.equals(arb.b2)) || (existArb.b1.equals(arb.b2) && existArb.b2.equals(arb.b1))) {
                return true;
            }
        }
        return false;
    }

    private void update(float dt) {
        int j;
        Body b1, b2;
        SubTriangle t1, t2;
        Vector<Vec2> vertices;
        Vec2 dCOM;
        for (int i = 0; i < (int) bodies.size(); i++) {
            b1 = bodies.get(i);
            for (j = i + 1; j < bodies.size(); j++) {
                b2 = bodies.get(j);
                Arbiter arbiter = new Arbiter(b1, b2);
                if (Collide.detectCollision(b1, b2, arbiter)) {
                    if (!isExist(arbiter, dt)) {
                        arbiterList.addElement(arbiter);
                    }
                }
            }
        }
        for (int i = 0; i < arbiterList.size(); i++) {
            arbiterList.get(i).timeout += dt;
            if (arbiterList.get(i).timeout > 20f / 60f) {
                arbiterList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < (int) bodies.size(); ++i) {
            Body b = bodies.get(i);
            if (b.isEnv) continue;
            b.setTorque(b.friction * b.mass * b.radius);
            b.setVel(Vec2.plus(b.getVel(), Vec2.scalar(Vec2.plus(gravity, Vec2.scalar(b.force, 1 / b.getMass())), dt)));
            if (b.getAngularVelocity() > 0) {
                b.setAngularVelocity(b.getAngularVelocity() - (b.torque / b.I) * dt);
            } else {
                b.setAngularVelocity(b.getAngularVelocity() + (b.torque / b.I) * dt);
            }
        }
        for (int i = 0; i < arbiterList.size(); i++) {
            Arbiter arbiter = arbiterList.get(i);
            arbiter.ApplyImpulse();
            Body temp1 = arbiter.b1;
            Body temp2 = arbiter.b2;
        }
        for (int i = 0; i < (int) bodies.size(); i++) {
            b1 = bodies.get(i);
            if (b1.isEnv) {
                continue;
            }
            dCOM = new Vec2(b1.getVel().getX() * dt, b1.getVel().getY() * dt);
            b1.setRotAngle(b1.getRotAngle() + b1.getAngularVelocity() * dt);
            b1.setCenterOfMass(Vec2.plus(b1.getCenterOfMass(), dCOM));
            vertices = new Vector<Vec2>();
            for (j = 0; j < b1.vertices.size(); j++) {
                vertices.addElement(Vec2.plus(b1.vertices.get(j), dCOM));
            }
            b1.vertices = vertices;
            for (int k = 0; k < b1.subTriangles.size(); k++) {
                vertices = new Vector<Vec2>();
                t1 = b1.subTriangles.get(k);
                t1.com = Vec2.plus(t1.com, dCOM);
                for (j = 0; j < t1.tvertices.size(); j++) {
                    vertices.addElement(Vec2.plus(t1.tvertices.get(j), dCOM));
                }
                t1.tvertices = vertices;
            }
        }
    }

    void Step(float dt) {
        if (isPlaying) update(dt);
    }

    public Vec2 getGravity() {
        return gravity;
    }

    public void setGravity(Vec2 gravity) {
        this.gravity = gravity;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
}
