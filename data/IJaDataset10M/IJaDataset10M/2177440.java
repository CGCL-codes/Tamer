package org.jzy3d.chart.graphs;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import org.jzy3d.chart.ChartView;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.rendering.canvas.ICanvas;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.scene.Scene;

public class GraphView extends ChartView {

    public GraphView(Scene scene, ICanvas canvas, Quality quality) {
        super(scene, canvas, quality);
    }

    public Coord3d projectMouse(int x, int y) {
        return cam.screenToModel(getCurrentGL(), glu, new Coord3d((float) x, (float) y, 0));
    }

    protected void correctCameraPositionForIncludingTextLabels(GL2 gl, GLU glu, float left, float right) {
    }

    protected GLU glu = new GLU();
}
