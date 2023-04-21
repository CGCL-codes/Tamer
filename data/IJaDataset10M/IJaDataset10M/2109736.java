package preprocessing.statistic.statisticalBlock;

import game.reports.JRXMLDefaultHolder;
import game.reports.ReportBuilder;
import jsc.distributions.Normal;
import jsc.swt.plot.LinearAxisModel;
import jsc.util.Scale;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.view.JasperViewer;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Kuchar Jaroslav - kuchaj1@fel.cvut.cz
 * Date: 29.3.2009
 * Time: 16:26:30
 */
public class StatisticalDataProcessor {

    private PairedDataSample data;

    List<TestResult> testResults;

    ReportBuilder repbuilder;

    JasperPrint printedReport;

    /**
     * constructor for processor
     */
    public StatisticalDataProcessor() {
        this.data = null;
        testResults = new ArrayList<TestResult>();
    }

    /**
     * constructor for processor
     *
     * @param data input paired data
     */
    public StatisticalDataProcessor(PairedDataSample data) {
        this.data = data;
        testResults = new ArrayList<TestResult>();
    }

    /**
     * returns array of test names in package "tests"
     *
     * @return array of strings
     */
    public String[] getTestsNames() {
        String[] tests = null;
        try {
            Class[] classes = StatisticalDataProcessor.getClasses(getClass().getPackage().getName() + ".tests");
            tests = new String[classes.length];
            int i = 0;
            for (Class c : classes) {
                AbstractTwoSampleTest ats = (AbstractTwoSampleTest) c.newInstance();
                tests[i] = ats.getName();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Arrays.sort(tests);
        return tests;
    }

    /**
     * process test specified by name
     *
     * @param name                  test name
     * @param alternativeHypothesis alternative hypothesis
     * @param significanceLevel     significance level
     */
    public void processTest(String name, int alternativeHypothesis, double significanceLevel) {
        try {
            Class[] classes = StatisticalDataProcessor.getClasses(getClass().getPackage().getName() + ".tests");
            for (Class c : classes) {
                AbstractTwoSampleTest test = (AbstractTwoSampleTest) c.newInstance();
                if (test.getName().equals(name)) {
                    test.processTest(this.data, alternativeHypothesis);
                    TestResult tr = new TestResult();
                    tr.setName(test.getName());
                    tr.setPpValue(test.getPValue());
                    tr.setTestStatistic(test.getTestStatistic());
                    tr.setHypothese(alternativeHypothesis);
                    tr.setSignificance(significanceLevel);
                    tr.setSystematicError(this.data.getSystematicError());
                    tr.setRandomError(this.data.getRandomError());
                    tr.setDescription(test.getDescription());
                    this.testResults.add(tr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * sets new data to process
     *
     * @param data data
     */
    public void setData(PairedDataSample data) {
        this.data = data;
    }

    /**
     * return object of results
     *
     * @return array of results
     */
    public TestResult[] getObjectResult() {
        return (TestResult[]) this.testResults.toArray();
    }

    /**
     * return text representation of all test results
     *
     * @return text results
     */
    public String getTextResult() {
        Boolean normality = true;
        StringBuffer out = new StringBuffer();
        out.append("<hr />");
        out.append("<font size=\"6\"><b>Introduction info(" + this.data.getNameOfSets() + "):</b></font><br />");
        out.append("<b>Size of data :</b> " + this.data.getFirstDataSample().length + "\n");
        out.append("<b>Systematic Error :</b> " + this.data.getSystematicError() + "\n");
        out.append("<b>Random Error : </b>" + this.data.getRandomError() + "\n");
        if (this.data.isFirstNormalDistrbution()) {
            out.append("First data sample is normal distribution \n");
        } else {
            out.append("First data sample is <font color=\"red\">NOT</font> normal distribution \n");
            normality = false;
        }
        if (this.data.isSecondNormalDistrbution()) {
            out.append("Second data sample is normal distribution \n");
        } else {
            out.append("Second data sample is <font color=\"red\">NOT</font> normal distribution \n");
            normality = false;
        }
        if (this.data.isTheSameDistribution()) {
            out.append("Data samples have similar distribution \n");
        } else {
            out.append("Data samples have <font color=\"red\">NOT</font> similar distribution \n");
        }
        if (!normality) {
            out.append("<b>Recommended tests:</b> Wilcoxon Test, Sign Test");
            if (this.data.getFirstDataSample().length >= 1000) {
                out.append(", T test (data size>1000)");
            }
        } else {
            out.append("<b>Recommended tests:</b> Fisher Test, T Test");
        }
        try {
            String str = "";
            str += "a\tb\r\n";
            PrintWriter pw = new PrintWriter(new File("kuchaj1log.txt"));
            for (int i = 0; i < this.data.getFirstDataSample().length; i++) {
                str += this.data.getFirstDataSample()[i] + "\t" + this.data.getSecondDataSample()[i] + "\r\n";
            }
            pw.write(str);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (this.testResults.size() > 0) {
            out.append("<br /><br /><font size=\"6\"><b>Tests results (" + this.data.getNameOfSets() + "):</b></font><br />");
        }
        boolean info = true;
        for (TestResult trr : this.testResults) {
            out.append(trr.toString());
            if (trr.getRejectingH0()) {
                info = false;
            }
        }
        if (this.testResults.size() > 0) {
            out.append("<br /><font size=\"6\" color=\"red\">Summary(" + this.data.getNameOfSets() + "):</font><br />");
            if (this.data.isTheSameDistribution() && info) {
                out.append("Success - there is no suspicion of the significant difference.");
            } else {
                out.append("Fail - there is suspicion of the significant difference.");
            }
        }
        out.append("<br /><hr />");
        String str = out.toString();
        str = str.replaceAll("\n", "<br />");
        str = str.replaceAll("\t", "&nbsp;&nbsp;&nbsp;");
        return str;
    }

    /**
     * returns jpanel and image represenation of all results
     *
     * @return jpanel result
     */
    public JPanel getPanelResult() {
        JPanel res = new JPanel();
        int startX = 0;
        int endX = 0;
        if (this.testResults.size() > 0) {
            JPanel out = new JPanel(new GridLayout(this.testResults.size(), 0));
            for (TestResult trr : this.testResults) {
                Scale sx = new Scale(-4.0, 4.0, 2, false, false);
                LinearAxisModel x = new LinearAxisModel("", sx, "");
                Scale sy = new Scale(0.0, 0.5, 2, false, false);
                LinearAxisModel y = new LinearAxisModel("", sy, "");
                PlotGraphPanel pp = new PlotGraphPanel(x, y, trr.getName());
                pp.addPdf(new Normal(), 100, Color.red);
                switch(trr.getHypothese()) {
                    case 0:
                        pp.addPdf(new Normal(), -4.0, -3.0 + ((6.0) * (trr.getPpValue() / 2.0)), 100, Color.red, true);
                        pp.addPdf(new Normal(), 3.0 - ((6.0) * (trr.getPpValue() / 2.0)), 4.0, 100, Color.red, true);
                        pp.addVerticalLine(-3.0 + ((6.0) * (trr.getSignificance() / 2.0)), Color.green, new BasicStroke());
                        pp.addVerticalLine(3.0 - ((6.0) * (trr.getSignificance() / 2.0)), Color.green, new BasicStroke());
                        pp.addCustomColor(Color.green);
                        pp.addCustomString("significance", (int) (85.0 + ((300.0) * (trr.getSignificance() / 2.0))), 190);
                        pp.addCustomString("significance", 400 - (int) (115.0 + ((300.0) * (trr.getSignificance() / 2.0))), 190);
                        startX = (int) (90.0 + ((300.0) * (trr.getSignificance() / 2.0)));
                        endX = 433 - (int) (90.0 + ((300.0) * (trr.getSignificance() / 2.0)));
                        pp.addCustomLine(startX, 42, endX, 42);
                        pp.addCustomLine(startX, 42, startX + 5, 37);
                        pp.addCustomLine(startX, 42, startX + 5, 47);
                        pp.addCustomLine(endX, 42, endX - 5, 37);
                        pp.addCustomLine(endX, 42, endX - 5, 47);
                        break;
                    case 1:
                        pp.addPdf(new Normal(), 3.0 - ((6.0) * (trr.getPpValue())), 4.0, 100, Color.red, true);
                        pp.addVerticalLine(3.0 - ((6.0) * (trr.getSignificance() / 2.0)), Color.green, new BasicStroke());
                        pp.addCustomColor(Color.green);
                        pp.addCustomString("significance", 400 - (int) (115.0 + ((300.0) * (trr.getSignificance() / 2.0))), 190);
                        startX = 50;
                        endX = 433 - (int) (90.0 + ((300.0) * (trr.getSignificance() / 2.0)));
                        pp.addCustomLine(startX, 42, endX, 42);
                        pp.addCustomLine(startX, 42, startX + 5, 37);
                        pp.addCustomLine(startX, 42, startX + 5, 47);
                        pp.addCustomLine(endX, 42, endX - 5, 37);
                        pp.addCustomLine(endX, 42, endX - 5, 47);
                        break;
                    case 2:
                        pp.addPdf(new Normal(), -4.0, -3.0 + ((6.0) * (trr.getPpValue())), 100, Color.red, true);
                        pp.addVerticalLine(-3.0 + ((6.0) * (trr.getSignificance() / 2.0)), Color.green, new BasicStroke());
                        pp.addCustomColor(Color.green);
                        pp.addCustomString("significance", (int) (85.0 + ((300.0) * (trr.getSignificance() / 2.0))), 190);
                        startX = (int) (90.0 + ((300.0) * (trr.getSignificance() / 2.0)));
                        endX = 380;
                        pp.addCustomLine(startX, 42, endX, 42);
                        pp.addCustomLine(startX, 42, startX + 5, 37);
                        pp.addCustomLine(startX, 42, startX + 5, 47);
                        pp.addCustomLine(endX, 42, endX - 5, 37);
                        pp.addCustomLine(endX, 42, endX - 5, 47);
                        break;
                }
                pp.addCustomString("H0 Accepting area", 180, 38);
                pp.addCustomColor(Color.red);
                pp.addCustomString("p-value", 245, 60);
                pp.setPreferredSize(new Dimension(400, 200));
                pp.setSize(new Dimension(400, 200));
                out.add(pp);
            }
            JScrollPane scrollPane = new JScrollPane(out);
            int pref = 500;
            pref = Math.min(this.testResults.size() * 200, pref);
            scrollPane.setPreferredSize(new Dimension(420, pref));
            res.add(scrollPane);
        }
        return res;
    }

    /**
     * clearing list of results
     */
    public void clearResults() {
        this.testResults.clear();
    }

    /**
     * creates jasperreports window with subreports of tests results
     */
    public void getJasperReportResult() {
        if (this.testResults.size() > 0) {
            repbuilder = new ReportBuilder();
            Object[] data = testResults.toArray();
            try {
                boolean normality = true;
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("dataSize", this.data.getFirstDataSample().length);
                params.put("systematicError", this.data.getSystematicError());
                params.put("randomError", this.data.getRandomError());
                if (this.data.isFirstNormalDistrbution()) {
                    params.put("firstNormal", "First data sample is normal distribution");
                } else {
                    params.put("firstNormal", "First data sample is not normal distribution");
                    normality = false;
                }
                if (this.data.isSecondNormalDistrbution()) {
                    params.put("secondNormal", "Second data sample is not normal distribution");
                } else {
                    params.put("secondNormal", "Second data sample is not normal distribution");
                    normality = false;
                }
                if (this.data.isTheSameDistribution()) {
                    params.put("sameDistribution", "Data samples have similar distribution");
                } else {
                    params.put("sameDistribution", "Data samples have not similar distribution");
                }
                if (!normality) {
                    if (this.data.getFirstDataSample().length >= 1000) {
                        params.put("recommendedTests", "Wilcoxon Test, Sign Test, T test (data size>1000)");
                    } else {
                        params.put("recommendedTests", "Wilcoxon Test, Sign Test");
                    }
                } else {
                    params.put("recommendedTests", "Fisher Test, T Test");
                }
                repbuilder.addSubreport(new JRXMLDefaultHolder("report-templates-source" + File.separator + "StatisticIntroductionReport.jrxml", "", params, new JREmptyDataSource()));
                repbuilder.addSubreport(new JRXMLDefaultHolder("report-templates-source" + File.separator + "StatisticReport.jrxml", "", new HashMap(), new JRBeanArrayDataSource(data)));
                Map<String, Object> params2 = new HashMap<String, Object>();
                boolean info = true;
                for (TestResult trr : this.testResults) {
                    if (trr.getRejectingH0()) {
                        info = false;
                    }
                }
                if (this.data.isTheSameDistribution() && info) {
                    params2.put("summary", "Success - there is no suspicion of the significant difference");
                } else {
                    params2.put("summary", "Fail - there is suspicion of the significant difference");
                }
                repbuilder.addSubreport(new JRXMLDefaultHolder("report-templates-source" + File.separator + "StatisticSummaryReport.jrxml", "", params2, new JREmptyDataSource()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.buildReport();
            this.preview();
        }
    }

    /**
     * building jasperreports report
     */
    public void buildReport() {
        try {
            printedReport = repbuilder.buildReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * shows jasperreports window
     */
    public void preview() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        if (printedReport != null) {
            JasperViewer.viewReport(printedReport, false);
        } else System.out.println("Build report first!");
    }

    /**
     * http://snippets.dzone.com/posts/show/4831
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * http://snippets.dzone.com/posts/show/4831
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
