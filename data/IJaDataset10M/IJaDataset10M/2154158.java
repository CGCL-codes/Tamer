package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

class ARBProgram {

    public static final int GL_PROGRAM_FORMAT_ASCII_ARB = 0x8875;

    public static final int GL_PROGRAM_LENGTH_ARB = 0x8627;

    public static final int GL_PROGRAM_FORMAT_ARB = 0x8876;

    public static final int GL_PROGRAM_BINDING_ARB = 0x8677;

    public static final int GL_PROGRAM_INSTRUCTIONS_ARB = 0x88A0;

    public static final int GL_MAX_PROGRAM_INSTRUCTIONS_ARB = 0x88A1;

    public static final int GL_PROGRAM_NATIVE_INSTRUCTIONS_ARB = 0x88A2;

    public static final int GL_MAX_PROGRAM_NATIVE_INSTRUCTIONS_ARB = 0x88A3;

    public static final int GL_PROGRAM_TEMPORARIES_ARB = 0x88A4;

    public static final int GL_MAX_PROGRAM_TEMPORARIES_ARB = 0x88A5;

    public static final int GL_PROGRAM_NATIVE_TEMPORARIES_ARB = 0x88A6;

    public static final int GL_MAX_PROGRAM_NATIVE_TEMPORARIES_ARB = 0x88A7;

    public static final int GL_PROGRAM_PARAMETERS_ARB = 0x88A8;

    public static final int GL_MAX_PROGRAM_PARAMETERS_ARB = 0x88A9;

    public static final int GL_PROGRAM_NATIVE_PARAMETERS_ARB = 0x88AA;

    public static final int GL_MAX_PROGRAM_NATIVE_PARAMETERS_ARB = 0x88AB;

    public static final int GL_PROGRAM_ATTRIBS_ARB = 0x88AC;

    public static final int GL_MAX_PROGRAM_ATTRIBS_ARB = 0x88AD;

    public static final int GL_PROGRAM_NATIVE_ATTRIBS_ARB = 0x88AE;

    public static final int GL_MAX_PROGRAM_NATIVE_ATTRIBS_ARB = 0x88AF;

    public static final int GL_MAX_PROGRAM_LOCAL_PARAMETERS_ARB = 0x88B4;

    public static final int GL_MAX_PROGRAM_ENV_PARAMETERS_ARB = 0x88B5;

    public static final int GL_PROGRAM_UNDER_NATIVE_LIMITS_ARB = 0x88B6;

    public static final int GL_PROGRAM_STRING_ARB = 0x8628;

    public static final int GL_PROGRAM_ERROR_POSITION_ARB = 0x864B;

    public static final int GL_CURRENT_MATRIX_ARB = 0x8641;

    public static final int GL_TRANSPOSE_CURRENT_MATRIX_ARB = 0x88B7;

    public static final int GL_CURRENT_MATRIX_STACK_DEPTH_ARB = 0x8640;

    public static final int GL_MAX_PROGRAM_MATRICES_ARB = 0x862F;

    public static final int GL_MAX_PROGRAM_MATRIX_STACK_DEPTH_ARB = 0x862E;

    public static final int GL_PROGRAM_ERROR_STRING_ARB = 0x8874;

    public static final int GL_MATRIX0_ARB = 0x88C0;

    public static final int GL_MATRIX1_ARB = 0x88C1;

    public static final int GL_MATRIX2_ARB = 0x88C2;

    public static final int GL_MATRIX3_ARB = 0x88C3;

    public static final int GL_MATRIX4_ARB = 0x88C4;

    public static final int GL_MATRIX5_ARB = 0x88C5;

    public static final int GL_MATRIX6_ARB = 0x88C6;

    public static final int GL_MATRIX7_ARB = 0x88C7;

    public static final int GL_MATRIX8_ARB = 0x88C8;

    public static final int GL_MATRIX9_ARB = 0x88C9;

    public static final int GL_MATRIX10_ARB = 0x88CA;

    public static final int GL_MATRIX11_ARB = 0x88CB;

    public static final int GL_MATRIX12_ARB = 0x88CC;

    public static final int GL_MATRIX13_ARB = 0x88CD;

    public static final int GL_MATRIX14_ARB = 0x88CE;

    public static final int GL_MATRIX15_ARB = 0x88CF;

    public static final int GL_MATRIX16_ARB = 0x88D0;

    public static final int GL_MATRIX17_ARB = 0x88D1;

    public static final int GL_MATRIX18_ARB = 0x88D2;

    public static final int GL_MATRIX19_ARB = 0x88D3;

    public static final int GL_MATRIX20_ARB = 0x88D4;

    public static final int GL_MATRIX21_ARB = 0x88D5;

    public static final int GL_MATRIX22_ARB = 0x88D6;

    public static final int GL_MATRIX23_ARB = 0x88D7;

    public static final int GL_MATRIX24_ARB = 0x88D8;

    public static final int GL_MATRIX25_ARB = 0x88D9;

    public static final int GL_MATRIX26_ARB = 0x88DA;

    public static final int GL_MATRIX27_ARB = 0x88DB;

    public static final int GL_MATRIX28_ARB = 0x88DC;

    public static final int GL_MATRIX29_ARB = 0x88DD;

    public static final int GL_MATRIX30_ARB = 0x88DE;

    public static final int GL_MATRIX31_ARB = 0x88DF;

    public static void glProgramStringARB(int target, int format, ByteBuffer string) {
        nglProgramStringARB(target, format, string.remaining(), string, string.position());
    }

    private static native void nglProgramStringARB(int target, int format, int length, Buffer string, int stringOffset);

    public static native void glBindProgramARB(int target, int program);

    public static void glDeleteProgramsARB(IntBuffer programs) {
        nglDeleteProgramsARB(programs.remaining(), programs, programs.position());
    }

    private static native void nglDeleteProgramsARB(int n, IntBuffer programs, int programsOffset);

    public static void glGenProgramsARB(IntBuffer programs) {
        nglGenProgramsARB(programs.remaining(), programs, programs.position());
    }

    private static native void nglGenProgramsARB(int n, IntBuffer programs, int programsOffset);

    public static native void glProgramEnvParameter4fARB(int target, int index, float x, float y, float z, float w);

    private static void checkProgramEnv(int index, Buffer buf) {
        if (index < 0) {
            throw new IllegalArgumentException("<index> must be greater than or equal to 0.");
        }
        if (buf.remaining() < 4) {
            throw new BufferUnderflowException();
        }
    }

    public static void glProgramEnvParameterARB(int target, int index, FloatBuffer params) {
        checkProgramEnv(index, params);
        nglProgramEnvParameter4fvARB(target, index, params, params.position());
    }

    private static native void nglProgramEnvParameter4fvARB(int target, int index, FloatBuffer params, int paramsOffset);

    public static native void glProgramLocalParameter4fARB(int target, int index, float x, float y, float z, float w);

    public static void glProgramLocalParameterARB(int target, int index, FloatBuffer params) {
        checkProgramEnv(index, params);
        nglProgramLocalParameter4fvARB(target, index, params, params.position());
    }

    private static native void nglProgramLocalParameter4fvARB(int target, int index, FloatBuffer params, int paramsOffset);

    public static void glGetProgramEnvParameterARB(int target, int index, FloatBuffer params) {
        checkProgramEnv(index, params);
        nglGetProgramEnvParameterfvARB(target, index, params, params.position());
    }

    private static native void nglGetProgramEnvParameterfvARB(int target, int index, FloatBuffer params, int paramsOffset);

    public static void glGetProgramLocalParameterARB(int target, int index, FloatBuffer params) {
        checkProgramEnv(index, params);
        nglGetProgramLocalParameterfvARB(target, index, params, params.position());
    }

    private static native void nglGetProgramLocalParameterfvARB(int target, int index, FloatBuffer params, int paramsOffset);

    public static void glGetProgramARB(int target, int parameterName, IntBuffer params) {
        nglGetProgramivARB(target, parameterName, params, params.position());
    }

    private static native void nglGetProgramivARB(int target, int parameterName, IntBuffer params, int paramsOffset);

    public static void glGetProgramStringARB(int target, int parameterName, ByteBuffer paramString) {
        nglGetProgramStringARB(target, parameterName, paramString, paramString.position());
    }

    private static native void nglGetProgramStringARB(int target, int parameterName, Buffer paramString, int paramStringOffset);

    public static native boolean glIsProgramARB(int program);
}
