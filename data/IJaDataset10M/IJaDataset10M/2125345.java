package yager.render;

import yager.utils.ShaderList;
import yager.world.Node;

/**
 * @author Ryan Hild (therealfreaker@sourceforge.net)
 */
public abstract class RenderAtom {

    protected ShaderList shaders = new ShaderList();

    protected Node node;

    public abstract int getType();

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void addShader(Shader shader) {
        shaders.add(shader);
    }

    public Shader[] getShaders() {
        return shaders.toArray();
    }

    public ShaderList getShaderList() {
        return shaders;
    }

    public void updateShader(Shader oldShader, Shader newShader) {
        int index = shaders.indexOf(oldShader);
        if (index < 0) addShader(newShader); else shaders.replace(index, newShader);
    }
}
