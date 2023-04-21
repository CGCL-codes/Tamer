package com.jmex.jbullet.collision.shapes;

import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jmex.jbullet.collision.shapes.CollisionShape.ShapeTypes;
import com.jmex.jbullet.util.Converter;
import java.util.List;

/**
 * Basic mesh collision shape
 * @author normenhansen
 */
public class MeshCollisionShape extends CollisionShape {

    /**
     * creates a collision shape from the TriMesh leaf in the given node
     * @param node the node to get the TriMesh from
     */
    public MeshCollisionShape(Node node) {
        createCollisionMesh(node);
    }

    /**
     * creates a collision shape from the given TriMesh
     * @param mesh the TriMesh to use
     */
    public MeshCollisionShape(TriMesh mesh) {
        createCollisionMesh(mesh);
    }

    /**
     * creates a mesh that represents this node in the physics space. Can only be
     * used if this Node has one (and only one) TriMesh as a child.<br>
     */
    private void createCollisionMesh(Node node) {
        List<Spatial> children = node.getChildren();
        if (children.size() == 0) {
            throw (new UnsupportedOperationException("PhysicsNode has no children, cannot compute collision mesh"));
        } else if (children.size() > 1) {
            throw (new UnsupportedOperationException("Can only create mesh from one single trimesh as leaf in this node."));
        }
        if (node.getChild(0) instanceof TriMesh) {
            TriMesh mesh = (TriMesh) node.getChild(0);
            createCollisionMesh(mesh);
        } else {
            throw (new UnsupportedOperationException("No usable trimesh attached to this node!"));
        }
    }

    private void createCollisionMesh(TriMesh mesh) {
        cShape = new BvhTriangleMeshShape(Converter.convert(mesh), true);
        cShape.setLocalScaling(Converter.convert(mesh.getWorldScale()));
        type = ShapeTypes.MESH;
    }
}
