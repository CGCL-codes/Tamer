package net.sourceforge.nattable.test.fixture;

import net.sourceforge.nattable.style.BorderStyle;
import net.sourceforge.nattable.util.GUIHelper;
import org.eclipse.swt.graphics.Color;

public class BorderStyleFixture extends BorderStyle {

    public static int THICKNESS = 2;

    public static Color COLOR = GUIHelper.COLOR_GREEN;

    public static LineStyleEnum LINE_STYLE = LineStyleEnum.DASHDOT;

    public BorderStyleFixture() {
        super(2, COLOR, LINE_STYLE);
    }
}
