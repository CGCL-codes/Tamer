package org.ode4j.drawstuff.internal;

import static org.cpp4j.Cstdio.fprintf;
import static org.cpp4j.Cstdio.stderr;
import org.ode4j.drawstuff.DrawStuff;
import org.ode4j.drawstuff.DrawStuff.DS_TEXTURE_NUMBER;
import org.ode4j.drawstuff.DrawStuff.dsFunctions;
import org.ode4j.math.DMatrix3C;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.OdeHelper;

public class DrawStuffNull implements DrawStuffApi {

    private static volatile boolean _run = true;

    @Override
    public void dsDrawBox(float[] pos, float[] R, float[] sides) {
    }

    @Override
    public void dsDrawCapsule(float[] pos, float[] R, float length, float radius) {
    }

    @Override
    public void dsDrawCylinder(float[] pos, float[] R, float length, float radius) {
    }

    @Override
    public void dsDrawLine(float[] pos1, float[] pos2) {
    }

    @Override
    public void dsDrawSphere(float[] pos, float[] R, float radius) {
    }

    @Override
    public void dsSetColor(float red, float green, float blue) {
    }

    @Override
    public void dsSetTexture(DS_TEXTURE_NUMBER texture_number) {
    }

    @Override
    public void dsSimulationLoop(String[] args, int window_width, int window_height, dsFunctions fn) {
        boolean initial_pause = false;
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("-pause")) initial_pause = true;
        }
        dsPlatformSimLoop(window_width, window_height, fn, initial_pause);
    }

    private static boolean firsttime = true;

    void dsPlatformSimLoop(int window_width, int window_height, dsFunctions fn, boolean initial_pause) {
        if (firsttime) {
            System.err.println();
            System.err.print("Using ode4j version: " + OdeHelper.getVersion());
            System.err.println("  [" + OdeHelper.getConfiguration() + "]");
            System.err.println();
            fprintf(stderr, "\n" + "Simulation test environment v%d.%02d\n" + "   Ctrl-P : pause / unpause (or say `-pause' on command line).\n" + "   Ctrl-O : single step when paused.\n" + "   Ctrl-T : toggle textures (or say `-notex' on command line).\n" + "   Ctrl-S : toggle shadows (or say `-noshadow' on command line).\n" + "   Ctrl-V : print current viewpoint coordinates (x,y,z,h,p,r).\n" + "   Ctrl-W : write frames to ppm files: frame/frameNNN.ppm\n" + "   Ctrl-X : exit.\n" + "\n" + "Change the camera position by clicking + dragging in the window.\n" + "   Left button - pan and tilt.\n" + "   Right button - forward and sideways.\n" + "   Left + Right button (or middle button) - sideways and up.\n" + "\n", DrawStuff.DS_VERSION >> 8, DrawStuff.DS_VERSION & 0xff);
            firsttime = false;
        }
        fn.start();
        long startTime = System.currentTimeMillis() + 5000;
        long fps = 0;
        _run = true;
        while (_run) {
            fn.step(false);
            if (startTime > System.currentTimeMillis()) {
                fps++;
            } else {
                long timeUsed = 5000 + (startTime - System.currentTimeMillis());
                startTime = System.currentTimeMillis() + 5000;
                System.out.println(fps + " frames in " + (float) (timeUsed / 1000f) + " seconds = " + (fps / (timeUsed / 1000f)));
                fps = 0;
            }
        }
        fn.stop();
    }

    @Override
    public double dsElapsedTime() {
        return 0;
    }

    @Override
    public void dsGetViewpoint(float[] xyz, float[] hpr) {
    }

    @Override
    public void dsSetViewpoint(float[] xyz, float[] hpr) {
    }

    @Override
    public void dsStop() {
        _run = false;
    }

    @Override
    public void dsDrawBox(DVector3C pos, DMatrix3C R, DVector3C sides) {
    }

    @Override
    public void dsDrawCapsule(DVector3C pos, DMatrix3C R, float length, float radius) {
    }

    @Override
    public void dsDrawConvex(DVector3C pos, DMatrix3C R, double[] _planes, int _planecount, double[] _points, int _pointcount, int[] _polygons) {
    }

    @Override
    public void dsDrawCylinder(DVector3C pos, DMatrix3C R, float length, float radius) {
    }

    @Override
    public void dsDrawLine(DVector3C pos1, DVector3C pos2) {
    }

    @Override
    public void dsDrawSphere(DVector3C pos, DMatrix3C R, float radius) {
    }

    @Override
    public void dsSetColorAlpha(float red, float green, float blue, float alpha) {
    }

    @Override
    public void dsSetDrawMode(int mode) {
    }

    @Override
    public void dsDrawTriangle(DVector3C pos, DMatrix3C rot, float[] v, int i, int j, int k, boolean solid) {
    }

    @Override
    public void dsDrawTriangle(DVector3C pos, DMatrix3C r, DVector3C v0, DVector3C v1, DVector3C v2, boolean solid) {
    }

    @Override
    public void dsDrawTriangle(DVector3C pos, DMatrix3C R, float[] v0, float[] v1, float[] v2, boolean solid) {
    }
}
