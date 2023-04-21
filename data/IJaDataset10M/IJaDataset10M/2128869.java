package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.List;

/**
 * A report result.
 * 
 * Note that it is posible that a report result does not contain any rows but a map of ReportResultGroup instead.
 * 
 * A simple report just contains a list of rows:
 * <ul>
 * <li>ReportResult
 * <ul>
 * <li>ReportRow</li>
 * <li>ReportRow</li>
 * <li>...</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * A more complex report may contain a hierarchy of group-by:
 * <ul>
 * <li>ReportResult
 * <ul>
 * <li>ReportResultGroup
 * <ul>
 * <li>ReportResultGroup
 * <ul>
 * <li>ReportRow</li>
 * <li>ReportRow</li>
 * <li>...</li>
 * </ul>
 * </li>
 * <li>ReportResultGroup
 * <ul>
 * <li>ReportRow</li>
 * <li>ReportRow</li>
 * <li>...</li>
 * </ul>
 * </li>
 * <li>...</li>
 * </ul>
 * </li>
 * <li>...</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author junglas
 */
public class ReportResult extends ReportResultBase {

    /** Name of the report. */
    String m_name;

    public ReportResult(final String name) {
        super(new ArrayList<ColumnDefinition>(), new ArrayList<ColumnDefinition>());
        m_name = name;
    }

    public ReportResult(final String name, final List<ColumnDefinition> columns, final List<ColumnDefinition> groupByColumns) {
        super(columns, groupByColumns);
        m_name = name;
    }

    public String getName() {
        return m_name;
    }
}
