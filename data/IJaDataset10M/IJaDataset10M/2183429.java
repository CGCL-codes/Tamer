package de.objectcode.time4u.server.web.ui.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.contexts.ServletLifecycle;
import org.jboss.seam.core.ConversationPropagation;
import org.jboss.seam.core.Manager;
import org.jboss.seam.web.AbstractResource;
import org.jboss.seam.web.ServletContexts;
import de.objectcode.time4u.server.ejb.seam.api.report.ColumnDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.ColumnType;
import de.objectcode.time4u.server.ejb.seam.api.report.CrossTableResult;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportResult;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportResultGroup;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportRow;
import de.objectcode.time4u.server.ejb.seam.api.report.ValueLabelPair;
import de.objectcode.time4u.server.ejb.seam.api.report.CrossTableResult.CrossTableRow;
import de.objectcode.time4u.server.web.ui.converter.StringArrayConverter;
import de.objectcode.time4u.server.web.ui.converter.TimeConverter;

@Scope(ScopeType.APPLICATION)
@Name("user.csvInteractiveReportResource")
@BypassInterceptors
@Install(precedence = Install.APPLICATION)
@Startup
public class CSVInteractiveReportResource extends AbstractResource {

    private static final String DELIM = ";";

    private static final String FIELD = "\"";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    private static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Override
    public void getResource(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        ServletLifecycle.beginRequest(request);
        try {
            ServletContexts.instance().setRequest(request);
            ConversationPropagation.instance().restoreConversationId(request.getParameterMap());
            Manager.instance().restoreConversation();
            ServletLifecycle.resumeConversation(request);
            final InteractiveFilter interactiveFilter = (InteractiveFilter) Contexts.getConversationContext().get("user.interactiveFilter");
            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            final PrintWriter out = response.getWriter();
            if (interactiveFilter.isHasPerson()) {
                final ReportResult reportResult = (ReportResult) Contexts.getConversationContext().get("user.reportResult");
                dumpReport(out, reportResult);
            } else {
                final InteractiveReportController controller = (InteractiveReportController) Contexts.getConversationContext().get("user.interactiveReportController");
                final CrossTableResult crossTable = (CrossTableResult) Contexts.getConversationContext().get("user.interactiveCrossTable");
                dumpCrossTable(out, crossTable, controller.getValueConverter());
            }
            out.close();
        } finally {
            ServletLifecycle.endRequest(request);
        }
    }

    @Override
    public String getResourcePath() {
        return "/csvinteractivereport";
    }

    private void dumpReport(final PrintWriter out, final ReportResult reportResult) throws IOException {
        boolean first = true;
        for (final ColumnDefinition column : reportResult.getGroupByColumns()) {
            if (!first) {
                out.print(DELIM);
            }
            out.print(toCSVString(column.getHeader()));
            first = false;
        }
        for (final ColumnDefinition column : reportResult.getColumns()) {
            if (!first) {
                out.print(DELIM);
            }
            out.print(toCSVString(column.getHeader()));
            first = false;
        }
        out.println();
        for (final ReportResultGroup group : reportResult.getGroups()) {
            writeReportGroup(0, group, out);
        }
        for (final ReportRow row : reportResult.getRows()) {
            writeReportRow(row, reportResult.getColumns(), out);
        }
    }

    private void dumpCrossTable(final PrintWriter out, final CrossTableResult crossTable, final Converter valueConverter) throws IOException {
        out.print(DELIM);
        for (final ValueLabelPair column : crossTable.getColumnHeaders()) {
            out.print(toCSVString(column.getLabel().toString()));
            out.print(DELIM);
        }
        out.println();
        for (final CrossTableRow row : crossTable.getRows()) {
            out.print(toCSVString(row.getRowHeader().getLabel().toString()));
            out.print(DELIM);
            for (final Object cell : row.getData()) {
                out.print(valueConverter.getAsString(FacesContext.getCurrentInstance(), null, cell));
                out.print(DELIM);
            }
            out.print(valueConverter.getAsString(FacesContext.getCurrentInstance(), null, row.getRowAggregate()));
            out.println();
        }
        out.print(DELIM);
        for (final Object aggregate : crossTable.getColumnAggregates()) {
            out.print(valueConverter.getAsString(FacesContext.getCurrentInstance(), null, aggregate));
            out.print(DELIM);
        }
        out.print(valueConverter.getAsString(FacesContext.getCurrentInstance(), null, crossTable.getTotalAggregate()));
        out.println();
    }

    private void writeReportRow(final ReportRow row, final List<ColumnDefinition> columns, final PrintWriter out) throws IOException {
        boolean first = true;
        for (int i = 0; i < columns.size(); i++) {
            if (!first) {
                out.print(DELIM);
            }
            writeValue(row.getData()[i], columns.get(i).getColumnType(), out);
            first = false;
        }
        out.println();
    }

    private void writeReportGroup(final int depth, final ReportResultGroup group, final PrintWriter out) throws IOException {
        for (int i = 0; i < depth; i++) {
            out.print(DELIM);
        }
        out.print(String.valueOf(group.getLabel()));
        for (int i = 0; i < group.getGroupByColumns().size() + group.getColumns().size(); i++) {
            out.print(DELIM);
        }
        out.println();
        for (final ReportResultGroup subGroup : group.getGroups()) {
            writeReportGroup(depth + 1, subGroup, out);
        }
        for (final ReportRow row : group.getRows()) {
            for (int i = 0; i < depth + 1; i++) {
                out.print(DELIM);
            }
            writeReportRow(row, group.getColumns(), out);
        }
    }

    private void writeValue(final Object value, final ColumnType columnType, final PrintWriter out) throws IOException {
        switch(columnType) {
            case NAME:
            case DESCRIPTION:
                if (value != null && value instanceof String) {
                    out.print(toCSVString((String) value));
                }
                break;
            case NAME_ARRAY:
                out.print(toCSVString(StringArrayConverter.format(value)));
                break;
            case INTEGER:
                if (value != null) {
                    out.print(value.toString());
                }
                break;
            case DATE:
                if (value != null && value instanceof Date) {
                    out.print(DATE_FORMAT.format((Date) value));
                }
                break;
            case TIME:
                if (value != null && value instanceof Integer) {
                    out.print(TimeConverter.format((Integer) value));
                }
                break;
            case TIMESTAMP:
                if (value != null && value instanceof Date) {
                    out.print(TIMESTAMP_FORMAT.format((Date) value));
                }
                break;
        }
    }

    private String toCSVString(final String str) {
        return FIELD + str.replace(FIELD, FIELD + FIELD) + FIELD;
    }
}
