package cat.quadriga.render.simple.materials;

import static org.lwjgl.opengl.GL20.glUniform3;
import java.nio.FloatBuffer;
import javax.vecmath.Matrix4f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import org.lwjgl.BufferUtils;
import cat.quadriga.render.simple.RenderManager;
import cat.quadriga.render.simple.VertexType;

public class Vector3fUniformMaterial extends MaterialDecorator {

    public int uniformID;

    public String parameterName;

    private Vector3f parameter = new Vector3f();

    private FloatBuffer fb = BufferUtils.createFloatBuffer(3);

    public Vector3fUniformMaterial(BaseMaterial base) {
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
        fb.rewind();
        fb.put(parameter.x);
        fb.put(parameter.y);
        fb.put(parameter.z);
        fb.rewind();
        glUniform3(uniformID, fb);
        preRenderShortcut.preRender(vt, worldMatrix, rm);
    }

    @Override
    public void setParameter(String parameterName, Object param) {
        if (this.parameterName.compareTo(parameterName) == 0) {
            parameter.set((Tuple3f) param);
        } else {
            baseMaterial.setParameter(parameterName, param);
        }
    }

    @Override
    public boolean hasParameters() {
        return true;
    }
}
