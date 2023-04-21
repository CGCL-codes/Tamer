package org.fenggui.decorator;

import org.fenggui.binding.render.Graphics;
import org.fenggui.theme.xml.IXMLStreamable;
import org.fenggui.util.Span;

/**
 * Decorators are graphics routines to beautify widgets, such as borders and backgrounds.
 * Decorators can be enabled and disabled which means that they are drawn or not. They
 * can also share the same label (state label) for a widget such that they get enabled and
 * disabled collectively. This way, widgets can disabled and enable groups of decorators
 * in their behavior routines (e.g. for mouse hover effects, or to draw a button differently
 * when pressed).
 *  
 * 
 * @author Johannes Schaback, last edited by $Author: marcmenghin $, $Date: 2009-03-13 15:56:05 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 606 $
 */
public interface IDecorator extends IXMLStreamable {

    public boolean isEnabled();

    public String getLabel();

    public Span getSpan();

    public void paint(Graphics g, int localX, int localY, int width, int height);

    public void setEnabled(boolean enable);

    public IDecorator copy();
}
