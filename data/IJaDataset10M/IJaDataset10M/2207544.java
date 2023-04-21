package equilibrium.commons.report.chart;

import org.jfree.data.category.DefaultCategoryDataset;
import equilibrium.commons.report.chart.barred.emphasized.EmphasizedBarChartDataset;

public class ChartViewUtil {

    public static JFreeChartDatasetAdapter buildBarChartDataset() {
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        categoryDataset.addValue(1, "seria1", "Kategoria 1");
        categoryDataset.addValue(2, "seria2", "Kategoria 1");
        categoryDataset.addValue(3, "seria3", "Kategoria 1");
        categoryDataset.addValue(2, "seria1", "Kategoria 2");
        categoryDataset.addValue(3, "seria2", "Kategoria 2");
        categoryDataset.addValue(1, "seria3", "Kategoria 2");
        return new JFreeChartDatasetAdapter(categoryDataset);
    }

    public static JFreeChartDatasetAdapter buildLineChartDataset() {
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        categoryDataset.addValue(1, "seria1", "Kategoria 1");
        categoryDataset.addValue(5, "seria2", "Kategoria 1");
        categoryDataset.addValue(10, "seria3", "Kategoria 1");
        categoryDataset.addValue(8, "seria1", "Kategoria 2");
        categoryDataset.addValue(15, "seria2", "Kategoria 2");
        categoryDataset.addValue(18, "seria3", "Kategoria 2");
        categoryDataset.addValue(2, "seria1", "Kategoria 3");
        categoryDataset.addValue(10, "seria2", "Kategoria 3");
        categoryDataset.addValue(15, "seria3", "Kategoria 3");
        categoryDataset.addValue(4, "seria1", "Kategoria 4");
        categoryDataset.addValue(10, "seria2", "Kategoria 4");
        categoryDataset.addValue(6, "seria3", "Kategoria 4");
        return new JFreeChartDatasetAdapter(categoryDataset);
    }

    public static EmphasizedBarChartDataset buildEmphasizedBarChartDataset() {
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        categoryDataset.addValue(1, "seria1", "Kategoria 1");
        categoryDataset.addValue(8, "seria1", "Kategoria 2");
        categoryDataset.addValue(2, "seria1", "Kategoria 3");
        categoryDataset.addValue(4, "seria1", "Kategoria 4");
        categoryDataset.addValue(5, "seria1", "Kategoria 5");
        DefaultCategoryDataset emphasisDataset = new DefaultCategoryDataset();
        emphasisDataset.addValue(0.4, "seria1", "Kategoria 1");
        emphasisDataset.addValue(0.8, "seria1", "Kategoria 2");
        emphasisDataset.addValue(0.2, "seria1", "Kategoria 3");
        emphasisDataset.addValue(0.4, "seria1", "Kategoria 4");
        emphasisDataset.addValue(0.5, "seria1", "Kategoria 5");
        return new EmphasizedBarChartDataset(categoryDataset, emphasisDataset);
    }

    public static EmphasizedBarChartDataset buildEmphasizedBarChartDatasetViaAddMethod() {
        EmphasizedBarChartDataset result = new EmphasizedBarChartDataset();
        result.addValue(1, 0.2, "seria1", "Kategoria 1");
        result.addValue(8, 0.3, "seria1", "Kategoria 2");
        result.addValue(2, 0.4, "seria1", "Kategoria 3");
        result.addValue(4, 0.5, "seria1", "Kategoria 4");
        result.addValue(1, 0.6, "seria1", "Kategoria 5");
        return result;
    }

    public static JFreeChartDatasetAdapter buildStackedBarChartDataset() {
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        categoryDataset.addValue(10, "seria1", "Super długa nazwa kategorii którą muszę dopasować do ogólnego rozmiaru wykresu");
        categoryDataset.addValue(20, "seria2", "Kategoria 1");
        categoryDataset.addValue(10, "seria3", "Kategoria 1");
        categoryDataset.addValue(40, "seria4", "Kategoria 1");
        categoryDataset.addValue(20, "seria5", "Kategoria 1");
        categoryDataset.addValue(5, "seria1", "Kategoria 2");
        categoryDataset.addValue(5, "seria2", "Kategoria 2");
        categoryDataset.addValue(10, "seria3", "Kategoria 2");
        categoryDataset.addValue(15, "seria4", "Kategoria 2");
        categoryDataset.addValue(65, "seria5", "Kategoria 2");
        categoryDataset.addValue(20, "seria1", "Kategoria 3");
        categoryDataset.addValue(50, "seria2", "Kategoria 3");
        categoryDataset.addValue(10, "seria3", "Kategoria 3");
        categoryDataset.addValue(15, "seria4", "Kategoria 3");
        categoryDataset.addValue(5, "seria5", "Kategoria 3");
        categoryDataset.addValue(45.54, "seria1", "Kategoria 4");
        categoryDataset.addValue(12, "seria2", "Kategoria 4");
        categoryDataset.addValue(8, "seria3", "Kategoria 4");
        categoryDataset.addValue(4, "seria4", "Kategoria 4");
        categoryDataset.addValue(39.46, "seria5", "Kategoria 4");
        return new JFreeChartDatasetAdapter(categoryDataset);
    }

    public static EmphasizedBarChartDataset buildEmphasizedStackedBarChartDataset() {
        EmphasizedBarChartDataset result = new EmphasizedBarChartDataset();
        result.addValue(20.3, 0.2, "seria1", "Super długa nazwa kategorii którą muszę dopasować do ogólnego rozmiaru wykresu");
        result.addValue(50, 0.3, "seria1", "Kategoria 2");
        result.addValue(20, 0.4, "seria1", "Kategoria 3");
        result.addValue(40, 0.5, "seria1", "Kategoria 4");
        result.addValue(90, 2.6, "seria1", "Kategoria 5");
        result.addValue(90, 2.6, "seria1", "Kategoria 6");
        result.addValue(90, 2.6, "seria1", "Kategoria 7");
        result.addValue(90, 2.6, "seria1", "Kategoria 8");
        result.addValue(90, 2.6, "seria1", "Kategoria 9");
        result.addValue(50, 0.2, "seria2", "Super długa nazwa kategorii którą muszę dopasować do ogólnego rozmiaru wykresu");
        result.addValue(10, 0.3, "seria2", "Kategoria 2");
        result.addValue(20, 0.4, "seria2", "Kategoria 3");
        result.addValue(40, 0.5, "seria2", "Kategoria 4");
        result.addValue(4.3, 2.6, "seria2", "Kategoria 5");
        result.addValue(90, 2.6, "seria2", "Kategoria 6");
        result.addValue(90, 2.6, "seria2", "Kategoria 7");
        result.addValue(90, 2.6, "seria2", "Kategoria 8");
        result.addValue(90, 2.6, "seria2", "Kategoria 9");
        result.addValue(29.7, 0.2, "seria3", "Super długa nazwa kategorii którą muszę dopasować do ogólnego rozmiaru wykresu");
        result.addValue(40, 0.3, "seria3", "Kategoria 2");
        result.addValue(60, 0.4, "seria3", "Kategoria 3");
        result.addValue(20, 0.5, "seria3", "Kategoria 4");
        result.addValue(5.7, 2.6, "seria3", "Kategoria 5");
        result.addValue(90, 2.6, "seria3", "Kategoria 6");
        result.addValue(90, 2.6, "seria3", "Kategoria 7");
        result.addValue(90, 2.6, "seria3", "Kategoria 8");
        result.addValue(90, 2.6, "seria3", "Kategoria 9");
        return result;
    }

    public static EmphasizedBarChartDataset buildPositiveNegativeAgreggattedNonAggregatedDataset() {
        EmphasizedBarChartDataset result = new EmphasizedBarChartDataset();
        result.addValue(10.3, 0.2, "seria1", "positive aggregated");
        result.addValue(0, 0, "seria2", "positive aggregated");
        result.addValue(40, 0.3, "seria3", "positive aggregated");
        result.addValue(0, 0, "seria4", "positive aggregated");
        result.addValue(49.7, 0.3, "seria5", "positive aggregated");
        result.addValue(0, 0, "seria6", "positive aggregated");
        result.addValue(0, 0, "seria7", "positive aggregated");
        result.addValue(0, 0, "seria8", "positive aggregated");
        result.addValue(0, 0, "seria9", "positive aggregated");
        result.addValue(10.3, 0.2, "seria1", "positive no aggregated");
        result.addValue(20, 0.2, "seria2", "positive no aggregated");
        result.addValue(29.7, 0.3, "seria3", "positive no aggregated");
        result.addValue(30, 0.3, "seria4", "positive no aggregated");
        result.addValue(10, 0.3, "seria5", "positive no aggregated");
        result.addValue(0, 0, "seria6", "positive no aggregated");
        result.addValue(0, 0, "seria7", "positive no aggregated");
        result.addValue(0, 0, "seria8", "positive no aggregated");
        result.addValue(0, 0, "seria9", "positive no aggregated");
        result.addValue(0, 0, "seria1", "negative aggregated");
        result.addValue(0, 0, "seria2", "negative aggregated");
        result.addValue(0, 0, "seria3", "negative aggregated");
        result.addValue(0, 0, "seria4", "negative aggregated");
        result.addValue(10, 0.3, "seria5", "negative aggregated");
        result.addValue(0, 0, "seria6", "negative aggregated");
        result.addValue(40, 0.3, "seria7", "negative aggregated");
        result.addValue(0, 0, "seria8", "negative aggregated");
        result.addValue(50, 0.3, "seria9", "negative aggregated");
        result.addValue(0, 0, "seria1", "negative no aggregated");
        result.addValue(0, 0, "seria2", "negative no aggregated");
        result.addValue(0, 0, "seria3", "negative no aggregated");
        result.addValue(0, 0, "seria4", "negative no aggregated");
        result.addValue(20, 0.3, "seria5", "negative no aggregated");
        result.addValue(30, 0.3, "seria6", "negative no aggregated");
        result.addValue(20, 0.3, "seria7", "negative no aggregated");
        result.addValue(15, 0.3, "seria8", "negative no aggregated");
        result.addValue(15, 0.3, "seria9", "Dział obsługi kilentów abonenckich");
        return result;
    }
}
