package com.android1.amarena2d.nodes.layer;

import com.android1.amarena2d.animation.Animation;
import com.android1.amarena2d.nodes.sprites.StaticSprite;
import com.android1.amarena2d.texture.ManagedTexture;
import com.android1.amarena2d.texture.TextureFrame;
import com.android1.amarena2d.texture.TexturedMesh;

public class StaticBackground extends StaticSprite {

    public StaticBackground(TexturedMesh texturedMesh) {
        super(texturedMesh);
    }

    public StaticBackground(TextureFrame textureFrame) {
        super(textureFrame);
    }

    public StaticBackground(ManagedTexture managedTexture) {
        super(managedTexture);
    }

    public StaticBackground(String texturePath) {
        super(texturePath);
    }

    public StaticBackground(Animation animation) {
        super(animation);
    }

    @Override
    public void init() {
        super.init();
        fitToScreen();
    }

    public void fitToScreen() {
        final float x = engine.getCamera().getX();
        final float y = engine.getCamera().getY();
        final float w = engine.getCamera().getWidth();
        final float h = engine.getCamera().getHeight();
        setXY(x - w / 2, y - h / 2);
        setSize(w, h);
    }
}
