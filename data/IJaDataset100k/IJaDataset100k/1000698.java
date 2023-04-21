package ponkOut.graphics;

import java.nio.DoubleBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class GameGraphicsObjectsManager extends GraphicsObjectsManager {

    private DoubleBuffer clipPlaneBuffer;

    private DoubleBuffer fakePlaneBuffer;

    private static CubeMap cubeMap;

    public static void init() {
        cubeMap = new CubeMap("cubeMap.tga", "cubeMap.tga", "cubeMap_top.tga", "cubeMap.tga", "cubeMap.tga", "cubeMap.tga");
    }

    public GameGraphicsObjectsManager() {
        double eqn[] = { 0.0, 0.0, 1.0, -Board.surfaceZPosition };
        clipPlaneBuffer = BufferUtils.createDoubleBuffer(4);
        clipPlaneBuffer.put(eqn).flip();
        double fakeEqn[] = { 0.0, 0.0, 0.0, 0.0 };
        fakePlaneBuffer = BufferUtils.createDoubleBuffer(4);
        fakePlaneBuffer.put(fakeEqn).flip();
    }

    @Override
    public void drawScene() {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, 0.0f, Board.surfaceZPosition);
        GL11.glScalef(1.0f, 1.0f, -1.0f);
        GL11.glTranslatef(0.0f, 0.0f, -Board.surfaceZPosition);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        Board.drawSurfaceIntoStencilBuffer();
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 1);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
        GL11.glCullFace(GL11.GL_FRONT);
        Lighting.updatePositions();
        cubeMap.update(true);
        GL11.glClipPlane(GL11.GL_CLIP_PLANE0, clipPlaneBuffer);
        Skybox.draw(true);
        drawObjects();
        GL11.glClipPlane(GL11.GL_CLIP_PLANE0, fakePlaneBuffer);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glPopMatrix();
        cubeMap.update(false);
        Lighting.updatePositions();
        Board.draw();
        Skybox.draw(false);
        drawObjects();
        drawHUD();
    }
}
