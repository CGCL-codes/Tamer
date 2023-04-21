package dr.evomodel.coalescent;

import dr.evolution.coalescent.DemographicFunction;
import dr.evolution.coalescent.ExpConstExpDemographic;
import dr.evomodelxml.coalescent.ExpConstExpDemographicModelParser;
import dr.inference.model.Parameter;

/**
 * This class models a two growth-phase demographic with a plateau in the middle
 *
 * @author Alexei Drummond
 * @author Andrew Rambaut
 * @version $Id: ExpConstExpDemographicModel.java,v 1.2 2006/08/18 07:44:25 alexei Exp $
 */
public class ExpConstExpDemographicModel extends DemographicModel {

    /**
     * Construct demographic model with default settings
     */
    public ExpConstExpDemographicModel(Parameter N0Parameter, Parameter N1Parameter, Parameter growthRateParameter, Parameter timeParameter, Parameter relTimeParameter, Type units) {
        this(ExpConstExpDemographicModelParser.EXP_CONST_EXP_MODEL, N0Parameter, N1Parameter, growthRateParameter, timeParameter, relTimeParameter, units);
    }

    /**
     * Construct demographic model with default settings
     */
    public ExpConstExpDemographicModel(String name, Parameter N0Parameter, Parameter N1Parameter, Parameter growthRateParameter, Parameter timeParameter, Parameter relTimeParameter, Type units) {
        super(name);
        expConstExp = new ExpConstExpDemographic(units);
        this.N0Parameter = N0Parameter;
        addVariable(N0Parameter);
        N0Parameter.addBounds(new Parameter.DefaultBounds(Double.POSITIVE_INFINITY, 0.0, 1));
        this.N1Parameter = N1Parameter;
        addVariable(N1Parameter);
        N1Parameter.addBounds(new Parameter.DefaultBounds(Double.POSITIVE_INFINITY, 0.0, 1));
        this.growthRateParameter = growthRateParameter;
        addVariable(growthRateParameter);
        growthRateParameter.addBounds(new Parameter.DefaultBounds(Double.POSITIVE_INFINITY, Double.MIN_VALUE, 1));
        this.timeParameter = timeParameter;
        addVariable(timeParameter);
        timeParameter.addBounds(new Parameter.DefaultBounds(Double.POSITIVE_INFINITY, Double.MIN_VALUE, 1));
        this.relTimeParameter = relTimeParameter;
        addVariable(relTimeParameter);
        relTimeParameter.addBounds(new Parameter.DefaultBounds(1.0, Double.MIN_VALUE, 1));
        setUnits(units);
    }

    public DemographicFunction getDemographicFunction() {
        expConstExp.setN0(N0Parameter.getParameterValue(0));
        double relTime = relTimeParameter.getParameterValue(0);
        double time2 = timeParameter.getParameterValue(0);
        double timeInModernGrowthPhase = time2 * relTime;
        double r = -Math.log(N1Parameter.getParameterValue(0)) / timeInModernGrowthPhase;
        expConstExp.setGrowthRate(r);
        expConstExp.setGrowthRate2(growthRateParameter.getParameterValue(0));
        expConstExp.setTime1(timeInModernGrowthPhase);
        expConstExp.setPlateauTime(time2 - timeInModernGrowthPhase);
        return expConstExp;
    }

    Parameter N0Parameter = null;

    Parameter N1Parameter = null;

    Parameter growthRateParameter = null;

    Parameter timeParameter = null;

    Parameter relTimeParameter = null;

    ExpConstExpDemographic expConstExp = null;
}
