package de.grogra.imp3d.glsl.renderpass.nostencil;

import javax.media.opengl.GL;
import de.grogra.imp3d.Camera;
import de.grogra.imp3d.glsl.GLSLDisplay;
import de.grogra.imp3d.glsl.OpenGLState;
import de.grogra.imp3d.glsl.light.LightPos;
import de.grogra.imp3d.glsl.renderpass.FullRenderPass;
import de.grogra.imp3d.glsl.utility.GLSLShader;

/**
 * This class sets up primary transparency information  
 * @author Konni Hartmann
 */
public class PrepareAlphaPass extends FullRenderPass {

    protected int getID() {
        return 2;
    }

    GLSLShader cs = null;

    class SetupAlphaShader extends GLSLShader {

        public SetupAlphaShader(OpenGLState glState) {
            super(glState);
        }

        final String baseLightF[] = { "#version 110\n", "#extension GL_ARB_texture_rectangle : enable\n", "uniform sampler2DRect fourthTex;\n", "void main() {\n", "	vec4 alpha = texture2DRect(fourthTex, gl_FragCoord.st);\n", "	gl_FragColor = vec4(alpha.rgb, 1.0);\n", "}" };

        @Override
        public String[] getFragmentShader(Object sh) {
            return baseLightF;
        }

        @Override
        public boolean needsRecompilation(Object data) {
            return false;
        }

        @Override
        public Class instanceFor() {
            return null;
        }

        @Override
        public void setupShader(GL gl, GLSLDisplay disp, Object data) {
            int tex0 = gl.glGetUniformLocation(getShaderProgramNumber(), "fourthTex");
            gl.glUniform1i(tex0, 0);
        }

        @Override
        public GLSLShader getInstance() {
            return this;
        }
    }

    ;

    @Override
    protected void epilogue(GLSLDisplay disp, OpenGLState glState, Object data) {
        assert (data instanceof LightPos);
        GL gl = glState.getGL();
        glState.disable(OpenGLState.STENCIL_TEST);
        glState.disable(OpenGLState.BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        glState.setDepthMask(true);
        glState.enable(OpenGLState.DEPTH_TEST);
        ViewPerspective(glState);
        gl.glPopAttrib();
        deactivateTextures(gl, 1);
    }

    @Override
    protected void prologue(GLSLDisplay disp, OpenGLState glState, Object data) {
        assert (data instanceof Integer);
        GL gl = glState.getGL();
        glState.getAlphaFBO().bind(glState);
        gl.glPushAttrib(GL.GL_VIEWPORT_BIT);
        gl.glViewport(0, 0, glState.width, glState.height);
        GLSLDisplay.printDebugInfoN("Preparing Alpha");
        gl.glReadBuffer(GL.GL_COLOR_ATTACHMENT0_EXT);
        gl.glDrawBuffer(GL.GL_COLOR_ATTACHMENT0_EXT);
        if (cs == null) cs = new SetupAlphaShader(glState);
        cs.activateShader(glState, disp, null);
        ViewOrtho(glState);
        glState.disable(OpenGLState.DEPTH_TEST);
        glState.setDepthMask(false);
        glState.getDeferredShadingFBO().bindAttachmentAsTexture(glState, 3, 0);
        glState.enable(OpenGLState.STENCIL_TEST);
        glState.enable(OpenGLState.BLEND);
        glState.disable(OpenGLState.ALPHA_TEST);
        gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ZERO);
        gl.glStencilFunc(GL.GL_EQUAL, 0x1, 0x1);
        gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);
    }

    @Override
    protected void render(GLSLDisplay disp, OpenGLState glState, Object data) {
        Camera c = disp.getView3D().getCamera();
        drawPrjQuad(glState, c);
    }
}
