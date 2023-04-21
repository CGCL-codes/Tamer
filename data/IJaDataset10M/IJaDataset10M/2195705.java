package net.sf.myra.hantminer.multilabel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.myra.datamining.AbstractObjectiveFunction;
import net.sf.myra.datamining.AbstractPruner;
import net.sf.myra.datamining.Evaluator;
import net.sf.myra.datamining.Rule;
import net.sf.myra.datamining.data.Dataset;
import net.sf.myra.datamining.data.Label;
import net.sf.myra.datamining.data.Dataset.Instance;
import net.sf.myra.framework.Cost;
import net.sf.myra.framework.Trail;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 1973 $ $Date:: 2009-02-17 12:48:24#$
 */
public class BranchPruner extends AbstractPruner {

    /**
	 * Default constructor.
	 * 
	 * @param function the rule quality measure function.
	 */
    public BranchPruner(AbstractObjectiveFunction function) {
        super(function, null);
    }

    public Trail explore(Trail trail) {
        AbstractObjectiveFunction function = getFunction();
        Dataset dataset = function.getDataset();
        Label prototype = dataset.getMetadata().getLabel();
        Rule rule = prune((Rule) trail, prototype, dataset);
        Cost cost = function.evaluate(rule);
        while (rule.getSize() > 1) {
            Rule candidate = rule.clone();
            candidate.remove(candidate.getLast());
            candidate = prune(candidate, prototype, dataset);
            Cost c = function.evaluate(candidate);
            if (c.compareTo(cost) > -1) {
                rule = candidate;
                cost = c;
            } else {
                break;
            }
        }
        return rule;
    }

    /**
	 * Prunes the consequent of the rule, removing one brach at a time until
	 * the quality of the rule is not improved or there is only one branch
	 * left.
	 * 
	 * @param rule the rule to be pruned.
	 * @param prototype the label prototype instance.
	 * @param dataset the dataset reference.
	 * 
	 * @return the pruned rule.
	 */
    @SuppressWarnings("unchecked")
    private Rule prune(Rule rule, Label prototype, Dataset dataset) {
        List<Instance> covered = Evaluator.findCoveredCases(rule, dataset);
        ArrayList<Label> branches = findBranches(covered);
        rule.setConsequent(buildLabel(prototype, branches));
        Cost cost = getFunction().evaluate(rule);
        while (branches.size() > 1) {
            Label removed = null;
            for (Label label : branches) {
                ArrayList<Label> reduced = (ArrayList<Label>) branches.clone();
                reduced.remove(label);
                Rule candidate = rule.clone();
                candidate.setConsequent(buildLabel(prototype, reduced));
                Cost c = getFunction().evaluate(candidate);
                if (c.compareTo(cost) > -1) {
                    rule = candidate;
                    cost = c;
                    removed = label;
                }
            }
            if (removed != null) {
                branches.remove(removed);
            } else {
                break;
            }
        }
        return rule;
    }

    /**
	 * Returns a collection of different labels (branches of the class
	 * hierarchy) from the specified list of covered instances.
	 *  
	 * @param covered the list of covered instances.
	 * 
	 * @return a collection of different labels.
	 */
    private ArrayList<Label> findBranches(List<Instance> covered) {
        ArrayList<Label> branches = new ArrayList<Label>(covered.size());
        for (Instance instance : covered) {
            Label label = instance.getLabel();
            boolean add = true;
            for (Iterator<Label> i = branches.iterator(); i.hasNext(); ) {
                Label l = i.next();
                if (l.contains(label)) {
                    add = false;
                } else if (label.contains(l)) {
                    i.remove();
                }
            }
            if (add) {
                branches.add(label);
            }
        }
        return branches;
    }

    /**
	 * Returns a label instance that represents the combinations of the
	 * specified labels.
	 * 
	 * @param prototype
	 * @param labels
	 * 
	 * @return
	 */
    private Label buildLabel(Label prototype, Collection<Label> labels) {
        Set<String> values = new HashSet<String>(prototype.length());
        for (Label label : labels) {
            values.addAll(label.getValues());
        }
        return prototype.toLabel(values);
    }
}
