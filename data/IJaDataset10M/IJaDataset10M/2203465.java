package PolynomialExpressionGenerator;

/**
 *
 * @author Nicholson.Bill
 */
public class ReportConfig {

    public static enum ReportMode {

        ExpandedToSimplified, SimplifiedToExpanded
    }

    ;

    public static enum WhatToPrint {

        exercisesOnly, ExercisesWithSolutions, BothCopies
    }

    ;

    private int totalProblems;

    private WhatToPrint whatToPrint;

    private String fileNameAndPath;

    private ReportMode reportMode;

    public ReportConfig() {
        reportMode = ReportMode.ExpandedToSimplified;
    }

    public ReportMode setReportMode(ReportMode reportMode) {
        this.reportMode = reportMode;
        return reportMode;
    }

    public ReportMode getReportMode() {
        return reportMode;
    }

    /**
     * Set the file name and path of the report.
     * @param fileNameAndPath The file name (including drive ID and path where the
     *        HTML file should be written.
     * @return fileNameAndPath The file name (including drive ID and path where
     *         the HTML file should be written.
     */
    public String setFileNameAndPath(String fileNameAndPath) {
        this.fileNameAndPath = fileNameAndPath;
        return fileNameAndPath;
    }

    /**
     * Get the current file name and path.
     * @return fileNameAndPath The file name (including drive ID and path where
     *         the HTML file should be written.
     */
    public String getFileNameAndPath() {
        return fileNameAndPath;
    }

    /**
     * Configure the report to display solutions, or not.
     * @param printSolutions True to display solutions on the report.
     * @return True if solutions should be displayed
     */
    public WhatToPrint setWhatToPrint(WhatToPrint whatToPrint) {
        this.whatToPrint = whatToPrint;
        return whatToPrint;
    }

    /**
     * Get the configuration setting that controls the display of solutions.
     * @return True if solutions should be displayed
     */
    public WhatToPrint getWhatToPrint() {
        return whatToPrint;
    }

    /**
     * Set total number of problems that should be on the report.
     * @param totalProblems  Total number of problems that should be on the report.
     * @return Total number of problems that should be on the report.
     */
    public int setTotalProblems(int totalProblems) {
        this.totalProblems = totalProblems;
        return totalProblems;
    }

    /**
     * Get total number of problems that should be on the report.
     * @return Total number of problems that should be on the report.
     */
    public int getTotalProblems() {
        return totalProblems;
    }
}
