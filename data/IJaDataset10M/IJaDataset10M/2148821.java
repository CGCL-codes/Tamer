package eu.annocultor.converter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.time.StopWatch;
import org.xml.sax.SAXException;
import eu.annocultor.api.ConverterKernel;
import eu.annocultor.api.CustomConverter;
import eu.annocultor.api.Factory;
import eu.annocultor.api.ObjectRule;
import eu.annocultor.api.Reporter;
import eu.annocultor.api.DataSource;
import eu.annocultor.api.Task;
import eu.annocultor.common.Utils;
import eu.annocultor.context.Concepts;
import eu.annocultor.context.Environment;
import eu.annocultor.context.EnvironmentAdapter;
import eu.annocultor.context.EnvironmentImpl;
import eu.annocultor.context.Namespace;
import eu.annocultor.context.Namespaces;
import eu.annocultor.data.destinations.RdfGraph;
import eu.annocultor.path.Path;
import eu.annocultor.rules.ObjectRuleImpl;
import eu.annocultor.triple.LiteralValue;
import eu.annocultor.triple.Property;
import eu.annocultor.triple.Triple;
import eu.annocultor.triple.XmlValue;
import eu.annocultor.utils.Helper;
import eu.annocultor.xconverter.api.DataObject;
import eu.annocultor.xconverter.api.DataObjectLinkedToRule;
import eu.annocultor.xconverter.api.Graph;

/**
 * XML analyzer: creates a report on XML structure, with separate statistics on
 * each XML element/attribute data values.
 * 
 * Requires the XML path to the record separating tag.
 * 
 * @author Borys Omelayenko
 * 
 */
public class Analyser extends CustomConverter {

    private static final String OPT_VALUES = "maxValues";

    private static final String OPT_MAX_VALUE_SIZE = "maxValueSize";

    private static final String OPT_FN = "fn";

    public static int MAX_VALUE_SIZE = 100;

    public static int MAX_VALUES = 50;

    public static void main(String... args) throws Exception {
        Options options = new Options();
        options.addOption(OptionBuilder.withArgName(OPT_FN).hasArg().isRequired().withDescription("XML file name to be analysed").withValueSeparator(',').create(OPT_FN));
        options.addOption(OptionBuilder.withArgName(OPT_MAX_VALUE_SIZE).hasArg().withDescription("Maximal size when values are counted separately. Longer values are counted altogether. Reasonable values are 100, 300, etc.").create(OPT_MAX_VALUE_SIZE));
        options.addOption(OptionBuilder.withArgName(OPT_VALUES).hasArg().withDescription("Maximal number of most frequent values displayed in the report. Reasonable values are 10, 25, 50").create(OPT_VALUES));
        CommandLineParser parser = new BasicParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, Utils.getCommandLineFromANNOCULTOR_ARGS(args));
        } catch (ParseException pe) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("analyse", options);
            return;
        }
        MAX_VALUE_SIZE = Integer.parseInt(cmd.getOptionValue(OPT_MAX_VALUE_SIZE, Integer.toString(MAX_VALUE_SIZE)));
        MAX_VALUES = Integer.parseInt(cmd.getOptionValue(OPT_VALUES, Integer.toString(MAX_VALUES)));
        Analyser analyser = new Analyser(new EnvironmentImpl());
        if (true) throw new Exception("unimplemented");
        System.exit(analyser.run());
    }

    /**
	 * A facade for a task.
	 * 
	 */
    private static class AnalyserTask implements Task {

        Task task;

        ObjectRule rule;

        public AnalyserTask(Task task) {
            super();
            this.task = task;
        }

        public List<ObjectRule> getObjectRules() {
            return task.getObjectRules();
        }

        public void setRule(ObjectRule rule) {
            this.rule = rule;
        }

        Path pathResponded = null;

        public List<ObjectRule> getRuleForSourcePath(Path path) {
            if (pathResponded == null) {
                pathResponded = path;
            }
            if (pathResponded.equals(path)) {
                return Collections.singletonList(rule);
            }
            return new ArrayList<ObjectRule>();
        }

        public Environment getEnvironment() {
            return task.getEnvironment();
        }

        public void addGraph(Graph graph) {
            task.addGraph(graph);
        }

        public void addPartListener(ObjectRule map) {
            task.addPartListener(map);
        }

        public String getDatasetDescription() {
            return task.getDatasetDescription();
        }

        public String getDatasetId() {
            return task.getDatasetId();
        }

        public String getDatasetURI() {
            return task.getDatasetURI();
        }

        public Set<Graph> getGraphs() {
            return task.getGraphs();
        }

        public Namespace getTargetNamespace() {
            return task.getTargetNamespace();
        }

        @Override
        public void setDataSource(DataSource dataSource) throws IOException {
            task.setDataSource(dataSource);
        }

        @Override
        public DataSource getDataSource() {
            return task.getDataSource();
        }

        public Reporter getReporter() {
            return task.getReporter();
        }
    }

    private static class ValueCount implements Comparable<ValueCount> {

        public String value;

        public int count;

        public ValueCount(String value) {
            super();
            this.value = value;
            this.count = 0;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(java.lang.Object obj) {
            return value.equals(obj);
        }

        public void inc() {
            count++;
        }

        public String percent(int total) {
            return "" + ((count * 100) / total);
        }

        public int compareTo(ValueCount a) {
            if (this.count < a.count) return 1;
            if (this.count > a.count) return -1;
            return 0;
        }
    }

    AnalyserTask task;

    /**
	 * Collected structure + statistics. <code>property, Map(value,count)</code>
	 */
    private SortedMap<String, Map<String, ValueCount>> statistics = new TreeMap<String, Map<String, ValueCount>>();

    private long passedBytes = 0;

    private StopWatch elapsed;

    private ObjectRule recordsMap = null;

    private void computeAndExportStatistics(SortedMap<String, Map<String, ValueCount>> statistics, File tmpDir) throws SAXException {
        Graph trg = new RdfGraph(null, task.getEnvironment(), "analyse", "", "");
        Namespaces namespaces = new Namespaces();
        try {
            trg.add(new Triple(Namespaces.ANNOCULTOR_REPORT + "Summary", new Property(Namespaces.ANNOCULTOR_REPORT + "topCount"), new LiteralValue(MAX_VALUES + ""), null));
        } catch (Exception e) {
            throw new SAXException(e);
        }
        for (String propertyName : statistics.keySet()) {
            StringBuffer message = new StringBuffer(propertyName + " has ");
            Map<String, ValueCount> values = statistics.get(propertyName);
            int totalRecords = 0;
            List<ValueCount> topTen = new LinkedList<ValueCount>();
            for (String value : values.keySet()) {
                ValueCount vc = values.get(value);
                topTen.add(vc);
                totalRecords += vc.count;
            }
            Collections.sort(topTen);
            String propertyUrl = Namespaces.ANNOCULTOR_REPORT + "__" + propertyName.replace('@', 'a').replaceAll(";", "/");
            int totalValues = values.size();
            message.append(totalValues + " values: ");
            int i = 0;
            boolean allUnique = false;
            try {
                for (Iterator<ValueCount> it = topTen.iterator(); it.hasNext() && i < MAX_VALUES; ) {
                    ValueCount count = it.next();
                    if (i == 0) {
                        allUnique = (count.count == 1);
                        message.append(allUnique ? " ALL UNIQUE \n" : "\n");
                        trg.add(new Triple(propertyUrl, Concepts.REPORTER.REPORT_NAME, new LiteralValue(propertyName), null));
                        trg.add(new Triple(propertyUrl, Concepts.REPORTER.REPORT_LABEL, new LiteralValue(Path.formatPath(new Path(propertyName.replace("*", "/")), namespaces)), null));
                        trg.add(new Triple(propertyUrl, Concepts.REPORTER.REPORT_TOTAL_VALUES, new LiteralValue("" + totalValues), null));
                        trg.add(new Triple(propertyUrl, Concepts.REPORTER.REPORT_ALL_UNIQUE, new LiteralValue("" + allUnique), null));
                    }
                    message.append(count.value + (allUnique ? "" : (" (" + count.count + ", " + count.percent(totalRecords) + "%)")) + " \n");
                    trg.add(new Triple(propertyUrl, Concepts.REPORTER.REPORT_VALUE, new LiteralValue(String.format("%07d", i) + "," + count.count + "," + count.percent(totalRecords) + "," + count.value), null));
                    i++;
                }
            } catch (Exception e) {
                throw new SAXException(e);
            }
        }
        try {
            trg.endRdf();
            System.out.println("Statistic saved to " + trg.getFinalFile(1).getCanonicalPath());
            Helper.xsl(trg.getFinalFile(1), new File(trg.getFinalFile(1).getCanonicalPath().replaceFirst("\\.rdf", ".html")), this.getClass().getResourceAsStream("/AnalyserReportRDF2HTML.xsl"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SAXException(e);
        }
    }

    public Analyser(Environment environment) throws Exception {
        super();
        task = new AnalyserTask(Factory.makeTask("xml", "", "Statistics report", Namespaces.NS, new AnalyzeEnvironment(environment)));
        recordsMap = ObjectRuleImpl.makeObjectRule(task, new Path(""), new Path(""), new Path(""), null, true);
        task.setRule(recordsMap);
    }

    private static class AnalyzeEnvironment extends EnvironmentAdapter {

        public AnalyzeEnvironment(Environment environment) {
            super(environment);
        }

        @Override
        public void completeWithDefaults() throws Exception {
            super.completeWithDefaults();
            setParameter(PARAMETERS.ANNOCULTOR_OUTPUT_DIR, getDocDir().getCanonicalPath());
        }
    }

    @Override
    public int run() throws Exception {
        ConverterHandler converterHandler = new StatisticsGatheringConverter(this);
        ConverterKernel converter = new Converter(task, converterHandler, null) {

            @Override
            public BufferedInputStream makeInputStream(File src) throws FileNotFoundException {
                elapsed = new StopWatch();
                try {
                    elapsed.start();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return new BufferedInputStream(new FileInputStream(src), 1024 * 1024) {

                    @Override
                    public synchronized int read() throws IOException {
                        int r = super.read();
                        passedBytes += r;
                        return r;
                    }

                    @Override
                    public synchronized int read(byte[] b, int off, int len) throws IOException {
                        int r = super.read(b, off, len);
                        passedBytes += r;
                        return r;
                    }
                };
            }
        };
        return converter.convert();
    }

    /**
	 * Overrides endDocument to compute statistics.
	 * 
	 */
    private class StatisticsGatheringConverter extends ConverterHandler {

        Analyser analyser;

        public StatisticsGatheringConverter(Analyser analyser) {
            super(analyser.task);
            this.analyser = analyser;
        }

        @Override
        protected String getTopCompletedPartSubject() {
            return "OBJECT";
        }

        @Override
        public void multiFileEndDocument() throws SAXException {
            super.multiFileEndDocument();
            log.info("Writing statistcis in RDF");
            computeAndExportStatistics(statistics, analyser.task.getEnvironment().getTmpDir());
        }

        @Override
        protected DataObjectLinkedToRule makeDataObjectForNewRecord(Path path, ObjectRule rule, DataObject parent) {
            return new StatisticsDataObject(path, rule, parent, statistics, analyser);
        }
    }

    private static class StatisticsDataObject extends DataObjectImpl {

        SortedMap<String, Map<String, ValueCount>> statistics = null;

        static long passedTags = 0;

        Analyser analyser;

        public StatisticsDataObject(Path path, ObjectRule rule, DataObject parent, SortedMap<String, Map<String, ValueCount>> statistics, Analyser analyser) {
            super(path, rule, parent);
            this.statistics = statistics;
            this.analyser = analyser;
        }

        @Override
        public ListOfValues getValues(Path query) {
            ListOfValues r = new ListOfValues();
            r.add(new XmlValue("dummy"));
            return r;
        }

        @Override
        public void addValue(Path path2, XmlValue newValue) throws Exception {
            Set<Path> allProperties = new HashSet<Path>();
            allProperties.addAll(path2.explicate());
            for (Path path : allProperties) {
                String pathStr = path.getPath();
                Map<String, ValueCount> propertyStats = statistics.get(pathStr);
                if (propertyStats == null) {
                    propertyStats = new TreeMap<String, ValueCount>();
                    statistics.put(pathStr, propertyStats);
                }
                String value = path.isAttributeQuery() ? path.getValue() : newValue.getValue().trim();
                if (MAX_VALUE_SIZE > 0 && value.length() > MAX_VALUE_SIZE) {
                    value = "LONG VALUE (MAX " + MAX_VALUE_SIZE + ")";
                }
                ValueCount vc = propertyStats.get(value);
                if (vc == null) {
                    vc = new ValueCount(value);
                    propertyStats.put(value, vc);
                }
                vc.inc();
            }
            final int _1000 = 100000;
            if (passedTags % _1000 == 0) {
                int distinctCounts = 0;
                int distinctValues = 1;
                System.out.print("Prop stat:");
                for (Entry<String, Map<String, ValueCount>> e : statistics.entrySet()) {
                    System.out.print(e.getValue().size() + ",");
                    for (Entry<String, ValueCount> ev : e.getValue().entrySet()) {
                        distinctValues++;
                        distinctCounts += ev.getValue().count;
                    }
                }
                System.out.println();
                System.out.println("Passed: " + (analyser.elapsed.getTime() / 1000) + " s, " + (analyser.passedBytes / 1000000) + " Mb XML, " + passedTags + " values, " + statistics.size() + " paths, " + distinctValues + " distinct values, " + (distinctValues / (statistics.size() + 1)) + " av values per property, " + (distinctCounts / distinctValues) + " occurrences per value");
                System.out.print("Saving stats...");
                analyser.computeAndExportStatistics(statistics, new File("."));
                System.out.println("done.");
            }
            passedTags++;
        }
    }
}
