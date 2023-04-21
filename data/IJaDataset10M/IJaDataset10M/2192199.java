package com.adobe.ac.ncss.metrics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProjectMetrics {

    private final String date;

    private final String time;

    private TotalPackageMetrics totalPackages;

    private AverageFunctionMetrics averageFunctions;

    private AverageClassMetrics averageObjects;

    private List<ClassMetrics> classMetrics;

    private List<FunctionMetrics> functionMetrics;

    private List<PackageMetrics> packageMetrics;

    public ProjectMetrics() {
        super();
        final Date now = new Date();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d", Locale.US);
        final SimpleDateFormat timeFormat = new SimpleDateFormat("k:m:s", Locale.US);
        this.date = dateFormat.format(now);
        this.time = timeFormat.format(now);
        this.classMetrics = new ArrayList<ClassMetrics>();
        this.functionMetrics = new ArrayList<FunctionMetrics>();
        this.packageMetrics = new ArrayList<PackageMetrics>();
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public List<ClassMetrics> getClassMetrics() {
        return classMetrics;
    }

    public List<FunctionMetrics> getFunctionMetrics() {
        return functionMetrics;
    }

    public List<PackageMetrics> getPackageMetrics() {
        return packageMetrics;
    }

    public void setClassMetrics(final List<ClassMetrics> classMetrics) {
        this.classMetrics = classMetrics;
    }

    public void setFunctionMetrics(final List<FunctionMetrics> functionMetrics) {
        this.functionMetrics = functionMetrics;
    }

    public void setPackageMetrics(final List<PackageMetrics> packageMetrics) {
        this.packageMetrics = packageMetrics;
    }

    public void setTotalPackages(final TotalPackageMetrics totalPackages) {
        this.totalPackages = totalPackages;
    }

    public TotalPackageMetrics getTotalPackages() {
        return totalPackages;
    }

    public AverageFunctionMetrics getAverageFunctions() {
        return averageFunctions;
    }

    public void setAverageFunctions(final AverageFunctionMetrics averageFunctions) {
        this.averageFunctions = averageFunctions;
    }

    public AverageClassMetrics getAverageObjects() {
        return averageObjects;
    }

    public void setAverageObjects(final AverageClassMetrics averageObjects) {
        this.averageObjects = averageObjects;
    }
}
