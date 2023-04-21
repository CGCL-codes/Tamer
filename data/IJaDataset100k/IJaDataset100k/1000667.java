package hats;

import java.io.BufferedReader;
import java.util.ArrayList;
import genomeEnums.Chrom;
import genomeEnums.Genotype;
import genomeEnums.Nuc;
import genomeUtils.GenomeConstants;
import genomeUtils.InfoOneSiteOneSample;
import genomeUtils.NucCounter;
import genomeUtils.PileupUtils;
import nutils.ArrayUtils;
import nutils.BitSetUtils;
import nutils.CompareUtils;
import nutils.IOUtils;
import nutils.PrimitiveWrapper;

public class PileUpParse {

    public static final int NumInitialFixedColumns = 3;

    public static final int NumColumnsPerSample = 14;

    public static final int NumColumnsPerSampleTissue = 3;

    public static final int NumColumnsPerSampleGenotypeInfo = 6;

    public static final int NumColumnsPerSampleGenotypeTissue = 3;

    public static final int NumColumnsPerSampleAmpInfo = 2;

    private static boolean isHeaderLine(String firstColumn) {
        return (Character.toUpperCase(firstColumn.trim().charAt(0)) == 'C');
    }

    public static PileUpRegion parsePileUp(String inFilename) {
        int numLines = IOUtils.countNumberLinesInFile(inFilename);
        PileUpRegion pur = new PileUpRegion(numLines);
        ArrayList<InfoOneSiteOneSample> iososList = null;
        NucCounter nc = new NucCounter();
        BufferedReader in = IOUtils.getBufferedReader(inFilename);
        Nuc[] genoTumor = new Nuc[2];
        Nuc[] genoNormal = new Nuc[2];
        System.out.println("Parsing File: " + inFilename);
        String line;
        int lineCounter = -1;
        PrimitiveWrapper.WIntegerSetOnce numSamples = new PrimitiveWrapper.WIntegerSetOnce(-1);
        while ((line = IOUtils.getNextLineInBufferedReader(in)) != null) {
            String[] comps = line.split("\\t");
            ++lineCounter;
            if ((lineCounter == 0) && isHeaderLine(comps[0])) {
                continue;
            }
            Chrom chrom = Chrom.getChrom(Byte.parseByte(comps[0]));
            int position = Integer.parseInt(comps[1]);
            Nuc referenceAllele = Nuc.getNuc(comps[2].charAt(0));
            int numSamplesForRow = (comps.length - NumInitialFixedColumns) / NumColumnsPerSample;
            if (!numSamples.set(numSamplesForRow)) {
                CompareUtils.ensureTrue(false, "ERROR: Input file must keep constant number of samples per row!  Row responsible: " + (lineCounter + 1));
            }
            if (iososList == null) {
                iososList = InfoOneSiteOneSample.createList(numSamples.mInt);
                System.out.println("Num Test Samples: " + numSamples.mInt);
            }
            for (int s = 0; s < numSamples.mInt; s++) {
                InfoOneSiteOneSample iosos = iososList.get(s);
                iosos.clear();
                int genotypeIndexTumor = (NumColumnsPerSample * s) + NumInitialFixedColumns;
                iosos.mTumor.mGenotypeCode = Genotype.getGenotype(Integer.parseInt(comps[genotypeIndexTumor]));
                genoTumor[0] = Nuc.getNuc(comps[genotypeIndexTumor + 1].charAt(0));
                genoTumor[1] = Nuc.getNuc(comps[genotypeIndexTumor + 2].charAt(0));
                sortNucArray(genoTumor, referenceAllele);
                int genotypeIndexNormal = genotypeIndexTumor + NumColumnsPerSampleGenotypeTissue;
                iosos.mNormal.mGenotypeCode = Genotype.getGenotype(Integer.parseInt(comps[genotypeIndexNormal]));
                genoNormal[0] = Nuc.getNuc(comps[genotypeIndexNormal + 1].charAt(0));
                genoNormal[1] = Nuc.getNuc(comps[genotypeIndexNormal + 2].charAt(0));
                sortNucArray(genoNormal, referenceAllele);
                int ampIndexTumor = genotypeIndexNormal + NumColumnsPerSampleGenotypeTissue;
                iosos.mTumor.mIsAmplified = BitSetUtils.intToBoolean(Integer.parseInt(comps[ampIndexTumor]));
                iosos.mNormal.mIsAmplified = BitSetUtils.intToBoolean(Integer.parseInt(comps[ampIndexTumor + 1]));
                int sampleIndexTumor = ampIndexTumor + NumColumnsPerSampleAmpInfo;
                iosos.mTumor.mNumReadsTotal = Integer.parseInt(comps[sampleIndexTumor]);
                parseReadString(comps[sampleIndexTumor + 1], iosos.mTumor, nc, position, referenceAllele, genoTumor);
                int sampleIndexNormal = sampleIndexTumor + NumColumnsPerSampleTissue;
                iosos.mNormal.mNumReadsTotal = Integer.parseInt(comps[sampleIndexNormal]);
                parseReadString(comps[sampleIndexNormal + 1], iosos.mNormal, nc, position, referenceAllele, genoNormal);
            }
            pur.registerLocus(chrom, position, referenceAllele, iososList);
        }
        IOUtils.closeBufferedReader(in);
        System.out.println("Calculating Pile Up Region Statistics...");
        pur.mSampleStats = generateCoverageStatistics(pur, null);
        return pur;
    }

    /** Given a string of reads, this parses those reads to tally counts of the reference and variant allele. */
    private static void parseReadString(String readStr, InfoOneSiteOneSample.InfoOneSiteSampleTissue iosst, NucCounter nc, int position, Nuc referenceAllele, Nuc[] geno) {
        int length = readStr.length();
        iosst.mAlleleB.clear();
        nc.clear();
        for (int i = 0; i < length; i++) {
            char ch = readStr.charAt(i);
            if ((ch == '.') || (ch == ',')) {
                nc.incrementCount(referenceAllele);
            } else if (ch == 'N') {
            } else if (Nuc.isValid(ch)) {
                nc.incrementCount(Nuc.getNuc(ch));
            } else if (ch == '$') {
            } else if (ch == '^') {
                i++;
            } else if (PileupUtils.isIndelFirstChar(ch)) {
                i = PileupUtils.getIndexAfterIndelString(readStr, i) - 1;
            }
        }
        iosst.mAlleleA.set(referenceAllele, (short) nc.getCount(referenceAllele));
        switch(iosst.mGenotypeCode) {
            case EnumHomozygous00:
                CompareUtils.ensureTrue(geno[0] == geno[1], "ERROR: PileUpParse.parseReadString(): Hom00 genotype inconsistency!");
                iosst.mAlleleB.clear();
                break;
            case EnumHeterozygous:
                CompareUtils.ensureTrue(geno[0] != geno[1], "ERROR: PileUpParse.parseReadString(): Het genotype inconsistency!");
                sortNucArray(geno, referenceAllele);
                CompareUtils.ensureTrue(geno[0] == referenceAllele, "ERROR: PileUpParse.parseReadString(): Reference not first in het genotype!");
                iosst.mAlleleB.set(geno[1], (short) nc.getCount(geno[1]));
                break;
            case EnumHomozygous11:
                CompareUtils.ensureTrue(geno[0] == geno[1], "ERROR: PileUpParse.parseReadString(): Hom11 genotype inconsistency!");
                iosst.mAlleleA.clear();
                iosst.mAlleleB.set(geno[1], (short) nc.getCount(geno[1]));
                break;
            case EnumInvalidGenotype:
                iosst.mAlleleA.clear();
                iosst.mAlleleB.clear();
                break;
            default:
                CompareUtils.ensureTrue(false, "ERROR: PileUpParse.parseReadString(): Invalid Genotype!");
        }
        if (!iosst.readsValidate()) {
        }
        iosst.mNumReadsTotal = iosst.calcNumReadsTotal();
    }

    public static ArrayList<PileUpOneSample> generateCoverageStatistics(PileUpRegion pur, ArrayList<PileUpOneSample> sampleStatsList) {
        InfoOneSiteOneSample iosos = new InfoOneSiteOneSample();
        sampleStatsList = (sampleStatsList == null) ? new ArrayList<PileUpOneSample>(pur.getNumSamples()) : sampleStatsList;
        Chrom chrom = pur.getChrom();
        for (int sampleIndex = 0; sampleIndex < pur.getNumSamples(); sampleIndex++) {
            PileUpOneSample puos = new PileUpOneSample();
            sampleStatsList.add(puos);
            PileUpRegionOneSample puros = pur.mRegionOverSamples.get(sampleIndex);
            CNRegion cnRegion = null;
            boolean wasPrevSiteEffectiveTumor = false;
            int numSites = pur.getNumSites();
            for (int siteIndex = 0; siteIndex < numSites; siteIndex++) {
                Nuc referenceAllele = pur.getReferenceAllele(siteIndex);
                puros.retrieveInformationOneLocus(siteIndex, iosos, referenceAllele);
                boolean tumorEffectiveSiteAmp = isEffectiveSite(iosos.mTumor.mGenotypeCode, iosos.mTumor.calcNumReadsTotal(), iosos.mTumor.mIsAmplified, Boolean.TRUE);
                boolean normalEffectiveSite = isEffectiveSite(iosos.mNormal.mGenotypeCode, iosos.mNormal.calcNumReadsTotal(), iosos.mNormal.mIsAmplified, Boolean.FALSE);
                if (normalEffectiveSite) {
                    puos.mAggregateCopyNeutral.mNumReadsNormal += iosos.mNormal.calcNumReadsTotal();
                    puos.mAggregateCopyNeutral.mNumEffectiveSitesNormal++;
                }
                if (tumorEffectiveSiteAmp) {
                    if (!wasPrevSiteEffectiveTumor) {
                        cnRegion = new CNRegion(chrom, siteIndex);
                        puos.getRegionsAmp().add(cnRegion);
                        wasPrevSiteEffectiveTumor = true;
                    } else {
                        boolean extendResult = cnRegion.extendRange(chrom, siteIndex);
                        CompareUtils.ensureTrue(extendResult, "ERROR: PileUpParse.generateCoverageStatistics(): Site Index cannot be decreasing!" + cnRegion.toString() + "\t" + siteIndex);
                    }
                    cnRegion.mNumReadsTumor += iosos.mTumor.calcNumReadsTotal();
                    cnRegion.mNumEffectiveSitesTumor++;
                    if (normalEffectiveSite) {
                        cnRegion.mNumReadsNormal += iosos.mNormal.calcNumReadsTotal();
                        cnRegion.mNumEffectiveSitesNormal++;
                    }
                } else {
                    cnRegion = null;
                    wasPrevSiteEffectiveTumor = false;
                    boolean tumorEffectiveSiteNonAmp = isEffectiveSite(iosos.mTumor.mGenotypeCode, iosos.mTumor.calcNumReadsTotal(), iosos.mTumor.mIsAmplified, Boolean.FALSE);
                    if (tumorEffectiveSiteNonAmp) {
                        puos.mAggregateCopyNeutral.mNumReadsTumor += iosos.mTumor.calcNumReadsTotal();
                        puos.mAggregateCopyNeutral.mNumEffectiveSitesTumor++;
                    }
                }
            }
            puos.mAggregateCopyNeutral.calcCoverages();
            System.out.println("-----------------------------------------");
            System.out.println("------- General Pileup Statistics -------");
            System.out.println("Sample:\t" + sampleIndex + "\tAggregateCopyNeutral\t" + "\tAvgCovgTumor:\t" + puos.mAggregateCopyNeutral.getCoverageTumor() + "\tAvgCovgTumorDiploid:\t" + puos.mAggregateCopyNeutral.getCoverageTumorDiploid(GenomeConstants.DefaultDiploidCopyNumber) + "\tAvgCovgNormal:\t" + puos.mAggregateCopyNeutral.getCoverageNormal());
            int regionCounter = 0;
            for (CNRegion theCNRegion : puos.getRegionsAmp()) {
                theCNRegion.calcCoverages();
                System.out.println("Sample:\t" + sampleIndex + "\tCNA_Region:\t" + (regionCounter++) + "\tPositionStart:\t" + pur.getPosition(theCNRegion.getRangeStart()) + "\tPositionEnd:\t" + pur.getPosition(theCNRegion.getRangeEnd()) + "\tRegionLength:\t" + (pur.getPosition(theCNRegion.getRangeEnd()) - pur.getPosition(theCNRegion.getRangeStart()) + 1) + "\tNumSites:\t" + theCNRegion.getRangeLength() + "\tCopyNumberTumorAmplicon:\t" + theCNRegion.calcCopyNumberTumor() + "\tAvgCovgTumor:\t" + theCNRegion.getCoverageTumor() + "\tAvgCovgTumorDiploid:\t" + theCNRegion.getCoverageTumorDiploid(theCNRegion.calcCopyNumberTumor()) + "\tAvgCovgNormal:\t" + theCNRegion.getCoverageNormal());
            }
            System.out.println("-----------------------------------------");
        }
        return sampleStatsList;
    }

    /** Conveys whether the site has a valid full genotype, has a positive read count, and has a desired amplification status. 
	 * @param shouldBeAmplified Set to TRUE, FALSE, or null (if amplification status doesn't matter). */
    public static boolean isEffectiveSite(Genotype g, int readCount, boolean isAmplified, Boolean shouldBeAmplified) {
        boolean amplifiedPass = (shouldBeAmplified == null) ? true : (isAmplified == shouldBeAmplified.booleanValue());
        return (amplifiedPass && g.isFullGenotype() && (readCount > 0));
    }

    /** This examines the reads in the alleles in the info package and assigns the correct
	 *  genotype if the read count for a particular allele is too small to not be an error.
	 */
    private static void callGenotype(InfoOneSiteOneSample.InfoOneSiteSampleTissue iosst) {
        if (iosst.mAlleleA.hasNoReads() && iosst.mAlleleB.hasNoReads()) {
            CompareUtils.ensureTrue(false, "ERROR: PileUpParse.callGenotype(): Must contain reads for at least one allele!");
        } else if (iosst.mAlleleA.hasNoReads()) {
            iosst.mGenotypeCode = Genotype.EnumHomozygous11;
        } else if (iosst.mAlleleB.hasNoReads()) {
            iosst.mGenotypeCode = Genotype.EnumHomozygous00;
        } else {
            iosst.mGenotypeCode = Genotype.EnumHeterozygous;
        }
        int minThresh = 2;
        int totalReads = iosst.mAlleleA.mNumReads + iosst.mAlleleB.mNumReads;
        if (totalReads > 7) {
            if (iosst.mAlleleA.mNumReads < minThresh) {
                iosst.mAlleleA.clear();
                iosst.mGenotypeCode = Genotype.EnumHomozygous11;
            }
            if (iosst.mAlleleB.mNumReads < minThresh) {
                iosst.mAlleleB.clear();
                iosst.mGenotypeCode = Genotype.EnumHomozygous00;
            }
        }
    }

    private static void TestParseReadString() {
        String readStr = ".$.tttt.t...tt.t.tt$tt.t..tttttt.ttt.+1A...t.t.+2AA.tt,ttt+1att+1a,.t.tttt.tt,t,ttct..,";
        InfoOneSiteOneSample.InfoOneSiteSampleTissue iosst = new InfoOneSiteOneSample.InfoOneSiteSampleTissue();
        iosst.mNumReadsTotal = 72;
        NucCounter nc = new NucCounter();
        int indexAtIndel = readStr.indexOf("+");
        int indexAfterIndel = PileupUtils.getIndexAfterIndelString(readStr, indexAtIndel);
        System.out.println("Index at indel:    " + indexAtIndel);
        System.out.println("Index after indel: " + indexAfterIndel);
        iosst.mGenotypeCode = Genotype.EnumHeterozygous;
        parseReadString(readStr, iosst, nc, 2243188, Nuc.T, new Nuc[] { Nuc.T, Nuc.A });
        System.out.println(iosst.mAlleleA.mAllele + "\t" + iosst.mAlleleB.mAllele + "\t" + iosst.mAlleleA.mNumReads + "\t" + iosst.mAlleleB.mNumReads);
    }

    /** Orders the Nuc[2] array such that the reference allele is first, if applicable. */
    public static void sortNucArray(Nuc[] nucs, Nuc alleleRef) {
        if ((nucs[0] != nucs[1]) && (nucs[1] == alleleRef)) {
            ArrayUtils.reverseArray(nucs);
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        parsePileUp(args[0]);
    }
}
