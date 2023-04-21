package javax.media.opengl;

import java.io.*;
import javax.media.opengl.*;
import javax.media.opengl.GLES2;
import javax.media.opengl.GLBase;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL;
import com.sun.gluegen.runtime.*;

/** <P> Composable pipeline which wraps an underlying {@link GL} implementation,
    providing error checking after each OpenGL method call. If an error occurs,
    causes a {@link GLException} to be thrown at exactly the point of failure.
    Sample code which installs this pipeline: </P>

<PRE>
     GL gl = drawable.setGL(new DebugGL(drawable.getGL()));
</PRE>
*/
public class DebugGLES2 implements javax.media.opengl.GLBase, javax.media.opengl.GL, javax.media.opengl.GL2ES2, javax.media.opengl.GLES2 {

    public static final boolean DEBUG = com.sun.opengl.impl.Debug.debug("DebugGLES2");

    public DebugGLES2(GLES2 downstreamGLES2) {
        if (downstreamGLES2 == null) {
            throw new IllegalArgumentException("null downstreamGLES2");
        }
        this.downstreamGLES2 = downstreamGLES2;
        _context = downstreamGLES2.getContext();
    }

    public boolean isGL() {
        return true;
    }

    public boolean isGL3bc() {
        return false;
    }

    public boolean isGL3() {
        return false;
    }

    public boolean isGL2() {
        return false;
    }

    public boolean isGLES1() {
        return false;
    }

    public boolean isGLES2() {
        return true;
    }

    public boolean isGL2ES1() {
        return false;
    }

    public boolean isGL2ES2() {
        return true;
    }

    public boolean isGL2GL3() {
        return false;
    }

    public boolean isGLES() {
        return isGLES2() || isGLES1();
    }

    public javax.media.opengl.GL getGL() {
        return this;
    }

    public javax.media.opengl.GL3bc getGL3bc() {
        throw new GLException("Not a GL3bc implementation");
    }

    public javax.media.opengl.GL3 getGL3() {
        throw new GLException("Not a GL3 implementation");
    }

    public javax.media.opengl.GL2 getGL2() {
        throw new GLException("Not a GL2 implementation");
    }

    public javax.media.opengl.GLES1 getGLES1() {
        throw new GLException("Not a GLES1 implementation");
    }

    public javax.media.opengl.GLES2 getGLES2() {
        return this;
    }

    public javax.media.opengl.GL2ES1 getGL2ES1() {
        throw new GLException("Not a GL2ES1 implementation");
    }

    public javax.media.opengl.GL2ES2 getGL2ES2() {
        return this;
    }

    public javax.media.opengl.GL2GL3 getGL2GL3() {
        throw new GLException("Not a GL2GL3 implementation");
    }

    public GLProfile getGLProfile() {
        return downstreamGLES2.getGLProfile();
    }

    public void glVertexAttribPointer(javax.media.opengl.GLArrayData arg0) {
        checkContext();
        downstreamGLES2.glVertexAttribPointer(arg0);
        String txt = new String("glVertexAttribPointer(" + "<javax.media.opengl.GLArrayData>" + ")");
        checkGLGetError(txt);
    }

    public void glUniformMatrix2fv(int arg0, int arg1, boolean arg2, float[] arg3, int arg4) {
        checkContext();
        downstreamGLES2.glUniformMatrix2fv(arg0, arg1, arg2, arg3, arg4);
        String txt = new String("glUniformMatrix2fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<boolean>" + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glUniform1f(int arg0, float arg1) {
        checkContext();
        downstreamGLES2.glUniform1f(arg0, arg1);
        String txt = new String("glUniform1f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glBlendEquationSeparate(int arg0, int arg1) {
        checkContext();
        downstreamGLES2.glBlendEquationSeparate(arg0, arg1);
        String txt = new String("glBlendEquationSeparate(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public boolean glIsShader(int arg0) {
        checkContext();
        boolean _res = downstreamGLES2.glIsShader(arg0);
        String txt = new String("glIsShader(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glGetActiveUniform(int arg0, int arg1, int arg2, int[] arg3, int arg4, int[] arg5, int arg6, int[] arg7, int arg8, byte[] arg9, int arg10) {
        checkContext();
        downstreamGLES2.glGetActiveUniform(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
        String txt = new String("glGetActiveUniform(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg8).toUpperCase() + ", " + "<[B>" + ", " + "<int> 0x" + Integer.toHexString(arg10).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGenTextures(int arg0, int[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glGenTextures(arg0, arg1, arg2);
        String txt = new String("glGenTextures(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glReadPixels(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, long arg6) {
        checkContext();
        downstreamGLES2.glReadPixels(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        String txt = new String("glReadPixels(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<long>" + ")");
        checkGLGetError(txt);
    }

    public void glBindBuffer(int arg0, int arg1) {
        checkContext();
        downstreamGLES2.glBindBuffer(arg0, arg1);
        String txt = new String("glBindBuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public boolean isExtensionAvailable(java.lang.String arg0) {
        return downstreamGLES2.isExtensionAvailable(arg0);
    }

    public void glTexParameterf(int arg0, int arg1, float arg2) {
        checkContext();
        downstreamGLES2.glTexParameterf(arg0, arg1, arg2);
        String txt = new String("glTexParameterf(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glBlendFuncSeparate(int arg0, int arg1, int arg2, int arg3) {
        checkContext();
        downstreamGLES2.glBlendFuncSeparate(arg0, arg1, arg2, arg3);
        String txt = new String("glBlendFuncSeparate(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glUniformMatrix4fv(int arg0, int arg1, boolean arg2, float[] arg3, int arg4) {
        checkContext();
        downstreamGLES2.glUniformMatrix4fv(arg0, arg1, arg2, arg3, arg4);
        String txt = new String("glUniformMatrix4fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<boolean>" + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public int glCreateShader(int arg0) {
        checkContext();
        int _res = downstreamGLES2.glCreateShader(arg0);
        String txt = new String("glCreateShader(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glGetFramebufferAttachmentParameteriv(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3) {
        checkContext();
        downstreamGLES2.glGetFramebufferAttachmentParameteriv(arg0, arg1, arg2, arg3);
        String txt = new String("glGetFramebufferAttachmentParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glUniformMatrix2fv(int arg0, int arg1, boolean arg2, java.nio.FloatBuffer arg3) {
        checkContext();
        downstreamGLES2.glUniformMatrix2fv(arg0, arg1, arg2, arg3);
        String txt = new String("glUniformMatrix2fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<boolean>" + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public java.nio.ByteBuffer glMapBuffer(int arg0, int arg1) {
        checkContext();
        java.nio.ByteBuffer _res = downstreamGLES2.glMapBuffer(arg0, arg1);
        String txt = new String("glMapBuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glDeleteFramebuffers(int arg0, java.nio.IntBuffer arg1) {
        checkContext();
        downstreamGLES2.glDeleteFramebuffers(arg0, arg1);
        String txt = new String("glDeleteFramebuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetActiveUniform(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3, java.nio.IntBuffer arg4, java.nio.IntBuffer arg5, java.nio.ByteBuffer arg6) {
        checkContext();
        downstreamGLES2.glGetActiveUniform(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        String txt = new String("glGetActiveUniform(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ", " + "<java.nio.IntBuffer>" + ", " + "<java.nio.IntBuffer>" + ", " + "<java.nio.ByteBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glUniformMatrix4fv(int arg0, int arg1, boolean arg2, java.nio.FloatBuffer arg3) {
        checkContext();
        downstreamGLES2.glUniformMatrix4fv(arg0, arg1, arg2, arg3);
        String txt = new String("glUniformMatrix4fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<boolean>" + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glCompressedTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.Buffer arg8) {
        checkContext();
        downstreamGLES2.glCompressedTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        String txt = new String("glCompressedTexSubImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ", " + "<java.nio.Buffer>" + ")");
        checkGLGetError(txt);
    }

    public int glGetAttribLocation(int arg0, java.lang.String arg1) {
        checkContext();
        int _res = downstreamGLES2.glGetAttribLocation(arg0, arg1);
        String txt = new String("glGetAttribLocation(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.lang.String>" + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glFlush() {
        checkContext();
        downstreamGLES2.glFlush();
        String txt = new String("glFlush(" + ")");
        checkGLGetError(txt);
    }

    public void glFrontFace(int arg0) {
        checkContext();
        downstreamGLES2.glFrontFace(arg0);
        String txt = new String("glFrontFace(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGenBuffers(int arg0, int[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glGenBuffers(arg0, arg1, arg2);
        String txt = new String("glGenBuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public boolean glIsVBOElementEnabled() {
        checkContext();
        boolean _res = downstreamGLES2.glIsVBOElementEnabled();
        String txt = new String("glIsVBOElementEnabled(" + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glDepthFunc(int arg0) {
        checkContext();
        downstreamGLES2.glDepthFunc(arg0);
        String txt = new String("glDepthFunc(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glBlendColor(float arg0, float arg1, float arg2, float arg3) {
        checkContext();
        downstreamGLES2.glBlendColor(arg0, arg1, arg2, arg3);
        String txt = new String("glBlendColor(" + "<float>" + ", " + "<float>" + ", " + "<float>" + ", " + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glUniform4fv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        checkContext();
        downstreamGLES2.glUniform4fv(arg0, arg1, arg2);
        String txt = new String("glUniform4fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glUniform3f(int arg0, float arg1, float arg2, float arg3) {
        checkContext();
        downstreamGLES2.glUniform3f(arg0, arg1, arg2, arg3);
        String txt = new String("glUniform3f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float>" + ", " + "<float>" + ", " + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glGenRenderbuffers(int arg0, int[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glGenRenderbuffers(arg0, arg1, arg2);
        String txt = new String("glGenRenderbuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glVertexAttrib2f(int arg0, float arg1, float arg2) {
        checkContext();
        downstreamGLES2.glVertexAttrib2f(arg0, arg1, arg2);
        String txt = new String("glVertexAttrib2f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float>" + ", " + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glVertexAttrib4fv(int arg0, java.nio.FloatBuffer arg1) {
        checkContext();
        downstreamGLES2.glVertexAttrib4fv(arg0, arg1);
        String txt = new String("glVertexAttrib4fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, long arg8) {
        checkContext();
        downstreamGLES2.glTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        String txt = new String("glTexImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ", " + "<long>" + ")");
        checkGLGetError(txt);
    }

    public int glGetError() {
        checkContext();
        int _res = downstreamGLES2.glGetError();
        String txt = new String("glGetError(" + ")");
        checkGLGetError(txt);
        return _res;
    }

    public boolean hasGLSL() {
        return downstreamGLES2.hasGLSL();
    }

    public void glGetVertexAttribiv(int arg0, int arg1, int[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glGetVertexAttribiv(arg0, arg1, arg2, arg3);
        String txt = new String("glGetVertexAttribiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetProgramiv(int arg0, int arg1, int[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glGetProgramiv(arg0, arg1, arg2, arg3);
        String txt = new String("glGetProgramiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glStencilFuncSeparate(int arg0, int arg1, int arg2, int arg3) {
        checkContext();
        downstreamGLES2.glStencilFuncSeparate(arg0, arg1, arg2, arg3);
        String txt = new String("glStencilFuncSeparate(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glDeleteRenderbuffers(int arg0, java.nio.IntBuffer arg1) {
        checkContext();
        downstreamGLES2.glDeleteRenderbuffers(arg0, arg1);
        String txt = new String("glDeleteRenderbuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glDeleteTextures(int arg0, int[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glDeleteTextures(arg0, arg1, arg2);
        String txt = new String("glDeleteTextures(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetShaderiv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        checkContext();
        downstreamGLES2.glGetShaderiv(arg0, arg1, arg2);
        String txt = new String("glGetShaderiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetBufferParameteriv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        checkContext();
        downstreamGLES2.glGetBufferParameteriv(arg0, arg1, arg2);
        String txt = new String("glGetBufferParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGenFramebuffers(int arg0, int[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glGenFramebuffers(arg0, arg1, arg2);
        String txt = new String("glGenFramebuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glCompileShader(int arg0) {
        checkContext();
        downstreamGLES2.glCompileShader(arg0);
        String txt = new String("glCompileShader(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public int glGetUniformLocation(int arg0, java.lang.String arg1) {
        checkContext();
        int _res = downstreamGLES2.glGetUniformLocation(arg0, arg1);
        String txt = new String("glGetUniformLocation(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.lang.String>" + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glUniform4fv(int arg0, int arg1, float[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glUniform4fv(arg0, arg1, arg2, arg3);
        String txt = new String("glUniform4fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public boolean glIsBuffer(int arg0) {
        checkContext();
        boolean _res = downstreamGLES2.glIsBuffer(arg0);
        String txt = new String("glIsBuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glFramebufferRenderbuffer(int arg0, int arg1, int arg2, int arg3) {
        checkContext();
        downstreamGLES2.glFramebufferRenderbuffer(arg0, arg1, arg2, arg3);
        String txt = new String("glFramebufferRenderbuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glUniform4f(int arg0, float arg1, float arg2, float arg3, float arg4) {
        checkContext();
        downstreamGLES2.glUniform4f(arg0, arg1, arg2, arg3, arg4);
        String txt = new String("glUniform4f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float>" + ", " + "<float>" + ", " + "<float>" + ", " + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glUniform4i(int arg0, int arg1, int arg2, int arg3, int arg4) {
        checkContext();
        downstreamGLES2.glUniform4i(arg0, arg1, arg2, arg3, arg4);
        String txt = new String("glUniform4i(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glClearDepth(double arg0) {
        checkContext();
        downstreamGLES2.glClearDepth(arg0);
        String txt = new String("glClearDepth(" + "<double>" + ")");
        checkGLGetError(txt);
    }

    public void glVertexAttrib4fv(int arg0, float[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glVertexAttrib4fv(arg0, arg1, arg2);
        String txt = new String("glVertexAttrib4fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public boolean glIsRenderbuffer(int arg0) {
        checkContext();
        boolean _res = downstreamGLES2.glIsRenderbuffer(arg0);
        String txt = new String("glIsRenderbuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glGetIntegerv(int arg0, int[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glGetIntegerv(arg0, arg1, arg2);
        String txt = new String("glGetIntegerv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glCompressedTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, java.nio.Buffer arg7) {
        checkContext();
        downstreamGLES2.glCompressedTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        String txt = new String("glCompressedTexImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<java.nio.Buffer>" + ")");
        checkGLGetError(txt);
    }

    public void glStencilFunc(int arg0, int arg1, int arg2) {
        checkContext();
        downstreamGLES2.glStencilFunc(arg0, arg1, arg2);
        String txt = new String("glStencilFunc(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glCompressedTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, long arg7) {
        checkContext();
        downstreamGLES2.glCompressedTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        String txt = new String("glCompressedTexImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<long>" + ")");
        checkGLGetError(txt);
    }

    public void glDrawArrays(int arg0, int arg1, int arg2) {
        checkContext();
        downstreamGLES2.glDrawArrays(arg0, arg1, arg2);
        String txt = new String("glDrawArrays(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glLineWidth(float arg0) {
        checkContext();
        downstreamGLES2.glLineWidth(arg0);
        String txt = new String("glLineWidth(" + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glUniform4iv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        checkContext();
        downstreamGLES2.glUniform4iv(arg0, arg1, arg2);
        String txt = new String("glUniform4iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glDeleteBuffers(int arg0, int[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glDeleteBuffers(arg0, arg1, arg2);
        String txt = new String("glDeleteBuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glStencilMask(int arg0) {
        checkContext();
        downstreamGLES2.glStencilMask(arg0);
        String txt = new String("glStencilMask(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glBindTexture(int arg0, int arg1) {
        checkContext();
        downstreamGLES2.glBindTexture(arg0, arg1);
        String txt = new String("glBindTexture(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glColorMask(boolean arg0, boolean arg1, boolean arg2, boolean arg3) {
        checkContext();
        downstreamGLES2.glColorMask(arg0, arg1, arg2, arg3);
        String txt = new String("glColorMask(" + "<boolean>" + ", " + "<boolean>" + ", " + "<boolean>" + ", " + "<boolean>" + ")");
        checkGLGetError(txt);
    }

    public javax.media.opengl.GLContext getContext() {
        return downstreamGLES2.getContext();
    }

    public void glTexParameteriv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        checkContext();
        downstreamGLES2.glTexParameteriv(arg0, arg1, arg2);
        String txt = new String("glTexParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glShaderBinary(int arg0, java.nio.IntBuffer arg1, int arg2, java.nio.Buffer arg3, int arg4) {
        checkContext();
        downstreamGLES2.glShaderBinary(arg0, arg1, arg2, arg3, arg4);
        String txt = new String("glShaderBinary(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<java.nio.Buffer>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glUniformMatrix3fv(int arg0, int arg1, boolean arg2, float[] arg3, int arg4) {
        checkContext();
        downstreamGLES2.glUniformMatrix3fv(arg0, arg1, arg2, arg3, arg4);
        String txt = new String("glUniformMatrix3fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<boolean>" + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public boolean glIsVBOArrayEnabled() {
        checkContext();
        boolean _res = downstreamGLES2.glIsVBOArrayEnabled();
        String txt = new String("glIsVBOArrayEnabled(" + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glClear(int arg0) {
        checkContext();
        downstreamGLES2.glClear(arg0);
        String txt = new String("glClear(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glClearColor(float arg0, float arg1, float arg2, float arg3) {
        checkContext();
        downstreamGLES2.glClearColor(arg0, arg1, arg2, arg3);
        String txt = new String("glClearColor(" + "<float>" + ", " + "<float>" + ", " + "<float>" + ", " + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glDepthRangef(float arg0, float arg1) {
        checkContext();
        downstreamGLES2.glDepthRangef(arg0, arg1);
        String txt = new String("glDepthRangef(" + "<float>" + ", " + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glShaderSource(int arg0, int arg1, java.lang.String[] arg2, int[] arg3, int arg4) {
        checkContext();
        downstreamGLES2.glShaderSource(arg0, arg1, arg2, arg3, arg4);
        String txt = new String("glShaderSource(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[Ljava.lang.String;>" + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetBooleanv(int arg0, java.nio.ByteBuffer arg1) {
        checkContext();
        downstreamGLES2.glGetBooleanv(arg0, arg1);
        String txt = new String("glGetBooleanv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.ByteBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.Buffer arg8) {
        checkContext();
        downstreamGLES2.glTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        String txt = new String("glTexImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ", " + "<java.nio.Buffer>" + ")");
        checkGLGetError(txt);
    }

    public void glUniform1iv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        checkContext();
        downstreamGLES2.glUniform1iv(arg0, arg1, arg2);
        String txt = new String("glUniform1iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glUniformMatrix3fv(int arg0, int arg1, boolean arg2, java.nio.FloatBuffer arg3) {
        checkContext();
        downstreamGLES2.glUniformMatrix3fv(arg0, arg1, arg2, arg3);
        String txt = new String("glUniformMatrix3fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<boolean>" + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetUniformiv(int arg0, int arg1, int[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glGetUniformiv(arg0, arg1, arg2, arg3);
        String txt = new String("glGetUniformiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glDepthRange(double arg0, double arg1) {
        checkContext();
        downstreamGLES2.glDepthRange(arg0, arg1);
        String txt = new String("glDepthRange(" + "<double>" + ", " + "<double>" + ")");
        checkGLGetError(txt);
    }

    public void glDisable(int arg0) {
        checkContext();
        downstreamGLES2.glDisable(arg0);
        String txt = new String("glDisable(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glCompressedTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, long arg8) {
        checkContext();
        downstreamGLES2.glCompressedTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        String txt = new String("glCompressedTexSubImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ", " + "<long>" + ")");
        checkGLGetError(txt);
    }

    public void glUniform2iv(int arg0, int arg1, int[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glUniform2iv(arg0, arg1, arg2, arg3);
        String txt = new String("glUniform2iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public java.lang.String glGetString(int arg0) {
        checkContext();
        java.lang.String _res = downstreamGLES2.glGetString(arg0);
        String txt = new String("glGetString(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glCopyTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7) {
        checkContext();
        downstreamGLES2.glCopyTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        String txt = new String("glCopyTexImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetShaderInfoLog(int arg0, int arg1, int[] arg2, int arg3, byte[] arg4, int arg5) {
        checkContext();
        downstreamGLES2.glGetShaderInfoLog(arg0, arg1, arg2, arg3, arg4, arg5);
        String txt = new String("glGetShaderInfoLog(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<[B>" + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glBufferData(int arg0, int arg1, java.nio.Buffer arg2, int arg3) {
        checkContext();
        downstreamGLES2.glBufferData(arg0, arg1, arg2, arg3);
        String txt = new String("glBufferData(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.Buffer>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glUniform3fv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        checkContext();
        downstreamGLES2.glUniform3fv(arg0, arg1, arg2);
        String txt = new String("glUniform3fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glUniform1fv(int arg0, int arg1, float[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glUniform1fv(arg0, arg1, arg2, arg3);
        String txt = new String("glUniform1fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glUniform3iv(int arg0, int arg1, int[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glUniform3iv(arg0, arg1, arg2, arg3);
        String txt = new String("glUniform3iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetTexParameteriv(int arg0, int arg1, int[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glGetTexParameteriv(arg0, arg1, arg2, arg3);
        String txt = new String("glGetTexParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetShaderInfoLog(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.ByteBuffer arg3) {
        checkContext();
        downstreamGLES2.glGetShaderInfoLog(arg0, arg1, arg2, arg3);
        String txt = new String("glGetShaderInfoLog(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ", " + "<java.nio.ByteBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetRenderbufferParameteriv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        checkContext();
        downstreamGLES2.glGetRenderbufferParameteriv(arg0, arg1, arg2);
        String txt = new String("glGetRenderbufferParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glBlendFunc(int arg0, int arg1) {
        checkContext();
        downstreamGLES2.glBlendFunc(arg0, arg1);
        String txt = new String("glBlendFunc(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glReadPixels(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, java.nio.Buffer arg6) {
        checkContext();
        downstreamGLES2.glReadPixels(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        String txt = new String("glReadPixels(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<java.nio.Buffer>" + ")");
        checkGLGetError(txt);
    }

    public void glLinkProgram(int arg0) {
        checkContext();
        downstreamGLES2.glLinkProgram(arg0);
        String txt = new String("glLinkProgram(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glUniform3fv(int arg0, int arg1, float[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glUniform3fv(arg0, arg1, arg2, arg3);
        String txt = new String("glUniform3fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glBindFramebuffer(int arg0, int arg1) {
        checkContext();
        downstreamGLES2.glBindFramebuffer(arg0, arg1);
        String txt = new String("glBindFramebuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glDisableVertexAttribArray(int arg0) {
        checkContext();
        downstreamGLES2.glDisableVertexAttribArray(arg0);
        String txt = new String("glDisableVertexAttribArray(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glUniform1fv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        checkContext();
        downstreamGLES2.glUniform1fv(arg0, arg1, arg2);
        String txt = new String("glUniform1fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glReleaseShaderCompiler() {
        checkContext();
        downstreamGLES2.glReleaseShaderCompiler();
        String txt = new String("glReleaseShaderCompiler(" + ")");
        checkGLGetError(txt);
    }

    public void glVertexAttribPointer(int arg0, int arg1, int arg2, boolean arg3, int arg4, long arg5) {
        checkContext();
        downstreamGLES2.glVertexAttribPointer(arg0, arg1, arg2, arg3, arg4, arg5);
        String txt = new String("glVertexAttribPointer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<boolean>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<long>" + ")");
        checkGLGetError(txt);
    }

    public void glVertexAttrib1f(int arg0, float arg1) {
        checkContext();
        downstreamGLES2.glVertexAttrib1f(arg0, arg1);
        String txt = new String("glVertexAttrib1f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glGenRenderbuffers(int arg0, java.nio.IntBuffer arg1) {
        checkContext();
        downstreamGLES2.glGenRenderbuffers(arg0, arg1);
        String txt = new String("glGenRenderbuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glTexParameterfv(int arg0, int arg1, float[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glTexParameterfv(arg0, arg1, arg2, arg3);
        String txt = new String("glTexParameterfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glUniform1i(int arg0, int arg1) {
        checkContext();
        downstreamGLES2.glUniform1i(arg0, arg1);
        String txt = new String("glUniform1i(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGenBuffers(int arg0, java.nio.IntBuffer arg1) {
        checkContext();
        downstreamGLES2.glGenBuffers(arg0, arg1);
        String txt = new String("glGenBuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glEGLImageTargetTexture2DOES(int arg0, java.nio.Buffer arg1) {
        checkContext();
        downstreamGLES2.glEGLImageTargetTexture2DOES(arg0, arg1);
        String txt = new String("glEGLImageTargetTexture2DOES(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.Buffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetVertexAttribfv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        checkContext();
        downstreamGLES2.glGetVertexAttribfv(arg0, arg1, arg2);
        String txt = new String("glGetVertexAttribfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, long arg8) {
        checkContext();
        downstreamGLES2.glTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        String txt = new String("glTexSubImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ", " + "<long>" + ")");
        checkGLGetError(txt);
    }

    public void glSampleCoverage(float arg0, boolean arg1) {
        checkContext();
        downstreamGLES2.glSampleCoverage(arg0, arg1);
        String txt = new String("glSampleCoverage(" + "<float>" + ", " + "<boolean>" + ")");
        checkGLGetError(txt);
    }

    public void glGetVertexAttribiv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        checkContext();
        downstreamGLES2.glGetVertexAttribiv(arg0, arg1, arg2);
        String txt = new String("glGetVertexAttribiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetProgramiv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        checkContext();
        downstreamGLES2.glGetProgramiv(arg0, arg1, arg2);
        String txt = new String("glGetProgramiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glValidateProgram(int arg0) {
        checkContext();
        downstreamGLES2.glValidateProgram(arg0);
        String txt = new String("glValidateProgram(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public boolean isFunctionAvailable(java.lang.String arg0) {
        return downstreamGLES2.isFunctionAvailable(arg0);
    }

    public void glStencilOpSeparate(int arg0, int arg1, int arg2, int arg3) {
        checkContext();
        downstreamGLES2.glStencilOpSeparate(arg0, arg1, arg2, arg3);
        String txt = new String("glStencilOpSeparate(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glVertexAttrib2fv(int arg0, float[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glVertexAttrib2fv(arg0, arg1, arg2);
        String txt = new String("glVertexAttrib2fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glDeleteTextures(int arg0, java.nio.IntBuffer arg1) {
        checkContext();
        downstreamGLES2.glDeleteTextures(arg0, arg1);
        String txt = new String("glDeleteTextures(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetShaderiv(int arg0, int arg1, int[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glGetShaderiv(arg0, arg1, arg2, arg3);
        String txt = new String("glGetShaderiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public boolean glIsEnabled(int arg0) {
        checkContext();
        boolean _res = downstreamGLES2.glIsEnabled(arg0);
        String txt = new String("glIsEnabled(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glGenFramebuffers(int arg0, java.nio.IntBuffer arg1) {
        checkContext();
        downstreamGLES2.glGenFramebuffers(arg0, arg1);
        String txt = new String("glGenFramebuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glBufferSubData(int arg0, int arg1, int arg2, java.nio.Buffer arg3) {
        checkContext();
        downstreamGLES2.glBufferSubData(arg0, arg1, arg2, arg3);
        String txt = new String("glBufferSubData(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<java.nio.Buffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetBufferParameteriv(int arg0, int arg1, int[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glGetBufferParameteriv(arg0, arg1, arg2, arg3);
        String txt = new String("glGetBufferParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public boolean glIsTexture(int arg0) {
        checkContext();
        boolean _res = downstreamGLES2.glIsTexture(arg0);
        String txt = new String("glIsTexture(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glBindAttribLocation(int arg0, int arg1, java.lang.String arg2) {
        checkContext();
        downstreamGLES2.glBindAttribLocation(arg0, arg1, arg2);
        String txt = new String("glBindAttribLocation(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.lang.String>" + ")");
        checkGLGetError(txt);
    }

    public void glGetVertexAttribfv(int arg0, int arg1, float[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glGetVertexAttribfv(arg0, arg1, arg2, arg3);
        String txt = new String("glGetVertexAttribfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glUseProgram(int arg0) {
        checkContext();
        downstreamGLES2.glUseProgram(arg0);
        String txt = new String("glUseProgram(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGenerateMipmap(int arg0) {
        checkContext();
        downstreamGLES2.glGenerateMipmap(arg0);
        String txt = new String("glGenerateMipmap(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glTexParameterfv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        checkContext();
        downstreamGLES2.glTexParameterfv(arg0, arg1, arg2);
        String txt = new String("glTexParameterfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glDeleteRenderbuffers(int arg0, int[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glDeleteRenderbuffers(arg0, arg1, arg2);
        String txt = new String("glDeleteRenderbuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetIntegerv(int arg0, java.nio.IntBuffer arg1) {
        checkContext();
        downstreamGLES2.glGetIntegerv(arg0, arg1);
        String txt = new String("glGetIntegerv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glDeleteShader(int arg0) {
        checkContext();
        downstreamGLES2.glDeleteShader(arg0);
        String txt = new String("glDeleteShader(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glEnable(int arg0) {
        checkContext();
        downstreamGLES2.glEnable(arg0);
        String txt = new String("glEnable(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glVertexAttrib2fv(int arg0, java.nio.FloatBuffer arg1) {
        checkContext();
        downstreamGLES2.glVertexAttrib2fv(arg0, arg1);
        String txt = new String("glVertexAttrib2fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glStencilMaskSeparate(int arg0, int arg1) {
        checkContext();
        downstreamGLES2.glStencilMaskSeparate(arg0, arg1);
        String txt = new String("glStencilMaskSeparate(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glCopyTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7) {
        checkContext();
        downstreamGLES2.glCopyTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        String txt = new String("glCopyTexSubImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glAttachShader(int arg0, int arg1) {
        checkContext();
        downstreamGLES2.glAttachShader(arg0, arg1);
        String txt = new String("glAttachShader(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glUniform3i(int arg0, int arg1, int arg2, int arg3) {
        checkContext();
        downstreamGLES2.glUniform3i(arg0, arg1, arg2, arg3);
        String txt = new String("glUniform3i(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetActiveAttrib(int arg0, int arg1, int arg2, int[] arg3, int arg4, int[] arg5, int arg6, int[] arg7, int arg8, byte[] arg9, int arg10) {
        checkContext();
        downstreamGLES2.glGetActiveAttrib(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
        String txt = new String("glGetActiveAttrib(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg8).toUpperCase() + ", " + "<[B>" + ", " + "<int> 0x" + Integer.toHexString(arg10).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGenTextures(int arg0, java.nio.IntBuffer arg1) {
        checkContext();
        downstreamGLES2.glGenTextures(arg0, arg1);
        String txt = new String("glGenTextures(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glDrawElements(int arg0, int arg1, int arg2, java.nio.Buffer arg3) {
        checkContext();
        downstreamGLES2.glDrawElements(arg0, arg1, arg2, arg3);
        String txt = new String("glDrawElements(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<java.nio.Buffer>" + ")");
        checkGLGetError(txt);
    }

    public void glVertexAttrib3f(int arg0, float arg1, float arg2, float arg3) {
        checkContext();
        downstreamGLES2.glVertexAttrib3f(arg0, arg1, arg2, arg3);
        String txt = new String("glVertexAttrib3f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float>" + ", " + "<float>" + ", " + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glUniform2fv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        checkContext();
        downstreamGLES2.glUniform2fv(arg0, arg1, arg2);
        String txt = new String("glUniform2fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glUniform2f(int arg0, float arg1, float arg2) {
        checkContext();
        downstreamGLES2.glUniform2f(arg0, arg1, arg2);
        String txt = new String("glUniform2f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float>" + ", " + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glUniform2i(int arg0, int arg1, int arg2) {
        checkContext();
        downstreamGLES2.glUniform2i(arg0, arg1, arg2);
        String txt = new String("glUniform2i(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public boolean glIsFramebuffer(int arg0) {
        checkContext();
        boolean _res = downstreamGLES2.glIsFramebuffer(arg0);
        String txt = new String("glIsFramebuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
        return _res;
    }

    public int glGetBoundBuffer(int arg0) {
        checkContext();
        int _res = downstreamGLES2.glGetBoundBuffer(arg0);
        String txt = new String("glGetBoundBuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glUniform2fv(int arg0, int arg1, float[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glUniform2fv(arg0, arg1, arg2, arg3);
        String txt = new String("glUniform2fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glPolygonOffset(float arg0, float arg1) {
        checkContext();
        downstreamGLES2.glPolygonOffset(arg0, arg1);
        String txt = new String("glPolygonOffset(" + "<float>" + ", " + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glGetFramebufferAttachmentParameteriv(int arg0, int arg1, int arg2, int[] arg3, int arg4) {
        checkContext();
        downstreamGLES2.glGetFramebufferAttachmentParameteriv(arg0, arg1, arg2, arg3, arg4);
        String txt = new String("glGetFramebufferAttachmentParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glDeleteProgram(int arg0) {
        checkContext();
        downstreamGLES2.glDeleteProgram(arg0);
        String txt = new String("glDeleteProgram(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glBindRenderbuffer(int arg0, int arg1) {
        checkContext();
        downstreamGLES2.glBindRenderbuffer(arg0, arg1);
        String txt = new String("glBindRenderbuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetActiveAttrib(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3, java.nio.IntBuffer arg4, java.nio.IntBuffer arg5, java.nio.ByteBuffer arg6) {
        checkContext();
        downstreamGLES2.glGetActiveAttrib(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        String txt = new String("glGetActiveAttrib(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ", " + "<java.nio.IntBuffer>" + ", " + "<java.nio.IntBuffer>" + ", " + "<java.nio.ByteBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glDeleteFramebuffers(int arg0, int[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glDeleteFramebuffers(arg0, arg1, arg2);
        String txt = new String("glDeleteFramebuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public boolean glIsProgram(int arg0) {
        checkContext();
        boolean _res = downstreamGLES2.glIsProgram(arg0);
        String txt = new String("glIsProgram(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glViewport(int arg0, int arg1, int arg2, int arg3) {
        checkContext();
        downstreamGLES2.glViewport(arg0, arg1, arg2, arg3);
        String txt = new String("glViewport(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public java.lang.Object getPlatformGLExtensions() {
        return downstreamGLES2.getPlatformGLExtensions();
    }

    public void glVertexAttrib1fv(int arg0, float[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glVertexAttrib1fv(arg0, arg1, arg2);
        String txt = new String("glVertexAttrib1fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetFloatv(int arg0, java.nio.FloatBuffer arg1) {
        checkContext();
        downstreamGLES2.glGetFloatv(arg0, arg1);
        String txt = new String("glGetFloatv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glStencilOp(int arg0, int arg1, int arg2) {
        checkContext();
        downstreamGLES2.glStencilOp(arg0, arg1, arg2);
        String txt = new String("glStencilOp(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glVertexAttrib3fv(int arg0, float[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glVertexAttrib3fv(arg0, arg1, arg2);
        String txt = new String("glVertexAttrib3fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetUniformfv(int arg0, int arg1, float[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glGetUniformfv(arg0, arg1, arg2, arg3);
        String txt = new String("glGetUniformfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void setSwapInterval(int arg0) {
        downstreamGLES2.setSwapInterval(arg0);
    }

    public void glVertexAttrib4f(int arg0, float arg1, float arg2, float arg3, float arg4) {
        checkContext();
        downstreamGLES2.glVertexAttrib4f(arg0, arg1, arg2, arg3, arg4);
        String txt = new String("glVertexAttrib4f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float>" + ", " + "<float>" + ", " + "<float>" + ", " + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glHint(int arg0, int arg1) {
        checkContext();
        downstreamGLES2.glHint(arg0, arg1);
        String txt = new String("glHint(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetShaderSource(int arg0, int arg1, int[] arg2, int arg3, byte[] arg4, int arg5) {
        checkContext();
        downstreamGLES2.glGetShaderSource(arg0, arg1, arg2, arg3, arg4, arg5);
        String txt = new String("glGetShaderSource(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<[B>" + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glBlendEquation(int arg0) {
        checkContext();
        downstreamGLES2.glBlendEquation(arg0);
        String txt = new String("glBlendEquation(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetProgramInfoLog(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.ByteBuffer arg3) {
        checkContext();
        downstreamGLES2.glGetProgramInfoLog(arg0, arg1, arg2, arg3);
        String txt = new String("glGetProgramInfoLog(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ", " + "<java.nio.ByteBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glUniform3iv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        checkContext();
        downstreamGLES2.glUniform3iv(arg0, arg1, arg2);
        String txt = new String("glUniform3iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetTexParameterfv(int arg0, int arg1, float[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glGetTexParameterfv(arg0, arg1, arg2, arg3);
        String txt = new String("glGetTexParameterfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glUniform2iv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        checkContext();
        downstreamGLES2.glUniform2iv(arg0, arg1, arg2);
        String txt = new String("glUniform2iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glRenderbufferStorage(int arg0, int arg1, int arg2, int arg3) {
        checkContext();
        downstreamGLES2.glRenderbufferStorage(arg0, arg1, arg2, arg3);
        String txt = new String("glRenderbufferStorage(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetFloatv(int arg0, float[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glGetFloatv(arg0, arg1, arg2);
        String txt = new String("glGetFloatv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glVertexAttrib1fv(int arg0, java.nio.FloatBuffer arg1) {
        checkContext();
        downstreamGLES2.glVertexAttrib1fv(arg0, arg1);
        String txt = new String("glVertexAttrib1fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public int getSwapInterval() {
        return downstreamGLES2.getSwapInterval();
    }

    public void glGetTexParameteriv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        checkContext();
        downstreamGLES2.glGetTexParameteriv(arg0, arg1, arg2);
        String txt = new String("glGetTexParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetUniformfv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        checkContext();
        downstreamGLES2.glGetUniformfv(arg0, arg1, arg2);
        String txt = new String("glGetUniformfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetRenderbufferParameteriv(int arg0, int arg1, int[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glGetRenderbufferParameteriv(arg0, arg1, arg2, arg3);
        String txt = new String("glGetRenderbufferParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glVertexAttrib3fv(int arg0, java.nio.FloatBuffer arg1) {
        checkContext();
        downstreamGLES2.glVertexAttrib3fv(arg0, arg1);
        String txt = new String("glVertexAttrib3fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetProgramInfoLog(int arg0, int arg1, int[] arg2, int arg3, byte[] arg4, int arg5) {
        checkContext();
        downstreamGLES2.glGetProgramInfoLog(arg0, arg1, arg2, arg3, arg4, arg5);
        String txt = new String("glGetProgramInfoLog(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<[B>" + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glDetachShader(int arg0, int arg1) {
        checkContext();
        downstreamGLES2.glDetachShader(arg0, arg1);
        String txt = new String("glDetachShader(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetTexParameterfv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        checkContext();
        downstreamGLES2.glGetTexParameterfv(arg0, arg1, arg2);
        String txt = new String("glGetTexParameterfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetShaderSource(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.ByteBuffer arg3) {
        checkContext();
        downstreamGLES2.glGetShaderSource(arg0, arg1, arg2, arg3);
        String txt = new String("glGetShaderSource(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ", " + "<java.nio.ByteBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetShaderPrecisionFormat(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.IntBuffer arg3) {
        checkContext();
        downstreamGLES2.glGetShaderPrecisionFormat(arg0, arg1, arg2, arg3);
        String txt = new String("glGetShaderPrecisionFormat(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetShaderPrecisionFormat(int arg0, int arg1, int[] arg2, int arg3, int[] arg4, int arg5) {
        checkContext();
        downstreamGLES2.glGetShaderPrecisionFormat(arg0, arg1, arg2, arg3, arg4, arg5);
        String txt = new String("glGetShaderPrecisionFormat(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetAttachedShaders(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.IntBuffer arg3) {
        checkContext();
        downstreamGLES2.glGetAttachedShaders(arg0, arg1, arg2, arg3);
        String txt = new String("glGetAttachedShaders(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glGetAttachedShaders(int arg0, int arg1, int[] arg2, int arg3, int[] arg4, int arg5) {
        checkContext();
        downstreamGLES2.glGetAttachedShaders(arg0, arg1, arg2, arg3, arg4, arg5);
        String txt = new String("glGetAttachedShaders(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glCullFace(int arg0) {
        checkContext();
        downstreamGLES2.glCullFace(arg0);
        String txt = new String("glCullFace(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glDepthMask(boolean arg0) {
        checkContext();
        downstreamGLES2.glDepthMask(arg0);
        String txt = new String("glDepthMask(" + "<boolean>" + ")");
        checkGLGetError(txt);
    }

    public boolean glUnmapBuffer(int arg0) {
        checkContext();
        boolean _res = downstreamGLES2.glUnmapBuffer(arg0);
        String txt = new String("glUnmapBuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
        return _res;
    }

    public int glCreateProgram() {
        checkContext();
        int _res = downstreamGLES2.glCreateProgram();
        String txt = new String("glCreateProgram(" + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glDrawElements(int arg0, int arg1, int arg2, long arg3) {
        checkContext();
        downstreamGLES2.glDrawElements(arg0, arg1, arg2, arg3);
        String txt = new String("glDrawElements(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<long>" + ")");
        checkGLGetError(txt);
    }

    public void glFinish() {
        checkContext();
        downstreamGLES2.glFinish();
        String txt = new String("glFinish(" + ")");
        checkGLGetError(txt);
    }

    public int glCheckFramebufferStatus(int arg0) {
        checkContext();
        int _res = downstreamGLES2.glCheckFramebufferStatus(arg0);
        String txt = new String("glCheckFramebufferStatus(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
        return _res;
    }

    public void glFramebufferTexture2D(int arg0, int arg1, int arg2, int arg3, int arg4) {
        checkContext();
        downstreamGLES2.glFramebufferTexture2D(arg0, arg1, arg2, arg3, arg4);
        String txt = new String("glFramebufferTexture2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glClearDepthf(float arg0) {
        checkContext();
        downstreamGLES2.glClearDepthf(arg0);
        String txt = new String("glClearDepthf(" + "<float>" + ")");
        checkGLGetError(txt);
    }

    public void glShaderBinary(int arg0, int[] arg1, int arg2, int arg3, java.nio.Buffer arg4, int arg5) {
        checkContext();
        downstreamGLES2.glShaderBinary(arg0, arg1, arg2, arg3, arg4, arg5);
        String txt = new String("glShaderBinary(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<java.nio.Buffer>" + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public java.lang.Object getExtension(java.lang.String arg0) {
        return downstreamGLES2.getExtension(arg0);
    }

    public void glTexParameteriv(int arg0, int arg1, int[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glTexParameteriv(arg0, arg1, arg2, arg3);
        String txt = new String("glTexParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glGetBooleanv(int arg0, byte[] arg1, int arg2) {
        checkContext();
        downstreamGLES2.glGetBooleanv(arg0, arg1, arg2);
        String txt = new String("glGetBooleanv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[B>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glUniform4iv(int arg0, int arg1, int[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glUniform4iv(arg0, arg1, arg2, arg3);
        String txt = new String("glUniform4iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glActiveTexture(int arg0) {
        checkContext();
        downstreamGLES2.glActiveTexture(arg0);
        String txt = new String("glActiveTexture(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glDeleteBuffers(int arg0, java.nio.IntBuffer arg1) {
        checkContext();
        downstreamGLES2.glDeleteBuffers(arg0, arg1);
        String txt = new String("glDeleteBuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glPixelStorei(int arg0, int arg1) {
        checkContext();
        downstreamGLES2.glPixelStorei(arg0, arg1);
        String txt = new String("glPixelStorei(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glTexParameteri(int arg0, int arg1, int arg2) {
        checkContext();
        downstreamGLES2.glTexParameteri(arg0, arg1, arg2);
        String txt = new String("glTexParameteri(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glShaderSource(int arg0, int arg1, java.lang.String[] arg2, java.nio.IntBuffer arg3) {
        checkContext();
        downstreamGLES2.glShaderSource(arg0, arg1, arg2, arg3);
        String txt = new String("glShaderSource(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[Ljava.lang.String;>" + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glScissor(int arg0, int arg1, int arg2, int arg3) {
        checkContext();
        downstreamGLES2.glScissor(arg0, arg1, arg2, arg3);
        String txt = new String("glScissor(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.Buffer arg8) {
        checkContext();
        downstreamGLES2.glTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        String txt = new String("glTexSubImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ", " + "<java.nio.Buffer>" + ")");
        checkGLGetError(txt);
    }

    public void glUniform(javax.media.opengl.GLUniformData arg0) {
        checkContext();
        downstreamGLES2.glUniform(arg0);
        String txt = new String("glUniform(" + "<javax.media.opengl.GLUniformData>" + ")");
        checkGLGetError(txt);
    }

    public void glGetUniformiv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        checkContext();
        downstreamGLES2.glGetUniformiv(arg0, arg1, arg2);
        String txt = new String("glGetUniformiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer>" + ")");
        checkGLGetError(txt);
    }

    public void glClearStencil(int arg0) {
        checkContext();
        downstreamGLES2.glClearStencil(arg0);
        String txt = new String("glClearStencil(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glEnableVertexAttribArray(int arg0) {
        checkContext();
        downstreamGLES2.glEnableVertexAttribArray(arg0);
        String txt = new String("glEnableVertexAttribArray(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glUniform1iv(int arg0, int arg1, int[] arg2, int arg3) {
        checkContext();
        downstreamGLES2.glUniform1iv(arg0, arg1, arg2, arg3);
        String txt = new String("glUniform1iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        checkGLGetError(txt);
    }

    public void glVertexAttribPointer(int arg0, int arg1, int arg2, boolean arg3, int arg4, java.nio.Buffer arg5) {
        checkContext();
        downstreamGLES2.glVertexAttribPointer(arg0, arg1, arg2, arg3, arg4, arg5);
        String txt = new String("glVertexAttribPointer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<boolean>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<java.nio.Buffer>" + ")");
        checkGLGetError(txt);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("DebugGLES2 [ implementing javax.media.opengl.GLES2,\n\t");
        sb.append(" downstream: " + downstreamGLES2.toString() + "\n\t]");
        return sb.toString();
    }

    private void checkGLGetError(String caller) {
        int err = downstreamGLES2.glGetError();
        if (err == GL_NO_ERROR) {
            return;
        }
        StringBuffer buf = new StringBuffer(Thread.currentThread() + " glGetError() returned the following error codes after a call to " + caller + ": ");
        int recursionDepth = 10;
        do {
            switch(err) {
                case GL_INVALID_ENUM:
                    buf.append("GL_INVALID_ENUM ");
                    break;
                case GL_INVALID_VALUE:
                    buf.append("GL_INVALID_VALUE ");
                    break;
                case GL_INVALID_OPERATION:
                    buf.append("GL_INVALID_OPERATION ");
                    break;
                case GL_OUT_OF_MEMORY:
                    buf.append("GL_OUT_OF_MEMORY ");
                    break;
                case GL_NO_ERROR:
                    throw new InternalError("Should not be treating GL_NO_ERROR as error");
                default:
                    buf.append("Unknown glGetError() return value: ");
            }
            buf.append("( " + err + " 0x" + Integer.toHexString(err).toUpperCase() + "), ");
        } while ((--recursionDepth >= 0) && (err = downstreamGLES2.glGetError()) != GL_NO_ERROR);
        throw new GLException(buf.toString());
    }

    private void checkContext() {
        GLContext currentContext = GLContext.getCurrent();
        if (currentContext == null) {
            throw new GLException("No OpenGL context is current on this thread");
        }
        if ((_context != null) && (_context != currentContext)) {
            throw new GLException("This GL object is being incorrectly used with a different GLContext than that which created it");
        }
    }

    private GLContext _context;

    private GLES2 downstreamGLES2;
}
