package com.sodad.metacode.ui;

import gate.AnnotationSet;

public class AnnotTreeASNode extends AnnotTreeNode {

    AnnotationSet itsData;

    public AnnotTreeASNode(AnnotationSet data) {
        itsData = data;
    }

    public String toString() {
        return itsData.getName();
    }
}
