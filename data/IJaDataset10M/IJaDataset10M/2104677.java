package org.apache.shindig.gadgets.templates;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.google.inject.ImplementedBy;
import javax.el.ELResolver;

/**
 * A Template Processor can process templates and evaluate expressions.
 */
@ImplementedBy(DefaultTemplateProcessor.class)
public interface TemplateProcessor {

    /**
   * Process an entire template.
   * 
   * @param template the DOM template, typically a script element
   * @param templateContext a template context providing top-level
   *     variables
   * @param globals ELResolver providing global variables other
   *     than those in the templateContext
   * @return a document fragment with the resolved content
   */
    DocumentFragment processTemplate(Element template, TemplateContext templateContext, ELResolver globals, TagRegistry registry);

    /**
   * @return the current template context.
   */
    TemplateContext getTemplateContext();

    /**
   * Process the children of an element or document.
   * @param result the node to which results should be appended
   * @param source the node whose children should be processed
   */
    void processChildNodes(Node result, Node source);

    void processRepeat(Node result, Element element, Iterable<?> dataList, Runnable onEachLoop);

    /**
   *  Evaluates an expression within the scope of this processor's context.
   *  @param expression The String expression
   *  @param type Expected result type
   *  @param defaultValue Default value to return 
   */
    <T> T evaluate(String expression, Class<T> type, T defaultValue);
}
