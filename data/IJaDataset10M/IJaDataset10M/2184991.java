package org.decisiondeck.xmcda_oo.persist;

import java.util.Map;
import java.util.Set;
import org.decisiondeck.xmcda_oo.data.SituationBasedData;
import org.decisiondeck.xmcda_oo.structure.Alternative;
import org.decisiondeck.xmcda_oo.structure.Alternatives;
import org.decisiondeck.xmcda_oo.structure.Criteria;
import org.decisiondeck.xmcda_oo.structure.Criterion;
import org.decisiondeck.xmcda_oo.structure.EvaluationMatrix;
import org.decisiondeck.xmcda_oo.structure.IndifferenceThreshold;
import org.decisiondeck.xmcda_oo.structure.PreferenceThreshold;
import org.decisiondeck.xmcda_oo.structure.Weight;
import org.decisiondeck.xmcda_oo.structure.Weights;
import org.decisiondeck.xmcda_oo.structure.Criterion.PreferenceDirection;

public class SimpleSituationData extends SituationBasedData {

    @Override
    protected void populateAlternatives(final Set<Alternative> alts) {
        alts.add(new Alternative("a01", "name 1"));
        alts.add(new Alternative("a02", null));
    }

    @Override
    protected void populateCriteria(final Set<Criterion> crits) {
        crits.add(new Criterion("c01", "name 1", PreferenceDirection.MINIMIZE));
        crits.add(new Criterion("c02", null, PreferenceDirection.UNKNOWN));
    }

    @Override
    protected void populateEvaluations(final EvaluationMatrix evals) {
        final Alternatives alternatives = retrieveAlternatives();
        final Criteria criteria = retrieveCriteria();
        evals.put(alternatives.get("a01"), criteria.get("c01"), 22080f);
        evals.put(alternatives.get("a01"), criteria.get("c02"), 105f);
        evals.put(alternatives.get("a02"), criteria.get("c01"), 28100f);
        evals.put(alternatives.get("a02"), criteria.get("c02"), 160f);
    }

    @Override
    protected void populateIndiffs(final Map<Criterion, IndifferenceThreshold> indiffs) {
        indiffs.put(retrieveCriteria().get("c01"), new IndifferenceThreshold(500f));
    }

    @Override
    protected void populatePrefs(final Map<Criterion, PreferenceThreshold> prefs) {
        prefs.put(retrieveCriteria().get("c02"), new PreferenceThreshold(0f));
    }

    @Override
    protected void populateWeights(final Weights weights) {
        weights.putWeight(retrieveCriteria().get("c02"), new Weight(0.4f));
    }
}
