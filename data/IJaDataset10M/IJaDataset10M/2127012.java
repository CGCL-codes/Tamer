package simbad.sim;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector3d;

/**
 * An Arch object to put in  the  environement.
 */
public class Arch extends BlockWorldCompositeObject {

    public Arch(Vector3d pos, EnvironmentDescription wd) {
        create3D(wd);
        translateTo(pos);
    }

    void create3D(EnvironmentDescription wd) {
        super.create3D();
        Box b1 = new Box(new Vector3d(-1.5, 0, 0), new Vector3f(1, 1.5f, 1), wd);
        Box b2 = new Box(new Vector3d(1.5, 0, 0), new Vector3f(1, 1.5f, 1), wd);
        Box b3 = new Box(new Vector3d(0, 1.5f, 0), new Vector3f(4, 0.5f, 1), wd);
        b1.setColor(wd.archColor);
        b2.setColor(wd.archColor);
        b3.setColor(wd.archColor);
        addComponent(b1);
        addComponent(b2);
        addComponent(b3);
    }
}
