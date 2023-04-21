package com.jrefinery.chart;

import java.util.*;

/**
 * This class contains static methods that perform various useful functions relating to data
 * sources.
 */
public class DataSources {

    /**
   * Returns the minimum domain value for the specified data source - this may involve iterating
   * over the entire data-set.
   */
    public static Number getMinimumDomainValue(DataSource data) {
        if (data instanceof DomainInfo) {
            DomainInfo info = (DomainInfo) data;
            return info.getMinimumDomainValue();
        } else if (data instanceof CategoryDataSource) {
            return null;
        } else if (data instanceof XYDataSource) {
            XYDataSource xyData = (XYDataSource) data;
            double minimum = Double.MAX_VALUE;
            int seriesCount = xyData.getSeriesCount();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                int itemCount = xyData.getItemCount(seriesIndex);
                for (int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
                    Number value = xyData.getXValue(seriesIndex, itemIndex);
                    if (value.doubleValue() < minimum) {
                        minimum = value.doubleValue();
                    }
                }
            }
            return new Double(minimum);
        } else return null;
    }

    /**
   * Returns the maximum domain value for the specified data source - this may involve iterating
   * over the entire data-set.
   */
    public static Number getMaximumDomainValue(DataSource data) {
        if (data instanceof DomainInfo) {
            DomainInfo info = (DomainInfo) data;
            return info.getMaximumDomainValue();
        } else if (data instanceof CategoryDataSource) {
            return null;
        } else if (data instanceof XYDataSource) {
            XYDataSource xyData = (XYDataSource) data;
            double maximum = Double.MIN_VALUE;
            int seriesCount = xyData.getSeriesCount();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                int itemCount = xyData.getItemCount(seriesIndex);
                for (int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
                    Number value = xyData.getXValue(seriesIndex, itemIndex);
                    if (value.doubleValue() > maximum) {
                        maximum = value.doubleValue();
                    }
                }
            }
            return new Double(maximum);
        } else return null;
    }

    /**
   * Returns the minimum range value for the specified data source - this may involve iterating
   * over the entire data-set.
   */
    public static Number getMinimumRangeValue(DataSource data) {
        if (data instanceof RangeInfo) {
            RangeInfo info = (RangeInfo) data;
            return info.getMinimumRangeValue();
        } else if (data instanceof CategoryDataSource) {
            CategoryDataSource categoryData = (CategoryDataSource) data;
            double minimum = Double.MAX_VALUE;
            int seriesCount = categoryData.getSeriesCount();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                Iterator iterator = categoryData.getCategories().iterator();
                while (iterator.hasNext()) {
                    Object category = iterator.next();
                    Number value = categoryData.getValue(seriesIndex, category);
                    if (value == null) continue;
                    if (value.doubleValue() < minimum) {
                        minimum = value.doubleValue();
                    }
                }
            }
            return new Double(minimum);
        } else if (data instanceof XYDataSource) {
            XYDataSource xyData = (XYDataSource) data;
            double minimum = Double.MAX_VALUE;
            int seriesCount = xyData.getSeriesCount();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                int itemCount = xyData.getItemCount(seriesIndex);
                for (int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
                    Number value = xyData.getYValue(seriesIndex, itemIndex);
                    if (value == null) continue;
                    if (value.doubleValue() < minimum) {
                        minimum = value.doubleValue();
                    }
                }
            }
            return new Double(minimum);
        } else return null;
    }

    /**
   * Returns the maximum range value for the specified data source - this may involve iterating
   * over the entire data-set.
   */
    public static Number getMaximumRangeValue(DataSource data) {
        if (data instanceof RangeInfo) {
            RangeInfo info = (RangeInfo) data;
            return info.getMaximumRangeValue();
        } else if (data instanceof CategoryDataSource) {
            CategoryDataSource categoryData = (CategoryDataSource) data;
            double maximum = Double.MIN_VALUE;
            int seriesCount = categoryData.getSeriesCount();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                Iterator iterator = categoryData.getCategories().iterator();
                while (iterator.hasNext()) {
                    Object category = iterator.next();
                    Number value = categoryData.getValue(seriesIndex, category);
                    if (value == null) continue;
                    if (value.doubleValue() > maximum) {
                        maximum = value.doubleValue();
                    }
                }
            }
            return new Double(maximum);
        } else if (data instanceof XYDataSource) {
            XYDataSource xyData = (XYDataSource) data;
            double maximum = Double.MIN_VALUE;
            int seriesCount = xyData.getSeriesCount();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                int itemCount = xyData.getItemCount(seriesIndex);
                for (int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
                    Number value = xyData.getYValue(seriesIndex, itemIndex);
                    if (value == null) continue;
                    if (value.doubleValue() > maximum) {
                        maximum = value.doubleValue();
                    }
                }
            }
            return new Double(maximum);
        } else return null;
    }
}
