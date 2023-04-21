package org.jcrpg.apps;

import java.io.File;
import java.nio.FloatBuffer;
import org.jcrpg.threed.J3DCore;
import org.jcrpg.threed.jme.effects.DepthOfFieldRenderPass;
import org.jcrpg.threed.jme.moving.AnimatedModelNode;
import org.jcrpg.threed.scene.model.moving.MovingModelAnimDescription;
import org.jcrpg.world.ai.fauna.mammals.gorilla.GorillaHorde;
import org.jcrpg.world.ai.humanoid.group.boarman.BoarmanTribe;
import org.jcrpg.world.ai.humanoid.group.myth.greek.member.HellPig;
import com.jme.app.SimplePassGame;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.light.LightNode;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.PassNode;
import com.jme.scene.PassNodeState;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.CullState.Face;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.effects.LensFlare;
import com.jmex.effects.LensFlareFactory;
import com.jmex.terrain.TerrainBlock;

/**
 * <code>TestLensFlare</code> Test of the lens flare effect in jME. Notice that
 * currently it doesn't do occlusion culling.
 * 
 * @author Joshua Slack
 * @version $Id: TestLensFlare.java,v 1.15 2006/11/16 19:59:29 nca Exp $
 */
public class AnimatedModelViewer extends SimplePassGame {

    long time = System.currentTimeMillis();

    int animCount = 0;

    @Override
    protected void simpleUpdate() {
        pManager.updatePasses(tpf);
        super.simpleUpdate();
        if (true) return;
        if (time - System.currentTimeMillis() < -4000) {
            time = System.currentTimeMillis();
            AnimatedModelNode n = this.n;
            {
                if (animCount == 0) n.playAnimation(MovingModelAnimDescription.ANIM_ATTACK_LOWER); else if (animCount == 1) n.playAnimation(MovingModelAnimDescription.ANIM_DEFEND_UPPER);
                if (animCount == 2) n.playAnimation(MovingModelAnimDescription.ANIM_PAIN);
                if (animCount == 3) n.changeToAnimation(MovingModelAnimDescription.ANIM_IDLE);
                animCount++;
                animCount = animCount % 4;
            }
            n = this.n2;
            {
                time = System.currentTimeMillis();
                if (animCount == 0) n.playAnimation(MovingModelAnimDescription.ANIM_ATTACK_LOWER); else if (animCount == 1) n.playAnimation(MovingModelAnimDescription.ANIM_DEFEND_UPPER);
                if (animCount == 2) n.playAnimation(MovingModelAnimDescription.ANIM_PAIN);
                if (animCount == 3) n.changeToAnimation(MovingModelAnimDescription.ANIM_IDLE);
                animCount++;
                animCount = animCount % 4;
            }
        }
    }

    private LightNode lightNode;

    LensFlare flare;

    static String mesh = null;

    static String anim = null;

    public static void main(String[] args) {
        if (args.length < 2) return;
        mesh = args[0];
        anim = args[1];
        AnimatedModelViewer app = new AnimatedModelViewer();
        app.start();
    }

    AnimatedModelNode n = null;

    AnimatedModelNode n2 = null;

    protected void simpleInitGame() {
        display.getRenderer().setBackgroundColor(ColorRGBA.lightGray);
        display.setTitle("Lens Flare!");
        cam.setLocation(new Vector3f(0.0f, 0.0f, 10.0f));
        cam.update();
        lightState.detachAll();
        SimpleResourceLocator loc1 = new SimpleResourceLocator(new File("./data/models/fauna/gorilla").toURI());
        SimpleResourceLocator loc2 = new SimpleResourceLocator(new File("./data/textures/common").toURI());
        SimpleResourceLocator loc3 = new SimpleResourceLocator(new File("./data/textures/low").toURI());
        ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, loc1);
        ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, loc2);
        ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, loc3);
        MovingModelAnimDescription des = new MovingModelAnimDescription();
        des.IDLE = anim;
        ;
        J3DCore.SETTINGS.LOGGING = false;
        for (int i = 0; i < 36; i++) {
            n = new AnimatedModelNode(HellPig.hellPig.modelName, HellPig.hellPig.animation, 0.1f, new float[] { 0, 0, 0 }, 1f);
            n.changeToAnimation(MovingModelAnimDescription.ANIM_IDLE_COMBAT);
            n.unlockTransforms();
            CullState s = display.getRenderer().createCullState();
            s.setCullFace(Face.None);
            n.setRenderState(s);
            n.setLocalTranslation(new Vector3f(i / 4, 2f + (i % 4) * 2f, 0));
            n.lockTransforms();
            n.lockBounds();
            n.setCullHint(CullHint.Never);
            rootNode.attachChild(n);
            rootNode.updateRenderState();
        }
        rootNode.attachChild(n);
        PointLight dr = new PointLight();
        dr.setEnabled(true);
        dr.setDiffuse(ColorRGBA.white);
        dr.setAmbient(ColorRGBA.gray);
        dr.setLocation(new Vector3f(0f, 0f, 0f));
        lightState.setTwoSidedLighting(false);
        lightNode = new LightNode("light");
        lightNode.setLight(dr);
        Vector3f min2 = new Vector3f(-0.5f, -0.5f, -0.5f);
        Vector3f max2 = new Vector3f(0.5f, 0.5f, 0.5f);
        lightNode.setLocalTranslation(new Vector3f(+14f, 14f, +14f));
        TextureState[] tex = new TextureState[4];
        tex[0] = display.getRenderer().createTextureState();
        tex[0].setTexture(TextureManager.loadTexture("./data/flare/flare1.png", Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear, Image.Format.RGBA8, 1.0f, true));
        tex[0].setEnabled(true);
        tex[1] = display.getRenderer().createTextureState();
        tex[1].setTexture(TextureManager.loadTexture("./data/flare/flare2.png", Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear));
        tex[1].setEnabled(true);
        tex[2] = display.getRenderer().createTextureState();
        tex[2].setTexture(TextureManager.loadTexture(("./data/flare/flare3.png"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear));
        tex[2].setEnabled(true);
        tex[3] = display.getRenderer().createTextureState();
        tex[3].setTexture(TextureManager.loadTexture(("./data/flare/flare4.png"), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear));
        tex[3].setEnabled(true);
        flare = LensFlareFactory.createBasicLensFlare("flare", tex);
        flare.setRootNode(rootNode);
        lightNode.attachChild(flare);
        Quad q = new Quad("a", 12, 12);
        Quad q2 = new Quad("a", 12, 12);
        BlendState as = display.getRenderer().createBlendState();
        as.setBlendEnabled(true);
        BlendState as2 = display.getRenderer().createBlendState();
        as2.setBlendEnabled(true);
        rootNode.updateRenderState();
        rootNode.attachChild(lightNode);
        RenderPass rootPass = new RenderPass();
        rootPass.add(rootNode);
        pManager.add(rootPass);
        DepthOfFieldRenderPass dof = new DepthOfFieldRenderPass(cam, 2, 4);
        dof.setBlurSize(0.013f);
        dof.setNearBlurDepth(-00f);
        dof.setFocalPlaneDepth(20f);
        dof.setFarBlurDepth(200f);
        dof.setRootSpatial(rootNode);
    }

    private TextureState createLightmapTextureState(String texture) {
        TextureState ts = display.getRenderer().createTextureState();
        return ts;
    }

    private TextureState createSplatTextureState(String texture, String alpha) {
        TextureState ts = display.getRenderer().createTextureState();
        boolean[][] data = new boolean[50][50];
        Texture t0 = null;
        t0.setScale(new Vector3f(10.1f, 10f, 10.1f));
        ts.setTexture(t0, 0);
        ts.apply();
        if (alpha != null) {
            addAlphaSplat(ts, alpha);
        }
        return ts;
    }

    private void addAlphaSplat(TextureState ts, String alpha) {
        boolean[][] data = new boolean[256][256];
        data[3][3] = true;
    }
}
