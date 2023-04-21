package de.matthiasmann.twl.renderer.lwjgl;

import de.matthiasmann.twl.renderer.AnimationState;
import de.matthiasmann.twl.renderer.FontCache;
import org.lwjgl.opengl.GL11;

/**
 * A font render cache - uses display lists
 * 
 * @author Matthias Mann
 */
public class LWJGLFontCache implements FontCache {

    private final LWJGLRenderer renderer;

    private final LWJGLFont font;

    private int id;

    private int width;

    private int height;

    private int[] multiLineInfo;

    private int numLines;

    LWJGLFontCache(LWJGLRenderer renderer, LWJGLFont font) {
        this.renderer = renderer;
        this.font = font;
        this.id = GL11.glGenLists(1);
    }

    public void draw(AnimationState as, int x, int y) {
        if (id != 0) {
            LWJGLFont.FontState fontState = font.evalFontState(as);
            renderer.tintState.setColor(fontState.color);
            GL11.glPushMatrix();
            GL11.glTranslatef(x + fontState.offsetX, y + fontState.offsetY, 0f);
            GL11.glCallList(id);
            if (fontState.style != 0) {
                if (numLines > 0) {
                    font.drawLines(fontState, 0, 0, multiLineInfo, numLines);
                } else {
                    font.drawLine(fontState, 0, 0, width);
                }
            }
            GL11.glPopMatrix();
        }
    }

    public void destroy() {
        if (id != 0) {
            GL11.glDeleteLists(id, 1);
            id = 0;
        }
    }

    boolean startCompile() {
        if (id != 0) {
            GL11.glNewList(id, GL11.GL_COMPILE);
            this.numLines = 0;
            return true;
        }
        return false;
    }

    void endCompile(int width, int height) {
        GL11.glEndList();
        this.width = width;
        this.height = height;
    }

    int[] getMultiLineInfo(int numLines) {
        if (multiLineInfo == null || multiLineInfo.length < numLines) {
            multiLineInfo = new int[numLines];
        }
        this.numLines = numLines;
        return multiLineInfo;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
