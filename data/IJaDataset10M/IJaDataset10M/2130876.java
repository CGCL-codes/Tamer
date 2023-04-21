package org.xith3d.transients;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus) [code cleaning]
 */
public class VectorHeap {

    private static ObjectHeap<Vector3f> heapFloat = new ObjectHeap<Vector3f>();

    private static ObjectHeap<Vector3d> heapDouble = new ObjectHeap<Vector3d>();

    public static synchronized Vector3f alloc() {
        Vector3f v = heapFloat.alloc();
        if (v == null) {
            return (new Vector3f());
        } else {
            return (v);
        }
    }

    public static Vector3d allocVector3d() {
        Vector3d v = heapDouble.alloc();
        if (v == null) {
            return (new Vector3d());
        } else {
            return (v);
        }
    }

    public static synchronized void free(Vector3f v) {
        heapFloat.free(v);
    }

    public static void free(Vector3d v) {
        heapDouble.free(v);
    }
}
