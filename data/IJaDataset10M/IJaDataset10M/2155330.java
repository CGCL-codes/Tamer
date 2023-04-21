package de.grogra.imp3d.gl20;

import de.grogra.imp3d.gl20.GL20Const;
import de.grogra.imp3d.gl20.GL20ResourceMeshSingleUser;
import de.grogra.imp3d.gl20.GL20ResourceShape;
import de.grogra.imp3d.PolygonArray;

public class GL20ResourceShapePolygons extends GL20ResourceShape {

    /**
	 * all changes that was made since last update
	 */
    private int changeMask = GL20Const.ALL_CHANGED;

    /**
	 * mesh resource of this <code>GL20ResourceShapeParallelogram</code>
	 */
    private GL20ResourceMeshSingleUser mesh = null;

    public GL20ResourceShapePolygons() {
        super(GL20Resource.GL20RESOURCE_SHAPE_POLYGONS);
        mesh = new GL20ResourceMeshSingleUser();
    }

    /**
	 * set the <code>PolygonArray</code> of this <code>GL20ResourceShapePolygons</code>
	 *
	 * @param polygonArray the <code>PolygonArray</code>
	 */
    public final void setPolygonArray(PolygonArray polygonArray) {
        mesh.setPolygonArray(polygonArray);
    }

    /**
	 * tell this <code>GL20ResourceShapePolygons</code> that it should apply the
	 * geometry to the <code>GL20GfxServer</code>
	 */
    public void applyGeometry() {
        super.applyGeometry();
        mesh.draw();
    }

    /**
	 * check if this <code>GL20ResourceShapePolygons</code> is up to date.
	 *
	 * @return <code>true</code> - this <code>GL20ResourceShapePolygons</code> is up to date
	 * @see <code>GL20Resource</code>
	 */
    public boolean isUpToDate() {
        if ((changeMask != 0) || (mesh.isUpToDate() == false)) return false; else return super.isUpToDate();
    }

    /**
	 * update the state of this <code>GL20ResourceShapePolygons</code>
	 *
	 * @see <code>GL20Resource</code>
	 */
    public void update() {
        if (changeMask != 0) {
            changeMask = 0;
        }
        super.update();
        mesh.update();
    }

    /**
	 * destroy this <code>GL20ResourceShapePolygons</code>
	 *
	 * @see <code>GL20Resource</code>
	 */
    public void destroy() {
        if (mesh != null) mesh.destroy();
        super.destroy();
    }
}
