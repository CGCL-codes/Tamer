package si.mk.k3.kbrowser;

import java.util.List;
import javax.media.j3d.Appearance;
import javax.media.j3d.Group;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TexCoordGeneration;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import si.mk.k3.kbrowser.X3DField.FieldID;

public class XIndexedTriangleSet extends XGeometry {

    private IndexedTriangleArray m_triangleSet;

    private XAppearance m_appearance;

    private boolean m_solid;

    private int m_indices[];

    private Point3f vert[] = { new Point3f(0.0f, -1.5f, 0f), new Point3f(1.0f, -1.5f, 0f), new Point3f(1.0f, 0f, 0f), new Point3f(0.0f, 0f, 0f) };

    private int vertIndices[] = { 0, 1, 2, 0, 2, 3 };

    private int normalIndices[] = { 0, 0, 0, 1, 1, 1 };

    private XTextureCoordinateGenerator m_texCoordGenerator;

    public XIndexedTriangleSet(String name, boolean solid, int indices[]) {
        super(name);
        m_solid = solid;
        m_indices = indices;
    }

    @Override
    public void addGeometry(Group group) {
        Shape3D shape = new Shape3D();
        shape.setAppearance(getAppearance());
        shape.setGeometry(m_triangleSet);
        group.addChild(shape);
    }

    public void setCoordinates(Point3d points[]) {
        int trianglePtsLen = Math.max(m_indices.length / 3, points.length);
        m_triangleSet = new IndexedTriangleArray(trianglePtsLen, IndexedTriangleArray.COORDINATES | IndexedTriangleArray.NORMALS, m_indices.length);
        m_triangleSet.setCoordinateIndices(0, m_indices);
        m_triangleSet.setCoordinates(0, points);
        setNormals(points);
    }

    @Override
    public void setAppearance(XAppearance appearance) {
        m_appearance = appearance;
    }

    public void setTextureCoordinateGenerator(XTextureCoordinateGenerator coordGen) {
        m_texCoordGenerator = coordGen;
    }

    public void setTextureCoordinates(Point2d[] coords) {
    }

    public void updateField(FieldID fieldID) {
    }

    /**
     * Calcualtes and sets normals perpendicular to triangles. If the number of
     * indices mod 3 != 0, the last indices are ignored.
     * @param points - points of triangles defined by m_indices
     */
    private void setNormals(Point3d points[]) {
        int noOfNormals = m_indices.length / 3;
        int normalIndices[] = new int[m_indices.length];
        Vector3f normals[] = new Vector3f[noOfNormals];
        for (int i = 0; i < normals.length; i++) {
            normals[i] = new Vector3f();
        }
        Vector3d v01 = new Vector3d();
        Vector3d v02 = new Vector3d();
        Vector3d normal = new Vector3d();
        for (int idx = 0, i = 0; i < noOfNormals; i++) {
            v01.sub(points[m_indices[idx + 1]], points[m_indices[idx]]);
            v02.sub(points[m_indices[idx + 2]], points[m_indices[idx]]);
            normal.cross(v01, v02);
            normal.normalize();
            normals[i].set(normal);
            normalIndices[idx] = i;
            normalIndices[++idx] = i;
            normalIndices[++idx] = i;
            ++idx;
        }
        m_triangleSet.setNormalIndices(0, normalIndices);
        m_triangleSet.setNormals(0, normals);
    }

    private Appearance getAppearance() {
        Appearance appearance = m_appearance.getJ3DNodeComponent();
        if (m_appearance.isTextureSet() && m_texCoordGenerator != null) {
            appearance.setTexCoordGeneration(m_texCoordGenerator.getJ3DNodeComponent());
        }
        return appearance;
    }

    public void getShape3Ds(List<Shape3D> shapes) {
        Shape3D shape = new Shape3D();
        shape.setAppearance(getAppearance());
        shape.setGeometry(m_triangleSet);
        shapes.add(shape);
        System.out.println("XIndexedTriangleSet Shape3D fetched!");
    }

    public String toString() {
        return "XIndexedTriangleSet: " + super.toString();
    }
}
