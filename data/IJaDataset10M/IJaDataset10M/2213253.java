package eu.cherrytree.paj.graphics.sprites;

import eu.cherrytree.paj.graphics.Camera;
import eu.cherrytree.paj.graphics.RenderData;
import eu.cherrytree.paj.graphics.Transformation;
import eu.cherrytree.paj.math.Vector3d;

public class CylindricalBillboard extends Billboard {

    private Vector3d dir = new Vector3d();

    private Vector3d axis;

    public CylindricalBillboard(RenderData trans, SpriteGeometry geometry, Vector3d axis) {
        super(trans, geometry);
        this.axis = new Vector3d(axis);
    }

    @Override
    public void update(Camera cam) {
        dir.set(cam.getLocation());
        dir.sub(getLocation());
        dir.project(axis);
        dir.add(getLocation());
        dir.sub(cam.getLocation());
        calculationTransform.setDirection(dir, Transformation.upVector);
        applyTransformation(calculationTransform);
    }
}
