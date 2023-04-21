package foucault.calculations;

public class VariableTemplate {

    private final VScope scope;

    private final String name;

    private final String label;

    private final VType type;

    private Double scale;

    private Double min;

    private Double max;

    private VUnit[] unit;

    private Object defaultValue;

    private CalculationTemplate calculationTemplate;

    public VariableTemplate(VScope scope, String name, String label, VType type) {
        this.name = name;
        this.label = label;
        this.scope = scope;
        this.type = type;
    }

    public VariableTemplate setScale(double scale) {
        this.scale = scale;
        return this;
    }

    public VariableTemplate setMinMax(double min, double max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public VariableTemplate setMin(double min) {
        this.min = min;
        return this;
    }

    public VariableTemplate setUnit(VUnit... unit) {
        this.unit = unit;
        return this;
    }

    public VariableTemplate setDefault(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public VariableTemplate setCalculationTemplate(CalculationTemplate calculationTemplate) {
        this.calculationTemplate = calculationTemplate;
        return this;
    }
}
