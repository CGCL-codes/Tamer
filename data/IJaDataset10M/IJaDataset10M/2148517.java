package org.thymeleaf.processor.attr;

import java.util.Collections;
import java.util.List;
import org.thymeleaf.Arguments;
import org.thymeleaf.DOMExecution;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractTextChildModifierAttrProcessor extends AbstractChildrenModifierAttrProcessor {

    public AbstractTextChildModifierAttrProcessor() {
        super();
    }

    @Override
    protected final List<Node> getNewChildren(final Arguments arguments, final TemplateResolution templateResolution, final Document document, final Element element, final Attr attribute, final String attributeName, final String attributeValue) {
        final String text = getText(arguments, templateResolution, document, element, attribute, attributeName, attributeValue);
        final Text newNode = document.createTextNode(text == null ? "" : text);
        DOMExecution.setExecutableNode(newNode, false);
        return Collections.singletonList((Node) newNode);
    }

    protected abstract String getText(final Arguments arguments, final TemplateResolution templateResolution, final Document document, final Element element, final Attr attribute, final String attributeName, final String attributeValue);
}
