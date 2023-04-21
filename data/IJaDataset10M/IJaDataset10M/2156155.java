package org.openconcerto.ui.coreanimation;

import java.awt.Color;
import javax.swing.JComponent;

public class JComponentAnimator {

    protected JComponent chk;

    protected static final int[] a = { 0, 30, 60, 90, 120, 150, 180, 210, 240, 255, 255, 255, 240, 210, 180, 150, 120, 90, 60, 0 };

    protected static Color[] yellowBG = new Color[a.length];

    protected static Color[] yellowFG = new Color[a.length];

    ;

    static {
        for (int i = 0; i < a.length; i++) {
            final Color bgcolor = new Color(255, 255 - (a[i] / 30), 255 - (a[i] / 5));
            yellowBG[i] = bgcolor;
            final Color fgcolor = new Color(a[i], a[i] / 2, 0);
            yellowFG[i] = fgcolor;
        }
    }

    protected int i = 0;

    protected int wait = 0;

    public JComponentAnimator(JComponent f) {
        chk = f;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JComponentAnimator) {
            return (((JComponentAnimator) obj).chk == chk);
        }
        return super.equals(obj);
    }
}
