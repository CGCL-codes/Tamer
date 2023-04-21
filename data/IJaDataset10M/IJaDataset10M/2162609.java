package org.jbox2d.testbed.tests;

import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.controllers.BuoyancyController;
import org.jbox2d.dynamics.controllers.BuoyancyControllerDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.testbed.AbstractExample;
import org.jbox2d.testbed.TestbedMain;

/**
 * @author eric
 *
 */
public class BuoyancyTest extends AbstractExample {

    /**
	 * @param _parent
	 */
    public BuoyancyTest(TestbedMain _parent) {
        super(_parent);
    }

    public String getName() {
        return "Buoyancy Test";
    }

    public void create() {
        BuoyancyControllerDef bcd = new BuoyancyControllerDef();
        bcd.offset = 15;
        bcd.normal.set(0, 1);
        bcd.density = 2;
        bcd.linearDrag = 2;
        bcd.angularDrag = 1;
        BuoyancyController bc = (BuoyancyController) m_world.createController(bcd);
        Body ground = null;
        {
            PolygonDef sd = new PolygonDef();
            sd.setAsBox(50.0f, 10.0f);
            BodyDef bd = new BodyDef();
            bd.position.set(0.0f, -10.0f);
            ground = m_world.createBody(bd);
            ground.createShape(sd);
        }
        {
            PolygonDef sd = new PolygonDef();
            sd.setAsBox(0.5f, 0.125f);
            sd.density = 2.0f;
            sd.friction = 0.2f;
            RevoluteJointDef jd = new RevoluteJointDef();
            final int numPlanks = 30;
            Body prevBody = ground;
            for (int i = 0; i < numPlanks; ++i) {
                BodyDef bd = new BodyDef();
                bd.position.set(-14.5f + 1.0f * i, 5.0f);
                Body body = m_world.createBody(bd);
                body.createShape(sd);
                body.setMassFromShapes();
                Vec2 anchor = new Vec2(-15.0f + 1.0f * i, 5.0f);
                jd.initialize(prevBody, body, anchor);
                m_world.createJoint(jd);
                prevBody = body;
                bc.addBody(body);
            }
            Vec2 anchor = new Vec2(-15.0f + 1.0f * numPlanks, 5.0f);
            jd.initialize(prevBody, ground, anchor);
            m_world.createJoint(jd);
        }
        for (int i = 0; i < 2; ++i) {
            PolygonDef sd = new PolygonDef();
            sd.vertices.add(new Vec2(-0.5f, 0.0f));
            sd.vertices.add(new Vec2(0.5f, 0.0f));
            sd.vertices.add(new Vec2(0.0f, 1.5f));
            sd.density = 1.0f;
            BodyDef bd = new BodyDef();
            bd.position.set(-8.0f + 8.0f * i, 12.0f);
            Body body = m_world.createBody(bd);
            body.createShape(sd);
            body.setMassFromShapes();
            bc.addBody(body);
        }
        for (int i = 0; i < 3; ++i) {
            CircleDef sd = new CircleDef();
            sd.radius = 0.5f;
            sd.density = 1.0f;
            BodyDef bd = new BodyDef();
            bd.position.set(-6.0f + 6.0f * i, 10.0f);
            Body body = m_world.createBody(bd);
            body.createShape(sd);
            body.setMassFromShapes();
            bc.addBody(body);
        }
    }
}
