package de.nameless.graphicEngine.lib;

import javax.media.opengl.GL;

public class NEWater extends NEQuad {

    public static float flow;

    public NEWater() {
        this.enableSprites(1, 2, 0, 0);
        this.setTexture("Water.png");
        this.xAngel = 90;
        this.setColor(0.2f, 0.2f, 0.5f, 0.25f);
    }

    @Override
    public void drawGL(GL gl, float TBM) {
        float Y = boxH * spriteY;
        float X = boxW * spriteX + flow;
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(X, Y);
        gl.glVertex3f(-width, -height, 0.0f);
        gl.glTexCoord2f(X + boxW, Y);
        gl.glVertex3f(width, -height, 0.0f);
        gl.glTexCoord2f(X + boxW, Y + boxH);
        gl.glVertex3f(width, height, 0.0f);
        gl.glTexCoord2f(X, Y + boxH);
        gl.glVertex3f(-width, height, 0.0f);
        gl.glEnd();
    }
}
