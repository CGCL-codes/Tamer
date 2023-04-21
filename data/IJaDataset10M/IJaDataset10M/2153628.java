package org.expasy.jpl.msmatch;

import java.io.File;
import java.text.ParseException;
import java.util.EnumSet;
import junit.framework.Assert;
import org.expasy.jpl.commons.base.cond.Condition;
import org.expasy.jpl.commons.base.process.ProcessException;
import org.expasy.jpl.commons.base.render.ImageRenderingException;
import org.expasy.jpl.commons.collection.ExtraIterable.AbstractExtraIterator;
import org.expasy.jpl.core.mol.chem.ChemicalFacade;
import org.expasy.jpl.core.mol.chem.MassCalculator;
import org.expasy.jpl.core.mol.chem.api.Molecule;
import org.expasy.jpl.core.mol.modif.ModificationFactory;
import org.expasy.jpl.core.mol.polymer.pept.Peptide;
import org.expasy.jpl.core.mol.polymer.pept.PeptideTypeCondition;
import org.expasy.jpl.core.mol.polymer.pept.fragmenter.FragmentationType;
import org.expasy.jpl.core.mol.polymer.pept.fragmenter.PeptideFragmenter;
import org.expasy.jpl.core.ms.spectrum.PeakList;
import org.expasy.jpl.core.ms.spectrum.PeakListImpl;
import org.expasy.jpl.core.ms.spectrum.peak.AnnotatedPeak;
import org.expasy.jpl.io.ms.MSScan;
import org.expasy.jpl.io.ms.reader.MGFReader;
import org.expasy.jpl.msmatch.PSMFactory.NoMatchException;
import org.junit.Test;

public class JPLMSPeptideMatchFactoryTest {

    PSMFactory factory;

    @Test
    public void test1() throws Exception {
        Peptide prec1 = new Peptide.Builder("EQVQSC({57})GPPPELLNGNVK").protons(2).build();
        String filename = ClassLoader.getSystemResource("EQVQSCGPPPELLNGNVK.mgf").getFile();
        Condition<AnnotatedPeak> cond = new PeptideTypeCondition.Builder<AnnotatedPeak>("y").accessor(AnnotatedPeak.TO_PEAK_TYPE).build();
        factory = new PSMFactory.Builder().fragmenter(new PeptideFragmenter.Builder(EnumSet.of(FragmentationType.AX, FragmentationType.BY, FragmentationType.CZ)).build()).condition(cond).tolerance(0.1).build();
        factory.process(prec1, getExpSpectrum(new File(filename)));
        PeakList pl = factory.getMatchedPeakList();
        System.out.println(pl);
    }

    @Test
    public void test1withloss() throws Exception {
        Peptide prec1 = new Peptide.Builder("EQVQSC({57})GPPPELLNGNVK").protons(2).build();
        String filename = ClassLoader.getSystemResource("EQVQSCGPPPELLNGNVK.mgf").getFile();
        Condition<AnnotatedPeak> cond = new PeptideTypeCondition.Builder<AnnotatedPeak>("y").accessor(AnnotatedPeak.TO_PEAK_TYPE).build();
        factory = new PSMFactory.Builder().fragmenter(new PeptideFragmenter.Builder(EnumSet.of(FragmentationType.AX, FragmentationType.BY, FragmentationType.CZ)).enableWALosses().build()).condition(cond).tolerance(0.1).annotPrioRule("charge>>ion:y>b>>loss>>mzdiff").build();
        factory.process(prec1, getExpSpectrum(new File(filename)));
        PeakList pl = factory.getMatchedPeakList();
        System.out.println(pl);
    }

    @Test
    public void test2() throws Exception {
        Peptide.Builder builder = new Peptide.Builder("TALNAASTVFGVNPSVLEK").forceModifDoubleDisplay().doubleDisplayPrecision(4);
        Molecule TMT6plex = ChemicalFacade.getMolecule("C8C[13]4H20N1N[15]O2");
        ModificationFactory modif = ModificationFactory.valueOf(TMT6plex);
        builder.addNtModif(modif);
        builder.addModifAt(modif, 18);
        Peptide prec1 = builder.protons(3).build();
        MassCalculator massCalc = MassCalculator.getMonoAccuracyInstance();
        Assert.assertEquals(2378.368, massCalc.getMass(prec1), 0.001);
        String filename = ClassLoader.getSystemResource("spectrum.mgf").getFile();
        factory = new PSMFactory.Builder().fragmenter(new PeptideFragmenter.Builder(EnumSet.of(FragmentationType.AX, FragmentationType.BY)).build()).tolerance(0.1).build();
        factory.process(prec1, getExpSpectrum(new File(filename)));
        PeakList pl = factory.getMatchedPeakList();
        System.out.println(pl);
    }

    @Test
    public void testEasyPSM() throws ProcessException, NoMatchException {
        Peptide peptide = new Peptide.Builder("FQNALLVR").protons(2).build();
        PeakList exp = PeakListImpl.valueOf("120.07913 Da (85002.6), 147.14932 Da (20311.2)," + " 158.09187 Da (33321.5), 165.10173 Da (487886.5), " + "166.10524 Da (26983.5), 175.11842 Da (428709.2), " + "189.95845 Da (19962.7), 191.11719 Da (33407.9), " + "194.08276 Da (24121.1), 214.08656 Da (55177.2), " + "219.11382 Da (25720.4), 231.11353 Da (44814.6), " + "242.08058 Da (456825.2), 243.08276 Da (46099.4), " + "243.10849 Da (52023.8), 248.13817 Da (37212.6), " + "257.15799 Da (29071.3), 257.63184 Da (22620.0), " + "258.12396 Da (54629.7), 259.1073 Da (1155085.0), " + "260.11102 Da (100888.2), 271.10437 Da (32599.6), " + "274.1868 Da (730705.4), 275.19058 Da (43924.3), " + "276.13367 Da (2763047.8), 277.13705 Da (308786.6), " + "297.11948 Da (71307.6), 299.17166 Da (88732.9), " + "302.14063 Da (30468.6), 314.14389 Da (33883.4), " + "327.14362 Da (24628.0), 333.15485 Da (114676.3), " + "336.35452 Da (19092.7), 342.13986 Da (94690.0), " + "355.13916 Da (113234.2), 356.12619 Da (26798.7), " + "365.17932 Da (38449.9), 369.1705 Da (25214.6), 372.16702 Da (29958.8), 373.14957 Da (128456.8), 382.20663 Da (45778.1), 387.27078 Da (1535544.1), 388.27377 Da (231026.0), 389.7265 Da (23360.6), 390.17599 Da (376453.7), 391.18036 Da (38358.8), 393.177 Da (59928.7), 398.1821 Da (24210.9), 398.24411 Da (389800.4), 398.745 Da (175268.0), 407.24933 Da (158893.6), 407.74963 Da (73894.9), 410.20303 Da (124971.4), 416.19128 Da (55744.5), 426.17642 Da (136733.0), 427.14539 Da (25924.6), 430.86868 Da (29826.5), 432.51132 Da (20404.0), 433.21854 Da (117547.9), 440.93558 Da (21676.3), 443.20233 Da (89819.9), 444.18591 Da (223414.5), 446.23682 Da (93097.1), 449.26855 Da (31001.8), 454.75525 Da (48249.3), 463.56717 Da (23418.1), 481.89767 Da (26756.7), 487.42245 Da (28547.1), 494.23459 Da (42221.4), 498.23856 Da (23227.2), 500.35382 Da (2125683.8), 501.35718 Da (518910.7), 506.25989 Da (32410.7), 511.25204 Da (26383.9), 516.63458 Da (172574.8), 516.83606 Da (223809.3), 517.03638 Da (151346.2), 517.23419 Da (107181.3), 517.44122 Da (87888.2), 522.23486 Da (56563.4), 523.28186 Da (87168.2), 524.03656 Da (26623.2), 524.29144 Da (31254.9), 529.27289 Da (69528.1), 530.84143 Da (204686.4), 531.04156 Da (366504.3), 531.24347 Da (227847.4), 531.44421 Da (194599.1), 531.64386 Da (163941.4), 531.84406 Da (48133.3), 539.25781 Da (62121.0), 540.24133 Da (59405.9), 541.23999 Da (29756.9), 545.05042 Da (290978.1), 545.25171 Da (437603.8), 545.45123 Da (394016.3), 545.65289 Da (156504.5), 545.85095 Da (139181.3), 546.05273 Da (79734.6), 546.2912 Da (67474.1), 547.90674 Da (28703.7), 557.26892 Da (168583.2), 558.26941 Da (39412.3), 571.39105 Da (1795740.9), 572.39429 Da (464801.3), 574.29724 Da (265545.8), 574.46851 Da (63384.5), 574.66431 Da (160325.1), 574.86542 Da (101548.5), 575.065 Da (111463.4), 575.25122 Da (27383.3), 575.31653 Da (43043.7), 575.46631 Da (64847.6), 580.01111 Da (33138.1), 605.27753 Da (53139.4), 608.77594 Da (41656.4), 609.02667 Da (106302.9), 609.27618 Da (63360.4), 609.53113 Da (42109.7), 622.35889 Da (43424.1), 641.30505 Da (39074.3), 645.54578 Da (48495.8), 645.7876 Da (44800.7), 646.03424 Da (49216.4), 646.29346 Da (27382.0), 646.54498 Da (36727.3), 652.33466 Da (36490.6), 667.42407 Da (28905.7), 668.40784 Da (326891.6), 669.41693 Da (64805.4), 670.35437 Da (169528.0), 671.36163 Da (55419.5), 672.61786 Da (19427.2), 685.4339 Da (9683550.0), 686.43683 Da (2819368.3), 687.37921 Da (160157.3), 688.3808 Da (62002.7), 702.29657 Da (50403.3), 741.44269 Da (33953.0), 752.39642 Da (28821.6), 768.44702 Da (34977.4), 769.422 Da (262716.6), 770.42249 Da (84223.1), 779.43768 Da (97814.4), 780.43652 Da (37195.4), 786.44867 Da (156154.1), 787.45392 Da (81289.6), 796.46625 Da (828688.1), 797.4707 Da (271233.0), 813.49017 Da (122162.8), 814.49493 Da (45114.7), 824.46472 Da (233690.5), 825.45819 Da (74863.0)");
        factory = new PSMFactory.Builder().fragmenter(new PeptideFragmenter.Builder(EnumSet.of(FragmentationType.PRECURSOR, FragmentationType.IMMONIUM, FragmentationType.AX, FragmentationType.BY, FragmentationType.CZ)).enableWALosses().build()).build();
        factory.process(peptide, exp);
        System.out.println(factory.getMatchedPeakList());
    }

    @Test(expected = ProcessException.class)
    public void testBadSpectrum() throws ImageRenderingException, ProcessException, NoMatchException {
        Peptide prec1 = new Peptide.Builder("EQVQSC({57})GPPPELLNGNVK").protons(2).build();
        PeakList pl = new PeakListImpl.Builder(new double[] { 1., 2. }).build();
        factory = new PSMFactory.Builder().fragmenter(new PeptideFragmenter.Builder(EnumSet.of(FragmentationType.AX, FragmentationType.BY, FragmentationType.CZ)).build()).tolerance(0.1).build();
        factory.process(prec1, pl);
    }

    private static PeakList getExpSpectrum(File file) throws ParseException {
        MGFReader reader = MGFReader.newInstance();
        reader.enableAutoScanNum(true);
        reader.parse(file);
        AbstractExtraIterator<MSScan> it = reader.iterator();
        return it.next().getPeakList();
    }
}
