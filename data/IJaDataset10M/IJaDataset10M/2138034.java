package org.lifeforachild.web.Report;

import java.util.List;
import org.lifeforachild.domain.Report;
import org.lifeforachild.domain.User;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;

/**
 * 
 * {@link User} specific report generator.
 * 
 * @author Serena Potts
 *
 */
public class UserReportGenerator extends ReportGenerator {

    /**
     * Add columns specific to the user report.
     * @param drb The report builder.
     * @throws ColumnBuilderException
     */
    void addColumns(DynamicReportBuilder drb, Object[] fields) throws ColumnBuilderException {
        addColumn(drb, "firstName", "First Name", String.class, 85);
        addColumn(drb, "lastName", "Last Name", String.class, 85);
        addColumn(drb, "username", "Username", String.class, 85);
        addColumn(drb, "enabled", "Enabled", Boolean.class, 85);
        addColumn(drb, "country.name", "Country", String.class, 85);
        addColumn(drb, "centre.name", "Centre", String.class, 85);
    }

    public List buildQuery(Report report) {
        return null;
    }

    public Object[] getDisplayFieldsList(String fields) {
        return new Object[] {};
    }

    public Object[] getDisplayFields(Report report) {
        return new Object[] {};
    }

    public String getFieldListAsString(Report report) {
        return "";
    }
}
