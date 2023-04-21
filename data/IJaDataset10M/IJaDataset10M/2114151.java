package com.ibm.realtime.flexotask.development.rewriting;

import com.ibm.realtime.flexotask.development.builder.BuildException;
import com.ibm.realtime.flexotask.development.validation.CodeAnalysisResult;
import com.ibm.realtime.flexotask.validation.ValidationContext;

/**
 * Extends the rewriter to enable checking of internal state. 
 */
public class TestCodeRewriter extends CodeRewriter {

    /** The validation context used for the rewriting */
    private ValidationContext context;

    public TestCodeRewriter(ValidationContext context, CodeAnalysisResult analysisResult) throws BuildException {
        super(context, analysisResult);
        this.context = context;
    }

    /**
	 * Returns true if the list of generated guard classes contains a guard class
	 * for the class having the provided name, otherwise false.
	 * @param className
	 * @return
	 * @throws Exception
	 */
    public boolean hasGuardForClass(String className) throws Exception {
        Class c = context.getClassLoader().loadClass(className);
        return guardClasses.containsKey(c);
    }
}
