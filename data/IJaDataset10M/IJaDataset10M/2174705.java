package com.silenistudios.silenus.raw;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import com.silenistudios.silenus.dom.Instance;

/**
 * This data structure contains all points and locations for one animation (scene).
 * Can be easily serialized and recovered later for re-use.
 * @author Karel
 *
 */
public class AnimationData implements Serializable {

    private static final long serialVersionUID = 2283183510219011650L;

    Vector<Instance> fInstances = new Vector<Instance>();

    Map<String, Integer> fLibraryItemNameToInstanceIndex = new HashMap<String, Integer>();

    AnimationFrameData[] fFrames;

    int fCurrentFrame = 0;

    int fAnimationLength;

    int fWidth;

    int fHeight;

    int fFrameRate;

    Vector<Integer> fMasks = new Vector<Integer>();

    public AnimationData(int animationLength, int width, int height, int frameRate) {
        fAnimationLength = animationLength;
        fWidth = width;
        fHeight = height;
        fFrameRate = frameRate;
        fFrames = new AnimationFrameData[fAnimationLength];
        for (int i = 0; i < fAnimationLength; ++i) fFrames[i] = new AnimationFrameData();
    }

    public void addInstance(AnimationInstanceData data) {
        int idx = fFrames[fCurrentFrame].addAnimationInstanceData(data);
        Instance instance = data.getInstance();
        String libraryItemName = instance.getLibraryItemName();
        if (data.isMask()) {
            fMasks.add(idx);
        }
        if (data.isMasked()) {
            data.setMasks(fMasks);
        }
        if (!fLibraryItemNameToInstanceIndex.containsKey(libraryItemName)) {
            fInstances.add(instance);
            fLibraryItemNameToInstanceIndex.put(libraryItemName, fInstances.size() - 1);
        }
        data.setIndex(fLibraryItemNameToInstanceIndex.get(libraryItemName));
    }

    public void advanceFrame() {
        ++fCurrentFrame;
    }

    public void setFrame(int frame) {
        fCurrentFrame = frame;
    }

    public void setFrame(int frameIndex, AnimationFrameData data) {
        assert (0 <= frameIndex && frameIndex < fFrames.length);
        fFrames[frameIndex] = data;
    }

    public AnimationFrameData getFrameData(int frameIndex) {
        assert (0 <= frameIndex && frameIndex < fFrames.length);
        return fFrames[frameIndex];
    }

    public int getAnimationLength() {
        return fFrames.length;
    }

    public int getFrameRate() {
        return fFrameRate;
    }

    public int getWidth() {
        return fWidth;
    }

    public int getHeight() {
        return fHeight;
    }

    public void resetMask() {
        fMasks.clear();
    }

    public String getJSON() {
        StringBuilder ss = new StringBuilder();
        ss.append("{");
        ss.append("\"frameRate\":").append(fFrameRate).append(",");
        ss.append("\"width\":").append(fWidth).append(",");
        ss.append("\"height\":").append(fHeight).append(",");
        ss.append("\"instances\":[");
        for (int i = 0; i < fInstances.size(); ++i) {
            if (i != 0) ss.append(",");
            Instance instance = fInstances.get(i);
            ss.append(instance.getJSON());
        }
        ss.append("],");
        ss.append("\"frames\":[");
        for (int i = 0; i < fFrames.length; ++i) {
            if (i != 0) ss.append(",");
            ss.append(fFrames[i].getJSON());
        }
        ss.append("]}");
        return ss.toString();
    }
}
