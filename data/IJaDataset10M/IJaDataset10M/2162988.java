package dr.evomodelxml.branchratemodel;

import dr.evomodel.branchratemodel.MixtureModelBranchRates;
import dr.evomodel.tree.TreeModel;
import dr.inference.distribution.ParametricDistributionModel;
import dr.inference.model.Parameter;
import dr.xml.*;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 * @author Wai Lok Sibon Li
 */
public class MixtureModelBranchRatesParser extends AbstractXMLObjectParser {

    public static final String MIXTURE_MODEL_BRANCH_RATES = "mixtureModelBranchRates";

    public static final String DISTRIBUTION = "distribution";

    public static final String RATE_CATEGORY_QUANTILES = "rateCategoryQuantiles";

    public static final String SINGLE_ROOT_RATE = "singleRootRate";

    public static final String NORMALIZE = "normalize";

    public static final String NORMALIZE_BRANCH_RATE_TO = "normalizeBranchRateTo";

    public static final String DISTRIBUTION_INDEX = "distributionIndex";

    public static final String USE_QUANTILE = "useQuantilesForRates";

    public String getParserName() {
        return MIXTURE_MODEL_BRANCH_RATES;
    }

    public Object parseXMLObject(XMLObject xo) throws XMLParseException {
        ArrayList<ParametricDistributionModel> modelsList = new ArrayList<ParametricDistributionModel>();
        final boolean normalize = xo.getAttribute(NORMALIZE, false);
        final double normalizeBranchRateTo = xo.getAttribute(NORMALIZE_BRANCH_RATE_TO, Double.NaN);
        final boolean useQuantilesForRates = xo.getAttribute(USE_QUANTILE, true);
        TreeModel tree = (TreeModel) xo.getChild(TreeModel.class);
        for (int i = 0; i < xo.getChildCount(); i++) {
            Object child = xo.getChild(i);
            if (child instanceof XMLObject) {
                if (((XMLObject) child).getName().equals(DISTRIBUTION)) {
                    XMLObject childXML = (XMLObject) child;
                    modelsList.add((ParametricDistributionModel) childXML.getChild(0));
                }
            }
        }
        ParametricDistributionModel[] models = modelsList.toArray(new ParametricDistributionModel[modelsList.size()]);
        Parameter rateCategoryQuantilesParameter = (Parameter) xo.getElementFirstChild(RATE_CATEGORY_QUANTILES);
        Parameter distributionIndexParameter = (Parameter) xo.getElementFirstChild(DISTRIBUTION_INDEX);
        Logger.getLogger("dr.evomodel").info("Using random discretized relaxed clock model with a mixture distribution.");
        for (int i = 0; i < models.length; i++) {
            Logger.getLogger("dr.evomodel").info("  parametric model " + (i + 1) + " = " + models[i].getModelName());
        }
        Logger.getLogger("dr.evomodel").info("   rate categories = " + rateCategoryQuantilesParameter.getDimension());
        if (normalize) {
            Logger.getLogger("dr.evomodel").info("   mean rate is normalized to " + normalizeBranchRateTo);
        }
        if (xo.hasAttribute(SINGLE_ROOT_RATE)) {
            Logger.getLogger("dr.evomodel").warning("   WARNING: single root rate is not implemented!");
        }
        if (!useQuantilesForRates) {
            Logger.getLogger("dr.evomodel").info("Rates are set to not being drawn using quantiles. Thus they are not drawn from any particular distribution.");
        }
        return new MixtureModelBranchRates(tree, rateCategoryQuantilesParameter, models, distributionIndexParameter, useQuantilesForRates, normalize, normalizeBranchRateTo);
    }

    public String getParserDescription() {
        return "This element returns a random discretized relaxed clock model." + "The branch rates are drawn from a continuous parametric distribution.";
    }

    public Class getReturnType() {
        return MixtureModelBranchRates.class;
    }

    public XMLSyntaxRule[] getSyntaxRules() {
        return rules;
    }

    private XMLSyntaxRule[] rules = new XMLSyntaxRule[] { AttributeRule.newBooleanRule(SINGLE_ROOT_RATE, true, "Whether only a single rate should be used for the two children branches of the root"), AttributeRule.newBooleanRule(NORMALIZE, true, "Whether the mean rate has to be normalized to a particular value"), AttributeRule.newDoubleRule(NORMALIZE_BRANCH_RATE_TO, true, "The mean rate to normalize to, if normalizing"), AttributeRule.newBooleanRule(USE_QUANTILE, true, "Whether or not to use quantiles to represent rates. If false then rates are not drawn " + "specifically from any of the distributions"), new ElementRule(TreeModel.class), new ElementRule(DISTRIBUTION, ParametricDistributionModel.class, "The distribution model for rates among branches", 1, Integer.MAX_VALUE), new ElementRule(DISTRIBUTION_INDEX, Parameter.class, "Operator that switches between the distributions of the branch rate distribution model", false), new ElementRule(RATE_CATEGORY_QUANTILES, Parameter.class, "The quantiles for", false) };
}
