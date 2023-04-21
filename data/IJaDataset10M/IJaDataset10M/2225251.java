package com.dukesoftware.ongakumusou3d.opengl.model3d.album;

import static javax.media.opengl.GL.GL_QUADS;
import java.awt.Font;
import javax.media.opengl.GL;
import com.dukesoftware.ongakumusou3d.data.primitive.Album;
import com.dukesoftware.utils.opengl.model.FrameSquare;
import com.dukesoftware.utils.opengl.tool.GLUtils;
import com.dukesoftware.utils.opengl.tool.MaterialRegistry;
import com.dukesoftware.utils.opengl.tool.model.ModelAdapterNoAlpha;
import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;

/**
 * 
 * <p></p>
 * <h5>update history</h5> 
 * <p></p>
 * @author 
 * @since 2007/04/03
 * @version last update 2007/04/03
 */
public class AlbumCaseTray extends ModelAdapterNoAlpha implements AlbumModel {

    public static final float WIDTH = 14.2f;

    public static final float HEIGHT = 12.4f;

    public static final float DEPTH = 0.8f;

    public static final float WIDTH_EDGE = 1.3f;

    public static final float WIDTH_EDGE_FRONT = 1.2f;

    public static final float DEPTH_LEFT = 1.0f;

    public static final float WIDTH2 = WIDTH / 2;

    public static final float HEIGHT2 = HEIGHT / 2;

    protected Album album;

    private static Texture TRAY_TEXTURE;

    private static final int FONT_SIZE = 9;

    private static final int FONT_STYLE = Font.PLAIN;

    private static final String FONT_NAME = "SansSerif";

    protected static final TextRenderer renderer = new TextRenderer(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE), true, false);

    private int albumCaseTrayListNum;

    public static final void loadTextures() {
        TRAY_TEXTURE = GLUtils.createTexture("src\\ongakumusou\\opengl\\model3d\\tray.PNG");
    }

    public String name() {
        return "Album Case Tray";
    }

    public void renderBody(GL gl) {
        setMaterial(gl);
        gl.glCallList(albumCaseTrayListNum);
    }

    public void renderBody2(GL gl) {
        gl.glCallList(albumCaseTrayListNum);
    }

    /**
	 * @param gl
	 */
    protected void commonDraw(GL gl) {
        gl.glBegin(GL_QUADS);
        gl.glVertex2f(-WIDTH2, HEIGHT2);
        gl.glVertex2f(WIDTH2, HEIGHT2);
        gl.glVertex2f(WIDTH2, -HEIGHT2);
        gl.glVertex2f(-WIDTH2, -HEIGHT2);
        gl.glEnd();
        gl.glBegin(GL_QUADS);
        gl.glVertex3f(WIDTH2, HEIGHT2, 0f);
        gl.glVertex3f(WIDTH2, HEIGHT2, DEPTH);
        gl.glVertex3f(WIDTH2, -HEIGHT2, DEPTH);
        gl.glVertex3f(WIDTH2, -HEIGHT2, 0f);
        gl.glEnd();
        gl.glBegin(GL_QUADS);
        gl.glVertex3f(-WIDTH2, HEIGHT2, DEPTH_LEFT);
        gl.glVertex3f(-WIDTH2, HEIGHT2, 0f);
        gl.glVertex3f(-WIDTH2, -HEIGHT2, 0f);
        gl.glVertex3f(-WIDTH2, -HEIGHT2, DEPTH_LEFT);
        gl.glEnd();
        TRAY_TEXTURE.enable();
        TRAY_TEXTURE.bind();
        gl.glBegin(GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-WIDTH2, HEIGHT2, DEPTH_LEFT);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(-WIDTH2, -HEIGHT2, DEPTH_LEFT);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(-WIDTH2 + WIDTH_EDGE_FRONT, -HEIGHT2, DEPTH_LEFT);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(-WIDTH2 + WIDTH_EDGE_FRONT, HEIGHT2, DEPTH_LEFT);
        gl.glEnd();
        TRAY_TEXTURE.disable();
        MaterialRegistry.setMaterialTrayGray(gl);
        gl.glBegin(GL_QUADS);
        gl.glVertex3f(-WIDTH2 + WIDTH_EDGE, HEIGHT2, DEPTH);
        gl.glVertex3f(-WIDTH2 + WIDTH_EDGE, -HEIGHT2, DEPTH);
        gl.glVertex3f(WIDTH2, -HEIGHT2, DEPTH);
        gl.glVertex3f(WIDTH2, HEIGHT2, DEPTH);
        gl.glEnd();
        gl.glBegin(GL_QUADS);
        gl.glVertex3f(-WIDTH2 + WIDTH_EDGE_FRONT, HEIGHT2, DEPTH_LEFT);
        gl.glVertex3f(-WIDTH2 + WIDTH_EDGE_FRONT, -HEIGHT2, DEPTH_LEFT);
        gl.glVertex3f(-WIDTH2 + WIDTH_EDGE, -HEIGHT2, DEPTH);
        gl.glVertex3f(-WIDTH2 + WIDTH_EDGE, HEIGHT2, DEPTH);
        gl.glEnd();
        MaterialRegistry.setMaterialCDCaseSide(gl);
        gl.glBegin(GL_QUADS);
        gl.glVertex3f(-WIDTH2, HEIGHT2, 0f);
        gl.glVertex3f(-WIDTH2, HEIGHT2, DEPTH);
        gl.glVertex3f(WIDTH2, HEIGHT2, DEPTH);
        gl.glVertex3f(WIDTH2, HEIGHT2, 0f);
        gl.glEnd();
        gl.glBegin(GL_QUADS);
        gl.glVertex3f(-WIDTH2, HEIGHT2, DEPTH);
        gl.glVertex3f(-WIDTH2, HEIGHT2, DEPTH_LEFT);
        gl.glVertex3f(-WIDTH2 + WIDTH_EDGE_FRONT, HEIGHT2, DEPTH_LEFT);
        gl.glVertex3f(-WIDTH2 + WIDTH_EDGE, HEIGHT2, DEPTH);
        gl.glEnd();
        gl.glBegin(GL_QUADS);
        gl.glVertex3f(-WIDTH2, -HEIGHT2, 0f);
        gl.glVertex3f(WIDTH2, -HEIGHT2, 0f);
        gl.glVertex3f(WIDTH2, -HEIGHT2, DEPTH);
        gl.glVertex3f(-WIDTH2, -HEIGHT2, DEPTH);
        gl.glEnd();
        gl.glBegin(GL_QUADS);
        gl.glVertex3f(-WIDTH2, -HEIGHT2, DEPTH);
        gl.glVertex3f(-WIDTH2 + WIDTH_EDGE, -HEIGHT2, DEPTH);
        gl.glVertex3f(-WIDTH2 + WIDTH_EDGE_FRONT, -HEIGHT2, DEPTH_LEFT);
        gl.glVertex3f(-WIDTH2, -HEIGHT2, DEPTH_LEFT);
        gl.glEnd();
        MaterialRegistry.setMaterialCDCaseEdge(gl);
        gl.glPushMatrix();
        gl.glRotatef(90, 0, 1, 0);
        gl.glTranslatef(-DEPTH / 2, 0f, WIDTH2 + 0.001f);
        FrameSquare.draw(gl, DEPTH, HEIGHT, 0.1f);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glRotatef(270, 0, 1, 0);
        gl.glTranslatef(DEPTH_LEFT / 2, 0f, WIDTH2 + 0.001f);
        FrameSquare.draw(gl, DEPTH_LEFT, HEIGHT, 0.1f);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glRotatef(180, 0, 1, 0);
        gl.glTranslatef(0f, 0f, 0.001f);
        FrameSquare.draw(gl, WIDTH, HEIGHT, 0.1f);
        gl.glPopMatrix();
    }

    @Override
    public void init(GL gl) {
        albumCaseTrayListNum = gl.glGenLists(1);
        gl.glNewList(albumCaseTrayListNum, GL.GL_COMPILE);
        commonDraw(gl);
        gl.glEndList();
        System.out.println("Initialize AlbumCaseTray...");
    }

    public void renderBody(GL gl, int key) {
        if (album != null) {
            setMaterial(gl);
            gl.glLoadName(key);
            gl.glCallList(albumCaseTrayListNum);
        }
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
