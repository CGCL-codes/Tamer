package net.sf.gham.core.entity.player.cellrenderer;

import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.sf.gham.core.util.FloatWithBoolean;
import net.sf.gham.swing.cellrenderer.Number2CellRenderer;
import net.sf.gham.swing.cellrenderer.PanelCellRenderer;

/**
 *
 * @author  Fabio Collini
 */
public class StarsCellRenderer extends PanelCellRenderer {

    private static StarsCellRenderer singleton = new StarsCellRenderer();

    public static StarsCellRenderer singleton() {
        return singleton;
    }

    private StarsCellRenderer() {
        ((FlowLayout) getLayout()).setAlignment(FlowLayout.LEFT);
        ((FlowLayout) getLayout()).setHgap(0);
        ((FlowLayout) getLayout()).setVgap(0);
    }

    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
        removeAll();
        if (obj instanceof FloatWithBoolean) {
            FloatWithBoolean stars = (FloatWithBoolean) obj;
            if (stars.isBooleanValue()) {
                for (int i = 1; i <= stars.getFloatValue(); i++) {
                    add(new JLabel(new ImageIcon(getClass().getResource("/icon/star.png"))));
                }
                if (((int) (stars.getFloatValue() * 10)) % 10 == 5) {
                    add(new JLabel(new ImageIcon(getClass().getResource("/icon/Praised_half.png"))));
                }
                return this;
            } else {
                return Number2CellRenderer.singleton().getTableCellRendererComponent(table, stars.getFloatValue(), isSelected, hasFocus, row, column);
            }
        }
        return this;
    }
}
