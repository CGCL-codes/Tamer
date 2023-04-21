package com.sun.opengl.impl.tessellator;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

class Render {

    private static final boolean USE_OPTIMIZED_CODE_PATH = false;

    private Render() {
    }

    private static final RenderFan renderFan = new RenderFan();

    private static final RenderStrip renderStrip = new RenderStrip();

    private static final RenderTriangle renderTriangle = new RenderTriangle();

    private static class FaceCount {

        public FaceCount() {
        }

        public FaceCount(long size, com.sun.opengl.impl.tessellator.GLUhalfEdge eStart, renderCallBack render) {
            this.size = size;
            this.eStart = eStart;
            this.render = render;
        }

        long size;

        com.sun.opengl.impl.tessellator.GLUhalfEdge eStart;

        renderCallBack render;
    }

    ;

    private static interface renderCallBack {

        void render(GLUtessellatorImpl tess, com.sun.opengl.impl.tessellator.GLUhalfEdge e, long size);
    }

    public static void __gl_renderMesh(GLUtessellatorImpl tess, com.sun.opengl.impl.tessellator.GLUmesh mesh) {
        com.sun.opengl.impl.tessellator.GLUface f;
        tess.lonelyTriList = null;
        for (f = mesh.fHead.next; f != mesh.fHead; f = f.next) {
            f.marked = false;
        }
        for (f = mesh.fHead.next; f != mesh.fHead; f = f.next) {
            if (f.inside && !f.marked) {
                RenderMaximumFaceGroup(tess, f);
                assert (f.marked);
            }
        }
        if (tess.lonelyTriList != null) {
            RenderLonelyTriangles(tess, tess.lonelyTriList);
            tess.lonelyTriList = null;
        }
    }

    static void RenderMaximumFaceGroup(GLUtessellatorImpl tess, com.sun.opengl.impl.tessellator.GLUface fOrig) {
        com.sun.opengl.impl.tessellator.GLUhalfEdge e = fOrig.anEdge;
        FaceCount max = new FaceCount();
        FaceCount newFace = new FaceCount();
        max.size = 1;
        max.eStart = e;
        max.render = renderTriangle;
        if (!tess.flagBoundary) {
            newFace = MaximumFan(e);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = MaximumFan(e.Lnext);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = MaximumFan(e.Onext.Sym);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = MaximumStrip(e);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = MaximumStrip(e.Lnext);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = MaximumStrip(e.Onext.Sym);
            if (newFace.size > max.size) {
                max = newFace;
            }
        }
        max.render.render(tess, max.eStart, max.size);
    }

    private static boolean Marked(com.sun.opengl.impl.tessellator.GLUface f) {
        return !f.inside || f.marked;
    }

    private static GLUface AddToTrail(com.sun.opengl.impl.tessellator.GLUface f, com.sun.opengl.impl.tessellator.GLUface t) {
        f.trail = t;
        f.marked = true;
        return f;
    }

    private static void FreeTrail(com.sun.opengl.impl.tessellator.GLUface t) {
        if (true) {
            while (t != null) {
                t.marked = false;
                t = t.trail;
            }
        } else {
        }
    }

    static FaceCount MaximumFan(com.sun.opengl.impl.tessellator.GLUhalfEdge eOrig) {
        FaceCount newFace = new FaceCount(0, null, renderFan);
        com.sun.opengl.impl.tessellator.GLUface trail = null;
        com.sun.opengl.impl.tessellator.GLUhalfEdge e;
        for (e = eOrig; !Marked(e.Lface); e = e.Onext) {
            trail = AddToTrail(e.Lface, trail);
            ++newFace.size;
        }
        for (e = eOrig; !Marked(e.Sym.Lface); e = e.Sym.Lnext) {
            trail = AddToTrail(e.Sym.Lface, trail);
            ++newFace.size;
        }
        newFace.eStart = e;
        FreeTrail(trail);
        return newFace;
    }

    private static boolean IsEven(long n) {
        return (n & 0x1L) == 0;
    }

    static FaceCount MaximumStrip(com.sun.opengl.impl.tessellator.GLUhalfEdge eOrig) {
        FaceCount newFace = new FaceCount(0, null, renderStrip);
        long headSize = 0, tailSize = 0;
        com.sun.opengl.impl.tessellator.GLUface trail = null;
        com.sun.opengl.impl.tessellator.GLUhalfEdge e, eTail, eHead;
        for (e = eOrig; !Marked(e.Lface); ++tailSize, e = e.Onext) {
            trail = AddToTrail(e.Lface, trail);
            ++tailSize;
            e = e.Lnext.Sym;
            if (Marked(e.Lface)) break;
            trail = AddToTrail(e.Lface, trail);
        }
        eTail = e;
        for (e = eOrig; !Marked(e.Sym.Lface); ++headSize, e = e.Sym.Onext.Sym) {
            trail = AddToTrail(e.Sym.Lface, trail);
            ++headSize;
            e = e.Sym.Lnext;
            if (Marked(e.Sym.Lface)) break;
            trail = AddToTrail(e.Sym.Lface, trail);
        }
        eHead = e;
        newFace.size = tailSize + headSize;
        if (IsEven(tailSize)) {
            newFace.eStart = eTail.Sym;
        } else if (IsEven(headSize)) {
            newFace.eStart = eHead;
        } else {
            --newFace.size;
            newFace.eStart = eHead.Onext;
        }
        FreeTrail(trail);
        return newFace;
    }

    private static class RenderTriangle implements renderCallBack {

        public void render(GLUtessellatorImpl tess, com.sun.opengl.impl.tessellator.GLUhalfEdge e, long size) {
            assert (size == 1);
            tess.lonelyTriList = AddToTrail(e.Lface, tess.lonelyTriList);
        }
    }

    static void RenderLonelyTriangles(GLUtessellatorImpl tess, com.sun.opengl.impl.tessellator.GLUface f) {
        com.sun.opengl.impl.tessellator.GLUhalfEdge e;
        int newState;
        int edgeState = -1;
        tess.callBeginOrBeginData(GL.GL_TRIANGLES);
        for (; f != null; f = f.trail) {
            e = f.anEdge;
            do {
                if (tess.flagBoundary) {
                    newState = (!e.Sym.Lface.inside) ? 1 : 0;
                    if (edgeState != newState) {
                        edgeState = newState;
                        tess.callEdgeFlagOrEdgeFlagData(edgeState != 0);
                    }
                }
                tess.callVertexOrVertexData(e.Org.data);
                e = e.Lnext;
            } while (e != f.anEdge);
        }
        tess.callEndOrEndData();
    }

    private static class RenderFan implements renderCallBack {

        public void render(GLUtessellatorImpl tess, com.sun.opengl.impl.tessellator.GLUhalfEdge e, long size) {
            tess.callBeginOrBeginData(GL.GL_TRIANGLE_FAN);
            tess.callVertexOrVertexData(e.Org.data);
            tess.callVertexOrVertexData(e.Sym.Org.data);
            while (!Marked(e.Lface)) {
                e.Lface.marked = true;
                --size;
                e = e.Onext;
                tess.callVertexOrVertexData(e.Sym.Org.data);
            }
            assert (size == 0);
            tess.callEndOrEndData();
        }
    }

    private static class RenderStrip implements renderCallBack {

        public void render(GLUtessellatorImpl tess, com.sun.opengl.impl.tessellator.GLUhalfEdge e, long size) {
            tess.callBeginOrBeginData(GL.GL_TRIANGLE_STRIP);
            tess.callVertexOrVertexData(e.Org.data);
            tess.callVertexOrVertexData(e.Sym.Org.data);
            while (!Marked(e.Lface)) {
                e.Lface.marked = true;
                --size;
                e = e.Lnext.Sym;
                tess.callVertexOrVertexData(e.Org.data);
                if (Marked(e.Lface)) break;
                e.Lface.marked = true;
                --size;
                e = e.Onext;
                tess.callVertexOrVertexData(e.Sym.Org.data);
            }
            assert (size == 0);
            tess.callEndOrEndData();
        }
    }

    public static void __gl_renderBoundary(GLUtessellatorImpl tess, com.sun.opengl.impl.tessellator.GLUmesh mesh) {
        com.sun.opengl.impl.tessellator.GLUface f;
        com.sun.opengl.impl.tessellator.GLUhalfEdge e;
        for (f = mesh.fHead.next; f != mesh.fHead; f = f.next) {
            if (f.inside) {
                tess.callBeginOrBeginData(GL.GL_LINE_LOOP);
                e = f.anEdge;
                do {
                    tess.callVertexOrVertexData(e.Org.data);
                    e = e.Lnext;
                } while (e != f.anEdge);
                tess.callEndOrEndData();
            }
        }
    }

    /************************ Quick-and-dirty decomposition ******************/
    private static final int SIGN_INCONSISTENT = 2;

    static int ComputeNormal(GLUtessellatorImpl tess, double[] norm, boolean check) {
        com.sun.opengl.impl.tessellator.CachedVertex[] v = tess.cache;
        int vn = tess.cacheCount;
        int vc;
        double dot, xc, yc, zc, xp, yp, zp;
        double[] n = new double[3];
        int sign = 0;
        if (!check) {
            norm[0] = norm[1] = norm[2] = 0.0;
        }
        vc = 1;
        xc = v[vc].coords[0] - v[0].coords[0];
        yc = v[vc].coords[1] - v[0].coords[1];
        zc = v[vc].coords[2] - v[0].coords[2];
        while (++vc < vn) {
            xp = xc;
            yp = yc;
            zp = zc;
            xc = v[vc].coords[0] - v[0].coords[0];
            yc = v[vc].coords[1] - v[0].coords[1];
            zc = v[vc].coords[2] - v[0].coords[2];
            n[0] = yp * zc - zp * yc;
            n[1] = zp * xc - xp * zc;
            n[2] = xp * yc - yp * xc;
            dot = n[0] * norm[0] + n[1] * norm[1] + n[2] * norm[2];
            if (!check) {
                if (dot >= 0) {
                    norm[0] += n[0];
                    norm[1] += n[1];
                    norm[2] += n[2];
                } else {
                    norm[0] -= n[0];
                    norm[1] -= n[1];
                    norm[2] -= n[2];
                }
            } else if (dot != 0) {
                if (dot > 0) {
                    if (sign < 0) return SIGN_INCONSISTENT;
                    sign = 1;
                } else {
                    if (sign > 0) return SIGN_INCONSISTENT;
                    sign = -1;
                }
            }
        }
        return sign;
    }

    public static boolean __gl_renderCache(GLUtessellatorImpl tess) {
        com.sun.opengl.impl.tessellator.CachedVertex[] v = tess.cache;
        int vn = tess.cacheCount;
        int vc;
        double[] norm = new double[3];
        int sign;
        if (tess.cacheCount < 3) {
            return true;
        }
        norm[0] = tess.normal[0];
        norm[1] = tess.normal[1];
        norm[2] = tess.normal[2];
        if (norm[0] == 0 && norm[1] == 0 && norm[2] == 0) {
            ComputeNormal(tess, norm, false);
        }
        sign = ComputeNormal(tess, norm, true);
        if (sign == SIGN_INCONSISTENT) {
            return false;
        }
        if (sign == 0) {
            return true;
        }
        if (!USE_OPTIMIZED_CODE_PATH) {
            return false;
        } else {
            switch(tess.windingRule) {
                case GLU.GLU_TESS_WINDING_ODD:
                case GLU.GLU_TESS_WINDING_NONZERO:
                    break;
                case GLU.GLU_TESS_WINDING_POSITIVE:
                    if (sign < 0) return true;
                    break;
                case GLU.GLU_TESS_WINDING_NEGATIVE:
                    if (sign > 0) return true;
                    break;
                case GLU.GLU_TESS_WINDING_ABS_GEQ_TWO:
                    return true;
            }
            tess.callBeginOrBeginData(tess.boundaryOnly ? GL.GL_LINE_LOOP : (tess.cacheCount > 3) ? GL.GL_TRIANGLE_FAN : GL.GL_TRIANGLES);
            tess.callVertexOrVertexData(v[0].data);
            if (sign > 0) {
                for (vc = 1; vc < vn; ++vc) {
                    tess.callVertexOrVertexData(v[vc].data);
                }
            } else {
                for (vc = vn - 1; vc > 0; --vc) {
                    tess.callVertexOrVertexData(v[vc].data);
                }
            }
            tess.callEndOrEndData();
            return true;
        }
    }
}
