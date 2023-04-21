package corina.graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import corina.core.App;
import corina.graph.GraphInfo.colorPair;

/**
 * @author Lucas Madar
 * 
 */
public class ColorComboBox extends JComboBox {

    private static class ColorIcon implements Icon {

        private Color color;

        private static final int WIDTH = 30;

        private static final int HEIGHT = 12;

        private static final int SPACER = 3;

        public int getIconHeight() {
            return HEIGHT;
        }

        public int getIconWidth() {
            return WIDTH + 2 * SPACER;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color.darker());
            g.drawRect(x + SPACER, y, WIDTH, HEIGHT);
            g.setColor(color);
            g.fillRect(x + SPACER + 1, y + 1, WIDTH - 1, HEIGHT - 1);
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }

    private static class ColorLabelRenderer extends JLabel implements ListCellRenderer {

        private ColorIcon icon = new ColorIcon();

        public ColorLabelRenderer() {
            setOpaque(true);
            setIcon(icon);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            GraphInfo.colorPair color = (GraphInfo.colorPair) value;
            setText(color.getColorName());
            icon.setColor(color.getColor());
            return this;
        }
    }

    private static final Color OTHER_COLOR = new Color(0, 204, 204);

    public void setColor(Color c) {
        int i;
        for (i = 0; i < GraphInfo.screenColors.length; i++) {
            if (c.equals(GraphInfo.screenColors[i].getColor())) break;
        }
        if (i != GraphInfo.screenColors.length) {
            setSelectedIndex(i);
            otherColor.setColor(OTHER_COLOR);
            return;
        }
        otherColor.setColor(c);
        setSelectedIndex(i);
    }

    public Color getSelectedColor() {
        int idx = getSelectedIndex();
        if (idx == GraphInfo.screenColors.length) return otherColor.getColor();
        return GraphInfo.screenColors[idx].getColor();
    }

    /**
	 * Create a new popup for a color preference.
	 * 
	 * @param preference
	 *            the preference key that this popup corresponds to
	 */
    public ColorComboBox() {
        boolean stdColor = false;
        for (int i = 0; i < GraphInfo.screenColors.length; i++) {
            addItem(GraphInfo.screenColors[i]);
        }
        otherColor = new GraphInfo.colorPair("Other...", OTHER_COLOR);
        addItem(otherColor);
        addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        setRenderer(new ColorLabelRenderer());
        setMaximumRowCount(getItemCount());
    }

    private GraphInfo.colorPair otherColor;
}
