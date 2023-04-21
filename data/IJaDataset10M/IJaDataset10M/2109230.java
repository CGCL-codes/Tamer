package org.gdi3d.bgutils;

import java.util.Enumeration;
import java.util.Vector;
import javax.media.ding3d.Billboard;
import javax.media.ding3d.BranchGroup;
import javax.media.ding3d.DistanceLOD;
import javax.media.ding3d.Geometry;
import javax.media.ding3d.GeometryArray;
import javax.media.ding3d.Group;
import javax.media.ding3d.IndexedQuadArray;
import javax.media.ding3d.IndexedTriangleArray;
import javax.media.ding3d.Leaf;
import javax.media.ding3d.Link;
import javax.media.ding3d.Node;
import javax.media.ding3d.RotPosPathInterpolator;
import javax.media.ding3d.Shape3D;
import javax.media.ding3d.SharedGroup;
import javax.media.ding3d.Text3D;
import javax.media.ding3d.Transform3D;
import javax.media.ding3d.TransformGroup;
import javax.media.ding3d.vecmath.Point3d;
import javax.media.ding3d.vecmath.Point3f;
import javax.media.ding3d.vecmath.Vector3f;

public class BranchGroupAnalyzer {

    private Vector<Geometry> checked_geometries = new Vector<Geometry>();

    public void checkBranchGroup(BranchGroup branchgroup) {
        for (int i = 0; i < branchgroup.numChildren(); i++) {
            Node node = branchgroup.getChild(i);
            checkNode(node, 0);
        }
    }

    private void checkNode(Node node, int level) {
        log("checkNode " + node.hashCode(), level);
        if (node instanceof Group) checkGroup((Group) node, level); else if (node instanceof Leaf) checkLeaf((Leaf) node, level); else System.out.println("BranchGroupAnalyzer node not supported: " + node.getClass().getName());
    }

    private void log(String s, int level) {
        for (int i = 0; i < level; i++) System.out.print("|-- ");
        System.out.println(s);
    }

    private void checkGroup(Group group, int level) {
        log("checkGroup " + group.hashCode(), level);
        if (group instanceof TransformGroup) {
            TransformGroup tgroup = (TransformGroup) group;
            Transform3D transform = new Transform3D();
            tgroup.getTransform(transform);
            log("transform: " + transform.hashCode() + "\n" + transform, level);
        }
        for (int i = 0; i < group.numChildren(); i++) {
            Node node = group.getChild(i);
            checkNode(node, level + 1);
        }
    }

    private void checkLeaf(Leaf leaf, int level) {
        log("checkLeaf " + leaf.hashCode(), level);
        if (leaf instanceof Shape3D) checkShape3D((Shape3D) leaf, level); else if (leaf instanceof DistanceLOD) checkDistanceLOD((DistanceLOD) leaf, level); else if (leaf instanceof Link) checkLink((Link) leaf, level); else if (leaf instanceof Billboard) checkBillboard((Billboard) leaf, level); else if (leaf instanceof RotPosPathInterpolator) checkRotPosPathInterpolator((RotPosPathInterpolator) leaf, level); else System.out.println("BranchGroupAnalyzer.checkLeaf leaf not supported: " + leaf.getClass().getName());
    }

    private void checkLink(Link link, int level) {
        log("checkLink " + link.hashCode(), level);
        SharedGroup sharedGroup = link.getSharedGroup();
        Boolean checked = (Boolean) sharedGroup.getUserData();
        if (checked == null || checked == false) {
            sharedGroup.setUserData(new Boolean(true));
        }
    }

    private void checkBillboard(Billboard billboard, int level) {
        log("checkBillboard " + billboard.hashCode(), level);
        Point3f position = new Point3f();
        billboard.getRotationPoint(position);
    }

    private void checkRotPosPathInterpolator(RotPosPathInterpolator leaf, int level) {
        log("checkRotPosPathInterpolator " + leaf.hashCode(), level);
    }

    private void checkDistanceLOD(DistanceLOD distanceLOD, int level) {
        log("checkDistanceLOD " + distanceLOD.hashCode(), level);
        Point3f position = new Point3f();
        distanceLOD.getPosition(position);
    }

    private void checkShape3D(Shape3D shape, int level) {
        log("checkShape3D " + shape.hashCode(), level);
        Enumeration<Geometry> en = shape.getAllGeometries();
        while (en.hasMoreElements()) {
            Geometry geo = en.nextElement();
            if (!checked_geometries.contains(geo)) {
                if (geo != null) {
                    if (geo instanceof GeometryArray) {
                        GeometryArray ga = (GeometryArray) geo;
                        checkGeometryArray(shape, ga, level);
                    } else if (geo instanceof Text3D) {
                        Text3D text3d = (Text3D) geo;
                        checkText3D(text3d, level);
                    } else System.out.println("ChangeOffset checkShape3D Geometry not supported: " + geo.getClass().getName());
                    checked_geometries.add(geo);
                }
            }
        }
    }

    private void checkText3D(Text3D text3d, int level) {
        log("checkText3D " + text3d.hashCode(), level);
        if (!(text3d.isLive() || text3d.isCompiled())) {
            Point3f position = new Point3f();
            text3d.getPosition(position);
        }
    }

    private void checkIndexedTriangleArray(Shape3D shape, IndexedTriangleArray ita, int level) {
        if (!(ita.isLive() || ita.isCompiled())) {
            ita.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
            int vertexCount = ita.getVertexCount();
            int indexCount = ita.getIndexCount();
            int vertexformat = ita.getVertexFormat();
            boolean hasCoordinates = (vertexformat & GeometryArray.COORDINATES) != 0;
            boolean hasNormals = (vertexformat & GeometryArray.NORMALS) != 0;
            Transform3D transform3d = new Transform3D();
            shape.getLocalToVworld(transform3d);
            if (hasCoordinates) {
                Point3d[] coordinates = new Point3d[vertexCount];
                for (int i = 0; i < vertexCount; i++) coordinates[i] = new Point3d();
                ita.getCoordinates(0, coordinates);
                Vector3f[] normals = null;
                if (hasNormals) {
                    normals = new Vector3f[vertexCount];
                    for (int i = 0; i < vertexCount; i++) normals[i] = new Vector3f();
                    ita.getNormals(0, normals);
                }
                log("checkIndexedTriangleArray num_vert " + vertexCount, level);
                for (int i = 0; i < vertexCount; i++) {
                    Point3d coord_local = coordinates[i];
                    Point3d coord_world = new Point3d(coord_local);
                    transform3d.transform(coord_world);
                }
                int[] coordinateIndices = new int[indexCount];
                ita.getCoordinateIndices(0, coordinateIndices);
                int numTriangles = indexCount / 3;
                int c = 0;
                for (int i = 0; i < numTriangles; i++) {
                    int[] tri_indices = new int[3];
                    tri_indices[0] = coordinateIndices[c++];
                    tri_indices[1] = coordinateIndices[c++];
                    tri_indices[2] = coordinateIndices[c++];
                    log("checkIndexedTriangleArray tri " + tri_indices[0] + " " + tri_indices[1] + " " + tri_indices[2], level);
                    Point3d[] coords_world = new Point3d[3];
                    for (int j = 0; j < 3; j++) {
                        coords_world[j] = new Point3d(coordinates[tri_indices[j]]);
                        transform3d.transform(coords_world[j]);
                    }
                    log("checkIndexedTriangleArray tri " + coords_world[0] + " " + coords_world[1] + " " + coords_world[2], level);
                }
            }
        }
    }

    private void checkIndexedQuadArray(Shape3D shape, IndexedQuadArray iqa, int level) {
        if (!(iqa.isLive() || iqa.isCompiled())) {
            iqa.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
            int vertexCount = iqa.getVertexCount();
            int indexCount = iqa.getIndexCount();
            int vertexformat = iqa.getVertexFormat();
            boolean hasCoordinates = (vertexformat & GeometryArray.COORDINATES) != 0;
            boolean hasNormals = (vertexformat & GeometryArray.NORMALS) != 0;
            Transform3D transform3d = new Transform3D();
            shape.getLocalToVworld(transform3d);
            if (hasCoordinates) {
                Point3d[] coordinates = new Point3d[vertexCount];
                for (int i = 0; i < vertexCount; i++) coordinates[i] = new Point3d();
                iqa.getCoordinates(0, coordinates);
                Vector3f[] normals = null;
                if (hasNormals) {
                    normals = new Vector3f[vertexCount];
                    for (int i = 0; i < vertexCount; i++) normals[i] = new Vector3f();
                    iqa.getNormals(0, normals);
                }
                log("checkIndexedQuadArray num_vert " + vertexCount, level);
                for (int i = 0; i < vertexCount; i++) {
                    Point3d coord_local = coordinates[i];
                    Point3d coord_world = new Point3d(coord_local);
                    transform3d.transform(coord_world);
                }
                int[] coordinateIndices = new int[indexCount];
                iqa.getCoordinateIndices(0, coordinateIndices);
                int numQuads = indexCount / 4;
                int c = 0;
                for (int i = 0; i < numQuads; i++) {
                    int[] quad_indices = new int[4];
                    quad_indices[0] = coordinateIndices[c++];
                    quad_indices[1] = coordinateIndices[c++];
                    quad_indices[2] = coordinateIndices[c++];
                    quad_indices[3] = coordinateIndices[c++];
                    log("checkIndexedQuadArray quad " + quad_indices[0] + " " + quad_indices[1] + " " + quad_indices[2] + " " + quad_indices[3], level);
                    Point3d[] coords_world = new Point3d[4];
                    for (int j = 0; j < 4; j++) {
                        coords_world[j] = new Point3d(coordinates[quad_indices[j]]);
                        transform3d.transform(coords_world[j]);
                    }
                    log("checkIndexedQuadArray quad " + coords_world[0] + " " + coords_world[1] + " " + coords_world[2] + " " + coords_world[3], level);
                }
            }
        }
    }

    private void checkGeometryArray(Shape3D shape, GeometryArray ga, int level) {
        log("checkGeometryArray " + ga.hashCode(), level);
        if (ga instanceof IndexedTriangleArray) {
            checkIndexedTriangleArray(shape, (IndexedTriangleArray) ga, level);
        } else if (ga instanceof IndexedQuadArray) {
            checkIndexedQuadArray(shape, (IndexedQuadArray) ga, level);
        } else {
            log("checkGeometryArray Object not supported: " + ga.getClass().getName(), level);
        }
    }
}
