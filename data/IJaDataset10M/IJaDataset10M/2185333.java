package com.volantis.xml.pipeline.sax.impl.template.parameter;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableQName;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.impl.template.TValue;
import com.volantis.xml.pipeline.sax.impl.template.TemplateExpressionValue;
import com.volantis.xml.pipeline.sax.impl.template.TemplateModel;
import org.xml.sax.SAXException;

/**
 * Action that will create a variable after processing the body of the
 * parameter element.
 */
public class CreateVariableAction extends EndParameterAction {

    /**
     * The name of the variable.
     */
    private final String variable;

    /**
     * The name of the parameter for which the variable is to be created.
     */
    private final String parameter;

    /**
     * Initialise.
     *
     * @param model     The template model.
     * @param variable  The name of the variable.
     * @param parameter The name of the parameter for which the variable is to
     *                  be created.
     */
    public CreateVariableAction(TemplateModel model, String variable, String parameter) {
        super(model);
        this.variable = variable;
        this.parameter = parameter;
    }

    public void doAction(DynamicProcess dynamicProcess) throws SAXException {
        TValue value = model.getParameterBlock().query(parameter);
        XMLPipelineContext pipelineContext = dynamicProcess.getPipelineContext();
        ExpressionContext context = pipelineContext.getExpressionContext();
        NamespacePrefixTracker namespacePrefixTracker = context.getNamespacePrefixTracker();
        ExpandedName name = namespacePrefixTracker.resolveQName(new ImmutableQName(variable), "");
        TemplateExpressionValue initialValue = new TemplateExpressionValue(parameter, value, pipelineContext);
        model.addPendingVariableDeclaration(name, initialValue);
        super.doAction(dynamicProcess);
    }
}
