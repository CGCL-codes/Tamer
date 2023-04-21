package homura.hde.main.core.scene;

import homura.hde.core.renderer.Renderer;
import homura.hde.core.scene.Node;
import homura.hde.core.scene.SceneElement;
import homura.hde.core.scene.Spatial;
import homura.hde.core.scene.TriMesh;
import homura.hde.core.scene.UserDataManager;
import homura.hde.core.scene.state.RenderState;
import homura.hde.util.export.InputCapsule;
import homura.hde.util.export.JMEExporter;
import homura.hde.util.export.JMEImporter;
import homura.hde.util.export.OutputCapsule;
import java.io.IOException;

/**
 * SharedNode allows the sharing of data
 * @author Mark Powell
 *
 */
public class SharedNode extends Node {

    private static final long serialVersionUID = 1L;

    private boolean updatesCollisionTree;

    public SharedNode() {
    }

    /**
     * Constructor creates a new <code>SharedNode</code> object.
     * 
     * @param name
     *            the name of this shared mesh.
     * @param target
     *            the Node to share the data.
     */
    public SharedNode(String name, Node target) {
        super(name);
        setTarget(target);
    }

    @Override
    public void draw(Renderer r) {
        super.draw(r);
    }

    /**
	 * <code>setTarget</code> sets the shared data.
	 * 
	 * @param target
	 *            the Node to share the data.
	 */
    private void setTarget(Node target) {
        if (target.getChildren() != null) for (int i = 0; i < target.getChildren().size(); i++) {
            processTarget(this, target.getChild(i));
        }
        copyNode(target, this);
        UserDataManager.getInstance().bind(this, target);
    }

    private void processTarget(Node parent, Spatial target) {
        if ((target.getType() & SceneElement.NODE) != 0) {
            Node ntarget = (Node) target;
            Node node = new Node();
            UserDataManager.getInstance().bind(node, target);
            copyNode(ntarget, node);
            parent.attachChild(node);
            if (ntarget.getChildren() != null) for (int i = 0; i < ntarget.getChildren().size(); i++) {
                processTarget(node, ntarget.getChild(i));
            }
        } else if ((target.getType() & SceneElement.TRIMESH) != 0) {
            if ((target.getType() & SceneElement.SHARED_MESH) != 0) {
                SharedMesh copy = new SharedMesh(this.getName() + target.getName(), (SharedMesh) target);
                parent.attachChild(copy);
            } else {
                SharedMesh copy = new SharedMesh(this.getName() + target.getName(), (TriMesh) target);
                parent.attachChild(copy);
            }
        }
    }

    private void copyNode(Node original, Node copy) {
        copy.setName(original.getName());
        copy.setCullMode(original.cullMode);
        copy.setLightCombineMode(original.lightCombineMode);
        copy.getLocalRotation().set(original.getLocalRotation());
        copy.getLocalScale().set(original.getLocalScale());
        copy.getLocalTranslation().set(original.getLocalTranslation());
        copy.setRenderQueueMode(original.renderQueueMode);
        copy.setTextureCombineMode(original.textureCombineMode);
        copy.setZOrder(original.getZOrder());
        for (int i = 0; i < RenderState.RS_MAX_STATE; i++) {
            RenderState state = original.getRenderState(i);
            if (state != null) {
                copy.setRenderState(state);
            }
        }
    }

    public void write(JMEExporter e) throws IOException {
        super.write(e);
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(updatesCollisionTree, "updatesCollisionTree", false);
    }

    public void read(JMEImporter e) throws IOException {
        super.read(e);
        InputCapsule capsule = e.getCapsule(this);
        updatesCollisionTree = capsule.readBoolean("updatesCollisionTree", false);
    }
}
