package au.org.cherel.datagen.app;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;
import net.sf.adatagenerator.api.CorrelationEvaluator;
import net.sf.adatagenerator.api.GeneratedPair;
import net.sf.adatagenerator.api.GeneratedPair.GeneratedDecision;
import net.sf.adatagenerator.api.Pair;
import net.sf.adatagenerator.api.Sink;
import net.sf.adatagenerator.api.Source;
import net.sf.adatagenerator.core.AbstractTee;
import net.sf.adatagenerator.core.DefaultPair;
import net.sf.adatagenerator.core.DefaultProcessingPair;
import net.sf.adatagenerator.core.SimpleCorrelationCounter;
import net.sf.adatagenerator.core.SimpleCorrelationEvaluator;
import net.sf.adatagenerator.pred.ExactMatchPredicate;
import net.sf.adatagenerator.pred.Predicate;
import net.sf.adatagenerator.pred.PredicateExporter.EXPORT_TYPE_JAVA;
import net.sf.adatagenerator.pred.ReflectionUtils;
import net.sf.adatagenerator.pred.TrimmedStringsLowerCase;
import au.org.cherel.datagen.api.GeneratedPatientRecord;
import au.org.cherel.datagen.api.PatientRecord;
import au.org.cherel.datagen.api.StackedPatientRecords;
import au.org.cherel.datagen.bean.PatientRecordBean;
import au.org.cherel.datagen.util.StackedPatientPairMrpsWriter;
import au.org.cherel.datagen.util.StackedPatientPairSinkAdaptor;

public class MutatingApp {

    /**
	 * A command line application that generates synthetic CHeReL pairs for
	 * various types of groups and then modifies the pairs, by introducing
	 * typos, field swaps, and nulls in various fields of one or both records
	 * of a pair. The application collects statistics on the field
	 * correlations within the modified pairs.
	 * <p>
	 * 
	 * Takes four optional command line parameters:
	 * <ul>
	 * <li/>Total number of pairs to be generated (default: 100)
	 * <li/>an integer indicating which source to use
	 * <li/>Absolute name of an output file (default: system out)
	 * <li/>Absolute name of an output file for correlation counts (default: system out)
	 * </ul>
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(CorrelatingApp.class.getName()).append(" main: ");
        for (String a : args) {
            sb.append(a).append(" ");
        }
        System.err.println(sb.toString());
        int finalCount = AppUtils.DEFAULT_COUNT;
        String fileName = null;
        String countsFile = null;
        Source<GeneratedPair<GeneratedPatientRecord>> flatSource = null;
        int idxSource = AppUtils.ALL_SOURCES.size() - 1;
        if (args != null) {
            switch(args.length) {
                case 4:
                    countsFile = args[3];
                case 3:
                    fileName = args[2];
                case 2:
                    idxSource = Integer.parseInt(args[1]);
                case 1:
                    finalCount = Integer.parseInt(args[0]);
                case 0:
                    break;
                default:
                    System.err.println("Ignoring '" + args[3] + "' and remaining args");
                    break;
            }
        }
        if (idxSource >= 0 && idxSource < AppUtils.ALL_SOURCES.size()) {
            flatSource = AppUtils.ALL_SOURCES.get(idxSource);
        }
        System.err.println("GENERATION PARAMETERS");
        System.err.println("Number of pairs:" + finalCount);
        System.err.println("Index of source:" + idxSource);
        System.err.println("Type of source: " + (flatSource == null ? "null" : flatSource.getClass().getName()));
        if (fileName == null) {
            System.err.println("Output: to standard out");
        } else {
            System.err.println("Output file: '" + fileName + "'");
        }
        if (countsFile == null) {
            System.err.println("Counts: to standard out");
        } else {
            System.err.println("Counts file: '" + countsFile + "'");
        }
        if (flatSource == null) {
            System.err.println("Aborting generation...");
            System.exit(1);
        }
        final Method[] methods = PatientRecordBean.class.getMethods();
        Set<Predicate<PatientRecord>> mutators = new LinkedHashSet<Predicate<PatientRecord>>();
        for (Method m : methods) {
            if (ReflectionUtils.isFieldAccessor(m)) {
                final String stem = ReflectionUtils.parseStemFromMethodName(m);
                String name = "aExactMatch" + stem;
                Predicate<PatientRecord> p = new ExactMatchPredicate<EXPORT_TYPE_JAVA, PatientRecord>(name, m, null);
                mutators.add(p);
                if (String.class.isAssignableFrom(m.getReturnType())) {
                    name = "aTrimmedStrings" + stem;
                    p = new TrimmedStringsLowerCase<PatientRecord>(name, m);
                    mutators.add(p);
                    name = "aTrimmedSubstringsLowerCase" + stem;
                    p = new TrimmedStringsLowerCase<PatientRecord>(name, m);
                    mutators.add(p);
                }
            }
        }
        CorrelationEvaluator<PatientRecord> pairModifier = new SimpleCorrelationEvaluator<PatientRecord>(mutators);
        SimpleCorrelationCounter<PatientRecord, GeneratedPatientRecord, GeneratedPair<GeneratedPatientRecord>> mutations = new SimpleCorrelationCounter<PatientRecord, GeneratedPatientRecord, GeneratedPair<GeneratedPatientRecord>>(pairModifier);
        Set<Predicate<PatientRecord>> predicates = new LinkedHashSet<Predicate<PatientRecord>>();
        for (Method m : methods) {
            if (ReflectionUtils.isFieldAccessor(m)) {
                final String stem = ReflectionUtils.parseStemFromMethodName(m);
                String name = "aExactMatch" + stem;
                Predicate<PatientRecord> p = new ExactMatchPredicate<EXPORT_TYPE_JAVA, PatientRecord>(name, m, null);
                predicates.add(p);
                if (String.class.isAssignableFrom(m.getReturnType())) {
                    name = "aTrimmedStrings" + stem;
                    p = new TrimmedStringsLowerCase<PatientRecord>(name, m);
                    predicates.add(p);
                    name = "aTrimmedSubstringsLowerCase" + stem;
                    p = new TrimmedStringsLowerCase<PatientRecord>(name, m);
                    predicates.add(p);
                }
            }
        }
        CorrelationEvaluator<PatientRecord> evaluator = new SimpleCorrelationEvaluator<PatientRecord>(predicates);
        SimpleCorrelationCounter<PatientRecord, GeneratedPatientRecord, GeneratedPair<GeneratedPatientRecord>> correlationCounter = new SimpleCorrelationCounter<PatientRecord, GeneratedPatientRecord, GeneratedPair<GeneratedPatientRecord>>(evaluator);
        File f = null;
        FileWriter fw = null;
        PrintWriter pw;
        if (fileName == null) {
            pw = new PrintWriter(System.out);
        } else {
            f = new File(fileName);
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);
        }
        Sink<GeneratedPair<StackedPatientRecords>> sink = new StackedPatientPairMrpsWriter(pw);
        Sink<GeneratedPair<GeneratedPatientRecord>> transformer = new StackedPatientPairSinkAdaptor(sink);
        @SuppressWarnings("unchecked") Sink<GeneratedPair<GeneratedPatientRecord>>[] sinks = (Sink<GeneratedPair<GeneratedPatientRecord>>[]) new Sink<?>[] { correlationCounter, transformer };
        Sink<GeneratedPair<GeneratedPatientRecord>> tee = new AbstractTee<GeneratedPair<GeneratedPatientRecord>>(sinks) {

            protected GeneratedPair<GeneratedPatientRecord> cloneElement(GeneratedPair<GeneratedPatientRecord> element) throws Exception {
                GeneratedPatientRecord gpr = element.getLeft();
                GeneratedPatientRecord clonedLeft = (GeneratedPatientRecord) gpr.clone();
                gpr = element.getRight();
                GeneratedPatientRecord clonedRight = (GeneratedPatientRecord) gpr.clone();
                Pair<GeneratedPatientRecord> pair = new DefaultPair<GeneratedPatientRecord>(clonedLeft, clonedRight);
                GeneratedDecision gd = element.getGeneratedDecision();
                GeneratedPair<GeneratedPatientRecord> retVal = new DefaultProcessingPair<GeneratedPatientRecord>(pair, gd);
                return retVal;
            }
        };
        flatSource.open();
        tee.open();
        int count = 0;
        while (flatSource.hasNext() && count < finalCount) {
            GeneratedPair<GeneratedPatientRecord> pair = flatSource.read();
            tee.write(pair);
            ++count;
        }
        if (count != finalCount) {
            System.err.println("WARNING: only processed " + count + " pairs.");
        }
        flatSource.close();
        pw.flush();
        if (fw != null) {
            fw.flush();
        }
        sink.close();
        if (countsFile == null) {
            pw = new PrintWriter(System.out);
        } else {
            f = new File(countsFile);
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);
        }
        AppUtils.printCounts(correlationCounter.getCounts(), pw);
    }
}
