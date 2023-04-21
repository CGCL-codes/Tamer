package org.shava.core.sa;

import org.shava.core.sa.contexts.CheckVariableDefinitionsContext;
import org.shava.core.sa.contexts.ScriptType;
import org.shava.core.sa.contexts.VariableType;
import org.shava.syntax.ast.Argument;
import org.shava.syntax.ast.ScriptDefinition;

public class VariablesDefinitionsValidator implements ScriptDefinitionValidator {

    public void validate(final ScriptDefinition sd) {
        final CheckVariableDefinitionsContext context = new CheckVariableDefinitionsContext();
        context.addSymbol(sd.getScriptName(), new ScriptType(sd));
        for (Argument a : sd.getArgs()) {
            context.addSymbol(a.getScriptNameValue(), new VariableType());
        }
        for (ScriptDefinition s : sd.getLetDefinitions()) {
            context.addSymbol(s.getScriptName(), new ScriptType(s));
        }
        for (ScriptDefinition s : sd.getLetDefinitions()) {
            final CheckVariableDefinitionsContext newContext = context.createNewContext();
            s.getScript().accept(new CheckVariableDefinitionsVisitor(), newContext);
        }
        sd.getScript().accept(new CheckVariableDefinitionsVisitor(), context);
    }
}
