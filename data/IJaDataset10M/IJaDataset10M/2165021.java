package org.aiotrade.charting.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import org.aiotrade.math.timeseries.Ser;
import org.aiotrade.charting.view.pane.XControlPane;

/**
 *
 * @author Caoyuan Deng
 */
public class PopupIndicatorChartView extends IndicatorChartView {

    public PopupIndicatorChartView() {
    }

    public PopupIndicatorChartView(ChartingController controller, Ser mainSer) {
        init(controller, mainSer);
    }

    @Override
    public void init(ChartingController controller, Ser mainSer) {
        super.init(controller, mainSer);
    }

    protected void initComponents() {
        xControlPane = new XControlPane(this, mainChartPane);
        xControlPane.setPreferredSize(new Dimension(10, CONTROL_HEIGHT));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = gbc.SOUTH;
        gbc.fill = gbc.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 100;
        gbc.weighty = 0;
        add(xControlPane, gbc);
        gbc.anchor = gbc.CENTER;
        gbc.fill = gbc.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 100;
        gbc.weighty = 100 - 100 / 6.18;
        add(glassPane, gbc);
        gbc.anchor = gbc.CENTER;
        gbc.fill = gbc.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 100;
        gbc.weighty = 100 - 100 / 6.18;
        add(mainLayeredPane, gbc);
        gbc.anchor = gbc.CENTER;
        gbc.fill = gbc.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        gbc.weighty = 100;
        add(axisYPane, gbc);
        gbc.anchor = gbc.CENTER;
        gbc.fill = gbc.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = gbc.RELATIVE;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 100;
        gbc.weighty = 0;
        add(axisXPane, gbc);
    }

    public XControlPane getXControlPane() {
        return xControlPane;
    }
}
