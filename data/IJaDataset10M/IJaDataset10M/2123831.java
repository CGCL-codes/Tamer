package org.jmol.viewer;

import org.jmol.g3d.Graphics3D;
import javax.vecmath.Point3f;
import javax.vecmath.Point3i;

class BbcageRenderer extends ShapeRenderer {

    final Point3i[] screens = new Point3i[8];

    {
        for (int i = 8; --i >= 0; ) screens[i] = new Point3i();
    }

    void render() {
        Bbcage bbcage = (Bbcage) shape;
        short mad = bbcage.mad;
        if (mad == 0) return;
        short colix = bbcage.colix;
        if (colix == 0) colix = Graphics3D.OLIVE;
        render(viewer, g3d, mad, colix, frame.bboxVertices, screens);
    }

    static void render(Viewer viewer, Graphics3D g3d, short mad, short colix, Point3f[] vertices, Point3i[] screens) {
        int zSum = 0;
        for (int i = 8; --i >= 0; ) {
            viewer.transformPoint(vertices[i], screens[i]);
            zSum += screens[i].z;
        }
        int widthPixels = mad;
        if (mad >= 20) {
            widthPixels = viewer.scaleToScreen(zSum / 8, mad);
        }
        for (int i = 0; i < 24; i += 2) {
            if (mad < 0) g3d.drawDottedLine(colix, screens[Bbcage.edges[i]], screens[Bbcage.edges[i + 1]]); else g3d.fillCylinder(colix, Graphics3D.ENDCAPS_SPHERICAL, widthPixels, screens[Bbcage.edges[i]], screens[Bbcage.edges[i + 1]]);
        }
    }
}
