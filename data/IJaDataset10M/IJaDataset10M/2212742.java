package nyc3d.street;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class StreetlightBase {

    String name;

    Node parentNode;

    Spatial spatial;

    Material material;

    public enum Type {

        COBRA_HEAD
    }

    ;

    Type type;

    Streetlight parentLight;

    public StreetlightBase(String name, Type type, AssetManager assetManager, Vector3f translation, Vector3f rotation, Vector3f scale, Node parentNode, Node rootNode, Streetlight parentLight) {
        this.name = name;
        this.parentNode = parentNode;
        this.type = type;
        this.parentLight = parentLight;
        switch(type) {
            case COBRA_HEAD:
                spatial = assetManager.loadModel("Models/CobraHeadStreetlightBase/CobraHeadStreetlightBase.j3o");
                break;
        }
        material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        material.setFloat("Shininess", 100f);
        spatial.setMaterial(material);
        spatial.setLocalTranslation(translation);
        spatial.rotate(rotation.x, rotation.y, rotation.z);
        spatial.scale(scale.x, scale.y, scale.z);
        spatial.setShadowMode(ShadowMode.Cast);
        parentNode.attachChild(spatial);
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Streetlight getParentLight() {
        return parentLight;
    }

    public void setParentLight(Streetlight parentLight) {
        this.parentLight = parentLight;
    }
}
