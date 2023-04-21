package tml.test;

import org.junit.BeforeClass;
import org.junit.Test;
import tml.Configuration;
import tml.corpus.SearchResultsCorpus;
import tml.corpus.CorpusParameters.DimensionalityReduction;
import tml.corpus.CorpusParameters.TermSelection;
import tml.vectorspace.TermWeighting.GlobalWeight;
import tml.vectorspace.TermWeighting.LocalWeight;

public class LanczosTest extends AbstractTmlIndexingTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        AbstractTmlIndexingTest.setUpBeforeClass();
        repository.addDocumentsInFolder(Configuration.getTmlFolder() + "/corpora/introLSA");
    }

    @Test
    public void timeBigCorpus() throws Exception {
        SearchResultsCorpus corpus = new SearchResultsCorpus("type:document");
        corpus.getParameters().setTermSelectionCriterion(TermSelection.DF);
        corpus.getParameters().setTermSelectionThreshold(2);
        corpus.getParameters().setDimensionalityReduction(DimensionalityReduction.NUM);
        corpus.getParameters().setDimensionalityReductionThreshold(2);
        corpus.getParameters().setTermWeightGlobal(GlobalWeight.None);
        corpus.getParameters().setTermWeightLocal(LocalWeight.TF);
        corpus.load(repository);
        corpus.getParameters().setLanczosSVD(true);
        corpus.getSemanticSpace().calculate();
    }
}
