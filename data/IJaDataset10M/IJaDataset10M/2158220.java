package equilibrium.commons.report.generator.transformer;

import java.util.HashMap;
import java.util.Map;
import equilibrium.commons.bean.BeansUtil;
import equilibrium.commons.report.ReportContext;
import equilibrium.commons.report.chart.ChartPropertiesFactory;
import equilibrium.commons.report.chart.ChartType;
import equilibrium.commons.report.chart.EasyReportsChartProperties;
import equilibrium.commons.report.chart.NumberedChartProperties;
import equilibrium.commons.report.config.model.ChartTypeBean;

public class SubreportProperitesToChartPropertiesTransformer {

    private Map<ChartTypeBean, EasyReportsChartProperties> defaultProperties;

    public SubreportProperitesToChartPropertiesTransformer() {
        ChartPropertiesFactory chartPropertiesFactory = new ChartPropertiesFactory();
        defaultProperties = new HashMap<ChartTypeBean, EasyReportsChartProperties>();
        defaultProperties.put(ChartTypeBean.BAR, chartPropertiesFactory.create(ChartType.BAR));
        defaultProperties.put(ChartTypeBean.EMPHASIZED_BAR, chartPropertiesFactory.create(ChartType.EMPHASIZED_BAR));
        defaultProperties.put(ChartTypeBean.EMPHASIZED_STACKED_BAR, chartPropertiesFactory.create(ChartType.EMPHASIZED_STACKED_BAR));
        defaultProperties.put(ChartTypeBean.LINE, chartPropertiesFactory.create(ChartType.LINE));
        defaultProperties.put(ChartTypeBean.STACKED_BAR, chartPropertiesFactory.create(ChartType.STACKED_BAR));
    }

    public EasyReportsChartProperties transform(Map<String, Object> properties, ChartTypeBean chartType, ReportContext context) {
        NumberedChartProperties chartProperites = findDefaultChartProperties(chartType);
        String chartTitle = extractChartTitle(properties, context);
        chartProperites.setChartTitle(chartTitle);
        return chartProperites;
    }

    private String extractChartTitle(Map<String, Object> properties, ReportContext context) {
        String chartTitle = (String) properties.get("chartTitle");
        return context.parseStringExpression(chartTitle);
    }

    private NumberedChartProperties findDefaultChartProperties(ChartTypeBean chartType) {
        NumberedChartProperties chartProperties = (NumberedChartProperties) defaultProperties.get(chartType);
        if (BeansUtil.isNull(chartProperties)) {
            throw new IllegalArgumentException("There is no chart type " + BeansUtil.toQuotedString(chartType));
        }
        return chartProperties;
    }
}
