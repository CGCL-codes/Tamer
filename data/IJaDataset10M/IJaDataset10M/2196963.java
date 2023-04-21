package de.haumacher.timecollect.common.report;

public interface ReportBuilder {

    Object createReport();

    Object createTable(Object report);

    Object createHead(Object table);

    void createColumn(Object head, int col, String label);

    void finishHead(Object head);

    Object createBody(Object table);

    void createHeading(Object body, int level, String label);

    Object createRow(Object body);

    void createCell(Object row, int col, String value);

    void finishRow(Object row);

    void finishBody(Object body);

    void finishTable(Object table);

    void finishReport(Object report);
}
