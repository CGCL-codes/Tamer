package org.jzy3d.plot3d.primitives.axes;

import java.nio.FloatBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Vector3d;
import org.jzy3d.plot3d.primitives.axes.layout.AxeBoxLayout;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.rendering.canvas.ICanvas;
import org.jzy3d.plot3d.rendering.view.Camera;
import org.jzy3d.plot3d.rendering.view.View;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;
import org.jzy3d.plot3d.text.ITextRenderer;
import org.jzy3d.plot3d.text.align.Halign;
import org.jzy3d.plot3d.text.align.Valign;
import org.jzy3d.plot3d.text.overlay.TextOverlay;
import org.jzy3d.plot3d.text.renderers.TextBitmapRenderer;
import com.jogamp.common.nio.Buffers;

/**The AxeBox displays a box with front face invisible and ticks labels.
 * @author Martin Pernollet
 */
public class AxeBox implements IAxe {

    public AxeBox(BoundingBox3d bbox) {
        this(bbox, new AxeBoxLayout());
    }

    public AxeBox(BoundingBox3d bbox, IAxeLayout layout) {
        this.layout = layout;
        if (bbox.valid()) setAxe(bbox); else setAxe(new BoundingBox3d(-1, 1, -1, 1, -1, 1));
        wholeBounds = new BoundingBox3d();
        init();
    }

    protected void init() {
        setScale(new Coord3d(1.0f, 1.0f, 1.0f));
    }

    public void dispose() {
        if (txtRenderer != null) txtRenderer.dispose();
    }

    public ITextRenderer getTextRenderer() {
        return txt;
    }

    public void setTextRenderer(ITextRenderer renderer) {
        txt = renderer;
    }

    public TextOverlay getExperimentalTextRenderer() {
        return txtRenderer;
    }

    /** Initialize a text renderer that will reference the target canvas for getting
	 * its dimensions (in order to convert coordinates from OpenGL2 to Java2d).
	 * 
	 * @param canvas
	 */
    public void setExperimentalTextOverlayRenderer(ICanvas canvas) {
        txtRenderer = new TextOverlay(canvas);
    }

    public View getView() {
        return view;
    }

    /** When setting a current view, the AxeBox can know the view is on mode CameraMode.TOP,
	 * and optimize some axis placement.*/
    public void setView(View view) {
        this.view = view;
    }

    /***********************************************************/
    @Override
    public void setAxe(BoundingBox3d bbox) {
        this.boxBounds = bbox;
        setAxeBox(bbox.getXmin(), bbox.getXmax(), bbox.getYmin(), bbox.getYmax(), bbox.getZmin(), bbox.getZmax());
    }

    @Override
    public BoundingBox3d getBoxBounds() {
        return boxBounds;
    }

    @Override
    public IAxeLayout getLayout() {
        return layout;
    }

    /** Return the boundingBox of this axis, including the volume occupied by the texts.
	 * This requires calling {@link draw()} before, which computes actual ticks position in 3d,
	 * and updates the bounds.
	 */
    public BoundingBox3d getWholeBounds() {
        return wholeBounds;
    }

    public Coord3d getCenter() {
        return center;
    }

    /**  Set the scaling factor that are applyed on this object before 
	 * GL2 commands.*/
    public void setScale(Coord3d scale) {
        this.scale = scale;
    }

    /**
	 * Draws the AxeBox. The camera is used to determine which axis is closest
	 * to the ur point ov view, in order to decide for an axis on which 
	 * to diplay the tick values.
	 */
    @Override
    public void draw(GL2 gl, GLU glu, Camera camera) {
        gl.glLoadIdentity();
        gl.glScalef(scale.x, scale.y, scale.z);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glFrontFace(GL2.GL_CCW);
        gl.glCullFace(GL2.GL_FRONT);
        quadIsHidden = getHiddenQuads(gl, camera);
        if (layout.isFaceDisplayed()) {
            Color quadcolor = layout.getQuadColor();
            gl.glPolygonMode(GL2.GL_BACK, GL2.GL_FILL);
            gl.glColor4f(quadcolor.r, quadcolor.g, quadcolor.b, quadcolor.a);
            gl.glLineWidth(1.0f);
            gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
            gl.glPolygonOffset(1.0f, 1.0f);
            drawCube(gl, GL2.GL_RENDER);
            gl.glDisable(GL2.GL_POLYGON_OFFSET_FILL);
        }
        Color gridcolor = layout.getGridColor();
        gl.glPolygonMode(GL2.GL_BACK, GL2.GL_LINE);
        gl.glColor4f(gridcolor.r, gridcolor.g, gridcolor.b, gridcolor.a);
        gl.glLineWidth(1);
        drawCube(gl, GL2.GL_RENDER);
        gl.glPolygonMode(GL2.GL_BACK, GL2.GL_LINE);
        gl.glColor4f(gridcolor.r, gridcolor.g, gridcolor.b, gridcolor.a);
        gl.glLineWidth(1);
        gl.glLineStipple(1, (short) 0xAAAA);
        gl.glEnable(GL2.GL_LINE_STIPPLE);
        for (int quad = 0; quad < 6; quad++) if (!quadIsHidden[quad]) drawGridOnQuad(gl, quad);
        gl.glDisable(GL2.GL_LINE_STIPPLE);
        wholeBounds.reset();
        wholeBounds.add(boxBounds);
        if (xrange > 0 && layout.isXTickLabelDisplayed()) {
            if (view != null && view.getViewMode().equals(ViewPositionMode.TOP)) {
                BoundingBox3d bbox = drawTicks(gl, glu, camera, 1, AXE_X, layout.getXTickColor(), Halign.LEFT, Valign.TOP);
                wholeBounds.add(bbox);
            } else {
                int xselect = findClosestXaxe(camera);
                if (xselect >= 0) {
                    BoundingBox3d bbox = drawTicks(gl, glu, camera, xselect, AXE_X, layout.getXTickColor());
                    wholeBounds.add(bbox);
                } else {
                    BoundingBox3d bbox = drawTicks(gl, glu, camera, 2, AXE_X, layout.getXTickColor(), Halign.CENTER, Valign.TOP);
                    wholeBounds.add(bbox);
                }
            }
        }
        if (yrange > 0 && layout.isYTickLabelDisplayed()) {
            if (view != null && view.getViewMode().equals(ViewPositionMode.TOP)) {
                BoundingBox3d bbox = drawTicks(gl, glu, camera, 2, AXE_Y, layout.getYTickColor(), Halign.LEFT, Valign.GROUND);
                wholeBounds.add(bbox);
            } else {
                int yselect = findClosestYaxe(camera);
                if (yselect >= 0) {
                    BoundingBox3d bbox = drawTicks(gl, glu, camera, yselect, AXE_Y, layout.getYTickColor());
                    wholeBounds.add(bbox);
                } else {
                    BoundingBox3d bbox = drawTicks(gl, glu, camera, 1, AXE_Y, layout.getYTickColor(), Halign.RIGHT, Valign.GROUND);
                    wholeBounds.add(bbox);
                }
            }
        }
        if (zrange > 0 && layout.isZTickLabelDisplayed()) {
            if (view != null && view.getViewMode().equals(ViewPositionMode.TOP)) {
            } else {
                int zselect = findClosestZaxe(camera);
                if (zselect >= 0) {
                    BoundingBox3d bbox = drawTicks(gl, glu, camera, zselect, AXE_Z, layout.getZTickColor());
                    wholeBounds.add(bbox);
                }
            }
        }
        gl.glDisable(GL2.GL_CULL_FACE);
    }

    /**
	 * Set the parameters and data of the AxeBox.
	 */
    protected void setAxeBox(float xmin, float xmax, float ymin, float ymax, float zmin, float zmax) {
        center = new Coord3d((xmax + xmin) / 2, (ymax + ymin) / 2, (zmax + zmin) / 2);
        xrange = xmax - xmin;
        yrange = ymax - ymin;
        zrange = zmax - zmin;
        quadx = new float[6][4];
        quady = new float[6][4];
        quadz = new float[6][4];
        quadx[0][0] = xmax;
        quady[0][0] = ymin;
        quadz[0][0] = zmax;
        quadx[0][1] = xmax;
        quady[0][1] = ymin;
        quadz[0][1] = zmin;
        quadx[0][2] = xmax;
        quady[0][2] = ymax;
        quadz[0][2] = zmin;
        quadx[0][3] = xmax;
        quady[0][3] = ymax;
        quadz[0][3] = zmax;
        quadx[1][0] = xmin;
        quady[1][0] = ymax;
        quadz[1][0] = zmax;
        quadx[1][1] = xmin;
        quady[1][1] = ymax;
        quadz[1][1] = zmin;
        quadx[1][2] = xmin;
        quady[1][2] = ymin;
        quadz[1][2] = zmin;
        quadx[1][3] = xmin;
        quady[1][3] = ymin;
        quadz[1][3] = zmax;
        quadx[2][0] = xmax;
        quady[2][0] = ymax;
        quadz[2][0] = zmax;
        quadx[2][1] = xmax;
        quady[2][1] = ymax;
        quadz[2][1] = zmin;
        quadx[2][2] = xmin;
        quady[2][2] = ymax;
        quadz[2][2] = zmin;
        quadx[2][3] = xmin;
        quady[2][3] = ymax;
        quadz[2][3] = zmax;
        quadx[3][0] = xmin;
        quady[3][0] = ymin;
        quadz[3][0] = zmax;
        quadx[3][1] = xmin;
        quady[3][1] = ymin;
        quadz[3][1] = zmin;
        quadx[3][2] = xmax;
        quady[3][2] = ymin;
        quadz[3][2] = zmin;
        quadx[3][3] = xmax;
        quady[3][3] = ymin;
        quadz[3][3] = zmax;
        quadx[4][0] = xmin;
        quady[4][0] = ymin;
        quadz[4][0] = zmax;
        quadx[4][1] = xmax;
        quady[4][1] = ymin;
        quadz[4][1] = zmax;
        quadx[4][2] = xmax;
        quady[4][2] = ymax;
        quadz[4][2] = zmax;
        quadx[4][3] = xmin;
        quady[4][3] = ymax;
        quadz[4][3] = zmax;
        quadx[5][0] = xmax;
        quady[5][0] = ymin;
        quadz[5][0] = zmin;
        quadx[5][1] = xmin;
        quady[5][1] = ymin;
        quadz[5][1] = zmin;
        quadx[5][2] = xmin;
        quady[5][2] = ymax;
        quadz[5][2] = zmin;
        quadx[5][3] = xmax;
        quady[5][3] = ymax;
        quadz[5][3] = zmin;
        normx = new float[6];
        normy = new float[6];
        normz = new float[6];
        normx[0] = xmax;
        normy[0] = 0;
        normz[0] = 0;
        normx[1] = xmin;
        normy[1] = 0;
        normz[1] = 0;
        normx[2] = 0;
        normy[2] = ymax;
        normz[2] = 0;
        normx[3] = 0;
        normy[3] = ymin;
        normz[3] = 0;
        normx[4] = 0;
        normy[4] = 0;
        normz[4] = zmax;
        normx[5] = 0;
        normy[5] = 0;
        normz[5] = zmin;
        int na = 4;
        int np = 2;
        int nq = 2;
        int i;
        axeXquads = new int[na][nq];
        axeYquads = new int[na][nq];
        axeZquads = new int[na][nq];
        i = 0;
        axeXquads[i][0] = 4;
        axeXquads[i][1] = 3;
        i = 1;
        axeXquads[i][0] = 3;
        axeXquads[i][1] = 5;
        i = 2;
        axeXquads[i][0] = 5;
        axeXquads[i][1] = 2;
        i = 3;
        axeXquads[i][0] = 2;
        axeXquads[i][1] = 4;
        i = 0;
        axeYquads[i][0] = 4;
        axeYquads[i][1] = 0;
        i = 1;
        axeYquads[i][0] = 0;
        axeYquads[i][1] = 5;
        i = 2;
        axeYquads[i][0] = 5;
        axeYquads[i][1] = 1;
        i = 3;
        axeYquads[i][0] = 1;
        axeYquads[i][1] = 4;
        i = 0;
        axeZquads[i][0] = 3;
        axeZquads[i][1] = 0;
        i = 1;
        axeZquads[i][0] = 0;
        axeZquads[i][1] = 2;
        i = 2;
        axeZquads[i][0] = 2;
        axeZquads[i][1] = 1;
        i = 3;
        axeZquads[i][0] = 1;
        axeZquads[i][1] = 3;
        axeXx = new float[na][np];
        axeXy = new float[na][np];
        axeXz = new float[na][np];
        axeYx = new float[na][np];
        axeYy = new float[na][np];
        axeYz = new float[na][np];
        axeZx = new float[na][np];
        axeZy = new float[na][np];
        axeZz = new float[na][np];
        i = 0;
        axeXx[i][0] = xmin;
        axeXy[i][0] = ymin;
        axeXz[i][0] = zmax;
        axeXx[i][1] = xmax;
        axeXy[i][1] = ymin;
        axeXz[i][1] = zmax;
        i = 1;
        axeXx[i][0] = xmin;
        axeXy[i][0] = ymin;
        axeXz[i][0] = zmin;
        axeXx[i][1] = xmax;
        axeXy[i][1] = ymin;
        axeXz[i][1] = zmin;
        i = 2;
        axeXx[i][0] = xmin;
        axeXy[i][0] = ymax;
        axeXz[i][0] = zmin;
        axeXx[i][1] = xmax;
        axeXy[i][1] = ymax;
        axeXz[i][1] = zmin;
        i = 3;
        axeXx[i][0] = xmin;
        axeXy[i][0] = ymax;
        axeXz[i][0] = zmax;
        axeXx[i][1] = xmax;
        axeXy[i][1] = ymax;
        axeXz[i][1] = zmax;
        i = 0;
        axeYx[i][0] = xmax;
        axeYy[i][0] = ymin;
        axeYz[i][0] = zmax;
        axeYx[i][1] = xmax;
        axeYy[i][1] = ymax;
        axeYz[i][1] = zmax;
        i = 1;
        axeYx[i][0] = xmax;
        axeYy[i][0] = ymin;
        axeYz[i][0] = zmin;
        axeYx[i][1] = xmax;
        axeYy[i][1] = ymax;
        axeYz[i][1] = zmin;
        i = 2;
        axeYx[i][0] = xmin;
        axeYy[i][0] = ymin;
        axeYz[i][0] = zmin;
        axeYx[i][1] = xmin;
        axeYy[i][1] = ymax;
        axeYz[i][1] = zmin;
        i = 3;
        axeYx[i][0] = xmin;
        axeYy[i][0] = ymin;
        axeYz[i][0] = zmax;
        axeYx[i][1] = xmin;
        axeYy[i][1] = ymax;
        axeYz[i][1] = zmax;
        i = 0;
        axeZx[i][0] = xmax;
        axeZy[i][0] = ymin;
        axeZz[i][0] = zmin;
        axeZx[i][1] = xmax;
        axeZy[i][1] = ymin;
        axeZz[i][1] = zmax;
        i = 1;
        axeZx[i][0] = xmax;
        axeZy[i][0] = ymax;
        axeZz[i][0] = zmin;
        axeZx[i][1] = xmax;
        axeZy[i][1] = ymax;
        axeZz[i][1] = zmax;
        i = 2;
        axeZx[i][0] = xmin;
        axeZy[i][0] = ymax;
        axeZz[i][0] = zmin;
        axeZx[i][1] = xmin;
        axeZy[i][1] = ymax;
        axeZz[i][1] = zmax;
        i = 3;
        axeZx[i][0] = xmin;
        axeZy[i][0] = ymin;
        axeZz[i][0] = zmin;
        axeZx[i][1] = xmin;
        axeZy[i][1] = ymin;
        axeZz[i][1] = zmax;
        layout.getXTicks(xmin, xmax);
        layout.getYTicks(ymin, ymax);
        layout.getZTicks(zmin, zmax);
    }

    /**
	 * Make all GL2 calls allowing to build a cube with 6 separate quads.
	 * Each quad is indexed from 0.0f to 5.0f using glPassThrough,
	 * and may be traced in feedback mode when mode=GL2.GL_FEEDBACK 
	 */
    protected void drawCube(GL2 gl, int mode) {
        for (int q = 0; q < 6; q++) {
            if (mode == GL2.GL_FEEDBACK) gl.glPassThrough((float) q);
            gl.glBegin(GL2.GL_QUADS);
            for (int v = 0; v < 4; v++) {
                gl.glVertex3f(quadx[q][v], quady[q][v], quadz[q][v]);
            }
            gl.glEnd();
        }
    }

    /**
	 * Draw a grid on the desired quad.
	 */
    protected void drawGridOnQuad(GL2 gl, int quad) {
        if ((quad != 0) && (quad != 1)) {
            float[] xticks = layout.getXTicks();
            for (int t = 0; t < xticks.length; t++) {
                gl.glBegin(GL2.GL_LINES);
                gl.glVertex3f(xticks[t], quady[quad][0], quadz[quad][0]);
                gl.glVertex3f(xticks[t], quady[quad][2], quadz[quad][2]);
                gl.glEnd();
            }
        }
        if ((quad != 2) && (quad != 3)) {
            float[] yticks = layout.getYTicks();
            for (int t = 0; t < yticks.length; t++) {
                gl.glBegin(GL2.GL_LINES);
                gl.glVertex3f(quadx[quad][0], yticks[t], quadz[quad][0]);
                gl.glVertex3f(quadx[quad][2], yticks[t], quadz[quad][2]);
                gl.glEnd();
            }
        }
        if ((quad != 4) && (quad != 5)) {
            float[] zticks = layout.getZTicks();
            for (int t = 0; t < zticks.length; t++) {
                gl.glBegin(GL2.GL_LINES);
                gl.glVertex3f(quadx[quad][0], quady[quad][0], zticks[t]);
                gl.glVertex3f(quadx[quad][2], quady[quad][2], zticks[t]);
                gl.glEnd();
            }
        }
    }

    protected BoundingBox3d drawTicks(GL2 gl, GLU glu, Camera cam, int axis, int direction, Color color) {
        return drawTicks(gl, glu, cam, axis, direction, color, null, null);
    }

    protected BoundingBox3d drawTicks(GL2 gl, GLU glu, Camera cam, int axis, int direction, Color color, Halign hal, Valign val) {
        int quad_0;
        int quad_1;
        Halign hAlign;
        Valign vAlign;
        float tickLength = 20.0f;
        float axeLabelDist = 2.5f;
        BoundingBox3d ticksTxtBounds = new BoundingBox3d();
        if (direction == AXE_X) {
            quad_0 = axeXquads[axis][0];
            quad_1 = axeXquads[axis][1];
        } else if (direction == AXE_Y) {
            quad_0 = axeYquads[axis][0];
            quad_1 = axeYquads[axis][1];
        } else {
            quad_0 = axeZquads[axis][0];
            quad_1 = axeZquads[axis][1];
        }
        float xpos = normx[quad_0] + normx[quad_1];
        float ypos = normy[quad_0] + normy[quad_1];
        float zpos = normz[quad_0] + normz[quad_1];
        float xlab;
        float ylab;
        float zlab;
        float xdir = (normx[quad_0] + normx[quad_1]) - center.x;
        float ydir = (normy[quad_0] + normy[quad_1]) - center.y;
        float zdir = (normz[quad_0] + normz[quad_1]) - center.z;
        xdir = xdir == 0 ? 0 : xdir / Math.abs(xdir);
        ydir = ydir == 0 ? 0 : ydir / Math.abs(ydir);
        zdir = zdir == 0 ? 0 : zdir / Math.abs(zdir);
        String axeLabel;
        int dist = 1;
        if (direction == AXE_X) {
            xlab = center.x;
            ylab = axeLabelDist * (yrange / tickLength) * dist * ydir + ypos;
            zlab = axeLabelDist * (zrange / tickLength) * dist * zdir + zpos;
            axeLabel = layout.getXAxeLabel();
        } else if (direction == AXE_Y) {
            xlab = axeLabelDist * (xrange / tickLength) * dist * xdir + xpos;
            ylab = center.y;
            zlab = axeLabelDist * (zrange / tickLength) * dist * zdir + zpos;
            axeLabel = layout.getYAxeLabel();
        } else {
            xlab = axeLabelDist * (xrange / tickLength) * dist * xdir + xpos;
            ylab = axeLabelDist * (yrange / tickLength) * dist * ydir + ypos;
            zlab = center.z;
            axeLabel = layout.getZAxeLabel();
        }
        if ((direction == AXE_X && layout.isXAxeLabelDisplayed()) || (direction == AXE_Y && layout.isYAxeLabelDisplayed()) || (direction == AXE_Z && layout.isZAxeLabelDisplayed())) {
            Coord3d labelPosition = new Coord3d(xlab, ylab, zlab);
            if (txtRenderer != null) txtRenderer.appendText(gl, glu, cam, axeLabel, labelPosition, Halign.CENTER, Valign.CENTER, color); else {
                BoundingBox3d labelBounds = txt.drawText(gl, glu, cam, axeLabel, labelPosition, Halign.CENTER, Valign.CENTER, color);
                if (labelBounds != null) ticksTxtBounds.add(labelBounds);
            }
        }
        float ticks[];
        if (direction == AXE_X) ticks = layout.getXTicks(); else if (direction == AXE_Y) ticks = layout.getYTicks(); else ticks = layout.getZTicks();
        String tickLabel = "";
        gl.glColor3f(color.r, color.g, color.b);
        gl.glLineWidth(1);
        for (int t = 0; t < ticks.length; t++) {
            if (direction == AXE_X) {
                xpos = ticks[t];
                xlab = xpos;
                ylab = (yrange / tickLength) * ydir + ypos;
                zlab = (zrange / tickLength) * zdir + zpos;
                tickLabel = layout.getXTickRenderer().format(xpos);
            } else if (direction == AXE_Y) {
                ypos = ticks[t];
                xlab = (xrange / tickLength) * xdir + xpos;
                ylab = ypos;
                zlab = (zrange / tickLength) * zdir + zpos;
                tickLabel = layout.getYTickRenderer().format(ypos);
            } else {
                zpos = ticks[t];
                xlab = (xrange / tickLength) * xdir + xpos;
                ylab = (yrange / tickLength) * ydir + ypos;
                zlab = zpos;
                tickLabel = layout.getZTickRenderer().format(zpos);
            }
            Coord3d tickPosition = new Coord3d(xlab, ylab, zlab);
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex3f(xpos, ypos, zpos);
            gl.glVertex3f(xlab, ylab, zlab);
            gl.glEnd();
            if (hal == null) hAlign = cam.side(tickPosition) ? Halign.LEFT : Halign.RIGHT; else hAlign = hal;
            if (val == null) {
                if (direction == AXE_Z) vAlign = Valign.CENTER; else {
                    if (zdir > 0) vAlign = Valign.TOP; else vAlign = Valign.BOTTOM;
                }
            } else vAlign = val;
            if (txtRenderer != null) txtRenderer.appendText(gl, glu, cam, tickLabel, tickPosition, hAlign, vAlign, color); else {
                BoundingBox3d tickBounds = txt.drawText(gl, glu, cam, tickLabel, tickPosition, hAlign, vAlign, color);
                if (tickBounds != null) ticksTxtBounds.add(tickBounds);
            }
        }
        return ticksTxtBounds;
    }

    /**
     * Selects the closest displayable X axe from camera
     */
    protected int findClosestXaxe(Camera cam) {
        int na = 4;
        double[] distAxeX = new double[na];
        for (int a = 0; a < na; a++) {
            if (quadIsHidden[axeXquads[a][0]] ^ quadIsHidden[axeXquads[a][1]]) distAxeX[a] = new Vector3d(axeXx[a][0], axeXy[a][0], axeXz[a][0], axeXx[a][1], axeXy[a][1], axeXz[a][1]).distance(cam.getEye()); else distAxeX[a] = Double.MAX_VALUE;
        }
        for (int a = 0; a < na; a++) {
            if (distAxeX[a] < Double.MAX_VALUE) {
                if (center.z > (axeXz[a][0] + axeXz[a][1]) / 2) distAxeX[a] *= -1;
            }
        }
        return min(distAxeX);
    }

    /**
     * Selects the closest displayable Y axe from camera
     */
    protected int findClosestYaxe(Camera cam) {
        int na = 4;
        double[] distAxeY = new double[na];
        for (int a = 0; a < na; a++) {
            if (quadIsHidden[axeYquads[a][0]] ^ quadIsHidden[axeYquads[a][1]]) distAxeY[a] = new Vector3d(axeYx[a][0], axeYy[a][0], axeYz[a][0], axeYx[a][1], axeYy[a][1], axeYz[a][1]).distance(cam.getEye()); else distAxeY[a] = Double.MAX_VALUE;
        }
        for (int a = 0; a < na; a++) {
            if (distAxeY[a] < Double.MAX_VALUE) {
                if (center.z > (axeYz[a][0] + axeYz[a][1]) / 2) distAxeY[a] *= -1;
            }
        }
        return min(distAxeY);
    }

    /**
     * Selects the closest displayable Z axe from camera
     */
    protected int findClosestZaxe(Camera cam) {
        int na = 4;
        double[] distAxeZ = new double[na];
        for (int a = 0; a < na; a++) {
            if (quadIsHidden[axeZquads[a][0]] ^ quadIsHidden[axeZquads[a][1]]) distAxeZ[a] = new Vector3d(axeZx[a][0], axeZy[a][0], axeZz[a][0], axeZx[a][1], axeZy[a][1], axeZz[a][1]).distance(cam.getEye()); else distAxeZ[a] = Double.MAX_VALUE;
        }
        for (int a = 0; a < na; a++) {
            if (distAxeZ[a] < Double.MAX_VALUE) {
                Coord3d axeCenter = new Coord3d((axeZx[a][0] + axeZx[a][1]) / 2, (axeZy[a][0] + axeZy[a][1]) / 2, (axeZz[a][0] + axeZz[a][1]) / 2);
                if (!cam.side(axeCenter)) distAxeZ[a] *= -1;
            }
        }
        return min(distAxeZ);
    }

    /** Return the index of the minimum value contained in the input array of doubles.
	 * If no value is smaller than Double.MAX_VALUE, the returned index is -1.*/
    protected int min(double[] values) {
        double minv = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < values.length; i++) if (values[i] < minv) {
            minv = values[i];
            index = i;
        }
        return index;
    }

    /** Computes the visibility of each cube face. */
    protected boolean[] getHiddenQuads(GL gl, Camera cam) {
        boolean[] status = new boolean[6];
        Coord3d se = cam.getEye().div(scale);
        if (se.x <= center.x) {
            status[0] = false;
            status[1] = true;
        } else {
            status[0] = true;
            status[1] = false;
        }
        if (se.y <= center.y) {
            status[2] = false;
            status[3] = true;
        } else {
            status[2] = true;
            status[3] = false;
        }
        if (se.z <= center.z) {
            status[4] = false;
            status[5] = true;
        } else {
            status[4] = true;
            status[5] = false;
        }
        return status;
    }

    /**
	 *  Print out parameters of a gl call in 3dColor mode.
	 */
    protected int print3DcolorVertex(int size, int count, float[] buffer) {
        int i;
        int id = size - count;
        int veclength = 7;
        System.out.print("  [" + id + "]");
        for (i = 0; i < veclength; i++) {
            System.out.print(" " + buffer[size - count]);
            count = count - 1;
        }
        System.out.println();
        return count;
    }

    /**
	 * Print out display status of quads.
	 */
    protected void printHiddenQuads() {
        for (int t = 0; t < quadIsHidden.length; t++) if (quadIsHidden[t]) System.out.println("Quad[" + t + "] is not displayed"); else System.out.println("Quad[" + t + "] is displayed");
    }

    /******************************************************************/
    protected static final int PRECISION = 6;

    protected View view;

    protected ITextRenderer txt = new TextBitmapRenderer();

    protected TextOverlay txtRenderer;

    protected IAxeLayout layout;

    protected BoundingBox3d boxBounds;

    protected BoundingBox3d wholeBounds;

    protected Coord3d center;

    protected Coord3d scale;

    protected float xrange;

    protected float yrange;

    protected float zrange;

    protected float quadx[][];

    protected float quady[][];

    protected float quadz[][];

    protected float normx[];

    protected float normy[];

    protected float normz[];

    protected float axeXx[][];

    protected float axeXy[][];

    protected float axeXz[][];

    protected float axeYx[][];

    protected float axeYy[][];

    protected float axeYz[][];

    protected float axeZx[][];

    protected float axeZy[][];

    protected float axeZz[][];

    protected int axeXquads[][];

    protected int axeYquads[][];

    protected int axeZquads[][];

    protected boolean quadIsHidden[];

    protected static final int AXE_X = 0;

    protected static final int AXE_Y = 1;

    protected static final int AXE_Z = 2;
}
