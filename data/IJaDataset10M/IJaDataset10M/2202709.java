package cz.cuni.mff.ksi.jinfer.crudemdl.processing;

import cz.cuni.mff.ksi.jinfer.autoeditor.AutoEditor;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.regexping.RegexpAutomaton;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.simplifying.KHContextMergeConditionTester;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Cluster;
import cz.cuni.mff.ksi.jinfer.crudemdl.clustering.Clusterer;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.regexping.RegexpAutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.regexping.RegexpAutomatonSimplifierStateRemoval;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.simplifying.GreedyAutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automaton.simplifying.MergeCondidionTester;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Class providing method for inferring DTD for single element. In this implementation
 * deterministic finite automaton is used.
 * First Prefix-Tree automaton is constructed using cluster.members() as positive
 * examples. Then, given KHContextMergeConditionTester, merging of states occurs
 * until there are no more states to merge. Currently k=2, h=1, so
 * producing 2,1-context automaton by merging.
 *
 * @author anti
 */
public class ClusterProcessorAutomatonMergingState implements ClusterProcessor<AbstractNode> {

    private static final Logger LOG = Logger.getLogger(ClusterProcessorAutomatonMergingState.class);

    @Override
    public AbstractNode processCluster(final Clusterer<AbstractNode> clusterer, final Cluster<AbstractNode> cluster) throws InterruptedException {
        final Automaton<AbstractNode> automaton = new Automaton<AbstractNode>(true);
        for (AbstractNode instance : cluster.getMembers()) {
            final Element element = (Element) instance;
            final Regexp<AbstractNode> rightSide = element.getSubnodes();
            if (!rightSide.isConcatenation()) {
                throw new IllegalArgumentException("Right side of rule at element: " + element.toString() + " is not a concatenation regexp.");
            }
            final List<AbstractNode> rightSideTokens = rightSide.getTokens();
            final List<AbstractNode> symbolString = new LinkedList<AbstractNode>();
            for (AbstractNode token : rightSideTokens) {
                if (token.isAttribute()) {
                    LOG.debug(token);
                    continue;
                }
                symbolString.add(clusterer.getRepresentantForItem(token));
            }
            automaton.buildPTAOnSymbol(symbolString);
        }
        LOG.debug("--- AutomatonMergingStateProcessor on element:");
        LOG.debug(cluster.getRepresentant());
        LOG.debug(">>> PTA automaton:");
        LOG.debug(automaton);
        LOG.debug("AUTO EDITOR: " + new AutoEditor<AbstractNode>().drawAutomatonToPickTwoStates(automaton));
        final AutomatonSimplifier<AbstractNode> automatonSimplifier = new GreedyAutomatonSimplifier<AbstractNode>();
        final List<MergeCondidionTester<AbstractNode>> l = new ArrayList<MergeCondidionTester<AbstractNode>>();
        l.add(new KHContextMergeConditionTester<AbstractNode>(2, 1));
        final Automaton<AbstractNode> simplifiedAutomaton = automatonSimplifier.simplify(automaton, l);
        LOG.debug(">>> After 2,1-context:");
        LOG.debug(simplifiedAutomaton);
        final RegexpAutomaton<AbstractNode> regexpAutomaton = new RegexpAutomaton<AbstractNode>(simplifiedAutomaton);
        LOG.debug(">>> After regexpautomaton created:");
        LOG.debug(regexpAutomaton);
        final RegexpAutomatonSimplifier<AbstractNode> regexpAutomatonSimplifier = new RegexpAutomatonSimplifierStateRemoval<AbstractNode>();
        final Regexp<AbstractNode> regexp = regexpAutomatonSimplifier.simplify(regexpAutomaton);
        LOG.debug(">>> And the regexp is:");
        LOG.debug(regexp);
        LOG.debug("--- End");
        return new Element(cluster.getRepresentant().getContext(), cluster.getRepresentant().getName(), cluster.getRepresentant().getMetadata(), regexp);
    }
}
