package net.sf.javascribe.generator.accessor;

import net.sf.javascribe.ProcessingException;
import net.sf.javascribe.generator.context.processor.JavaCodeExecutionContext;
import net.sf.javascribe.generator.java.JavaCodeSnippet;
import net.sf.javascribe.generator.util.ValueExpression;

/**
 * @author User
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IntegerTypeAccessor extends JavaTypeAccessor {

    public JavaCodeSnippet setVar(String varName, String expression, JavaCodeExecutionContext execCtx) throws ProcessingException;

    /**
	 * Given the specified value expression, find the code that represents it.
	 * @param expr The expression to find the code expression for.
	 * @param execCtx Current Java code execution context.
	 * @return String code for the given expression.
	 */
    public String evaluateExpression(ValueExpression expr, JavaCodeExecutionContext execCtx);
}
