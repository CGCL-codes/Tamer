package gestalt.candidates.shadow;

import gestalt.candidates.glsl.ShaderManager;
import gestalt.candidates.glsl.ShaderProgram;
import gestalt.shape.AbstractShape;
import gestalt.util.JoglUtil;

public class JoglGLSLShadowMaterial4ud extends JoglGLSLShadowMaterial {

    public static float epsilon = 2f;

    public static float shadowedVal = 0.5f;

    public JoglGLSLShadowMaterial4ud(ShaderManager theShaderManager, ShaderProgram theShaderProgram, JoglGLSLShadowMap theJoglGLSLShadowMap, AbstractShape theParent) {
        super(theShaderManager, theShaderProgram, theJoglGLSLShadowMap, theParent);
    }

    protected void setUniforms() {
        _myShaderManager.setUniform(_myShaderProgram, "shadowMap", JoglUtil.getTextureUnitID(_myProjectionTexture.getTextureUnit()));
        _myShaderManager.setUniform(_myShaderProgram, "epsilon", epsilon);
        _myShaderManager.setUniform(_myShaderProgram, "shadowedVal", shadowedVal);
    }
}
