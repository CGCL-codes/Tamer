package dr.evomodel.treelikelihood;

import dr.evolution.LinkedGroup;
import dr.evolution.util.Taxon;
import dr.evolution.util.TaxonList;
import dr.evomodel.tree.HiddenLinkageModel;
import dr.evomodel.tree.TreeModel;
import dr.inference.model.AbstractModelLikelihood;
import dr.inference.model.Model;
import dr.inference.model.Variable;
import dr.inference.model.Variable.ChangeType;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author Aaron Darling
 */
public class HiddenLinkageLikelihood extends AbstractModelLikelihood {

    TreeModel tree;

    HiddenLinkageModel hlm;

    public HiddenLinkageLikelihood(HiddenLinkageModel hlm, TreeModel tree) {
        super("HiddenLinkageLikelihood");
        this.hlm = hlm;
        this.tree = tree;
    }

    protected void acceptState() {
    }

    protected void handleModelChangedEvent(Model model, Object object, int index) {
    }

    protected void handleVariableChangedEvent(Variable variable, int index, ChangeType type) {
    }

    protected void restoreState() {
    }

    protected void storeState() {
    }

    public double getLogLikelihood() {
        double logL = 0;
        ArrayList<LinkedGroup> linkedGroups = hlm.getData().getConstraints();
        for (LinkedGroup lg : linkedGroups) {
            TaxonList tl = lg.getLinkedReads();
            int found = 0;
            for (int l = 0; l < hlm.getLinkageGroupCount(); l++) {
                Set<Taxon> group = hlm.getGroup(l);
                for (int i = 0; i < tl.getTaxonCount(); i++) {
                    if (group.contains(tl.getTaxon(i))) found++;
                }
                if (found == tl.getTaxonCount()) {
                    logL += Math.log(lg.getLinkageProbability());
                    break;
                } else if (found > 0) {
                    logL += Math.log(1.0 - lg.getLinkageProbability());
                    break;
                }
            }
        }
        if (hlm.getData().getFixedReferenceTree()) {
            logL += Double.NEGATIVE_INFINITY;
        }
        return logL;
    }

    public Model getModel() {
        return this;
    }

    public void makeDirty() {
    }
}
