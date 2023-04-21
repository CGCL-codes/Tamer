package uk.ac.shef.wit.trex.representation.walk;

import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import uk.ac.shef.wit.trex.TrexException;
import uk.ac.shef.wit.trex.representation.Representation;
import uk.ac.shef.wit.trex.representation.RepresentationIterator;
import uk.ac.shef.wit.trex.representation.RepresentationModel;
import uk.ac.shef.wit.trex.representation.RepresentationNode;
import uk.ac.shef.wit.trex.util.logger.Logger;
import java.util.*;

/**
 * Given a set of nodes and a path to follow, this walk returns the set of nodes at the end of the path (reverse traversal: SLOW).
 */
public class WalkReverseNodesAt extends WalkPathFollower {

    protected Map _cache;

    public WalkReverseNodesAt(final String relationTypeLabel) {
        super(relationTypeLabel);
    }

    public Walk prepare(final Representation representation) throws TrexException {
        super.prepare(representation);
        if (_cache == null) {
            final RepresentationModel model = representation.getModel();
            final int relationTypeIndex = model.getIndexOf(_relationTypeId);
            final Object domainOfRelation = model.getDomainOfRelation(_relationTypeId);
            Logger.progress(new StringBuffer().append("caching ").append(model.getLabelOf(domainOfRelation)).append(" nodes with relation ").append(_relationTypeLabel).toString());
            _cache = new Object2ObjectAVLTreeMap();
            for (RepresentationIterator it = representation.getNodesOfType(domainOfRelation); it.hasNext(); ) {
                final RepresentationNode nodeFrom = it.nextNode();
                final RepresentationNode nodeTo = nodeFrom.follow(relationTypeIndex);
                if (nodeTo != null) {
                    ObjectArrayList cached = (ObjectArrayList) _cache.get(nodeTo);
                    if (cached == null) _cache.put(nodeTo, cached = new ObjectArrayList(1));
                    cached.add(nodeFrom);
                }
            }
        }
        return this;
    }

    public Set walk(final Set nodes) {
        final Set detected = new HashSet();
        for (Iterator it = nodes.iterator(); it.hasNext(); ) {
            final WalkNode walkNode = (WalkNode) it.next();
            final Collection found = (Collection) _cache.get(walkNode.getNode());
            if (found != null) {
                for (Iterator it2 = found.iterator(); it2.hasNext(); ) {
                    final RepresentationNode node = (RepresentationNode) it2.next();
                    detected.add(new WalkNode(node, extendPath(walkNode.getPath(), _relationTypeLabel, node.getLabel())));
                }
            }
        }
        return detected;
    }
}
