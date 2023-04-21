package com.google.gwt.benchmarks.viewer.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;
import java.util.List;

/**
 * A data object for Report.
 */
public class Report implements IsSerializable {

    private List<Category> categories;

    private Date date;

    private String dateString;

    private String gwtVersion;

    private String id;

    public List<Category> getCategories() {
        return categories;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString() {
        return dateString;
    }

    public String getGwtVersion() {
        return gwtVersion;
    }

    public String getId() {
        return id;
    }

    public ReportSummary getSummary() {
        int numTests = 0;
        boolean testsPassed = true;
        for (int i = 0; i < categories.size(); ++i) {
            Category c = categories.get(i);
            List<Benchmark> benchmarks = c.getBenchmarks();
            numTests += benchmarks.size();
        }
        return new ReportSummary(id, date, dateString, numTests, testsPassed);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public void setGwtVersion(String gwtVersion) {
        this.gwtVersion = gwtVersion;
    }

    public void setId(String id) {
        this.id = id;
    }
}
