package jmetest.scalarfields;

import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;

/**
 * Demo to show off the {@link ScalarFieldPolygonisator} for scalar fields.
 *
 * @author Daniel Gronau
 * @author Joshua Ellen (performace and test update)
 */
public class TestMetaballs extends SimpleGame {

    private MetaBalls metaBalls = null;

    private Texture texture = null;

    private Text envMapToggleText = null;

    private final String envMapToggleString = "Press E to toggle EnvMap mode: ";

    private int frameCounter = 0;

    public static void main(String[] args) {
        TestMetaballs app = new TestMetaballs();
        app.setConfigShowMode(ConfigShowMode.NeverShow);
        app.start();
    }

    @Override
    protected void simpleInitGame() {
        display.setTitle("MetaBalls Test");
        metaBalls = new MetaBalls();
        metaBalls.setRenderState(createTextureState("terrain/road.jpg"));
        rootNode.attachChild(metaBalls);
        this.lightState.get(0).setAmbient(ColorRGBA.white);
        this.lightState.get(0).setDiffuse(ColorRGBA.white);
        final MaterialState state = display.getRenderer().createMaterialState();
        state.setColorMaterial(MaterialState.ColorMaterial.Diffuse);
        state.setShininess(100);
        state.setAmbient(new ColorRGBA(0.4f, 0.4f, 0.4f, 1));
        metaBalls.setRenderState(state);
        lightState.setEnabled(false);
        final String tempString = envMapToggleString + " (OFF)";
        envMapToggleText = new Text(tempString, tempString);
        envMapToggleText.setRenderState(Text.getDefaultFontTextureState());
        envMapToggleText.setRenderState(Text.getFontBlend());
        envMapToggleText.setLocalTranslation(10, envMapToggleText.getHeight() + 5, 0);
        statNode.attachChild(envMapToggleText);
        KeyBindingManager.getKeyBindingManager().set("toggleEnvMap", KeyInput.KEY_E);
    }

    @Override
    protected void simpleUpdate() {
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggleEnvMap", false)) {
            if (texture.getEnvironmentalMapMode().equals(Texture.EnvironmentalMapMode.ReflectionMap)) {
                texture.setEnvironmentalMapMode(Texture.EnvironmentalMapMode.None);
                envMapToggleText.print(envMapToggleString + " (OFF)");
            } else {
                texture.setEnvironmentalMapMode(Texture.EnvironmentalMapMode.ReflectionMap);
                envMapToggleText.print(envMapToggleString + " (ON)");
            }
        }
        if (frameCounter % 10 == 0) {
            metaBalls.updateGeometricState(tpf, false);
            frameCounter = -1;
        }
        ++frameCounter;
    }

    private TextureState createTextureState(String textureString) {
        try {
            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, new SimpleResourceLocator(Thread.currentThread().getContextClassLoader().getResource("jmetest/data/texture/")));
        } catch (Exception e) {
            System.err.println("Unable to access texture directory.");
            e.printStackTrace();
        }
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        texture = TextureManager.loadTexture(textureString, Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear, ts.getMaxAnisotropic(), false);
        texture.setWrap(Texture.WrapMode.Repeat);
        ts.setTexture(texture);
        return ts;
    }
}
