package org.catapult.web.template.config;

import java.util.List;
import java.util.Stack;
import org.catapult.web.config.DefaultElementDefinition;
import org.catapult.web.config.ElementDefinition;
import org.catapult.web.config.NodeDefinition;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.support.ManagedList;

/**
 * @author aku
 * @version $Rev: 149 $ $Date: 2008-11-18 13:08:05 -0500 (Tue, 18 Nov 2008) $
 */
public class TemplateDefinition extends DefaultElementDefinition {

    /**
     * 
     */
    private static final long serialVersionUID = 8163691843048625841L;

    private static final ThreadLocal<Stack<TemplateDefinition>> templateStack = new InheritableThreadLocal<Stack<TemplateDefinition>>();

    /**
     * @param name
     */
    public TemplateDefinition() {
        super("template");
        setCurrentInstance(this);
        init();
    }

    private void init() {
        ManagedList overrides = new ManagedList();
        overrides.setMergeEnabled(true);
        getPropertyValues().addPropertyValue("overrides", overrides);
        ManagedList content = new ManagedList();
        content.setMergeEnabled(true);
        getPropertyValues().addPropertyValue("content", content);
    }

    private static Stack<TemplateDefinition> currentTemplateStack() {
        Stack<TemplateDefinition> stack = templateStack.get();
        if (stack == null) {
            stack = new Stack<TemplateDefinition>();
            templateStack.set(stack);
        }
        return stack;
    }

    /**
     * @return the currentInstance
     */
    public static TemplateDefinition getCurrentInstance() {
        return currentTemplateStack().isEmpty() ? null : currentTemplateStack().peek();
    }

    /**
     * @param currentInstance
     *            the currentInstance to set
     */
    private static void setCurrentInstance(TemplateDefinition currentInstance) {
        currentTemplateStack().push(currentInstance);
    }

    /**
     * 
     */
    public void release() {
        currentTemplateStack().pop();
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ElementDefinition> getOverrides() {
        return (List<ElementDefinition>) getPropertyValues().getPropertyValue("overrides").getValue();
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<NodeDefinition> getContent() {
        return (List<NodeDefinition>) getPropertyValues().getPropertyValue("content").getValue();
    }

    public boolean isTemplate() {
        boolean isTemplate = false;
        PropertyValue pv = getPropertyValues().getPropertyValue("template");
        if (pv != null) {
            Boolean pvValue = (Boolean) pv.getValue();
            isTemplate = pvValue == null ? false : pvValue.booleanValue();
        }
        return isTemplate;
    }

    public void setTemplate(boolean template) {
        getPropertyValues().addPropertyValue("template", Boolean.valueOf(template));
    }
}
