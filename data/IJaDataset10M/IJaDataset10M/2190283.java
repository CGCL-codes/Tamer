package progranet.model.service.tags;

import java.text.ParseException;
import progranet.omg.core.types.DataType;
import progranet.omg.ocl.expression.Compiler;
import progranet.omg.ocl.expression.OclException;
import progranet.omg.ocl.expression.OclExpression;

public abstract class ScriptletTag extends Tag {

    protected OclExpression script;

    protected ScriptletTag(OclExpression script, CompilationContext compilationContext) {
        this.script = script;
    }

    public static ScriptletTag compile(String script, CompilationContext compilationContext) throws OclException, ParseException {
        OclExpression expression = Compiler.compile(script, compilationContext.getCompilationContext());
        return (expression.getType() instanceof DataType) ? new ScriptletTagForData(expression, compilationContext) : new ScriptletTagForElement(expression, compilationContext);
    }
}
