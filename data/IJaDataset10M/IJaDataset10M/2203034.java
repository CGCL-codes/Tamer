package com.selcukcihan.xfacej.xengine;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import com.selcukcihan.xfacej.xmath.Vector3;

public class KeyframeInterpolator {

    private static void interpolateDrawables(Drawable pKey1, Drawable pKey2, Drawable pTarg, float w) {
        MeshManager pMM = MeshManager.getInstance();
        DeformableGeometry pKeyMesh1 = pMM.getMesh(pKey1.getMeshName());
        DeformableGeometry pKeyMesh2 = pMM.getMesh(pKey2.getMeshName());
        DeformableGeometry pTargMesh = pMM.getMesh(pTarg.getMeshName());
        int vertCount = pKeyMesh1.getVertexCount();
        final Vector3Buffer pVert1 = pKeyMesh1.getDeformedVerticesVector();
        final Vector3Buffer pVert2 = pKeyMesh2.getDeformedVerticesVector();
        final Vector3Buffer pNorm1 = pKeyMesh1.getNormals();
        final Vector3Buffer pNorm2 = pKeyMesh2.getNormals();
        assert (vertCount == pKeyMesh2.getVertexCount()) : "vertex counts not matching in drawable morphing";
        Vector<Vector3> pVertTarg = new Vector<Vector3>(vertCount);
        Vector<Vector3> pNormTarg = new Vector<Vector3>(vertCount);
        float w_2cube = (2 * w * w * w);
        float w_3sqr = (3 * w * w);
        int hh = 0;
        while (pVert1.hasRemaining()) {
            Vector3 v1 = pVert1.get();
            Vector3 v2;
            if (pVert1 != pVert2) v2 = pVert2.get(); else v2 = new Vector3(v1);
            Vector3 v3 = v1.opMultiplyScalar(w_2cube - w_3sqr + 1).opAdd(v2.opMultiplyScalar(w_3sqr - w_2cube));
            pVertTarg.add(v3);
            hh++;
        }
        assert (pNorm1.size() == pNorm2.size()) : "yeni: normal vertex counts not matching in drawable morphing";
        while (pNorm1.hasRemaining()) {
            Vector3 v1 = pNorm1.get();
            Vector3 v2;
            if (pNorm1 != pNorm2) v2 = pNorm2.get(); else v2 = new Vector3(v1);
            Vector3 v3 = v1.opMultiplyScalar(w_2cube - w_3sqr + 1).opAdd(v2.opMultiplyScalar(w_3sqr - w_2cube));
            pNormTarg.add(v3);
            hh++;
        }
        pTargMesh.setDeformedVertices(pVertTarg);
        pTargMesh.setNormals(pNormTarg);
    }

    public static Entity interpolate(final Entity fromEnt1, final Entity fromEnt2, Entity toEnt, float w) {
        LinkedList<Drawable> src1 = fromEnt1.getDrawables();
        LinkedList<Drawable> src2 = fromEnt2.getDrawables();
        if (toEnt.getDrawableCount() != fromEnt1.getDrawableCount()) {
            toEnt.release(true);
            toEnt.copyFrom(fromEnt1, true);
        }
        LinkedList<Drawable> dst = toEnt.getDrawables();
        Iterator<Drawable> it1 = src1.iterator();
        Iterator<Drawable> it2 = src2.iterator();
        Iterator<Drawable> it3 = dst.iterator();
        while (it1.hasNext()) {
            assert (it2.hasNext()) : "Keyframe Morpher, src2 drawable count not matching src1's";
            assert (it3.hasNext()) : "Keyframe Morpher, dst drawable count not matching src1's";
            interpolateDrawables(it1.next(), it2.next(), it3.next(), w);
        }
        return toEnt;
    }
}
