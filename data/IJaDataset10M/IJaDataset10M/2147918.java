package main.Gui;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import main.Memory.Memory;

/**
 *
 * @author usuario
 */
public class DisplaySign {

    Picture signBkg;

    private Node guiNode;

    private Node textNode = new Node("text");

    private boolean displayed = false;

    public void initHud(AssetManager assetManager, Node guiNode) {
        this.guiNode = guiNode;
        Texture tex = assetManager.loadTexture("Textures/sign.png");
        signBkg = new Picture("SignBkg");
        signBkg.setTexture(assetManager, (Texture2D) tex, true);
        signBkg.setWidth(tex.getImage().getWidth() * 2);
        signBkg.setHeight(tex.getImage().getHeight() * 2);
        signBkg.setPosition(60, 60);
    }

    public void display(Integer signId, BitmapFont font) {
        if (displayed) return;
        BitmapText[] texts = new BitmapText[Memory.getSigns().get(signId).length];
        int linha = 0;
        for (String signText : Memory.getSigns().get(signId)) {
            BitmapText thisLine = new BitmapText(font, false);
            thisLine.setSize(font.getCharSet().getRenderedSize());
            thisLine.setColor(ColorRGBA.Black);
            thisLine.setText(signText);
            thisLine.setLocalTranslation(110, 550 - linha * thisLine.getLineHeight(), 0);
            textNode.attachChild(thisLine);
            linha++;
        }
        guiNode.attachChild(signBkg);
        guiNode.attachChild(textNode);
        displayed = true;
    }

    public void close() {
        guiNode.detachChild(signBkg);
        guiNode.detachChild(textNode);
        displayed = false;
    }
}
