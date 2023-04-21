package org.thymeleaf.expression;

import java.util.Map;
import org.thymeleaf.Arguments;
import org.thymeleaf.templateresolver.TemplateResolution;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public interface IExpressionEvaluator {

    public Object evaluate(final Arguments arguments, final TemplateResolution templateResolution, final String expression, final Object root);

    public Object evaluate(final Arguments arguments, final TemplateResolution templateResolution, final String expression, final Object root, final Map<String, Object> additionalContextVariables);
}
