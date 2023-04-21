package de.grogra.imp3d.glsl.renderpass;

import java.awt.Dimension;
import javax.media.opengl.GL;
import com.sun.opengl.util.GLUT;
import de.grogra.imp3d.Camera;
import de.grogra.imp3d.glsl.GLSLDisplay;
import de.grogra.imp3d.glsl.OpenGLState;
import de.grogra.imp3d.glsl.utility.GLSLShader;

public class PresentDebugImagePass extends RenderPass {

    class GLSLRenderImage extends GLSLShader {

        public GLSLRenderImage(OpenGLState glState) {
            super(glState);
        }

        String[] shaderF = { "#version 110\n", "#extension GL_ARB_texture_rectangle : enable\n", "uniform sampler2DRect tex;\n", "uniform vec2 size;\n", "uniform bool showAlpha;\n", "varying vec2 uv;\n", "void main() {\n", "	vec4 col = texture2DRect(tex, uv * size);", "	col.rgb = showAlpha ? vec3(col.a) : col.rgb;\n", "	gl_FragColor = clamp(vec4(col.rgb, 1.0), 0.0, 1.0);\n", "}" };

        @Override
        protected String[] getFragmentShader(Object data) {
            return shaderF;
        }

        @Override
        public GLSLShader getInstance() {
            return null;
        }

        @Override
        public Class<?> instanceFor() {
            return null;
        }

        @Override
        public boolean needsRecompilation(Object data) {
            return false;
        }

        protected void setupDynamicUniforms(GL gl, GLSLDisplay disp, Object data, int shaderNo) {
            assert (data instanceof Boolean);
            int size = gl.glGetUniformLocation(shaderNo, "size");
            int showAlpha = gl.glGetUniformLocation(shaderNo, "showAlpha");
            Dimension dim = disp.getView3D().getSize();
            gl.glUniform2f(size, dim.width, dim.height);
            gl.glUniform1i(showAlpha, ((Boolean) data) ? 1 : 0);
        }

        ;

        @Override
        protected void setupShader(GL gl, GLSLDisplay disp, Object data) {
            int loc = gl.glGetUniformLocation(getShaderProgramNumber(), "tex");
            gl.glUniform1i(loc, 0);
        }
    }

    GLSLRenderImage presentShader = null;

    @Override
    protected void epilogue(GLSLDisplay disp, OpenGLState glState, Object data) {
        GL gl = glState.getGL();
        glState.setDepthMask(true);
        glState.enable(OpenGLState.DEPTH_TEST);
        deactivateTextures(gl, 1, GL.GL_TEXTURE_RECTANGLE_ARB);
        ViewPerspective(glState);
    }

    @Override
    protected void prologue(GLSLDisplay disp, OpenGLState glState, Object data) {
        assert (data instanceof Integer);
        GL gl = glState.getGL();
        ViewOrtho(glState);
        glState.setFBO(0);
        if (presentShader == null) presentShader = new GLSLRenderImage(glState);
        presentShader.activateShader(glState, disp, new Boolean(false));
        glState.disable(OpenGLState.DEPTH_TEST);
        glState.disable(OpenGLState.STENCIL_TEST);
        glState.setDepthMask(false);
        gl.glClearColor(1, 1, 1, 1);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    }

    public class TextureCollection {

        int[] img;

        boolean[] showAlpha;

        boolean[] showStencil;

        public TextureCollection() {
            showAlpha = new boolean[] { false, false, false, false, false, false, true, true, false };
            showStencil = new boolean[] { false, false, false, false, false, false, false, false, true };
            img = new int[9];
        }
    }

    TextureCollection txC = new TextureCollection();

    protected void renderStencil(OpenGLState glState, Camera c, int x, int y) {
        GL gl = glState.getGL();
        glState.enable(OpenGLState.STENCIL_TEST);
        gl.glStencilFunc(GL.GL_EQUAL, 2, 2);
        gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);
        gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
        drawPrjQuad(glState, c, x * (glState.width / 3), (y) * (glState.height / 3), glState.width / 3, glState.height / 3);
        gl.glStencilFunc(GL.GL_EQUAL, 1, 1);
        gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);
        gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
        drawPrjQuad(glState, c, x * (glState.width / 3), (y) * (glState.height / 3), glState.width / 3, glState.height / 3);
        glState.disable(OpenGLState.STENCIL_TEST);
    }

    void activate(OpenGLState glState, int i) {
        switch(i) {
            case 0:
                glState.getDeferredShadingFBO().bindAttachmentAsTexture(glState, 0, 0);
                break;
            case 1:
                glState.getDeferredShadingFBO().bindAttachmentAsTexture(glState, 1, 0);
                break;
            case 2:
                glState.getHDRFBO().bindAttachmentAsTexture(glState, 0, 0);
                break;
            case 3:
                glState.getDeferredShadingFBO().bindAttachmentAsTexture(glState, 2, 0);
                break;
            case 4:
                glState.getDeferredShadingFBO().bindAttachmentAsTexture(glState, 3, 0);
                break;
            case 5:
                glState.getHDRFBO().bindAttachmentAsTexture(glState, 1, 0);
                break;
            case 6:
                glState.getDeferredShadingFBO().bindAttachmentAsTexture(glState, 0, 0);
                break;
            case 7:
                glState.getDeferredShadingFBO().bindAttachmentAsTexture(glState, 1, 0);
                break;
            default:
                deactivateTextures(glState.getGL(), 1);
                break;
        }
    }

    @Override
    protected void render(GLSLDisplay disp, OpenGLState glState, Object data) {
        GL gl = glState.getGL();
        Camera c = disp.getView3D().getCamera();
        for (int y = 0; y < 3; ++y) for (int x = 0; x < 3; ++x) {
            if (!txC.showStencil[y * 3 + x]) {
                int showAlpha = gl.glGetUniformLocation(presentShader.getShaderProgramNumber(), "showAlpha");
                gl.glUniform1i(showAlpha, txC.showAlpha[y * 3 + x] ? 1 : 0);
                activate(glState, y * 3 + x);
                drawPrjQuad(glState, c, x * (glState.width / 3), (y) * (glState.height / 3), glState.width / 3, glState.height / 3);
            }
        }
        glState.enable(OpenGLState.LIGHTING);
        drawString(glState.getGL(), disp, glState.currentPassName, 5, 5);
        glState.disable(OpenGLState.LIGHTING);
    }

    private final GLUT glut = new GLUT();

    private static final float black[] = { 0.0f, 0.0f, 0.0f, 1.0f };

    protected void drawString(GL gl, GLSLDisplay disp, String string, int x, int y) {
        int width = disp.getView().getSize().width;
        int height = disp.getView().getSize().height;
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glOrtho(-5, 5, -5, 5, 0, 100);
        gl.glViewport(2 * width / 3 + x, height / 3 - 30 - y, 100, 30);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glDisable(GL.GL_COLOR_MATERIAL);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, black, 0);
        gl.glRasterPos3d(-4, 0, 0);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, string);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPopMatrix();
    }

    @Override
    public void process(GLSLDisplay disp, OpenGLState glState, Object data) {
        if (disp.getDebugRenderPass() == glState.renderPass) {
            glState.debugDrawn = true;
            super.process(disp, glState, data);
        }
    }
}
