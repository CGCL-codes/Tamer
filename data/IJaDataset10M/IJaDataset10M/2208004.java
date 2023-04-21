package net.sf.ngrease.core.metalanguage;

import net.sf.ngrease.core.ast.Element;
import net.sf.ngrease.core.ast.ElementIterator;
import net.sf.ngrease.core.ast.ElementList;
import net.sf.ngrease.core.ast.NgreaseException;

public class TransformExpression extends BaseExpression {

    private final Element source;

    private final ElementList targets;

    public TransformExpression(Element expressionElement, Element source, ElementList targets) {
        super(expressionElement);
        this.source = source;
        this.targets = targets;
    }

    public EvaluationResult evaluate(EvaluationContext context, ServiceConfiguration serviceConfiguration) {
        TransformerFactory transformerFactory = context.getTransformerFactory();
        Element evaluatedSource = metaEvaluateToElement(source, context, serviceConfiguration);
        Element currentSource = evaluatedSource;
        for (ElementIterator iter = targets.elementIterator(); iter.hasNext(); ) {
            Element target = iter.nextElement();
            Element evaluatedTarget = metaEvaluateToElement(target, context, serviceConfiguration);
            EvaluationContext subContext = getContextForTransformerExpression(context, currentSource, serviceConfiguration);
            Element transformerExpressionElement = getTransformerExpressionElement(transformerFactory, currentSource, evaluatedTarget);
            if (iter.hasNext()) {
                currentSource = metaEvaluateToElement(transformerExpressionElement, subContext, serviceConfiguration);
            } else {
                return nonConstant(transformerExpressionElement, subContext);
            }
        }
        throw new IllegalStateException("Shouldn't happen!");
    }

    private Element getTransformerExpressionElement(TransformerFactory transformerFactory, Element source, Element target) {
        Element transformerExpressionElement = transformerFactory.createTransformer(source, target);
        if (transformerExpressionElement == null) {
            throw new NgreaseException("Don't know how to transform to\n" + target + "from\n" + source);
        }
        return transformerExpressionElement;
    }

    private EvaluationContext getContextForTransformerExpression(EvaluationContext context, Element source, ServiceConfiguration serviceConfiguration) {
        Expression evaluateSourceExpression = new ConstantExpression(source);
        ExpressionBuilderSelector expressionBuilderSelector = new ExpressionBuilderFixedExpressionImpl("source", evaluateSourceExpression);
        EvaluationContext subContext = serviceConfiguration.getEvaluationContextBuilder().buildEvaluationContext(context, expressionBuilderSelector);
        return subContext;
    }
}
