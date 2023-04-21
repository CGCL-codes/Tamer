package javax.media.opengl;

import java.io.*;
import javax.media.opengl.*;
import javax.media.opengl.GLES2;
import javax.media.opengl.GLBase;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL;
import com.sun.gluegen.runtime.*;

/** <P> Composable pipeline which wraps an underlying {@link GL} implementation,
    providing tracing information to a user-specified {@link java.io.PrintStream}
    before and after each OpenGL method call. Sample code which installs this pipeline: </P>

<PRE>
     GL gl = drawable.setGL(new TraceGL(drawable.getGL(), System.err));
</PRE>
*/
public class TraceGLES2 implements javax.media.opengl.GLBase, javax.media.opengl.GL, javax.media.opengl.GL2ES2, javax.media.opengl.GLES2 {

    public static final boolean DEBUG = com.sun.opengl.impl.Debug.debug("TraceGLES2");

    public TraceGLES2(GLES2 downstreamGLES2, PrintStream stream) {
        if (downstreamGLES2 == null) {
            throw new IllegalArgumentException("null downstreamGLES2");
        }
        this.downstreamGLES2 = downstreamGLES2;
        this.stream = stream;
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
        printIndent();
        print("glVertexAttribPointer(" + "<javax.media.opengl.GLArrayData> " + arg0 + ")");
        downstreamGLES2.glVertexAttribPointer(arg0);
        println("");
    }

    public void glUniformMatrix2fv(int arg0, int arg1, boolean arg2, float[] arg3, int arg4) {
        printIndent();
        print("glUniformMatrix2fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<boolean> " + arg2 + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        downstreamGLES2.glUniformMatrix2fv(arg0, arg1, arg2, arg3, arg4);
        println("");
    }

    public void glUniform1f(int arg0, float arg1) {
        printIndent();
        print("glUniform1f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float> " + arg1 + ")");
        downstreamGLES2.glUniform1f(arg0, arg1);
        println("");
    }

    public void glBlendEquationSeparate(int arg0, int arg1) {
        printIndent();
        print("glBlendEquationSeparate(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        downstreamGLES2.glBlendEquationSeparate(arg0, arg1);
        println("");
    }

    public boolean glIsShader(int arg0) {
        printIndent();
        print("glIsShader(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        boolean _res = downstreamGLES2.glIsShader(arg0);
        println(" = " + _res);
        return _res;
    }

    public void glGetActiveUniform(int arg0, int arg1, int arg2, int[] arg3, int arg4, int[] arg5, int arg6, int[] arg7, int arg8, byte[] arg9, int arg10) {
        printIndent();
        print("glGetActiveUniform(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg8).toUpperCase() + ", " + "<[B>" + ", " + "<int> 0x" + Integer.toHexString(arg10).toUpperCase() + ")");
        downstreamGLES2.glGetActiveUniform(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
        println("");
    }

    public void glGenTextures(int arg0, int[] arg1, int arg2) {
        printIndent();
        print("glGenTextures(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glGenTextures(arg0, arg1, arg2);
        println("");
    }

    public void glReadPixels(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, long arg6) {
        printIndent();
        print("glReadPixels(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<long> " + arg6 + ")");
        downstreamGLES2.glReadPixels(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        println("");
    }

    public void glBindBuffer(int arg0, int arg1) {
        printIndent();
        print("glBindBuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        downstreamGLES2.glBindBuffer(arg0, arg1);
        println("");
    }

    public boolean isExtensionAvailable(java.lang.String arg0) {
        return downstreamGLES2.isExtensionAvailable(arg0);
    }

    public void glTexParameterf(int arg0, int arg1, float arg2) {
        printIndent();
        print("glTexParameterf(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<float> " + arg2 + ")");
        downstreamGLES2.glTexParameterf(arg0, arg1, arg2);
        println("");
    }

    public void glBlendFuncSeparate(int arg0, int arg1, int arg2, int arg3) {
        printIndent();
        print("glBlendFuncSeparate(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glBlendFuncSeparate(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glUniformMatrix4fv(int arg0, int arg1, boolean arg2, float[] arg3, int arg4) {
        printIndent();
        print("glUniformMatrix4fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<boolean> " + arg2 + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        downstreamGLES2.glUniformMatrix4fv(arg0, arg1, arg2, arg3, arg4);
        println("");
    }

    public int glCreateShader(int arg0) {
        printIndent();
        print("glCreateShader(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        int _res = downstreamGLES2.glCreateShader(arg0);
        println(" = " + _res);
        return _res;
    }

    public void glGetFramebufferAttachmentParameteriv(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3) {
        printIndent();
        print("glGetFramebufferAttachmentParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg3 + ")");
        downstreamGLES2.glGetFramebufferAttachmentParameteriv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glUniformMatrix2fv(int arg0, int arg1, boolean arg2, java.nio.FloatBuffer arg3) {
        printIndent();
        print("glUniformMatrix2fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<boolean> " + arg2 + ", " + "<java.nio.FloatBuffer> " + arg3 + ")");
        downstreamGLES2.glUniformMatrix2fv(arg0, arg1, arg2, arg3);
        println("");
    }

    public java.nio.ByteBuffer glMapBuffer(int arg0, int arg1) {
        printIndent();
        print("glMapBuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        java.nio.ByteBuffer _res = downstreamGLES2.glMapBuffer(arg0, arg1);
        println(" = " + _res);
        return _res;
    }

    public void glDeleteFramebuffers(int arg0, java.nio.IntBuffer arg1) {
        printIndent();
        print("glDeleteFramebuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg1 + ")");
        downstreamGLES2.glDeleteFramebuffers(arg0, arg1);
        println("");
    }

    public void glGetActiveUniform(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3, java.nio.IntBuffer arg4, java.nio.IntBuffer arg5, java.nio.ByteBuffer arg6) {
        printIndent();
        print("glGetActiveUniform(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg3 + ", " + "<java.nio.IntBuffer> " + arg4 + ", " + "<java.nio.IntBuffer> " + arg5 + ", " + "<java.nio.ByteBuffer> " + arg6 + ")");
        downstreamGLES2.glGetActiveUniform(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        println("");
    }

    public void glUniformMatrix4fv(int arg0, int arg1, boolean arg2, java.nio.FloatBuffer arg3) {
        printIndent();
        print("glUniformMatrix4fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<boolean> " + arg2 + ", " + "<java.nio.FloatBuffer> " + arg3 + ")");
        downstreamGLES2.glUniformMatrix4fv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glCompressedTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.Buffer arg8) {
        printIndent();
        print("glCompressedTexSubImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ", " + "<java.nio.Buffer> " + arg8 + ")");
        downstreamGLES2.glCompressedTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        println("");
    }

    public int glGetAttribLocation(int arg0, java.lang.String arg1) {
        printIndent();
        print("glGetAttribLocation(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.lang.String> " + arg1 + ")");
        int _res = downstreamGLES2.glGetAttribLocation(arg0, arg1);
        println(" = " + _res);
        return _res;
    }

    public void glFlush() {
        printIndent();
        print("glFlush(" + ")");
        downstreamGLES2.glFlush();
        println("");
    }

    public void glFrontFace(int arg0) {
        printIndent();
        print("glFrontFace(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glFrontFace(arg0);
        println("");
    }

    public void glGenBuffers(int arg0, int[] arg1, int arg2) {
        printIndent();
        print("glGenBuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glGenBuffers(arg0, arg1, arg2);
        println("");
    }

    public boolean glIsVBOElementEnabled() {
        printIndent();
        print("glIsVBOElementEnabled(" + ")");
        boolean _res = downstreamGLES2.glIsVBOElementEnabled();
        println(" = " + _res);
        return _res;
    }

    public void glDepthFunc(int arg0) {
        printIndent();
        print("glDepthFunc(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glDepthFunc(arg0);
        println("");
    }

    public void glBlendColor(float arg0, float arg1, float arg2, float arg3) {
        printIndent();
        print("glBlendColor(" + "<float> " + arg0 + ", " + "<float> " + arg1 + ", " + "<float> " + arg2 + ", " + "<float> " + arg3 + ")");
        downstreamGLES2.glBlendColor(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glUniform4fv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        printIndent();
        print("glUniform4fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer> " + arg2 + ")");
        downstreamGLES2.glUniform4fv(arg0, arg1, arg2);
        println("");
    }

    public void glUniform3f(int arg0, float arg1, float arg2, float arg3) {
        printIndent();
        print("glUniform3f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float> " + arg1 + ", " + "<float> " + arg2 + ", " + "<float> " + arg3 + ")");
        downstreamGLES2.glUniform3f(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glGenRenderbuffers(int arg0, int[] arg1, int arg2) {
        printIndent();
        print("glGenRenderbuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glGenRenderbuffers(arg0, arg1, arg2);
        println("");
    }

    public void glVertexAttrib2f(int arg0, float arg1, float arg2) {
        printIndent();
        print("glVertexAttrib2f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float> " + arg1 + ", " + "<float> " + arg2 + ")");
        downstreamGLES2.glVertexAttrib2f(arg0, arg1, arg2);
        println("");
    }

    public void glVertexAttrib4fv(int arg0, java.nio.FloatBuffer arg1) {
        printIndent();
        print("glVertexAttrib4fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.FloatBuffer> " + arg1 + ")");
        downstreamGLES2.glVertexAttrib4fv(arg0, arg1);
        println("");
    }

    public void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, long arg8) {
        printIndent();
        print("glTexImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ", " + "<long> " + arg8 + ")");
        downstreamGLES2.glTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        println("");
    }

    public int glGetError() {
        printIndent();
        print("glGetError(" + ")");
        int _res = downstreamGLES2.glGetError();
        println(" = " + _res);
        return _res;
    }

    public boolean hasGLSL() {
        return downstreamGLES2.hasGLSL();
    }

    public void glGetVertexAttribiv(int arg0, int arg1, int[] arg2, int arg3) {
        printIndent();
        print("glGetVertexAttribiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glGetVertexAttribiv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glGetProgramiv(int arg0, int arg1, int[] arg2, int arg3) {
        printIndent();
        print("glGetProgramiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glGetProgramiv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glStencilFuncSeparate(int arg0, int arg1, int arg2, int arg3) {
        printIndent();
        print("glStencilFuncSeparate(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glStencilFuncSeparate(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glDeleteRenderbuffers(int arg0, java.nio.IntBuffer arg1) {
        printIndent();
        print("glDeleteRenderbuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg1 + ")");
        downstreamGLES2.glDeleteRenderbuffers(arg0, arg1);
        println("");
    }

    public void glDeleteTextures(int arg0, int[] arg1, int arg2) {
        printIndent();
        print("glDeleteTextures(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glDeleteTextures(arg0, arg1, arg2);
        println("");
    }

    public void glGetShaderiv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        printIndent();
        print("glGetShaderiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ")");
        downstreamGLES2.glGetShaderiv(arg0, arg1, arg2);
        println("");
    }

    public void glGetBufferParameteriv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        printIndent();
        print("glGetBufferParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ")");
        downstreamGLES2.glGetBufferParameteriv(arg0, arg1, arg2);
        println("");
    }

    public void glGenFramebuffers(int arg0, int[] arg1, int arg2) {
        printIndent();
        print("glGenFramebuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glGenFramebuffers(arg0, arg1, arg2);
        println("");
    }

    public void glCompileShader(int arg0) {
        printIndent();
        print("glCompileShader(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glCompileShader(arg0);
        println("");
    }

    public int glGetUniformLocation(int arg0, java.lang.String arg1) {
        printIndent();
        print("glGetUniformLocation(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.lang.String> " + arg1 + ")");
        int _res = downstreamGLES2.glGetUniformLocation(arg0, arg1);
        println(" = " + _res);
        return _res;
    }

    public void glUniform4fv(int arg0, int arg1, float[] arg2, int arg3) {
        printIndent();
        print("glUniform4fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glUniform4fv(arg0, arg1, arg2, arg3);
        println("");
    }

    public boolean glIsBuffer(int arg0) {
        printIndent();
        print("glIsBuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        boolean _res = downstreamGLES2.glIsBuffer(arg0);
        println(" = " + _res);
        return _res;
    }

    public void glFramebufferRenderbuffer(int arg0, int arg1, int arg2, int arg3) {
        printIndent();
        print("glFramebufferRenderbuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glFramebufferRenderbuffer(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glUniform4f(int arg0, float arg1, float arg2, float arg3, float arg4) {
        printIndent();
        print("glUniform4f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float> " + arg1 + ", " + "<float> " + arg2 + ", " + "<float> " + arg3 + ", " + "<float> " + arg4 + ")");
        downstreamGLES2.glUniform4f(arg0, arg1, arg2, arg3, arg4);
        println("");
    }

    public void glUniform4i(int arg0, int arg1, int arg2, int arg3, int arg4) {
        printIndent();
        print("glUniform4i(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        downstreamGLES2.glUniform4i(arg0, arg1, arg2, arg3, arg4);
        println("");
    }

    public void glClearDepth(double arg0) {
        printIndent();
        print("glClearDepth(" + "<double> " + arg0 + ")");
        downstreamGLES2.glClearDepth(arg0);
        println("");
    }

    public void glVertexAttrib4fv(int arg0, float[] arg1, int arg2) {
        printIndent();
        print("glVertexAttrib4fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glVertexAttrib4fv(arg0, arg1, arg2);
        println("");
    }

    public boolean glIsRenderbuffer(int arg0) {
        printIndent();
        print("glIsRenderbuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        boolean _res = downstreamGLES2.glIsRenderbuffer(arg0);
        println(" = " + _res);
        return _res;
    }

    public void glGetIntegerv(int arg0, int[] arg1, int arg2) {
        printIndent();
        print("glGetIntegerv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glGetIntegerv(arg0, arg1, arg2);
        println("");
    }

    public void glCompressedTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, java.nio.Buffer arg7) {
        printIndent();
        print("glCompressedTexImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<java.nio.Buffer> " + arg7 + ")");
        downstreamGLES2.glCompressedTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        println("");
    }

    public void glStencilFunc(int arg0, int arg1, int arg2) {
        printIndent();
        print("glStencilFunc(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glStencilFunc(arg0, arg1, arg2);
        println("");
    }

    public void glCompressedTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, long arg7) {
        printIndent();
        print("glCompressedTexImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<long> " + arg7 + ")");
        downstreamGLES2.glCompressedTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        println("");
    }

    public void glDrawArrays(int arg0, int arg1, int arg2) {
        printIndent();
        print("glDrawArrays(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glDrawArrays(arg0, arg1, arg2);
        println("");
    }

    public void glLineWidth(float arg0) {
        printIndent();
        print("glLineWidth(" + "<float> " + arg0 + ")");
        downstreamGLES2.glLineWidth(arg0);
        println("");
    }

    public void glUniform4iv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        printIndent();
        print("glUniform4iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ")");
        downstreamGLES2.glUniform4iv(arg0, arg1, arg2);
        println("");
    }

    public void glDeleteBuffers(int arg0, int[] arg1, int arg2) {
        printIndent();
        print("glDeleteBuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glDeleteBuffers(arg0, arg1, arg2);
        println("");
    }

    public void glStencilMask(int arg0) {
        printIndent();
        print("glStencilMask(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glStencilMask(arg0);
        println("");
    }

    public void glBindTexture(int arg0, int arg1) {
        printIndent();
        print("glBindTexture(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        downstreamGLES2.glBindTexture(arg0, arg1);
        println("");
    }

    public void glColorMask(boolean arg0, boolean arg1, boolean arg2, boolean arg3) {
        printIndent();
        print("glColorMask(" + "<boolean> " + arg0 + ", " + "<boolean> " + arg1 + ", " + "<boolean> " + arg2 + ", " + "<boolean> " + arg3 + ")");
        downstreamGLES2.glColorMask(arg0, arg1, arg2, arg3);
        println("");
    }

    public javax.media.opengl.GLContext getContext() {
        return downstreamGLES2.getContext();
    }

    public void glTexParameteriv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        printIndent();
        print("glTexParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ")");
        downstreamGLES2.glTexParameteriv(arg0, arg1, arg2);
        println("");
    }

    public void glShaderBinary(int arg0, java.nio.IntBuffer arg1, int arg2, java.nio.Buffer arg3, int arg4) {
        printIndent();
        print("glShaderBinary(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg1 + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<java.nio.Buffer> " + arg3 + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        downstreamGLES2.glShaderBinary(arg0, arg1, arg2, arg3, arg4);
        println("");
    }

    public void glUniformMatrix3fv(int arg0, int arg1, boolean arg2, float[] arg3, int arg4) {
        printIndent();
        print("glUniformMatrix3fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<boolean> " + arg2 + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        downstreamGLES2.glUniformMatrix3fv(arg0, arg1, arg2, arg3, arg4);
        println("");
    }

    public boolean glIsVBOArrayEnabled() {
        printIndent();
        print("glIsVBOArrayEnabled(" + ")");
        boolean _res = downstreamGLES2.glIsVBOArrayEnabled();
        println(" = " + _res);
        return _res;
    }

    public void glClear(int arg0) {
        printIndent();
        print("glClear(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glClear(arg0);
        println("");
    }

    public void glClearColor(float arg0, float arg1, float arg2, float arg3) {
        printIndent();
        print("glClearColor(" + "<float> " + arg0 + ", " + "<float> " + arg1 + ", " + "<float> " + arg2 + ", " + "<float> " + arg3 + ")");
        downstreamGLES2.glClearColor(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glDepthRangef(float arg0, float arg1) {
        printIndent();
        print("glDepthRangef(" + "<float> " + arg0 + ", " + "<float> " + arg1 + ")");
        downstreamGLES2.glDepthRangef(arg0, arg1);
        println("");
    }

    public void glShaderSource(int arg0, int arg1, java.lang.String[] arg2, int[] arg3, int arg4) {
        printIndent();
        print("glShaderSource(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[Ljava.lang.String;>" + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        downstreamGLES2.glShaderSource(arg0, arg1, arg2, arg3, arg4);
        println("");
    }

    public void glGetBooleanv(int arg0, java.nio.ByteBuffer arg1) {
        printIndent();
        print("glGetBooleanv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.ByteBuffer> " + arg1 + ")");
        downstreamGLES2.glGetBooleanv(arg0, arg1);
        println("");
    }

    public void glTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.Buffer arg8) {
        printIndent();
        print("glTexImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ", " + "<java.nio.Buffer> " + arg8 + ")");
        downstreamGLES2.glTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        println("");
    }

    public void glUniform1iv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        printIndent();
        print("glUniform1iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ")");
        downstreamGLES2.glUniform1iv(arg0, arg1, arg2);
        println("");
    }

    public void glUniformMatrix3fv(int arg0, int arg1, boolean arg2, java.nio.FloatBuffer arg3) {
        printIndent();
        print("glUniformMatrix3fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<boolean> " + arg2 + ", " + "<java.nio.FloatBuffer> " + arg3 + ")");
        downstreamGLES2.glUniformMatrix3fv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glGetUniformiv(int arg0, int arg1, int[] arg2, int arg3) {
        printIndent();
        print("glGetUniformiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glGetUniformiv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glDepthRange(double arg0, double arg1) {
        printIndent();
        print("glDepthRange(" + "<double> " + arg0 + ", " + "<double> " + arg1 + ")");
        downstreamGLES2.glDepthRange(arg0, arg1);
        println("");
    }

    public void glDisable(int arg0) {
        printIndent();
        print("glDisable(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glDisable(arg0);
        println("");
    }

    public void glCompressedTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, long arg8) {
        printIndent();
        print("glCompressedTexSubImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ", " + "<long> " + arg8 + ")");
        downstreamGLES2.glCompressedTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        println("");
    }

    public void glUniform2iv(int arg0, int arg1, int[] arg2, int arg3) {
        printIndent();
        print("glUniform2iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glUniform2iv(arg0, arg1, arg2, arg3);
        println("");
    }

    public java.lang.String glGetString(int arg0) {
        printIndent();
        print("glGetString(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        java.lang.String _res = downstreamGLES2.glGetString(arg0);
        println(" = " + _res);
        return _res;
    }

    public void glCopyTexImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7) {
        printIndent();
        print("glCopyTexImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ")");
        downstreamGLES2.glCopyTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        println("");
    }

    public void glGetShaderInfoLog(int arg0, int arg1, int[] arg2, int arg3, byte[] arg4, int arg5) {
        printIndent();
        print("glGetShaderInfoLog(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<[B>" + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ")");
        downstreamGLES2.glGetShaderInfoLog(arg0, arg1, arg2, arg3, arg4, arg5);
        println("");
    }

    public void glBufferData(int arg0, int arg1, java.nio.Buffer arg2, int arg3) {
        printIndent();
        print("glBufferData(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.Buffer> " + arg2 + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glBufferData(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glUniform3fv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        printIndent();
        print("glUniform3fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer> " + arg2 + ")");
        downstreamGLES2.glUniform3fv(arg0, arg1, arg2);
        println("");
    }

    public void glUniform1fv(int arg0, int arg1, float[] arg2, int arg3) {
        printIndent();
        print("glUniform1fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glUniform1fv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glUniform3iv(int arg0, int arg1, int[] arg2, int arg3) {
        printIndent();
        print("glUniform3iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glUniform3iv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glGetTexParameteriv(int arg0, int arg1, int[] arg2, int arg3) {
        printIndent();
        print("glGetTexParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glGetTexParameteriv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glGetShaderInfoLog(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.ByteBuffer arg3) {
        printIndent();
        print("glGetShaderInfoLog(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ", " + "<java.nio.ByteBuffer> " + arg3 + ")");
        downstreamGLES2.glGetShaderInfoLog(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glGetRenderbufferParameteriv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        printIndent();
        print("glGetRenderbufferParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ")");
        downstreamGLES2.glGetRenderbufferParameteriv(arg0, arg1, arg2);
        println("");
    }

    public void glBlendFunc(int arg0, int arg1) {
        printIndent();
        print("glBlendFunc(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        downstreamGLES2.glBlendFunc(arg0, arg1);
        println("");
    }

    public void glReadPixels(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, java.nio.Buffer arg6) {
        printIndent();
        print("glReadPixels(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<java.nio.Buffer> " + arg6 + ")");
        downstreamGLES2.glReadPixels(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        println("");
    }

    public void glLinkProgram(int arg0) {
        printIndent();
        print("glLinkProgram(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glLinkProgram(arg0);
        println("");
    }

    public void glUniform3fv(int arg0, int arg1, float[] arg2, int arg3) {
        printIndent();
        print("glUniform3fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glUniform3fv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glBindFramebuffer(int arg0, int arg1) {
        printIndent();
        print("glBindFramebuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        downstreamGLES2.glBindFramebuffer(arg0, arg1);
        println("");
    }

    public void glDisableVertexAttribArray(int arg0) {
        printIndent();
        print("glDisableVertexAttribArray(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glDisableVertexAttribArray(arg0);
        println("");
    }

    public void glUniform1fv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        printIndent();
        print("glUniform1fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer> " + arg2 + ")");
        downstreamGLES2.glUniform1fv(arg0, arg1, arg2);
        println("");
    }

    public void glReleaseShaderCompiler() {
        printIndent();
        print("glReleaseShaderCompiler(" + ")");
        downstreamGLES2.glReleaseShaderCompiler();
        println("");
    }

    public void glVertexAttribPointer(int arg0, int arg1, int arg2, boolean arg3, int arg4, long arg5) {
        printIndent();
        print("glVertexAttribPointer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<boolean> " + arg3 + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<long> " + arg5 + ")");
        downstreamGLES2.glVertexAttribPointer(arg0, arg1, arg2, arg3, arg4, arg5);
        println("");
    }

    public void glVertexAttrib1f(int arg0, float arg1) {
        printIndent();
        print("glVertexAttrib1f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float> " + arg1 + ")");
        downstreamGLES2.glVertexAttrib1f(arg0, arg1);
        println("");
    }

    public void glGenRenderbuffers(int arg0, java.nio.IntBuffer arg1) {
        printIndent();
        print("glGenRenderbuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg1 + ")");
        downstreamGLES2.glGenRenderbuffers(arg0, arg1);
        println("");
    }

    public void glTexParameterfv(int arg0, int arg1, float[] arg2, int arg3) {
        printIndent();
        print("glTexParameterfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glTexParameterfv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glUniform1i(int arg0, int arg1) {
        printIndent();
        print("glUniform1i(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        downstreamGLES2.glUniform1i(arg0, arg1);
        println("");
    }

    public void glGenBuffers(int arg0, java.nio.IntBuffer arg1) {
        printIndent();
        print("glGenBuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg1 + ")");
        downstreamGLES2.glGenBuffers(arg0, arg1);
        println("");
    }

    public void glEGLImageTargetTexture2DOES(int arg0, java.nio.Buffer arg1) {
        printIndent();
        print("glEGLImageTargetTexture2DOES(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.Buffer> " + arg1 + ")");
        downstreamGLES2.glEGLImageTargetTexture2DOES(arg0, arg1);
        println("");
    }

    public void glGetVertexAttribfv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        printIndent();
        print("glGetVertexAttribfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer> " + arg2 + ")");
        downstreamGLES2.glGetVertexAttribfv(arg0, arg1, arg2);
        println("");
    }

    public void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, long arg8) {
        printIndent();
        print("glTexSubImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ", " + "<long> " + arg8 + ")");
        downstreamGLES2.glTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        println("");
    }

    public void glSampleCoverage(float arg0, boolean arg1) {
        printIndent();
        print("glSampleCoverage(" + "<float> " + arg0 + ", " + "<boolean> " + arg1 + ")");
        downstreamGLES2.glSampleCoverage(arg0, arg1);
        println("");
    }

    public void glGetVertexAttribiv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        printIndent();
        print("glGetVertexAttribiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ")");
        downstreamGLES2.glGetVertexAttribiv(arg0, arg1, arg2);
        println("");
    }

    public void glGetProgramiv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        printIndent();
        print("glGetProgramiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ")");
        downstreamGLES2.glGetProgramiv(arg0, arg1, arg2);
        println("");
    }

    public void glValidateProgram(int arg0) {
        printIndent();
        print("glValidateProgram(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glValidateProgram(arg0);
        println("");
    }

    public boolean isFunctionAvailable(java.lang.String arg0) {
        return downstreamGLES2.isFunctionAvailable(arg0);
    }

    public void glStencilOpSeparate(int arg0, int arg1, int arg2, int arg3) {
        printIndent();
        print("glStencilOpSeparate(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glStencilOpSeparate(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glVertexAttrib2fv(int arg0, float[] arg1, int arg2) {
        printIndent();
        print("glVertexAttrib2fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glVertexAttrib2fv(arg0, arg1, arg2);
        println("");
    }

    public void glDeleteTextures(int arg0, java.nio.IntBuffer arg1) {
        printIndent();
        print("glDeleteTextures(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg1 + ")");
        downstreamGLES2.glDeleteTextures(arg0, arg1);
        println("");
    }

    public void glGetShaderiv(int arg0, int arg1, int[] arg2, int arg3) {
        printIndent();
        print("glGetShaderiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glGetShaderiv(arg0, arg1, arg2, arg3);
        println("");
    }

    public boolean glIsEnabled(int arg0) {
        printIndent();
        print("glIsEnabled(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        boolean _res = downstreamGLES2.glIsEnabled(arg0);
        println(" = " + _res);
        return _res;
    }

    public void glGenFramebuffers(int arg0, java.nio.IntBuffer arg1) {
        printIndent();
        print("glGenFramebuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg1 + ")");
        downstreamGLES2.glGenFramebuffers(arg0, arg1);
        println("");
    }

    public void glBufferSubData(int arg0, int arg1, int arg2, java.nio.Buffer arg3) {
        printIndent();
        print("glBufferSubData(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<java.nio.Buffer> " + arg3 + ")");
        downstreamGLES2.glBufferSubData(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glGetBufferParameteriv(int arg0, int arg1, int[] arg2, int arg3) {
        printIndent();
        print("glGetBufferParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glGetBufferParameteriv(arg0, arg1, arg2, arg3);
        println("");
    }

    public boolean glIsTexture(int arg0) {
        printIndent();
        print("glIsTexture(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        boolean _res = downstreamGLES2.glIsTexture(arg0);
        println(" = " + _res);
        return _res;
    }

    public void glBindAttribLocation(int arg0, int arg1, java.lang.String arg2) {
        printIndent();
        print("glBindAttribLocation(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.lang.String> " + arg2 + ")");
        downstreamGLES2.glBindAttribLocation(arg0, arg1, arg2);
        println("");
    }

    public void glGetVertexAttribfv(int arg0, int arg1, float[] arg2, int arg3) {
        printIndent();
        print("glGetVertexAttribfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glGetVertexAttribfv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glUseProgram(int arg0) {
        printIndent();
        print("glUseProgram(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glUseProgram(arg0);
        println("");
    }

    public void glGenerateMipmap(int arg0) {
        printIndent();
        print("glGenerateMipmap(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glGenerateMipmap(arg0);
        println("");
    }

    public void glTexParameterfv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        printIndent();
        print("glTexParameterfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer> " + arg2 + ")");
        downstreamGLES2.glTexParameterfv(arg0, arg1, arg2);
        println("");
    }

    public void glDeleteRenderbuffers(int arg0, int[] arg1, int arg2) {
        printIndent();
        print("glDeleteRenderbuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glDeleteRenderbuffers(arg0, arg1, arg2);
        println("");
    }

    public void glGetIntegerv(int arg0, java.nio.IntBuffer arg1) {
        printIndent();
        print("glGetIntegerv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg1 + ")");
        downstreamGLES2.glGetIntegerv(arg0, arg1);
        println("");
    }

    public void glDeleteShader(int arg0) {
        printIndent();
        print("glDeleteShader(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glDeleteShader(arg0);
        println("");
    }

    public void glEnable(int arg0) {
        printIndent();
        print("glEnable(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glEnable(arg0);
        println("");
    }

    public void glVertexAttrib2fv(int arg0, java.nio.FloatBuffer arg1) {
        printIndent();
        print("glVertexAttrib2fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.FloatBuffer> " + arg1 + ")");
        downstreamGLES2.glVertexAttrib2fv(arg0, arg1);
        println("");
    }

    public void glStencilMaskSeparate(int arg0, int arg1) {
        printIndent();
        print("glStencilMaskSeparate(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        downstreamGLES2.glStencilMaskSeparate(arg0, arg1);
        println("");
    }

    public void glCopyTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7) {
        printIndent();
        print("glCopyTexSubImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ")");
        downstreamGLES2.glCopyTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        println("");
    }

    public void glAttachShader(int arg0, int arg1) {
        printIndent();
        print("glAttachShader(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        downstreamGLES2.glAttachShader(arg0, arg1);
        println("");
    }

    public void glUniform3i(int arg0, int arg1, int arg2, int arg3) {
        printIndent();
        print("glUniform3i(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glUniform3i(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glGetActiveAttrib(int arg0, int arg1, int arg2, int[] arg3, int arg4, int[] arg5, int arg6, int[] arg7, int arg8, byte[] arg9, int arg10) {
        printIndent();
        print("glGetActiveAttrib(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg8).toUpperCase() + ", " + "<[B>" + ", " + "<int> 0x" + Integer.toHexString(arg10).toUpperCase() + ")");
        downstreamGLES2.glGetActiveAttrib(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
        println("");
    }

    public void glGenTextures(int arg0, java.nio.IntBuffer arg1) {
        printIndent();
        print("glGenTextures(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg1 + ")");
        downstreamGLES2.glGenTextures(arg0, arg1);
        println("");
    }

    public void glDrawElements(int arg0, int arg1, int arg2, java.nio.Buffer arg3) {
        printIndent();
        print("glDrawElements(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<java.nio.Buffer> " + arg3 + ")");
        downstreamGLES2.glDrawElements(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glVertexAttrib3f(int arg0, float arg1, float arg2, float arg3) {
        printIndent();
        print("glVertexAttrib3f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float> " + arg1 + ", " + "<float> " + arg2 + ", " + "<float> " + arg3 + ")");
        downstreamGLES2.glVertexAttrib3f(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glUniform2fv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        printIndent();
        print("glUniform2fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer> " + arg2 + ")");
        downstreamGLES2.glUniform2fv(arg0, arg1, arg2);
        println("");
    }

    public void glUniform2f(int arg0, float arg1, float arg2) {
        printIndent();
        print("glUniform2f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float> " + arg1 + ", " + "<float> " + arg2 + ")");
        downstreamGLES2.glUniform2f(arg0, arg1, arg2);
        println("");
    }

    public void glUniform2i(int arg0, int arg1, int arg2) {
        printIndent();
        print("glUniform2i(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glUniform2i(arg0, arg1, arg2);
        println("");
    }

    public boolean glIsFramebuffer(int arg0) {
        printIndent();
        print("glIsFramebuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        boolean _res = downstreamGLES2.glIsFramebuffer(arg0);
        println(" = " + _res);
        return _res;
    }

    public int glGetBoundBuffer(int arg0) {
        printIndent();
        print("glGetBoundBuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        int _res = downstreamGLES2.glGetBoundBuffer(arg0);
        println(" = " + _res);
        return _res;
    }

    public void glUniform2fv(int arg0, int arg1, float[] arg2, int arg3) {
        printIndent();
        print("glUniform2fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glUniform2fv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glPolygonOffset(float arg0, float arg1) {
        printIndent();
        print("glPolygonOffset(" + "<float> " + arg0 + ", " + "<float> " + arg1 + ")");
        downstreamGLES2.glPolygonOffset(arg0, arg1);
        println("");
    }

    public void glGetFramebufferAttachmentParameteriv(int arg0, int arg1, int arg2, int[] arg3, int arg4) {
        printIndent();
        print("glGetFramebufferAttachmentParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        downstreamGLES2.glGetFramebufferAttachmentParameteriv(arg0, arg1, arg2, arg3, arg4);
        println("");
    }

    public void glDeleteProgram(int arg0) {
        printIndent();
        print("glDeleteProgram(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glDeleteProgram(arg0);
        println("");
    }

    public void glBindRenderbuffer(int arg0, int arg1) {
        printIndent();
        print("glBindRenderbuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        downstreamGLES2.glBindRenderbuffer(arg0, arg1);
        println("");
    }

    public void glGetActiveAttrib(int arg0, int arg1, int arg2, java.nio.IntBuffer arg3, java.nio.IntBuffer arg4, java.nio.IntBuffer arg5, java.nio.ByteBuffer arg6) {
        printIndent();
        print("glGetActiveAttrib(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg3 + ", " + "<java.nio.IntBuffer> " + arg4 + ", " + "<java.nio.IntBuffer> " + arg5 + ", " + "<java.nio.ByteBuffer> " + arg6 + ")");
        downstreamGLES2.glGetActiveAttrib(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        println("");
    }

    public void glDeleteFramebuffers(int arg0, int[] arg1, int arg2) {
        printIndent();
        print("glDeleteFramebuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glDeleteFramebuffers(arg0, arg1, arg2);
        println("");
    }

    public boolean glIsProgram(int arg0) {
        printIndent();
        print("glIsProgram(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        boolean _res = downstreamGLES2.glIsProgram(arg0);
        println(" = " + _res);
        return _res;
    }

    public void glViewport(int arg0, int arg1, int arg2, int arg3) {
        printIndent();
        print("glViewport(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glViewport(arg0, arg1, arg2, arg3);
        println("");
    }

    public java.lang.Object getPlatformGLExtensions() {
        return downstreamGLES2.getPlatformGLExtensions();
    }

    public void glVertexAttrib1fv(int arg0, float[] arg1, int arg2) {
        printIndent();
        print("glVertexAttrib1fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glVertexAttrib1fv(arg0, arg1, arg2);
        println("");
    }

    public void glGetFloatv(int arg0, java.nio.FloatBuffer arg1) {
        printIndent();
        print("glGetFloatv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.FloatBuffer> " + arg1 + ")");
        downstreamGLES2.glGetFloatv(arg0, arg1);
        println("");
    }

    public void glStencilOp(int arg0, int arg1, int arg2) {
        printIndent();
        print("glStencilOp(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glStencilOp(arg0, arg1, arg2);
        println("");
    }

    public void glVertexAttrib3fv(int arg0, float[] arg1, int arg2) {
        printIndent();
        print("glVertexAttrib3fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glVertexAttrib3fv(arg0, arg1, arg2);
        println("");
    }

    public void glGetUniformfv(int arg0, int arg1, float[] arg2, int arg3) {
        printIndent();
        print("glGetUniformfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glGetUniformfv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void setSwapInterval(int arg0) {
        downstreamGLES2.setSwapInterval(arg0);
    }

    public void glVertexAttrib4f(int arg0, float arg1, float arg2, float arg3, float arg4) {
        printIndent();
        print("glVertexAttrib4f(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<float> " + arg1 + ", " + "<float> " + arg2 + ", " + "<float> " + arg3 + ", " + "<float> " + arg4 + ")");
        downstreamGLES2.glVertexAttrib4f(arg0, arg1, arg2, arg3, arg4);
        println("");
    }

    public void glHint(int arg0, int arg1) {
        printIndent();
        print("glHint(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        downstreamGLES2.glHint(arg0, arg1);
        println("");
    }

    public void glGetShaderSource(int arg0, int arg1, int[] arg2, int arg3, byte[] arg4, int arg5) {
        printIndent();
        print("glGetShaderSource(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<[B>" + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ")");
        downstreamGLES2.glGetShaderSource(arg0, arg1, arg2, arg3, arg4, arg5);
        println("");
    }

    public void glBlendEquation(int arg0) {
        printIndent();
        print("glBlendEquation(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glBlendEquation(arg0);
        println("");
    }

    public void glGetProgramInfoLog(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.ByteBuffer arg3) {
        printIndent();
        print("glGetProgramInfoLog(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ", " + "<java.nio.ByteBuffer> " + arg3 + ")");
        downstreamGLES2.glGetProgramInfoLog(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glUniform3iv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        printIndent();
        print("glUniform3iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ")");
        downstreamGLES2.glUniform3iv(arg0, arg1, arg2);
        println("");
    }

    public void glGetTexParameterfv(int arg0, int arg1, float[] arg2, int arg3) {
        printIndent();
        print("glGetTexParameterfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glGetTexParameterfv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glUniform2iv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        printIndent();
        print("glUniform2iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ")");
        downstreamGLES2.glUniform2iv(arg0, arg1, arg2);
        println("");
    }

    public void glRenderbufferStorage(int arg0, int arg1, int arg2, int arg3) {
        printIndent();
        print("glRenderbufferStorage(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glRenderbufferStorage(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glGetFloatv(int arg0, float[] arg1, int arg2) {
        printIndent();
        print("glGetFloatv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[F>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glGetFloatv(arg0, arg1, arg2);
        println("");
    }

    public void glVertexAttrib1fv(int arg0, java.nio.FloatBuffer arg1) {
        printIndent();
        print("glVertexAttrib1fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.FloatBuffer> " + arg1 + ")");
        downstreamGLES2.glVertexAttrib1fv(arg0, arg1);
        println("");
    }

    public int getSwapInterval() {
        return downstreamGLES2.getSwapInterval();
    }

    public void glGetTexParameteriv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        printIndent();
        print("glGetTexParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ")");
        downstreamGLES2.glGetTexParameteriv(arg0, arg1, arg2);
        println("");
    }

    public void glGetUniformfv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        printIndent();
        print("glGetUniformfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer> " + arg2 + ")");
        downstreamGLES2.glGetUniformfv(arg0, arg1, arg2);
        println("");
    }

    public void glGetRenderbufferParameteriv(int arg0, int arg1, int[] arg2, int arg3) {
        printIndent();
        print("glGetRenderbufferParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glGetRenderbufferParameteriv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glVertexAttrib3fv(int arg0, java.nio.FloatBuffer arg1) {
        printIndent();
        print("glVertexAttrib3fv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.FloatBuffer> " + arg1 + ")");
        downstreamGLES2.glVertexAttrib3fv(arg0, arg1);
        println("");
    }

    public void glGetProgramInfoLog(int arg0, int arg1, int[] arg2, int arg3, byte[] arg4, int arg5) {
        printIndent();
        print("glGetProgramInfoLog(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<[B>" + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ")");
        downstreamGLES2.glGetProgramInfoLog(arg0, arg1, arg2, arg3, arg4, arg5);
        println("");
    }

    public void glDetachShader(int arg0, int arg1) {
        printIndent();
        print("glDetachShader(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        downstreamGLES2.glDetachShader(arg0, arg1);
        println("");
    }

    public void glGetTexParameterfv(int arg0, int arg1, java.nio.FloatBuffer arg2) {
        printIndent();
        print("glGetTexParameterfv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.FloatBuffer> " + arg2 + ")");
        downstreamGLES2.glGetTexParameterfv(arg0, arg1, arg2);
        println("");
    }

    public void glGetShaderSource(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.ByteBuffer arg3) {
        printIndent();
        print("glGetShaderSource(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ", " + "<java.nio.ByteBuffer> " + arg3 + ")");
        downstreamGLES2.glGetShaderSource(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glGetShaderPrecisionFormat(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.IntBuffer arg3) {
        printIndent();
        print("glGetShaderPrecisionFormat(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ", " + "<java.nio.IntBuffer> " + arg3 + ")");
        downstreamGLES2.glGetShaderPrecisionFormat(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glGetShaderPrecisionFormat(int arg0, int arg1, int[] arg2, int arg3, int[] arg4, int arg5) {
        printIndent();
        print("glGetShaderPrecisionFormat(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ")");
        downstreamGLES2.glGetShaderPrecisionFormat(arg0, arg1, arg2, arg3, arg4, arg5);
        println("");
    }

    public void glGetAttachedShaders(int arg0, int arg1, java.nio.IntBuffer arg2, java.nio.IntBuffer arg3) {
        printIndent();
        print("glGetAttachedShaders(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ", " + "<java.nio.IntBuffer> " + arg3 + ")");
        downstreamGLES2.glGetAttachedShaders(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glGetAttachedShaders(int arg0, int arg1, int[] arg2, int arg3, int[] arg4, int arg5) {
        printIndent();
        print("glGetAttachedShaders(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ")");
        downstreamGLES2.glGetAttachedShaders(arg0, arg1, arg2, arg3, arg4, arg5);
        println("");
    }

    public void glCullFace(int arg0) {
        printIndent();
        print("glCullFace(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glCullFace(arg0);
        println("");
    }

    public void glDepthMask(boolean arg0) {
        printIndent();
        print("glDepthMask(" + "<boolean> " + arg0 + ")");
        downstreamGLES2.glDepthMask(arg0);
        println("");
    }

    public boolean glUnmapBuffer(int arg0) {
        printIndent();
        print("glUnmapBuffer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        boolean _res = downstreamGLES2.glUnmapBuffer(arg0);
        println(" = " + _res);
        return _res;
    }

    public int glCreateProgram() {
        printIndent();
        print("glCreateProgram(" + ")");
        int _res = downstreamGLES2.glCreateProgram();
        println(" = " + _res);
        return _res;
    }

    public void glDrawElements(int arg0, int arg1, int arg2, long arg3) {
        printIndent();
        print("glDrawElements(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<long> " + arg3 + ")");
        downstreamGLES2.glDrawElements(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glFinish() {
        printIndent();
        print("glFinish(" + ")");
        downstreamGLES2.glFinish();
        println("");
    }

    public int glCheckFramebufferStatus(int arg0) {
        printIndent();
        print("glCheckFramebufferStatus(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        int _res = downstreamGLES2.glCheckFramebufferStatus(arg0);
        println(" = " + _res);
        return _res;
    }

    public void glFramebufferTexture2D(int arg0, int arg1, int arg2, int arg3, int arg4) {
        printIndent();
        print("glFramebufferTexture2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ")");
        downstreamGLES2.glFramebufferTexture2D(arg0, arg1, arg2, arg3, arg4);
        println("");
    }

    public void glClearDepthf(float arg0) {
        printIndent();
        print("glClearDepthf(" + "<float> " + arg0 + ")");
        downstreamGLES2.glClearDepthf(arg0);
        println("");
    }

    public void glShaderBinary(int arg0, int[] arg1, int arg2, int arg3, java.nio.Buffer arg4, int arg5) {
        printIndent();
        print("glShaderBinary(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<java.nio.Buffer> " + arg4 + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ")");
        downstreamGLES2.glShaderBinary(arg0, arg1, arg2, arg3, arg4, arg5);
        println("");
    }

    public java.lang.Object getExtension(java.lang.String arg0) {
        return downstreamGLES2.getExtension(arg0);
    }

    public void glTexParameteriv(int arg0, int arg1, int[] arg2, int arg3) {
        printIndent();
        print("glTexParameteriv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glTexParameteriv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glGetBooleanv(int arg0, byte[] arg1, int arg2) {
        printIndent();
        print("glGetBooleanv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<[B>" + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glGetBooleanv(arg0, arg1, arg2);
        println("");
    }

    public void glUniform4iv(int arg0, int arg1, int[] arg2, int arg3) {
        printIndent();
        print("glUniform4iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glUniform4iv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glActiveTexture(int arg0) {
        printIndent();
        print("glActiveTexture(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glActiveTexture(arg0);
        println("");
    }

    public void glDeleteBuffers(int arg0, java.nio.IntBuffer arg1) {
        printIndent();
        print("glDeleteBuffers(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg1 + ")");
        downstreamGLES2.glDeleteBuffers(arg0, arg1);
        println("");
    }

    public void glPixelStorei(int arg0, int arg1) {
        printIndent();
        print("glPixelStorei(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ")");
        downstreamGLES2.glPixelStorei(arg0, arg1);
        println("");
    }

    public void glTexParameteri(int arg0, int arg1, int arg2) {
        printIndent();
        print("glTexParameteri(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ")");
        downstreamGLES2.glTexParameteri(arg0, arg1, arg2);
        println("");
    }

    public void glShaderSource(int arg0, int arg1, java.lang.String[] arg2, java.nio.IntBuffer arg3) {
        printIndent();
        print("glShaderSource(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[Ljava.lang.String;>" + ", " + "<java.nio.IntBuffer> " + arg3 + ")");
        downstreamGLES2.glShaderSource(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glScissor(int arg0, int arg1, int arg2, int arg3) {
        printIndent();
        print("glScissor(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glScissor(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glTexSubImage2D(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, java.nio.Buffer arg8) {
        printIndent();
        print("glTexSubImage2D(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg5).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg6).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg7).toUpperCase() + ", " + "<java.nio.Buffer> " + arg8 + ")");
        downstreamGLES2.glTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        println("");
    }

    public void glUniform(javax.media.opengl.GLUniformData arg0) {
        printIndent();
        print("glUniform(" + "<javax.media.opengl.GLUniformData> " + arg0 + ")");
        downstreamGLES2.glUniform(arg0);
        println("");
    }

    public void glGetUniformiv(int arg0, int arg1, java.nio.IntBuffer arg2) {
        printIndent();
        print("glGetUniformiv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<java.nio.IntBuffer> " + arg2 + ")");
        downstreamGLES2.glGetUniformiv(arg0, arg1, arg2);
        println("");
    }

    public void glClearStencil(int arg0) {
        printIndent();
        print("glClearStencil(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glClearStencil(arg0);
        println("");
    }

    public void glEnableVertexAttribArray(int arg0) {
        printIndent();
        print("glEnableVertexAttribArray(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ")");
        downstreamGLES2.glEnableVertexAttribArray(arg0);
        println("");
    }

    public void glUniform1iv(int arg0, int arg1, int[] arg2, int arg3) {
        printIndent();
        print("glUniform1iv(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<[I>" + ", " + "<int> 0x" + Integer.toHexString(arg3).toUpperCase() + ")");
        downstreamGLES2.glUniform1iv(arg0, arg1, arg2, arg3);
        println("");
    }

    public void glVertexAttribPointer(int arg0, int arg1, int arg2, boolean arg3, int arg4, java.nio.Buffer arg5) {
        printIndent();
        print("glVertexAttribPointer(" + "<int> 0x" + Integer.toHexString(arg0).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg1).toUpperCase() + ", " + "<int> 0x" + Integer.toHexString(arg2).toUpperCase() + ", " + "<boolean> " + arg3 + ", " + "<int> 0x" + Integer.toHexString(arg4).toUpperCase() + ", " + "<java.nio.Buffer> " + arg5 + ")");
        downstreamGLES2.glVertexAttribPointer(arg0, arg1, arg2, arg3, arg4, arg5);
        println("");
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("TraceGLES2 [ implementing javax.media.opengl.GLES2,\n\t");
        sb.append(" downstream: " + downstreamGLES2.toString() + "\n\t]");
        return sb.toString();
    }

    private PrintStream stream;

    private int indent = 0;

    protected String dumpArray(Object obj) {
        if (obj == null) return "[null]";
        StringBuffer sb = new StringBuffer("[");
        int len = java.lang.reflect.Array.getLength(obj);
        int count = Math.min(len, 16);
        for (int i = 0; i < count; i++) {
            sb.append(java.lang.reflect.Array.get(obj, i));
            if (i < count - 1) sb.append(',');
        }
        if (len > 16) sb.append("...").append(len);
        sb.append(']');
        return sb.toString();
    }

    protected void print(String str) {
        stream.print(str);
    }

    protected void println(String str) {
        stream.println(str);
    }

    protected void printIndent() {
        for (int i = 0; i < indent; i++) {
            stream.print(' ');
        }
    }

    private GLES2 downstreamGLES2;
}
