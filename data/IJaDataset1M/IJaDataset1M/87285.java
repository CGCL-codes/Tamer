package net.sourceforge.align.model.translation;

import static net.sourceforge.align.model.Util.createWidList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Test;

public class TranslationModelFactoryTest {

    @Test
    public void train() {
        int[][] sourceWidArray = { new int[] {}, new int[] { 2 }, new int[] { 1, 2, 3 } };
        int[][] targetWidArray = { new int[] { 3 }, new int[] { 2 }, new int[] { 1, 2 } };
        List<List<Integer>> sourceWidList = createWidList(sourceWidArray);
        List<List<Integer>> targetWidList = createWidList(targetWidArray);
        TranslationModel model = TranslationModelFactory.getInstance().train(1, sourceWidList, targetWidList);
        SourceData sourceData;
        List<TargetData> targetDataList;
        TargetData targetData;
        sourceData = model.get(0);
        targetDataList = sourceData.getTranslationList();
        assertEquals(3, targetDataList.size());
        targetData = targetDataList.get(0);
        assertEquals(3, targetData.getWid());
        assertEquals(0.5, targetData.getProbability(), 0.01);
        targetData = targetDataList.get(1);
        assertEquals(2, targetData.getWid());
        assertEquals(0.375, targetData.getProbability(), 0.01);
        targetData = targetDataList.get(2);
        assertEquals(1, targetData.getWid());
        assertEquals(0.125, targetData.getProbability(), 0.01);
        sourceData = model.get(1);
        targetDataList = sourceData.getTranslationList();
        assertEquals(2, targetDataList.size());
        targetData = targetDataList.get(0);
        assertEquals(1, targetData.getWid());
        assertEquals(0.5, targetData.getProbability(), 0.01);
        targetData = targetDataList.get(1);
        assertEquals(2, targetData.getWid());
        assertEquals(0.5, targetData.getProbability(), 0.01);
        sourceData = model.get(2);
        targetDataList = sourceData.getTranslationList();
        assertEquals(2, targetDataList.size());
        targetData = targetDataList.get(0);
        assertEquals(2, targetData.getWid());
        assertEquals(0.75, targetData.getProbability(), 0.01);
        targetData = targetDataList.get(1);
        assertEquals(1, targetData.getWid());
        assertEquals(0.25, targetData.getProbability(), 0.01);
        sourceData = model.get(3);
        targetDataList = sourceData.getTranslationList();
        assertEquals(2, targetDataList.size());
        targetData = targetDataList.get(0);
        assertEquals(1, targetData.getWid());
        assertEquals(0.5, targetData.getProbability(), 0.01);
        targetData = targetDataList.get(1);
        assertEquals(2, targetData.getWid());
        assertEquals(0.5, targetData.getProbability(), 0.01);
        for (int i = 2; i < 4; ++i) {
            TranslationModel oldModel = model;
            model = TranslationModelFactory.getInstance().train(i, sourceWidList, targetWidList);
            assertTrue("Iteration " + i, model.get(0).getTranslationProbability(3) > oldModel.get(0).getTranslationProbability(3));
            double delta0To2 = oldModel.get(0).getTranslationProbability(2) - model.get(0).getTranslationProbability(2);
            double delta0To1 = oldModel.get(0).getTranslationProbability(1) - model.get(0).getTranslationProbability(1);
            assertTrue("Iteration " + i, delta0To2 < delta0To1);
            assertTrue("Iteration " + i, model.get(2).getTranslationProbability(2) > oldModel.get(2).getTranslationProbability(2));
        }
    }
}
