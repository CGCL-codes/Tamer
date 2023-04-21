package dr.evomodel.continuous;

import dr.evolution.continuous.Continuous;
import dr.evolution.continuous.ContinuousTraitLikelihood;
import dr.evolution.continuous.Contrastable;
import dr.evomodel.tree.TreeModel;
import dr.inference.model.Statistic;
import dr.xml.*;
import java.util.ArrayList;

/**
 * Calculates the likelihood of a set of continuous attributes on a tree.
 *
 * @version $Id: RootTraitMLE.java,v 1.3 2006/06/18 16:20:58 alexei Exp $
 *
 * @author Alexei Drummond
 */
public class RootTraitMLE extends Statistic.Abstract {

    public static final String ROOT_TRAIT_MLE = "rootTraitMLE";

    public static final String TRAIT = "trait";

    public static final String NAME = "name";

    public RootTraitMLE(TreeModel treeModel, String[] traitNames) {
        this.treeModel = treeModel;
        this.traitNames = traitNames;
        mles = new Contrastable[traitNames.length];
    }

    /**
	 * Get MLE estimate of the given trait
	 * @return the MLE estimate of the given trait index.
	 */
    public double getStatisticValue(int i) {
        contTraitLikelihood.calculateLikelihood(treeModel, traitNames, mles, 1.0);
        if (mles[i] instanceof Continuous) return ((Continuous) mles[i]).getValue();
        return -1.0;
    }

    public int getDimension() {
        return traitNames.length;
    }

    public static XMLObjectParser PARSER = new AbstractXMLObjectParser() {

        public String getParserName() {
            return ROOT_TRAIT_MLE;
        }

        public Object parseXMLObject(XMLObject xo) throws XMLParseException {
            TreeModel treeModel = (TreeModel) xo.getChild(TreeModel.class);
            ArrayList<String> traits = new ArrayList<String>();
            for (int i = 0; i < xo.getChildCount(); i++) {
                Object child = xo.getChild(i);
                if (xo.getChild(i) instanceof XMLObject) {
                    XMLObject cxo = (XMLObject) xo.getChild(i);
                    if (cxo.getName().equals(TRAIT)) {
                        traits.add(cxo.getStringAttribute(NAME));
                    }
                }
            }
            String[] traitNames = new String[traits.size()];
            for (int i = 0; i < traitNames.length; i++) {
                traitNames[i] = traits.get(i);
            }
            RootTraitMLE rootTraitMLE = new RootTraitMLE(treeModel, traitNames);
            return rootTraitMLE;
        }

        public String getParserDescription() {
            return "A maximum likelihood estimate of trait value at the root.";
        }

        public Class getReturnType() {
            return RootTraitMLE.class;
        }

        public XMLSyntaxRule[] getSyntaxRules() {
            return rules;
        }

        private XMLSyntaxRule[] rules = new XMLSyntaxRule[] { new ElementRule(TreeModel.class) };
    };

    private String[] traitNames = null;

    private Contrastable[] mles = null;

    private TreeModel treeModel = null;

    private ContinuousTraitLikelihood contTraitLikelihood = new ContinuousTraitLikelihood();
}
