package urban.shapes;

import static org.apache.commons.collections15.CollectionUtils.collect;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections15.Transformer;
import urban.model.Rule;
import urban.shapes.RuleGraph.WithHash;
import urban.transformers.RuleGraphToRuleTransformer;

/**
 * Generates rules from shapes.
 * 
 * <h3>How the algorithm should work</h3>
 * 
 * <p>Using the shapes and a generator a list of rules (one per shape/generator config) is created</p>
 * 
 * <p>The list is then used to generate another list that contains all of the possible combinations of the first list, with any rule not capable of being combined being added to a result list</p>
 * <p>This step is now repeated until the list becomes empty</p>
 */
public class RuleFactory {

    private final ShapeParameters params;

    private final Transformer<RuleGraph, WithHash> transformRuleGraphToWithHash = new Transformer<RuleGraph, WithHash>() {

        @Override
        public WithHash transform(RuleGraph arg0) {
            return arg0.withHash();
        }
    };

    private final Transformer<WithHash, RuleGraph> transformWithHashToRuleGraph = new Transformer<WithHash, RuleGraph>() {

        @Override
        public RuleGraph transform(WithHash arg0) {
            return arg0.getRuleGraph();
        }
    };

    private Transformer<RuleGraph, Rule> transformGraphToRule = new RuleGraphToRuleTransformer();

    private Comparator<RuleGraph> order = new Comparator<RuleGraph>() {

        @Override
        public int compare(RuleGraph o1, RuleGraph o2) {
            String s1 = o1.toString();
            String s2 = o2.toString();
            int l = s2.length() - s1.length();
            if (l != 0) return l;
            return s1.compareTo(s2);
        }
    };

    /**
	 * @param params provides the required shapes and generators to generate rules with
	 */
    public RuleFactory(ShapeParameters params) {
        this.params = params;
    }

    /**
	 * @return a list of generated rules
	 */
    public List<Rule> generateRules() {
        List<Rule> rules = new LinkedList<Rule>();
        for (Generator g : params.constants.keySet()) rules.addAll(generateRules(g));
        return rules;
    }

    /**
	 * @param g
	 * @return a list of rules for the specified generator
	 */
    public Collection<? extends Rule> generateRules(Generator g) {
        Set<WithHash> rgs = new HashSet<WithHash>(collect(generateInitialRules(g), transformRuleGraphToWithHash));
        if (rgs.isEmpty()) return Collections.singleton(transformGraphToRule.transform(g.getRuleGraph()));
        List<WithHash> list = new ArrayList<WithHash>();
        while (!rgs.isEmpty()) rgs = mergeRules(g, rgs, list);
        List<RuleGraph> list2 = new ArrayList<RuleGraph>(collect(list, transformWithHashToRuleGraph));
        Collections.sort(list2, order);
        return collect(list2, transformGraphToRule);
    }

    /**
	 * Takes a list of rules, for example
	 * ["A(f~0,x) <-> A(f~1,x)","A(f~0,y!_) <-> A(f~1,y!_)","A(f~0,y) <-> A(f~1,y)","A(f~0,x!_,y!_) <-> A(f~1,x!_,y!_)"]
	 * and either merges them or adds them a the list parameter to for example returning
	 * ["A(f~0,x,y!_) <-> A(f~1,x,y!_)","A(f~0,x,y) <-> A(f~1,x,y)"] and adding
	 * ["A(f~0,x!_,y!_) <-> A(f~1,x!_,y!_)"] to the parameter list
	 * 
	 * @param g used in the merge process
	 * @param rules to be merged
	 * @param list rules that cannot be merged will be added to this
	 * @return the merged list of rules
	 */
    private Set<WithHash> mergeRules(Generator g, Set<WithHash> rules, Collection<WithHash> list) {
        Set<WithHash> next = new HashSet<WithHash>();
        Set<WithHash> mergedRules = new HashSet<WithHash>();
        Iterator<WithHash> iterator = rules.iterator();
        while (iterator.hasNext()) {
            WithHash r = iterator.next();
            iterator.remove();
            for (WithHash r2 : rules) {
                RuleGraph merged = new RuleMerger(r.getRuleGraph(), r2.getRuleGraph()).merge();
                if (merged != null) {
                    next.add(merged.withHash());
                    mergedRules.add(r);
                    mergedRules.add(r2);
                }
            }
            if (!mergedRules.contains(r)) {
                if (!r.getRuleGraph().hasSymmetricalBond() || !list.contains(r.getRuleGraph().getMirror().withHash())) list.add(r);
            }
        }
        return next;
    }

    private LinkedList<RuleGraph> generateInitialRules(Generator g) {
        LinkedList<RuleGraph> list = new LinkedList<RuleGraph>();
        for (Shape s : params.epsilons.keySet()) list.addAll(s.getMatchPermutations(g.getRuleGraph()));
        return list;
    }
}
