package org.designerator.color.interfaces;

import imagefp.color.GradientData;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

public interface IGradient {

    public abstract void update(Rectangle shapeBounds);

    public void setColors(Color[] colors);

    public abstract Color[] getColors();

    public abstract GradientData getGradientData();

    public abstract void updateGradient(Color color, int position);
}
