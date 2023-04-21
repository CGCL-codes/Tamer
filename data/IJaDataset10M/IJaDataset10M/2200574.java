package com.gusto.engine.recommend.prediction;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.gusto.engine.colfil.Distance;
import com.gusto.engine.colfil.Rating;
import com.gusto.engine.recommend.PredictionService;
import com.gusto.engine.recommend.prediction.base.BaseHybridImpl;

/**
 * <p>Weighting algorithm, 
 * executes both collaborative and semantic and weights the predictions.</p>
 * 
 * @author amokrane.belloui@gmail.com
 *
 */
public class HybridWeighting extends BaseHybridImpl implements PredictionService {

    private Logger log = Logger.getLogger(getClass());

    protected Double doPrediction(long userId, long itemId, List<Distance> userNeighborhoodCol, List<Distance> itemNeighborhoodCol, List<Distance> userNeighborhoodSem, List<Distance> itemNeighborhoodSem) {
        Map<Long, Double> usersWeights = this.buildUserWeights(userId, userNeighborhoodCol);
        Map<Long, Double> itemsWeights = this.buildItemWeights(itemId, itemNeighborhoodCol);
        List<Rating> evals = collaborativeService.getSubMatrix(usersWeights.keySet(), itemsWeights.keySet());
        Map<Long, Double> usersWeightsSem = this.buildUserWeights(userId, userNeighborhoodSem);
        Map<Long, Double> itemsWeightsSem = this.buildItemWeights(itemId, itemNeighborhoodSem);
        List<Rating> evalsSem = collaborativeService.getSubMatrix(usersWeightsSem.keySet(), itemsWeightsSem.keySet());
        Double val = null;
        Double valCollaborative = null;
        Double valSemantic = null;
        double user_mean = collaborativeService.getUserMeanRating(userId);
        double item_mean = collaborativeService.getItemMeanRating(itemId);
        if (evals.size() >= params.getColMinEvals()) {
            valCollaborative = this.buildPrediction(evals, user_mean, item_mean, usersWeights, itemsWeights);
        }
        if (evalsSem.size() >= params.getSemMinEvals()) {
            valSemantic = this.buildPrediction(evalsSem, user_mean, item_mean, usersWeightsSem, itemsWeightsSem);
        }
        if ((valCollaborative != null) && (valSemantic != null)) {
            val = (valCollaborative + valSemantic) / 2;
            log.debug("Weighting between Col and Sem " + val);
            nbHybrid++;
        } else {
            if (valCollaborative != null) {
                val = valCollaborative;
                log.debug("Weighting (Col only) " + val);
                nbCollaborative++;
            } else if (valSemantic != null) {
                val = valSemantic;
                log.debug("Weighting (Sem only) " + val);
                nbSemantic++;
            }
        }
        return val;
    }
}
