package org.xith3d.render.jsr231;

import java.nio.FloatBuffer;
import javax.media.opengl.GL;
import javax.vecmath.Color4f;
import javax.vecmath.Matrix4f;
import org.xith3d.Xith3DDefaults;
import org.xith3d.render.CanvasPeer;
import org.xith3d.render.Option;
import org.xith3d.render.prerender.RenderAtom;
import org.xith3d.render.shader.Shader;
import org.xith3d.render.shader.ShaderPeer;
import org.xith3d.render.shader.TextureAttrShader;
import org.xith3d.scenegraph.TextureAttributes;
import org.xith3d.utility.logs.Log;
import org.xith3d.utility.profile.ProfileTimer;
import com.sun.opengl.util.BufferUtil;

/**
 * @author David Yazel
 * @author Lilian Chamontin (jsr231 port)
 * @author Marvin Froehlich (aka Qudus) [code cleaning]
 */
public class TextureAttrShaderPeer implements ShaderPeer {

    private static float trans[] = new float[16];

    private FloatBuffer texBlendColorBuffer = BufferUtil.newFloatBuffer(4);

    private static final Color4f DEFAULT_TEXTURE_BLEND_COLOR = new Color4f(0f, 0f, 0f, 1f);

    private static FloatBuffer matrixBuffer = BufferUtil.newFloatBuffer(16);

    public TextureAttrShaderPeer() {
    }

    private static void setCombineMode(GL gl, int channel, int mode) {
        switch(mode) {
            case TextureAttributes.COMBINE_ADD:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_ADD);
                break;
            case TextureAttributes.COMBINE_ADD_SIGNED:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_ADD_SIGNED);
                break;
            case TextureAttributes.COMBINE_REPLACE:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_REPLACE);
                break;
            case TextureAttributes.COMBINE_MODULATE:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_MODULATE);
                break;
            case TextureAttributes.COMBINE_SUBTRACT:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_SUBTRACT);
                break;
            case TextureAttributes.COMBINE_INTERPOLATE:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_INTERPOLATE);
                break;
            case TextureAttributes.COMBINE_DOT3:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_DOT3_RGB);
                break;
        }
    }

    private static void setCombineSource(GL gl, int channel, int mode) {
        switch(mode) {
            case TextureAttributes.COMBINE_PREVIOUS_TEXTURE_UNIT_STATE:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_PREVIOUS);
                break;
            case TextureAttributes.COMBINE_TEXTURE_COLOR:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_TEXTURE);
                break;
            case TextureAttributes.COMBINE_TEXTURE0:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_TEXTURE0);
                break;
            case TextureAttributes.COMBINE_TEXTURE1:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_TEXTURE1);
                break;
            case TextureAttributes.COMBINE_OBJECT_COLOR:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_PRIMARY_COLOR);
                break;
            case TextureAttributes.COMBINE_CONSTANT_COLOR:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_CONSTANT);
                break;
            default:
                throw new Error("Unrecognized combine source : " + mode);
        }
    }

    private static void setCombineFunction(GL gl, int channel, int mode) {
        switch(mode) {
            case TextureAttributes.COMBINE_SRC_COLOR:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_SRC_COLOR);
                break;
            case TextureAttributes.COMBINE_ONE_MINUS_SRC_COLOR:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_ONE_MINUS_SRC_COLOR);
                break;
            case TextureAttributes.COMBINE_ONE_MINUS_SRC_ALPHA:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_ONE_MINUS_SRC_ALPHA);
                break;
            case TextureAttributes.COMBINE_SRC_ALPHA:
                gl.glTexEnvf(GL.GL_TEXTURE_ENV, channel, GL.GL_SRC_ALPHA);
                break;
            default:
                throw new Error("Unrecognized combine source : " + mode);
        }
    }

    private static void setTransform(GL gl, Matrix4f mat) {
        trans[0] = mat.m00;
        trans[1] = mat.m10;
        trans[2] = mat.m20;
        trans[3] = mat.m30;
        trans[4] = mat.m01;
        trans[5] = mat.m11;
        trans[6] = mat.m21;
        trans[7] = mat.m31;
        trans[8] = mat.m02;
        trans[9] = mat.m12;
        trans[10] = mat.m22;
        trans[11] = mat.m32;
        trans[12] = mat.m03;
        trans[13] = mat.m13;
        trans[14] = mat.m23;
        trans[15] = mat.m33;
        matrixBuffer.put(trans);
        matrixBuffer.rewind();
        gl.glLoadMatrixf(matrixBuffer);
    }

    private void setTextureAttributes(GL gl, TextureAttributes ta) {
        if (ta == null) {
            gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
            if (Xith3DDefaults.getLocalDebug(CanvasPeerImplBase.DEBUG_GL)) Log.debug("tetxure mode is MODULATE");
            return;
        }
        if ((ta == null) || (ta.getTextureTransform() == null)) {
            gl.glMatrixMode(GL.GL_TEXTURE);
            gl.glLoadIdentity();
            gl.glMatrixMode(GL.GL_MODELVIEW);
        } else {
            gl.glMatrixMode(GL.GL_TEXTURE);
            setTransform(gl, ta.getTextureTransform().getMatrix4f());
            gl.glMatrixMode(GL.GL_MODELVIEW);
        }
        switch(ta.getTextureMode()) {
            case TextureAttributes.MODULATE:
                gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
                if (Xith3DDefaults.getLocalDebug(CanvasPeerImplBase.DEBUG_GL)) Log.debug("tetxure mode is MODULATE");
                break;
            case TextureAttributes.REPLACE:
                gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
                if (Xith3DDefaults.getLocalDebug(CanvasPeerImplBase.DEBUG_GL)) Log.debug("tetxure mode is REPLACE");
                break;
            case TextureAttributes.BLEND:
                gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_BLEND);
                if (Xith3DDefaults.getLocalDebug(CanvasPeerImplBase.DEBUG_GL)) Log.debug("tetxure mode is BLEND");
                break;
            case TextureAttributes.DECAL:
                gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_DECAL);
                if (Xith3DDefaults.getLocalDebug(CanvasPeerImplBase.DEBUG_GL)) Log.debug("tetxure mode is DECAL");
                break;
            case TextureAttributes.COMBINE:
                gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_COMBINE);
                Color4f tbc = ta.getTextureBlendColor();
                if (tbc == null) {
                    tbc = DEFAULT_TEXTURE_BLEND_COLOR;
                }
                texBlendColorBuffer.put(tbc.x);
                texBlendColorBuffer.put(tbc.y);
                texBlendColorBuffer.put(tbc.z);
                texBlendColorBuffer.put(tbc.w);
                texBlendColorBuffer.rewind();
                gl.glTexEnvfv(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_COLOR, texBlendColorBuffer);
                setCombineMode(gl, GL.GL_COMBINE_RGB, ta.getCombineRgbMode());
                setCombineMode(gl, GL.GL_COMBINE_ALPHA, ta.getCombineAlphaMode());
                setCombineSource(gl, GL.GL_SOURCE0_RGB, ta.getCombineRgbSource(0));
                setCombineSource(gl, GL.GL_SOURCE0_ALPHA, ta.getCombineAlphaSource(0));
                setCombineFunction(gl, GL.GL_OPERAND0_RGB, ta.getCombineRgbFunction(0));
                setCombineFunction(gl, GL.GL_OPERAND0_ALPHA, ta.getCombineAlphaFunction(0));
                setCombineSource(gl, GL.GL_SOURCE1_RGB, ta.getCombineRgbSource(1));
                setCombineSource(gl, GL.GL_SOURCE1_ALPHA, ta.getCombineAlphaSource(1));
                setCombineFunction(gl, GL.GL_OPERAND1_RGB, ta.getCombineRgbFunction(1));
                setCombineFunction(gl, GL.GL_OPERAND1_ALPHA, ta.getCombineAlphaFunction(1));
                setCombineSource(gl, GL.GL_SOURCE2_RGB, ta.getCombineRgbSource(2));
                setCombineSource(gl, GL.GL_SOURCE2_ALPHA, ta.getCombineAlphaSource(2));
                setCombineFunction(gl, GL.GL_OPERAND2_RGB, ta.getCombineRgbFunction(2));
                setCombineFunction(gl, GL.GL_OPERAND2_ALPHA, ta.getCombineAlphaFunction(2));
                gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_RGB_SCALE, ta.getCombineRgbScale());
                if (Xith3DDefaults.getLocalDebug(CanvasPeerImplBase.DEBUG_GL)) Log.debug("tetxure mode is COMBINE");
                break;
            default:
                if (Xith3DDefaults.getLocalDebug(CanvasPeerImplBase.DEBUG_GL)) Log.debug("tetxure mode is UNKNOWN");
        }
    }

    public void shade(CanvasPeer canvas, RenderAtom atom, Shader shader) {
        if (!canvas.getRenderOptions().getOption(Option.USE_TEXTURES)) {
            return;
        }
        ProfileTimer.startProfile("TextureShaderPeer::shade");
        final GL gl = ((CanvasPeerImplBase) canvas).getGL();
        TextureAttrShader attrShader = (TextureAttrShader) shader;
        TextureAttributes ta = attrShader.getTexture();
        ShapeAtomPeer.clutchTextureUnit(gl, attrShader.getUnit());
        setTextureAttributes(gl, ta);
        ProfileTimer.endProfile();
    }

    public void doneShading(CanvasPeer canvas, RenderAtom atom, Shader shader) {
        if (!canvas.getRenderOptions().getOption(Option.USE_TEXTURES)) {
            return;
        }
    }
}
