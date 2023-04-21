package org.zebrafish.fill;

import org.zebrafish.util.Separator;

/**
 * Gradien Fill List.
 * 
 * @author Max Chu
 * @author Joseph S. Kuo
 * @version $Revision: 30 $, $Date: 2008-10-07 01:35:32 -0400 (Tue, 07 Oct 2008) $
 * @since 0.1
 */
public class GradientFillList extends AbstractFillList<GradientFill> {

    /**
	 * Constructs a <code>GradienFillList</code> object.
	 * 
	 * @param type the fill type
	 * @param angle the angle
	 */
    public GradientFillList(String type, Integer angle) {
        super(type, angle);
    }

    @Override
    public StringBuffer createStringBuffer() {
        StringBuffer sb = super.createStringBuffer();
        return sb.append(getType()).append(",lg,").append(getAngle()).append(Separator.COMMA);
    }
}
