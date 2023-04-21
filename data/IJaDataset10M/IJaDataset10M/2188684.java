package fr.soleil.mambo.actions.view.listeners;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JColorChooser;
import fr.esrf.tangoatk.widget.util.ATKFontChooser;
import fr.soleil.mambo.containers.view.dialogs.ChartGeneralTabbedPane;

public class GeneralPanelMouseListener extends MouseAdapter {

    private ChartGeneralTabbedPane chartGeneralTabbedPane;

    public GeneralPanelMouseListener(ChartGeneralTabbedPane chartGeneralTabbedPane) {
        super();
        this.chartGeneralTabbedPane = chartGeneralTabbedPane;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == chartGeneralTabbedPane.getGeneralFontHeaderBtn()) {
            Font f = ATKFontChooser.getNewFont(chartGeneralTabbedPane, "Choose Header Font", chartGeneralTabbedPane.getHeaderFont());
            if (f != null) {
                chartGeneralTabbedPane.setHeaderFont(f);
            }
        } else if (e.getSource() == chartGeneralTabbedPane.getGeneralFontLabelBtn()) {
            Font f = ATKFontChooser.getNewFont(chartGeneralTabbedPane, "Choose label Font", chartGeneralTabbedPane.getLabelFont());
            if (f != null) {
                chartGeneralTabbedPane.setLabelFont(f);
            }
        } else if (e.getSource() == chartGeneralTabbedPane.getGeneralBackColorBtn()) {
            Color c = JColorChooser.showDialog(chartGeneralTabbedPane, "Choose marker Color", chartGeneralTabbedPane.getBackgroundColor());
            if (c != null) {
                chartGeneralTabbedPane.setBackgroundColor(c);
            }
        }
    }
}
