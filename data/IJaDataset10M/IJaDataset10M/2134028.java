package cz.cuni.mff.ksi.jinfer.base.clustering;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import java.util.List;

/**
 * Clusterer implementation in which two rules belong to the same cluster when
 * they have the same name and the same context.
 *
 * @author vektor
 */
public class ContextClusterer extends AbstractClustererImpl {

    @Override
    protected boolean clusters(final AbstractNode n, final AbstractNode first) {
        return first.getName().equalsIgnoreCase(n.getName()) && equalContexts(first.getContext(), n.getContext());
    }

    private boolean equalContexts(final List<String> c1, final List<String> c2) {
        if (c1 == null || c2 == null || c1.size() != c2.size()) {
            return false;
        }
        int i = 0;
        for (final String s : c1) {
            if (!s.equalsIgnoreCase(c2.get(i))) {
                return false;
            }
            i++;
        }
        return true;
    }
}
