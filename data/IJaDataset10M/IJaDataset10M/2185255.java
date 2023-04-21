package tml.test;

import java.io.File;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import tml.annotators.PennTreeAnnotator;
import tml.corpus.TextDocument;
import tml.corpus.CorpusParameters.DimensionalityReduction;
import tml.utils.StanfordUtils;
import edu.stanford.nlp.trees.Tree;

/**
 * This class 
 * @author Jorge Villalon
 *
 */
public class PennTreeMetaDataIndexingTest extends TmlIndexingTest {

    private static PennTreeAnnotator annotator = null;

    private static String complicated = "(ROOT\r\n  (ADJP (RB Firstly)\r\n    (SBAR\r\n      (S\r\n        (S\r\n          (NP\r\n            (NP (DT the) (NN use))\r\n            (PP (IN of)\r\n              (NP (NNP English))))\r\n          (VP (VBP have)\r\n            (VP (VBN become)\r\n              (NP\r\n                (NP (DT the) (NN way))\r\n                (PP (IN of)\r\n                  (NP (NNP communicate))))\r\n              (PP (IN with)\r\n                (NP\r\n                  (NP (JJ other) (NNS people))\r\n                  (SBAR\r\n                    (WHNP (WDT which))\r\n                    (S\r\n                      (VP (VBP are) (RB not)\r\n                        (ADJP (JJ native)\r\n                          (PP (TO to)\r\n                            (NP (DT the) (NN language))))\r\n                        (SBAR (IN that)\r\n                          (S\r\n                            (NP (NN individual))\r\n                            (VP (VBP speak))))))))))))\r\n        (CC and)\r\n        (S\r\n          (NP\r\n            (NP (DT a) (JJ good) (NN example))\r\n            (PP (IN of)\r\n              (NP (DT this))))\r\n          (VP (VBZ is)\r\n            (NP\r\n              (NP (DT the) (NN use))\r\n              (PP (IN of)\r\n                (NP\r\n                  (NP (NNP English))\r\n                  (PP (IN in)\r\n                    (NP\r\n                      (NP\r\n                        (ADJP (JJ \"Political)\r\n                          (CC and)\r\n                          (JJ Military))\r\n                        (JJ Might,) (JJ economic) (NNS power,))\r\n                      (CC and)\r\n                      (NP (JJ religious) (NNP influence\") (NNP (Crystal,) (NNP D.) (CD (1992).)))))))))))))\r\n\r\n";

    /**
	 * @throws java.lang.Exception
	 */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TmlIndexingTest.setUpBeforeClass();
        documentsFolder = prop.getProperty("tml.tests.resourcespath") + "/corpora/txt";
        annotator = new PennTreeAnnotator();
        filesToAdd = new File[1];
        filesToAdd[0] = new File(documentsFolder + "/Diagnostic 01.txt");
        repository.addAnnotator(annotator);
        repository.addDocumentsInList(filesToAdd);
    }

    /**
	 * @throws Exception
	 */
    @Test
    public void readPennStringFromDocument() throws Exception {
        TextDocument doc = repository.getTextDocument("Diagnostic01");
        doc.getParameters().setDimensionalityReduction(DimensionalityReduction.NO);
        doc.load(repository);
    }

    /**
	 * @throws Exception
	 */
    @Test
    public void createTreeFromComplicatedPennString() throws Exception {
        Tree tree = StanfordUtils.getTreeFromString("simKey", complicated);
        assertNotNull(tree);
    }
}
