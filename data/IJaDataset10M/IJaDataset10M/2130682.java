package net.community.chest.awt.border;

import java.awt.Color;
import javax.swing.border.LineBorder;

/**
 * <P>Copyright as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jul 1, 2009 10:26:28 AM
 */
public class ExtendedLineBorder extends LineBorder {

    /**
	 * 
	 */
    private static final long serialVersionUID = -294246302531831220L;

    public ExtendedLineBorder(Color color) {
        this(color, 1);
    }

    public ExtendedLineBorder(Color color, int thicknessValue) {
        this(color, thicknessValue, false);
    }

    public ExtendedLineBorder(Color color, int thicknessValue, boolean roundedCornersValue) {
        super(color, thicknessValue, roundedCornersValue);
    }

    public ExtendedLineBorder(LineBorder b) {
        this(b.getLineColor(), b.getThickness(), b.getRoundedCorners());
    }

    public void setLineColor(Color c) {
        lineColor = c;
    }

    public void setThickness(int v) {
        thickness = v;
    }

    public void setRoundedCorners(boolean v) {
        roundedCorners = v;
    }
}
