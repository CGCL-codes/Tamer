package com.fundboss.display;

import java.awt.GridLayout;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jfree.chart.JFreeChart;
import com.fundboss.display.GraphDisplayConfig.AssetDisplayConfig;
import com.fundboss.display.GraphDisplayConfig.TraderDisplayConfig;

public class CheckListPanel extends JPanel {

    public CheckListPanel(final GraphDisplayConfig config, final GraphResults display) {
        GridLayout layout = new GridLayout(0, 1);
        setLayout(layout);
        add(new JLabel("Market Summary"));
        addCheckbox(display, config.getIndexPrice(), new JCheckBox("Index Price"));
        addCheckbox(display, config.getIndexDivYield(), new JCheckBox("Index Div Yield"));
        addCheckbox(display, config.getIndexExcessReturns(), new JCheckBox("Index Excess Returns"));
        addCheckbox(display, config.getIndexDividend(), new JCheckBox("Index Dividend"));
        addCheckbox(display, config.getIndexVolatility(), new JCheckBox("Index Volatility"));
        addCheckbox(display, config.getIndexVolume(), new JCheckBox("Index Volume"));
        addCheckbox(display, config.getAverageRsq(), new JCheckBox("Average R^2"));
        addCheckbox(display, config.getRiskFree(), new JCheckBox("Risk free interest rate"));
        addCheckbox(display, config.getEquityRiskPremium(), new JCheckBox("Equity Risk Premium"));
        Map<Class, TraderDisplayConfig> tradersConfig = config.getTradersConfig();
        for (Class traderType : tradersConfig.keySet()) {
            add(new JLabel(traderType.getSimpleName()));
            TraderDisplayConfig traderConfig = tradersConfig.get(traderType);
            addCheckbox(display, traderConfig.getWorth(), new JCheckBox("Worth"));
            addCheckbox(display, traderConfig.getHoldings(), new JCheckBox("Holdings (Value)"));
            addCheckbox(display, traderConfig.getHoldingsNumber(), new JCheckBox("Holdings (Number)"));
            addCheckbox(display, traderConfig.getLeverageRatio(), new JCheckBox("Leverage Ratio"));
        }
        Map<Integer, AssetDisplayConfig> assetsConfig = config.getAssetsConfig();
        Set<Integer> assetsKeys = assetsConfig.keySet();
        if (assetsKeys.size() <= 10) {
            for (Integer count : assetsConfig.keySet()) {
                add(new JLabel("Asset " + count));
                AssetDisplayConfig assetConfig = assetsConfig.get(count);
                addCheckbox(display, assetConfig.getBeta(), new JCheckBox("Beta"));
                addCheckbox(display, assetConfig.getDivYield(), new JCheckBox("Div yield"));
                addCheckbox(display, assetConfig.getExcessReturns(), new JCheckBox("Excess Return"));
                addCheckbox(display, assetConfig.getCashflow(), new JCheckBox("Dividend"));
                addCheckbox(display, assetConfig.getLiquidity(), new JCheckBox("Liquidity"));
                addCheckbox(display, assetConfig.getPrice(), new JCheckBox("Price"));
                addCheckbox(display, assetConfig.getRsq(), new JCheckBox("R^2"));
                addCheckbox(display, assetConfig.getVolatility(), new JCheckBox("Volatility"));
                addCheckbox(display, assetConfig.getVolume(), new JCheckBox("Volume"));
                addCheckbox(display, assetConfig.getStandardError(), new JCheckBox("Standard Error"));
            }
        }
    }

    private class ChartBuilder extends SwingWorker<JFreeChart, String> {

        private GraphResults display;

        public ChartBuilder(GraphResults display) {
            this.display = display;
        }

        @Override
        protected JFreeChart doInBackground() throws Exception {
            return display.buildChart();
        }

        @Override
        protected void done() {
            try {
                display.displayLineChart(get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addCheckbox(final GraphResults display, final SeriesDisplay configItem, final JCheckBox checkBox) {
        checkBox.setSelected(configItem.getShow());
        add(checkBox);
        checkBox.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                configItem.setShow(checkBox.isSelected());
                new ChartBuilder(display).execute();
            }
        });
    }
}
