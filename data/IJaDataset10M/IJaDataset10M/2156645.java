package org.karlsland.m3g;

import java.util.Arrays;
import java.util.List;

public class MorphingMesh extends Mesh {

    private native void jni_initialize(VertexBuffer base, VertexBuffer[] targets, IndexBuffer[] submeshes, Appearance[] appearances);

    private native void jni_initialize(VertexBuffer base, VertexBuffer[] targets, IndexBuffer submesh, Appearance appearance);

    private native void jni_finalize();

    private native IndexBuffer jni_getMorphTarget(int index);

    private native int jni_getMorphTargetCount();

    private native void jni_getWeights(float[] weights);

    private native void jni_setWeights(float[] weights);

    private native String jni_print();

    private List<VertexBuffer> morphTargets;

    public MorphingMesh(VertexBuffer base, VertexBuffer[] targets, IndexBuffer[] submeshes, Appearance[] appearances) {
        super(base, submeshes, appearances);
        this.morphTargets = Arrays.asList(targets);
        jni_initialize(base, targets, submeshes, appearances);
    }

    public MorphingMesh(VertexBuffer base, VertexBuffer[] targets, IndexBuffer submesh, Appearance appearance) {
        super(base, submesh, appearance);
        this.morphTargets = Arrays.asList(targets);
        jni_initialize(base, targets, submesh, appearance);
    }

    @Override
    protected void finalize() {
        jni_finalize();
    }

    public IndexBuffer getMorphTarget(int index) {
        return jni_getMorphTarget(index);
    }

    public int getMorphTargetCount() {
        return jni_getMorphTargetCount();
    }

    public void getWeights(float[] weights) {
        jni_getWeights(weights);
    }

    public void setWeights(float[] weigths) {
        jni_setWeights(weigths);
    }

    @Override
    public String toString() {
        return jni_print();
    }
}
