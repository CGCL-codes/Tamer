package com.volatileengine.scene;

/**
 * 
 * @author Administrator
 */
public class DirectionalLight extends Light {

    @Override
    public LightType getType() {
        return LightType.DIRECTIONAL;
    }
}
