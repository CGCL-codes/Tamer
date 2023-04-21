package org.thymeleaf.processor.tag;

import java.util.Collections;
import java.util.Map;
import org.thymeleaf.Arguments;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractSelectionTargetTagProcessor extends AbstractTagProcessor {

    public AbstractSelectionTargetTagProcessor() {
        super();
    }

    public final TagProcessResult process(final Arguments arguments, final TemplateResolution templateResolution, final Document document, final Element element) {
        final Object newSelectionTarget = getNewSelectionTarget(arguments, templateResolution, document, element);
        final Map<String, Object> additionalLocalVariables = getAdditionalLocalVariables(arguments, templateResolution, document, element);
        if (additionalLocalVariables == null || additionalLocalVariables.isEmpty()) {
            return TagProcessResult.forRemoveTagWithSelectionTarget(newSelectionTarget);
        }
        return TagProcessResult.forRemoveTagWithSelectionTarget(additionalLocalVariables, newSelectionTarget);
    }

    @SuppressWarnings("unused")
    protected Map<String, Object> getAdditionalLocalVariables(final Arguments arguments, final TemplateResolution templateResolution, final Document document, final Element element) {
        return Collections.emptyMap();
    }

    protected abstract Object getNewSelectionTarget(final Arguments arguments, final TemplateResolution templateResolution, final Document document, final Element element);
}
