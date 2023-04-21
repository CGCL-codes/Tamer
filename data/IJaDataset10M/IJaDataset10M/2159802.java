package net.sf.javascribe.generator.operations.java14;

import java.util.HashMap;
import net.sf.javascribe.ProcessingException;
import net.sf.javascribe.generator.Operation;
import net.sf.javascribe.generator.context.processor.CodeExecutionContext;
import net.sf.javascribe.generator.context.processor.JavaCodeExecutionContext;
import net.sf.javascribe.generator.java.JavaCodeSnippet;

/**
 * @author User
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TryOperation implements Operation {

    JavaCodeExecutionContext execCtx = null;

    public void setParameters(HashMap<String, String> parameters) {
    }

    public void init(CodeExecutionContext ctx) throws ProcessingException {
        execCtx = (JavaCodeExecutionContext) ctx;
    }

    public JavaCodeSnippet startTag() {
        JavaCodeSnippet ret = new JavaCodeSnippet();
        ret.getSource().append("try {\n");
        return ret;
    }

    public JavaCodeSnippet endTag() {
        JavaCodeSnippet ret = new JavaCodeSnippet();
        ret.getSource().append("}\n");
        return ret;
    }
}
