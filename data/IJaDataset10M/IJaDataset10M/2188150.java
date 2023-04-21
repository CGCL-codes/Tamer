package com.google.gwt.junit.benchmarks;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.HasMetaData;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.util.Util;
import com.google.gwt.junit.client.TestResults;
import com.google.gwt.junit.client.Trial;
import com.google.gwt.junit.rebind.BenchmarkGenerator;
import com.google.gwt.util.tools.Utility;
import junit.framework.TestCase;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.ISourceElementRequestor;
import org.eclipse.jdt.internal.compiler.SourceElementParser;
import org.eclipse.jdt.internal.compiler.SourceElementRequestorAdapter;
import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Generates a detailed report that contains the results of all of the
 * benchmark-related unit tests executed during a unit test session. The primary
 * user of this class is JUnitShell.
 * 
 * The report is in XML format. To view the XML reports, use benchmarkViewer.
 */
public class BenchmarkReport {

    /**
   * Converts a set of test results for a single benchmark method into XML.
   */
    private class BenchmarkXml {

        private MetaData metaData;

        private List results;

        private TestCase test;

        BenchmarkXml(TestCase test, List results) {
            this.test = test;
            this.results = results;
            Map methodMetaData = (Map) testMetaData.get(test.getClass().toString());
            metaData = (MetaData) methodMetaData.get(test.getName());
        }

        Element toElement(Document doc) {
            Element benchmark = doc.createElement("benchmark");
            benchmark.setAttribute("class", test.getClass().getName());
            benchmark.setAttribute("name", metaData.getTestName());
            benchmark.setAttribute("description", metaData.getTestDescription());
            String sourceCode = metaData.getSourceCode();
            if (sourceCode != null) {
                Element sourceCodeElement = doc.createElement("source_code");
                sourceCodeElement.appendChild(doc.createTextNode(sourceCode));
                benchmark.appendChild(sourceCodeElement);
            }
            for (Iterator it = results.iterator(); it.hasNext(); ) {
                TestResults result = (TestResults) it.next();
                benchmark.appendChild(toElement(doc, result));
            }
            return benchmark;
        }

        private Element toElement(Document doc, TestResults result) {
            Element resultElement = doc.createElement("result");
            resultElement.setAttribute("host", result.getHost());
            resultElement.setAttribute("agent", result.getAgent());
            List trials = result.getTrials();
            for (Iterator it = trials.iterator(); it.hasNext(); ) {
                Trial trial = (Trial) it.next();
                Element trialElement = toElement(doc, trial);
                resultElement.appendChild(trialElement);
            }
            return resultElement;
        }

        private Element toElement(Document doc, Trial trial) {
            Element trialElement = doc.createElement("trial");
            Map variables = trial.getVariables();
            for (Iterator it = variables.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                Object name = entry.getKey();
                Object value = entry.getValue();
                Element variableElement = doc.createElement("variable");
                variableElement.setAttribute("name", name.toString());
                variableElement.setAttribute("value", value.toString());
                trialElement.appendChild(variableElement);
            }
            trialElement.setAttribute("timing", String.valueOf(trial.getRunTimeMillis()));
            Throwable exception = trial.getException();
            if (exception != null) {
                Element exceptionElement = doc.createElement("exception");
                exceptionElement.appendChild(doc.createTextNode(exception.toString()));
                trialElement.appendChild(exceptionElement);
            }
            return trialElement;
        }
    }

    /**
   * Parses a .java source file to get the source code for methods.
   * 
   * This Parser takes some shortcuts based on the fact that it's only being
   * used to locate test methods for unit tests. (For example, only requiring a
   * method name instead of a full type signature for lookup).
   * 
   * TODO(tobyr) I think that I might be able to replace all this code with a
   * call to the existing metadata interface. Check declEnd/declStart in
   * JAbstractMethod.
   */
    private static class Parser {

        static class MethodBody {

            int declarationEnd;

            int declarationStart;

            String source;
        }

        private MethodBody currentMethod;

        private Map methods = new HashMap();

        private char[] sourceContents;

        Parser(JClassType klass) throws IOException {
            Map settings = new HashMap();
            settings.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_4);
            settings.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_4);
            settings.put(CompilerOptions.OPTION_DocCommentSupport, CompilerOptions.ENABLED);
            CompilerOptions options = new CompilerOptions(settings);
            IProblemFactory problemFactory = new DefaultProblemFactory(Locale.getDefault());
            ISourceElementRequestor requestor = new SourceElementRequestorAdapter() {

                public void enterMethod(MethodInfo methodInfo) {
                    String name = new String(methodInfo.name);
                    if (name.startsWith("test")) {
                        currentMethod = new MethodBody();
                        currentMethod.declarationStart = methodInfo.declarationStart;
                        methods.put(name, currentMethod);
                    }
                }

                public void exitMethod(int declarationEnd, int defaultValueStart, int defaultValueEnd) {
                    if (currentMethod != null) {
                        currentMethod.declarationEnd = declarationEnd;
                        currentMethod = null;
                    }
                }
            };
            boolean reportLocalDeclarations = true;
            boolean optimizeStringLiterals = true;
            SourceElementParser parser = new SourceElementParser(requestor, problemFactory, options, reportLocalDeclarations, optimizeStringLiterals);
            File sourceFile = findSourceFile(klass);
            sourceContents = read(sourceFile);
            CompilationUnit unit = new CompilationUnit(sourceContents, sourceFile.getName(), null);
            parser.parseCompilationUnit(unit, true);
        }

        /**
     * Returns the source code for the method of the given name.
     * 
     * @return null if the source code for the method can not be located
     */
        public String getMethod(JMethod method) {
            return new String(sourceContents, method.getDeclStart(), method.getDeclEnd() - method.getDeclStart() + 1);
        }
    }

    /**
   * Converts an entire report into XML.
   */
    private class ReportXml {

        private Map categoryElementMap = new HashMap();

        private Date date = new Date();

        private String version = "unknown";

        Element toElement(Document doc) {
            Element report = doc.createElement("gwt_benchmark_report");
            String dateString = DateFormat.getDateTimeInstance().format(date);
            report.setAttribute("date", dateString);
            report.setAttribute("gwt_version", version);
            for (Iterator entryIt = testResults.entrySet().iterator(); entryIt.hasNext(); ) {
                Map.Entry entry = (Map.Entry) entryIt.next();
                TestCase test = (TestCase) entry.getKey();
                List results = (List) entry.getValue();
                BenchmarkXml xml = new BenchmarkXml(test, results);
                Element categoryElement = getCategoryElement(doc, report, xml.metaData.getCategory().getClassName());
                categoryElement.appendChild(xml.toElement(doc));
            }
            return report;
        }

        /**
     * Locates or creates the category element by the specified name.
     * 
     * @param doc The document to search
     * @return The matching category element
     */
        private Element getCategoryElement(Document doc, Element report, String name) {
            Element e = (Element) categoryElementMap.get(name);
            if (e != null) {
                return e;
            }
            Element categoryElement = doc.createElement("category");
            categoryElementMap.put(name, categoryElement);
            CategoryImpl category = (CategoryImpl) testCategories.get(name);
            categoryElement.setAttribute("name", category.getName());
            categoryElement.setAttribute("description", category.getDescription());
            report.appendChild(categoryElement);
            return categoryElement;
        }
    }

    private static final String GWT_BENCHMARK_CATEGORY = "gwt.benchmark.category";

    private static final String GWT_BENCHMARK_DESCRIPTION = "gwt.benchmark.description";

    private static final String GWT_BENCHMARK_NAME = "gwt.benchmark.name";

    private static File findSourceFile(JClassType klass) {
        final char separator = File.separator.charAt(0);
        String filePath = klass.getPackage().getName().replace('.', separator) + separator + klass.getSimpleSourceName() + ".java";
        String[] paths = getClassPath();
        for (int i = 0; i < paths.length; ++i) {
            File maybeSourceFile = new File(paths[i] + separator + filePath);
            if (maybeSourceFile.exists()) {
                return maybeSourceFile;
            }
        }
        return null;
    }

    private static String[] getClassPath() {
        String path = System.getProperty("java.class.path");
        return path.split(File.pathSeparator);
    }

    private static String getSimpleMetaData(HasMetaData hasMetaData, String name) {
        String[][] allValues = hasMetaData.getMetaData(name);
        if (allValues == null) {
            return null;
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < allValues.length; ++i) {
            String[] values = allValues[i];
            for (int j = 0; j < values.length; ++j) {
                result.append(values[j]);
                result.append(" ");
            }
        }
        String resultString = result.toString().trim();
        return resultString.equals("") ? null : resultString;
    }

    private static char[] read(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        StringBuffer source = new StringBuffer((int) f.length());
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            source.append(line);
            source.append("\n");
        }
        char[] buf = new char[source.length()];
        source.getChars(0, buf.length, buf, 0);
        return buf;
    }

    private Map testCategories = new HashMap();

    private Map testMetaData = new HashMap();

    private Map testResults = new HashMap();

    private TypeOracle typeOracle;

    private TreeLogger logger;

    public BenchmarkReport(TreeLogger logger) {
        this.logger = logger;
    }

    /**
   * Adds the Benchmark to the report. All of the metadata about the benchmark
   * (category, name, description, etc...) is recorded from the TypeOracle.
   */
    public void addBenchmark(JClassType benchmarkClass, TypeOracle typeOracle) {
        this.typeOracle = typeOracle;
        String categoryType = getSimpleMetaData(benchmarkClass, GWT_BENCHMARK_CATEGORY);
        Map zeroArgMethods = BenchmarkGenerator.getNotOverloadedTestMethods(benchmarkClass);
        Map parameterizedMethods = BenchmarkGenerator.getParameterizedTestMethods(benchmarkClass, TreeLogger.NULL);
        List testMethods = new ArrayList(zeroArgMethods.size() + parameterizedMethods.size());
        testMethods.addAll(zeroArgMethods.values());
        testMethods.addAll(parameterizedMethods.values());
        Map metaDataMap = (Map) testMetaData.get(benchmarkClass.toString());
        if (metaDataMap == null) {
            metaDataMap = new HashMap();
            testMetaData.put(benchmarkClass.toString(), metaDataMap);
        }
        Parser parser = null;
        try {
            parser = new Parser(benchmarkClass);
        } catch (IOException e) {
            logger.log(TreeLogger.WARN, "Unable to parse the code for " + benchmarkClass, e);
        }
        for (int i = 0; i < testMethods.size(); ++i) {
            JMethod method = (JMethod) testMethods.get(i);
            String methodName = method.getName();
            String methodCategoryType = getSimpleMetaData(method, GWT_BENCHMARK_CATEGORY);
            if (methodCategoryType == null) {
                methodCategoryType = categoryType;
            }
            CategoryImpl methodCategory = getCategory(methodCategoryType);
            StringBuffer sourceCode = parser == null ? null : new StringBuffer(parser.getMethod(method));
            StringBuffer summary = new StringBuffer();
            StringBuffer comment = new StringBuffer();
            getComment(sourceCode, summary, comment);
            MetaData metaData = new MetaData(benchmarkClass.toString(), methodName, sourceCode != null ? sourceCode.toString() : null, methodCategory, methodName, summary.toString());
            metaDataMap.put(methodName, metaData);
        }
    }

    public void addBenchmarkResults(TestCase test, TestResults results) {
        List currentResults = (List) testResults.get(test);
        if (currentResults == null) {
            currentResults = new ArrayList();
            testResults.put(test, currentResults);
        }
        currentResults.add(results);
    }

    /**
   * Generates reports for all of the benchmarks which were added to the
   * generator.
   * 
   * @param outputPath The path to write the reports to.
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws IOException If anything goes wrong writing to outputPath
   */
    public void generate(String outputPath) throws ParserConfigurationException, IOException {
        if (testResults.size() == 0) {
            return;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        doc.appendChild(new ReportXml().toElement(doc));
        byte[] xmlBytes = Util.toXmlUtf8(doc);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputPath);
            fos.write(xmlBytes);
        } finally {
            Utility.close(fos);
        }
    }

    private CategoryImpl getCategory(String name) {
        CategoryImpl c = (CategoryImpl) testCategories.get(name);
        if (c != null) {
            return c;
        }
        String categoryName = "";
        String categoryDescription = "";
        if (name != null) {
            JClassType categoryType = typeOracle.findType(name);
            if (categoryType != null) {
                categoryName = getSimpleMetaData(categoryType, GWT_BENCHMARK_NAME);
                categoryDescription = getSimpleMetaData(categoryType, GWT_BENCHMARK_DESCRIPTION);
            }
        }
        c = new CategoryImpl(name, categoryName, categoryDescription);
        testCategories.put(name, c);
        return c;
    }

    /**
   * Parses out the JavaDoc comment from a string of source code. Returns the
   * first sentence summary in <code>summary</code> and the body of the entire
   * comment (including the summary) in <code>comment</code>.
   */
    private void getComment(StringBuffer sourceCode, StringBuffer summary, StringBuffer comment) {
        if (sourceCode == null) {
            return;
        }
        summary.setLength(0);
        comment.setLength(0);
        String regex = "/\\*\\*(.(?!}-\\*/))*\\*/";
        Pattern p = Pattern.compile(regex, Pattern.DOTALL);
        Matcher m = p.matcher(sourceCode);
        if (!m.find()) {
            return;
        }
        String commentStr = m.group();
        p = Pattern.compile("(/\\*\\*\\s*)" + "(((\\s*\\**\\s*)([^\n\r]*)[\n\r]+)*)");
        m = p.matcher(commentStr);
        if (!m.find()) {
            return;
        }
        String stripped = m.group(2);
        p = Pattern.compile("^\\p{Blank}*\\**\\p{Blank}*", Pattern.MULTILINE);
        String bareComment = p.matcher(stripped).replaceAll("");
        BreakIterator iterator = BreakIterator.getSentenceInstance();
        iterator.setText(bareComment);
        int firstSentenceEnd = iterator.next();
        if (firstSentenceEnd == BreakIterator.DONE) {
            summary.append(bareComment);
        } else {
            summary.append(bareComment.substring(0, firstSentenceEnd));
        }
        comment.append(bareComment);
        p = Pattern.compile("[^\\r\\n]+[\\r\\n]+(\\s+)\\*", Pattern.MULTILINE);
        m = p.matcher(sourceCode);
        int indentLen = 0;
        if (m.find()) {
            String indent = m.group(1);
            indentLen = indent.length() - 1;
        }
        StringBuffer leadingIndent = new StringBuffer();
        for (int i = 0; i < indentLen; ++i) {
            leadingIndent.append(' ');
        }
        sourceCode.insert(0, leadingIndent);
    }
}
