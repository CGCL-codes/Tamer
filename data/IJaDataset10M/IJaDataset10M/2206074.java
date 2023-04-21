package jgm.gui.tabs;

import jgm.gui.components.PlayerChart;
import java.awt.*;

public class PlayerChartTab extends Tab {

    public PlayerChart chart;

    public PlayerChartTab(jgm.gui.GUI gui) {
        super(gui, new BorderLayout(), "Health/Mana Chart");
        chart = new PlayerChart();
        chart.setBorder(null);
        add(chart, BorderLayout.CENTER);
    }
}
