package nl.utwente.ewi.tpl.ast.tree;

import java.util.Map;
import nl.utwente.ewi.tpl.Target;
import nl.utwente.ewi.tpl.context.ContextualException;
import nl.utwente.ewi.tpl.runtime.MultiplicityType;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

public abstract class Parameter extends Node {

    protected int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public abstract void check(Target target, Map<String, NodeDef> knownTypes, Map<String, Parameter> knownParameters, boolean superStructureDefined, boolean first) throws ContextualException;

    public abstract StringTemplate generateField(StringTemplateGroup templates);

    public abstract StringTemplate generateGetSet(StringTemplateGroup templates);

    public abstract String getParameterName();

    public abstract String getParameterOriginalName();

    public abstract String getParameterType();

    public abstract MultiplicityType getParameterMultiplicity();

    public abstract NodeDef getTypeNode();

    public String getMultiplicityTemplateName() {
        return getParameterMultiplicity().getMultiplicityTemplateName();
    }
}
