package com.silenistudios.silenus.raw;

import java.io.Serializable;
import java.util.Vector;
import com.silenistudios.silenus.dom.Instance;

/**
 * Base class that contains data about one particular instance (bitmap, shape, ...) for one particular frame.
 * @author Karel
 *
 */
public abstract class AnimationInstanceData implements Serializable {

    Instance fInstance;

    int fIndex;

    TransformationMatrix fTransformationMatrix;

    boolean fMask;

    boolean fMasked;

    Vector<Integer> fMasks = null;

    public AnimationInstanceData(Instance instance, TransformationMatrix matrix) {
        fInstance = instance;
        fTransformationMatrix = matrix.clone();
        fMask = instance.isMask();
        fMasked = instance.isMasked();
    }

    public void setIndex(int index) {
        fIndex = index;
    }

    public int getIndex() {
        return fIndex;
    }

    public boolean isMask() {
        return fMask;
    }

    public boolean isMasked() {
        return fMasked;
    }

    public void setMasks(Vector<Integer> masks) {
        fMasks = new Vector<Integer>();
        for (Integer i : masks) fMasks.add(i);
    }

    public Instance getInstance() {
        return fInstance;
    }

    public TransformationMatrix getTransformationMatrix() {
        return fTransformationMatrix;
    }

    protected String getBasicJSON() {
        StringBuilder ss = new StringBuilder();
        TransformationMatrix m = getTransformationMatrix();
        ss.append("\"translate\":[").append(m.getTranslateX()).append(",").append(m.getTranslateY()).append("],");
        ss.append("\"scale\":[").append(m.getScaleX()).append(",").append(m.getScaleY()).append("],");
        ss.append("\"rotation\":").append(m.getRotation()).append(",");
        ss.append("\"instanceIndex\":").append(fIndex);
        if (fMask) ss.append(",\"mask\":true");
        if (fMasked) {
            ss.append(",\"masked\":true");
            ss.append(",\"masks\":[");
            for (int i = 0; i < fMasks.size(); ++i) {
                if (i != 0) ss.append(",");
                ss.append(fMasks.get(i));
            }
            ss.append("]");
        }
        return ss.toString();
    }

    public abstract String getJSON();
}
