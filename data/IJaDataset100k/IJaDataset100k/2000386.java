package edu.jhu.joshua.decoder.feature_function;

import edu.jhu.joshua.decoder.feature_function.MapFFState;
import edu.jhu.joshua.decoder.feature_function.FeatureFunction;
import edu.jhu.joshua.decoder.feature_function.translation_model.Rule;
import java.util.ArrayList;

/**
 * This class provides the "Model" version of FeatureFunction from
 * before the reorganization. The final methods are to enable
 * inlining whenever it's possible.
 * 
 * @author wren ng thornton <wren@users.sourceforge.net>
 * @version $LastChangedDate: 2008-07-28 18:44:45 -0400 (Mon, 28 Jul 2008) $
 */
public abstract class DefaultFF implements FeatureFunction<MapFFState> {

    private boolean stateful = true;

    protected double weight = 0.0;

    public DefaultFF(double weight_) {
        this.weight = weight_;
    }

    public final void setStateless() {
        this.stateful = false;
    }

    public final boolean isStateful() {
        return this.stateful;
    }

    public final double getWeight() {
        return this.weight;
    }

    public void putWeight(final double weight_) {
        this.weight = weight_;
    }

    /**
	 * Generic estimator for FeatureFunctions which are Stateless
	 */
    public double estimate(final Rule rule) {
        if (this.stateful) {
            return 0.0;
        } else {
            final FFState state = this.transition(rule, null, -1, -1);
            if (null == state) {
                return 0.0;
            } else {
                return state.getTransitionCost();
            }
        }
    }

    /**
	 * Generic transition function assuming our estimator is correct
	 */
    public MapFFState transition(final Rule rule, final ArrayList<MapFFState> previous_states, final int i, final int j) {
        MapFFState state = new MapFFState();
        state.putTransitionCost(this.estimate(rule));
        return state;
    }

    public double finalTransition(final MapFFState state) {
        return 0.0;
    }
}
