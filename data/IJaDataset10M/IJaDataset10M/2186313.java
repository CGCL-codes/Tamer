package a03.swing.plaf.style;

import java.awt.Color;
import java.awt.Paint;

public interface A03TabbedPaneStyle extends A03FontStyle, A03StyleConstants, A03ArrowStyle {

    public Paint getContentBorderPaint(int state, int tabPlacement, int x, int y, int width, int height);

    public Paint getTabAreaBackgroundPaint(int width, int height);

    public Paint getTabBorderPaint(int state, int tabPlacement, int x, int y, int width, int height);

    public Paint getTabBackgroundPaint(int state, int tabPlacement, int x, int y, int width, int height);

    public Color getForegroundColor(int state);

    public Color getContentAreaColor();
}
