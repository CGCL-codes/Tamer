package org.shopformat.controller.admin.reports;

import org.hibernate.Criteria;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.shopformat.domain.product.Brand;
import org.shopformat.domain.product.Category;
import org.shopformat.domain.product.ProductGroup;
import org.shopformat.domain.product.ProductItem;

public class ChartService {

    public static final int MONTH_RANGE = 1;

    public static final int QUARTER_RANGE = 2;

    public static final int YEAR_RANGE = 3;

    public static final int ALL_RANGE = 4;

    private ChartService() {
    }

    public static AbstractDataset getStockChartBySupplier(int numberOfSlices) {
        return getStockChart(numberOfSlices, "productGroup.brand.supplier");
    }

    public static AbstractDataset getStockChartByBrand(int numberOfSlices) {
        return getStockChart(numberOfSlices, "productGroup.brand");
    }

    public static AbstractDataset getStockChartByCategory(int numberOfSlices, int categoryLevel) {
        DefaultPieDataset data = null;
        return data;
    }

    private static DefaultPieDataset getStockChart(int numberOfSlices, String path) {
        DefaultPieDataset ds = new DefaultPieDataset();
        return ds;
    }

    public static AbstractDataset getSalesChart(int rangeType) {
        return null;
    }

    public static AbstractDataset getSalesChart(Category category, int rangeType) {
        return null;
    }

    public static AbstractDataset getSalesChart(Brand brand, int rangeType) {
        return null;
    }

    public static AbstractDataset getSalesChart(ProductGroup productGroup, int rangeType) {
        return null;
    }

    public static AbstractDataset getSalesChart(ProductItem productItem) {
        return null;
    }

    public static AbstractDataset getSummaryChart() {
        return null;
    }

    public static AbstractDataset getSummaryChart(Category category) {
        return null;
    }

    private static void addToChart(DefaultCategoryDataset summaryChart, Criteria criteria, String title) {
    }

    public static void setValidStatusCriteria(Criteria criteria, String statusString) {
    }

    private static String getCategoryPath(Category category) {
        return "productItem.productGroup.category.parent.parent";
    }
}
