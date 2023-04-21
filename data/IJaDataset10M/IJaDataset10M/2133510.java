package dr.evomodel.branchratemodel;

import dr.evolution.tree.NodeRef;
import dr.evolution.tree.Tree;
import dr.inference.model.AbstractModel;
import dr.inference.model.Model;
import dr.inference.model.Parameter;
import dr.inference.model.Variable;
import dr.xml.*;

/**
 * @author Andrew Rambaut
 * @version $Id: TipBranchRateModel.java,v 1.1 2005/12/14 16:46:20 rambaut Exp $
 */
public class TipBranchRateModel extends AbstractBranchRateModel {

    public static final String TIP_BRANCH_RATE_MODEL = "tipBranchRateModel";

    public static final String EXTERNAL_RATE = "externalRate";

    public static final String INTERNAL_RATE = "internalRate";

    private Parameter internalRateParameter;

    private Parameter externalRateParameter;

    public TipBranchRateModel(Parameter externalRateParameter, Parameter internalRateParameter) {
        super(TIP_BRANCH_RATE_MODEL);
        this.internalRateParameter = internalRateParameter;
        addVariable(internalRateParameter);
        this.externalRateParameter = externalRateParameter;
        addVariable(externalRateParameter);
    }

    public void handleModelChangedEvent(Model model, Object object, int index) {
        fireModelChanged();
    }

    protected final void handleVariableChangedEvent(Variable variable, int index, Parameter.ChangeType type) {
        fireModelChanged();
    }

    protected void storeState() {
    }

    protected void restoreState() {
    }

    protected void acceptState() {
    }

    public static XMLObjectParser PARSER = new AbstractXMLObjectParser() {

        public String getParserName() {
            return TIP_BRANCH_RATE_MODEL;
        }

        public Object parseXMLObject(XMLObject xo) throws XMLParseException {
            Parameter externalRateParameter = (Parameter) xo.getElementFirstChild(EXTERNAL_RATE);
            Parameter internalRateParameter = (Parameter) xo.getElementFirstChild(INTERNAL_RATE);
            TipBranchRateModel tipBranchRateModel = new TipBranchRateModel(externalRateParameter, internalRateParameter);
            System.out.println("Using tip branch rate model.");
            return tipBranchRateModel;
        }

        public String getParserDescription() {
            return "This element returns a branch rate model that has a different rate for external branches than internal.";
        }

        public Class getReturnType() {
            return TipBranchRateModel.class;
        }

        public XMLSyntaxRule[] getSyntaxRules() {
            return rules;
        }

        private XMLSyntaxRule[] rules = new XMLSyntaxRule[] { new ElementRule(EXTERNAL_RATE, Parameter.class, "The molecular evolutionary rate parameter for external branches", false), new ElementRule(INTERNAL_RATE, Parameter.class, "The molecular evolutionary rate parameter for internal branches", false) };
    };

    public double getBranchRate(final Tree tree, final NodeRef node) {
        if (tree.isExternal(node)) {
            return externalRateParameter.getParameterValue(0);
        } else {
            return internalRateParameter.getParameterValue(0);
        }
    }
}
