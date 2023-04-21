package be.lassi.ui.fixtures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

public class UniversePanel extends JPanel {

    private int dismissDelay;

    private int initialDelay;

    private final UniversePanelLayout layouter = new UniversePanelLayout();

    public UniversePanel() {
        setToolTipText("Test");
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(final MouseEvent e) {
                ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
                dismissDelay = toolTipManager.getDismissDelay();
                initialDelay = toolTipManager.getInitialDelay();
                toolTipManager.setDismissDelay(Integer.MAX_VALUE);
                toolTipManager.setInitialDelay(0);
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
                toolTipManager.setDismissDelay(dismissDelay);
                toolTipManager.setInitialDelay(initialDelay);
            }

            @Override
            public void mouseClicked(final MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (!layouter.isOutside(x, y)) {
                    System.out.println("Set start address " + layouter.getChannel(x, y));
                }
            }
        });
    }

    @Override
    public String getToolTipText(final MouseEvent e) {
        String result = null;
        if (!layouter.isOutside(e.getX(), e.getY())) {
            StringBuilder b = new StringBuilder();
            b.append("<html>");
            b.append("DMX channel ");
            b.append(layouter.getChannel(e.getX(), e.getY()));
            b.append("&nbsp;&nbsp;&nbsp;&nbsp;");
            b.append("<br>");
            b.append("<br>");
            b.append("<b><u>Fixture</u></b>");
            b.append("<br>");
            b.append("  channel=");
            b.append("</html>");
            int xspaces = e.getX() % 10;
            int yspaces = e.getY() % 10;
            for (int i = 0; i < xspaces; i++) {
                b.append(" ");
            }
            b.append("\n");
            for (int i = 0; i < yspaces; i++) {
                b.append(" ");
            }
            result = b.toString();
        }
        return result;
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Rectangle bounds = getBounds();
        layouter.setBounds(bounds);
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= 12; i++) {
            int x = layouter.xOffset + i * layouter.boxWidth;
            g.drawLine(x, layouter.yOffset, x, layouter.yOffset + layouter.totalHeight);
        }
        for (int i = 13; i <= UniversePanelLayout.COLUMN_COUNT; i++) {
            int x = layouter.xOffset + i * layouter.boxWidth;
            g.drawLine(x, layouter.yOffset, x, layouter.yOffset + layouter.totalHeight - layouter.boxHeight);
        }
        for (int i = 0; i <= UniversePanelLayout.ROW_COUNT; i++) {
            int y = layouter.yOffset + i * layouter.boxHeight;
            int x2 = layouter.xOffset + layouter.totalWidth;
            if (i == UniversePanelLayout.ROW_COUNT) {
                x2 = layouter.xOffset + (12 * layouter.boxWidth);
            }
            g.drawLine(layouter.xOffset, y, x2, y);
        }
        g.setColor(Color.GRAY);
        int gap = 3;
        for (int i = 0; i < 28; i++) {
            int x = layouter.xChannel(i);
            int y = layouter.yChannel(i);
            g.fillRect(x + gap, y + gap, layouter.boxWidth - gap - gap, layouter.boxHeight - gap - gap);
        }
        int x = layouter.xChannel(30);
        int y = layouter.yChannel(30);
        g.fillRect(x + gap, y + gap, 5 * layouter.boxWidth - gap - gap, layouter.boxHeight - gap - gap);
        g.setColor(Color.BLUE);
        x = layouter.xChannel(109);
        y = layouter.yChannel(109);
        g.fillRect(x + gap, y + gap, 5 * layouter.boxWidth - gap - gap, layouter.boxHeight - gap - gap);
    }
}
