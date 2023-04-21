package com.golemgame.toolbar.sound;

import com.golemgame.states.GeneralSettings;
import com.golemgame.states.StateManager;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.simplemonkey.IMouseListener;
import com.simplemonkey.widgets.TextureWidget;

public class VolumeMeter extends TextureWidget {

    private Texture bottomLayer;

    private Texture topLayer;

    private Vector3f topTranslation = new Vector3f();

    private MaterialState color;

    private boolean isMute = false;

    private ColorRGBA volCol = new ColorRGBA(24f / 255f, 82f / 255f, 150f / 255f, 1f);

    public VolumeMeter() {
        super("Volume Meter");
        bottomLayer = TextureManager.loadTexture(StateManager.loadResource("com/golemgame/data/textures/menu/volume.png"));
        bottomLayer.setApply(Texture.AM_MODULATE);
        topLayer = TextureManager.loadTexture(StateManager.loadResource("com/golemgame/data/textures/menu/volume1.png"));
        topLayer.setBlendColor(volCol);
        topLayer.setApply(Texture.AM_BLEND);
        topTranslation.y = 0f;
        topLayer.setTranslation(topTranslation);
        topLayer.setWrap(Texture.WM_BCLAMP_S_BCLAMP_T);
        super.setTexture(bottomLayer, 0);
        super.setTexture(topLayer, 1);
        color = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        color.setDiffuse(new ColorRGBA(0, 0, 0, 150f / 255f));
        color.setColorMaterial(MaterialState.CM_NONE);
        color.setMaterialFace(MaterialState.MF_FRONT);
        AlphaState semiTransparentAlpha = DisplaySystem.getDisplaySystem().getRenderer().createAlphaState();
        semiTransparentAlpha.setBlendEnabled(true);
        semiTransparentAlpha.setSrcFunction(AlphaState.SB_SRC_ALPHA);
        semiTransparentAlpha.setDstFunction(AlphaState.SB_ONE_MINUS_SRC_ALPHA);
        semiTransparentAlpha.setTestEnabled(true);
        semiTransparentAlpha.setTestFunction(AlphaState.TF_GREATER);
        super.getSpatial().setRenderState(semiTransparentAlpha);
        super.getSpatial().setRenderState(color);
        super.getSpatial().updateRenderState();
        super.addMouseListener(new IMouseListener() {

            private boolean pressed = false;

            public void mouseMove(float x, float y) {
                if (pressed && !isMute) {
                    float vol = y / getHeight();
                    if (vol < 0.05f) vol = 0f;
                    if (vol > 0.95f) vol = 1f;
                    GeneralSettings.getInstance().getVolume().setValue(vol);
                }
            }

            public void mouseOff() {
            }

            public void mousePress(boolean pressed, int button, float x, float y) {
                if (button == 0) this.pressed = pressed; else this.pressed = false;
                if (pressed && !isMute) {
                    float vol = y / getHeight();
                    if (vol < 0.05f) vol = 0f;
                    if (vol > 0.95f) vol = 1f;
                    GeneralSettings.getInstance().getVolume().setValue(vol);
                }
            }
        });
    }

    @Override
    public void layout() {
        super.layout();
        topLayer.setTranslation(topTranslation);
        super.getSpatial().updateRenderState();
    }

    public void setVolume(float volume) {
        topTranslation.y = (1f - volume);
        topLayer.setTranslation(topTranslation);
    }

    public void setMute(Boolean mute) {
        this.isMute = mute;
        if (mute) {
            topLayer.setBlendColor(ColorRGBA.gray);
        } else {
            topLayer.setBlendColor(volCol);
        }
    }
}
