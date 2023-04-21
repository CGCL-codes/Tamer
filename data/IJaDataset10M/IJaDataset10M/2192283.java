package net.sourceforge.cobertura.reporting.html;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import net.sourceforge.cobertura.reporting.Clazz;
import net.sourceforge.cobertura.reporting.CoverageReport;
import net.sourceforge.cobertura.reporting.Package;
import net.sourceforge.cobertura.reporting.SourceFileLocator;
import net.sourceforge.cobertura.reporting.Util;
import net.sourceforge.cobertura.reporting.html.files.CopyFiles;

public class HTMLReport {

    private File outputDir;

    private SourceFileLocator sourceFileLocator;

    private CoverageReport coverage;

    private static final String INCOMPLETE_FLAG = " <font color=\"FF0000\"> (!)</font> ";

    private static final String COVERAGE_REPORT = "Coverage Report";

    private static final String NA = "N/A";

    private static final String CCN = "CCN";

    private static final String NCSS = "NCSS";

    private static final String JVDC = "JVDC";

    private long timeInMillis;

    /**
	 * Create a coverage report
	 */
    public HTMLReport(CoverageReport coverage, File outputDir, File sourceDir) throws Exception {
        this.outputDir = outputDir;
        sourceFileLocator = new SourceFileLocator(sourceDir);
        this.coverage = coverage;
        timeInMillis = System.currentTimeMillis();
        CopyFiles.copy(outputDir);
        generateSourceFiles();
        generateClassLists();
        generatePackageList();
        generateOverviews();
    }

    /**
	 * Prints out the header of an html file
	 * 
	 * @param out the PrintStream
	 * @param pageTitle the html title
	 * @param title the title to display
	 * @param isSortable indicates that the page contains sortable data
	 * @param overview true if the page is an overview
	 * @param metrics the metrics to display or null
	 */
    private void printFileHeader(PrintStream out, String pageTitle, String title, boolean isSortable, boolean overview, double[] metrics) {
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + pageTitle + "</title>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
        out.println("<link title=\"Style\" type=\"text/css\" rel=\"stylesheet\" href=\"css/main.css\" />");
        if (isSortable) {
            out.println("<link title=\"Style\" type=\"text/css\" rel=\"stylesheet\" href=\"css/sortabletable.css\" />");
            out.println("<script type=\"text/javascript\" src=\"js/popup.js\"></script>");
            out.println("<script type=\"text/javascript\" src=\"js/sortabletable.js\"></script>");
            out.println("<script type=\"text/javascript\" src=\"js/percentagesorttype.js\"></script>");
        }
        out.println("</head>");
        out.println("<body>");
        out.print("<table width=\"100%\"");
        if (overview) {
            out.print(" class=\"title\"");
        }
        out.println(">");
        out.println("<tr><td width=\"40%\">");
        out.println("<h3>" + title + "</h3>");
        out.println("</td>");
        if (overview) {
            out.println("<td width=\"30%\">");
            if (metrics != null) generateMetricsTable(out, metrics);
            out.println("</td>");
        }
        out.println("</tr>");
        out.println("</table>");
        if (overview) {
            out.println("<table width=\"100%\"><tr><td height=\"20\">");
            out.println("<div align=\"right\"><a href=\"http://cobertura4j2me.dreameffect.org\" target=\"_blank\">Need help?</a></div>");
            out.println("</td></tr></table>");
        }
    }

    /**
     * TODO
     */
    private void generateMetricsTable(PrintStream out, double[] metrics) {
        out.println("<table class=\"metrics\">");
        out.println("<tr><td align=\"left\">Metrics:</td>");
        out.println("<td>" + CCN + ": " + getDoubleValue(metrics[Util.METRIC_CCN]) + "</td>");
        out.println("<td>Methods: " + getDoubleValue(metrics[Util.METRIC_NUM_METHODS]) + "</td></tr>");
        out.println("<tr><td></td>");
        out.println("<td>" + NCSS + ": " + getDoubleValue(metrics[Util.METRIC_NCSS]) + "</td>");
        out.println("<td>Classes: " + getDoubleValue(metrics[Util.METRIC_NUM_CLASSES]) + "</td></tr>");
        out.println("<tr><td></td>");
        out.println("<td>" + JVDC + ": " + getPercentValue(metrics[Util.METRIC_JVDC]) + "</td>");
        out.println("<td>Packages: " + getDoubleValue(metrics[Util.METRIC_NUM_PACKAGES]) + "</td></tr>");
        out.println("</table>");
    }

    /**
     * Prints out the legend legend
     * 
     * @param out, the PrintStream
     * @param isSource true if legend is for a source file,
     *  false otherwise
     */
    private void generateLegend(PrintStream out, boolean isSource) {
        out.println("<div class=\"legend\">Legend:</div>");
        out.println("<table width=\"800\">");
        out.println("<tr>");
        if (isSource) {
            out.println("   <td class=\"numLineInstr\" width=\"8\"></td>");
            out.println("   <td width=\"150\">Instrumented line</td>");
        }
        out.println("   <td width=\"20\">" + CCN + ":</td>");
        out.println("   <td>Cyclomatic Complexity</td>");
        out.println("</tr>");
        out.println("<tr>");
        if (isSource) {
            out.println("   <td class=\"nbHitsCovered\"></td>");
            out.println("   <td>Covered line</td>");
        }
        out.println("   <td width=\"20\">" + NCSS + ":</td>");
        out.println("   <td>Non-Commenting Source Statement</td>");
        out.println("</tr>");
        out.println("<tr>");
        if (isSource) {
            out.println("   <td class=\"nbHitsUncovered\"></td>");
            out.println("   <td>Uncovered line</td>");
        }
        out.println("   <td>" + JVDC + ":</td>");
        out.println("   <td>JaVaDoc Comment</td>");
        out.println("</tr>");
        out.print("</table><br>");
    }

    /**
	 * Prints out the footer of an html file
	 * 
	 * @param out the PrintStream
	 * @param signature true if the signature should be printed out
	 */
    private void printFileFooter(PrintStream out, boolean signature) {
        if (signature) {
            out.println("<div class=\"footer\">");
            out.println("Reports generated by <a href=\"http://cobertura4j2me.dreameffect.org/\" target=\"_blank\">Cobertura for J2ME</a>.");
            out.println("<br>");
            out.println(DateFormat.getInstance().format(new Date(timeInMillis)));
            out.println("</div>");
        }
        out.println("</body>");
        out.println("</html>");
    }

    private void generatePackageList() throws IOException {
        File file = new File(outputDir, "frame-packages.html");
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream(file));
            printFileHeader(out, COVERAGE_REPORT, "Packages", false, false, null);
            out.println("<table width=\"100%\">");
            out.println("<tr>");
            out.println("<td nowrap=\"nowrap\"><a href=\"frame-summary.html\" onClick='parent.classList.location.href=\"frame-classes.html\"' target=\"summary\">All</a></td>");
            out.println("</tr>");
            Iterator iter = coverage.getPackages().iterator();
            while (iter.hasNext()) {
                Package pkg = (Package) iter.next();
                String url1 = "frame-summary-" + pkg.getName() + ".html";
                String url2 = "frame-classes-" + pkg.getName() + ".html";
                out.println("<tr>");
                out.println("<td nowrap=\"nowrap\"><a href=\"" + url1 + "\" onClick='parent.classList.location.href=\"" + url2 + "\"' target=\"summary\">" + pkg.getName() + "</a></td>");
                out.println("</tr>");
            }
            out.println("</table>");
            printFileFooter(out, false);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private void generateClassLists() throws IOException {
        generateClassList(null);
        Iterator iter = coverage.getPackages().iterator();
        while (iter.hasNext()) {
            Package pkg = (Package) iter.next();
            generateClassList(pkg);
        }
    }

    private void generateClassList(Package pkg) throws IOException {
        String filename;
        Set classes;
        if (pkg == null) {
            filename = "frame-classes.html";
            classes = coverage.getClasses();
        } else {
            filename = "frame-classes-" + pkg.getName() + ".html";
            classes = pkg.getClasses();
        }
        File file = new File(outputDir, filename);
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream(file));
            String title = pkg == null ? "All Packages" : pkg.getName();
            printFileHeader(out, COVERAGE_REPORT + " Classes", title, false, false, null);
            out.println("<h5>Classes</h5>");
            out.println("<table width=\"100%\">");
            for (Iterator iter = classes.iterator(); iter.hasNext(); ) {
                Clazz clazz = (Clazz) iter.next();
                out.println("<tr>");
                String percentCovered;
                if (clazz.getNumberOfLines() > 0) percentCovered = getPercentValue(clazz.getLineCoverageRate()); else percentCovered = NA;
                out.println("<td nowrap=\"nowrap\">" + (clazz.isDataComplete() ? "" : INCOMPLETE_FLAG) + "<a target=\"summary\" href=\"" + clazz.getLongName() + ".html\">" + clazz.getName() + "</a> <i>(" + percentCovered + ")</i></td>");
                out.println("</tr>");
            }
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");
            printFileFooter(out, false);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private void generateOverviews() throws IOException {
        generateOverview(null);
        Iterator iter = coverage.getPackages().iterator();
        while (iter.hasNext()) {
            Package pkg = (Package) iter.next();
            generateOverview(pkg);
        }
    }

    private void generateTopResultTable(PrintStream out, String id, boolean packageTitle) {
        out.println("<table class=\"report\" id=\"" + id + "\">\n");
        out.println("<thead>\n");
        out.println("<tr>\n");
        out.println("  <td class=\"heading\">" + (packageTitle ? "Package" : "") + "</td>\n");
        out.println("  <td class=\"heading\" width=\"10%\"># Classes</td>\n");
        generateCommonTableColumns(out);
        out.println("\n");
        out.println("</tr>\n");
        out.println("</thead>\n");
        out.println("<tbody>\n");
    }

    private void generateBottomResultTable(PrintStream out, String id, boolean sort) {
        out.println("</tbody>\n");
        out.println("</table>\n");
        if (sort) {
            out.println("<script type=\"text/javascript\">\n");
            out.println("var packageTable = new SortableTable(document.getElementById(\"" + id + "\"),\n");
            out.println("    [\"String\", \"Number\", \"Percentage\", \"Percentage\", \"Percentage\", \"Number\", \"Number\"]);\n");
            out.println("packageTable.sort(0);\n");
            out.println("</script>\n");
        }
    }

    private void generateOverview(Package pkg) throws IOException {
        String filename;
        if (pkg == null) {
            filename = "frame-summary.html";
        } else {
            filename = "frame-summary-" + pkg.getName() + ".html";
        }
        File file = new File(outputDir, filename);
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream(file));
            Set clazzes = pkg == null ? coverage.getClasses() : pkg.getClasses();
            double[] metrics = Util.getMetrics(sourceFileLocator, clazzes);
            metrics[Util.METRIC_NUM_PACKAGES] = pkg != null ? -1 : coverage.getPackages().size();
            metrics[Util.METRIC_NUM_CLASSES] = clazzes.size();
            printFileHeader(out, COVERAGE_REPORT, COVERAGE_REPORT, true, true, metrics);
            Set packages = null;
            if (pkg == null) {
                generateTopResultTable(out, "projectresults", false);
                generateTableRowForTotal(out);
                generateBottomResultTable(out, "projectresults", false);
                packages = coverage.getPackages();
                out.println("</p>");
                generateTopResultTable(out, "packageResults", true);
            } else {
                generateTopResultTable(out, "packageResults", true);
                generateTableRowForPackage(out, pkg);
            }
            if (packages != null && packages.size() > 0) {
                Iterator iter = packages.iterator();
                while (iter.hasNext()) {
                    Package subpkg = (Package) iter.next();
                    generateTableRowForPackage(out, subpkg);
                }
            }
            generateBottomResultTable(out, "packageResults", true);
            out.println("</p>");
            Set classes;
            if (pkg == null) {
                classes = new TreeSet();
                Set allClasses = coverage.getClasses();
                if (allClasses.size() > 0) {
                    Iterator iter = allClasses.iterator();
                    while (iter.hasNext()) {
                        Clazz clazz = (Clazz) iter.next();
                        if (clazz.getPackageName() == null) {
                            classes.add(clazz);
                        }
                    }
                }
            } else {
                classes = pkg.getClasses();
            }
            if (classes.size() > 0) {
                out.println("<p>");
                out.println("<table class=\"report\" id=\"classResults\">");
                generateTableHeaderForClasses(out, false);
                out.println("<tbody>");
                Iterator iter = classes.iterator();
                while (iter.hasNext()) {
                    Clazz clazz = (Clazz) iter.next();
                    generateTableRowForClass(out, clazz);
                }
                generateBottomResultTable(out, "classResults", true);
                out.println("</p>");
            }
            generateLegend(out, false);
            printFileFooter(out, true);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private void generateSourceFiles() throws IOException {
        Iterator iter = coverage.getClasses().iterator();
        while (iter.hasNext()) {
            Clazz clazz = (Clazz) iter.next();
            generateSourceFile(clazz);
        }
    }

    private void generateSourceFile(Clazz clazz) throws IOException {
        String filename = clazz.getLongName() + ".html";
        File file = new File(outputDir, filename);
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream(file));
            String title = COVERAGE_REPORT;
            if (!clazz.isDataComplete()) title += INCOMPLETE_FLAG;
            double[] metrics = Util.getMetrics(sourceFileLocator, clazz);
            metrics[Util.METRIC_NUM_CLASSES] = -1;
            metrics[Util.METRIC_NUM_PACKAGES] = -1;
            printFileHeader(out, COVERAGE_REPORT, title, false, true, metrics);
            generateTopResultTable(out, "packageResult", true);
            generateTableRowForPackage(out, coverage.getPackage(clazz.getPackageName()));
            generateBottomResultTable(out, "packageResult", false);
            out.println("<p>");
            out.println("<table class=\"report\">");
            generateTableHeaderForClasses(out, true);
            generateTableRowForClass(out, clazz);
            out.println("</table>");
            out.println("</p>");
            out.println("<p>");
            out.println("<table cellspacing=\"0\" cellpadding=\"0\" class=\"src\">");
            BufferedReader br = null;
            try {
                File sourcefile = sourceFileLocator.locateSourceFile(clazz.getLongFileName());
                if (sourcefile != null) {
                    br = new BufferedReader(new FileReader(sourcefile));
                    String lineStr;
                    JavaToHtml javaToHtml = new JavaToHtml();
                    int lineNumber = 1;
                    while ((lineStr = br.readLine()) != null) {
                        out.println("<tr>");
                        if (clazz.isValidSourceLine(lineNumber)) {
                            int numberOfHits = clazz.getNumberOfHits(lineNumber);
                            out.println("  <td class=\"numLineInstr\">&nbsp;" + lineNumber + "</td>");
                            if (numberOfHits > 0) {
                                out.println("  <td class=\"nbHitsCovered\">&nbsp;" + numberOfHits + "</td>");
                                out.println("  <td class=\"src\"><pre class=\"src\">&nbsp;" + javaToHtml.process(lineStr) + "</pre></td>");
                            } else {
                                out.println("  <td class=\"nbHitsUncovered\">&nbsp;" + numberOfHits + "</td>");
                                out.println("  <td class=\"src\"><pre class=\"src\"><span class=\"srcUncovered\">&nbsp;" + javaToHtml.process(lineStr) + "</span></pre></td>");
                            }
                        } else {
                            out.println("  <td class=\"numLine\">&nbsp;" + lineNumber + "</td>");
                            out.println("  <td class=\"nbHits\">&nbsp;</td>");
                            out.println("  <td class=\"src\"><pre class=\"src\">&nbsp;" + javaToHtml.process(lineStr) + "</pre></td>");
                        }
                        out.println("</tr>");
                        lineNumber++;
                    }
                } else {
                    out.println("Source file not available");
                }
            } finally {
                if (br != null) {
                    br.close();
                }
            }
            out.println("</table>");
            out.println("</p>");
            generateLegend(out, true);
            printFileFooter(out, true);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private static void generateCommonTableColumns(PrintStream out) {
        out.println("  <td class=\"heading\" width=\"18%\">");
        out.println("Line Coverage");
        out.println("</td>");
        out.println("  <td class=\"heading\" width=\"18%\">");
        out.println("Branch Coverage");
        out.println("</td>");
        out.println("  <td class=\"heading\" width=\"18%\">");
        out.println("Method Coverage");
        out.println("</td>");
    }

    private static void generateTableHeaderForClasses(PrintStream out, boolean displaySource) {
        out.println("<thead>");
        out.println("<tr>");
        out.println("  <td class=\"heading\">Class" + (displaySource ? "" : "es") + "</td>");
        generateCommonTableColumns(out);
        out.println("</tr>");
        out.println("</thead>");
    }

    private static void generateNAPercent(PrintStream out) {
        out.println("<table cellpadding=\"0\" cellspacing=\"0\" align=\"right\">");
        out.println("<tr>");
        out.println("<td>");
        out.println(NA);
        out.println("&nbsp;</td>");
        out.println("<td>");
        out.println("<table class=\"percentGraph\" cellpadding=\"0\" cellspacing=\"0\">");
        out.println("<tr><td class=\"NA\" width=\"100\"></td></tr>");
        out.println("</table>");
        out.println("</td>");
        out.println("</tr>");
        out.println("</table>");
    }

    private static void generateTableColumnsForNA(PrintStream out) {
        for (int i = 0; i < 3; i++) {
            out.print("<td class=\"value\">");
            generateNAPercent(out);
            out.println("</td>");
        }
    }

    private static void generateTableColumnsFromData(PrintStream out, double lineCoverage, double branchCoverage, double methodCoverage) {
        out.print("<td class=\"value\">");
        generatePercentResult(out, lineCoverage);
        out.println("</td>");
        out.print("<td class=\"value\">");
        generatePercentResult(out, branchCoverage);
        out.println("</td>");
        out.print("<td class=\"value\">");
        generatePercentResult(out, methodCoverage);
        out.println("</td>");
    }

    private void generateTableRowForTotal(PrintStream out) {
        double lineCoverage = coverage.getLineCoverageRate();
        double branchCoverage = coverage.getBranchCoverageRate();
        double methodCoverage = coverage.getMethodCoverageRate();
        out.println("  <tr>");
        out.println("<td class=\"text\"><b>All Packages</b></td>");
        out.print("<td class=\"value\">");
        out.print(coverage.getNumberOfClasses());
        out.println("</td>");
        if (coverage.getNumberOfLines() == 0) {
            generateTableColumnsForNA(out);
        } else {
            generateTableColumnsFromData(out, lineCoverage, branchCoverage, methodCoverage);
        }
        out.println("</tr>");
    }

    private void generateTableRowForPackage(PrintStream out, Package pkg) {
        String url1 = "frame-summary-" + pkg.getName() + ".html";
        String url2 = "frame-classes-" + pkg.getName() + ".html";
        double lineCoverage = pkg.getLineCoverageRate();
        double branchCoverage = pkg.getBranchCoverageRate();
        double methodCoverage = pkg.getMethodCoverageRate();
        out.println("  <tr>");
        out.print("<td class=\"text\"><a href=\"");
        out.print(url1);
        out.print("\" onClick='parent.classList.location.href=\"");
        out.print(url2);
        out.print("\"'>");
        out.print(pkg.getName());
        out.println("</a></td>");
        out.println("<td class=\"value\">");
        out.println(pkg.getClasses().size());
        out.println("</td>");
        if (pkg.getNumberOfLines() == 0) {
            generateTableColumnsForNA(out);
        } else {
            generateTableColumnsFromData(out, lineCoverage, branchCoverage, methodCoverage);
        }
        out.println("</tr>");
    }

    private void generateTableRowForClass(PrintStream out, Clazz clazz) {
        double lineCoverage = clazz.getLineCoverageRate();
        double branchCoverage = clazz.getBranchCoverageRate();
        double methodCoverage = clazz.getMethodCoverageRate();
        out.println("  <tr>");
        out.print("<td class=\"text\"><a href=\"");
        out.print(clazz.getLongName());
        out.print(".html\">");
        out.print(clazz.getName());
        out.println("</a>");
        if (!clazz.isDataComplete()) {
            out.print(INCOMPLETE_FLAG);
        }
        out.println("</td>");
        if (clazz.getNumberOfLines() == 0) {
            generateTableColumnsForNA(out);
        } else {
            generateTableColumnsFromData(out, lineCoverage, branchCoverage, methodCoverage);
        }
        out.println("</tr>");
    }

    private static void generatePercentResult(PrintStream out, double percentValue) {
        double rest = 1d - percentValue;
        out.println("<table cellpadding=\"0\" cellspacing=\"0\" align=\"right\">");
        out.println("<tr>");
        out.println("<td>" + getPercentValue(percentValue) + "&nbsp;</td>");
        out.println("<td>");
        out.println("<table class=\"percentGraph\" cellpadding=\"0\" cellspacing=\"0\">");
        out.println("<tr>");
        out.println("<td class=\"covered\" width=\"" + (int) (percentValue * 100) + "\"></td>");
        out.println("<td class=\"uncovered\" width=\"" + (int) (rest * 100) + "\"></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("</td>");
        out.println("</tr>");
        out.println("</table>");
    }

    private static String getDoubleValue(double value) {
        if (value == -1) return "-";
        NumberFormat formatter;
        formatter = new DecimalFormat();
        return formatter.format(value);
    }

    private static String getPercentValue(double value) {
        if (value == -1) return "-";
        double percentil = Math.floor(value * 1000 + 0.5d) / 10;
        if (percentil == Math.ceil(percentil)) {
            return (int) (percentil) + "%";
        }
        return percentil + "%";
    }
}
