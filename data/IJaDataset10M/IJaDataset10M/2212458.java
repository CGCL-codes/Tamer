package cyclopean.world;

import java.io.IOException;
import javax.media.opengl.GL;
import cyclopean.Paths;
import util.file.TextIO;
import javagame.core.io.video.shader.Shader;

/**
 * 
 *
 * @author Jaco van der Westhuizen
 */
public class BlocksShader extends Shader {

    final int blocksTex;

    final int colorsTex;

    final int texturesTex;

    /**
     * @param gl
     * @param vertexProg
     * @param fragProg
	 * @throws IOException 
     */
    public BlocksShader(GL gl, int blocksTex, int colorsTex, int texturesTex) throws IOException {
        super(gl, TextIO.read(Paths.SHADERS + "blocks_vert.glsl"), TextIO.read(Paths.SHADERS + "blocks_frag.glsl"));
        this.blocksTex = blocksTex;
        this.colorsTex = colorsTex;
        this.texturesTex = texturesTex;
    }

    @Override
    protected void initialize(GL gl) {
        gl.glUniform1i(gl.glGetUniformLocation(this.program, "blocks"), 0);
        gl.glUniform1i(gl.glGetUniformLocation(this.program, "colors"), 1);
        gl.glUniform1i(gl.glGetUniformLocation(this.program, "textures"), 2);
    }

    @Override
    public void prepare(GL gl) {
        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_3D, blocksTex);
        gl.glActiveTexture(GL.GL_TEXTURE1);
        gl.glBindTexture(GL.GL_TEXTURE_3D, colorsTex);
        gl.glActiveTexture(GL.GL_TEXTURE2);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texturesTex);
    }
}
