package org.ode4j.demo;

import org.ode4j.drawstuff.DrawStuff.dsFunctions;
import org.ode4j.math.DMatrix3;
import org.ode4j.math.DMatrix3C;
import org.ode4j.math.DQuaternion;
import org.ode4j.math.DVector3;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.DBox;
import org.ode4j.ode.DContactGeom;
import org.ode4j.ode.DContactGeomBuffer;
import org.ode4j.ode.DConvex;
import org.ode4j.ode.DGeom;
import org.ode4j.ode.DJointGroup;
import org.ode4j.ode.DSpace;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeHelper;
import org.ode4j.ode.internal.CollideBoxBox;
import org.ode4j.ode.internal.DxConvex;
import static org.ode4j.drawstuff.DrawStuff.*;
import static org.ode4j.ode.OdeMath.*;

class DemoConvexCD extends dsFunctions {

    private double planes[] = { 1.0f, 0.0f, 0.0f, 0.25f, 0.0f, 1.0f, 0.0f, 0.25f, 0.0f, 0.0f, 1.0f, 0.25f, -1.0f, 0.0f, 0.0f, 0.25f, 0.0f, -1.0f, 0.0f, 0.25f, 0.0f, 0.0f, -1.0f, 0.25f };

    private final int planecount = 6;

    private double points[] = { 0.25f, 0.25f, 0.25f, -0.25f, 0.25f, 0.25f, 0.25f, -0.25f, 0.25f, -0.25f, -0.25f, 0.25f, 0.25f, 0.25f, -0.25f, -0.25f, 0.25f, -0.25f, 0.25f, -0.25f, -0.25f, -0.25f, -0.25f, -0.25f };

    private final int pointcount = 8;

    private int polygons[] = { 4, 0, 2, 6, 4, 4, 1, 0, 4, 5, 4, 0, 1, 3, 2, 4, 3, 1, 5, 7, 4, 2, 3, 7, 6, 4, 5, 4, 6, 7 };

    private DGeom[] geoms;

    private DBox[] boxes = new DBox[2];

    private DConvex[] convex = new DConvex[2];

    private DSpace space;

    private DWorld world;

    private DJointGroup contactgroup;

    private DVector3 geom1pos = new DVector3(0.0, 0.250, 0.50);

    private DQuaternion geom1quat = new DQuaternion(1, 0, 0, 0);

    private boolean DumpInfo = true;

    private int drawmode = DS_WIREFRAME;

    private final DVector3C fixed_pos_0 = new DVector3(0.0, 0.0, 0.25);

    private final DMatrix3C fixed_rot_0 = new DMatrix3(1, 0, 0, 0, 1, 0, 0, 0, 1);

    private final DVector3C fixed_pos_1 = new DVector3(0.000000, 0.450000, 0.600000);

    private final DMatrix3C fixed_rot_1 = new DMatrix3(0.708311, -0.705472, -0.000000, 0.516939, 0.519297, -0.679785, 0.480067, 0.481293, 0.733034);

    public void start() {
        float[] xyz = new float[3], hpr = new float[3];
        dsGetViewpoint(xyz, hpr);
        hpr[0] += 7;
        dsSetViewpoint(xyz, hpr);
        convex[0] = OdeHelper.createConvex(space, planes, planecount, points, pointcount, polygons);
        convex[1] = OdeHelper.createConvex(space, planes, planecount, points, pointcount, polygons);
        boxes[0] = OdeHelper.createBox(space, 0.5, 0.5, 0.5);
        boxes[1] = OdeHelper.createBox(space, 0.5, 0.5, 0.5);
        geoms = convex;
        {
            convex[0].setPosition(fixed_pos_0);
            convex[1].setPosition(fixed_pos_1);
            convex[0].setRotation(fixed_rot_0);
            convex[1].setRotation(fixed_rot_1);
            boxes[0].setPosition(fixed_pos_0);
            boxes[1].setPosition(fixed_pos_1);
            boxes[0].setRotation(fixed_rot_0);
            boxes[1].setRotation(fixed_rot_1);
        }
    }

    private void simLoop(boolean pause) {
        int contactcount;
        final DVector3 ss = new DVector3(0.02, 0.02, 0.02);
        DContactGeomBuffer contacts = new DContactGeomBuffer(8);
        if (geoms == convex) contactcount = new DxConvex.CollideConvexConvex().dColliderFn(geoms[0], geoms[1], 8, contacts); else contactcount = new CollideBoxBox().dColliderFn(geoms[0], geoms[1], 8, contacts);
        DVector3C pos;
        DMatrix3C R;
        dsSetTexture(DS_TEXTURE_NUMBER.DS_WOOD);
        pos = geoms[0].getPosition();
        R = geoms[0].getRotation();
        dsSetColor(0.6f, 0.6f, 1);
        dsSetDrawMode(drawmode);
        dsDrawConvex(pos, R, planes, planecount, points, pointcount, polygons);
        dsSetDrawMode(DS_POLYFILL);
        pos = geoms[1].getPosition();
        R = geoms[1].getRotation();
        dsSetColor(0.4f, 1, 1);
        dsSetDrawMode(drawmode);
        dsDrawConvex(pos, R, planes, planecount, points, pointcount, polygons);
        dsSetDrawMode(DS_POLYFILL);
        DMatrix3 RI = new DMatrix3();
        RI.setIdentity();
        dsSetColor(1.0f, 0, 0);
        for (int i = 0; i < contactcount; ++i) {
            if (DumpInfo) {
                DContactGeom contact = contacts.get(i);
                System.out.print("Contact " + i + " Normal " + contact.normal + " Depth " + contact.depth + " Pos " + contact.pos + " ");
                if (contact.g1 == geoms[0]) {
                    System.out.println("Geoms 1 2");
                } else {
                    System.out.println("Geoms 2 1");
                }
            }
            dsDrawBox(contacts.get(i).pos, RI, ss);
        }
        if (DumpInfo) DumpInfo = false;
    }

    public void command(char cmd) {
        DQuaternion q = new DQuaternion();
        boolean changed = false;
        switch(cmd) {
            case 'w':
                geom1pos.add0(0.05);
                changed = true;
                break;
            case 'a':
                geom1pos.add1(-0.05);
                changed = true;
                break;
            case 's':
                geom1pos.add0(-0.05);
                changed = true;
                break;
            case 'd':
                geom1pos.add1(+0.05);
                changed = true;
                break;
            case 'e':
                geom1pos.add2(-0.05);
                changed = true;
                break;
            case 'q':
                geom1pos.add2(+0.05);
                changed = true;
                break;
            case 'i':
                dQFromAxisAndAngle(q, 0, 0, 1, 0.0174532925);
                dQMultiply0(geom1quat, geom1quat, q);
                changed = true;
                break;
            case 'j':
                dQFromAxisAndAngle(q, 1, 0, 0, 0.0174532925);
                dQMultiply0(geom1quat, geom1quat, q);
                changed = true;
                break;
            case 'k':
                dQFromAxisAndAngle(q, 0, 0, 1, -0.0174532925);
                dQMultiply0(geom1quat, geom1quat, q);
                changed = true;
                break;
            case 'l':
                dQFromAxisAndAngle(q, 1, 0, 0, -0.0174532925);
                dQMultiply0(geom1quat, geom1quat, q);
                changed = true;
                break;
            case 'm':
                if (drawmode != DS_POLYFILL) {
                    drawmode = DS_POLYFILL;
                } else {
                    drawmode = DS_WIREFRAME;
                }
                break;
            case 'n':
                if (geoms != convex) geoms = convex; else geoms = boxes;
                if (geoms == convex) {
                    System.out.println("CONVEX------------------------------------------------------>");
                } else {
                    System.out.println("BOX--------------------------------------------------------->");
                }
                break;
            default:
                dsPrint("received command %d (`%c')\n", cmd, cmd);
        }
        DumpInfo = true;
    }

    public static void main(String[] args) {
        new DemoConvexCD().demo(args);
    }

    private void demo(String[] args) {
        world = OdeHelper.createWorld();
        space = OdeHelper.createHashSpace(null);
        contactgroup = OdeHelper.createJointGroup();
        dsSimulationLoop(args, 400, 400, this);
        contactgroup.destroy();
        space.destroy();
        world.destroy();
    }

    @Override
    public void step(boolean pause) {
        simLoop(pause);
    }

    @Override
    public void stop() {
    }
}
