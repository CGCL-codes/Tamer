package org.sgmiller.formicidae.compiler;

/**
 * Simple exception sub-class to denote a formicidae compiler error.  These are typically 
 * (and alas, frequently) used for user syntax errors in ant source, but are sometimes 
 * also employed to convey unexpected (and hopefully rare) internal programming errors
 * in the compiler program itself.  The exception messages the compiler sets will 
 * clearly indicate which is the case, and are typically quite verbose and detailed.
 * 
 * @author evoke
 * @since 1.4, Nov 28 2005
 */
public class FormicidaeCompilerException extends java.lang.Exception {

    /**
   * Privatized to ban default construction, an exception message is required.
   */
    private FormicidaeCompilerException() {
        ;
    }

    /**
   * Recommended constructor.
   * 
   * @param message The exception message, preferrably verbose with detailed context.
   */
    public FormicidaeCompilerException(String message) {
        super(message);
    }
}
