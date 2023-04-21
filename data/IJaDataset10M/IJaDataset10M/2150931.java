package net.sf.breed.orbiter.ui.jogl.draw;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.media.opengl.GL;
import net.sf.breed.orbiter.model.OrekitOrbit;
import net.sf.breed.orbiter.model.OrekitPosition;
import net.sf.breed.util.joglib.JoglibContext;
import net.sf.breed.util.joglib.JoglibFontCollection;
import net.sf.breed.util.joglib.JoglibLight;
import net.sf.breed.util.joglib.JoglibMaterial;
import net.sf.breed.util.joglib.JoglibTextureCollection;
import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.UTCScale;

/**
 * Draws a satellite into a GL context.
 * 
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since 7 Jan 2009
 */
public class SatelliteDrawer {

    /** The texture id for the satellite body. */
    private int bodyTextureId;

    /** The texture id for the solar panels. */
    private int solarPanelTextureId;

    /** The position of the satellite. */
    private OrekitPosition position;

    /** The date formatter. */
    private SimpleDateFormat dateFormat;

    /**
     * @param position The satellite position (moving).
     */
    public SatelliteDrawer(OrekitPosition position) {
        this.position = position;
    }

    /**
     * @param context The context.
     */
    public void initSatellite(JoglibContext context) {
        JoglibTextureCollection textures = context.getTextureCollection();
        bodyTextureId = textures.loadImage("res/image/board_32x32.png");
        solarPanelTextureId = textures.loadImage("res/image/solar_panel_32x32.png");
    }

    /**
     * Draws a satellite in one position of specified orbit.
     * 
     * @param context The context.
     */
    public void drawSatellite(JoglibContext context) {
        float sizeCorrectionFactor = 0.05f;
        final GL gl = context.getGL();
        JoglibMaterial.setWhite(gl);
        gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
        drawBody(context, sizeCorrectionFactor);
        drawSolarPanels(context, sizeCorrectionFactor);
        gl.glLoadIdentity();
        gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
    }

    /**
     * @param context The context.
     * @param size The size of the box, e.g. 1.
     */
    private void drawBody(JoglibContext context, float size) {
        final GL gl = context.getGL();
        final float[] pointCoords = position.getPointCoords();
        gl.glTranslatef(pointCoords[0], pointCoords[1], pointCoords[2]);
        gl.glScalef(size, size, size);
        gl.glBindTexture(GL.GL_TEXTURE_2D, bodyTextureId);
        drawCube(gl);
    }

    /**
     * Draws the solar panels.
     * 
     * @param context The context.
     * @param size The size of the panels.
     */
    private void drawSolarPanels(JoglibContext context, float size) {
        final GL gl = context.getGL();
        final float panelWid = 8.0f;
        final float panelHei = 1.5f;
        gl.glBindTexture(GL.GL_TEXTURE_2D, solarPanelTextureId);
        float[] p1 = new float[] { -1.2f, +panelHei, 0f };
        float[] p2 = new float[] { -(1.2f + panelWid), +panelHei, 0f };
        float[] p3 = new float[] { -(1.2f + panelWid), -panelHei, 0f };
        float[] p4 = new float[] { -1.2f, -panelHei, 0f };
        drawQuad(gl, p1, p2, p3, p4);
        p1[0] = -p1[0];
        p2[0] = -p2[0];
        p3[0] = -p3[0];
        p4[0] = -p4[0];
        drawQuad(gl, p1, p2, p3, p4);
    }

    /**
     * Draws a cube.
     * 
     * @param gl The GL context.
     */
    public void drawCube(GL gl) {
        gl.glBegin(GL.GL_QUADS);
        final float[] frontUL = new float[] { -1.0f, -1.0f, 1.0f };
        final float[] frontUR = new float[] { 1.0f, -1.0f, 1.0f };
        final float[] frontLR = new float[] { 1.0f, 1.0f, 1.0f };
        final float[] frontLL = new float[] { -1.0f, 1.0f, 1.0f };
        final float[] backUL = new float[] { -1.0f, -1.0f, -1.0f };
        final float[] backLL = new float[] { -1.0f, 1.0f, -1.0f };
        final float[] backLR = new float[] { 1.0f, 1.0f, -1.0f };
        final float[] backUR = new float[] { 1.0f, -1.0f, -1.0f };
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3fv(frontUL, 0);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3fv(frontUR, 0);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3fv(frontLR, 0);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3fv(frontLL, 0);
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3fv(backUL, 0);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3fv(backLL, 0);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3fv(backLR, 0);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3fv(backUR, 0);
        gl.glNormal3f(0.0f, 1.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3fv(backLL, 0);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3fv(frontLL, 0);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3fv(frontLR, 0);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3fv(backLR, 0);
        gl.glNormal3f(0.0f, -1.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3fv(backUL, 0);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3fv(backUR, 0);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3fv(frontUR, 0);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3fv(frontUL, 0);
        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3fv(backUR, 0);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3fv(backLR, 0);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3fv(frontLR, 0);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3fv(frontUR, 0);
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3fv(backUL, 0);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3fv(frontUL, 0);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3fv(frontLL, 0);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3fv(backLL, 0);
        gl.glEnd();
    }

    /**
     * Draws a quad.
     * 
     * @param gl The GL context.
     * @param p1 Point 1.
     * @param p2 Point 2.
     * @param p3 Point 3.
     * @param p4 Point 4.
     */
    public static void drawQuad(GL gl, float[] p1, float[] p2, float[] p3, float[] p4) {
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3fv(p1, 0);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3fv(p2, 0);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3fv(p3, 0);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3fv(p4, 0);
        gl.glEnd();
    }

    /**
     * Draws the current date at the current position.
     * 
     * @param context The Joglib context.
     * @param fonts The fonts collection.
     */
    public void drawText(JoglibContext context) {
        final JoglibFontCollection fonts = context.getFontCollection();
        final GL gl = context.getGL();
        gl.glLoadIdentity();
        gl.glTranslatef(0.3f, 1, 0.01f);
        gl.glRotatef(90f, 0, 0, 1);
        final boolean DATE_NOT_ELAPSED_TIME = false;
        String text = DATE_NOT_ELAPSED_TIME ? getDateString() : getTimeString();
        fonts.setFontScaleFactor(0.05f);
        fonts.drawString(text, 0, 0, 0);
        gl.glLoadIdentity();
    }

    /**
     * @return Returns the time elapsed since the first orbital point.
     */
    private String getTimeString() {
        int seconds = (int) position.getPointElapsedTime10();
        int minutes = (seconds / 60) % 60;
        int hours = (seconds / (60 * 60)) % 24;
        int days = (seconds / (60 * 60 * 24));
        StringBuilder time = new StringBuilder(128);
        if (hours < 10) {
            time.append("0");
        }
        time.append(hours);
        time.append(":");
        if (minutes < 10) {
            time.append("0");
        }
        time.append(minutes);
        if (days > 0) {
            time.append(" (+");
            time.append(days);
            time.append(" day");
            if (days > 1) {
                time.append("s");
            }
            time.append(")");
        }
        return time.toString();
    }

    /**
     * @return The current date string.
     */
    private String getDateString() {
        if (null == dateFormat) {
            String pattern = "yyyy-MM-dd hh:mm";
            dateFormat = new SimpleDateFormat(pattern);
        }
        String text = null;
        AbsoluteDate pointDate = position.getPointDate10();
        try {
            Date date = null != pointDate ? pointDate.toDate(UTCScale.getInstance()) : new Date();
            text = dateFormat.format(date);
        } catch (OrekitException exc) {
            exc.printStackTrace();
            text = exc.getMessage();
        }
        return text;
    }

    /**
     * Sets the light as coming from the satellite.
     * 
     * @param light The light representative.
     * @param orekitOrbit The current orbit.
     * @param isModKeyPressed Whether a modify key is pressed. Defaults to
     *        false.
     */
    public void setLight(JoglibLight light, OrekitOrbit orekitOrbit, boolean isModKeyPressed) {
        final float[] pointCoords = position.getPointCoords();
        final float[] lightPos = new float[4];
        System.arraycopy(pointCoords, 0, lightPos, 0, 3);
        if (isModKeyPressed) {
            light.setSpotLight(lightPos, 1f, 0.5f);
        } else {
            light.setSpotLight(lightPos, 0.2f, 0.8f);
        }
    }
}
