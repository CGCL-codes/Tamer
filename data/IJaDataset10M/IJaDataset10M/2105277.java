package ra.lajolla.nouveau.optimizedphipsi;

import java.io.File;
import junit.framework.TestCase;
import ra.lajolla.ngramto3dtranslators.INGramTo3DTranslator;
import ra.lajolla.ngramto3dtranslators.NGramToStringTranslatorBasedOnSingleMatchingNGramsManyResults;
import ra.lajolla.scoringfunctions.EScoringFunctionRelativeSettings;
import ra.lajolla.scoringfunctions.IScoringFunction;
import ra.lajolla.scoringfunctions.ScoreAccordingToScoringAtomDistanceOnlyIfNGramsAreSimilarFastNotIdealAndBasedOnTMSCORE;
import ra.lajolla.transformation.IFileToStringTranslator;
import ra.lajolla.transformation.IResidueToStringTransformer;
import ra.lajolla.transformation.protein.BetterOptimizedPhiPsiTranslator;
import ra.lajolla.transformation.protein.PDBProteinTranslator;
import ra.lajolla.transformation.protein.ProteinMatchRunner;
import ra.lajolla.utilities.DeleteDirRecursively;

public class ProteinAlignmentNonIdenticalTest extends TestCase {

    static INGramTo3DTranslator ngramTo3DTranslator = new NGramToStringTranslatorBasedOnSingleMatchingNGramsManyResults();

    static IScoringFunction scoringFunction = new ScoreAccordingToScoringAtomDistanceOnlyIfNGramsAreSimilarFastNotIdealAndBasedOnTMSCORE(EScoringFunctionRelativeSettings.basedOnSizeOfQueryWhatIsTheTargetInTMSCORE);

    /**
	 * 
	 * 
	 * too exact => should yield NO results...!
	 * 
	 * 
	 */
    public void testIdenticalMatchAndSearchAndAlignment() {
        String tempDir = "src/test/tmp";
        int ngramSize = 20;
        String pathToTargetDirOrFile = "src/test/resources/thymidylate_difficult/pdb1axw.ent";
        String pathToQueryDirOrFile = "src/test/resources/thymidylate_difficult/pdb1j3j.ent";
        String outputFilePath = tempDir + File.separator + this.getClass().getSimpleName() + File.separator;
        boolean dealOnlyWithFirstModel = true;
        IResidueToStringTransformer iResidueToStringTransformer = new BetterOptimizedPhiPsiTranslator();
        IFileToStringTranslator iFileToStringTranslator = new PDBProteinTranslator(iResidueToStringTransformer, dealOnlyWithFirstModel, scoringFunction, ngramTo3DTranslator);
        double thresholdOfRefinementScoreUnderWichResultIsOmitted = 0.99999;
        DeleteDirRecursively.deleteDir(new File(tempDir));
        ProteinMatchRunner.executeSearch(ngramSize, iFileToStringTranslator, iResidueToStringTransformer, pathToTargetDirOrFile, pathToQueryDirOrFile, outputFilePath, dealOnlyWithFirstModel, thresholdOfRefinementScoreUnderWichResultIsOmitted, 1);
        File chainADir = new File(outputFilePath + File.separator + "pdb1j3j.ent-model-0-chain-A/");
        String[] allFiles = chainADir.list();
        if (allFiles.length > 0) {
            fail();
        }
        chainADir = new File(outputFilePath + File.separator + "pdb1j3j.ent-model-0-chain-B/");
        allFiles = chainADir.list();
        if (allFiles.length > 0) {
            fail();
        }
        chainADir = new File(outputFilePath + File.separator + "pdb1j3j.ent-model-0-chain-C/");
        allFiles = chainADir.list();
        if (allFiles.length > 0) {
            fail();
        }
        chainADir = new File(outputFilePath + File.separator + "pdb1j3j.ent-model-0-chain-D/");
        allFiles = chainADir.list();
        if (allFiles.length > 0) {
            fail();
        }
        DeleteDirRecursively.deleteDir(new File(tempDir));
    }

    /**
	 * 
	 * 
	 * too exact => should yield results => RMSD is better!
	 * 
	 * 
	 */
    public void testAlignmentsOfDifferentThimidylateSynthases() {
        String tempDir = "src/test/tmp";
        int ngramSize = 20;
        int angleDiscretion = 90;
        String pathToTargetDirOrFile = "src/test/resources/thymidylate_difficult/pdb1axw.ent";
        String pathToQueryDirOrFile = "src/test/resources/thymidylate_difficult/pdb1j3j.ent";
        String outputFilePath = tempDir + File.separator + this.getClass().getSimpleName() + File.separator;
        boolean dealOnlyWithFirstModel = true;
        IResidueToStringTransformer iResidueToStringTransformer = new BetterOptimizedPhiPsiTranslator();
        IFileToStringTranslator iFileToStringTranslator = new PDBProteinTranslator(iResidueToStringTransformer, dealOnlyWithFirstModel, scoringFunction, ngramTo3DTranslator);
        double thresholdOfRefinementScoreUnderWichResultIsOmitted = 0.7;
        DeleteDirRecursively.deleteDir(new File(tempDir));
        ProteinMatchRunner.executeSearch(ngramSize, iFileToStringTranslator, iResidueToStringTransformer, pathToTargetDirOrFile, pathToQueryDirOrFile, outputFilePath, dealOnlyWithFirstModel, thresholdOfRefinementScoreUnderWichResultIsOmitted, 1);
        File chainADir = new File(outputFilePath + File.separator + "pdb1j3j.ent-model-0-chain-A/");
        String[] allFiles = chainADir.list();
        if (allFiles.length > 0) {
            fail();
        }
        chainADir = new File(outputFilePath + File.separator + "pdb1j3j.ent-model-0-chain-B/");
        allFiles = chainADir.list();
        if (allFiles.length > 0) {
            fail();
        }
        chainADir = new File(outputFilePath + File.separator + "pdb1j3j.ent-model-0-chain-C/");
        allFiles = chainADir.list();
        if (allFiles.length != 3) {
            fail();
        }
        chainADir = new File(outputFilePath + File.separator + "pdb1j3j.ent-model-0-chain-D/");
        allFiles = chainADir.list();
        if (allFiles.length != 3) {
            fail();
        }
    }
}
