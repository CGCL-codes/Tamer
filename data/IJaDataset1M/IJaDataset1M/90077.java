package org.easyway.effects.translator;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.easyway.annotations.Optimized;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.objects.BaseObject;
import org.easyway.objects.texture.TextureCompact;
import org.easyway.objects.texture.TextureFBO;
import org.easyway.system.StaticRef;
import org.easyway.system.state.OpenGLState;
import org.easyway.utils.ImageUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@Optimized
public class FixedStripesOut extends BaseObject implements IDrawing, ILayerID {

    private static final long serialVersionUID = 1578623115107939214L;

    protected int layer;

    /**
	 * the drawing sheet
	 */
    private int idLayer = -1;

    protected TextureCompact screen;

    protected TextureFBO dinamicTexture;

    protected TextureCompact linkedTexture;

    protected IntBuffer effect;

    protected int size;

    protected int stepx;

    protected long time;

    public FixedStripesOut(long time, int size) {
        this(time, size, autoAddToLists, 3);
    }

    public FixedStripesOut(long time, int size, boolean autoAddToLists) {
        this(time, size, autoAddToLists, 3);
    }

    public FixedStripesOut(long time, int size, int idLayer) {
        this(time, size, autoAddToLists, idLayer);
    }

    public FixedStripesOut(long time, int size, boolean autoAddToLists, int idLayer) {
        super(autoAddToLists);
        StaticRef.layers[idLayer].add(this);
        screen = ImageUtils.getScreenShot();
        this.time = time * 1000000;
        this.size = size;
        if (size % 2 == 0) {
            stepx = size / 2;
        } else {
            stepx = ((int) (size / 2)) + 1;
        }
        effect = makeEffect();
        linkedTexture = ImageUtils.makeTexture(screen.width, screen.height);
        linkedTexture.isSolid = false;
        dinamicTexture = new TextureFBO(linkedTexture);
    }

    protected IntBuffer makeEffect() {
        IntBuffer buf = BufferUtils.createIntBuffer(4);
        GL11.glGenTextures(buf);
        ByteBuffer data = BufferUtils.createByteBuffer(size * stepx * 4);
        data.rewind();
        int count = 1, dir = 1;
        for (int y = size - 1, x; y >= 0; --y) {
            for (x = 0; x < stepx; ++x) {
                data.put((byte) 255);
                data.put((byte) 255);
                data.put((byte) 255);
                if (x <= count) data.put((byte) 255); else data.put((byte) 0);
            }
            if (count == stepx - 1) dir = -1;
            count += dir;
        }
        data.rewind();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, buf.get(0));
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, stepx, size, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
        return buf;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayere(int layer) {
        this.layer = layer;
    }

    float dec = 0;

    public void render() {
        float step = (float) ((float) StaticRef.core.getElaspedTime() / (float) time);
        dec += step * (float) stepx;
        if (dec >= stepx) {
            destroy();
            return;
        }
        OpenGLState.disableBlending();
        OpenGLState.enableAlphaTest();
        dinamicTexture.startDrawing();
        {
            GL11.glLoadIdentity();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, effect.get(0));
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(dec / (float) stepx, (float) StaticRef.getCamera().getHeight() / (float) size);
            GL11.glVertex2f(0, 0);
            GL11.glTexCoord2f(dec / (float) stepx, (float) StaticRef.getCamera().getHeight() / (float) size);
            GL11.glVertex2f(StaticRef.getCamera().getWidth(), 0);
            GL11.glTexCoord2f(dec / (float) stepx, 0);
            GL11.glVertex2f(StaticRef.getCamera().getWidth(), StaticRef.getCamera().getHeight());
            GL11.glTexCoord2f(dec / (float) stepx, 0);
            GL11.glVertex2f(0, StaticRef.getCamera().getHeight());
            GL11.glEnd();
            GL11.glColorMask(true, true, true, false);
            screen.bind();
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0, screen.yEnd);
            GL11.glVertex2f(0, 0);
            GL11.glTexCoord2f(screen.xEnd, screen.yEnd);
            GL11.glVertex2f(StaticRef.getCamera().getWidth(), 0);
            GL11.glTexCoord2f(screen.xEnd, 0);
            GL11.glVertex2f(StaticRef.getCamera().getWidth(), StaticRef.getCamera().getHeight());
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, StaticRef.getCamera().getHeight());
            GL11.glEnd();
            GL11.glColorMask(true, true, true, true);
        }
        dinamicTexture.endDrawing();
        linkedTexture.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, linkedTexture.yEnd);
        GL11.glVertex2f(0, 0);
        GL11.glTexCoord2f(linkedTexture.xEnd, linkedTexture.yEnd);
        GL11.glVertex2f(StaticRef.getCamera().getWidth(), 0);
        GL11.glTexCoord2f(linkedTexture.xEnd, 0);
        GL11.glVertex2f(StaticRef.getCamera().getWidth(), StaticRef.getCamera().getHeight());
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(0, StaticRef.getCamera().getHeight());
        GL11.glEnd();
    }

    public void onDestroy() {
        GL11.glDeleteTextures(effect);
    }

    public int getIdLayer() {
        return idLayer;
    }

    public void setIdLayer(int id) {
        if (idLayer != -1) {
            StaticRef.layers[idLayer].remove(this);
        }
        if (id < 0) {
            id = 0;
        } else if (id > StaticRef.layers.length) {
            id = StaticRef.layers.length;
        }
        idLayer = id;
        StaticRef.layers[idLayer].add(this);
    }
}
