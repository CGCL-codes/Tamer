package serfsoftherealm.rendering;

import org.lwjgl.opengl.GL11;
import serfsoftherealm.Paths;
import serfsoftherealm.texture.lwjgl.LWJGLTextureArea;
import serfsoftherealm.texture.lwjgl.LWJGLTextureManager;

public class BackgroundRenderer extends Renderer {

    private LWJGLTextureManager textureMgr;

    public BackgroundRenderer(LWJGLTextureManager textureMgr) {
        this.textureMgr = textureMgr;
    }

    public void render(float dt) {
        super.render(dt);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, sceneWidth, sceneHeight, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        LWJGLTextureArea bg = textureMgr.acquire(Paths.BACKGROUND);
        float left = (sceneWidth - 2.0f) / 2.0f;
        float right = left + 2.0f;
        float top = 0;
        float bottom = sceneHeight;
        float[] vertices = { left, top, right, bottom };
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, bg.getGLIdentifier());
        GL11.glBegin(GL11.GL_TRIANGLES);
        RenderUtils.drawSimple2DQuad(vertices, bg.getCoords());
        GL11.glEnd();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    @Override
    public void pick(int x, int y, int eventButton) {
    }
}
