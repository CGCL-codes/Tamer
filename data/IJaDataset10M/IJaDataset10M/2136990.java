package org.jcvi.tasker.table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;
import org.joda.time.DateTime;
import org.jcvi.common.core.Range.CoordinateSystem;
import org.jcvi.glk.task.Task;
import org.jcvi.glk.task.TaskFeatureType;
import org.jcvi.glk.task.TaskResult;
import org.jcvi.glk.task.TaskSourceContig;
import org.jcvi.tasker.AutoTasker;
import org.jcvi.tasker.BasicFeatureTypeLookup;
import org.jcvi.tasker.CommonFeatureType;
import org.jcvi.tasker.SampleInfo;
import org.jcvi.tasker.TargetValidationState;
import org.jcvi.tasker.TaskFeatureTypeLookup;
import org.jcvi.tasker.Tasker;
import org.jcvi.tasker.db.DatabaseSampleInfo;
import org.jcvi.tasker.swing.LocalIcon;

/**
 *
 * @author jsitz@jcvi.org
 */
public class SampleTablePanel extends JPanel implements Printable {

    /** The Serial Version UID. */
    private static final long serialVersionUID = -7745674128704711042L;

    /** The standard {@link Border} to use for table cells */
    static final Border TABLE_BORDER = BorderFactory.createLineBorder(Color.BLACK);

    /** The standard {@link Border} to use for table cells */
    static final Border REGION_CELL_BORDER = BorderFactory.createCompoundBorder(SampleTablePanel.TABLE_BORDER, BorderFactory.createEmptyBorder(5, 10, 5, 10));

    /** The icon identifier used for icons with no ambiguities. */
    static final String NO_AMBIGUITY_ICON = "good";

    /** The icon identifier used for non-zero low ambiguity counts. */
    static final String LOW_AMBIGUITY_ICON = "warning";

    /** The icon identifier used for high ambigutity counts. */
    static final String HIGH_AMBIGUITY_ICON = "error";

    /**
     * Loads an {@link Icon} using {@link LocalIcon} based on a <code>boolean</code> value.
     *
     * @param value The value to test.
     * @param trueIcon The <code>LocalIcon</code> path to use if the value is <code>true</code>
     * or <code>null</code> if no icon is to be used.
     * @param falseIcon The <code>LocalIcon</code> path to use if the value is
     * <code>false</code> or <code>null</code> if no icon is to be used.
     * @return The {@link Icon} loaded or <code>null</code> if no icon was selected.
     */
    protected static Icon getIconForBoolean(boolean value, String trueIcon, String falseIcon) {
        if (value && trueIcon != null) return LocalIcon.get(trueIcon); else if (!value && falseIcon != null) return LocalIcon.get(falseIcon); else return null;
    }

    /**
     * Loads an {@link Icon} using {@link LocalIcon} based on the given coverage levels
     *
     * @param lowCount The number of low coverage regions found.
     * @param goodIcon The <code>LocalIcon</code> path to use if no coverage problems were found
     * or <code>null</code> if no icon is to be used.
     * @param lowIcon The <code>LocalIcon</code> path to use if low (non-zero) coverage
     * regions exist or <code>null</code> if no icon is to be used.
     * @param zeroIcon The <code>LocalIcon</code> path to use if zero coverage regions exist or
     * <code>null</code> if no icon is to be used.
     * @return The {@link Icon} loaded or <code>null</code> if no icon was selected.
     */
    protected static Icon getIconForCoverage(int lowCount, String goodIcon, String lowIcon, String zeroIcon) {
        if (lowCount > 0) {
            return LocalIcon.get(lowIcon);
        }
        return LocalIcon.get(goodIcon);
    }

    /**
     * Retrieves an {@link Icon} from {@link LocalIcon} appropriate for the given
     * {@link TargetValidationState}.
     *
     * @param state The state to retrieve an icon for.
     * @return The icon to use or <code>null</code> if no icon is to be used.
     */
    protected static Icon getIconForValidationState(TargetValidationState state) {
        switch(state) {
            case VALID:
                return LocalIcon.get("good");
            case INVALID:
                return LocalIcon.get("error");
            case UNCERTAIN:
                return LocalIcon.get("warning");
            default:
                return null;
        }
    }

    /**
     * A <code>RowBuilder</code> is a utility class which populates a panel with a row of
     * table data given the results of a {@link Tasker} run.
     *
     * @author jsitz@jcvi.org
     */
    protected class RowBuilder implements Runnable {

        /** The target panel to add the row to. */
        private final JPanel panel;

        /** The <code>Tasker</code> analysis results. */
        private final TaskResult result;

        /** The feature type for concensus ambiguities. */
        private final TaskFeatureType ambiguityType;

        /** The feature type for low clone coverage. */
        private final TaskFeatureType cloneCoverageType;

        /** The feature type for low sequence coverage. */
        private final TaskFeatureType seqCoverageType;

        /**
         * Constructs a new <code>SampleTablePanel.RowBuilder</code>.
         *
         * @param panel The target panel to add the row to.
         * @param result The {@link TaskResult} generated by <code>Tasker</code> analysis.
         */
        public RowBuilder(JPanel panel, TaskResult result) {
            super();
            this.panel = panel;
            this.result = result;
            final TaskFeatureTypeLookup lookup = new BasicFeatureTypeLookup();
            this.ambiguityType = lookup.getTypeFor(CommonFeatureType.AMBIGUITY);
            this.cloneCoverageType = lookup.getTypeFor(CommonFeatureType.LOWCLONECOVERAGE);
            this.seqCoverageType = lookup.getTypeFor(CommonFeatureType.LOWSEQCOVERAGE);
        }

        @Override
        public void run() {
            final String region = this.result.getRegion();
            final CellFactory factory = SampleTablePanel.this.getCellFactory();
            final Set<TaskSourceContig> contigs = this.result.getSourceContigs();
            this.panel.add(factory.buildRegionCell(region), "newline, grow, spany " + (contigs.size() + 1));
            for (final TaskSourceContig contig : contigs) {
                final int ambiguities = this.result.getFeatures(this.ambiguityType, contig.getId()).size();
                final int seqCoverage = this.result.getFeatures(this.seqCoverageType, contig.getId()).size();
                final int cloneCoverage = this.result.getFeatures(this.cloneCoverageType, contig.getId()).size();
                final int maxAmbiguities = AutoTasker.getMaxAmbiguities();
                final TableCell ambiguitiesCell = factory.buildTextCell("(" + ambiguities + ")");
                if (ambiguities > maxAmbiguities) {
                    ambiguitiesCell.setIcon(LocalIcon.get(SampleTablePanel.HIGH_AMBIGUITY_ICON));
                } else if (ambiguities < 1) {
                    ambiguitiesCell.setIcon(LocalIcon.get(SampleTablePanel.NO_AMBIGUITY_ICON));
                    ambiguitiesCell.setText("");
                } else {
                    ambiguitiesCell.setIcon(LocalIcon.get(SampleTablePanel.LOW_AMBIGUITY_ICON));
                }
                this.panel.add(factory.buildTextCell(contig.getId()), "grow, growprio 100");
                this.panel.add(factory.buildLeadingValueCell(String.valueOf(contig.getReferenceCoverage().getBegin(CoordinateSystem.RESIDUE_BASED))), "grow, growprio 0");
                this.panel.add(factory.buildMiddleValueCell((" - ")), "grow, growprio 0");
                this.panel.add(factory.buildTrailingValueCell(String.valueOf(contig.getReferenceCoverage().getEnd(CoordinateSystem.RESIDUE_BASED))), "grow, growprio 0");
                this.panel.add(factory.buildNumericalCell(contig.getLength() + "bp"), "grow");
                this.panel.add(factory.buildIconCell(SampleTablePanel.getIconForBoolean(contig.coversCdsStart(), "good", null)), "grow");
                this.panel.add(factory.buildIconCell(SampleTablePanel.getIconForBoolean(contig.coversCdsStop(), "good", null)), "grow");
                this.panel.add(ambiguitiesCell, "grow");
                this.panel.add(factory.buildIconCell(SampleTablePanel.getIconForCoverage(seqCoverage, "good", "warning", "error")), "grow");
                this.panel.add(factory.buildIconCell(SampleTablePanel.getIconForCoverage(cloneCoverage, "good", "warning", "error")), "grow");
                this.panel.add(factory.buildIconCell(SampleTablePanel.getIconForValidationState(contig.getValidationState())), "grow, wrap");
            }
            final StringBuilder taskList = new StringBuilder();
            for (final Task task : this.result.getTasks()) {
                if (taskList.length() > 0) {
                    taskList.append(", ");
                }
                taskList.append(task.getTaskWell());
            }
            if (taskList.length() < 1) {
                taskList.append("No Tasks Suggested");
            }
            this.panel.add(factory.buildTextCell(taskList.toString(), SwingConstants.LEFT), "grow, spanx, wrap");
            factory.advanceRow();
        }
    }

    /** The inner panel which tightly wraps the table. */
    private final JPanel innerPanel;

    /** The factory object which builds table cells. */
    private final CellFactory cellFactory;

    private final SampleInfo sampleInfo;

    /**
     * Constructs a new <code>SampleTablePanel</code>.
     */
    public SampleTablePanel(SampleInfo sampleInfo) {
        super(new MigLayout("insets dialog"));
        this.sampleInfo = sampleInfo;
        this.innerPanel = new JPanel(new MigLayout("insets 0, fill, gap 0px 0px"));
        this.innerPanel.setOpaque(false);
        this.cellFactory = new CellFactory();
        this.init();
    }

    /**
     * Initializes the panel.
     */
    private void init() {
        this.setOpaque(false);
        this.innerPanel.setBorder(SampleTablePanel.TABLE_BORDER);
        final JLabel sampleLabel = new JLabel("Sample: ");
        final JLabel sampleName = new FieldCell(sampleInfo.getSampleName());
        final Font sampleFont = sampleLabel.getFont().deriveFont(Font.BOLD).deriveFont(18.0F);
        sampleLabel.setFont(sampleFont);
        sampleName.setFont(sampleFont);
        this.add(sampleLabel, "split 2, spany 2, shrink 20, alignx right");
        this.add(sampleName, "spany 2, pushx 100, growx");
        this.add(new JLabel("Tracking ID: "), "alignx right");
        this.add(new FieldCell(sampleInfo.getSampleName()), "growx, pushx 40, aligny bottom");
        this.add(new JLabel("Date: "), "alignx right");
        this.add(new FieldCell(new DateTime().toString("YYYY-MM-dd HH:mm:ss")), "growx, pushx 60, aligny bottom");
        this.add(new JLabel("Type: "), "newline 3, alignx right");
        this.add(new FieldCell(sampleInfo.getSampleType()), "growx, pushx 40, aligny bottom");
        this.add(new JLabel("Project: "), "alignx right");
        String project = null;
        if (sampleInfo instanceof DatabaseSampleInfo) {
            project = ((DatabaseSampleInfo) sampleInfo).getProjectName();
        }
        this.add(new FieldCell(project), "growx, pushx 60, aligny bottom");
        this.innerPanel.add(new HeaderCell("SEGMENT"), "spany 2, grow");
        this.innerPanel.add(new HeaderCell("CONTIG"), "spany 2, grow");
        this.innerPanel.add(new HeaderCell("COVERS"), "spany 2, spanx 3, grow, growprio 0");
        this.innerPanel.add(new HeaderCell("LENGTH"), "spany 2, grow");
        this.innerPanel.add(new HeaderCell("END COVERAGE"), "spanx 2, grow");
        this.innerPanel.add(new HeaderCell("AMBIGUITIES"), "spany 2, grow");
        this.innerPanel.add(new HeaderCell("LOW COVERAGE"), "spanx 2, grow");
        this.innerPanel.add(new HeaderCell("VALID"), "spany 2, grow, wrap");
        this.innerPanel.add(new HeaderCell("START"), "grow");
        this.innerPanel.add(new HeaderCell("STOP"), "grow");
        this.innerPanel.add(new HeaderCell("SEQ"), "grow");
        this.innerPanel.add(new HeaderCell("AMP"), "grow");
        this.add(this.innerPanel, "newline 10, push, spanx, growx, wrap, aligny top");
    }

    /**
     * Fetches the {@link CellFactory} used by this panel.
     *
     * @return A <code>CellFactory</code>.
     */
    public final CellFactory getCellFactory() {
        return this.cellFactory;
    }

    /**
     * Adds the results from a {@link Tasker} to the panel.  This creates a new row set under
     * a single region name for the <code>Tasker</code>.  Adding another <code>Tasker</code>
     * later with the same region will create a second row set instead of adding to the first.
     *
     * @param result The {@link TaskResult} generated by the <code>Tasker</code> analysis.
     */
    public void addTaskerResults(TaskResult result) {
        EventQueue.invokeLater(new RowBuilder(this.innerPanel, result));
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex != 0) return Printable.NO_SUCH_PAGE;
        final Graphics2D g2 = (Graphics2D) graphics;
        final Dimension gridSize = this.getSize();
        final double scaleFactor = Math.min(pageFormat.getImageableWidth() / gridSize.width, pageFormat.getImageableHeight() / gridSize.height);
        final double scaledHeight = this.getHeight() * scaleFactor;
        if (pageFormat.getOrientation() == PageFormat.PORTRAIT) {
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2.scale(scaleFactor, scaleFactor);
        } else {
            final double excessHeight = pageFormat.getImageableHeight() - scaledHeight;
            g2.translate(pageFormat.getImageableX() + excessHeight, pageFormat.getImageableY());
            g2.scale(scaleFactor, scaleFactor);
        }
        this.paint(graphics);
        return Printable.PAGE_EXISTS;
    }
}
