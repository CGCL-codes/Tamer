package com.bluebrim.text.shared.swing;

import javax.swing.text.*;

/**
 * Interface for text views that can be broken into fragments
 * 
 * @author: Dennis Malmstr�m
 */
interface CoBreakableView {

    View createFragment(int p0, int p1);

    float getAscent();

    float getDescent();

    float getFontSize();

    float getHyphenWidth();

    float getMinimalPartialSpan(float x0, int p0, int p1, float minimumRelativeSpaceWidth);

    float getMinimalSpan(float x0, float minimumRelativeSpaceWidth);

    void paint(com.bluebrim.base.shared.CoPaintable g, java.awt.Shape alloc, boolean broken);
}
