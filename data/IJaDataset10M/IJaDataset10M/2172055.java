package com.uplexis.idealize.hotspots.output;

import java.util.List;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import com.uplexis.idealize.hotspots.input.bean.BaseInputBean;

/**
 * Interface for defining methods to serialize recommendations to be sent
 * accross the network.
 * 
 * @author Felipe Melo
 */
public interface RecommendationSerializer {

    /**
	 * Serializa a series of recommended item ids
	 * 
	 * @param recommendations
	 *            List<RecommendedItem> with item id and rating
	 * @param input
	 *            InputBean with input data to be used
	 * @return String with data ready to be sent throught the network.
	 */
    public String serialize(List<RecommendedItem> recommendations, BaseInputBean input);
}
