package net.sf.ngrease.core.metalanguage;

import net.sf.ngrease.core.ast.Element;
import net.sf.ngrease.core.ast.ElementDefaultImpl;
import net.sf.ngrease.core.ast.ElementIterator;
import net.sf.ngrease.core.ast.ElementList;

public class SeparateChildrenExpression extends BaseExpression {

    private final Element ofElement;

    private final Element withElement;

    public SeparateChildrenExpression(Element expressionElement, Element ofElement, Element withElement) {
        super(expressionElement);
        this.ofElement = ofElement;
        this.withElement = withElement;
    }

    public EvaluationResult evaluate(EvaluationContext context, ServiceConfiguration serviceConfiguration) {
        Element evaluatedOf = metaEvaluateToElement(ofElement, context, serviceConfiguration);
        Element evaluatedWith = metaEvaluateToElement(withElement, context, serviceConfiguration);
        ElementList.Builder children = ElementList.Builder.with();
        for (ElementIterator iter = evaluatedOf.getChildren().elementIterator(); iter.hasNext(); ) {
            Element child = iter.nextElement();
            children.element(child);
            if (iter.hasNext()) {
                addSeparators(children, evaluatedWith.getChildren());
            }
        }
        return constant(new ElementDefaultImpl(evaluatedOf.getSymbol(), evaluatedOf.getAttributes(), children.build()));
    }

    private void addSeparators(ElementList.Builder target, ElementList separators) {
        for (ElementIterator iter = separators.elementIterator(); iter.hasNext(); ) {
            Element separator = iter.nextElement();
            target.element(separator);
        }
    }
}
