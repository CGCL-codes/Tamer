package de.mpiwg.vspace.java.transformation.ids;

import de.mpiwg.vspace.metamodel.transformed.TransformedPackage;

public class BranchingPointIDSetter extends SlideIDSetter {

    @Override
    public String getRegisteredClass() {
        return TransformedPackage.Literals.BRANCHING_POINT.getName();
    }
}
