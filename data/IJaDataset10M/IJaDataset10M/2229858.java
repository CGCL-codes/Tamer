package homura.hde.app.core.game.state;

import homura.hde.app.HDEView;
import homura.hde.app.core.game.StandardGame;
import homura.hde.core.scene.Node;
import homura.hde.core.scene.Text;
import homura.hde.core.scene.state.AlphaState;
import homura.hde.core.scene.state.TextureState;
import homura.hde.core.texture.Texture;
import homura.hde.core.texture.TextureManager;

/**
 * <code>TextGameState</code> provides a GameState that can be used to display simple text.
 * This is similar to the typical FPS counter seen in SimpleGame but can be used for any text.
 * 
 * @author Matthew D. Hicks
 */
public class TextGameState extends BasicGameState {

    private static final String FONT_LOCATION = "/com/jme/app/defaultfont.tga";

    private Text textObject;

    private Node textNode;

    private String text;

    public TextGameState(String text) {
        super("TextGameState");
        this.text = text;
        AlphaState as = HDEView.getDisplaySystem().getRenderer().createAlphaState();
        as.setBlendEnabled(true);
        as.setSrcFunction(AlphaState.SB_SRC_ALPHA);
        as.setDstFunction(AlphaState.DB_ONE);
        as.setTestEnabled(true);
        as.setTestFunction(AlphaState.TF_GREATER);
        as.setEnabled(true);
        TextureState font = HDEView.getDisplaySystem().getRenderer().createTextureState();
        font.setTexture(TextureManager.loadTexture(StandardGame.class.getResource(FONT_LOCATION), Texture.MM_LINEAR, Texture.FM_LINEAR));
        font.setEnabled(true);
        textObject = new Text("Text", "");
        textObject.setTextureCombineMode(TextureState.REPLACE);
        textNode = new Node("TextNode");
        textNode.attachChild(textObject);
        textNode.setRenderState(font);
        textNode.setRenderState(as);
        textNode.updateGeometricState(0.0f, true);
        textNode.updateRenderState();
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void update(float tpf) {
        textObject.print(text);
    }

    public void render(float tpf) {
        HDEView.getDisplaySystem().getRenderer().draw(textNode);
    }

    public void cleanup() {
    }
}
