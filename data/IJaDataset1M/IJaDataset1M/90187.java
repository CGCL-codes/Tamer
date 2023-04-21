package cat.quadriga.render.simple.materials;

import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import java.nio.FloatBuffer;
import javax.vecmath.Matrix4f;
import org.lwjgl.BufferUtils;
import cat.quadriga.render.simple.RenderManager;
import cat.quadriga.render.simple.VertexType;

public class WorldMatrixMaterial extends MaterialDecorator {

    public int world;

    private FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    public WorldMatrixMaterial(BaseMaterial base) {
        super(base);
    }

    @Override
    public BaseMaterial activateShortcut() {
        return activateShortcut;
    }

    @Override
    public BaseMaterial cleanUpShortcut() {
        return cleanUpShortcut;
    }

    @Override
    public void cleanUp() {
        cleanUpShortcut.activate();
    }

    @Override
    public void activate() {
        activateShortcut.activate();
    }

    @Override
    public void preRender(VertexType vt, Matrix4f worldMatrix, RenderManager rm) {
        RenderManager.matrixToBuffer(worldMatrix, fb);
        glUniformMatrix4(world, false, fb);
        preRenderShortcut.preRender(vt, worldMatrix, rm);
    }
}
