package parts;

import java.util.*;
import java.io.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import edit.Environment;
import edit.Palette;

/** PartGeometry holds the J3D geometry representing a type of part, including any sub parts */
public class FlexiConnectorSceneGraph extends BranchGroup implements JLugSceneGraph {

    private PartDefFlexiConnector partDef;

    private TransformGroup tg;

    private Point3d ptLower, ptLowerExclStuds;

    private Point3d ptUpper, ptUpperExclStuds;

    public FlexiConnectorSceneGraph(PartDefFlexiConnector partDef) {
        this.partDef = partDef;
    }

    public BranchGroup createInstance() throws IOException, FileNotFoundException {
        return createInstance((Appearance) null);
    }

    public BranchGroup createInstance(Appearance appearance) throws IOException, FileNotFoundException {
        if (appearance == null) {
            appearance = new Appearance();
        }
        BranchGroup bg = new BranchGroup();
        TransformGroup tg = new TransformGroup();
        NURB nurb = new NURB(partDef);
        Shape3D curve = nurb.createCurveGeometry();
        Shape3D surface = nurb.createSurfaceGeometry();
        tg.addChild(curve);
        tg.addChild(surface);
        bg.addChild(tg);
        curve.setAppearance(appearance);
        bg.setPickable(true);
        tg.setPickable(true);
        bg.setCapability(Group.ALLOW_CHILDREN_READ);
        tg.setCapability(Group.ALLOW_CHILDREN_READ);
        return bg;
    }

    public BranchGroup createInstance(Color3f colour) throws IOException, FileNotFoundException {
        return createInstance(colour, 0.0f);
    }

    public BranchGroup createInstance(Color3f colour, float alpha) throws IOException, FileNotFoundException {
        BranchGroup bg = new BranchGroup();
        TransformGroup tg = new TransformGroup();
        NURB nurb = new NURB(partDef);
        Shape3D curve = nurb.createCurveGeometry();
        Shape3D surface = nurb.createSurfaceGeometry();
        tg.addChild(curve);
        tg.addChild(surface);
        bg.addChild(tg);
        bg.setCapability(ALLOW_PICKABLE_WRITE);
        bg.setCapability(Group.ALLOW_CHILDREN_READ);
        tg.setCapability(Group.ALLOW_CHILDREN_READ);
        Appearance appearance = surface.getAppearance();
        appearance.getMaterial().setAmbientColor(colour);
        System.out.println("Setting colour to: " + colour.toString());
        appearance.getMaterial().setDiffuseColor(colour);
        appearance.getTransparencyAttributes().setTransparency(alpha);
        if (alpha == 0.0f) {
            appearance.getTransparencyAttributes().setTransparencyMode(TransparencyAttributes.NONE);
        } else {
            appearance.getTransparencyAttributes().setTransparencyMode(Palette.getCurrentTransparencyMode());
        }
        bg.setPickable(true);
        tg.setPickable(true);
        return bg;
    }
}
