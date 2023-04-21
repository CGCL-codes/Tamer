package engine.misc;

import com.ardor3d.renderer.ContextCapabilities;

public class ReliableContextCapabilities extends ContextCapabilities {

    public ReliableContextCapabilities(final ContextCapabilities defaultCaps) {
        super(defaultCaps);
        if (defaultCaps.getDisplayRenderer().startsWith("Mesa DRI R200 ")) _maxTextureSize = defaultCaps.getMaxTextureSize() / 2;
    }
}
