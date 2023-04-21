package progranet.model.service.tags;

import java.text.ParseException;
import java.util.Map;
import progranet.model.exception.ModelException;
import progranet.omg.ocl.expression.Compiler;
import progranet.omg.ocl.expression.OclException;
import progranet.omg.ocl.expression.OclExpression;

public class GanesaTagGet extends GanesaTag {

    private static final String ATTRIBUTE_VALUE = "value";

    private static final String ATTRIBUTE_VAR = "var";

    private OclExpression valueExpression;

    private String varName;

    private CompilationContext innerCompilationContext;

    protected GanesaTagGet(Map<String, String> attributes, CompilationContext compilationContext, Template template) throws ParseException, OclException, ModelException {
        super(attributes, compilationContext, template);
        String value = attributes.get(ATTRIBUTE_VALUE);
        if (value == null) throw new ParseException("Value attribute for 'get' not found", 0);
        attributes.remove(ATTRIBUTE_VALUE);
        this.varName = attributes.get(ATTRIBUTE_VAR);
        if (varName == null) throw new ParseException("Var attribute for 'get' not found", 0);
        attributes.remove(ATTRIBUTE_VAR);
        this.valueExpression = Compiler.compile(value, compilationContext.getCompilationContext());
        progranet.omg.ocl.expression.CompilationContext innerOclContext = new progranet.omg.ocl.expression.CompilationContext(compilationContext.getCompilationContext());
        innerOclContext.addVariable(this.varName, valueExpression.getType());
        this.innerCompilationContext = new CompilationContext(compilationContext, innerOclContext);
        this.content = compilationContext.getReportService().parse(template, this.innerCompilationContext);
        checkUnexpectedAttributes(attributes, compilationContext);
    }

    @Override
    public String execute(ExecutionContext executionContext) throws OclException, ModelException, ParseException {
        if (!this.isRendered(executionContext)) return "";
        Object value = this.valueExpression.execute(executionContext.getExecutionContext());
        progranet.omg.ocl.expression.ExecutionContext innerOclContext = new progranet.omg.ocl.expression.ExecutionContext(executionContext.getExecutionContext());
        innerOclContext.setVariable(this.varName, value);
        ExecutionContext innerExecutionContext = new ExecutionContext(executionContext, innerOclContext, this.innerCompilationContext);
        return this.verbatim(executionContext, this.content.execute(innerExecutionContext));
    }
}
