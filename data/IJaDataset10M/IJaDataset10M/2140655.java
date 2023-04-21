package org.vikamine.gui.subgroup.visualization.continuousDistribution;

public class DistributionBarInformation {

    private double subgroupTotal;

    private double populationTotal;

    private double subgroupBar;

    private double populationBar;

    private double maxAbsolute;

    private double minValueX;

    private double maxValueX;

    private double maxPercentage;

    private double widthPercentage;

    public DistributionBarInformation(double n_subgroup_bar, double n_population_bar, double n_max, double maxPercentage, double minValueX, double maxValueX, double n_subgroup_total, double n_population_total, double widthPercentage) {
        super();
        this.subgroupBar = n_subgroup_bar;
        this.populationBar = n_population_bar;
        this.subgroupTotal = n_subgroup_total;
        this.populationTotal = n_population_total;
        this.populationBar = n_population_bar;
        this.maxAbsolute = n_max;
        this.minValueX = minValueX;
        this.maxValueX = maxValueX;
        this.maxPercentage = maxPercentage;
        this.widthPercentage = widthPercentage;
    }

    public double getMaxAbsolute() {
        return maxAbsolute;
    }

    public double getMaxPercentage() {
        return maxPercentage;
    }

    public double getMaxValueX() {
        return maxValueX;
    }

    public double getMinValueX() {
        return minValueX;
    }

    public double getPopulationBar() {
        return populationBar;
    }

    public double getPopulationTotal() {
        return populationTotal;
    }

    public double getSubgroupBar() {
        return subgroupBar;
    }

    public double getSubgroupTotal() {
        return subgroupTotal;
    }

    public double getWidthPercentage() {
        return widthPercentage;
    }
}
