package org.gljava.opengl.x.swig;

import java.nio.*;

public class Glew implements GlewConstants {

    public static long SWIG_glew_init() {
        return GlewJNI.SWIG_glew_init();
    }

    public static ShortBuffer glewGetErrorString(long error) {
        return GlewJNI.glewGetErrorString(error);
    }

    public static void glClearIndex(float c) {
        GlewJNI.glClearIndex(c);
    }

    public static void glClearColor(float red, float green, float blue, float alpha) {
        GlewJNI.glClearColor(red, green, blue, alpha);
    }

    public static void glClear(long mask) {
        GlewJNI.glClear(mask);
    }

    public static void glIndexMask(long mask) {
        GlewJNI.glIndexMask(mask);
    }

    public static void glColorMask(short red, short green, short blue, short alpha) {
        GlewJNI.glColorMask(red, green, blue, alpha);
    }

    public static void glAlphaFunc(long func, float ref) {
        GlewJNI.glAlphaFunc(func, ref);
    }

    public static void glBlendFunc(long sfactor, long dfactor) {
        GlewJNI.glBlendFunc(sfactor, dfactor);
    }

    public static void glLogicOp(long opcode) {
        GlewJNI.glLogicOp(opcode);
    }

    public static void glCullFace(long mode) {
        GlewJNI.glCullFace(mode);
    }

    public static void glFrontFace(long mode) {
        GlewJNI.glFrontFace(mode);
    }

    public static void glPointSize(float size) {
        GlewJNI.glPointSize(size);
    }

    public static void glLineWidth(float width) {
        GlewJNI.glLineWidth(width);
    }

    public static void glLineStipple(int factor, int pattern) {
        GlewJNI.glLineStipple(factor, pattern);
    }

    public static void glPolygonMode(long face, long mode) {
        GlewJNI.glPolygonMode(face, mode);
    }

    public static void glPolygonOffset(float factor, float units) {
        GlewJNI.glPolygonOffset(factor, units);
    }

    public static void glPolygonStipple(ShortBuffer mask) {
        GlewJNI.glPolygonStipple(mask);
    }

    public static void glGetPolygonStipple(ShortBuffer mask) {
        GlewJNI.glGetPolygonStipple(mask);
    }

    public static void glEdgeFlag(short flag) {
        GlewJNI.glEdgeFlag(flag);
    }

    public static void glEdgeFlagv(ShortBuffer flag) {
        GlewJNI.glEdgeFlagv(flag);
    }

    public static void glScissor(int x, int y, int width, int height) {
        GlewJNI.glScissor(x, y, width, height);
    }

    public static void glClipPlane(long plane, DoubleBuffer equation) {
        GlewJNI.glClipPlane(plane, equation);
    }

    public static void glGetClipPlane(long plane, DoubleBuffer equation) {
        GlewJNI.glGetClipPlane(plane, equation);
    }

    public static void glDrawBuffer(long mode) {
        GlewJNI.glDrawBuffer(mode);
    }

    public static void glReadBuffer(long mode) {
        GlewJNI.glReadBuffer(mode);
    }

    public static void glEnable(long cap) {
        GlewJNI.glEnable(cap);
    }

    public static void glDisable(long cap) {
        GlewJNI.glDisable(cap);
    }

    public static short glIsEnabled(long cap) {
        return GlewJNI.glIsEnabled(cap);
    }

    public static void glEnableClientState(long cap) {
        GlewJNI.glEnableClientState(cap);
    }

    public static void glDisableClientState(long cap) {
        GlewJNI.glDisableClientState(cap);
    }

    public static void glGetBooleanv(long pname, ShortBuffer params) {
        GlewJNI.glGetBooleanv(pname, params);
    }

    public static void glGetDoublev(long pname, DoubleBuffer params) {
        GlewJNI.glGetDoublev(pname, params);
    }

    public static void glGetFloatv(long pname, FloatBuffer params) {
        GlewJNI.glGetFloatv(pname, params);
    }

    public static void glGetIntegerv(long pname, IntBuffer params) {
        GlewJNI.glGetIntegerv(pname, params);
    }

    public static void glPushAttrib(long mask) {
        GlewJNI.glPushAttrib(mask);
    }

    public static void glPopAttrib() {
        GlewJNI.glPopAttrib();
    }

    public static void glPushClientAttrib(long mask) {
        GlewJNI.glPushClientAttrib(mask);
    }

    public static void glPopClientAttrib() {
        GlewJNI.glPopClientAttrib();
    }

    public static int glRenderMode(long mode) {
        return GlewJNI.glRenderMode(mode);
    }

    public static long glGetError() {
        return GlewJNI.glGetError();
    }

    public static ShortBuffer glGetString(long name) {
        return GlewJNI.glGetString(name);
    }

    public static void glFinish() {
        GlewJNI.glFinish();
    }

    public static void glFlush() {
        GlewJNI.glFlush();
    }

    public static void glHint(long target, long mode) {
        GlewJNI.glHint(target, mode);
    }

    public static void glClearDepth(double depth) {
        GlewJNI.glClearDepth(depth);
    }

    public static void glDepthFunc(long func) {
        GlewJNI.glDepthFunc(func);
    }

    public static void glDepthMask(short flag) {
        GlewJNI.glDepthMask(flag);
    }

    public static void glDepthRange(double near_val, double far_val) {
        GlewJNI.glDepthRange(near_val, far_val);
    }

    public static void glClearAccum(float red, float green, float blue, float alpha) {
        GlewJNI.glClearAccum(red, green, blue, alpha);
    }

    public static void glAccum(long op, float value) {
        GlewJNI.glAccum(op, value);
    }

    public static void glMatrixMode(long mode) {
        GlewJNI.glMatrixMode(mode);
    }

    public static void glOrtho(double left, double right, double bottom, double top, double near_val, double far_val) {
        GlewJNI.glOrtho(left, right, bottom, top, near_val, far_val);
    }

    public static void glFrustum(double left, double right, double bottom, double top, double near_val, double far_val) {
        GlewJNI.glFrustum(left, right, bottom, top, near_val, far_val);
    }

    public static void glViewport(int x, int y, int width, int height) {
        GlewJNI.glViewport(x, y, width, height);
    }

    public static void glPushMatrix() {
        GlewJNI.glPushMatrix();
    }

    public static void glPopMatrix() {
        GlewJNI.glPopMatrix();
    }

    public static void glLoadIdentity() {
        GlewJNI.glLoadIdentity();
    }

    public static void glLoadMatrixd(DoubleBuffer m) {
        GlewJNI.glLoadMatrixd(m);
    }

    public static void glLoadMatrixf(FloatBuffer m) {
        GlewJNI.glLoadMatrixf(m);
    }

    public static void glMultMatrixd(DoubleBuffer m) {
        GlewJNI.glMultMatrixd(m);
    }

    public static void glMultMatrixf(FloatBuffer m) {
        GlewJNI.glMultMatrixf(m);
    }

    public static void glRotated(double angle, double x, double y, double z) {
        GlewJNI.glRotated(angle, x, y, z);
    }

    public static void glRotatef(float angle, float x, float y, float z) {
        GlewJNI.glRotatef(angle, x, y, z);
    }

    public static void glScaled(double x, double y, double z) {
        GlewJNI.glScaled(x, y, z);
    }

    public static void glScalef(float x, float y, float z) {
        GlewJNI.glScalef(x, y, z);
    }

    public static void glTranslated(double x, double y, double z) {
        GlewJNI.glTranslated(x, y, z);
    }

    public static void glTranslatef(float x, float y, float z) {
        GlewJNI.glTranslatef(x, y, z);
    }

    public static short glIsList(long list) {
        return GlewJNI.glIsList(list);
    }

    public static void glDeleteLists(long list, int range) {
        GlewJNI.glDeleteLists(list, range);
    }

    public static long glGenLists(int range) {
        return GlewJNI.glGenLists(range);
    }

    public static void glNewList(long list, long mode) {
        GlewJNI.glNewList(list, mode);
    }

    public static void glEndList() {
        GlewJNI.glEndList();
    }

    public static void glCallList(long list) {
        GlewJNI.glCallList(list);
    }

    public static void glCallLists(int n, long type, Buffer lists) {
        GlewJNI.glCallLists(n, type, lists);
    }

    public static void glListBase(long base) {
        GlewJNI.glListBase(base);
    }

    public static void glBegin(long mode) {
        GlewJNI.glBegin(mode);
    }

    public static void glEnd() {
        GlewJNI.glEnd();
    }

    public static void glVertex2d(double x, double y) {
        GlewJNI.glVertex2d(x, y);
    }

    public static void glVertex2f(float x, float y) {
        GlewJNI.glVertex2f(x, y);
    }

    public static void glVertex2i(int x, int y) {
        GlewJNI.glVertex2i(x, y);
    }

    public static void glVertex2s(short x, short y) {
        GlewJNI.glVertex2s(x, y);
    }

    public static void glVertex3d(double x, double y, double z) {
        GlewJNI.glVertex3d(x, y, z);
    }

    public static void glVertex3f(float x, float y, float z) {
        GlewJNI.glVertex3f(x, y, z);
    }

    public static void glVertex3i(int x, int y, int z) {
        GlewJNI.glVertex3i(x, y, z);
    }

    public static void glVertex3s(short x, short y, short z) {
        GlewJNI.glVertex3s(x, y, z);
    }

    public static void glVertex4d(double x, double y, double z, double w) {
        GlewJNI.glVertex4d(x, y, z, w);
    }

    public static void glVertex4f(float x, float y, float z, float w) {
        GlewJNI.glVertex4f(x, y, z, w);
    }

    public static void glVertex4i(int x, int y, int z, int w) {
        GlewJNI.glVertex4i(x, y, z, w);
    }

    public static void glVertex4s(short x, short y, short z, short w) {
        GlewJNI.glVertex4s(x, y, z, w);
    }

    public static void glVertex2dv(DoubleBuffer v) {
        GlewJNI.glVertex2dv(v);
    }

    public static void glVertex2fv(FloatBuffer v) {
        GlewJNI.glVertex2fv(v);
    }

    public static void glVertex2iv(IntBuffer v) {
        GlewJNI.glVertex2iv(v);
    }

    public static void glVertex2sv(ShortBuffer v) {
        GlewJNI.glVertex2sv(v);
    }

    public static void glVertex3dv(DoubleBuffer v) {
        GlewJNI.glVertex3dv(v);
    }

    public static void glVertex3fv(FloatBuffer v) {
        GlewJNI.glVertex3fv(v);
    }

    public static void glVertex3iv(IntBuffer v) {
        GlewJNI.glVertex3iv(v);
    }

    public static void glVertex3sv(ShortBuffer v) {
        GlewJNI.glVertex3sv(v);
    }

    public static void glVertex4dv(DoubleBuffer v) {
        GlewJNI.glVertex4dv(v);
    }

    public static void glVertex4fv(FloatBuffer v) {
        GlewJNI.glVertex4fv(v);
    }

    public static void glVertex4iv(IntBuffer v) {
        GlewJNI.glVertex4iv(v);
    }

    public static void glVertex4sv(ShortBuffer v) {
        GlewJNI.glVertex4sv(v);
    }

    public static void glNormal3b(byte nx, byte ny, byte nz) {
        GlewJNI.glNormal3b(nx, ny, nz);
    }

    public static void glNormal3d(double nx, double ny, double nz) {
        GlewJNI.glNormal3d(nx, ny, nz);
    }

    public static void glNormal3f(float nx, float ny, float nz) {
        GlewJNI.glNormal3f(nx, ny, nz);
    }

    public static void glNormal3i(int nx, int ny, int nz) {
        GlewJNI.glNormal3i(nx, ny, nz);
    }

    public static void glNormal3s(short nx, short ny, short nz) {
        GlewJNI.glNormal3s(nx, ny, nz);
    }

    public static void glNormal3bv(ByteBuffer v) {
        GlewJNI.glNormal3bv(v);
    }

    public static void glNormal3dv(DoubleBuffer v) {
        GlewJNI.glNormal3dv(v);
    }

    public static void glNormal3fv(FloatBuffer v) {
        GlewJNI.glNormal3fv(v);
    }

    public static void glNormal3iv(IntBuffer v) {
        GlewJNI.glNormal3iv(v);
    }

    public static void glNormal3sv(ShortBuffer v) {
        GlewJNI.glNormal3sv(v);
    }

    public static void glIndexd(double c) {
        GlewJNI.glIndexd(c);
    }

    public static void glIndexf(float c) {
        GlewJNI.glIndexf(c);
    }

    public static void glIndexi(int c) {
        GlewJNI.glIndexi(c);
    }

    public static void glIndexs(short c) {
        GlewJNI.glIndexs(c);
    }

    public static void glIndexub(short c) {
        GlewJNI.glIndexub(c);
    }

    public static void glIndexdv(DoubleBuffer c) {
        GlewJNI.glIndexdv(c);
    }

    public static void glIndexfv(FloatBuffer c) {
        GlewJNI.glIndexfv(c);
    }

    public static void glIndexiv(IntBuffer c) {
        GlewJNI.glIndexiv(c);
    }

    public static void glIndexsv(ShortBuffer c) {
        GlewJNI.glIndexsv(c);
    }

    public static void glIndexubv(ShortBuffer c) {
        GlewJNI.glIndexubv(c);
    }

    public static void glColor3b(byte red, byte green, byte blue) {
        GlewJNI.glColor3b(red, green, blue);
    }

    public static void glColor3d(double red, double green, double blue) {
        GlewJNI.glColor3d(red, green, blue);
    }

    public static void glColor3f(float red, float green, float blue) {
        GlewJNI.glColor3f(red, green, blue);
    }

    public static void glColor3i(int red, int green, int blue) {
        GlewJNI.glColor3i(red, green, blue);
    }

    public static void glColor3s(short red, short green, short blue) {
        GlewJNI.glColor3s(red, green, blue);
    }

    public static void glColor3ub(short red, short green, short blue) {
        GlewJNI.glColor3ub(red, green, blue);
    }

    public static void glColor3ui(long red, long green, long blue) {
        GlewJNI.glColor3ui(red, green, blue);
    }

    public static void glColor3us(int red, int green, int blue) {
        GlewJNI.glColor3us(red, green, blue);
    }

    public static void glColor4b(byte red, byte green, byte blue, byte alpha) {
        GlewJNI.glColor4b(red, green, blue, alpha);
    }

    public static void glColor4d(double red, double green, double blue, double alpha) {
        GlewJNI.glColor4d(red, green, blue, alpha);
    }

    public static void glColor4f(float red, float green, float blue, float alpha) {
        GlewJNI.glColor4f(red, green, blue, alpha);
    }

    public static void glColor4i(int red, int green, int blue, int alpha) {
        GlewJNI.glColor4i(red, green, blue, alpha);
    }

    public static void glColor4s(short red, short green, short blue, short alpha) {
        GlewJNI.glColor4s(red, green, blue, alpha);
    }

    public static void glColor4ub(short red, short green, short blue, short alpha) {
        GlewJNI.glColor4ub(red, green, blue, alpha);
    }

    public static void glColor4ui(long red, long green, long blue, long alpha) {
        GlewJNI.glColor4ui(red, green, blue, alpha);
    }

    public static void glColor4us(int red, int green, int blue, int alpha) {
        GlewJNI.glColor4us(red, green, blue, alpha);
    }

    public static void glColor3bv(ByteBuffer v) {
        GlewJNI.glColor3bv(v);
    }

    public static void glColor3dv(DoubleBuffer v) {
        GlewJNI.glColor3dv(v);
    }

    public static void glColor3fv(FloatBuffer v) {
        GlewJNI.glColor3fv(v);
    }

    public static void glColor3iv(IntBuffer v) {
        GlewJNI.glColor3iv(v);
    }

    public static void glColor3sv(ShortBuffer v) {
        GlewJNI.glColor3sv(v);
    }

    public static void glColor3ubv(ShortBuffer v) {
        GlewJNI.glColor3ubv(v);
    }

    public static void glColor3uiv(IntBuffer v) {
        GlewJNI.glColor3uiv(v);
    }

    public static void glColor3usv(IntBuffer v) {
        GlewJNI.glColor3usv(v);
    }

    public static void glColor4bv(ByteBuffer v) {
        GlewJNI.glColor4bv(v);
    }

    public static void glColor4dv(DoubleBuffer v) {
        GlewJNI.glColor4dv(v);
    }

    public static void glColor4fv(FloatBuffer v) {
        GlewJNI.glColor4fv(v);
    }

    public static void glColor4iv(IntBuffer v) {
        GlewJNI.glColor4iv(v);
    }

    public static void glColor4sv(ShortBuffer v) {
        GlewJNI.glColor4sv(v);
    }

    public static void glColor4ubv(ShortBuffer v) {
        GlewJNI.glColor4ubv(v);
    }

    public static void glColor4uiv(IntBuffer v) {
        GlewJNI.glColor4uiv(v);
    }

    public static void glColor4usv(IntBuffer v) {
        GlewJNI.glColor4usv(v);
    }

    public static void glTexCoord1d(double s) {
        GlewJNI.glTexCoord1d(s);
    }

    public static void glTexCoord1f(float s) {
        GlewJNI.glTexCoord1f(s);
    }

    public static void glTexCoord1i(int s) {
        GlewJNI.glTexCoord1i(s);
    }

    public static void glTexCoord1s(short s) {
        GlewJNI.glTexCoord1s(s);
    }

    public static void glTexCoord2d(double s, double t) {
        GlewJNI.glTexCoord2d(s, t);
    }

    public static void glTexCoord2f(float s, float t) {
        GlewJNI.glTexCoord2f(s, t);
    }

    public static void glTexCoord2i(int s, int t) {
        GlewJNI.glTexCoord2i(s, t);
    }

    public static void glTexCoord2s(short s, short t) {
        GlewJNI.glTexCoord2s(s, t);
    }

    public static void glTexCoord3d(double s, double t, double r) {
        GlewJNI.glTexCoord3d(s, t, r);
    }

    public static void glTexCoord3f(float s, float t, float r) {
        GlewJNI.glTexCoord3f(s, t, r);
    }

    public static void glTexCoord3i(int s, int t, int r) {
        GlewJNI.glTexCoord3i(s, t, r);
    }

    public static void glTexCoord3s(short s, short t, short r) {
        GlewJNI.glTexCoord3s(s, t, r);
    }

    public static void glTexCoord4d(double s, double t, double r, double q) {
        GlewJNI.glTexCoord4d(s, t, r, q);
    }

    public static void glTexCoord4f(float s, float t, float r, float q) {
        GlewJNI.glTexCoord4f(s, t, r, q);
    }

    public static void glTexCoord4i(int s, int t, int r, int q) {
        GlewJNI.glTexCoord4i(s, t, r, q);
    }

    public static void glTexCoord4s(short s, short t, short r, short q) {
        GlewJNI.glTexCoord4s(s, t, r, q);
    }

    public static void glTexCoord1dv(DoubleBuffer v) {
        GlewJNI.glTexCoord1dv(v);
    }

    public static void glTexCoord1fv(FloatBuffer v) {
        GlewJNI.glTexCoord1fv(v);
    }

    public static void glTexCoord1iv(IntBuffer v) {
        GlewJNI.glTexCoord1iv(v);
    }

    public static void glTexCoord1sv(ShortBuffer v) {
        GlewJNI.glTexCoord1sv(v);
    }

    public static void glTexCoord2dv(DoubleBuffer v) {
        GlewJNI.glTexCoord2dv(v);
    }

    public static void glTexCoord2fv(FloatBuffer v) {
        GlewJNI.glTexCoord2fv(v);
    }

    public static void glTexCoord2iv(IntBuffer v) {
        GlewJNI.glTexCoord2iv(v);
    }

    public static void glTexCoord2sv(ShortBuffer v) {
        GlewJNI.glTexCoord2sv(v);
    }

    public static void glTexCoord3dv(DoubleBuffer v) {
        GlewJNI.glTexCoord3dv(v);
    }

    public static void glTexCoord3fv(FloatBuffer v) {
        GlewJNI.glTexCoord3fv(v);
    }

    public static void glTexCoord3iv(IntBuffer v) {
        GlewJNI.glTexCoord3iv(v);
    }

    public static void glTexCoord3sv(ShortBuffer v) {
        GlewJNI.glTexCoord3sv(v);
    }

    public static void glTexCoord4dv(DoubleBuffer v) {
        GlewJNI.glTexCoord4dv(v);
    }

    public static void glTexCoord4fv(FloatBuffer v) {
        GlewJNI.glTexCoord4fv(v);
    }

    public static void glTexCoord4iv(IntBuffer v) {
        GlewJNI.glTexCoord4iv(v);
    }

    public static void glTexCoord4sv(ShortBuffer v) {
        GlewJNI.glTexCoord4sv(v);
    }

    public static void glRasterPos2d(double x, double y) {
        GlewJNI.glRasterPos2d(x, y);
    }

    public static void glRasterPos2f(float x, float y) {
        GlewJNI.glRasterPos2f(x, y);
    }

    public static void glRasterPos2i(int x, int y) {
        GlewJNI.glRasterPos2i(x, y);
    }

    public static void glRasterPos2s(short x, short y) {
        GlewJNI.glRasterPos2s(x, y);
    }

    public static void glRasterPos3d(double x, double y, double z) {
        GlewJNI.glRasterPos3d(x, y, z);
    }

    public static void glRasterPos3f(float x, float y, float z) {
        GlewJNI.glRasterPos3f(x, y, z);
    }

    public static void glRasterPos3i(int x, int y, int z) {
        GlewJNI.glRasterPos3i(x, y, z);
    }

    public static void glRasterPos3s(short x, short y, short z) {
        GlewJNI.glRasterPos3s(x, y, z);
    }

    public static void glRasterPos4d(double x, double y, double z, double w) {
        GlewJNI.glRasterPos4d(x, y, z, w);
    }

    public static void glRasterPos4f(float x, float y, float z, float w) {
        GlewJNI.glRasterPos4f(x, y, z, w);
    }

    public static void glRasterPos4i(int x, int y, int z, int w) {
        GlewJNI.glRasterPos4i(x, y, z, w);
    }

    public static void glRasterPos4s(short x, short y, short z, short w) {
        GlewJNI.glRasterPos4s(x, y, z, w);
    }

    public static void glRasterPos2dv(DoubleBuffer v) {
        GlewJNI.glRasterPos2dv(v);
    }

    public static void glRasterPos2fv(FloatBuffer v) {
        GlewJNI.glRasterPos2fv(v);
    }

    public static void glRasterPos2iv(IntBuffer v) {
        GlewJNI.glRasterPos2iv(v);
    }

    public static void glRasterPos2sv(ShortBuffer v) {
        GlewJNI.glRasterPos2sv(v);
    }

    public static void glRasterPos3dv(DoubleBuffer v) {
        GlewJNI.glRasterPos3dv(v);
    }

    public static void glRasterPos3fv(FloatBuffer v) {
        GlewJNI.glRasterPos3fv(v);
    }

    public static void glRasterPos3iv(IntBuffer v) {
        GlewJNI.glRasterPos3iv(v);
    }

    public static void glRasterPos3sv(ShortBuffer v) {
        GlewJNI.glRasterPos3sv(v);
    }

    public static void glRasterPos4dv(DoubleBuffer v) {
        GlewJNI.glRasterPos4dv(v);
    }

    public static void glRasterPos4fv(FloatBuffer v) {
        GlewJNI.glRasterPos4fv(v);
    }

    public static void glRasterPos4iv(IntBuffer v) {
        GlewJNI.glRasterPos4iv(v);
    }

    public static void glRasterPos4sv(ShortBuffer v) {
        GlewJNI.glRasterPos4sv(v);
    }

    public static void glRectd(double x1, double y1, double x2, double y2) {
        GlewJNI.glRectd(x1, y1, x2, y2);
    }

    public static void glRectf(float x1, float y1, float x2, float y2) {
        GlewJNI.glRectf(x1, y1, x2, y2);
    }

    public static void glRecti(int x1, int y1, int x2, int y2) {
        GlewJNI.glRecti(x1, y1, x2, y2);
    }

    public static void glRects(short x1, short y1, short x2, short y2) {
        GlewJNI.glRects(x1, y1, x2, y2);
    }

    public static void glRectdv(DoubleBuffer v1, DoubleBuffer v2) {
        GlewJNI.glRectdv(v1, v2);
    }

    public static void glRectfv(FloatBuffer v1, FloatBuffer v2) {
        GlewJNI.glRectfv(v1, v2);
    }

    public static void glRectiv(IntBuffer v1, IntBuffer v2) {
        GlewJNI.glRectiv(v1, v2);
    }

    public static void glRectsv(ShortBuffer v1, ShortBuffer v2) {
        GlewJNI.glRectsv(v1, v2);
    }

    public static void glVertexPointer(int size, long type, int stride, Buffer ptr) {
        GlewJNI.glVertexPointer(size, type, stride, ptr);
    }

    public static void glNormalPointer(long type, int stride, Buffer ptr) {
        GlewJNI.glNormalPointer(type, stride, ptr);
    }

    public static void glColorPointer(int size, long type, int stride, Buffer ptr) {
        GlewJNI.glColorPointer(size, type, stride, ptr);
    }

    public static void glIndexPointer(long type, int stride, Buffer ptr) {
        GlewJNI.glIndexPointer(type, stride, ptr);
    }

    public static void glTexCoordPointer(int size, long type, int stride, Buffer ptr) {
        GlewJNI.glTexCoordPointer(size, type, stride, ptr);
    }

    public static void glEdgeFlagPointer(int stride, Buffer ptr) {
        GlewJNI.glEdgeFlagPointer(stride, ptr);
    }

    public static void glGetPointerv(long pname, SWIGTYPE_p_p_void params) {
        GlewJNI.glGetPointerv(pname, SWIGTYPE_p_p_void.getCPtr(params));
    }

    public static void glArrayElement(int i) {
        GlewJNI.glArrayElement(i);
    }

    public static void glDrawArrays(long mode, int first, int count) {
        GlewJNI.glDrawArrays(mode, first, count);
    }

    public static void glDrawElements(long mode, int count, long type, Buffer indices) {
        GlewJNI.glDrawElements(mode, count, type, indices);
    }

    public static void glInterleavedArrays(long format, int stride, Buffer pointer) {
        GlewJNI.glInterleavedArrays(format, stride, pointer);
    }

    public static void glShadeModel(long mode) {
        GlewJNI.glShadeModel(mode);
    }

    public static void glLightf(long light, long pname, float param) {
        GlewJNI.glLightf(light, pname, param);
    }

    public static void glLighti(long light, long pname, int param) {
        GlewJNI.glLighti(light, pname, param);
    }

    public static void glLightfv(long light, long pname, FloatBuffer params) {
        GlewJNI.glLightfv(light, pname, params);
    }

    public static void glLightiv(long light, long pname, IntBuffer params) {
        GlewJNI.glLightiv(light, pname, params);
    }

    public static void glGetLightfv(long light, long pname, FloatBuffer params) {
        GlewJNI.glGetLightfv(light, pname, params);
    }

    public static void glGetLightiv(long light, long pname, IntBuffer params) {
        GlewJNI.glGetLightiv(light, pname, params);
    }

    public static void glLightModelf(long pname, float param) {
        GlewJNI.glLightModelf(pname, param);
    }

    public static void glLightModeli(long pname, int param) {
        GlewJNI.glLightModeli(pname, param);
    }

    public static void glLightModelfv(long pname, FloatBuffer params) {
        GlewJNI.glLightModelfv(pname, params);
    }

    public static void glLightModeliv(long pname, IntBuffer params) {
        GlewJNI.glLightModeliv(pname, params);
    }

    public static void glMaterialf(long face, long pname, float param) {
        GlewJNI.glMaterialf(face, pname, param);
    }

    public static void glMateriali(long face, long pname, int param) {
        GlewJNI.glMateriali(face, pname, param);
    }

    public static void glMaterialfv(long face, long pname, FloatBuffer params) {
        GlewJNI.glMaterialfv(face, pname, params);
    }

    public static void glMaterialiv(long face, long pname, IntBuffer params) {
        GlewJNI.glMaterialiv(face, pname, params);
    }

    public static void glGetMaterialfv(long face, long pname, FloatBuffer params) {
        GlewJNI.glGetMaterialfv(face, pname, params);
    }

    public static void glGetMaterialiv(long face, long pname, IntBuffer params) {
        GlewJNI.glGetMaterialiv(face, pname, params);
    }

    public static void glColorMaterial(long face, long mode) {
        GlewJNI.glColorMaterial(face, mode);
    }

    public static void glPixelZoom(float xfactor, float yfactor) {
        GlewJNI.glPixelZoom(xfactor, yfactor);
    }

    public static void glPixelStoref(long pname, float param) {
        GlewJNI.glPixelStoref(pname, param);
    }

    public static void glPixelStorei(long pname, int param) {
        GlewJNI.glPixelStorei(pname, param);
    }

    public static void glPixelTransferf(long pname, float param) {
        GlewJNI.glPixelTransferf(pname, param);
    }

    public static void glPixelTransferi(long pname, int param) {
        GlewJNI.glPixelTransferi(pname, param);
    }

    public static void glPixelMapfv(long map, int mapsize, FloatBuffer values) {
        GlewJNI.glPixelMapfv(map, mapsize, values);
    }

    public static void glPixelMapuiv(long map, int mapsize, IntBuffer values) {
        GlewJNI.glPixelMapuiv(map, mapsize, values);
    }

    public static void glPixelMapusv(long map, int mapsize, IntBuffer values) {
        GlewJNI.glPixelMapusv(map, mapsize, values);
    }

    public static void glGetPixelMapfv(long map, FloatBuffer values) {
        GlewJNI.glGetPixelMapfv(map, values);
    }

    public static void glGetPixelMapuiv(long map, IntBuffer values) {
        GlewJNI.glGetPixelMapuiv(map, values);
    }

    public static void glGetPixelMapusv(long map, IntBuffer values) {
        GlewJNI.glGetPixelMapusv(map, values);
    }

    public static void glBitmap(int width, int height, float xorig, float yorig, float xmove, float ymove, ShortBuffer bitmap) {
        GlewJNI.glBitmap(width, height, xorig, yorig, xmove, ymove, bitmap);
    }

    public static void glReadPixels(int x, int y, int width, int height, long format, long type, Buffer pixels) {
        GlewJNI.glReadPixels(x, y, width, height, format, type, pixels);
    }

    public static void glDrawPixels(int width, int height, long format, long type, Buffer pixels) {
        GlewJNI.glDrawPixels(width, height, format, type, pixels);
    }

    public static void glCopyPixels(int x, int y, int width, int height, long type) {
        GlewJNI.glCopyPixels(x, y, width, height, type);
    }

    public static void glStencilFunc(long func, int ref, long mask) {
        GlewJNI.glStencilFunc(func, ref, mask);
    }

    public static void glStencilMask(long mask) {
        GlewJNI.glStencilMask(mask);
    }

    public static void glStencilOp(long fail, long zfail, long zpass) {
        GlewJNI.glStencilOp(fail, zfail, zpass);
    }

    public static void glClearStencil(int s) {
        GlewJNI.glClearStencil(s);
    }

    public static void glTexGend(long coord, long pname, double param) {
        GlewJNI.glTexGend(coord, pname, param);
    }

    public static void glTexGenf(long coord, long pname, float param) {
        GlewJNI.glTexGenf(coord, pname, param);
    }

    public static void glTexGeni(long coord, long pname, int param) {
        GlewJNI.glTexGeni(coord, pname, param);
    }

    public static void glTexGendv(long coord, long pname, DoubleBuffer params) {
        GlewJNI.glTexGendv(coord, pname, params);
    }

    public static void glTexGenfv(long coord, long pname, FloatBuffer params) {
        GlewJNI.glTexGenfv(coord, pname, params);
    }

    public static void glTexGeniv(long coord, long pname, IntBuffer params) {
        GlewJNI.glTexGeniv(coord, pname, params);
    }

    public static void glGetTexGendv(long coord, long pname, DoubleBuffer params) {
        GlewJNI.glGetTexGendv(coord, pname, params);
    }

    public static void glGetTexGenfv(long coord, long pname, FloatBuffer params) {
        GlewJNI.glGetTexGenfv(coord, pname, params);
    }

    public static void glGetTexGeniv(long coord, long pname, IntBuffer params) {
        GlewJNI.glGetTexGeniv(coord, pname, params);
    }

    public static void glTexEnvf(long target, long pname, float param) {
        GlewJNI.glTexEnvf(target, pname, param);
    }

    public static void glTexEnvi(long target, long pname, int param) {
        GlewJNI.glTexEnvi(target, pname, param);
    }

    public static void glTexEnvfv(long target, long pname, FloatBuffer params) {
        GlewJNI.glTexEnvfv(target, pname, params);
    }

    public static void glTexEnviv(long target, long pname, IntBuffer params) {
        GlewJNI.glTexEnviv(target, pname, params);
    }

    public static void glGetTexEnvfv(long target, long pname, FloatBuffer params) {
        GlewJNI.glGetTexEnvfv(target, pname, params);
    }

    public static void glGetTexEnviv(long target, long pname, IntBuffer params) {
        GlewJNI.glGetTexEnviv(target, pname, params);
    }

    public static void glTexParameterf(long target, long pname, float param) {
        GlewJNI.glTexParameterf(target, pname, param);
    }

    public static void glTexParameteri(long target, long pname, int param) {
        GlewJNI.glTexParameteri(target, pname, param);
    }

    public static void glTexParameterfv(long target, long pname, FloatBuffer params) {
        GlewJNI.glTexParameterfv(target, pname, params);
    }

    public static void glTexParameteriv(long target, long pname, IntBuffer params) {
        GlewJNI.glTexParameteriv(target, pname, params);
    }

    public static void glGetTexParameterfv(long target, long pname, FloatBuffer params) {
        GlewJNI.glGetTexParameterfv(target, pname, params);
    }

    public static void glGetTexParameteriv(long target, long pname, IntBuffer params) {
        GlewJNI.glGetTexParameteriv(target, pname, params);
    }

    public static void glGetTexLevelParameterfv(long target, int level, long pname, FloatBuffer params) {
        GlewJNI.glGetTexLevelParameterfv(target, level, pname, params);
    }

    public static void glGetTexLevelParameteriv(long target, int level, long pname, IntBuffer params) {
        GlewJNI.glGetTexLevelParameteriv(target, level, pname, params);
    }

    public static void glTexImage1D(long target, int level, int internalFormat, int width, int border, long format, long type, Buffer pixels) {
        GlewJNI.glTexImage1D(target, level, internalFormat, width, border, format, type, pixels);
    }

    public static void glTexImage2D(long target, int level, int internalFormat, int width, int height, int border, long format, long type, Buffer pixels) {
        GlewJNI.glTexImage2D(target, level, internalFormat, width, height, border, format, type, pixels);
    }

    public static void glGetTexImage(long target, int level, long format, long type, Buffer pixels) {
        GlewJNI.glGetTexImage(target, level, format, type, pixels);
    }

    public static void glGenTextures(int n, IntBuffer textures) {
        GlewJNI.glGenTextures(n, textures);
    }

    public static void glDeleteTextures(int n, IntBuffer textures) {
        GlewJNI.glDeleteTextures(n, textures);
    }

    public static void glBindTexture(long target, long texture) {
        GlewJNI.glBindTexture(target, texture);
    }

    public static void glPrioritizeTextures(int n, IntBuffer textures, FloatBuffer priorities) {
        GlewJNI.glPrioritizeTextures(n, textures, priorities);
    }

    public static short glAreTexturesResident(int n, IntBuffer textures, ShortBuffer residences) {
        return GlewJNI.glAreTexturesResident(n, textures, residences);
    }

    public static short glIsTexture(long texture) {
        return GlewJNI.glIsTexture(texture);
    }

    public static void glTexSubImage1D(long target, int level, int xoffset, int width, long format, long type, Buffer pixels) {
        GlewJNI.glTexSubImage1D(target, level, xoffset, width, format, type, pixels);
    }

    public static void glTexSubImage2D(long target, int level, int xoffset, int yoffset, int width, int height, long format, long type, Buffer pixels) {
        GlewJNI.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
    }

    public static void glCopyTexImage1D(long target, int level, long internalformat, int x, int y, int width, int border) {
        GlewJNI.glCopyTexImage1D(target, level, internalformat, x, y, width, border);
    }

    public static void glCopyTexImage2D(long target, int level, long internalformat, int x, int y, int width, int height, int border) {
        GlewJNI.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
    }

    public static void glCopyTexSubImage1D(long target, int level, int xoffset, int x, int y, int width) {
        GlewJNI.glCopyTexSubImage1D(target, level, xoffset, x, y, width);
    }

    public static void glCopyTexSubImage2D(long target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
        GlewJNI.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
    }

    public static void glMap1d(long target, double u1, double u2, int stride, int order, DoubleBuffer points) {
        GlewJNI.glMap1d(target, u1, u2, stride, order, points);
    }

    public static void glMap1f(long target, float u1, float u2, int stride, int order, FloatBuffer points) {
        GlewJNI.glMap1f(target, u1, u2, stride, order, points);
    }

    public static void glMap2d(long target, double u1, double u2, int ustride, int uorder, double v1, double v2, int vstride, int vorder, DoubleBuffer points) {
        GlewJNI.glMap2d(target, u1, u2, ustride, uorder, v1, v2, vstride, vorder, points);
    }

    public static void glMap2f(long target, float u1, float u2, int ustride, int uorder, float v1, float v2, int vstride, int vorder, FloatBuffer points) {
        GlewJNI.glMap2f(target, u1, u2, ustride, uorder, v1, v2, vstride, vorder, points);
    }

    public static void glGetMapdv(long target, long query, DoubleBuffer v) {
        GlewJNI.glGetMapdv(target, query, v);
    }

    public static void glGetMapfv(long target, long query, FloatBuffer v) {
        GlewJNI.glGetMapfv(target, query, v);
    }

    public static void glGetMapiv(long target, long query, IntBuffer v) {
        GlewJNI.glGetMapiv(target, query, v);
    }

    public static void glEvalCoord1d(double u) {
        GlewJNI.glEvalCoord1d(u);
    }

    public static void glEvalCoord1f(float u) {
        GlewJNI.glEvalCoord1f(u);
    }

    public static void glEvalCoord1dv(DoubleBuffer u) {
        GlewJNI.glEvalCoord1dv(u);
    }

    public static void glEvalCoord1fv(FloatBuffer u) {
        GlewJNI.glEvalCoord1fv(u);
    }

    public static void glEvalCoord2d(double u, double v) {
        GlewJNI.glEvalCoord2d(u, v);
    }

    public static void glEvalCoord2f(float u, float v) {
        GlewJNI.glEvalCoord2f(u, v);
    }

    public static void glEvalCoord2dv(DoubleBuffer u) {
        GlewJNI.glEvalCoord2dv(u);
    }

    public static void glEvalCoord2fv(FloatBuffer u) {
        GlewJNI.glEvalCoord2fv(u);
    }

    public static void glMapGrid1d(int un, double u1, double u2) {
        GlewJNI.glMapGrid1d(un, u1, u2);
    }

    public static void glMapGrid1f(int un, float u1, float u2) {
        GlewJNI.glMapGrid1f(un, u1, u2);
    }

    public static void glMapGrid2d(int un, double u1, double u2, int vn, double v1, double v2) {
        GlewJNI.glMapGrid2d(un, u1, u2, vn, v1, v2);
    }

    public static void glMapGrid2f(int un, float u1, float u2, int vn, float v1, float v2) {
        GlewJNI.glMapGrid2f(un, u1, u2, vn, v1, v2);
    }

    public static void glEvalPoint1(int i) {
        GlewJNI.glEvalPoint1(i);
    }

    public static void glEvalPoint2(int i, int j) {
        GlewJNI.glEvalPoint2(i, j);
    }

    public static void glEvalMesh1(long mode, int i1, int i2) {
        GlewJNI.glEvalMesh1(mode, i1, i2);
    }

    public static void glEvalMesh2(long mode, int i1, int i2, int j1, int j2) {
        GlewJNI.glEvalMesh2(mode, i1, i2, j1, j2);
    }

    public static void glFogf(long pname, float param) {
        GlewJNI.glFogf(pname, param);
    }

    public static void glFogi(long pname, int param) {
        GlewJNI.glFogi(pname, param);
    }

    public static void glFogfv(long pname, FloatBuffer params) {
        GlewJNI.glFogfv(pname, params);
    }

    public static void glFogiv(long pname, IntBuffer params) {
        GlewJNI.glFogiv(pname, params);
    }

    public static void glFeedbackBuffer(int size, long type, FloatBuffer buffer) {
        GlewJNI.glFeedbackBuffer(size, type, buffer);
    }

    public static void glPassThrough(float token) {
        GlewJNI.glPassThrough(token);
    }

    public static void glSelectBuffer(int size, IntBuffer buffer) {
        GlewJNI.glSelectBuffer(size, buffer);
    }

    public static void glInitNames() {
        GlewJNI.glInitNames();
    }

    public static void glLoadName(long name) {
        GlewJNI.glLoadName(name);
    }

    public static void glPushName(long name) {
        GlewJNI.glPushName(name);
    }

    public static void glPopName() {
        GlewJNI.glPopName();
    }

    public static void gluBeginCurve(SWIGTYPE_p_GLUnurbs nurb) {
        GlewJNI.gluBeginCurve(SWIGTYPE_p_GLUnurbs.getCPtr(nurb));
    }

    public static void gluBeginPolygon(SWIGTYPE_p_GLUtesselator tess) {
        GlewJNI.gluBeginPolygon(SWIGTYPE_p_GLUtesselator.getCPtr(tess));
    }

    public static void gluBeginSurface(SWIGTYPE_p_GLUnurbs nurb) {
        GlewJNI.gluBeginSurface(SWIGTYPE_p_GLUnurbs.getCPtr(nurb));
    }

    public static void gluBeginTrim(SWIGTYPE_p_GLUnurbs nurb) {
        GlewJNI.gluBeginTrim(SWIGTYPE_p_GLUnurbs.getCPtr(nurb));
    }

    public static int gluBuild1DMipmaps(long target, int internalFormat, int width, long format, long type, Buffer data) {
        return GlewJNI.gluBuild1DMipmaps(target, internalFormat, width, format, type, data);
    }

    public static int gluBuild2DMipmaps(long target, int internalFormat, int width, int height, long format, long type, Buffer data) {
        return GlewJNI.gluBuild2DMipmaps(target, internalFormat, width, height, format, type, data);
    }

    public static void gluCylinder(SWIGTYPE_p_GLUquadric quad, double base, double top, double height, int slices, int stacks) {
        GlewJNI.gluCylinder(SWIGTYPE_p_GLUquadric.getCPtr(quad), base, top, height, slices, stacks);
    }

    public static void gluDeleteNurbsRenderer(SWIGTYPE_p_GLUnurbs nurb) {
        GlewJNI.gluDeleteNurbsRenderer(SWIGTYPE_p_GLUnurbs.getCPtr(nurb));
    }

    public static void gluDeleteQuadric(SWIGTYPE_p_GLUquadric quad) {
        GlewJNI.gluDeleteQuadric(SWIGTYPE_p_GLUquadric.getCPtr(quad));
    }

    public static void gluDeleteTess(SWIGTYPE_p_GLUtesselator tess) {
        GlewJNI.gluDeleteTess(SWIGTYPE_p_GLUtesselator.getCPtr(tess));
    }

    public static void gluDisk(SWIGTYPE_p_GLUquadric quad, double inner, double outer, int slices, int loops) {
        GlewJNI.gluDisk(SWIGTYPE_p_GLUquadric.getCPtr(quad), inner, outer, slices, loops);
    }

    public static void gluEndCurve(SWIGTYPE_p_GLUnurbs nurb) {
        GlewJNI.gluEndCurve(SWIGTYPE_p_GLUnurbs.getCPtr(nurb));
    }

    public static void gluEndPolygon(SWIGTYPE_p_GLUtesselator tess) {
        GlewJNI.gluEndPolygon(SWIGTYPE_p_GLUtesselator.getCPtr(tess));
    }

    public static void gluEndSurface(SWIGTYPE_p_GLUnurbs nurb) {
        GlewJNI.gluEndSurface(SWIGTYPE_p_GLUnurbs.getCPtr(nurb));
    }

    public static void gluEndTrim(SWIGTYPE_p_GLUnurbs nurb) {
        GlewJNI.gluEndTrim(SWIGTYPE_p_GLUnurbs.getCPtr(nurb));
    }

    public static ShortBuffer gluErrorString(long error) {
        return GlewJNI.gluErrorString(error);
    }

    public static void gluGetNurbsProperty(SWIGTYPE_p_GLUnurbs nurb, long property, FloatBuffer data) {
        GlewJNI.gluGetNurbsProperty(SWIGTYPE_p_GLUnurbs.getCPtr(nurb), property, data);
    }

    public static ShortBuffer gluGetString(long name) {
        return GlewJNI.gluGetString(name);
    }

    public static void gluGetTessProperty(SWIGTYPE_p_GLUtesselator tess, long which, DoubleBuffer data) {
        GlewJNI.gluGetTessProperty(SWIGTYPE_p_GLUtesselator.getCPtr(tess), which, data);
    }

    public static void gluLoadSamplingMatrices(SWIGTYPE_p_GLUnurbs nurb, FloatBuffer model, FloatBuffer perspective, IntBuffer view) {
        GlewJNI.gluLoadSamplingMatrices(SWIGTYPE_p_GLUnurbs.getCPtr(nurb), model, perspective, view);
    }

    public static void gluLookAt(double eyeX, double eyeY, double eyeZ, double centerX, double centerY, double centerZ, double upX, double upY, double upZ) {
        GlewJNI.gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public static SWIGTYPE_p_GLUnurbs gluNewNurbsRenderer() {
        long cPtr = GlewJNI.gluNewNurbsRenderer();
        return (cPtr == 0) ? null : new SWIGTYPE_p_GLUnurbs(cPtr, false);
    }

    public static SWIGTYPE_p_GLUquadric gluNewQuadric() {
        long cPtr = GlewJNI.gluNewQuadric();
        return (cPtr == 0) ? null : new SWIGTYPE_p_GLUquadric(cPtr, false);
    }

    public static SWIGTYPE_p_GLUtesselator gluNewTess() {
        long cPtr = GlewJNI.gluNewTess();
        return (cPtr == 0) ? null : new SWIGTYPE_p_GLUtesselator(cPtr, false);
    }

    public static void gluNextContour(SWIGTYPE_p_GLUtesselator tess, long type) {
        GlewJNI.gluNextContour(SWIGTYPE_p_GLUtesselator.getCPtr(tess), type);
    }

    public static void gluNurbsCurve(SWIGTYPE_p_GLUnurbs nurb, int knotCount, FloatBuffer knots, int stride, FloatBuffer control, int order, long type) {
        GlewJNI.gluNurbsCurve(SWIGTYPE_p_GLUnurbs.getCPtr(nurb), knotCount, knots, stride, control, order, type);
    }

    public static void gluNurbsProperty(SWIGTYPE_p_GLUnurbs nurb, long property, float value) {
        GlewJNI.gluNurbsProperty(SWIGTYPE_p_GLUnurbs.getCPtr(nurb), property, value);
    }

    public static void gluNurbsSurface(SWIGTYPE_p_GLUnurbs nurb, int sKnotCount, FloatBuffer sKnots, int tKnotCount, FloatBuffer tKnots, int sStride, int tStride, FloatBuffer control, int sOrder, int tOrder, long type) {
        GlewJNI.gluNurbsSurface(SWIGTYPE_p_GLUnurbs.getCPtr(nurb), sKnotCount, sKnots, tKnotCount, tKnots, sStride, tStride, control, sOrder, tOrder, type);
    }

    public static void gluOrtho2D(double left, double right, double bottom, double top) {
        GlewJNI.gluOrtho2D(left, right, bottom, top);
    }

    public static void gluPartialDisk(SWIGTYPE_p_GLUquadric quad, double inner, double outer, int slices, int loops, double start, double sweep) {
        GlewJNI.gluPartialDisk(SWIGTYPE_p_GLUquadric.getCPtr(quad), inner, outer, slices, loops, start, sweep);
    }

    public static void gluPerspective(double fovy, double aspect, double zNear, double zFar) {
        GlewJNI.gluPerspective(fovy, aspect, zNear, zFar);
    }

    public static void gluPickMatrix(double x, double y, double delX, double delY, IntBuffer viewport) {
        GlewJNI.gluPickMatrix(x, y, delX, delY, viewport);
    }

    public static int gluProject(double objX, double objY, double objZ, DoubleBuffer model, DoubleBuffer proj, IntBuffer view, DoubleBuffer winX, DoubleBuffer winY, DoubleBuffer winZ) {
        return GlewJNI.gluProject(objX, objY, objZ, model, proj, view, winX, winY, winZ);
    }

    public static void gluPwlCurve(SWIGTYPE_p_GLUnurbs nurb, int count, FloatBuffer data, int stride, long type) {
        GlewJNI.gluPwlCurve(SWIGTYPE_p_GLUnurbs.getCPtr(nurb), count, data, stride, type);
    }

    public static void gluQuadricDrawStyle(SWIGTYPE_p_GLUquadric quad, long draw) {
        GlewJNI.gluQuadricDrawStyle(SWIGTYPE_p_GLUquadric.getCPtr(quad), draw);
    }

    public static void gluQuadricNormals(SWIGTYPE_p_GLUquadric quad, long normal) {
        GlewJNI.gluQuadricNormals(SWIGTYPE_p_GLUquadric.getCPtr(quad), normal);
    }

    public static void gluQuadricOrientation(SWIGTYPE_p_GLUquadric quad, long orientation) {
        GlewJNI.gluQuadricOrientation(SWIGTYPE_p_GLUquadric.getCPtr(quad), orientation);
    }

    public static void gluQuadricTexture(SWIGTYPE_p_GLUquadric quad, short texture) {
        GlewJNI.gluQuadricTexture(SWIGTYPE_p_GLUquadric.getCPtr(quad), texture);
    }

    public static int gluScaleImage(long format, int wIn, int hIn, long typeIn, Buffer dataIn, int wOut, int hOut, long typeOut, Buffer dataOut) {
        return GlewJNI.gluScaleImage(format, wIn, hIn, typeIn, dataIn, wOut, hOut, typeOut, dataOut);
    }

    public static void gluSphere(SWIGTYPE_p_GLUquadric quad, double radius, int slices, int stacks) {
        GlewJNI.gluSphere(SWIGTYPE_p_GLUquadric.getCPtr(quad), radius, slices, stacks);
    }

    public static void gluTessBeginContour(SWIGTYPE_p_GLUtesselator tess) {
        GlewJNI.gluTessBeginContour(SWIGTYPE_p_GLUtesselator.getCPtr(tess));
    }

    public static void gluTessBeginPolygon(SWIGTYPE_p_GLUtesselator tess, Buffer data) {
        GlewJNI.gluTessBeginPolygon(SWIGTYPE_p_GLUtesselator.getCPtr(tess), data);
    }

    public static void gluTessEndContour(SWIGTYPE_p_GLUtesselator tess) {
        GlewJNI.gluTessEndContour(SWIGTYPE_p_GLUtesselator.getCPtr(tess));
    }

    public static void gluTessEndPolygon(SWIGTYPE_p_GLUtesselator tess) {
        GlewJNI.gluTessEndPolygon(SWIGTYPE_p_GLUtesselator.getCPtr(tess));
    }

    public static void gluTessNormal(SWIGTYPE_p_GLUtesselator tess, double valueX, double valueY, double valueZ) {
        GlewJNI.gluTessNormal(SWIGTYPE_p_GLUtesselator.getCPtr(tess), valueX, valueY, valueZ);
    }

    public static void gluTessProperty(SWIGTYPE_p_GLUtesselator tess, long which, double data) {
        GlewJNI.gluTessProperty(SWIGTYPE_p_GLUtesselator.getCPtr(tess), which, data);
    }

    public static void gluTessVertex(SWIGTYPE_p_GLUtesselator tess, DoubleBuffer location, Buffer data) {
        GlewJNI.gluTessVertex(SWIGTYPE_p_GLUtesselator.getCPtr(tess), location, data);
    }

    public static int gluUnProject(double winX, double winY, double winZ, DoubleBuffer model, DoubleBuffer proj, IntBuffer view, DoubleBuffer objX, DoubleBuffer objY, DoubleBuffer objZ) {
        return GlewJNI.gluUnProject(winX, winY, winZ, model, proj, view, objX, objY, objZ);
    }
}
