package de.mpiwg.vspace.generation.control.internal;

import org.eclipse.emf.ecore.EObject;

public class FeatureProperty {

    public String prefix;

    public EObject linkedObject;

    public FeatureProperty(String prefix, EObject object) {
        this.prefix = prefix;
        this.linkedObject = object;
    }
}
