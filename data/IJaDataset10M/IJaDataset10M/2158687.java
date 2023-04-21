package edu.usc.epigenome.uecgatk.BisSNP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.sf.samtools.SAMFileWriter;
import net.sf.samtools.SAMFileWriterFactory;
import net.sf.samtools.SAMSequenceDictionary;
import org.broadinstitute.sting.utils.variantcontext.Genotype;
import org.broadinstitute.sting.utils.variantcontext.VariantContext;
import org.broadinstitute.sting.utils.codecs.vcf.SortingVCFWriter;
import org.broadinstitute.sting.utils.codecs.vcf.VCFConstants;
import org.broadinstitute.sting.utils.codecs.vcf.VCFFilterHeaderLine;
import org.broadinstitute.sting.utils.codecs.vcf.VCFHeader;
import org.broadinstitute.sting.utils.codecs.vcf.VCFHeaderLine;
import org.broadinstitute.sting.utils.codecs.vcf.VCFHeaderLineType;
import org.broadinstitute.sting.utils.codecs.vcf.VCFInfoHeaderLine;
import org.broadinstitute.sting.utils.codecs.vcf.VCFWriter;
import org.broadinstitute.sting.utils.collections.Pair;
import org.broadinstitute.sting.commandline.Argument;
import org.broadinstitute.sting.commandline.ArgumentCollection;
import org.broadinstitute.sting.commandline.Output;
import org.broadinstitute.sting.commandline.RodBinding;
import org.broadinstitute.sting.gatk.DownsampleType;
import org.broadinstitute.sting.gatk.GenomeAnalysisEngine;
import org.broadinstitute.sting.gatk.contexts.AlignmentContext;
import org.broadinstitute.sting.gatk.contexts.ReferenceContext;
import org.broadinstitute.sting.gatk.datasources.rmd.ReferenceOrderedDataSource;
import org.broadinstitute.sting.gatk.datasources.reads.SAMDataSource;
import org.broadinstitute.sting.gatk.refdata.RefMetaDataTracker;
import org.broadinstitute.sting.gatk.walkers.BAQMode;
import org.broadinstitute.sting.gatk.walkers.By;
import org.broadinstitute.sting.gatk.walkers.DataSource;
import org.broadinstitute.sting.gatk.walkers.Downsample;
import org.broadinstitute.sting.gatk.walkers.LocusWalker;
import org.broadinstitute.sting.gatk.walkers.Reference;
import org.broadinstitute.sting.gatk.walkers.TreeReducible;
import org.broadinstitute.sting.gatk.walkers.Window;
import org.broadinstitute.sting.gatk.walkers.annotator.VariantAnnotatorEngine;
import org.broadinstitute.sting.gatk.walkers.genotyper.GenotypeLikelihoodsCalculationModel;
import org.broadinstitute.sting.gatk.walkers.genotyper.UnifiedArgumentCollection;
import org.broadinstitute.sting.gatk.walkers.genotyper.UnifiedGenotyper;
import org.broadinstitute.sting.gatk.walkers.genotyper.UnifiedGenotyperEngine;
import org.broadinstitute.sting.gatk.walkers.genotyper.VariantCallContext;
import org.broadinstitute.sting.gatk.walkers.genotyper.UnifiedGenotyper.UGStatistics;
import org.broadinstitute.sting.utils.GenomeLoc;
import org.broadinstitute.sting.utils.SampleUtils;
import org.broadinstitute.sting.utils.baq.BAQ;
import org.broadinstitute.sting.utils.pileup.PileupElement;
import org.broadinstitute.sting.utils.pileup.ReadBackedPileup;
import org.broadinstitute.sting.utils.sam.GATKSAMRecord;
import edu.usc.epigenome.uecgatk.BisSNP.BisulfiteEnums.MethylSNPModel;
import edu.usc.epigenome.uecgatk.BisSNP.BisulfiteEnums.OUTPUT_MODE;
import edu.usc.epigenome.uecgatk.YapingWalker.verboseWriter;
import edu.usc.epigenome.uecgatk.YapingWriter.FormatWriterBase;
import edu.usc.epigenome.uecgatk.YapingWriter.NOMeSeqReads;
import edu.usc.epigenome.uecgatk.YapingWriter.SortingCpgReadsWriter;
import edu.usc.epigenome.uecgatk.YapingWriter.SortingFormatWriterBase;
import edu.usc.epigenome.uecgatk.YapingWriter.cpgReads;
import edu.usc.epigenome.uecgatk.YapingWriter.cpgReadsWriterImp;
import edu.usc.epigenome.uecgatk.YapingWriter.SortingNOMeSeqReadsWriter;
import edu.usc.epigenome.uecgatk.YapingWriter.NOMeSeqReadsWriterImp;

/**
 * A Bisulfite genotyper. Works for single-sample data right now. 
 */
@BAQMode(QualityMode = BAQ.QualityMode.OVERWRITE_QUALS, ApplicationTime = BAQ.ApplicationTime.ON_INPUT)
@Reference(window = @Window(start = -500, stop = 500))
@By(DataSource.REFERENCE)
@Downsample(by = DownsampleType.NONE)
public class BisulfiteGenotyper extends LocusWalker<BisulfiteVariantCallContext, BisulfiteGenotyper.ContextCondition> implements TreeReducible<BisulfiteGenotyper.ContextCondition> {

    @ArgumentCollection
    private static BisulfiteArgumentCollection BAC = new BisulfiteArgumentCollection();

    private static boolean autoEstimateC = false;

    private static boolean secondIteration = false;

    private static Set<String> samples = null;

    private static int MAXIMUM_CACHE_FOR_OUTPUT_VCF = 3000000;

    private static long COUNT_CACHE_FOR_OUTPUT_VCF = 1;

    private static long COUNT_CACHE_FOR_OUTPUT_READS = 1;

    private static int MAXIMUM_CACHE_FOR_OUTPUT_READS = 300000;

    protected TcgaVCFWriter writer = null;

    protected SortingTcgaVCFWriter multiThreadWriter = null;

    protected TcgaVCFWriter additionalWriterForDefaultTcgaMode = null;

    protected SortingTcgaVCFWriter multiAdditionalWriterForDefaultTcgaMode = null;

    protected FormatWriterBase readsWriter = null;

    protected SortingFormatWriterBase multiThreadCpgReadsWriter = null;

    protected TcgaVCFWriter verboseWriter = null;

    protected SAMFileWriter samWriter = null;

    private int SAMPLE_READS_MEAN_COVERAGE = 30;

    private BisulfiteGenotyperEngine BG_engine = null;

    private String argCommandline = "";

    private CytosinePatternsUserDefined cytosineDefinedMemorizedForSecondRun = null;

    /**
     * Inner class for collecting output statistics
     */
    public static class ContextCondition {

        /** The total number of passes examined -- i.e., the number of map calls */
        long nBasesVisited = 0;

        /** The number of bases that were potentially callable -- i.e., those not at excessive coverage or masked with N */
        long nBasesCallable = 0;

        /** The number of bases called confidently (according to user threshold), either ref or other */
        long nBasesCalledConfidently = 0;

        /** The number of bases for which calls were emitted */
        long nCallsMade = 0;

        /** The total number of extended events encountered */
        long nExtendedEvents = 0;

        double percentCallableOfAll() {
            return (100.0 * nBasesCallable) / (nBasesVisited - nExtendedEvents);
        }

        double percentCalledOfAll() {
            return (100.0 * nBasesCalledConfidently) / (nBasesVisited - nExtendedEvents);
        }

        double percentCalledOfCallable() {
            return (100.0 * nBasesCalledConfidently) / (nBasesCallable);
        }

        HashMap<String, HashMap<String, Pair<Integer, Double>>> cytosineMethySummaryByReadsGroup = new HashMap<String, HashMap<String, Pair<Integer, Double>>>();

        HashMap<String, Pair<Integer, Double>> cytosineMethySummary = new HashMap<String, Pair<Integer, Double>>();

        void makeCytosineMap() {
            for (String cytosineKey : BAC.cytosineDefined.getContextDefined().keySet()) {
                Pair<Integer, Double> methySummary = new Pair<Integer, Double>(0, 0.0);
                cytosineMethySummary.put(cytosineKey, methySummary);
            }
            for (String sample : samples) {
                HashMap<String, Pair<Integer, Double>> cytosineMethySummaryTmp = new HashMap<String, Pair<Integer, Double>>();
                for (String cytosineKey : BAC.cytosineDefined.getContextDefined().keySet()) {
                    Pair<Integer, Double> methySummary = new Pair<Integer, Double>(0, 0.0);
                    cytosineMethySummaryTmp.put(cytosineKey, methySummary);
                }
                cytosineMethySummaryByReadsGroup.put(sample, cytosineMethySummaryTmp);
            }
        }
    }

    /**
     * Initialize the samples, output, and genotype calculation model
     *
     **/
    public void initialize() {
        samples = new TreeSet<String>();
        if (BAC.ASSUME_SINGLE_SAMPLE != null) {
            samples = SampleUtils.getSAMFileSamples(getToolkit().getSAMFileHeader());
            if (!samples.isEmpty()) {
                System.out.println("sample name provided was masked by bam file header");
            } else {
                samples.add(BAC.ASSUME_SINGLE_SAMPLE);
            }
        } else {
            samples = SampleUtils.getSAMFileSamples(getToolkit().getSAMFileHeader());
            System.out.println("samples provided: " + samples.toString());
            if (samples.isEmpty()) {
                System.err.println("No sample name provided, program will automately provide the bam file header: " + samples.toString());
            }
        }
        SAMSequenceDictionary refDict = getToolkit().getMasterSequenceDictionary();
        if (autoEstimateC) {
            if (secondIteration) {
                initiateVCFInDifferentOutmode(refDict);
            } else {
            }
        } else {
            initiateVCFInDifferentOutmode(refDict);
        }
        if (!secondIteration) cytosineDefinedMemorizedForSecondRun = new CytosinePatternsUserDefined(BAC.cytosineContextsAcquired);
    }

    /**
     * get VCF header for the output VCF file
     *
     **/
    private Set<VCFHeaderLine> getHeaderInfo() {
        Set<VCFHeaderLine> headerInfo = new HashSet<VCFHeaderLine>();
        if (!BAC.NO_SLOD) headerInfo.add(new VCFInfoHeaderLine(VCFConstants.STRAND_BIAS_KEY, 1, VCFHeaderLineType.Float, "Strand Bias"));
        headerInfo.add(new VCFInfoHeaderLine(BisulfiteVCFConstants.CYTOSINE_TYPE, -1, VCFHeaderLineType.String, "Cytosine Type"));
        headerInfo.add(new VCFInfoHeaderLine(BisulfiteVCFConstants.GENOTYPE_TYPE, 1, VCFHeaderLineType.String, "Genotype Type"));
        headerInfo.add(new VCFInfoHeaderLine(BisulfiteVCFConstants.C_STRAND_KEY, 1, VCFHeaderLineType.String, "Cytosine in negative strand"));
        headerInfo.add(new VCFInfoHeaderLine(BisulfiteVCFConstants.NUMBER_OF_C_KEY, 1, VCFHeaderLineType.Integer, "number of C in this Cytosine position"));
        headerInfo.add(new VCFInfoHeaderLine(BisulfiteVCFConstants.NUMBER_OF_T_KEY, 1, VCFHeaderLineType.Integer, "number of T in this Cytosine position"));
        headerInfo.add(new VCFInfoHeaderLine(BisulfiteVCFConstants.CYTOSINE_METHY_VALUE, 1, VCFHeaderLineType.Float, "Methylation value in this Cytosine position"));
        if (BAC.dbsnp.isBound()) headerInfo.add(new VCFInfoHeaderLine(VCFConstants.DBSNP_KEY, 0, VCFHeaderLineType.Flag, "dbSNP Membership"));
        if (BAC.STANDARD_CONFIDENCE_FOR_EMITTING < BAC.STANDARD_CONFIDENCE_FOR_CALLING) headerInfo.add(new VCFFilterHeaderLine(UnifiedGenotyperEngine.LOW_QUAL_FILTER_NAME, "Low quality"));
        headerInfo.add(new VCFHeaderLine(BisulfiteVCFConstants.PROGRAM_ARGS, argCommandline));
        return headerInfo;
    }

    /**
     * Compute at a given locus.
     *
     * @param tracker the meta data tracker
     * @param refContext the reference base
     * @param rawContext contextual information around the locus
     * @return the VariantCallContext object
     */
    public BisulfiteVariantCallContext map(RefMetaDataTracker tracker, ReferenceContext refContext, AlignmentContext rawContext) {
        BG_engine = new BisulfiteGenotyperEngine(tracker, refContext, rawContext, BAC, getToolkit(), autoEstimateC, secondIteration);
        if (secondIteration) {
        } else {
            if (BAC.orad) {
            }
        }
        return BG_engine.getBisulfiteVariantCallContext();
    }

    /**
     * Initiate statistics object.
     */
    public ContextCondition reduceInit() {
        ContextCondition initiated = new ContextCondition();
        initiated.makeCytosineMap();
        return initiated;
    }

    public ContextCondition treeReduce(ContextCondition lhs, ContextCondition rhs) {
        lhs.nBasesCallable += rhs.nBasesCallable;
        lhs.nBasesCalledConfidently += rhs.nBasesCalledConfidently;
        lhs.nBasesVisited += rhs.nBasesVisited;
        lhs.nCallsMade += rhs.nCallsMade;
        if (!rhs.cytosineMethySummary.isEmpty()) {
            for (String key : rhs.cytosineMethySummary.keySet()) {
                Pair<Integer, Double> rhsValue = rhs.cytosineMethySummary.get(key);
                Pair<Integer, Double> lhsValue;
                if (lhs.cytosineMethySummary.containsKey(key)) {
                    lhsValue = lhs.cytosineMethySummary.get(key);
                    lhsValue.first += rhsValue.getFirst();
                    lhsValue.second += rhsValue.getSecond();
                    lhs.cytosineMethySummary.put(key, lhsValue);
                }
            }
        }
        if (!rhs.cytosineMethySummaryByReadsGroup.isEmpty()) {
            for (String sampleKey : rhs.cytosineMethySummaryByReadsGroup.keySet()) {
                if (lhs.cytosineMethySummaryByReadsGroup.containsKey(sampleKey)) {
                    HashMap<String, Pair<Integer, Double>> cytosineMethySummaryLhs = lhs.cytosineMethySummaryByReadsGroup.get(sampleKey);
                    for (String cytosineKey : rhs.cytosineMethySummaryByReadsGroup.get(sampleKey).keySet()) {
                        Pair<Integer, Double> rhsValue = rhs.cytosineMethySummaryByReadsGroup.get(sampleKey).get(cytosineKey);
                        Pair<Integer, Double> lhsValue;
                        if (cytosineMethySummaryLhs.containsKey(cytosineKey)) {
                            lhsValue = cytosineMethySummaryLhs.get(cytosineKey);
                            lhsValue.first += rhsValue.getFirst();
                            lhsValue.second += rhsValue.getSecond();
                            cytosineMethySummaryLhs.put(cytosineKey, lhsValue);
                        }
                    }
                    lhs.cytosineMethySummaryByReadsGroup.put(sampleKey, cytosineMethySummaryLhs);
                }
            }
        }
        return lhs;
    }

    /**
     * calculate statistics number in each reduce steps.
     */
    public ContextCondition reduce(BisulfiteVariantCallContext value, ContextCondition sum) {
        sum.nBasesVisited++;
        if (value == null || value.getVariantContext() == null) return sum;
        sum.nBasesCallable++;
        sum.nBasesCalledConfidently += value.confidentlyCalled ? 1 : 0;
        if (!value.shouldEmit) return sum;
        for (String sampleKey : value.getBisulfiteContextsGenotypeLikelihoods().keySet()) {
            BisulfiteContextsGenotypeLikelihoods bcglTmp = value.getBisulfiteContextsGenotypeLikelihoods().get(sampleKey);
            HashMap<String, CytosineParameters> cytosineParameters = bcglTmp.getCytosineParameters();
            HashMap<String, Pair<Integer, Double>> cytosineMethySummaryInEachReadGroup = sum.cytosineMethySummaryByReadsGroup.get(sampleKey);
            for (String cytosineKey : cytosineParameters.keySet()) {
                if (cytosineParameters.get(cytosineKey).isCytosinePattern) {
                    Integer cytosineNum = cytosineMethySummaryInEachReadGroup.get(cytosineKey).getFirst() + 1;
                    Double methyValue = cytosineMethySummaryInEachReadGroup.get(cytosineKey).getSecond();
                    if (!Double.isNaN(bcglTmp.getMethylationLevel())) {
                        methyValue += bcglTmp.getMethylationLevel();
                    }
                    Pair<Integer, Double> sumValue = cytosineMethySummaryInEachReadGroup.get(cytosineKey);
                    sumValue.set(cytosineNum, methyValue);
                    cytosineMethySummaryInEachReadGroup.put(cytosineKey, sumValue);
                }
            }
            sum.cytosineMethySummaryByReadsGroup.put(sampleKey, cytosineMethySummaryInEachReadGroup);
        }
        for (String cytosineKey : BAC.cytosineDefined.getContextDefined().keySet()) {
            Integer cytosineNumTotal = 0;
            Double methyValueTotal = 0.0;
            for (HashMap<String, Pair<Integer, Double>> cytosineMethyEachRG : sum.cytosineMethySummaryByReadsGroup.values()) {
                if (cytosineMethyEachRG.containsKey(cytosineKey)) {
                    cytosineNumTotal += cytosineMethyEachRG.get(cytosineKey).getFirst();
                    methyValueTotal += cytosineMethyEachRG.get(cytosineKey).getSecond();
                }
            }
            Pair<Integer, Double> sumValueInTotal = sum.cytosineMethySummary.get(cytosineKey);
            sumValueInTotal.set(cytosineNumTotal, methyValueTotal);
            sum.cytosineMethySummary.put(cytosineKey, sumValueInTotal);
        }
        try {
            sum.nCallsMade++;
            readsReport(value);
            if (autoEstimateC) {
                if (secondIteration) {
                    outputVCFInDifferentOutmode(value);
                }
            } else {
                outputVCFInDifferentOutmode(value);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage() + "; this is often caused by using the --assume_single_sample_reads argument with the wrong sample name");
        }
        return sum;
    }

    public void onTraversalDone(ContextCondition sum) {
        String fn = BAC.vfn1 + ".BisSNPMethySummarizeList.txt";
        PrintWriter outWriter = null;
        String outLine = null;
        try {
            outWriter = new PrintWriter(new File(fn));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        logger.info(String.format("Visited bases                                %d", sum.nBasesVisited));
        logger.info(String.format("Callable bases                               %d", sum.nBasesCallable));
        logger.info(String.format("Confidently called bases                     %d", sum.nBasesCalledConfidently));
        logger.info(String.format("%% callable bases of all loci                 %3.3f", sum.percentCallableOfAll()));
        logger.info(String.format("%% confidently called bases of all loci       %3.3f", sum.percentCalledOfAll()));
        logger.info(String.format("%% confidently called bases of callable loci  %3.3f", sum.percentCalledOfCallable()));
        logger.info(String.format("Actual calls made                            %d", sum.nCallsMade));
        if (!sum.cytosineMethySummary.isEmpty()) {
            logger.info(String.format("-------------Methylation summary in total"));
            outLine = outLine + String.format("##Methylation summary in total:") + "\n";
            for (String key : sum.cytosineMethySummary.keySet()) {
                Pair<Integer, Double> methyValue = sum.cytosineMethySummary.get(key);
                logger.info(String.format("%% Methylation level of %s loci in total:       %3.3f", key, methyValue.getSecond() / methyValue.getFirst()));
                logger.info(String.format(" number of %s loci in total:       %d", key, methyValue.getFirst() / samples.size()));
                outLine = outLine + key + ":" + methyValue.getSecond() / methyValue.getFirst() + "\t" + methyValue.getFirst() / samples.size() + "\n";
            }
        }
        if (!sum.cytosineMethySummaryByReadsGroup.isEmpty()) {
            for (String sampleKey : sum.cytosineMethySummaryByReadsGroup.keySet()) {
                logger.info(String.format("---------- Methylation summary in Read Group:        %s", sampleKey));
                outLine = outLine + String.format("##Methylation summary in Read Group:\t%s", sampleKey) + "\n";
                for (String key : sum.cytosineMethySummaryByReadsGroup.get(sampleKey).keySet()) {
                    Pair<Integer, Double> methyValue = sum.cytosineMethySummaryByReadsGroup.get(sampleKey).get(key);
                    logger.info(String.format("%% Methylation level of %s loci in total:       %3.3f", key, methyValue.getSecond() / methyValue.getFirst()));
                    logger.info(String.format(" number of %s loci in total:       %d", key, methyValue.getFirst()));
                    outLine = outLine + key + ":" + methyValue.getSecond() / methyValue.getFirst() + "\t" + methyValue.getFirst() + "\n";
                }
            }
        }
        if (autoEstimateC && !secondIteration) {
            for (String cytosineKey : cytosineDefinedMemorizedForSecondRun.getContextDefined().keySet()) {
                CytosineParameters cytosineParameters = cytosineDefinedMemorizedForSecondRun.getContextDefined().get(cytosineKey);
                Pair<Integer, Double> methyValue = sum.cytosineMethySummary.get(cytosineKey);
                cytosineParameters.cytosineMethylation = methyValue.getSecond() / (methyValue.getFirst() * samples.size());
                cytosineDefinedMemorizedForSecondRun.getContextDefined().put(cytosineKey, cytosineParameters);
            }
        }
        if (BAC.orad) {
            samWriter.close();
        }
        if (BAC.OutputMode == OUTPUT_MODE.DEFAULT_FOR_TCGA) {
        }
        if (getToolkit().getArguments().numberOfThreads > 1 && ((autoEstimateC && secondIteration) || (!autoEstimateC && !secondIteration))) {
            multiThreadWriter.close();
            if (BAC.OutputMode == OUTPUT_MODE.DEFAULT_FOR_TCGA) {
                multiAdditionalWriterForDefaultTcgaMode.close();
            }
        }
        if (BAC.fnocrd != null) {
            if ((autoEstimateC && secondIteration) || (!autoEstimateC && !secondIteration)) {
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiThreadCpgReadsWriter.close();
                } else {
                    readsWriter.close();
                }
            }
        }
        outWriter.println(outLine);
        outWriter.close();
    }

    public void setCytosineMethyStatus(CytosinePatternsUserDefined cytosineDefinedMemorizedForSecondRun) {
        BAC.cytosineDefined = cytosineDefinedMemorizedForSecondRun.clone();
    }

    public void setAutoParameters(boolean autoEstimateC, boolean secondIteration, String argCommandline) {
        this.autoEstimateC = autoEstimateC;
        this.secondIteration = secondIteration;
        this.argCommandline = argCommandline;
    }

    public CytosinePatternsUserDefined getCytosineMethyStatus() {
        return cytosineDefinedMemorizedForSecondRun.clone();
    }

    public TcgaVCFWriter getWriter() {
        return writer;
    }

    public void setWriter(TcgaVCFWriter writer) {
        this.writer = writer;
    }

    private void initiateVCFInDifferentOutmode(SAMSequenceDictionary refDict) {
        File outputVcfFile = new File(BAC.vfn1);
        writer = new TcgaVCFWriter(outputVcfFile, refDict, true);
        writer.setRefSource(getToolkit().getArguments().referenceFile.toString());
        writer.writeHeader(new VCFHeader(getHeaderInfo(), samples));
        if (getToolkit().getArguments().numberOfThreads > 1) {
            multiThreadWriter = new SortingTcgaVCFWriter(writer, MAXIMUM_CACHE_FOR_OUTPUT_VCF);
            if (BAC.ovd) {
                File outputVerboseFile = new File(BAC.fnovd);
                verboseWriter = new TcgaVCFWriter(outputVerboseFile, refDict, false);
                verboseWriter.writeHeader(new VCFHeader(getHeaderInfo(), samples));
            }
        }
        if (BAC.OutputMode == OUTPUT_MODE.DEFAULT_FOR_TCGA) {
            File outputAdditionalVcfFile = new File(BAC.vfn2);
            additionalWriterForDefaultTcgaMode = new TcgaVCFWriter(outputAdditionalVcfFile, refDict, true);
            additionalWriterForDefaultTcgaMode.writeHeader(new VCFHeader(getHeaderInfo(), samples));
            if (getToolkit().getArguments().numberOfThreads > 1) {
                multiAdditionalWriterForDefaultTcgaMode = new SortingTcgaVCFWriter(additionalWriterForDefaultTcgaMode, MAXIMUM_CACHE_FOR_OUTPUT_VCF);
            }
        }
        if (BAC.orad) {
            File outputBamFile = new File(BAC.fnorad);
            SAMFileWriterFactory samFileWriterFactory = new SAMFileWriterFactory();
            samFileWriterFactory.setCreateIndex(true);
            samWriter = samFileWriterFactory.makeBAMWriter(getToolkit().getSAMFileHeader(), false, outputBamFile);
        }
        if (BAC.fnocrd != null) {
            File outputReadsDetailFile = new File(BAC.fnocrd);
            if (BAC.sequencingMode == MethylSNPModel.GM) {
                readsWriter = new NOMeSeqReadsWriterImp(outputReadsDetailFile);
            } else {
                readsWriter = new cpgReadsWriterImp(outputReadsDetailFile);
            }
            if (getToolkit().getArguments().numberOfThreads > 1) {
                if (BAC.sequencingMode == MethylSNPModel.GM) {
                    multiThreadCpgReadsWriter = new SortingNOMeSeqReadsWriter(readsWriter, MAXIMUM_CACHE_FOR_OUTPUT_READS);
                } else {
                    multiThreadCpgReadsWriter = new SortingCpgReadsWriter(readsWriter, MAXIMUM_CACHE_FOR_OUTPUT_READS);
                }
                multiThreadCpgReadsWriter.writeHeader(true);
            } else {
                readsWriter.addHeader(true);
            }
        }
    }

    private void outputVCFInDifferentOutmode(BisulfiteVariantCallContext value) {
        if (!value.shouldEmit) return;
        if (BAC.OutputMode == OUTPUT_MODE.EMIT_ALL_CYTOSINES) {
            if (value.isC) {
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiThreadWriter.add(value.getVariantContext());
                    COUNT_CACHE_FOR_OUTPUT_VCF++;
                    if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                        multiThreadWriter.writerFlush();
                        System.gc();
                    }
                } else {
                    writer.add(value.getVariantContext());
                }
            }
        } else if (BAC.OutputMode == OUTPUT_MODE.EMIT_ALL_CPG) {
            if (value.isCpg) {
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiThreadWriter.add(value.getVariantContext());
                    COUNT_CACHE_FOR_OUTPUT_VCF++;
                    if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                        multiThreadWriter.writerFlush();
                        System.gc();
                    }
                } else {
                    writer.add(value.getVariantContext());
                }
            }
        } else if (BAC.OutputMode == OUTPUT_MODE.EMIT_VARIANTS_ONLY) {
            if (value.getVariantContext().isVariant()) {
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiThreadWriter.add(value.getVariantContext());
                    COUNT_CACHE_FOR_OUTPUT_VCF++;
                    if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                        multiThreadWriter.writerFlush();
                        System.gc();
                    }
                } else {
                    writer.add(value.getVariantContext());
                }
            }
        } else if (BAC.OutputMode == OUTPUT_MODE.EMIT_HET_SNPS_ONLY) {
            if (value.isHetSnp()) {
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiThreadWriter.add(value.getVariantContext());
                    COUNT_CACHE_FOR_OUTPUT_VCF++;
                    if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                        multiThreadWriter.writerFlush();
                        System.gc();
                    }
                } else {
                    writer.add(value.getVariantContext());
                }
            }
        } else if (BAC.OutputMode == OUTPUT_MODE.DEFAULT_FOR_TCGA) {
            if (value.isCpg) {
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiThreadWriter.add(value.getVariantContext());
                    COUNT_CACHE_FOR_OUTPUT_VCF++;
                    if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                        multiThreadWriter.writerFlush();
                        System.gc();
                    }
                } else {
                    writer.add(value.getVariantContext());
                }
            }
            if (value.getVariantContext().isVariant()) {
                if (getToolkit().getArguments().numberOfThreads > 1) {
                    multiAdditionalWriterForDefaultTcgaMode.add(value.getVariantContext());
                    COUNT_CACHE_FOR_OUTPUT_VCF++;
                    if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                        multiAdditionalWriterForDefaultTcgaMode.writerFlush();
                        System.gc();
                    }
                } else {
                    additionalWriterForDefaultTcgaMode.add(value.getVariantContext());
                }
            }
        } else {
            if (getToolkit().getArguments().numberOfThreads > 1) {
                multiThreadWriter.add(value.getVariantContext());
                COUNT_CACHE_FOR_OUTPUT_VCF++;
                if (COUNT_CACHE_FOR_OUTPUT_VCF % MAXIMUM_CACHE_FOR_OUTPUT_VCF == 0) {
                    multiThreadWriter.writerFlush();
                    System.gc();
                }
                if (BAC.ovd) {
                    verboseWriter.add(value.getVariantContext());
                }
            } else {
                writer.add(value.getVariantContext());
            }
        }
    }

    private void readsReport(BisulfiteVariantCallContext value) {
        if (BAC.sequencingMode == MethylSNPModel.GM) {
            if (BAC.fnocrd != null && ((autoEstimateC && secondIteration) || (!autoEstimateC && !secondIteration))) {
                boolean positiveStrand = true;
                String sampleContext = "";
                for (String key : value.getSummaryAcrossRG().cytosinePatternConfirmedSet) {
                    if (key.equalsIgnoreCase("C")) {
                        continue;
                    }
                    if (value.getSummaryAcrossRG().cytosinePatternStrand == '+') {
                        positiveStrand = true;
                    } else {
                        positiveStrand = false;
                    }
                    if (sampleContext.equals("")) {
                        sampleContext = key;
                    } else {
                        sampleContext = sampleContext + "," + key;
                    }
                }
                if (!sampleContext.equals("")) {
                    readsDetailReportForNOMeSeq(value, positiveStrand, getToolkit().getArguments().numberOfThreads > 1, sampleContext);
                }
            }
        } else {
            if (value.isCpg) {
                if (BAC.fnocrd != null && ((autoEstimateC && secondIteration) || (!autoEstimateC && !secondIteration))) {
                    if (value.getSummaryAcrossRG().cytosinePatternStrand == '+') {
                        readsDetailReport(value, true, getToolkit().getArguments().numberOfThreads > 1);
                    } else {
                        readsDetailReport(value, false, getToolkit().getArguments().numberOfThreads > 1);
                    }
                }
            }
        }
    }

    public void readsDetailReport(BisulfiteVariantCallContext value, boolean posStrand, boolean multiThread) {
        AlignmentContext rawContext = value.rawContext;
        if (rawContext.hasReads()) {
            for (PileupElement p : rawContext.getBasePileup()) {
                GATKSAMRecordFilterStorage GATKrecordFilterStor = new GATKSAMRecordFilterStorage((GATKSAMRecord) p.getRead(), BAC, p.getOffset());
                if (!GATKrecordFilterStor.isGoodBase()) {
                    continue;
                }
                char strand;
                if (p.getRead().getReadNegativeStrandFlag()) {
                    strand = '-';
                    if (!posStrand) {
                        char methyStatus;
                        if (p.getBase() == BaseUtilsMore.G) {
                            methyStatus = 'm';
                            cpgReads cr = new cpgReads(rawContext.getContig(), rawContext.getLocation().getStart(), methyStatus, p.getQual(), strand, p.getRead().getReadName());
                            if (multiThread) {
                                multiThreadCpgReadsWriter.add(cr);
                            } else {
                                readsWriter.add(cr);
                            }
                        } else if (p.getBase() == BaseUtilsMore.A) {
                            methyStatus = 'u';
                            cpgReads cr = new cpgReads(rawContext.getContig(), rawContext.getLocation().getStart(), methyStatus, p.getQual(), strand, p.getRead().getReadName());
                            if (multiThread) {
                                multiThreadCpgReadsWriter.add(cr);
                            } else {
                                readsWriter.add(cr);
                            }
                        }
                    }
                } else {
                    strand = '+';
                    if (posStrand) {
                        char methyStatus;
                        if (p.getBase() == BaseUtilsMore.C) {
                            methyStatus = 'm';
                            cpgReads cr = new cpgReads(rawContext.getContig(), rawContext.getLocation().getStart(), methyStatus, p.getQual(), strand, p.getRead().getReadName());
                            if (multiThread) {
                                multiThreadCpgReadsWriter.add(cr);
                            } else {
                                readsWriter.add(cr);
                            }
                        } else if (p.getBase() == BaseUtilsMore.T) {
                            methyStatus = 'u';
                            cpgReads cr = new cpgReads(rawContext.getContig(), rawContext.getLocation().getStart(), methyStatus, p.getQual(), strand, p.getRead().getReadName());
                            if (multiThread) {
                                multiThreadCpgReadsWriter.add(cr);
                            } else {
                                readsWriter.add(cr);
                            }
                        }
                    }
                }
            }
        }
    }

    public void readsDetailReportForNOMeSeq(BisulfiteVariantCallContext value, boolean posStrand, boolean multiThread, String sampleContext) {
        if (value.rawContext.hasReads()) {
            String ref = "";
            if (posStrand) {
                ref = ref + BaseUtilsMore.convertByteToString(value.ref.getBase());
            } else {
                ref = ref + BaseUtilsMore.convertByteToString(BaseUtilsMore.iupacCodeComplement(value.ref.getBase()));
            }
            GenomeLoc locPre = value.ref.getGenomeLocParser().createGenomeLoc(value.ref.getLocus().getContig(), value.ref.getLocus().getStart() - 1);
            GenomeLoc locPost = value.ref.getGenomeLocParser().createGenomeLoc(value.ref.getLocus().getContig(), value.ref.getLocus().getStart() + 1);
            if (value.ref.getWindow().containsP(locPre)) {
                ReferenceContext tmpRef = new ReferenceContext(value.ref.getGenomeLocParser(), locPre, value.ref.getWindow(), value.ref.getBases());
                if (posStrand) {
                    ref = BaseUtilsMore.convertByteToString(BaseUtilsMore.toIupacCodeNOMeSeqMode(tmpRef.getBase(), 1)) + ref;
                } else {
                    ref = ref + BaseUtilsMore.convertByteToString(BaseUtilsMore.toIupacCodeNOMeSeqMode(BaseUtilsMore.iupacCodeComplement(tmpRef.getBase()), 3));
                }
            }
            if (value.ref.getWindow().containsP(locPost)) {
                ReferenceContext tmpRef = new ReferenceContext(value.ref.getGenomeLocParser(), locPost, value.ref.getWindow(), value.ref.getBases());
                if (posStrand) {
                    ref = ref + BaseUtilsMore.convertByteToString(BaseUtilsMore.toIupacCodeNOMeSeqMode(tmpRef.getBase(), 3));
                } else {
                    ref = BaseUtilsMore.convertByteToString(BaseUtilsMore.toIupacCodeNOMeSeqMode(BaseUtilsMore.iupacCodeComplement(tmpRef.getBase()), 1)) + ref;
                }
            }
            for (PileupElement p : value.rawContext.getBasePileup()) {
                GATKSAMRecordFilterStorage GATKrecordFilterStor = new GATKSAMRecordFilterStorage((GATKSAMRecord) p.getRead(), BAC, p.getOffset());
                if (!GATKrecordFilterStor.isGoodBase()) {
                    continue;
                }
                char strand;
                COUNT_CACHE_FOR_OUTPUT_READS++;
                if (COUNT_CACHE_FOR_OUTPUT_READS % MAXIMUM_CACHE_FOR_OUTPUT_READS == 0) {
                    if (multiThread) {
                        multiThreadCpgReadsWriter.writerFlush();
                    } else {
                        readsWriter.writerFlush();
                    }
                    System.gc();
                }
                if (p.getRead().getReadNegativeStrandFlag()) {
                    strand = '-';
                    if (!posStrand) {
                        char methyStatus;
                        if (p.getBase() == BaseUtilsMore.G) {
                            methyStatus = 'm';
                            NOMeSeqReads cr = new NOMeSeqReads(value.rawContext.getContig(), value.rawContext.getLocation().getStart(), sampleContext, ref, methyStatus, p.getQual(), strand, p.getRead().getReadName());
                            if (multiThread) {
                                multiThreadCpgReadsWriter.add(cr);
                            } else {
                                ((NOMeSeqReadsWriterImp) readsWriter).add(cr);
                            }
                        } else if (p.getBase() == BaseUtilsMore.A) {
                            methyStatus = 'u';
                            NOMeSeqReads cr = new NOMeSeqReads(value.rawContext.getContig(), value.rawContext.getLocation().getStart(), sampleContext, ref, methyStatus, p.getQual(), strand, p.getRead().getReadName());
                            if (multiThread) {
                                multiThreadCpgReadsWriter.add(cr);
                            } else {
                                readsWriter.add(cr);
                            }
                        }
                    }
                } else {
                    strand = '+';
                    if (posStrand) {
                        char methyStatus;
                        if (p.getBase() == BaseUtilsMore.C) {
                            methyStatus = 'm';
                            NOMeSeqReads cr = new NOMeSeqReads(value.rawContext.getContig(), value.rawContext.getLocation().getStart(), sampleContext, ref, methyStatus, p.getQual(), strand, p.getRead().getReadName());
                            if (multiThread) {
                                multiThreadCpgReadsWriter.add(cr);
                            } else {
                                readsWriter.add(cr);
                            }
                        } else if (p.getBase() == BaseUtilsMore.T) {
                            methyStatus = 'u';
                            NOMeSeqReads cr = new NOMeSeqReads(value.rawContext.getContig(), value.rawContext.getLocation().getStart(), sampleContext, ref, methyStatus, p.getQual(), strand, p.getRead().getReadName());
                            if (multiThread) {
                                multiThreadCpgReadsWriter.add(cr);
                            } else {
                                readsWriter.add(cr);
                            }
                        }
                    }
                }
            }
        }
    }

    public static BisulfiteArgumentCollection getBAC() {
        return BAC;
    }
}
