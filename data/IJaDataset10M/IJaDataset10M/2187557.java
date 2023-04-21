package de.grogra.imp3d.gl20;

import javax.vecmath.Matrix4d;
import de.grogra.imp3d.gl20.GL20Const;
import de.grogra.imp3d.gl20.GL20MeshServer;
import de.grogra.imp3d.gl20.GL20ResourceMeshMultiUser;
import de.grogra.imp3d.gl20.GL20ResourceShape;
import de.grogra.imp3d.PolygonArray;
import de.grogra.xl.util.FloatList;
import de.grogra.xl.util.IntList;

public class GL20ResourceShapeBox extends GL20ResourceShape {

    /**
	 * the name of the mesh
	 */
    private static final String MESH_BOX_NAME = new String("BOX");

    /**
	 * halfWidth attribute bit
	 */
    private static final int HALF_WIDTH = 0x1;

    /**
	 * halfLength attribute bit
	 */
    private static final int HALF_LENGTH = 0x2;

    /**
	 * height attribute bit
	 */
    private static final int HEIGHT = 0x4;

    /**
	 * all changes that was made since last update
	 */
    private int changeMask = GL20Const.ALL_CHANGED;

    /**
	 * halfWidth attribute
	 */
    private float halfWidth = 0.5f;

    /**
	 * halfLength attribute
	 */
    private float halfLength = 0.5f;

    /**
	 * height attribute
	 */
    private float height = 1.0f;

    /**
	 * mesh resource for this <code>GL20ResourceShapeBox</code>
	 */
    private GL20ResourceMeshMultiUser mesh;

    public GL20ResourceShapeBox() {
        super(GL20Resource.GL20RESOURCE_SHAPE_BOX);
        GL20MeshServer meshServer = GL20MeshServer.getInstance();
        mesh = meshServer.getMultiUserMeshByName(MESH_BOX_NAME);
        if (mesh == null) {
            mesh = new GL20ResourceMeshMultiUser(MESH_BOX_NAME);
            PolygonArray polygonArray = new PolygonArray();
            polygonArray.init(3);
            polygonArray.planar = true;
            polygonArray.edgeCount = 4;
            FloatList vertexList = polygonArray.vertices;
            FloatList uvList = polygonArray.uv;
            IntList polygonList = polygonArray.polygons;
            polygonArray.setNormal(0, 0.0f, 0.0f, -1.0f);
            polygonArray.setNormal(1, 0.0f, 0.0f, -1.0f);
            polygonArray.setNormal(2, 0.0f, 0.0f, -1.0f);
            polygonArray.setNormal(3, 0.0f, 0.0f, -1.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            uvList.add(0.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            uvList.add(1.0f);
            vertexList.add(-1.0f);
            vertexList.add(-1.0f);
            vertexList.add(0.0f);
            vertexList.add(-1.0f);
            vertexList.add(1.0f);
            vertexList.add(0.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(0.0f);
            vertexList.add(1.0f);
            vertexList.add(-1.0f);
            vertexList.add(0.0f);
            polygonList.add(0);
            polygonList.add(1);
            polygonList.add(2);
            polygonList.add(3);
            polygonArray.setNormal(4, 0.0f, 0.0f, 1.0f);
            polygonArray.setNormal(5, 0.0f, 0.0f, 1.0f);
            polygonArray.setNormal(6, 0.0f, 0.0f, 1.0f);
            polygonArray.setNormal(7, 0.0f, 0.0f, 1.0f);
            uvList.add(0.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            uvList.add(1.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            vertexList.add(-1.0f);
            vertexList.add(-1.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(-1.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(-1.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            polygonList.add(4);
            polygonList.add(5);
            polygonList.add(6);
            polygonList.add(7);
            polygonArray.setNormal(8, -1.0f, 0.0f, 0.0f);
            polygonArray.setNormal(9, -1.0f, 0.0f, 0.0f);
            polygonArray.setNormal(10, -1.0f, 0.0f, 0.0f);
            polygonArray.setNormal(11, -1.0f, 0.0f, 0.0f);
            uvList.add(1.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            uvList.add(1.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            uvList.add(0.0f);
            uvList.add(0.0f);
            vertexList.add(-1.0f);
            vertexList.add(-1.0f);
            vertexList.add(0.0f);
            vertexList.add(-1.0f);
            vertexList.add(-1.0f);
            vertexList.add(1.0f);
            vertexList.add(-1.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(-1.0f);
            vertexList.add(1.0f);
            vertexList.add(0.0f);
            polygonList.add(8);
            polygonList.add(9);
            polygonList.add(10);
            polygonList.add(11);
            polygonArray.setNormal(12, 1.0f, 0.0f, 0.0f);
            polygonArray.setNormal(13, 1.0f, 0.0f, 0.0f);
            polygonArray.setNormal(14, 1.0f, 0.0f, 0.0f);
            polygonArray.setNormal(15, 1.0f, 0.0f, 0.0f);
            uvList.add(0.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            uvList.add(1.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(-1.0f);
            vertexList.add(0.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(0.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(-1.0f);
            vertexList.add(1.0f);
            polygonList.add(12);
            polygonList.add(13);
            polygonList.add(14);
            polygonList.add(15);
            polygonArray.setNormal(16, 0.0f, 1.0f, 0.0f);
            polygonArray.setNormal(17, 0.0f, 1.0f, 0.0f);
            polygonArray.setNormal(18, 0.0f, 1.0f, 0.0f);
            polygonArray.setNormal(19, 0.0f, 1.0f, 0.0f);
            uvList.add(1.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            uvList.add(1.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            uvList.add(0.0f);
            uvList.add(0.0f);
            vertexList.add(-1.0f);
            vertexList.add(1.0f);
            vertexList.add(0.0f);
            vertexList.add(-1.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(1.0f);
            vertexList.add(0.0f);
            polygonList.add(16);
            polygonList.add(17);
            polygonList.add(18);
            polygonList.add(19);
            polygonArray.setNormal(20, 0.0f, -1.0f, 0.0f);
            polygonArray.setNormal(21, 0.0f, -1.0f, 0.0f);
            polygonArray.setNormal(22, 0.0f, -1.0f, 0.0f);
            polygonArray.setNormal(23, 0.0f, -1.0f, 0.0f);
            uvList.add(0.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            uvList.add(1.0f);
            uvList.add(0.0f);
            uvList.add(1.0f);
            vertexList.add(-1.0f);
            vertexList.add(-1.0f);
            vertexList.add(0.0f);
            vertexList.add(1.0f);
            vertexList.add(-1.0f);
            vertexList.add(0.0f);
            vertexList.add(1.0f);
            vertexList.add(-1.0f);
            vertexList.add(1.0f);
            vertexList.add(-1.0f);
            vertexList.add(-1.0f);
            vertexList.add(1.0f);
            polygonList.add(20);
            polygonList.add(21);
            polygonList.add(22);
            polygonList.add(23);
            mesh.setPolygonArray(polygonArray);
        }
        mesh.registerUser(this);
    }

    /**
	 * set the half width of this <code>GL20ResourceShapeBox</code>
	 *
	 * @param halfWidth the half width
	 */
    public final void setHalfWidth(float halfWidth) {
        if (this.halfWidth != halfWidth) {
            this.halfWidth = halfWidth;
            changeMask |= HALF_WIDTH;
        }
    }

    /**
	 * get the half width of this <code>GL20ResourceShapeBox</code>
	 *
	 * @return the half width
	 */
    public final float getHalfWidth() {
        return halfWidth;
    }

    /**
	 * set the half length of this <code>GL20ResourceShapeBox</code>
	 *
	 * @param halfLength the half length
	 */
    public final void setHalfLength(float halfLength) {
        if (this.halfLength != halfLength) {
            this.halfLength = halfLength;
            changeMask |= HALF_LENGTH;
        }
    }

    /**
	 * get the half length of this <code>GL20ResourceShapeBox</code>
	 *
	 * @return the half length
	 */
    public final float getHalfLength() {
        return halfLength;
    }

    /**
	 * set the height of this <code>GL20ResourceShapeBox</code>
	 *
	 * @param height the height
	 */
    public final void setHeight(float height) {
        if (this.height != height) {
            this.height = height;
            changeMask |= HEIGHT;
        }
    }

    /**
	 * get the height of this <code>GL20ResourceShapeBox</code>
	 *
	 * @return the height
	 */
    public final float getHeight() {
        return height;
    }

    /**
	 * tell this <code>GL20ResourceShape</code> that it should apply the
	 * geometry to the <code>GL20GfxServer</code>
	 */
    public void applyGeometry() {
        super.applyGeometry();
        mesh.draw();
    }

    /**
	 * check if this <code>GL20ResourceShapeBox</code> is up to date.
	 *
	 * @return <code>true</code> - this <code>GL20ResourceShapeBox</code> is up to date
	 * @see <code>GL20Resource</code>
	 */
    public boolean isUpToDate() {
        if ((changeMask != 0)) return false; else return super.isUpToDate();
    }

    /**
	 * update this <code>GL20ResourceShapeBox</code> is up to date.
	 *
	 * @see <code>GL20Resource</code>
	 */
    public void update() {
        if (changeMask != 0) {
            setShapeTransformationMatrix(new Matrix4d((double) halfWidth, 0.0, 0.0, 0.0, 0.0, (double) halfLength, 0.0, 0.0, 0.0, 0.0, (double) height, 0.0, 0.0, 0.0, 0.0, 1.0));
            changeMask = 0;
        }
        super.update();
    }

    /**
	 * destroy this <code>GL20ResourceShapeBox</code>
	 *
	 * @see <code>GL20Resource</code>
	 */
    public void destroy() {
        mesh.unregisterUser(this);
        mesh = null;
        super.destroy();
    }
}
