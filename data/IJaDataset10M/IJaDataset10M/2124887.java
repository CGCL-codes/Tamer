package com.sun.opengl.util.glsl;

import java.io.PrintStream;
import java.nio.*;
import java.util.*;
import javax.media.opengl.*;

public class ShaderUtil {

    abstract static class Impl {

        public abstract String getShaderInfoLog(GL gl, int shaderObj);

        public abstract String getProgramInfoLog(GL gl, int programObj);

        public abstract boolean isShaderStatusValid(GL gl, int shaderObj, int name);

        public abstract boolean isShaderStatusValid(GL gl, int shaderObj, int name, PrintStream verboseOut);

        public abstract boolean isShaderStatusValid(GL gl, IntBuffer shaders, int name);

        public abstract boolean isShaderStatusValid(GL gl, IntBuffer shaders, int name, PrintStream verboseOut);

        public abstract boolean isProgramStatusValid(GL gl, int programObj, int name);

        public abstract boolean isProgramValid(GL gl, int programObj);

        public abstract boolean isProgramValid(GL gl, int programObj, PrintStream verboseOut);

        public abstract void createShader(GL gl, int type, IntBuffer shaders);

        public abstract Set getShaderBinaryFormats(GL gl);

        public abstract boolean isShaderCompilerAvailable(GL gl);

        public abstract void shaderSource(GL gl, int shader, java.lang.String[] source);

        public abstract void shaderSource(GL gl, IntBuffer shaders, java.lang.String[][] sources);

        public abstract void shaderBinary(GL gl, IntBuffer shaders, int binFormat, java.nio.Buffer bin);

        public abstract void compileShader(GL gl, IntBuffer shaders);

        public abstract void attachShader(GL gl, int program, IntBuffer shaders);

        public abstract void detachShader(GL gl, int program, IntBuffer shaders);

        public abstract void deleteShader(GL gl, IntBuffer shaders);
    }

    static class GL2ES2Impl extends Impl {

        public String getShaderInfoLog(GL _gl, int shaderObj) {
            GL2ES2 gl = _gl.getGL2ES2();
            int[] infoLogLength = new int[1];
            gl.glGetShaderiv(shaderObj, gl.GL_INFO_LOG_LENGTH, infoLogLength, 0);
            if (infoLogLength[0] == 0) {
                return "(no info log)";
            }
            int[] charsWritten = new int[1];
            byte[] infoLogBytes = new byte[infoLogLength[0]];
            gl.glGetShaderInfoLog(shaderObj, infoLogLength[0], charsWritten, 0, infoLogBytes, 0);
            return new String(infoLogBytes, 0, charsWritten[0]);
        }

        public String getProgramInfoLog(GL _gl, int programObj) {
            GL2ES2 gl = _gl.getGL2ES2();
            int[] infoLogLength = new int[1];
            gl.glGetProgramiv(programObj, gl.GL_INFO_LOG_LENGTH, infoLogLength, 0);
            if (infoLogLength[0] == 0) {
                return "(no info log)";
            }
            int[] charsWritten = new int[1];
            byte[] infoLogBytes = new byte[infoLogLength[0]];
            gl.glGetProgramInfoLog(programObj, infoLogLength[0], charsWritten, 0, infoLogBytes, 0);
            return new String(infoLogBytes, 0, charsWritten[0]);
        }

        public boolean isShaderStatusValid(GL _gl, int shaderObj, int name) {
            return isShaderStatusValid(_gl, shaderObj, name, null);
        }

        public boolean isShaderStatusValid(GL _gl, int shaderObj, int name, PrintStream verboseOut) {
            GL2ES2 gl = _gl.getGL2ES2();
            int[] ires = new int[1];
            gl.glGetShaderiv(shaderObj, name, ires, 0);
            boolean res = ires[0] == 1;
            if (!res && null != verboseOut) {
                verboseOut.println("Shader status invalid: " + getShaderInfoLog(gl, shaderObj));
            }
            return res;
        }

        public boolean isShaderStatusValid(GL _gl, IntBuffer shaders, int name) {
            return isShaderStatusValid(_gl, shaders, name, null);
        }

        public boolean isShaderStatusValid(GL _gl, IntBuffer shaders, int name, PrintStream verboseOut) {
            boolean res = true;
            for (int i = shaders.position(); i < shaders.limit(); i++) {
                res = isShaderStatusValid(_gl, shaders.get(i), name, verboseOut) && res;
            }
            return res;
        }

        public boolean isProgramStatusValid(GL _gl, int programObj, int name) {
            GL2ES2 gl = _gl.getGL2ES2();
            int[] ires = new int[1];
            gl.glGetProgramiv(programObj, name, ires, 0);
            return ires[0] == 1;
        }

        public boolean isProgramValid(GL _gl, int programObj) {
            return isProgramValid(_gl, programObj, null);
        }

        public boolean isProgramValid(GL _gl, int programObj, PrintStream verboseOut) {
            GL2ES2 gl = _gl.getGL2ES2();
            int[] ires = new int[1];
            if (!gl.glIsProgram(programObj)) {
                if (null != verboseOut) {
                    verboseOut.println("Program name invalid: " + programObj);
                }
                return false;
            }
            if (!isProgramStatusValid(gl, programObj, gl.GL_LINK_STATUS)) {
                if (null != verboseOut) {
                    verboseOut.println("Program link failed: " + programObj + "\n\t" + getProgramInfoLog(gl, programObj));
                }
                return false;
            }
            if (!gl.isGLES2() || isShaderCompilerAvailable(gl)) {
                gl.glValidateProgram(programObj);
                if (!isProgramStatusValid(gl, programObj, gl.GL_VALIDATE_STATUS)) {
                    if (null != verboseOut) {
                        verboseOut.println("Program validation failed: " + programObj + "\n\t" + getProgramInfoLog(gl, programObj));
                    }
                    return false;
                }
            }
            return true;
        }

        public void createShader(GL _gl, int type, IntBuffer shaders) {
            GL2ES2 gl = _gl.getGL2ES2();
            for (int i = shaders.position(); i < shaders.limit(); i++) {
                shaders.put(i, gl.glCreateShader(type));
            }
        }

        private Boolean shaderCompilerAvailable = null;

        private Set shaderBinaryFormats = null;

        public Set getShaderBinaryFormats(GL _gl) {
            GL2ES2 gl = _gl.getGL2ES2();
            if (null == shaderBinaryFormats) {
                if (gl.getContext() != GLContext.getCurrent()) {
                    return new HashSet(0);
                }
                int[] param = new int[1];
                shaderBinaryFormats = new HashSet();
                if (gl.isGLES2()) {
                    gl.glGetIntegerv(GL2ES2.GL_NUM_SHADER_BINARY_FORMATS, param, 0);
                    int numFormats = param[0];
                    if (numFormats > 0) {
                        int[] formats = new int[numFormats];
                        gl.glGetIntegerv(GL2ES2.GL_SHADER_BINARY_FORMATS, formats, 0);
                        for (int i = 0; i < numFormats; i++) {
                            shaderBinaryFormats.add(new Integer(formats[i]));
                        }
                    }
                }
            }
            return shaderBinaryFormats;
        }

        public boolean isShaderCompilerAvailable(GL _gl) {
            GL2ES2 gl = _gl.getGL2ES2();
            if (null == shaderCompilerAvailable) {
                if (gl.getContext() != GLContext.getCurrent()) {
                    return false;
                }
                Set bfs = getShaderBinaryFormats(gl);
                if (gl.isGLES2()) {
                    byte[] param = new byte[1];
                    gl.glGetBooleanv(GL2ES2.GL_SHADER_COMPILER, param, 0);
                    boolean v = param[0] != (byte) 0x00;
                    if (!v && bfs.size() == 0) {
                        v = true;
                    }
                    shaderCompilerAvailable = new Boolean(v);
                } else if (gl.isGL2() || gl.isGL2ES2()) {
                    shaderCompilerAvailable = new Boolean(true);
                } else {
                    throw new GLException("Invalid OpenGL profile");
                }
            }
            return shaderCompilerAvailable.booleanValue();
        }

        public void shaderSource(GL _gl, int shader, java.lang.String[] source) {
            GL2ES2 gl = _gl.getGL2ES2();
            if (!isShaderCompilerAvailable(_gl)) {
                throw new GLException("No compiler is available");
            }
            int count = (null != source) ? source.length : 0;
            if (count == 0) {
                throw new GLException("No sources specified");
            }
            int[] lengths = new int[count];
            for (int i = 0; i < count; i++) {
                lengths[i] = source[i].length();
            }
            gl.glShaderSource(shader, count, source, lengths, 0);
        }

        public void shaderSource(GL _gl, IntBuffer shaders, java.lang.String[][] sources) {
            int sourceNum = (null != sources) ? sources.length : 0;
            int shaderNum = (null != shaders) ? shaders.remaining() : 0;
            if (shaderNum <= 0 || sourceNum <= 0 || shaderNum != sourceNum) {
                throw new GLException("Invalid number of shaders and/or sources: shaders=" + shaderNum + ", sources=" + sourceNum);
            }
            for (int i = 0; i < sourceNum; i++) {
                shaderSource(_gl, shaders.get(shaders.position() + i), sources[i]);
            }
        }

        public void shaderBinary(GL _gl, IntBuffer shaders, int binFormat, java.nio.Buffer bin) {
            GL2ES2 gl = _gl.getGL2ES2();
            if (getShaderBinaryFormats(gl).size() <= 0) {
                throw new GLException("No binary formats are supported");
            }
            int shaderNum = shaders.remaining();
            if (shaderNum <= 0) {
                throw new GLException("No shaders specified");
            }
            if (null == bin) {
                throw new GLException("Null shader binary");
            }
            int binLength = bin.remaining();
            if (0 >= binLength) {
                throw new GLException("Empty shader binary (remaining == 0)");
            }
            gl.glShaderBinary(shaderNum, shaders, binFormat, bin, binLength);
        }

        public void compileShader(GL _gl, IntBuffer shaders) {
            GL2ES2 gl = _gl.getGL2ES2();
            for (int i = shaders.position(); i < shaders.limit(); i++) {
                gl.glCompileShader(shaders.get(i));
            }
        }

        public void attachShader(GL _gl, int program, IntBuffer shaders) {
            GL2ES2 gl = _gl.getGL2ES2();
            for (int i = shaders.position(); i < shaders.limit(); i++) {
                gl.glAttachShader(program, shaders.get(i));
            }
        }

        public void detachShader(GL _gl, int program, IntBuffer shaders) {
            GL2ES2 gl = _gl.getGL2ES2();
            for (int i = shaders.position(); i < shaders.limit(); i++) {
                gl.glDetachShader(program, shaders.get(i));
            }
        }

        public void deleteShader(GL _gl, IntBuffer shaders) {
            GL2ES2 gl = _gl.getGL2ES2();
            for (int i = shaders.position(); i < shaders.limit(); i++) {
                gl.glDeleteShader(shaders.get(i));
            }
        }
    }

    public static String getShaderInfoLog(GL gl, int shaderObj) {
        return getImpl(gl).getShaderInfoLog(gl, shaderObj);
    }

    public static String getProgramInfoLog(GL gl, int programObj) {
        return getImpl(gl).getProgramInfoLog(gl, programObj);
    }

    public static boolean isShaderStatusValid(GL gl, int shaderObj, int name) {
        return getImpl(gl).isShaderStatusValid(gl, shaderObj, name);
    }

    public static boolean isShaderStatusValid(GL gl, int shaderObj, int name, PrintStream verboseOut) {
        return getImpl(gl).isShaderStatusValid(gl, shaderObj, name, verboseOut);
    }

    public static boolean isShaderStatusValid(GL gl, IntBuffer shaders, int name) {
        return getImpl(gl).isShaderStatusValid(gl, shaders, name);
    }

    public static boolean isShaderStatusValid(GL gl, IntBuffer shaders, int name, PrintStream verboseOut) {
        return getImpl(gl).isShaderStatusValid(gl, shaders, name, verboseOut);
    }

    public static boolean isProgramStatusValid(GL gl, int programObj, int name) {
        return getImpl(gl).isProgramStatusValid(gl, programObj, name);
    }

    public static boolean isProgramValid(GL gl, int programObj) {
        return getImpl(gl).isProgramValid(gl, programObj);
    }

    public static boolean isProgramValid(GL gl, int programObj, PrintStream verboseOut) {
        return getImpl(gl).isProgramValid(gl, programObj, verboseOut);
    }

    public static void createShader(GL gl, int type, IntBuffer shaders) {
        getImpl(gl).createShader(gl, type, shaders);
    }

    public static Set getShaderBinaryFormats(GL gl) {
        return getImpl(gl).getShaderBinaryFormats(gl);
    }

    public static boolean isShaderCompilerAvailable(GL gl) {
        return getImpl(gl).isShaderCompilerAvailable(gl);
    }

    public static void shaderSource(GL gl, int shader, java.lang.String[] source) {
        getImpl(gl).shaderSource(gl, shader, source);
    }

    public static void shaderSource(GL gl, IntBuffer shaders, java.lang.String[][] sources) {
        getImpl(gl).shaderSource(gl, shaders, sources);
    }

    public static void shaderBinary(GL gl, IntBuffer shaders, int binFormat, java.nio.Buffer bin) {
        getImpl(gl).shaderBinary(gl, shaders, binFormat, bin);
    }

    public static void compileShader(GL gl, IntBuffer shaders) {
        getImpl(gl).compileShader(gl, shaders);
    }

    public static void attachShader(GL gl, int program, IntBuffer shaders) {
        getImpl(gl).attachShader(gl, program, shaders);
    }

    public static void detachShader(GL gl, int program, IntBuffer shaders) {
        getImpl(gl).detachShader(gl, program, shaders);
    }

    public static void deleteShader(GL gl, IntBuffer shaders) {
        getImpl(gl).deleteShader(gl, shaders);
    }

    private static Impl getImpl(GL _gl) {
        GL2ES2 gl = _gl.getGL2ES2();
        GLContext context = gl.getContext();
        Impl impl = (Impl) context.getAttachedObject(ShaderUtil.class.getName());
        if (impl == null) {
            impl = new GL2ES2Impl();
            context.putAttachedObject(ShaderUtil.class.getName(), impl);
        }
        return impl;
    }
}
