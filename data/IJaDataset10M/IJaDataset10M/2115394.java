package dsr;

import com.jme.intersection.PickResults;
import com.jme.math.Ray;
import com.jme.scene.Geometry;
import com.jme.scene.TriMesh;
import dsr.models.ICollidable;

public final class MyPickResults extends PickResults {

    private TriMesh ignore;

    public MyPickResults(TriMesh _ignore) {
        setCheckDistance(true);
        ignore = _ignore;
    }

    public void setObjectToIgnore(TriMesh i) {
        ignore = i;
    }

    public void addPick(Ray ray, Geometry g) {
        if (g != ignore) {
            MyPickData p = new MyPickData(ray, g, true);
            if (ignore != null) {
                float dist = ignore.getWorldTranslation().distance(g.getWorldTranslation());
                dist -= g.getModelBound().getVolume();
                p.setDistance(dist);
            }
            super.addPickData(p);
        }
    }

    public GameObject getGameObject(int i) {
        Object o = super.getPickData(i).getTargetMesh();
        if (o instanceof ICollidable) {
            ICollidable ic = (ICollidable) super.getPickData(i).getTargetMesh();
            return (GameObject) ic.getOwner();
        } else {
            throw new RuntimeException("Not ICollideable!");
        }
    }

    public void processPick() {
    }
}
