package dr.app.beauti.options;

import dr.app.beauti.types.PriorScaleType;
import dr.evolution.datatype.DataType;
import dr.math.MathUtils;
import java.util.List;

/**
 * @author Alexei Drummond
 * @author Andrew Rambaut
 * @author Walter Xie
 */
public abstract class PartitionOptions extends ModelOptions {

    protected String partitionName;

    protected final BeautiOptions options;

    protected double[] avgRootAndRate = new double[] { 1.0, 1.0 };

    public PartitionOptions(BeautiOptions options) {
        this.options = options;
    }

    public PartitionOptions(BeautiOptions options, String name) {
        this.options = options;
        this.partitionName = name;
        initModelParametersAndOpererators();
    }

    protected abstract void initModelParametersAndOpererators();

    protected abstract void selectParameters(List<Parameter> params);

    protected abstract void selectOperators(List<Operator> ops);

    public abstract String getPrefix();

    protected void createParameterTree(PartitionOptions options, String name, String description, boolean isNodeHeight, double value) {
        new Parameter.Builder(name, description).isNodeHeight(isNodeHeight).scaleType(PriorScaleType.TIME_SCALE).isNonNegative(true).initial(value).partitionOptions(options).build(parameters);
    }

    protected void createAllMusParameter(PartitionOptions options, String name, String description) {
        new Parameter.Builder(name, description).partitionOptions(options).build(parameters);
    }

    public Parameter getParameter(String name) {
        Parameter parameter = parameters.get(name);
        if (parameter == null) {
            throw new IllegalArgumentException("Parameter with name, " + name + ", is unknown");
        }
        parameter.setPrefix(getPrefix());
        autoScale(parameter);
        return parameter;
    }

    public Operator getOperator(String name) {
        Operator operator = operators.get(name);
        if (operator == null) throw new IllegalArgumentException("Operator with name, " + name + ", is unknown");
        operator.setPrefix(getPrefix());
        return operator;
    }

    public String getName() {
        return partitionName;
    }

    public void setName(String name) {
        this.partitionName = name;
    }

    public String toString() {
        return getName();
    }

    public DataType getDataType() {
        return options.getDataPartitions(this).get(0).getDataType();
    }

    public double[] getAvgRootAndRate() {
        return avgRootAndRate;
    }

    public void setAvgRootAndRate() {
        this.avgRootAndRate = options.clockModelOptions.calculateInitialRootHeightAndRate(options.getDataPartitions(this));
    }

    protected void autoScale(Parameter param) {
        double avgInitialRootHeight = avgRootAndRate[0];
        double avgInitialRate = avgRootAndRate[1];
        double birthRateMaximum = 1E6;
        birthRateMaximum = 1E6 * avgInitialRate;
        if (!param.isPriorEdited()) {
            switch(param.scaleType) {
                case TIME_SCALE:
                    param.initial = avgInitialRootHeight;
                    break;
                case LOG_TIME_SCALE:
                    param.initial = Math.log(avgInitialRootHeight);
                    break;
                case T50_SCALE:
                    param.initial = avgInitialRootHeight / 5.0;
                    break;
                case GROWTH_RATE_SCALE:
                    param.initial = avgInitialRootHeight / 1000;
                    if (param.getBaseName().startsWith("logistic")) {
                        param.scale = Math.log(1000) / avgInitialRootHeight;
                    } else {
                        param.scale = Math.log(10000) / avgInitialRootHeight;
                    }
                    break;
                case BIRTH_RATE_SCALE:
                    param.initial = MathUtils.round(1 / options.treeModelOptions.getExpectedAvgBranchLength(avgInitialRootHeight), 2);
                    break;
                case ORIGIN_SCALE:
                    param.initial = MathUtils.round(avgInitialRootHeight * 1.1, 2);
                    break;
                case SUBSTITUTION_RATE_SCALE:
                    param.initial = avgInitialRate;
                    break;
                case LOG_STDEV_SCALE:
                    break;
                case SUBSTITUTION_PARAMETER_SCALE:
                    break;
                case ROOT_RATE_SCALE:
                    param.initial = avgInitialRate;
                    param.shape = 0.5;
                    param.scale = param.initial / 0.5;
                    break;
                case LOG_VAR_SCALE:
                    param.initial = avgInitialRate;
                    param.shape = 2.0;
                    param.scale = param.initial / 2.0;
                    break;
            }
        }
    }

    public BeautiOptions getOptions() {
        return options;
    }
}
