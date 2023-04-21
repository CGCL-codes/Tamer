package com.enerjy.analyzer.java;

/**
 * Interface for describing whether and how a problem can be fixed.
 */
public interface IFixCapability {

    /**
     * @return Whether or not the problem can be escaped with a magic comment.
     */
    boolean canBeEscaped();

    /**
     * Obtain the fixes to a problem within the given context.
     * 
     * @param context Context in which the fix will be applied.
     * @return An array of operations that can be applied to fix the problem. Return an empty or <tt>null</tt> array if no
     *         operations apply.
     */
    IFixOperation[] getFixes(IFixContext context);
}
