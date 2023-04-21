package org.blogtrader.platform.core.netbeans.options.colors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.blogtrader.platform.core.analysis.chart.QuoteChart;
import org.blogtrader.platform.core.analysis.chartview.QuoteChartView;
import org.blogtrader.platform.core.analysis.chartview.TickeringQuoteChartView;
import org.blogtrader.platform.core.option.CityLightsColorTheme;
import org.blogtrader.platform.core.option.AbstractColorTheme;
import org.blogtrader.platform.core.option.GrayColorTheme;
import org.blogtrader.platform.core.option.OptionsManager;
import org.blogtrader.platform.core.option.WhiteColorTheme;
import org.blogtrader.platform.core.persistence.PersistenceManager;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;

/**
 * Implementation of one panel in Options Dialog.
 *
 * @author Caoyuan Deng
 */
public final class ColorFontOptionsPanelController extends OptionsPanelController {

    private PropertyChangeSupport pcs;

    private ColorFontOptionsPanel optionsPanel;

    private boolean changed = false;

    private String currentPropertyValue = "property value";

    public ColorFontOptionsPanelController() {
        optionsPanel = new ColorFontOptionsPanel();
    }

    public void update() {
        String colorThemeStr = null;
        AbstractColorTheme colorTheme = OptionsManager.getColorTheme();
        if (colorTheme instanceof WhiteColorTheme) {
            colorThemeStr = "White";
        } else if (colorTheme instanceof CityLightsColorTheme) {
            colorThemeStr = "City Lights";
        } else {
            colorThemeStr = "Gray";
        }
        optionsPanel.colorThemeBox.setSelectedItem(colorThemeStr);
        optionsPanel.reverseColorBox.setSelected(OptionsManager.isReversedPositiveNegativeColor());
        optionsPanel.quoteChartStyleBox.setSelectedItem(OptionsManager.getQuoteChartStyle());
        optionsPanel.antiAliasBox.setSelected(OptionsManager.isAntiAlias());
        optionsPanel.initPreviewPanle();
    }

    /**
     * this method is called when Ok button has been pressed
     * save values here */
    public void applyChanges() {
        String colorThemeStr = (String) optionsPanel.colorThemeBox.getSelectedItem();
        AbstractColorTheme colorTheme = null;
        if (colorThemeStr.equalsIgnoreCase("White")) {
            colorTheme = new WhiteColorTheme();
        } else if (colorThemeStr.equalsIgnoreCase("City Lights")) {
            colorTheme = new CityLightsColorTheme();
        } else {
            colorTheme = new GrayColorTheme();
        }
        OptionsManager.setColorTheme(colorTheme);
        QuoteChart.Style style = (QuoteChart.Style) optionsPanel.quoteChartStyleBox.getSelectedItem();
        OptionsManager.setQuoteChartStyle(style);
        OptionsManager.setReversedPositiveNegativeColor(optionsPanel.reverseColorBox.isSelected());
        OptionsManager.setAntiAlias(optionsPanel.antiAliasBox.isSelected());
        PersistenceManager.getDefalut().saveProperties();
        QuoteChartView.switchAllQuoteChartStyle(style);
        TickeringQuoteChartView.switchAllQuoteChartStyle(style);
        for (Object c : TopComponent.getRegistry().getOpened()) {
            if (c instanceof TopComponent) {
                ((TopComponent) c).repaint();
            }
        }
    }

    /**
     * This method is called when Cancel button has been pressed
     * revert any possible changes here
     */
    public void cancel() {
        OptionsManager.setOptionsLoaded(false);
    }

    public boolean isValid() {
        return true;
    }

    public boolean isChanged() {
        return changed;
    }

    public HelpCtx getHelpCtx() {
        return new HelpCtx("netbeans.optionsDialog.editor.example");
    }

    public JComponent getComponent(Lookup masterLookup) {
        return optionsPanel;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
    }
}
