package net.sf.ussrp.bus.transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.TreeMap;
import net.sf.ussrp.bus.transformation.BaseTransform;
import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.charts.design.JRDesignAreaPlot;
import net.sf.jasperreports.charts.design.JRDesignBarPlot;
import net.sf.jasperreports.charts.design.JRDesignBar3DPlot;
import net.sf.jasperreports.charts.design.JRDesignPie3DPlot;
import net.sf.jasperreports.charts.design.JRDesignCategorySeries;
import net.sf.jasperreports.charts.design.JRDesignCategoryDataset;
import net.sf.jasperreports.charts.design.JRDesignPieDataset;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
* ChartTransform is used to manipulate charts as defined in the report design.
*/
public class ChartTransform extends BaseTransform {

    protected final Log logger = LogFactory.getLog(getClass());

    public void Chart() {
        ;
    }

    /**
	* Changes a chart's style from one style to another at runtime.
	* <p>
	* @param  	jasperDesign  a JasperDesign object to edit
	* @param  	index from the fixed order that ChartTransform.getCharts() will return
	* all charts (0 based).
	* @param  	newChartType  the new chart type (e.g. JRDesignChart.CHART_TYPE_STACKEDBAR)
	* for the chart specified by the key parameter
	*/
    public void changeChart(JasperDesign jasperDesign, int anIndex, byte newChartType) throws Exception {
        if (jasperDesign == null) throw new Exception("changeChart() called with a null jasperDesign");
        logger.info("CHANGE chart at index " + anIndex + " to type " + newChartType);
        int index = 0;
        JRElement[] jre = null;
        for (Iterator it = getJRBands(jasperDesign).iterator(); it.hasNext(); ) {
            JRDesignBand jrBand = (JRDesignBand) it.next();
            jre = jrBand.getElements();
            if (jre == null) continue;
            for (int i = 0; i < jre.length; i++) {
                if ((jre[i] instanceof JRDesignChart)) {
                    index++;
                    if (anIndex + 1 != index) continue;
                    JRDesignChart jdc = (JRDesignChart) jre[i];
                    JRChartDataset oldDs = (JRChartDataset) jdc.getDataset();
                    byte oldChartType = jdc.getChartType();
                    if ((!(ChartInfo.isSupportedChart(newChartType))) || (!(ChartInfo.isSupportedChart(oldChartType)))) throw new Exception("ChartTransform.changeChart(), " + "unsupported chart type: oldType: " + oldChartType + ", newType: " + newChartType);
                    JRChartPlot oldPlot = jdc.getPlot();
                    jdc.setChartType(newChartType);
                    JRChartPlot newPlot = jdc.getPlot();
                    JRChartDataset newDs = jdc.getDataset();
                    transformDatasetAndPlot(oldDs, newDs, oldPlot, newPlot, oldChartType, newChartType);
                }
            }
        }
    }

    public JRDesignChart findChart(JasperDesign jasperDesign, int anIndex) {
        int index = 0;
        JRElement[] jre = null;
        for (Iterator it = getJRBands(jasperDesign).iterator(); it.hasNext(); ) {
            JRDesignBand jrBand = (JRDesignBand) it.next();
            jre = jrBand.getElements();
            if (jre == null) continue;
            for (int i = 0; i < jre.length; i++) {
                if ((jre[i] instanceof JRDesignChart)) {
                    index++;
                    if (anIndex + 1 == index) return (JRDesignChart) jre[i];
                }
            }
        }
        return null;
    }

    public List getCharts(JasperDesign jasperDesign) {
        int index = 0;
        JRElement[] jre = null;
        ArrayList cis = new ArrayList();
        for (Iterator it = getJRBands(jasperDesign).iterator(); it.hasNext(); ) {
            JRDesignBand jrBand = (JRDesignBand) it.next();
            jre = jrBand.getElements();
            if (jre == null) continue;
            for (int i = 0; i < jre.length; i++) {
                if ((jre[i] instanceof JRDesignChart) && (ChartInfo.isSupportedChart(((JRDesignChart) jre[i]).getChartType()))) {
                    cis.add(new ChartInfo((JRDesignChart) jre[i]));
                }
            }
        }
        return cis;
    }

    private void transformDatasetAndPlot(JRChartDataset oldDataset, JRChartDataset newDataset, JRChartPlot oldPlot, JRChartPlot newPlot, byte oldChartType, byte newChartType) {
        logger.info("in ChartTransform >> transformDatasetAndPlot()");
        if (oldDataset.getDatasetType() == JRChartDataset.CATEGORY_DATASET && newDataset.getDatasetType() == JRChartDataset.PIE_DATASET) {
            JRCategorySeries[] dsSer = ((JRDesignCategoryDataset) oldDataset).getSeries();
            if (dsSer.length > 0) {
                ((JRDesignPieDataset) newDataset).setValueExpression(dsSer[0].getValueExpression());
                ((JRDesignPieDataset) newDataset).setKeyExpression(dsSer[0].getCategoryExpression());
                ((JRDesignPieDataset) newDataset).setLabelExpression(dsSer[0].getLabelExpression());
            }
        } else if (oldDataset.getDatasetType() == JRChartDataset.PIE_DATASET && newDataset.getDatasetType() == JRChartDataset.CATEGORY_DATASET) {
            JRDesignCategorySeries cs = new JRDesignCategorySeries();
            cs.setCategoryExpression(((JRDesignPieDataset) oldDataset).getKeyExpression());
            cs.setValueExpression(((JRDesignPieDataset) oldDataset).getValueExpression());
            cs.setLabelExpression(((JRDesignPieDataset) oldDataset).getLabelExpression());
            JRDesignExpression jrde = new JRDesignExpression();
            jrde.setValueClassName("String");
            jrde.addTextChunk("\"Value\"");
            cs.setSeriesExpression(jrde);
            ((JRDesignCategoryDataset) newDataset).addCategorySeries(cs);
        } else if (oldDataset.getDatasetType() == JRChartDataset.CATEGORY_DATASET && newDataset.getDatasetType() == JRChartDataset.CATEGORY_DATASET) {
            JRCategorySeries[] dsSer = ((JRDesignCategoryDataset) oldDataset).getSeries();
            for (int l = 0; l < dsSer.length; l++) {
                ((JRDesignCategoryDataset) newDataset).addCategorySeries((JRDesignCategorySeries) dsSer[l]);
            }
            JRExpression s1 = null, s2 = null;
            boolean s3 = false;
            if (oldChartType == JRDesignChart.CHART_TYPE_AREA) {
                JRDesignAreaPlot jap = (JRDesignAreaPlot) oldPlot;
                s1 = jap.getCategoryAxisLabelExpression();
                s2 = jap.getValueAxisLabelExpression();
            }
            if (oldChartType == JRDesignChart.CHART_TYPE_STACKEDBAR) {
                JRDesignBarPlot jap = (JRDesignBarPlot) oldPlot;
                s1 = jap.getCategoryAxisLabelExpression();
                s2 = jap.getValueAxisLabelExpression();
                s3 = jap.isShowLabels();
            }
            if (oldChartType == JRDesignChart.CHART_TYPE_BAR3D) {
                JRDesignBar3DPlot jap = (JRDesignBar3DPlot) oldPlot;
                s1 = jap.getCategoryAxisLabelExpression();
                s2 = jap.getValueAxisLabelExpression();
                s3 = jap.isShowLabels();
            }
            newPlot.setBackcolor(oldPlot.getBackcolor());
            newPlot.setOrientation(oldPlot.getOrientation());
            newPlot.setBackgroundAlpha(oldPlot.getBackgroundAlpha());
            newPlot.setForegroundAlpha(oldPlot.getForegroundAlpha());
            if (newChartType == JRDesignChart.CHART_TYPE_STACKEDBAR) {
                JRDesignBarPlot jbp = (JRDesignBarPlot) newPlot;
                jbp.setCategoryAxisLabelExpression(s1);
                jbp.setValueAxisLabelExpression(s2);
                jbp.setShowTickLabels(true);
                jbp.setShowTickMarks(true);
                jbp.setShowLabels(s3);
            }
            if (newChartType == JRDesignChart.CHART_TYPE_BAR3D) {
                JRDesignBar3DPlot jbp = (JRDesignBar3DPlot) newPlot;
                jbp.setCategoryAxisLabelExpression(s1);
                jbp.setValueAxisLabelExpression(s2);
                jbp.setShowLabels(s3);
            }
            if (newChartType == JRDesignChart.CHART_TYPE_AREA) {
                JRDesignAreaPlot jbp = (JRDesignAreaPlot) newPlot;
                jbp.setCategoryAxisLabelExpression(s1);
                jbp.setValueAxisLabelExpression(s2);
            }
            if (newChartType == JRDesignChart.CHART_TYPE_PIE3D) {
                JRDesignPie3DPlot jbp = (JRDesignPie3DPlot) newPlot;
            }
        }
    }
}
