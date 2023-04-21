package it.unitn.disi.smatch.filters;

import it.unitn.disi.smatch.SMatchConstants;
import it.unitn.disi.smatch.data.mappings.IContextMapping;
import it.unitn.disi.smatch.data.mappings.IMappingElement;
import it.unitn.disi.smatch.data.trees.INode;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import java.util.Iterator;

/**
 * Filters mapping removing all links which logically follow from the other links in the mapping.
 * <p/>
 * For more details see:
 * <p/>
 * <a href="http://eprints.biblio.unitn.it/archive/00001525/">http://eprints.biblio.unitn.it/archive/00001525/</a>
 * <p/>
 * Giunchiglia, Fausto and Maltese, Vincenzo and Autayeu, Aliaksandr. Computing minimal mappings.
 * Technical Report DISI-08-078, Department of Information Engineering and Computer Science, University of Trento.
 * Proc. of the Fourth Ontology Matching Workshop at ISWC 2009.
 *
 * @author Aliaksandr Autayeu avtaev@gmail.com
 */
public class RedundantMappingFilter extends BaseFilter implements IMappingFilter {

    private static final Logger log = Logger.getLogger(RedundantMappingFilter.class);

    public IContextMapping<INode> filter(IContextMapping<INode> mapping) {
        if (log.isEnabledFor(Level.INFO)) {
            log.info("Filtering started...");
        }
        long start = System.currentTimeMillis();
        long counter = 0;
        long total = (long) mapping.size();
        long reportInt = (total / 20) + 1;
        IContextMapping<INode> result = mappingFactory.getContextMappingInstance(mapping.getSourceContext(), mapping.getTargetContext());
        for (IMappingElement<INode> e : mapping) {
            if (!isRedundant(mapping, e)) {
                result.setRelation(e.getSource(), e.getTarget(), e.getRelation());
            }
            counter++;
            if ((SMatchConstants.LARGE_TASK < total) && (0 == (counter % reportInt)) && log.isEnabledFor(Level.INFO)) {
                log.info(100 * counter / total + "%");
            }
        }
        if (log.isEnabledFor(Level.INFO)) {
            log.info("Filtering finished: " + (System.currentTimeMillis() - start) + " ms");
        }
        return result;
    }

    /**
     * Checks the relation between source and target is redundant or not for minimal mapping.
     *
     * @param mapping a mapping
     * @param e       a mapping element
     * @return true for redundant relation
     */
    private boolean isRedundant(IContextMapping<INode> mapping, IMappingElement<INode> e) {
        switch(e.getRelation()) {
            case IMappingElement.LESS_GENERAL:
                {
                    if (verifyCondition1(mapping, e)) {
                        return true;
                    }
                    break;
                }
            case IMappingElement.MORE_GENERAL:
                {
                    if (verifyCondition2(mapping, e)) {
                        return true;
                    }
                    break;
                }
            case IMappingElement.DISJOINT:
                {
                    if (verifyCondition3(mapping, e)) {
                        return true;
                    }
                    break;
                }
            case IMappingElement.EQUIVALENCE:
                {
                    if (verifyCondition4(mapping, e)) {
                        return true;
                    }
                    break;
                }
            default:
                {
                    return false;
                }
        }
        return false;
    }

    protected boolean verifyCondition1(IContextMapping<INode> mapping, IMappingElement<INode> e) {
        boolean result = findRelation(IMappingElement.LESS_GENERAL, e.getSource().getAncestors(), e.getTarget(), mapping) || findRelation(IMappingElement.LESS_GENERAL, e.getSource(), e.getTarget().getDescendants(), mapping) || findRelation(IMappingElement.LESS_GENERAL, e.getSource().getAncestors(), e.getTarget().getDescendants(), mapping);
        return result;
    }

    protected boolean verifyCondition2(IContextMapping<INode> mapping, IMappingElement<INode> e) {
        boolean result = findRelation(IMappingElement.MORE_GENERAL, e.getSource(), e.getTarget().getAncestors(), mapping) || findRelation(IMappingElement.MORE_GENERAL, e.getSource().getDescendants(), e.getTarget(), mapping) || findRelation(IMappingElement.MORE_GENERAL, e.getSource().getDescendants(), e.getTarget().getAncestors(), mapping);
        return result;
    }

    protected boolean verifyCondition3(IContextMapping<INode> mapping, IMappingElement<INode> e) {
        boolean result = findRelation(IMappingElement.DISJOINT, e.getSource(), e.getTarget().getAncestors(), mapping) || findRelation(IMappingElement.DISJOINT, e.getSource().getAncestors(), e.getTarget(), mapping) || findRelation(IMappingElement.DISJOINT, e.getSource().getAncestors(), e.getTarget().getAncestors(), mapping);
        return result;
    }

    protected boolean verifyCondition4(IContextMapping<INode> mapping, IMappingElement<INode> e) {
        boolean result = (findRelation(IMappingElement.EQUIVALENCE, e.getSource(), e.getTarget().getAncestors(), mapping) && findRelation(IMappingElement.EQUIVALENCE, e.getSource().getAncestors(), e.getTarget(), mapping)) || (findRelation(IMappingElement.EQUIVALENCE, e.getSource(), e.getTarget().getDescendants(), mapping) && findRelation(IMappingElement.EQUIVALENCE, e.getSource().getDescendants(), e.getTarget(), mapping)) || (findRelation(IMappingElement.EQUIVALENCE, e.getSource().getAncestors(), e.getTarget().getDescendants(), mapping) && findRelation(IMappingElement.EQUIVALENCE, e.getSource().getDescendants(), e.getTarget().getAncestors(), mapping));
        return result;
    }

    public boolean findRelation(char relation, Iterator<INode> sourceNodes, INode targetNode, IContextMapping<INode> mapping) {
        while (sourceNodes.hasNext()) {
            if (relation == getRelation(mapping, sourceNodes.next(), targetNode)) {
                return true;
            }
        }
        return false;
    }

    public boolean findRelation(char relation, INode sourceNode, Iterator<INode> targetNodes, IContextMapping<INode> mapping) {
        while (targetNodes.hasNext()) {
            if (relation == getRelation(mapping, sourceNode, targetNodes.next())) {
                return true;
            }
        }
        return false;
    }

    public boolean findRelation(char relation, Iterator<INode> sourceNodes, Iterator<INode> targetNodes, IContextMapping<INode> mapping) {
        while (sourceNodes.hasNext()) {
            INode sourceNode = sourceNodes.next();
            while (targetNodes.hasNext()) {
                if (relation == getRelation(mapping, sourceNode, targetNodes.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected char getRelation(IContextMapping<INode> mapping, INode a, INode b) {
        return mapping.getRelation(a, b);
    }
}
