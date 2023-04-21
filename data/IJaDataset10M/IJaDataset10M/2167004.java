package de.erdesignerng.visual.java3d;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 * Helper Class.
 */
public class Helper {

    private Helper() {
    }

    public static BranchGroup addElementAt(Node aShape, Vector3f aTranslation, float aScale) {
        BranchGroup theGroup = new BranchGroup();
        theGroup.setCapability(BranchGroup.ALLOW_DETACH);
        Transform3D theTransform = new Transform3D();
        theTransform.setTranslation(aTranslation);
        theTransform.setScale(aScale);
        TransformGroup theTransformGroup = new TransformGroup(theTransform);
        theTransformGroup.addChild(aShape);
        theGroup.addChild(theTransformGroup);
        return theGroup;
    }
}
