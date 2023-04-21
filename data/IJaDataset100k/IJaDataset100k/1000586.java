package org.xith3d.loaders.models.impl.bsp;

import java.util.ArrayList;
import java.util.List;
import org.xith3d.collider.BiTreeCollider;
import org.xith3d.collider.ColliderGeometry;
import org.xith3d.collider.ColliderNode;
import org.xith3d.loaders.models.base.Scene;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.TransformGroup;

/**
 * Contains a whole loaded BSP scene.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class BSPScene extends Scene<TransformGroup> {

    private BSPClusterManager clusterManager;

    protected void setClusterManager(BSPClusterManager clusterManager) {
        this.clusterManager = clusterManager;
    }

    public BSPClusterManager getClusterManager() {
        return (clusterManager);
    }

    @Override
    protected void addShapeNode(Shape3D face) {
        super.addShapeNode(face);
    }

    @SuppressWarnings("unchecked")
    public List<ColliderNode> getColliders() {
        List<ColliderNode> colliders = new ArrayList<ColliderNode>();
        for (Shape3D face : getShapeNodes()) {
            ColliderGeometry cg = new ColliderGeometry();
            cg.setModel(face);
            BiTreeCollider collider = new BiTreeCollider();
            collider.build(cg);
            ColliderNode cn = new ColliderNode(face, ColliderNode.CT_GEOMETRY, ColliderNode.CT_GEOMETRY, false, collider);
            cn.setTwoSided(true);
            colliders.add(cn);
        }
        return (colliders);
    }

    protected BSPScene() {
        super();
    }
}
