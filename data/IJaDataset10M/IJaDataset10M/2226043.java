package gui.visualization.draw.graph;

import gui.visualization.QualityPreset;
import gui.visualization.control.graph.GLFlowGraphControl;
import gui.visualization.control.graph.GLSimpleNodeControl;
import java.awt.Color;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import opengl.drawingutils.GLColor;
import opengl.drawingutils.GLVector;
import opengl.framework.abs.AbstractDrawable;

public class GLSimpleNode extends AbstractDrawable<GLEdge, GLSimpleNodeControl> {

    int nodeDisplayMode = GLU.GLU_FILL;

    GLColor nodeColor = new GLColor(154, 154, 147);

    double radius;

    private static QualityPreset qualityPreset = QualityPreset.HighQuality;

    public GLSimpleNode(GLSimpleNodeControl control) {
        super(control);
        this.radius = 13 * 0.1;
        position = new GLVector(control.getPosition());
    }

    @Override
    public void performDrawing(GL gl) {
        super.performDrawing(gl);
    }

    @Override
    public void performStaticDrawing(GL gl) {
        beginDraw(gl);
        drawNode(gl);
        staticDrawAllChildren(gl);
        endDraw(gl);
    }

    final GLColor lineColor = new GLColor(Color.black);

    protected void drawNode(GL gl) {
        glu.gluQuadricDrawStyle(quadObj, nodeDisplayMode);
        double xOffset = -this.getControl().getXPosition();
        double yOffset = this.getControl().getYPosition();
        nodeColor.draw(gl);
        nodeDisplayMode = GLU.GLU_FILL;
        glu.gluSphere(quadObj, radius, qualityPreset.nodeSlices, qualityPreset.nodeStacks);
        lineColor.draw(gl);
    }

    @Override
    public void update() {
    }
}
