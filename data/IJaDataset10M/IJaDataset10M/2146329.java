package org.easyway.shader;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.easyway.utils.Utility;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.Util;

/** ALPHA */
@Deprecated
public class Shaders {

    int programma;

    public Shaders(String filename) {
        ByteBuffer vdata = getProgramCode("v" + filename);
        ByteBuffer fdata = getProgramCode("f" + filename);
        int vertexShader = ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
        int fragmentShader = ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        ARBShaderObjects.glShaderSourceARB(vertexShader, vdata);
        ARBShaderObjects.glShaderSourceARB(fragmentShader, fdata);
        ARBShaderObjects.glCompileShaderARB(vertexShader);
        ARBShaderObjects.glCompileShaderARB(fragmentShader);
        int programObject = ARBShaderObjects.glCreateProgramObjectARB();
        ARBShaderObjects.glAttachObjectARB(programObject, vertexShader);
        ARBShaderObjects.glAttachObjectARB(programObject, fragmentShader);
        ARBShaderObjects.glLinkProgramARB(programObject);
        ARBShaderObjects.glValidateProgramARB(programObject);
        printLogInfo(programObject);
        ARBShaderObjects.glUseProgramObjectARB(0);
        programma = programObject;
    }

    public void setUniform(String name, int value) {
        ByteBuffer buff = toByteString(name, true);
        int location = ARBShaderObjects.glGetUniformLocationARB(programma, buff);
        ARBShaderObjects.glUniform1iARB(location, value);
    }

    public void setUniform(String name, IntBuffer value) {
        ByteBuffer buff = toByteString(name, true);
        int location = ARBShaderObjects.glGetUniformLocationARB(programma, buff);
        ARBShaderObjects.glUniform1ARB(location, value);
    }

    public void setUniform(String name, float value) {
        ByteBuffer buff = toByteString(name, true);
        int location = ARBShaderObjects.glGetUniformLocationARB(programma, buff);
        ARBShaderObjects.glUniform1fARB(location, value);
    }

    public void use() {
        ARBShaderObjects.glUseProgramObjectARB(programma);
    }

    public static void reset() {
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    private static void printLogInfo(int obj) {
        IntBuffer iVal = BufferUtils.createIntBuffer(1);
        ARBShaderObjects.glGetObjectParameterARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);
        int length = iVal.get();
        System.out.println("Info log length:" + length);
        if (length > 0) {
            ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
            iVal.flip();
            ARBShaderObjects.glGetInfoLogARB(obj, iVal, infoLog);
            byte[] infoBytes = new byte[length];
            infoLog.get(infoBytes);
            String out = new String(infoBytes);
            System.out.println("Info log:\n" + out);
        }
        Util.checkGLError();
    }

    private static ByteBuffer getProgramCode(String filename) {
        InputStream fileInputStream;
        {
            String temp = "./../../..";
            if (filename.charAt(0) != '/') temp = temp + '/';
            fileInputStream = Utility.class.getResourceAsStream(temp + filename);
        }
        byte[] shaderCode = null;
        try {
            if (fileInputStream == null) fileInputStream = new FileInputStream(filename);
            DataInputStream dataStream = new DataInputStream(fileInputStream);
            dataStream.readFully(shaderCode = new byte[fileInputStream.available()]);
            fileInputStream.close();
            dataStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        ByteBuffer shaderPro = BufferUtils.createByteBuffer(shaderCode.length);
        shaderPro.put(shaderCode);
        shaderPro.flip();
        return shaderPro;
    }

    private static ByteBuffer toByteString(String str, boolean isNullTerminated) {
        int length = str.length();
        if (isNullTerminated) ++length;
        ByteBuffer buff = BufferUtils.createByteBuffer(length);
        buff.put(str.getBytes());
        if (isNullTerminated) buff.put((byte) 0);
        buff.flip();
        return buff;
    }
}
