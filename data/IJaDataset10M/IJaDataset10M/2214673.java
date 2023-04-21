package com.android1.amarena2d.animation;

import com.android1.amarena2d.texture.TextureAtlas;

public class AtlasAnimation implements Animation {

    protected String name;

    protected Frame[] frames;

    protected float delay;

    protected TextureAtlas textureAtlas;

    public AtlasAnimation(TextureAtlas textureAtlas, float delay, Frame... frames) {
        this(textureAtlas, null, delay, frames);
    }

    public AtlasAnimation(TextureAtlas textureAtlas, String name, float delay, Frame... frames) {
        this.textureAtlas = textureAtlas;
        this.name = name;
        this.delay = delay;
        this.frames = frames;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public boolean hasName() {
        return name != null && name.length() > 0;
    }

    public float getDuration() {
        return frames.length * delay;
    }

    public String getName() {
        return name;
    }

    public Frame getFrame(int i) {
        return frames[i];
    }

    public Frame[] getFrames() {
        return frames;
    }

    public float getDelay() {
        return delay;
    }

    public AtlasAnimation copyReverse() {
        Frame[] reverse = new Frame[frames.length];
        int j = 0;
        for (int i = frames.length - 1; i >= 0; i--) {
            Frame textureFrame = frames[i];
            reverse[j++] = textureFrame;
        }
        return new AtlasAnimation(textureAtlas, name, delay, reverse);
    }

    @Override
    public void init() {
        if (!isInit()) this.textureAtlas.init();
    }

    @Override
    public void dispose() {
        this.textureAtlas.dispose();
    }

    @Override
    public boolean isInit() {
        return textureAtlas.isInit();
    }
}
