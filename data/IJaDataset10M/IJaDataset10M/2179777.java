package com.kenstevens.stratdom.ui.image;

import org.eclipse.swt.graphics.PaletteData;

public interface PixelFilter {

    public boolean match(PaletteData palette, int pixelValue);
}
