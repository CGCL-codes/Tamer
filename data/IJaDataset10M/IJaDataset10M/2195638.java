package org.jbox2d.testbed.tests;

import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.joints.PrismaticJoint;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.testbed.AbstractExample;
import org.jbox2d.testbed.TestbedMain;

public class MotorsAndLimits extends AbstractExample {

    RevoluteJoint m_joint1;

    RevoluteJoint m_joint2;

    PrismaticJoint m_joint3;

    public MotorsAndLimits(TestbedMain _parent) {
        super(_parent);
    }

    public String getExampleInstructions() {
        return "[l] toggles prismatic limit\n[m] toggles motor\n[p] reverses prismatic motor direction";
    }

    public String getName() {
        return "Motors And Limits";
    }

    public void create() {
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
            sd.setAsBox(2.0f, 0.5f);
            sd.density = 5.0f;
            sd.friction = 0.05f;
            BodyDef bd = new BodyDef();
            RevoluteJointDef rjd = new RevoluteJointDef();
            Body body = null;
            Body prevBody = ground;
            float y = 8.0f;
            bd.position.set(3.0f, y);
            body = m_world.createBody(bd);
            body.createShape(sd);
            body.setMassFromShapes();
            rjd.initialize(prevBody, body, new Vec2(0.0f, y));
            rjd.motorSpeed = 1.0f * MathUtils.PI;
            rjd.maxMotorTorque = 10000.0f;
            rjd.enableMotor = true;
            m_joint1 = (RevoluteJoint) m_world.createJoint(rjd);
            prevBody = body;
            bd.position.set(9.0f, y);
            body = m_world.createBody(bd);
            body.createShape(sd);
            body.setMassFromShapes();
            rjd.initialize(prevBody, body, new Vec2(6.0f, y));
            rjd.motorSpeed = 0.5f * MathUtils.PI;
            rjd.maxMotorTorque = 2000.0f;
            rjd.enableMotor = true;
            rjd.lowerAngle = -0.5f * MathUtils.PI;
            rjd.upperAngle = 0.5f * MathUtils.PI;
            rjd.enableLimit = true;
            m_joint2 = (RevoluteJoint) m_world.createJoint(rjd);
            bd.position.set(-10.0f, 10.0f);
            bd.angle = 0.5f * MathUtils.PI;
            body = m_world.createBody(bd);
            body.createShape(sd);
            body.setMassFromShapes();
            PrismaticJointDef pjd = new PrismaticJointDef();
            pjd.initialize(ground, body, new Vec2(-10.0f, 10.0f), new Vec2(1.0f, 0.0f));
            pjd.motorSpeed = 10.0f;
            pjd.maxMotorForce = 1000.0f;
            pjd.enableMotor = true;
            pjd.lowerTranslation = 0.0f;
            pjd.upperTranslation = 20.0f;
            pjd.enableLimit = true;
            m_joint3 = (PrismaticJoint) m_world.createJoint(pjd);
        }
    }

    public void preStep() {
        if (newKeyDown['l']) {
            m_joint2.enableLimit(!m_joint2.isLimitEnabled());
            m_joint3.enableLimit(!m_joint3.isLimitEnabled());
            m_joint2.getBody1().wakeUp();
            m_joint3.getBody2().wakeUp();
        }
        if (newKeyDown['m']) {
            m_joint1.enableMotor(!m_joint1.isMotorEnabled());
            m_joint2.enableMotor(!m_joint2.isMotorEnabled());
            m_joint3.enableMotor(!m_joint3.isMotorEnabled());
            m_joint2.getBody1().wakeUp();
            m_joint3.getBody2().wakeUp();
        }
        if (newKeyDown['p']) {
            m_joint3.getBody2().wakeUp();
            m_joint3.setMotorSpeed(-m_joint3.getMotorSpeed());
            settings.pause = false;
        }
    }

    public void postStep() {
        float torque1 = m_joint1.getMotorTorque();
        float torque2 = m_joint2.getMotorTorque();
        float force3 = m_joint3.getMotorForce();
        m_debugDraw.drawString(5, m_textLine, "Motor Torque = " + torque1 + ", " + torque2 + " : Motor Force = " + force3, white);
        m_textLine += textLineHeight;
    }
}
