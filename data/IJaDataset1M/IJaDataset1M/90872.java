package astcentric.structure.vl.basic;

import java.util.List;
import astcentric.structure.basic.Node;
import astcentric.structure.bl.CalculationContext;
import astcentric.structure.bl.Data;
import astcentric.structure.bl.Function;
import astcentric.structure.bl.Util;

class CompileDataNodeCalculator extends AbstractCalculatorWithNodeArguments {

    private final Resources _resources;

    CompileDataNodeCalculator(Resources resources) {
        _resources = resources;
    }

    protected Data doCalculate(CalculationContext calculationContext, List<Function> arguments) {
        Util.checkArguments(arguments, 1);
        Node node = getNodeData(arguments, 0).getNode();
        return _resources.getCompiler().compileData(node);
    }
}
