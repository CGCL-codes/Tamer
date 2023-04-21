package gestalt.extension.gpgpu.particles;

import gestalt.candidates.glsl.ShaderManager;
import gestalt.candidates.glsl.ShaderProgram;
import mathematik.Util;

public class ParticleResetterRandom extends AbstractParticleResetter {

    public float[] range;

    public ParticleResetterRandom(ShaderManager theShaderManager, ShaderProgram theShaderProgram, String theShader) {
        super(theShaderManager, theShaderProgram, theShader);
    }

    public void draw() {
        _myShaderManager.setUniform(_myShaderProgram, "resetposition", getResetPosition());
    }

    private float getResetPosition() {
        return Util.random(range[0], range[1]);
    }
}
