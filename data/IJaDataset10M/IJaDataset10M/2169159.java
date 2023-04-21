package de.laures.cewolf.example;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import org.jfree.data.category.DefaultCategoryDataset;

public class PageViewCountData2 implements DatasetProducer, Serializable {

    private final String categories[] = { "mon", "tue", "wen", "thu", "fri", "sat", "sun" };

    private final String seriesNames[] = { "cewolfset.jsp", "tutorial.jsp", "testpage.jsp", "performancetest.jsp" };

    public Object produceDataset(Map params) throws DatasetProduceException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int series = 0; series < seriesNames.length; series++) {
            int lastY = (int) (Math.random() * 1000D + 1000D);
            for (int i = 0; i < categories.length; i++) {
                int y = lastY + (int) (Math.random() * 200D - 100D);
                lastY = y;
                dataset.addValue(y, seriesNames[series], categories[i]);
            }
        }
        return dataset;
    }

    public boolean hasExpired(Map params, Date since) {
        return false;
    }

    public String getProducerId() {
        return "PageViewCountData DatasetProducer";
    }
}
