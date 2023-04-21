package net.sourceforge.align.model.language;

import static net.sourceforge.align.model.Util.createWidList;
import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Test;

public class LanguageModelFactoryTest {

    @Test
    public void train() {
        int[][] widArray = new int[][] { new int[] { 1, 2, 1 }, new int[] { 1 }, new int[] { 2 }, new int[] {} };
        List<List<Integer>> widList = createWidList(widArray);
        LanguageModel model = LanguageModelFactory.getInstance().train(widList);
        assertEquals(0.6f, model.getWordProbability(1), 0.01f);
        assertEquals(0.4f, model.getWordProbability(2), 0.01f);
        assertEquals(0.0f, model.getWordProbability(0), 0.01f);
        assertEquals(0.2f, model.getSingletonWordProbability());
    }
}
