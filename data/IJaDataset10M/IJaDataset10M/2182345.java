package com.gusto.engine.recommend;

import com.gusto.engine.colfil.Prediction;

/**
 * <p>Predicts the rating a user would give to an item.</p>
 * 
 * @author amokrane.belloui@gmail.com
 * 
 */
public interface PredictionService {

    /**
	 * Ask for a prediction
	 * @param userId
	 * @param itemId
	 * @param includePrediction
	 * @return
	 * @throws Exception
	 */
    public Prediction predict(long userId, long itemId, boolean includePrediction) throws Exception;
}
