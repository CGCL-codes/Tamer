package org.lwjgl.opengl;

import java.nio.ShortBuffer;

public class NVHalfFloat {

    public static final int GL_HALF_FLOAT_NV = 0x140B;

    public static native void glVertex2hNV(short x, short y);

    public static native void glVertex3hNV(short x, short y, short z);

    public static native void glVertex4hNV(short x, short y, short z, short w);

    public static native void glNormal3hNV(short nx, short ny, short nz);

    public static native void glColor3hNV(short red, short green, short blue);

    public static native void glColor4hNV(short red, short green, short blue, short alpha);

    public static native void glTexCoord1hNV(short s);

    public static native void glTexCoord2hNV(short s, short t);

    public static native void glTexCoord3hNV(short s, short t, short r);

    public static native void glTexCoord4hNV(short s, short t, short r, short q);

    public static native void glMultiTexCoord1hNV(int target, short s);

    public static native void glMultiTexCoord2hNV(int target, short s, short t);

    public static native void glMultiTexCoord3hNV(int target, short s, short t, short r);

    public static native void glMultiTexCoord4hNV(int target, short s, short t, short r, short q);

    public static native void glFogCoordhNV(short fog);

    public static native void glSecondaryColor3hNV(short red, short green, short blue);

    public static native void glVertexWeighthNV(short weight);

    public static native void glVertexAttrib1hNV(int index, short x);

    public static native void glVertexAttrib2hNV(int index, short x, short y);

    public static native void glVertexAttrib3hNV(int index, short x, short y, short z);

    public static native void glVertexAttrib4hNV(int index, short x, short y, short z, short w);

    public static void glVertexAttribs1hNV(int index, ShortBuffer attribs) {
        nglVertexAttribs1hvNV(index, attribs.remaining(), attribs, attribs.position());
    }

    private static native void nglVertexAttribs1hvNV(int index, int n, ShortBuffer attribs, int attribsOffset);

    public static void glVertexAttribs2hNV(int index, ShortBuffer attribs) {
        nglVertexAttribs2hvNV(index, attribs.remaining() >> 1, attribs, attribs.position());
    }

    private static native void nglVertexAttribs2hvNV(int index, int n, ShortBuffer attribs, int attribsOffset);

    public static void glVertexAttribs3hNV(int index, ShortBuffer attribs) {
        nglVertexAttribs3hvNV(index, attribs.remaining() / 3, attribs, attribs.position());
    }

    private static native void nglVertexAttribs3hvNV(int index, int n, ShortBuffer attribs, int attribsOffset);

    public static void glVertexAttribs4hNV(int index, ShortBuffer attribs) {
        nglVertexAttribs4hvNV(index, attribs.remaining() >> 2, attribs, attribs.position());
    }

    private static native void nglVertexAttribs4hvNV(int index, int n, ShortBuffer attribs, int attribsOffset);
}
