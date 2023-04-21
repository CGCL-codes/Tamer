package astcentric.structure.validation;

import java.util.List;
import astcentric.structure.basic.Node;
import astcentric.structure.basic.NodeCollection;
import astcentric.structure.basic.NodeTool;
import astcentric.structure.query.NodeCollector;
import astcentric.structure.tool.AbstractCompiler;
import astcentric.structure.tool.Compiler;

abstract class AbstractNodeValidatorFactoryWithFactory extends AbstractCompiler<ExtendedNodeValidator, ValidatorCompilationContext> {

    private final ValidationSpecificationNodes _specificationNodes;

    protected final Compiler<ExtendedNodeValidator, ValidatorCompilationContext> _validatorCompiler;

    private final Compiler<NodeCollector, ValidatorCompilationContext> _collectorCompiler;

    protected AbstractNodeValidatorFactoryWithFactory(ValidationSpecificationNodes specificationNodes, Compiler<ExtendedNodeValidator, ValidatorCompilationContext> validatorCompiler, Compiler<NodeCollector, ValidatorCompilationContext> collectorCompiler) {
        _specificationNodes = specificationNodes;
        _validatorCompiler = validatorCompiler;
        _collectorCompiler = collectorCompiler;
    }

    protected NodeValidatorCollection collectChildValidators(NodeCollection specification, final ValidatorCompilationContext context) {
        final NodeValidatorCollection result = createNodeValidatorCollection();
        List<Node> children = NodeTool.getNodesOf(specification);
        result.createAndAddValidators(children, context, _specificationNodes, _validatorCompiler, _collectorCompiler);
        return result;
    }

    protected NodeValidatorCollection createNodeValidatorCollection() {
        return new NodeValidatorAnd();
    }
}
