package org.jfree.report.structure;

/**
 * Creation-Date: 04.03.2006, 21:39:13
 *
 * @author Thomas Morgner
 */
public abstract class ReportDefinition extends Section {

    private String query;

    protected ReportDefinition() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public ReportDefinition getReport() {
        return this;
    }

    public Group getGroup() {
        return null;
    }
}
