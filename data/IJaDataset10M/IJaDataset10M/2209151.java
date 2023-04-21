package com.jmex.model.ogrexml.anim;

import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Combines mesh and bone animations into one class for easier access
 */
public class Animation implements Serializable, Savable {

    private static final long serialVersionUID = 1L;

    private String name;

    private float length;

    private BoneAnimation boneAnim;

    private MeshAnimation meshAnim;

    Animation(BoneAnimation boneAnim, MeshAnimation meshAnim) {
        this.boneAnim = boneAnim;
        this.meshAnim = meshAnim;
        if (boneAnim == null) {
            this.name = meshAnim.getName();
            this.length = meshAnim.getLength();
        } else if (meshAnim == null) {
            this.name = boneAnim.getName();
            this.length = boneAnim.getLength();
        } else {
            this.name = boneAnim.getName();
            this.length = Math.max(boneAnim.getLength(), meshAnim.getLength());
        }
    }

    /**
     * Serialization only. Do not use.
     */
    public Animation() {
    }

    void setBoneAnimation(BoneAnimation boneAnim) {
        this.boneAnim = boneAnim;
        this.length = Math.max(boneAnim.getLength(), meshAnim.getLength());
    }

    void setMeshAnimation(MeshAnimation meshAnim) {
        this.meshAnim = meshAnim;
        this.length = Math.max(boneAnim.getLength(), meshAnim.getLength());
    }

    public boolean hasMeshAnimation() {
        return meshAnim != null;
    }

    public boolean hasBoneAnimation() {
        return boneAnim != null;
    }

    public String getName() {
        return name;
    }

    public float getLength() {
        return length;
    }

    void setTime(float time, OgreMesh[] targets, Skeleton skeleton, float weight, ArrayList<Integer> affectedBones) {
        if (meshAnim != null) meshAnim.setTime(time, targets, weight);
        if (boneAnim != null) {
            boneAnim.setTime(time, skeleton, weight, affectedBones);
        }
    }

    public MeshAnimation getMeshAnimation() {
        return meshAnim;
    }

    public BoneAnimation getBoneAnimation() {
        return boneAnim;
    }

    public void write(JMEExporter e) throws IOException {
        OutputCapsule out = e.getCapsule(this);
        out.write(name, "name", "");
        out.write(length, "length", -1f);
        out.write(boneAnim, "boneAnim", null);
        out.write(meshAnim, "meshAnim", null);
    }

    public void read(JMEImporter i) throws IOException {
        InputCapsule in = i.getCapsule(this);
        name = in.readString("name", "");
        length = in.readFloat("length", -1f);
        boneAnim = (BoneAnimation) in.readSavable("boneAnim", null);
        meshAnim = (MeshAnimation) in.readSavable("meshAnim", null);
    }

    public Class getClassTag() {
        return Animation.class;
    }
}
