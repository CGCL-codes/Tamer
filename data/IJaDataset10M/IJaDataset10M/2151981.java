package org.lcelb.accounts.manager.ui.workbench.views.chart.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ChartType;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.lcelb.accounts.manager.common.util.Couple;
import org.lcelb.accounts.manager.data.extensions.transaction.MonthTransactions;
import org.lcelb.accounts.manager.data.helper.computation.AbstractTotalHandler;
import org.lcelb.accounts.manager.data.helper.computation.MonthHolder;
import org.lcelb.accounts.manager.data.helper.computation.category.CategoryMonthTotal;
import org.lcelb.accounts.manager.data.helper.computation.category.CategoryMonthTotalHandler;
import org.lcelb.accounts.manager.data.transaction.category.AbstractCategory;
import org.lcelb.accounts.manager.ui.extensions.AccountsManagerUIExtensionsActivator;
import org.lcelb.accounts.manager.ui.extensions.UIPreferenceConstants;
import org.lcelb.accounts.manager.ui.helper.MiscHelper;
import org.lcelb.accounts.manager.ui.widgets.chart.AbstractChart;
import org.lcelb.accounts.manager.ui.widgets.chart.BarChart;
import org.lcelb.accounts.manager.ui.widgets.chart.PieChart;
import org.lcelb.accounts.manager.ui.widgets.chart.TubeChart;
import org.lcelb.accounts.manager.ui.workbench.views.chart.AbstractMonthTotalsChartPage;

/**
 * Displays a pie chart representing month totals per category for selected month.
 * @author La Carotte
 */
public class CategoryMonthTotalsChartPage extends AbstractMonthTotalsChartPage implements IPropertyChangeListener {

    /**
   * Chart type i.e Tube or Pie chart.
   */
    private ChartType _type;

    /**
   * Category vs Color mappings.
   */
    private static Map<AbstractCategory, RGB> __categoryColorMappings;

    /**
   * Alpha component value.
   */
    private int _alphaComponent;

    /**
   * Palette used to render base serie.
   */
    private Palette _palette;

    /**
   * Constructor.
   * @param chartType_p
   */
    public CategoryMonthTotalsChartPage(ChartType chartType_p) {
        _type = chartType_p;
        _palette = AttributeFactory.eINSTANCE.createPalette();
        _palette.setName("AM custom palette");
        __categoryColorMappings = new HashMap<AbstractCategory, RGB>(0);
        IPreferenceStore preferenceStore = AccountsManagerUIExtensionsActivator.getDefault().getPreferenceStore();
        _alphaComponent = preferenceStore.getInt(UIPreferenceConstants.CHART_VIEWS_ALPHA_COMPONENT);
        preferenceStore.addPropertyChangeListener(this);
    }

    /**
   * Add a color entry for given category month total.
   * @param categoryMonthTotal_p
   * @param colorEntries_p
   */
    private void addColorEntry(CategoryMonthTotal categoryMonthTotal_p, List<Fill> colorEntries_p) {
        IPreferenceStore preferenceStore = AccountsManagerUIExtensionsActivator.getDefault().getPreferenceStore();
        RGB colorRGB = MiscHelper.getColor(preferenceStore, MiscHelper.getCategoryPreferenceName(categoryMonthTotal_p.getName()));
        if (null == colorRGB) {
            AbstractCategory category = categoryMonthTotal_p.getCategory();
            colorRGB = __categoryColorMappings.get(category);
            if (null == colorRGB) {
                int[] color = MiscHelper.getColorFor(categoryMonthTotal_p.getName(), 255);
                colorRGB = new RGB(color[0], color[1], color[2]);
                __categoryColorMappings.put(category, colorRGB);
            }
        }
        colorEntries_p.add(ColorDefinitionImpl.create(colorRGB.red, colorRGB.green, colorRGB.blue, _alphaComponent));
    }

    /**
   * Change the chart type with given value and rebuilt it.
   * @param type_p
   */
    public void changeChartType(ChartType type_p) {
        _type = type_p;
        MonthHolder holder = (MonthHolder) getTotalHandler().getAdapter(MonthHolder.class);
        if (null == holder) {
            return;
        }
        setChart(buildChart());
        setChartTitle(holder.getMonth(), false, false);
        markDirty();
        renderChart(false, false);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    protected AbstractChart createChart(Couple<DataSet, DataSet> dataSets_p) {
        AbstractChart result = null;
        if (ChartType.BAR_LITERAL == _type) {
            result = new BarChart(null, dataSets_p.getKey(), dataSets_p.getValue());
            ((TubeChart) result).setxAxisLabelStyle(SWT.VERTICAL);
        } else if (ChartType.PIE_LITERAL == _type) {
            result = new PieChart(null, dataSets_p.getKey(), dataSets_p.getValue());
        }
        result.setBaseSeriePalette(_palette);
        return result;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    protected AbstractTotalHandler createTotalHandler(MonthTransactions monthTransactions_p) {
        return new CategoryMonthTotalHandler(monthTransactions_p);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void dispose() {
        super.dispose();
        AccountsManagerUIExtensionsActivator.getDefault().getPreferenceStore().removePropertyChangeListener(this);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    protected Couple<DataSet, DataSet> getDataSets() {
        Object[] elements = getTotalHandler().getElements();
        List<String> baseDataSet = new ArrayList<String>(elements.length);
        List<Double> orthogonalDataSet = new ArrayList<Double>(elements.length);
        Palette palette = getPalette();
        List<Fill> colorEntries = palette.getEntries();
        colorEntries.clear();
        for (Object element : elements) {
            CategoryMonthTotal currentElement = (CategoryMonthTotal) element;
            double total = currentElement.getTotal();
            if (0.0 > total) {
                baseDataSet.add(currentElement.getName());
                orthogonalDataSet.add(new Double(handleOrthogonalValue(total)));
                addColorEntry(currentElement, colorEntries);
            }
        }
        return new Couple<DataSet, DataSet>(TextDataSetImpl.create(baseDataSet), NumberDataSetImpl.create(orthogonalDataSet));
    }

    /**
   * Get the palette used in base serie.
   * @return the palette
   */
    protected Palette getPalette() {
        return _palette;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void propertyChange(PropertyChangeEvent event_p) {
        String property = event_p.getProperty();
        boolean renderChart = false;
        if (UIPreferenceConstants.CHART_VIEWS_ALPHA_COMPONENT.equals(property)) {
            _alphaComponent = ((Integer) event_p.getNewValue()).intValue();
            renderChart = true;
        } else if (MiscHelper.isCategoryPreferenceName(property)) {
            renderChart = true;
        }
        if (renderChart) {
            renderChart(false, true);
        }
    }
}
