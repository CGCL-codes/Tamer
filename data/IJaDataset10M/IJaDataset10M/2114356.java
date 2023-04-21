package dr.inference.model;

import dr.inference.loggers.LogColumn;
import dr.inference.loggers.Loggable;
import dr.inference.loggers.NumberColumn;
import dr.math.LogTricks;
import dr.util.Citable;
import dr.util.Citation;
import dr.util.CommonCitations;
import dr.xml.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Marc A. Suchard
 * @author Andrew Rambaut
 */
public class IntegratedMixtureModel extends AbstractModelLikelihood implements Citable, Loggable {

    public static final String MIXTURE_MODEL = "integratedMixtureModel";

    public static final String NORMALIZE = "normalize";

    public IntegratedMixtureModel(List<AbstractModelLikelihood> likelihoodList) {
        super(MIXTURE_MODEL);
        this.likelihoodList = likelihoodList;
        for (AbstractModelLikelihood model : likelihoodList) {
            addModel(model);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Constructing an integrated finite mixture model\n");
        sb.append("\tComponents:\n");
        for (AbstractModelLikelihood model : likelihoodList) {
            sb.append("\t\t\t").append(model.getId()).append("\n");
        }
        sb.append("\tPlease cite:\n");
        sb.append(Utils.getCitationString((this)));
        Logger.getLogger("dr.inference.model").info(sb.toString());
    }

    protected void handleModelChangedEvent(Model model, Object object, int index) {
    }

    protected final void handleVariableChangedEvent(Variable variable, int index, Parameter.ChangeType type) {
    }

    protected void storeState() {
    }

    protected void restoreState() {
    }

    protected void acceptState() {
    }

    public Model getModel() {
        return this;
    }

    public double getLogLikelihood() {
        return getLogLikelihoodSum();
    }

    private double getLogLikelihoodSum() {
        double logSum = Double.NEGATIVE_INFINITY;
        double pi = 1.0 / likelihoodList.size();
        for (int i = 0; i < likelihoodList.size(); ++i) {
            if (pi > 0.0) {
                logSum = LogTricks.logSum(logSum, Math.log(pi) + likelihoodList.get(i).getLogLikelihood());
            }
        }
        return logSum;
    }

    public void makeDirty() {
    }

    public LogColumn[] getColumns() {
        LogColumn[] columns = new LogColumn[likelihoodList.size()];
        for (int i = 0; i < likelihoodList.size(); ++i) {
            columns[i] = new MixtureColumn(MIXTURE_MODEL, i);
        }
        return columns;
    }

    private class MixtureColumn extends NumberColumn {

        public MixtureColumn(String label, int dim) {
            super(label);
            this.dim = dim;
        }

        @Override
        public double getDoubleValue() {
            double logSum = getLogLikelihoodSum();
            return likelihoodList.get(dim).getLogLikelihood() - logSum;
        }

        private final int dim;
    }

    public static XMLObjectParser PARSER = new AbstractXMLObjectParser() {

        public String getParserName() {
            return MIXTURE_MODEL;
        }

        public Object parseXMLObject(XMLObject xo) throws XMLParseException {
            Parameter weights = (Parameter) xo.getChild(Parameter.class);
            List<AbstractModelLikelihood> likelihoodList = new ArrayList<AbstractModelLikelihood>();
            for (int i = 0; i < xo.getChildCount(); i++) {
                if (xo.getChild(i) instanceof Likelihood) likelihoodList.add((AbstractModelLikelihood) xo.getChild(i));
            }
            if (weights.getDimension() != likelihoodList.size()) {
                throw new XMLParseException("Dim of " + weights.getId() + " does not match the number of likelihoods");
            }
            if (xo.hasAttribute(NORMALIZE)) {
                if (xo.getBooleanAttribute(NORMALIZE)) {
                    double sum = 0;
                    for (int i = 0; i < weights.getDimension(); i++) sum += weights.getParameterValue(i);
                    for (int i = 0; i < weights.getDimension(); i++) weights.setParameterValue(i, weights.getParameterValue(i) / sum);
                }
            }
            if (!normalized(weights)) throw new XMLParseException("Parameter +" + weights.getId() + " must lie on the simplex");
            return new IntegratedMixtureModel(likelihoodList);
        }

        private boolean normalized(Parameter p) {
            double sum = 0;
            for (int i = 0; i < p.getDimension(); i++) sum += p.getParameterValue(i);
            return (sum == 1.0);
        }

        public String getParserDescription() {
            return "This element represents a finite mixture of likelihood models.";
        }

        public Class getReturnType() {
            return CompoundModel.class;
        }

        public XMLSyntaxRule[] getSyntaxRules() {
            return rules;
        }

        private final XMLSyntaxRule[] rules = { AttributeRule.newBooleanRule(NORMALIZE, true), new ElementRule(Likelihood.class, 2, Integer.MAX_VALUE), new ElementRule(Parameter.class) };
    };

    List<AbstractModelLikelihood> likelihoodList;

    public List<Citation> getCitations() {
        List<Citation> citations = new ArrayList<Citation>();
        citations.add(CommonCitations.LEMEY_MIXTURE_2012);
        return citations;
    }
}
