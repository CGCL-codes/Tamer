package com.googlecode.mvnmigrate.report;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import org.apache.ibatis.migration.Change;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.project.MavenProject;

/**
 * View of staus report.
 *
 * @version $Id: MigrationStatusReportView.java 152 2010-07-24 10:37:57Z marco.speranza79 $
 */
public class MigrationStatusReportView {

    /**
     * Generates the report.
     *
     * @param changes list of {@link Change}
     * @param sink the {@link Sink} instance
     * @param bundle the {@link ResourceBundle} instance
     */
    public void generateReport(Map<MavenProject, List<Change>> changes, Sink sink, ResourceBundle bundle, boolean isAggregate) {
        sink.head();
        sink.title();
        sink.text(bundle.getString("migration.status.report.header"));
        sink.title_();
        sink.head_();
        sink.body();
        sink.section1();
        sink.sectionTitle1();
        sink.text(bundle.getString("migration.status.report.mainTitle"));
        sink.sectionTitle1_();
        sink.section1_();
        sink.lineBreak();
        sink.section2();
        sink.sectionTitle2();
        sink.text(bundle.getString("migration.status.report.secondSectionTitle"));
        sink.sectionTitle2_();
        for (Entry<MavenProject, List<Change>> entries : changes.entrySet()) {
            if (isAggregate) {
                sink.section3();
                sink.sectionTitle3();
                sink.text(bundle.getString("migration.status.report.moduleTitle") + entries.getKey().getName());
                sink.sectionTitle3_();
            }
            generateStatisticsTable(sink, entries.getValue());
        }
        sink.section2_();
        sink.lineBreak();
        sink.section3();
        sink.sectionTitle2();
        sink.text(bundle.getString("migration.status.report.thirdSectionTitle"));
        sink.sectionTitle2_();
        for (Entry<MavenProject, List<Change>> entries : changes.entrySet()) {
            if (isAggregate) {
                sink.section3();
                sink.sectionTitle3();
                sink.text(bundle.getString("migration.status.report.moduleTitle") + entries.getKey().getName());
                sink.sectionTitle3_();
            }
            generateChangesTable(sink, entries.getValue());
        }
        sink.section3_();
        sink.body_();
        sink.flush();
        sink.close();
    }

    /**
     * Generates statistic table.
     *
     * @param sink
     * @param changes
     */
    private void generateStatisticsTable(Sink sink, List<Change> changes) {
        sink.table();
        sink.tableRow();
        sink.tableCell();
        sink.text(" Number of migration changes: ");
        sink.tableCell_();
        sink.tableCell();
        sink.text("" + changes.size());
        sink.tableCell_();
        sink.tableRow_();
        sink.tableRow();
        sink.tableCell();
        sink.text(" Number of pending migrations: ");
        sink.tableCell_();
        int nop = numberOfPending(changes);
        sink.tableCell();
        sink.text(nop + "  (" + calcPerc(changes.size(), nop) + ")  ");
        sink.nonBreakingSpace();
        sink.figure();
        sink.figureGraphics(nop == 0 ? "images/icon_success_sml.gif" : "images/icon_warning_sml.gif");
        sink.figure_();
        sink.tableCell_();
        sink.tableRow_();
        sink.table_();
    }

    /**
     * Calculates the percentage.
     *
     * @param size
     * @param nop
     * @return
     */
    private String calcPerc(int tot, int nop) {
        return "" + ((100 * nop) / tot) + "%";
    }

    /**
     * Return the number of pending change found.
     *
     * @param changes list of {@link Change}
     * @return Return the number of pending change found.
     */
    private int numberOfPending(List<Change> changes) {
        int numberOfPending = 0;
        for (Change change : changes) {
            if (change.getAppliedTimestamp() == null) {
                numberOfPending++;
            }
        }
        return numberOfPending;
    }

    /**
     * Generate a table for the given dependencies iterator.
     *
     * @param sink
     * @param iter
     */
    public void generateChangesTable(Sink sink, List<Change> iter) {
        sink.table();
        sink.tableRow();
        sink.tableCell();
        sink.bold();
        sink.text("ID");
        sink.bold_();
        sink.tableCell_();
        sink.tableCell();
        sink.bold();
        sink.text("Applied At");
        sink.bold_();
        sink.tableCell_();
        sink.tableCell();
        sink.bold();
        sink.text("Description");
        sink.bold_();
        sink.tableCell_();
        sink.tableCell();
        sink.bold();
        sink.text("Filename");
        sink.bold_();
        sink.tableCell_();
        sink.tableCell();
        sink.bold();
        sink.text("Status");
        sink.bold_();
        sink.tableCell_();
        sink.tableRow_();
        for (Change change : iter) {
            sink.tableRow();
            sink.tableCell();
            sink.text("" + change.getId());
            sink.tableCell_();
            sink.tableCell();
            sink.text(change.getAppliedTimestamp() == null ? " ... pending ... " : change.getAppliedTimestamp());
            sink.tableCell_();
            sink.tableCell();
            sink.text(change.getDescription());
            sink.tableCell_();
            sink.tableCell();
            sink.text(change.getFilename());
            sink.tableCell_();
            sink.tableCell();
            sink.figure();
            sink.figureGraphics(change.getAppliedTimestamp() != null ? "images/icon_success_sml.gif" : "images/icon_warning_sml.gif");
            sink.figure_();
            sink.tableCell_();
            sink.tableRow_();
        }
        sink.table_();
        sink.horizontalRule();
    }
}
