package net.sf.maltcms.apps;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import cross.tools.StringTools;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import net.sf.maltcms.evaluation.spi.classification.ClassificationPerformanceTest;
import net.sf.maltcms.evaluation.api.classification.EntityGroup;
import net.sf.maltcms.evaluation.spi.classification.PeakRTFeatureVector;
import net.sf.maltcms.evaluation.api.classification.PeakRTFeatureVectorComparator;
import net.sf.maltcms.evaluation.spi.EntityGroupBuilder;
import net.sf.maltcms.evaluation.spi.classification.PerformanceMetrics;

/**
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE
 * 
 * 
 */
public class MultipleAlignmentRowEvaluation {

    private final List<EntityGroup> gt;

    private final List<String> toolFiles;

    private final File outputDir;

    /**
     * Creates a new Eval object given a ground truth file and
     * an array of tool result files.
     * @param outputDir
     * @param groundTruth
     * @param toolResults
     */
    public MultipleAlignmentRowEvaluation(File outputDir, File groundTruth, File... toolResults) {
        this.outputDir = outputDir;
        this.outputDir.mkdirs();
        EntityGroupBuilder egb = new EntityGroupBuilder();
        gt = egb.buildCSVPeakAssociationGroups(groundTruth);
        toolFiles = new ArrayList<String>(toolResults.length);
        for (File file : toolResults) {
            toolFiles.add(file.getAbsolutePath());
        }
    }

    private List<EntityGroup> createEntityGroupsForFile(EntityGroupBuilder egb, File file) {
        List<EntityGroup> toolGroup = egb.buildCSVPeakAssociationGroups(file);
        String toolname = StringTools.removeFileExt(file.getName());
        System.out.println("Adding groups for toolName: " + toolname);
        return toolGroup;
    }

    private void appendToEvaluationResults(List<String> l, PrintStream dos) {
        StringBuilder sb = new StringBuilder();
        for (String s : l) {
            sb.append(s);
            sb.append("\t");
        }
        dos.print(sb.toString());
        dos.println();
    }

    /**
     * Run evaluation on all given instances of tool results against 
     * the configured ground truth, with a maximum deviation of delta.
     * 
     * @param delta
     */
    public void eval(final double delta) {
        System.out.println("Evaluating " + toolFiles.size() + " tool results with delta: " + delta);
        ClassificationPerformanceTest<PeakRTFeatureVector> cpt = new ClassificationPerformanceTest<PeakRTFeatureVector>(gt, new PeakRTFeatureVectorComparator(delta));
        List<String> categories = new ArrayList<String>(Arrays.asList("FullName", "Sensitivity", "Specificity", "Precision", "FPR", "Accuracy", "F1", "GTEntities", "ToolEntities", "GTExclusive", "ToolExclusive", "Common", "TP", "TN", "FP", "FN"));
        File basedir = outputDir;
        System.out.println("Creating evaluationResults.csv");
        EntityGroupBuilder egb = new EntityGroupBuilder();
        try {
            PrintStream dos = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File(basedir, "evaluationResults.csv"))));
            appendToEvaluationResults(categories, dos);
            try {
                for (int i = 0; i < toolFiles.size(); i++) {
                    String toolName = StringTools.removeFileExt(new File(toolFiles.get(i)).getName());
                    List<EntityGroup> egs = createEntityGroupsForFile(egb, new File(toolFiles.get(i)));
                    File fout = new File(basedir, toolName + ".txt");
                    if (fout.exists()) {
                        System.err.println("Warning! File exists: " + fout.getAbsolutePath());
                    }
                    writeToolResultFile(fout, toolName, cpt, egs, dos);
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e.getLocalizedMessage());
                System.exit(1);
            }
            System.out.println("Done creating tool results!");
            dos.flush();
            dos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void writeToolResultFile(File fout, String toolName, ClassificationPerformanceTest<PeakRTFeatureVector> cpt, List<EntityGroup> egs, PrintStream dos) {
        List<String> toolResult = new ArrayList<String>();
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(fout));
            bw.write("Results for tool: " + toolName + "\n");
            try {
                PerformanceMetrics pm = cpt.performTest(toolName, egs);
                toolResult.add(pm.getToolName());
                toolResult.add(pm.getSensitivity() + "");
                toolResult.add(pm.getSpecificity() + "");
                toolResult.add(pm.getPrecision() + "");
                toolResult.add(pm.getFPR() + "");
                toolResult.add(pm.getAccuracy() + "");
                toolResult.add(pm.getF1() + "");
                toolResult.add(pm.getGroundTruthEntities() + "");
                toolResult.add(pm.getToolEntities() + "");
                toolResult.add(pm.getUnmatchedGroundTruthEntities() + "");
                toolResult.add(pm.getUnmatchedToolEntities() + "");
                toolResult.add(pm.getCommonEntities() + "");
                toolResult.add(pm.getTp() + "");
                toolResult.add(pm.getTn() + "");
                toolResult.add(pm.getFp() + "");
                toolResult.add(pm.getFn() + "");
                appendToEvaluationResults(toolResult, dos);
                bw.write(pm.toString());
                bw.flush();
            } catch (IllegalArgumentException e) {
                System.err.println(e.getLocalizedMessage());
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage());
        } finally {
            if (bw != null) {
                try {
                    System.out.println("Closing result file!");
                    bw.close();
                } catch (IOException ioex) {
                    System.err.println(ioex.getLocalizedMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        Options options = new Options();
        Option outdir = OptionBuilder.withArgName("OUTDIR").hasOptionalArg().withDescription("Base directory where to store output.").create("o");
        Option gtfile = OptionBuilder.withArgName("FILE").hasArg().isRequired().withDescription("Absolute path to ground truth file.").create("g");
        Option toolfile = OptionBuilder.withArgName("PATH").hasArgs().isRequired().withDescription("Absolute path to base directory containing tool result files or absolute path to single tool result (.csv files).").create("t");
        Option delta = OptionBuilder.withArgName("DOUBLE").hasArg().isRequired(false).withDescription("Delta value for feature matching between ground truth and tool results.").create("d");
        options.addOption(outdir);
        options.addOption(gtfile);
        options.addOption(toolfile);
        options.addOption(delta);
        if (args.length == 0) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("java -jar maltcms.jar " + MultipleAlignmentRowEvaluation.class.getCanonicalName(), options, true);
            System.exit(1);
        }
        GnuParser gp = new GnuParser();
        try {
            CommandLine cl = gp.parse(options, args);
            File outputDir = new File(System.getProperty("user.dir"));
            if (cl.hasOption("o")) {
                outputDir = new File(cl.getOptionValue("o"));
            }
            File gtFile = new File(cl.getOptionValue("g"));
            System.out.println("GroundTruth: " + gtFile.getAbsolutePath());
            File basedir = new File(cl.getOptionValue("t"));
            File[] testFiles = null;
            if (basedir.isDirectory()) {
                if (basedir.exists()) {
                    System.out.println("Basedir exists!");
                } else {
                    System.err.println("Basedir: " + basedir.getAbsolutePath() + " does not exist!");
                    System.exit(1);
                }
                testFiles = basedir.listFiles(new java.io.FileFilter() {

                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getAbsolutePath().endsWith(".csv");
                    }
                });
            } else if (basedir.isFile() && basedir.getAbsolutePath().endsWith(".csv")) {
                testFiles = new File[] { basedir };
            }
            if (testFiles == null) {
                System.err.println("Could not locate tool results for path " + basedir.getAbsolutePath());
                System.exit(1);
            }
            System.out.println("Tool files: " + Arrays.toString(testFiles));
            MultipleAlignmentRowEvaluation re = new MultipleAlignmentRowEvaluation(outputDir, gtFile, testFiles);
            double deltav = 0;
            if (cl.hasOption("d")) {
                deltav = Double.valueOf(cl.getOptionValue("d"));
            }
            re.eval(deltav);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
