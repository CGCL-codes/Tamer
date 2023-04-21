package com.jme.util.export.binary.modules;

import com.jme.scene.state.ColorMaskState;
import com.jme.system.DisplaySystem;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.Savable;
import com.jme.util.export.binary.BinaryLoaderModule;

public class BinaryColorMaskStateModule implements BinaryLoaderModule {

    public String getKey() {
        return ColorMaskState.class.getName();
    }

    public Savable load(InputCapsule inputCapsule) {
        return DisplaySystem.getDisplaySystem().getRenderer().createColorMaskState();
    }
}
