package a03.swing.plaf;

import java.awt.Color;
import javax.swing.JList;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

public interface A03ListDelegate extends A03BorderDelegate {

    public Color getForeground(JList list, int layoutOrientation, int row);

    public ColorUIResource getBackground();

    public Color getBackground(JList list, int layoutOrientation, int row);

    public FontUIResource getFont();

    public ColorUIResource getSelectionBackground();

    public ColorUIResource getSelectionForeground();

    public Color getDisabledSelectionBackground();

    public Color getDisabledSelectionForeground();
}
