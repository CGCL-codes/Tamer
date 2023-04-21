package cz.cuni.mff.ksi.jinfer.attrstats.heuristics.construction.fidax;

import cz.cuni.mff.ksi.jinfer.attrstats.utils.MappingUtils;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.DeletableList;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.attrstats.utils.ImageSizeComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the heuristic algorithm for finding ID set as described
 * in the "Finding ID Attributes in XML Documents" article.
 *
 * @author vektor
 */
public final class FidaxAlgorithm {

    private FidaxAlgorithm() {
    }

    /**
   * Finds the "best" ID set in the specified attribute mapping model. For
   * detailed information on the algorithm itself, please refer to the article.
   *
   * @param model Model to work on.
   * @param alpha Weight of the attribute mapping <cite>support</cite> in its total weight.
   * @param beta Weight of the attribute mapping <cite>coverage</cite> in its total weight.
   *
   * @return List of attribute mappings constituting the ID set found.
   */
    public static IdSet findIDSet(final AMModel model, final double alpha, final double beta) {
        final List<AttributeMappingId> C = MappingUtils.getCandidates(model);
        Collections.sort(C, new ImageSizeComparator(model));
        final Map<AttributeMappingId, Double> weights = new HashMap<AttributeMappingId, Double>();
        final Set<String> types = new HashSet<String>();
        for (final AttributeMappingId mapping : C) {
            types.add(mapping.getElement());
            weights.put(mapping, Double.valueOf(model.weight(mapping, alpha, beta)));
        }
        final List<AttributeMappingId> C1 = new ArrayList<AttributeMappingId>();
        for (final String type : types) {
            final AttributeMappingId m = findMaxWeight(type, weights);
            C1.add(m);
        }
        final DeletableList<AttributeMappingId> C2 = new DeletableList<AttributeMappingId>(C1);
        while (C2.hasNext()) {
            final AttributeMappingId m = C2.next();
            final List<AttributeMappingId> conflicts = new ArrayList<AttributeMappingId>();
            double conflicsWeight = 0;
            for (final AttributeMappingId c : C2.getLive()) {
                if (!c.equals(m) && MappingUtils.imagesIntersect(m, c, model)) {
                    conflicsWeight += weights.get(c);
                    conflicts.add(c);
                }
            }
            if (weights.get(m) > conflicsWeight) {
                C2.removeAll(conflicts);
            } else {
                C2.remove(m);
            }
        }
        return new IdSet(C2.getLive());
    }

    private static AttributeMappingId findMaxWeight(final String type, final Map<AttributeMappingId, Double> weights) {
        double maxWeight = 0;
        AttributeMappingId max = null;
        for (final Map.Entry<AttributeMappingId, Double> e : weights.entrySet()) {
            final double weight = e.getValue().doubleValue();
            if (e.getKey().isType(type) && weight > maxWeight) {
                maxWeight = weight;
                max = e.getKey();
            }
        }
        if (max == null) {
            throw new IllegalStateException("No mappings of type " + type + " found");
        }
        return max;
    }
}
