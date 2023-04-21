package dr.evomodel.coalescent;

import dr.evolution.coalescent.ConstExpConst;
import dr.evolution.coalescent.DemographicFunction;
import dr.evomodelxml.coalescent.ConstExpConstModelParser;
import dr.inference.model.Parameter;

/**
 * Exponential growth from a constant ancestral population size.
 *
 * @author Alexei Drummond
 * @author Andrew Rambaut
 * @version $Id: ConstantExponentialModel.java,v 1.8 2005/10/28 02:49:17 alexei Exp $
 */
public class ConstExpConstModel extends DemographicModel {

    /**
     * Construct demographic model with default settings
     */
    public ConstExpConstModel(Parameter N0Parameter, Parameter N1Parameter, Parameter timeParameter, Parameter growthRateParameter, Type units, boolean usingGrowthRate) {
        this(ConstExpConstModelParser.CONST_EXP_CONST_MODEL, N0Parameter, N1Parameter, timeParameter, growthRateParameter, units, usingGrowthRate);
    }

    /**
     * Construct demographic model with default settings
     */
    public ConstExpConstModel(String name, Parameter N0Parameter, Parameter N1Parameter, Parameter timeParameter, Parameter growthRateParameter, Type units, boolean usingGrowthRate) {
        super(name);
        constExpConst = new ConstExpConst(units);
        this.N0Parameter = N0Parameter;
        addVariable(N0Parameter);
        N0Parameter.addBounds(new Parameter.DefaultBounds(Double.POSITIVE_INFINITY, 0.0, 1));
        this.N1Parameter = N0Parameter;
        addVariable(N1Parameter);
        N1Parameter.addBounds(new Parameter.DefaultBounds(Double.POSITIVE_INFINITY, 0.0, 1));
        this.timeParameter = timeParameter;
        addVariable(timeParameter);
        timeParameter.addBounds(new Parameter.DefaultBounds(Double.POSITIVE_INFINITY, 0.0, 1));
        this.growthRateParameter = growthRateParameter;
        addVariable(growthRateParameter);
        growthRateParameter.addBounds(new Parameter.DefaultBounds(Double.POSITIVE_INFINITY, 0.0, 1));
        this.usingGrowthRate = usingGrowthRate;
        setUnits(units);
    }

    public DemographicFunction getDemographicFunction() {
        double time = timeParameter.getParameterValue(0);
        double N0 = N0Parameter.getParameterValue(0);
        double N1 = N1Parameter.getParameterValue(0);
        double growthRate = growthRateParameter.getParameterValue(0);
        if (!usingGrowthRate) {
            double doublingTime = growthRate;
            growthRate = Math.log(2) / doublingTime;
        }
        constExpConst.setGrowthRate(growthRate);
        constExpConst.setN0(N0);
        constExpConst.setN1(N1);
        constExpConst.setTime1(time);
        return constExpConst;
    }

    private final Parameter N0Parameter;

    private final Parameter N1Parameter;

    private final Parameter timeParameter;

    private final Parameter growthRateParameter;

    private final ConstExpConst constExpConst;

    private final boolean usingGrowthRate;
}
