package org.gzigzag.vob;

import java.awt.*;

/** A subclass of <code>RawTextStyle</code> drawing a background behind
 *  the text. This can, for example, be used for showing selections.
 *  <p>
 *  The background is simply a solid color.
 */
public class BgTextStyle extends RawTextStyle {

    public static final String rcsid = "$Id: BgTextStyle.java,v 1.1 2001/08/12 18:10:42 bfallenstein Exp $";

    public Color bg;

    public BgTextStyle(Color bg, ScalableFont font, Color fg) {
        super(font, fg);
        this.bg = bg;
    }

    public void render(java.awt.Graphics g, char[] chars, int offs, int len, int scale, int x, int y, int w, int h, boolean boxDrawn, Vob.RenderInfo info) {
        g.fillRect(x, y, w, h);
        super.render(g, chars, offs, len, scale, x, y, w, h, true, info);
    }
}
