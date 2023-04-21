package org.xteam.sled.model;

import java.util.ArrayList;
import java.util.List;

public class LatentSequence extends BaseSequence<ILatentSequent> {

    public LatentSequence(boolean hasPrefix, List<ILatentSequent> elements, boolean hasSuffix) {
        super(hasPrefix, elements, hasSuffix);
    }

    public LatentSequence(LatentLabel seq) {
        super(seq);
    }

    public LabelledSequence injectLatent() {
        List<ILabelledSequent> n = new ArrayList<ILabelledSequent>();
        for (ILatentSequent e : elements) {
            n.add(e.injectLatent());
        }
        return new LabelledSequence(hasPrefix, hasSuffix, n);
    }

    @Override
    protected BaseSequence<ILatentSequent> createSequence() {
        return new LatentSequence(false, new ArrayList<ILatentSequent>(), false);
    }

    @Override
    public ISequence createSequence(boolean hasPrefix, boolean hasSuffix, List<ISequent> elements) {
        List<ILatentSequent> newElements = new ArrayList<ILatentSequent>();
        for (ISequent e : elements) {
            newElements.add((ILatentSequent) e);
        }
        return new LatentSequence(hasPrefix, newElements, hasSuffix);
    }
}
