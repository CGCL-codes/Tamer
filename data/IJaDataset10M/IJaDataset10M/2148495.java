package vidis.ui.model.impl;

import java.awt.Color;
import javax.media.opengl.GL;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import org.apache.log4j.Logger;
import vidis.data.var.IVariableContainer;
import vidis.data.var.vars.AVariable;
import vidis.sim.Simulator;
import vidis.ui.config.Configuration;
import vidis.ui.events.IVidisEvent;
import vidis.ui.model.structure.ASimObject;

public class Packet extends ASimObject {

    private Logger logger = Logger.getLogger(Packet.class);

    public Packet(IVariableContainer c, Link link) {
        super(c);
        link.addPacket(this);
    }

    private static int displayListId = -1;

    private double position = Math.random() * 360;

    private void drawText(GL gl, String text, double angle, double x, double y, double z, Vector3d move) {
        if (Configuration.DISPLAY_WIREFRAME) gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
        gl.glPushMatrix();
        gl.glCullFace(GL.GL_FRONT);
        gl.glFrontFace(GL.GL_CW);
        gl.glTranslated(0.0 + move.x, 0.8 + move.y, 0.0 + move.z);
        gl.glScaled(0.001, 0.001, 0.001);
        gl.glRotated(angle, x, y, z);
        gl.glRotated(Configuration.LOOK_ANGLE_X, -1, 0, 0);
        gl.glRotated(Configuration.LOOK_ANGLE_Y, 0, -1, 0);
        textRenderer.begin3DRendering();
        textRenderer.setUseVertexArrays(false);
        textRenderer.draw3D(text, 0f, 0f, 0f, 1f);
        textRenderer.end3DRendering();
        gl.glPopMatrix();
        if (Configuration.DISPLAY_WIREFRAME) gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
    }

    @Override
    public void renderObject(GL gl) {
        setColors(getVariableColor1(), getVariableColor2());
        useColor(gl, getVariableColor1());
        useMaterial(gl);
        if (displayListId == -1) {
            preRenderObject(gl);
        }
        if (!Simulator.getInstance().getPlayer().isPaused() && getPosition() != null) {
            try {
                gl.glRotated(position, getPosition().x, getPosition().y, getPosition().z);
                position += Math.random() * 10 + 4;
            } catch (NullPointerException e) {
                logger.warn(e);
            }
        }
        gl.glCallList(displayListId);
    }

    public void preRenderObject(GL gl) {
        requireTextRenderer();
        displayListId = gl.glGenLists(1);
        gl.glNewList(displayListId, GL.GL_COMPILE);
        glut.glutSolidSphere(.15f, 6, 6);
        gl.glEndList();
    }

    @Override
    protected void handleEvent(IVidisEvent e) {
    }

    public Point3d getPosition() {
        try {
            Tuple3d pos = (Tuple3d) getVariableById(AVariable.COMMON_IDENTIFIERS.POSITION).getData();
            return new Point3d(pos);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public double getHitRadius() {
        return 0.15;
    }

    private boolean mouse = false;

    public void onMouseIn() {
        mouse = true;
    }

    public void onMouseOut() {
        mouse = false;
    }

    public String getId() {
        try {
            return (String) getVariableById(AVariable.COMMON_IDENTIFIERS.ID).getData();
        } catch (Exception e) {
            return "no id";
        }
    }

    @Override
    public void renderObjectText(GL gl) {
        String text = "";
        textRenderer.setColor(Color.black);
        try {
            text = getVariableById(AVariable.COMMON_IDENTIFIERS.NAME).getData().toString();
        } catch (NullPointerException e) {
        } finally {
            if (text.length() > 0) {
                gl.glPushMatrix();
                drawText(gl, text, 0, 0, 1, 0, new Vector3d(0, 0, 0));
                gl.glPopMatrix();
            }
        }
    }

    public void kill() {
        try {
            guiObj.getParent().getParent().removeChild(guiObj);
        } catch (NullPointerException e) {
        }
    }

    @Override
    protected Color getDefaultColor() {
        return Color.green;
    }

    @Override
    protected boolean isMouseOver() {
        return mouse;
    }
}
