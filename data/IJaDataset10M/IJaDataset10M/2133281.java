package jat.vr;

import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;

public class jat_behavior {

    public static xyz_Steering xyz_Behavior;

    private static float TRANSLATE = 1.e2f;

    public static void behavior(BranchGroup BG_root, BranchGroup BG_vp, BoundingSphere bounds) {
        xyz_Behavior = new xyz_Steering(jat_view.TG_vp);
        xyz_Behavior.setSchedulingBounds(bounds);
        BG_vp.addChild(xyz_Behavior);
        MouseRotate RotationBehavior = new MouseRotate(MouseBehavior.INVERT_INPUT);
        RotationBehavior.setSchedulingBounds(bounds);
        RotationBehavior.setFactor(0.01);
        RotationBehavior.setTransformGroup(jat_view.TG_vp);
        BG_vp.addChild(RotationBehavior);
        MouseZoom ZoomBehavior = new MouseZoom(MouseBehavior.INVERT_INPUT);
        ZoomBehavior.setSchedulingBounds(bounds);
        ZoomBehavior.setFactor(TRANSLATE);
        ZoomBehavior.setTransformGroup(jat_view.TG_vp);
        BG_vp.addChild(ZoomBehavior);
    }

    public void set_translate(float translate) {
    }
}
