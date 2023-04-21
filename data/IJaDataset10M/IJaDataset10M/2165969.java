package cb.recommender.cache.controller;

import java.util.LinkedList;
import java.util.List;
import org.apache.mahout.cf.taste.impl.recommender.GenericRecommendedItem;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import cb.recommender.base.input.bean.CBInputBean;
import cb.recommender.cache.storable.CBStorableRecommendationsIdValue;
import com.uplexis.idealize.base.cache.Cache;
import com.uplexis.idealize.base.loggers.IdealizeLogger;
import com.uplexis.idealize.hotspots.controller.recommender.Controller;
import com.uplexis.idealize.hotspots.input.bean.BaseInputBean;

/**
 * Create a controller for dealing with CB recommendations requests, storage
 * item id and value similarity.
 * 
 * @author Alex Amorim Dutra
 */
public final class CBCacheControllerIdValue extends Controller {

    private final String canonicalName = this.getClass().getCanonicalName();

    private IdealizeLogger logger = IdealizeLogger.getInstance();

    @Override
    public List<RecommendedItem> getRecommendations(BaseInputBean input, int howMany) {
        long itemId = ((CBInputBean) input).getItemId();
        if (itemId < 0) {
            this.logger.logFatal(this.canonicalName + ".getRecommendations: could not get recommendations: invalid item id: " + itemId);
            return null;
        }
        if (howMany <= 0) {
            this.logger.logFatal(this.canonicalName + ".getRecommendations: could not get recommendations: invalid amount or requested recommendations: " + howMany);
            return null;
        }
        if (Cache.getInstance().getData() != null) {
            GenericRecommendedItem[] recArray = ((CBStorableRecommendationsIdValue) Cache.getInstance().getData()).getRecommendations(new Long(itemId), howMany);
            if (recArray.length == 0) {
                batchProcessor.notifyCacheChange(input);
                recArray = ((CBStorableRecommendationsIdValue) Cache.getInstance().getData()).getRecommendations(new Long(itemId), howMany);
            }
            List<RecommendedItem> recommendations = new LinkedList<RecommendedItem>();
            int length = recArray.length;
            for (int i = 0; i < length; i++) recommendations.add(recArray[i]);
            return recommendations;
        }
        return null;
    }

    @Override
    public boolean update(BaseInputBean feedback) {
        return false;
    }
}
