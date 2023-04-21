package edu.jhu.joshua.decoder.feature_function.language_model;

import java.util.ArrayList;

/**
 * 
 * @version $LastChangedDate: 2008-07-28 18:26:17 -0400 (Mon, 28 Jul 2008) $
 */
public abstract class LMClient {

    public LMClient() {
    }

    public LMClient(String hostname, int port) {
    }

    public void close_client() {
    }

    public abstract double get_prob(ArrayList ngram, int order);

    public abstract double get_prob(int[] ngram, int order);

    public abstract double get_prob_backoff_state(int[] ngram, int n_additional_bow);

    public abstract int[] get_left_euqi_state(int[] original_state_wrds, int order, double[] cost);

    public abstract int[] get_right_euqi_state(int[] original_state, int order);
}
