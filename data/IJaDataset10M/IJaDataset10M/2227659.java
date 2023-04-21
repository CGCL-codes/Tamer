package edu.kit.aifb.concept.builder.category;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.kit.aifb.TestContextManager;
import edu.kit.aifb.concept.IConceptIterator;
import edu.kit.aifb.concept.IConceptVector;
import edu.kit.aifb.terrier.concept.TerrierESAIndex;
import edu.kit.aifb.wikipedia.mlc.MLCDatabase;
import edu.kit.aifb.wikipedia.mlc.MLCFactory;
import edu.kit.aifb.wikipedia.mlc.MLCategory;
import edu.kit.aifb.wikipedia.mlc.MLCategoryTree;

public class TopDownCatTreeVectorBuilderTest {

    static MLCFactory mlcFactory;

    static TerrierESAIndex catIndex;

    static MLCategoryTree tree;

    @BeforeClass
    public static void initialize() throws SQLException {
        mlcFactory = (MLCFactory) TestContextManager.getContext().getBean(MLCFactory.class);
        catIndex = (TerrierESAIndex) TestContextManager.getContext().getBean("wp200909_mlc_categories_concept_index_en");
        tree = new MLCategoryTree(mlcFactory.getRootCat(), catIndex);
    }

    double[] scores;

    MLCategory rootCat;

    Collection<MLCategory> subCats;

    public int getDocId(MLCategory cat) {
        return catIndex.getConceptId(MLCDatabase.getConceptName(cat.getId()));
    }

    @Before
    public void buildScores() throws SQLException {
        scores = new double[catIndex.size()];
        rootCat = mlcFactory.getRootCat();
        subCats = rootCat.getSubCategories();
        scores[getDocId(rootCat)] = 1;
        Iterator<MLCategory> it = subCats.iterator();
        scores[getDocId(it.next())] = 2.0;
        scores[getDocId(it.next())] = .5;
    }

    MaxScoreTopDownCatTreeVectorBuilder maxScoreBuilder;

    MeanScoreTopDownCatTreeVectorBuilder meanScoreBuilder;

    @Before
    public void initializeBuilders() {
        maxScoreBuilder = new MaxScoreTopDownCatTreeVectorBuilder();
        maxScoreBuilder.setApplicationContext(TestContextManager.getContext());
        maxScoreBuilder.setSize(10);
        maxScoreBuilder.tree = tree;
        meanScoreBuilder = new MeanScoreTopDownCatTreeVectorBuilder();
        meanScoreBuilder.setApplicationContext(TestContextManager.getContext());
        meanScoreBuilder.setSize(10);
        meanScoreBuilder.tree = tree;
    }

    @Test
    public void maxScorePropagateScores() {
        maxScoreBuilder.propagateScores(scores);
        Assert.assertEquals(2.0, scores[getDocId(rootCat)], .01);
        Iterator<MLCategory> it = subCats.iterator();
        Assert.assertEquals(2.0, scores[getDocId(it.next())], .01);
        Assert.assertEquals(.5, scores[getDocId(it.next())], .01);
    }

    @Test
    public void maxScoreConceptVector() {
        maxScoreBuilder.reset("test", scores.length);
        maxScoreBuilder.scores = scores;
        IConceptVector cv = maxScoreBuilder.getConceptVector();
        Assert.assertEquals(2.0, cv.get(getDocId(rootCat)), .01);
        Iterator<MLCategory> it = subCats.iterator();
        Assert.assertEquals(2.0, cv.get(getDocId(it.next())), .01);
        Assert.assertEquals(.5, cv.get(getDocId(it.next())), .01);
    }

    @Test
    public void meanScorePropagateScores() {
        meanScoreBuilder.propagateScores(scores);
        Assert.assertEquals((1 + 2 + .5) / 3, scores[getDocId(rootCat)], .01);
        Iterator<MLCategory> it = subCats.iterator();
        Assert.assertEquals(2.0, scores[getDocId(it.next())], .01);
        Assert.assertEquals(.5, scores[getDocId(it.next())], .01);
    }

    @Test
    public void meanScoreConceptVector() {
        meanScoreBuilder.reset("test", scores.length);
        meanScoreBuilder.scores = scores;
        IConceptVector cv = meanScoreBuilder.getConceptVector();
        Iterator<MLCategory> it = subCats.iterator();
        IConceptIterator cvIt = cv.orderedIterator();
        Assert.assertTrue(cvIt.next());
        Assert.assertEquals(2.0, cvIt.getValue(), .01);
        Assert.assertEquals(getDocId(it.next()), cvIt.getId());
        Assert.assertTrue(cvIt.next());
        Assert.assertEquals((1 + 2 + .5) / 3, cvIt.getValue(), .01);
        Assert.assertEquals(getDocId(rootCat), cvIt.getId());
        Assert.assertTrue(cvIt.next());
        Assert.assertEquals(.5, cvIt.getValue(), .01);
        Assert.assertEquals(getDocId(it.next()), cvIt.getId());
        Assert.assertFalse(cvIt.next());
    }
}
