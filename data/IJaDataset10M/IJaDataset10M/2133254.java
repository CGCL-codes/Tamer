package org.jdesktop.j3d.examples.picking;

import javax.media.j3d.*;
import javax.vecmath.*;

class TetrahedronLA extends LineArray {

    TetrahedronLA() {
        super(12, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        Point3f verts[] = new Point3f[4];
        Color3f colors[] = new Color3f[4];
        verts[0] = new Point3f(1.0f, 1.0f, 1.0f);
        verts[1] = new Point3f(1.0f, -1.0f, -1.0f);
        verts[2] = new Point3f(-1.0f, -1.0f, 1.0f);
        verts[3] = new Point3f(-1.0f, 1.0f, -1.0f);
        colors[0] = new Color3f(1.0f, 0.0f, 0.0f);
        colors[1] = new Color3f(0.0f, 1.0f, 0.0f);
        colors[2] = new Color3f(0.0f, 0.0f, 1.0f);
        colors[3] = new Color3f(1.0f, 1.0f, 0.0f);
        Point3f pnts[] = new Point3f[12];
        Color3f clrs[] = new Color3f[12];
        pnts[0] = verts[0];
        clrs[0] = colors[0];
        pnts[1] = verts[1];
        clrs[1] = colors[1];
        pnts[2] = verts[1];
        clrs[2] = colors[1];
        pnts[3] = verts[2];
        clrs[3] = colors[2];
        pnts[4] = verts[2];
        clrs[4] = colors[2];
        pnts[5] = verts[0];
        clrs[5] = colors[0];
        pnts[6] = verts[1];
        clrs[6] = colors[1];
        pnts[7] = verts[3];
        clrs[7] = colors[3];
        pnts[8] = verts[2];
        clrs[8] = colors[2];
        pnts[9] = verts[3];
        clrs[9] = colors[3];
        pnts[10] = verts[0];
        clrs[10] = colors[0];
        pnts[11] = verts[3];
        clrs[11] = colors[3];
        setCoordinates(0, pnts);
        setColors(0, clrs);
    }
}
