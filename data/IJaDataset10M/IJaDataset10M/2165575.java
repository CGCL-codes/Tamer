package org.expasy.jpl.msident.model;

import java.io.File;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import junit.framework.Assert;
import org.expasy.jpl.commons.base.TypedDatum.IncompatibleTypedDatumException;
import org.expasy.jpl.commons.collection.graph.Edge;
import org.expasy.jpl.commons.collection.graph.GraphExporter;
import org.expasy.jpl.commons.collection.graph.Node;
import org.expasy.jpl.core.mol.polymer.pept.Peptide;
import org.expasy.jpl.core.mol.polymer.pept.fragmenter.FragmentationType;
import org.expasy.jpl.core.mol.polymer.pept.fragmenter.PeptideFragmenter;
import org.expasy.jpl.core.mol.polymer.pept.rule.EditionRule;
import org.expasy.jpl.core.mol.polymer.pept.rule.PeptideEditorFactory;
import org.expasy.jpl.core.ms.spectrum.annot.FragmentAnnotationImpl.ModifFormat;
import org.expasy.jpl.io.hadoop.ms.HMapMSManager;
import org.expasy.jpl.io.ms.reader.MGFReader;
import org.expasy.jpl.io.ms.writer.SPTXTWriter;
import org.expasy.jpl.msident.format.pepxml.io.MSnRun;
import org.expasy.jpl.msident.format.pepxml.io.MSnRunBuilder;
import org.expasy.jpl.msident.format.pepxml.io.PepXMLReader;
import org.expasy.jpl.msident.format.pepxml.io.PepXMLReader2Test;
import org.expasy.jpl.msident.model.impl.MSSCandidate;
import org.expasy.jpl.msident.model.impl.PeptideCandidate;
import org.expasy.jpl.msident.model.impl.SpectrumAnnotator;
import org.expasy.jpl.msident.model.impl.result.PSMResultsImpl;
import org.expasy.jpl.msident.model.impl.result.PeptideMatchingScore;
import org.expasy.jpl.msmatch.PSMFactory;
import org.junit.Before;
import org.junit.Test;

public class JPLSpectrumAnnotatorTest {

    SpectrumAnnotator annotator;

    String pepXmlFilename;

    MSnRun run;

    @Before
    public void setUp() throws Exception {
        pepXmlFilename = "albu-co3_tandem.pep.xml";
        pepXmlFilename = ClassLoader.getSystemResource(pepXmlFilename).getFile();
        PepXMLReader reader = PepXMLReader2Test.newPepXMLReader(new MGFReader(new MGFReader.TitleParserRegexImpl("scan (\\d+)")));
        reader.parse(new File(pepXmlFilename));
        Collection<MSnRun> runs = new ArrayList<MSnRun>();
        for (MSnRun run : reader) {
            runs.add(run);
        }
        annotator = new SpectrumAnnotator();
        run = runs.iterator().next();
    }

    @Test
    public void testAnnotator() throws Exception {
        PeptideFragmenter fragmenter = new PeptideFragmenter.Builder(EnumSet.of(FragmentationType.AX, FragmentationType.BY, FragmentationType.CZ, FragmentationType.PRECURSOR, FragmentationType.IMMONIUM)).modifFormat(ModifFormat.DOUBLE).build();
        PSMFactory factory = new PSMFactory.Builder().fragmenter(fragmenter).tolerance(0.1).build();
        annotator.setPSMFactory(factory);
        annotator.setFilterConditionXP("peptideprophet>=0.9 & hyperscore >= 606.0");
        annotator.process(run);
        SPTXTWriter writer = SPTXTWriter.newInstance();
        writer.open(File.createTempFile("annotatedSpectra", ".sptxt"));
        Assert.assertEquals(4, annotator.getAnnotatedSpectra().size());
        writer.addAll(annotator.getAnnotatedSpectra());
        writer.flush();
        writer.close();
    }

    @Test
    public void testAnnotatorCoupledWithWriter() throws Exception {
        PeptideFragmenter fragmenter = new PeptideFragmenter.Builder(EnumSet.of(FragmentationType.AX, FragmentationType.BY, FragmentationType.CZ, FragmentationType.PRECURSOR, FragmentationType.IMMONIUM)).modifFormat(ModifFormat.DOUBLE).build();
        PSMFactory factory = new PSMFactory.Builder().fragmenter(fragmenter).tolerance(0.1).build();
        annotator.setPSMFactory(factory);
        annotator.setFilterConditionXP("peptideprophet>=0.9 & hyperscore >= 606.0");
        SPTXTWriter writer = SPTXTWriter.newInstance();
        writer.open(File.createTempFile("annotatedSpectra", ".sptxt"));
        annotator.process(run, writer);
        Assert.assertEquals(0, annotator.getAnnotatedSpectra().size());
    }

    @Test
    public void testAnnotator2() throws Exception {
        PeptideFragmenter fragmenter = new PeptideFragmenter.Builder(EnumSet.of(FragmentationType.AX, FragmentationType.BY, FragmentationType.CZ, FragmentationType.PRECURSOR, FragmentationType.IMMONIUM)).modifFormat(ModifFormat.DOUBLE).build();
        PSMFactory factory = new PSMFactory.Builder().fragmenter(fragmenter).tolerance(0.1).build();
        annotator.setPSMFactory(factory);
        annotator.setFilterConditionXP("peptideprophet>=0.9 & hyperscore >= 606.0");
        annotator.process(run);
    }

    @Test
    public void testAnnotator3() throws Exception {
        PeptideFragmenter fragmenter = new PeptideFragmenter.Builder(EnumSet.of(FragmentationType.AX, FragmentationType.BY, FragmentationType.CZ, FragmentationType.PRECURSOR, FragmentationType.IMMONIUM)).modifFormat(ModifFormat.DOUBLE).build();
        PSMFactory factory = new PSMFactory.Builder().fragmenter(fragmenter).tolerance(0.1).build();
        annotator.setPSMFactory(factory);
        annotator.setFilterConditionXP("hyperscore >= 606");
        annotator.process(run);
    }

    @Test
    public void testBuildRun() throws Exception {
        run = createRun();
        PeptideCandidate cand = run.getPeptideCandidates().get("NNNNNNNNNNNNDDNGNNNNNNSGNDNNNTTNNDSNNK/4");
        System.out.println(cand);
    }

    @Test
    public void testAnnotatorWeirdPeptide() throws Exception {
        PeptideFragmenter fragmenter = new PeptideFragmenter.Builder(EnumSet.of(FragmentationType.AX, FragmentationType.BY, FragmentationType.CZ, FragmentationType.PRECURSOR, FragmentationType.IMMONIUM)).modifFormat(ModifFormat.DOUBLE).enableWALosses().build();
        fragmenter = new PeptideFragmenter.Builder(EnumSet.of(FragmentationType.AX, FragmentationType.BY, FragmentationType.CZ)).modifFormat(ModifFormat.DOUBLE).enableWALosses().build();
        PSMFactory factory = new PSMFactory.Builder().fragmenter(fragmenter).tolerance(0.1).build();
        GraphExporter<Node, Edge> exporter = new GraphExporter.Builder<Node, Edge>().printStream(new PrintStream("/tmp/graph1.txt")).build();
        run = createRun();
        exporter.exportKPGraph(run.getGraph());
        run.mergeMSNodes();
        exporter.setPrintStream(new PrintStream("/tmp/graph2.txt"));
        exporter.exportKPGraph(run.getGraph());
        annotator.setPSMFactory(factory);
        annotator.setFilterConditionXP("peptideprophet>=0.9");
        SPTXTWriter writer = SPTXTWriter.newInstance();
        writer.open(new File("/tmp/weirdspectra.sptxt"));
        System.out.println("processing...");
        annotator.process(run, writer);
        System.out.println("end processing!");
    }

    @Test
    public void testWL() {
        List<EditionRule> rules = Arrays.asList(EditionRule.AMMONIUM_LOSS_KNQR_RULE, EditionRule.WATER_LOSS_STDE_RULE);
        System.out.println(rules);
        PeptideEditorFactory factory = PeptideEditorFactory.newInstance(rules);
        factory.setMaxSiteNumber(2);
        Peptide peptide = new Peptide.Builder("NNNNNNNNNNNNDDNGNNNNNNSGNDNNNTTNNDSNNK").protons(4).build();
        peptide = new Peptide.Builder("RQLANT({79.97})PAK").protons(2).build();
        for (Peptide pep : factory.transform(peptide)) {
            System.out.println(pep);
        }
    }

    private static MSnRun createRun() throws ParseException, IncompatibleTypedDatumException {
        MSnRunBuilder builder = new MSnRunBuilder();
        HMapMSManager hmsManager = HMapMSManager.newInstance();
        hmsManager.addResourcePath("/home/def/Documents/SIB/jpl/data/mgf/liber-bug/mgf");
        hmsManager.resetDelegatedReader(new MGFReader(new MGFReader.TitleParserRegexImpl("[^.]+\\.c\\.(\\d+)")));
        hmsManager.enableForceConversion(false);
        PeptideCandidate pepCand = new PeptideCandidate.Builder("NNNNNNNNNNNNDDNGNNNNNNSGNDNNNTTNNDSNNK").charge(4).build();
        MSSCandidate scanCand = new MSSCandidate("chludwig_M1103_126.c.00234.00234.4", MSSCandidate.SPECTRUM_ID_PARSER);
        scanCand.setStartScanIndex(234);
        scanCand.setEndScanIndex(234);
        scanCand.setPrecursorCharge(4);
        scanCand.setHmsManager(hmsManager);
        PSMResultsImpl results = new PSMResultsImpl(scanCand, pepCand, 1, 1);
        PeptideMatchingScore score = new PeptideMatchingScore("peptideprophet");
        score.assignValue("0.999");
        results.addScore(score);
        builder.addMatch(results);
        scanCand = new MSSCandidate("chludwig_M1103_126.c.00243.00243.4", MSSCandidate.SPECTRUM_ID_PARSER);
        scanCand.setStartScanIndex(243);
        scanCand.setEndScanIndex(243);
        scanCand.setPrecursorCharge(4);
        scanCand.setHmsManager(hmsManager);
        results = new PSMResultsImpl(scanCand, pepCand, 1, 1);
        score = new PeptideMatchingScore("peptideprophet");
        score.assignValue("0.9037");
        results.addScore(score);
        builder.addMatch(results);
        scanCand = new MSSCandidate("chludwig_M1103_123.c.00314.00314.4", MSSCandidate.SPECTRUM_ID_PARSER);
        scanCand.setStartScanIndex(314);
        scanCand.setEndScanIndex(314);
        scanCand.setPrecursorCharge(4);
        scanCand.setHmsManager(hmsManager);
        results = new PSMResultsImpl(scanCand, pepCand, 1, 1);
        score = new PeptideMatchingScore("peptideprophet");
        score.assignValue("1.00");
        results.addScore(score);
        builder.addMatch(results);
        return builder.build();
    }
}
