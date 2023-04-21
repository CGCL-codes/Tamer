package de.grogra.imp3d.glsl.utility;

import javax.media.opengl.GL;
import de.grogra.imp3d.glsl.GLSLDisplay;
import de.grogra.imp3d.glsl.OpenGLState;
import de.grogra.imp3d.glsl.material.GLSLMaterial;

/**
 * Base for all complete shaders like
 * 
 * @author Konni Hartmann
 */
public abstract class GLSLShader implements GLSLOpenGLObject {

    public GLSLShader(OpenGLState glState) {
        super();
        if (glState != null) glState.staticObjects.add(this);
    }

    /**
	 * Standard Vertex Shader used by many other Shaders
	 */
    static final String vStdSrc[] = { "#version 110\n", "varying vec3 normal;", "varying vec4 pos;", "varying vec3 n_pos;", "varying vec3 g_pos;", "varying vec4 u_pos;", "varying vec2 uv;", "varying vec2 TexUnit2;", "void main() {", " uv = gl_MultiTexCoord0.st;", " TexUnit2 = gl_MultiTexCoord1.st;", " gl_Position = ftransform();", " u_pos = gl_Position;", " pos = gl_ModelViewMatrix * gl_Vertex;", " n_pos = gl_Vertex.xyz;", " g_pos = (gl_TextureMatrix[3]*gl_Vertex).xyz;", " normal = normalize(gl_NormalMatrix * gl_Normal);", "}" };

    /**
	 * Output InfoLog to get a clue on what went wrong
	 * 
	 * @param gl
	 * @param shader
	 */
    private static void printShaderInfoLog(GL gl, int shader) {
        int infologLength[] = new int[1];
        int charsWritten[] = new int[1];
        byte infoLog[];
        gl.glGetShaderiv(shader, GL.GL_INFO_LOG_LENGTH, infologLength, 0);
        if (infologLength[0] > 0) {
            GLSLDisplay.printDebugInfoN("Shader-Log for " + shader);
            infoLog = new byte[infologLength[0]];
            gl.glGetShaderInfoLog(shader, infologLength[0], charsWritten, 0, infoLog, 0);
            String log = new String(infoLog);
            GLSLDisplay.printDebugInfoN(log);
        }
    }

    /**
	 * Same as <code>printShaderInfoLog</code> only for Programs.
	 * 
	 * @param gl
	 * @param program
	 */
    protected static void printProgramInfoLog(GL gl, int program) {
        int infologLength[] = new int[1];
        int charsWritten[] = new int[1];
        byte infoLog[];
        gl.glGetProgramiv(program, GL.GL_INFO_LOG_LENGTH, infologLength, 0);
        if (infologLength[0] > 0) {
            GLSLDisplay.printDebugInfoN("Program-Log for " + program);
            infoLog = new byte[infologLength[0]];
            gl.glGetProgramInfoLog(program, infologLength[0], charsWritten, 0, infoLog, 0);
            String log = new String(infoLog);
            GLSLDisplay.printDebugInfoN(log);
        }
    }

    static final int PROGRAM = 0;

    static final int VERTEX_SHADER = 1;

    static final int FRAGMENT_SHADER = 2;

    /**
	 * Compile and link Shaders to a Program.
	 * 
	 * @param gl
	 * @param fsrc
	 * @param vsrc
	 * @return Returns Index of the Program and its Shaders
	 */
    private static int[] compileShader(GL gl, String[] fsrc, String[] vsrc) {
        int[] result = new int[3];
        int status[] = new int[1];
        result[VERTEX_SHADER] = gl.glCreateShader(GL.GL_VERTEX_SHADER);
        result[FRAGMENT_SHADER] = gl.glCreateShader(GL.GL_FRAGMENT_SHADER);
        gl.glShaderSource(result[VERTEX_SHADER], vsrc.length, vsrc, (int[]) null, 0);
        gl.glCompileShader(result[VERTEX_SHADER]);
        printShaderInfoLog(gl, result[VERTEX_SHADER]);
        gl.glShaderSource(result[FRAGMENT_SHADER], fsrc.length, fsrc, (int[]) null, 0);
        gl.glCompileShader(result[FRAGMENT_SHADER]);
        printShaderInfoLog(gl, result[FRAGMENT_SHADER]);
        if (GLSLDisplay.DEBUG) {
            GLSLDisplay.printDebugInfoN("--- Compiled fragment program");
            for (int i = 0; i < fsrc.length; i++) System.out.println(fsrc[i]);
            GLSLDisplay.printDebugInfoN("---");
        }
        result[PROGRAM] = gl.glCreateProgram();
        gl.glAttachShader(result[PROGRAM], result[VERTEX_SHADER]);
        gl.glAttachShader(result[PROGRAM], result[FRAGMENT_SHADER]);
        gl.glLinkProgram(result[PROGRAM]);
        return result;
    }

    /**
	 * Given by OpenGL to access ShaderProgram
	 */
    private int[] GLSLProgramNumber = { 0, 0, 0 };

    /**
	 * Getter for Shader Programnumber
	 * 
	 * @return the OpenGL id of the associated shader-program-id
	 */
    public int getShaderProgramNumber() {
        return GLSLProgramNumber[PROGRAM];
    }

    /**
	 * Return false if Shader depends only on Uniforms
	 * 
	 * @param s
	 *            Associated Shader
	 * @return true, if Sourcecode needs to be regenerated and recompiled
	 */
    public boolean needsRecompilation(Object data) {
        return false;
    }

    /**
	 * 
	 * @return Class represented by this Shader
	 */
    public Class instanceFor() {
        return null;
    }

    /**
	 * For "hardcompiled" Shaders this method should return a new instance
	 * 
	 * @return
	 */
    public GLSLShader getInstance() {
        return this;
    }

    /**
	 * 
	 * @param sh
	 * @return
	 */
    protected String[] getVertexShader(Object data) {
        return vStdSrc;
    }

    /**
	 * 
	 * @param sh
	 * @return
	 */
    protected abstract String[] getFragmentShader(Object data);

    /**
	 * Function that loads uniforms of the represented Shader. 
	 * @param gl 
	 * @param disp GLSLDisplay that tries using this shader 
	 * @param data The GroIMP-Shader-Object represented by this shader.
	 */
    protected void setupDynamicUniforms(GL gl, GLSLDisplay disp, Object data, int shaderNo) {
    }

    /**
	 * 
	 * @param gl
	 * @param disp
	 * @param s
	 */
    protected void setupShader(GL gl, GLSLDisplay disp, Object data) {
    }

    /**
	 * Only public method. Use this to activate (and compile) this Shader for
	 * use in rendering.
	 * 
	 * @param gl
	 * @param disp
	 * @param sh
	 * @param s
	 */
    public void activateShader(OpenGLState glState, GLSLDisplay disp, Object data) {
        GL gl = glState.getGL();
        if (getShaderProgramNumber() == 0 || needsRecompilation(data)) {
            if (getShaderProgramNumber() != 0) deleteShader(gl, false);
            GLSLProgramNumber = compileShader(gl, getFragmentShader(data), getVertexShader(data));
            glState.setActiveProgram(getShaderProgramNumber());
            setupShader(gl, disp, data);
        }
        if (!GLSLDisplay.DEBUG) glState.setActiveProgram(getShaderProgramNumber()); else {
            glState.setActiveProgram(getShaderProgramNumber());
        }
        setupDynamicUniforms(gl, disp, data, getShaderProgramNumber());
        if (getShaderProgramNumber() == 0) GLSLDisplay.printDebugInfoN("! Error activating Shader: " + this);
        if (GLSLDisplay.DEBUG) {
            gl.glValidateProgram(getShaderProgramNumber());
            int status[] = new int[1];
            gl.glGetProgramiv(getShaderProgramNumber(), GL.GL_VALIDATE_STATUS, status, 0);
            if (status[0] == GL.GL_FALSE) printProgramInfoLog(gl, getShaderProgramNumber());
        }
    }

    /**
	 * Remove OpenGL side of this shader
	 * 
	 * @param gl
	 */
    public void deleteShader(GL gl, boolean javaonly) {
        if (GLSLProgramNumber[PROGRAM] == 0) return;
        if (!javaonly) {
            gl.glDetachShader(GLSLProgramNumber[PROGRAM], GLSLProgramNumber[VERTEX_SHADER]);
            gl.glDetachShader(GLSLProgramNumber[PROGRAM], GLSLProgramNumber[FRAGMENT_SHADER]);
            gl.glDeleteShader(GLSLProgramNumber[VERTEX_SHADER]);
        }
        GLSLProgramNumber[VERTEX_SHADER] = 0;
        if (!javaonly) gl.glDeleteShader(GLSLProgramNumber[FRAGMENT_SHADER]);
        GLSLProgramNumber[FRAGMENT_SHADER] = 0;
        if (!javaonly) gl.glDeleteProgram(GLSLProgramNumber[PROGRAM]);
        GLSLProgramNumber[PROGRAM] = 0;
        gl.glGetError();
    }

    public void cleanup(OpenGLState glState, boolean javaonly) {
        deleteShader(glState.getGL(), javaonly);
    }
}
