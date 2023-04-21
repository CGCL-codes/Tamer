package de.grogra.imp3d.gl20;

import java.lang.Math;
import javax.vecmath.Matrix4d;
import de.grogra.imp3d.gl20.GL20Const;
import de.grogra.imp3d.gl20.GL20MeshServer;
import de.grogra.imp3d.gl20.GL20ResourceMeshMultiUser;
import de.grogra.imp3d.gl20.GL20ResourceShape;
import de.grogra.imp3d.PolygonArray;
import de.grogra.xl.util.FloatList;
import de.grogra.xl.util.IntList;

public class GL20ResourceShapeSphere extends GL20ResourceShape {

    /**
	 * the name of the mesh
	 */
    private static final String MESH_SPHERE_NAME = new String("SPHERE");

    /**
	 * radius attribute bit
	 */
    private static final int RADIUS = 0x1;

    /**
	 * all changes that was made since last update
	 */
    private int changeMask = GL20Const.ALL_CHANGED;

    /**
	 * radius attribute
	 */
    private float radius = 1.0f;

    /**
	 * mesh resource for this <code>GL20ResourceShapeSphere</code>
	 */
    private GL20ResourceMeshMultiUser mesh;

    /**
	 * create a <code>PolygonArray</code> for a sphere
	 *
	 * @param subDivisions number of sub divisions in horizontal and vertical way
	 * @return <code>PolygonArray</code> of a sphere OR <code>null</code> if
	 * <code>subDivisions</code> is out of range [3,..)
	 */
    private final PolygonArray createSpherePolygonArray(int subDivisions) {
        if (subDivisions < 3) return null;
        PolygonArray polygonArray = new PolygonArray();
        polygonArray.init(3);
        polygonArray.planar = true;
        polygonArray.edgeCount = 3;
        FloatList vertexList = polygonArray.vertices;
        FloatList uvList = polygonArray.uv;
        IntList polygonList = polygonArray.polygons;
        int vertexCount = 0;
        final double piOverSubDivisions = Math.PI / (double) subDivisions;
        final int SUB_DIVS = subDivisions;
        float x, y, radius, height;
        radius = (float) Math.sin(piOverSubDivisions);
        height = (float) Math.cos(piOverSubDivisions);
        for (int vStep = 0; vStep <= SUB_DIVS; vStep++) {
            if (vStep == 0) {
                polygonArray.setNormal(vertexCount++, 0.0f, 0.0f, 1.0f);
                uvList.add(0.0f);
                uvList.add(0.0f);
                vertexList.add(0.0f);
                vertexList.add(0.0f);
                vertexList.add(1.0f);
            } else if (vStep == SUB_DIVS) {
                polygonArray.setNormal(vertexCount++, 0.0f, 0.0f, -1.0f);
                uvList.add(0.0f);
                uvList.add(1.0f);
                vertexList.add(0.0f);
                vertexList.add(0.0f);
                vertexList.add(-1.0f);
                for (int hStep = 0; hStep <= SUB_DIVS; hStep++) {
                    polygonList.add(vertexCount - 1);
                    polygonList.add(vertexCount - SUB_DIVS - 1 + hStep);
                    polygonList.add(vertexCount - SUB_DIVS - 2 + hStep);
                }
            } else {
                final float oneOverSUB_DIVS = 1.0f / SUB_DIVS;
                final float texCoordV = oneOverSUB_DIVS * (float) vStep;
                final float angleV = (float) piOverSubDivisions * (float) vStep;
                radius = (float) Math.sin(angleV);
                height = (float) Math.cos(angleV);
                if (vStep == 1) {
                    for (int hStep = 0; hStep <= SUB_DIVS; hStep++) {
                        final float angleH = 2.0f * (float) piOverSubDivisions * (float) hStep;
                        x = (float) Math.cos(angleH) * radius;
                        y = (float) Math.sin(angleH) * radius;
                        polygonArray.setNormal(vertexCount++, x, y, height);
                        uvList.add(oneOverSUB_DIVS * (float) hStep);
                        uvList.add(texCoordV);
                        vertexList.add(x);
                        vertexList.add(y);
                        vertexList.add(height);
                        if (hStep > 0) {
                            polygonList.add(0);
                            polygonList.add(vertexCount - 2);
                            polygonList.add(vertexCount - 1);
                        }
                    }
                } else {
                    for (int hStep = 0; hStep <= SUB_DIVS; hStep++) {
                        final float angleH = 2.0f * (float) piOverSubDivisions * (float) hStep;
                        x = (float) Math.cos(angleH) * radius;
                        y = (float) Math.sin(angleH) * radius;
                        polygonArray.setNormal(vertexCount++, x, y, height);
                        uvList.add(oneOverSUB_DIVS * (float) hStep);
                        uvList.add(texCoordV);
                        vertexList.add(x);
                        vertexList.add(y);
                        vertexList.add(height);
                        if (hStep > 0) {
                            polygonList.add(vertexCount - 2);
                            polygonList.add(vertexCount - 1);
                            polygonList.add(vertexCount - SUB_DIVS - 3);
                            polygonList.add(vertexCount - SUB_DIVS - 3);
                            polygonList.add(vertexCount - 1);
                            polygonList.add(vertexCount - SUB_DIVS - 2);
                        }
                    }
                }
            }
        }
        return polygonArray;
    }

    public GL20ResourceShapeSphere() {
        super(GL20Resource.GL20RESOURCE_SHAPE_SPHERE);
        GL20MeshServer meshServer = GL20MeshServer.getInstance();
        mesh = meshServer.getMultiUserMeshByName(MESH_SPHERE_NAME);
        if (mesh == null) {
            mesh = new GL20ResourceMeshMultiUser(MESH_SPHERE_NAME);
            mesh.setPolygonArray(createSpherePolygonArray(20));
        }
        mesh.registerUser(this);
    }

    /**
	 * set the radius of this <code>GL20ResourceShapeSphere</code>
	 *
	 * @param radius the radius
	 */
    public final void setRadius(float radius) {
        if (this.radius != radius) {
            this.radius = radius;
            changeMask |= RADIUS;
        }
    }

    /**
	 * get the radius of this <code>GL20ResourceShapeSphere</code>
	 *
	 * @return the radius
	 */
    public final float getRadius() {
        return radius;
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
	 * check if this <code>GL20ResourceShapeSphere</code> is up to date.
	 *
	 * @return <code>true</code> - this <code>GL20ResourceShapeSphere</code> is up to date
	 * @see <code>GL20Resource</code>
	 */
    public boolean isUpToDate() {
        if (changeMask != 0) return false; else return super.isUpToDate();
    }

    /**
	 * update the state of this <code>GL20ResourceShapeSphere</code>
	 *
	 * @see <code>GL20Resource</code>
	 */
    public void update() {
        if (changeMask != 0) {
            setShapeTransformationMatrix(new Matrix4d((double) radius, 0.0, 0.0, 0.0, 0.0, (double) radius, 0.0, 0.0, 0.0, 0.0, (double) radius, 0.0, 0.0, 0.0, 0.0, 1.0));
            changeMask = 0;
        }
        super.update();
    }

    /**
	 * destroy this <code>GL20ResourceShapeSphere</code>
	 *
	 * @see <code>GL20Resource</code>
	 */
    public void destroy() {
        mesh.unregisterUser(this);
        mesh = null;
        super.destroy();
    }
}
