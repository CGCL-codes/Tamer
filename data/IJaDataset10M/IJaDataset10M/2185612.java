package com.newisys.dv.ifgen.schema;

import java.util.List;

/**
 * Ifgen schema object for template instances.
 * 
 * @author Jon Nall
 */
public final class IfgenTemplateInst extends IfgenNamedTestbenchMember {

    private final IfgenName instanceName;

    private final IfgenUnresolvedName templateName;

    private final IfgenTemplateKind kind;

    private final List<IfgenExpression> arguments;

    private IfgenTemplate template;

    public IfgenTemplateInst(IfgenSchema schema, IfgenUnresolvedName templateName, IfgenName instanceName, IfgenTemplateKind kind, List<IfgenExpression> arguments) {
        super(schema, instanceName);
        this.instanceName = instanceName;
        this.templateName = templateName;
        this.kind = kind;
        this.arguments = arguments;
    }

    public List<IfgenExpression> getArgs() {
        return arguments;
    }

    public IfgenTemplateKind getTemplateKind() {
        return kind;
    }

    public IfgenName getInstanceName() {
        return instanceName;
    }

    public IfgenUnresolvedName getUnresolvedTemplateName() {
        return templateName;
    }

    public void setTemplate(IfgenTemplate template) {
        this.template = template;
    }

    public IfgenTemplate getTemplate() {
        return template;
    }
}
