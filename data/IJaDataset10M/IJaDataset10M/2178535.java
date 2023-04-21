package homura.hde.main.core.scene;

import homura.hde.app.HDEView;
import homura.hde.core.renderer.Renderer;
import homura.hde.core.scene.Node;
import homura.hde.core.scene.SceneElement;
import homura.hde.core.scene.bounding.BoundingBox;
import homura.hde.core.scene.shape.Quad;
import homura.hde.core.scene.state.LightState;
import homura.hde.core.scene.state.RenderState;
import homura.hde.core.scene.state.TextureState;
import homura.hde.core.scene.state.ZBufferState;
import homura.hde.util.JmeException;
import homura.hde.util.export.InputCapsule;
import homura.hde.util.export.JMEExporter;
import homura.hde.util.export.JMEImporter;
import homura.hde.util.export.OutputCapsule;
import homura.hde.util.export.Savable;
import homura.hde.util.image.Texture;
import homura.hde.util.maths.Quaternion;
import homura.hde.util.maths.Vector3f;
import java.io.IOException;

/**
 * A Box made of textured quads that simulate having a sky, horizon and so forth
 * around your scene. Either attach to a camera node or update on each frame to
 * set this skybox at the camera's position.
 * 
 * @author David Bitkowski
 * @author Jack Lindamood (javadoc only)
 * @version $Id: Skybox.java,v 1.16 2006/05/12 21:19:22 nca Exp $
 */
public class Skybox extends Node {

    private static final long serialVersionUID = 1L;

    /** The +Z side of the skybox. */
    public static final int NORTH = 0;

    /** The -Z side of the skybox. */
    public static final int SOUTH = 1;

    /** The -X side of the skybox. */
    public static final int EAST = 2;

    /** The +X side of the skybox. */
    public static final int WEST = 3;

    /** The +Y side of the skybox. */
    public static final int UP = 4;

    /** The -Y side of the skybox. */
    public static final int DOWN = 5;

    private float xExtent;

    private float yExtent;

    private float zExtent;

    private Quad[] skyboxQuads;

    public Skybox() {
    }

    /**
     * Creates a new skybox. The size of the skybox and name is specified here.
     * By default, no textures are set.
     * 
     * @param name
     *            The name of the skybox.
     * @param xExtent
     *            The x size of the skybox in both directions from the center.
     * @param yExtent
     *            The y size of the skybox in both directions from the center.
     * @param zExtent
     *            The z size of the skybox in both directions from the center.
     */
    public Skybox(String name, float xExtent, float yExtent, float zExtent) {
        super(name);
        this.xExtent = xExtent;
        this.yExtent = yExtent;
        this.zExtent = zExtent;
        initialize();
    }

    public int getType() {
        return (SceneElement.NODE | SceneElement.SKY_BOX);
    }

    /**
     * Set the texture to be displayed on the given side of the skybox. Replaces
     * any existing texture on that side.
     * 
     * @param direction
     *            One of Skybox.NORTH, Skybox.SOUTH, and so on...
     * @param texture
     *            The texture for that side to assume.
     */
    public void setTexture(int direction, Texture texture) {
        if (direction < 0 || direction > 5) {
            throw new JmeException("Direction " + direction + " is not a valid side for the skybox");
        }
        skyboxQuads[direction].clearRenderState(RenderState.RS_TEXTURE);
        setTexture(direction, texture, 0);
    }

    /**
     * Set the texture to be displayed on the given side of the skybox. Only
     * replaces the texture at the index specified by textureUnit.
     * 
     * @param direction
     *            One of Skybox.NORTH, Skybox.SOUTH, and so on...
     * @param texture
     *            The texture for that side to assume.
     * @param textureUnit
     *            The texture unite of the given side's TextureState the texture
     *            will assume.
     */
    public void setTexture(int direction, Texture texture, int textureUnit) {
        if (direction < 0 || direction > 5) {
            throw new JmeException("Direction " + direction + " is not a valid side for the skybox");
        }
        TextureState ts = (TextureState) skyboxQuads[direction].getRenderState(RenderState.RS_TEXTURE);
        if (ts == null) {
            ts = HDEView.getDisplaySystem().getRenderer().createTextureState();
        }
        ts.setTexture(texture, textureUnit);
        ts.setEnabled(true);
        skyboxQuads[direction].setRenderState(ts);
        return;
    }

    public Texture getTexture(int direction) {
        if (direction < 0 || direction > 5 || skyboxQuads[direction].getRenderState(RenderState.RS_TEXTURE) == null) {
            return null;
        }
        return ((TextureState) skyboxQuads[direction].getRenderState(RenderState.RS_TEXTURE)).getTexture();
    }

    private void initialize() {
        HDEView display = HDEView.getDisplaySystem();
        skyboxQuads = new Quad[6];
        skyboxQuads[NORTH] = new Quad("north", xExtent * 2, yExtent * 2);
        skyboxQuads[NORTH].setLocalRotation(new Quaternion(new float[] { 0, (float) Math.toRadians(180), 0 }));
        skyboxQuads[NORTH].setLocalTranslation(new Vector3f(0, 0, zExtent));
        skyboxQuads[SOUTH] = new Quad("south", xExtent * 2, yExtent * 2);
        skyboxQuads[SOUTH].setLocalTranslation(new Vector3f(0, 0, -zExtent));
        skyboxQuads[EAST] = new Quad("east", zExtent * 2, yExtent * 2);
        skyboxQuads[EAST].setLocalRotation(new Quaternion(new float[] { 0, (float) Math.toRadians(90), 0 }));
        skyboxQuads[EAST].setLocalTranslation(new Vector3f(-xExtent, 0, 0));
        skyboxQuads[WEST] = new Quad("west", zExtent * 2, yExtent * 2);
        skyboxQuads[WEST].setLocalRotation(new Quaternion(new float[] { 0, (float) Math.toRadians(270), 0 }));
        skyboxQuads[WEST].setLocalTranslation(new Vector3f(xExtent, 0, 0));
        skyboxQuads[UP] = new Quad("up", xExtent * 2, zExtent * 2);
        skyboxQuads[UP].setLocalRotation(new Quaternion(new float[] { (float) Math.toRadians(90), (float) Math.toRadians(270), 0 }));
        skyboxQuads[UP].setLocalTranslation(new Vector3f(0, yExtent, 0));
        skyboxQuads[DOWN] = new Quad("down", xExtent * 2, zExtent * 2);
        skyboxQuads[DOWN].setLocalRotation(new Quaternion(new float[] { (float) Math.toRadians(270), (float) Math.toRadians(270), 0 }));
        skyboxQuads[DOWN].setLocalTranslation(new Vector3f(0, -yExtent, 0));
        setLightCombineMode(LightState.OFF);
        setTextureCombineMode(TextureState.REPLACE);
        ZBufferState zbuff = display.getRenderer().createZBufferState();
        zbuff.setWritable(false);
        zbuff.setEnabled(true);
        zbuff.setFunction(ZBufferState.CF_LEQUAL);
        setRenderState(zbuff);
        setCullMode(SceneElement.CULL_NEVER);
        for (int i = 0; i < 6; i++) {
            skyboxQuads[i].setTextureCombineMode(TextureState.REPLACE);
            skyboxQuads[i].setLightCombineMode(LightState.OFF);
            skyboxQuads[i].setCullMode(SceneElement.CULL_NEVER);
            skyboxQuads[i].setModelBound(new BoundingBox());
            skyboxQuads[i].updateModelBound();
            skyboxQuads[i].setRenderQueueMode(Renderer.QUEUE_SKIP);
            skyboxQuads[i].setVBOInfo(null);
            attachChild(skyboxQuads[i]);
        }
    }

    /**
     * Retrieve the quad indicated by the given side.
     * 
     * @param direction
     *            One of Skybox.NORTH, Skybox.SOUTH, and so on...
     * @return The Quad that makes up that side of the Skybox.
     */
    public Quad getSide(int direction) {
        return skyboxQuads[direction];
    }

    /**
     * Force all of the textures to load. This prevents pauses later during the
     * application as you pan around the world.
     */
    public void preloadTextures() {
        for (int x = 0; x < 6; x++) {
            TextureState ts = (TextureState) skyboxQuads[x].getRenderState(RenderState.RS_TEXTURE);
            if (ts != null) ts.apply();
        }
    }

    public void write(JMEExporter e) throws IOException {
        super.write(e);
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(xExtent, "xExtent", 0);
        capsule.write(yExtent, "yExtent", 0);
        capsule.write(zExtent, "zExtent", 0);
        capsule.write(skyboxQuads, "skyboxQuads", null);
    }

    public void read(JMEImporter e) throws IOException {
        super.read(e);
        InputCapsule capsule = e.getCapsule(this);
        xExtent = capsule.readFloat("xExtent", 0);
        yExtent = capsule.readFloat("yExtent", 0);
        zExtent = capsule.readFloat("zExtent", 0);
        Savable[] savs = capsule.readSavableArray("skyboxQuads", null);
        if (savs == null) {
            skyboxQuads = null;
            initialize();
        } else {
            skyboxQuads = new Quad[savs.length];
            for (int x = 0; x < savs.length; x++) {
                skyboxQuads[x] = (Quad) savs[x];
            }
        }
    }
}
