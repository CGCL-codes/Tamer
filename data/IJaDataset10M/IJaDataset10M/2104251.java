package com.potix.web.servlet.dsp;

import java.io.IOException;

/**
 * Defines an interpretation of a DSP page.
 * It is a parsed result of a DSP page by use of {@link Interpreter#parse}.
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public interface Interpretation {

    /** Interprets this interpretation of a DSP page, and generates
	 * the result to the output specified in {@link DSPContext}.
	 *
	 * @param dc the interpreter context; never null.
	 */
    public void interpret(DSPContext dc) throws javax.servlet.ServletException, IOException;
}
