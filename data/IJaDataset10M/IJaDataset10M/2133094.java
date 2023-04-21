package syn3d.builtin.xith3d;

import syn3d.base.ActiveNode;
import syn3d.nodes.GroupNode;
import syn3d.nodes.NodeResourcesManager;
import syn3d.nodes.RootNode;
import syn3d.nodes.xith3d.BranchGroupNodeXith3D;
import syn3d.nodes.xith3d.CubeNodeXith3D;
import syn3d.nodes.xith3d.SceneNodeXith3D;
import syn3d.nodes.xith3d.SphereNodeXith3D;
import syn3d.nodes.xith3d.SwitchNodeXith3D;
import syn3d.nodes.xith3d.TransformGroupNodeXith3D;
import syn3d.builtin.ControlledSyn3DBuiltin;

/**
 * This plugin provides all basic types for a Xith3D scene graph
 * 
 * @author Nicolas Brodu
 */
public class Xith3DSyn3DBuiltin extends ControlledSyn3DBuiltin {

    protected static String[] nodeTypes = { NodeResourcesManager.getResources().getString("Scene"), NodeResourcesManager.getResources().getString("BranchGroup"), NodeResourcesManager.getResources().getString("Switch"), NodeResourcesManager.getResources().getString("TransformGroup"), NodeResourcesManager.getResources().getString("Sphere"), NodeResourcesManager.getResources().getString("Cube") };

    public String[] getNodes() {
        return nodeTypes;
    }

    public boolean canCreate(String node, ActiveNode parent) {
        if (node == null) return false;
        if (node.equals(NodeResourcesManager.getResources().getString("Scene"))) return parent instanceof RootNode; else if (node.equals(NodeResourcesManager.getResources().getString("BranchGroup"))) return parent instanceof GroupNode; else if (node.equals(NodeResourcesManager.getResources().getString("Switch"))) return parent instanceof GroupNode; else if (node.equals(NodeResourcesManager.getResources().getString("TransformGroup"))) return parent instanceof GroupNode; else if (node.equals(NodeResourcesManager.getResources().getString("Sphere"))) return parent instanceof GroupNode; else if (node.equals(NodeResourcesManager.getResources().getString("Cube"))) return parent instanceof GroupNode;
        return false;
    }

    public ActiveNode create(String node, ActiveNode parent) {
        if (node == null) return null;
        if (node.equals(NodeResourcesManager.getResources().getString("Scene"))) return new SceneNodeXith3D(parent, pluginManager); else if (node.equals(NodeResourcesManager.getResources().getString("BranchGroup"))) return new BranchGroupNodeXith3D(parent); else if (node.equals(NodeResourcesManager.getResources().getString("TransformGroup"))) return new TransformGroupNodeXith3D(parent); else if (node.equals(NodeResourcesManager.getResources().getString("Switch"))) return new SwitchNodeXith3D(parent); else if (node.equals(NodeResourcesManager.getResources().getString("Sphere"))) return new SphereNodeXith3D(parent); else if (node.equals(NodeResourcesManager.getResources().getString("Cube"))) return new CubeNodeXith3D(parent);
        return null;
    }
}
