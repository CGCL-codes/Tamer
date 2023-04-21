package com.enerjy.analyzer.java;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import com.enerjy.common.jdt.IExternalBindingResolver;

/**
 * Interface for the Java rule manager used by rules to create problems and obtain AST bindings.
 */
public interface IJavaRuleManager {

    /**
     * Construct a problem.
     * 
     * @param ruleKey Unique key of the rule that identified the problem.
     * @param node AST node exhibiting the problem.
     * @param message Message describing the problem.
     * @return the newly created problem.
     */
    JavaProblem addProblem(String ruleKey, ASTNode node, String message);

    /**
     * Obtain a type binding from the AST.
     * 
     * @param jvmSig Full JVM signature of the type binding to acquire (ie: "Ljava/lang/String;")
     * @return The located type binding, or <code>null</code> if not found.
     */
    ITypeBinding resolveType(String jvmSig);

    /**
     * Returns the binding resolver owned by this manager.
     * @return The binding resolver owned by this manager.
     */
    IExternalBindingResolver getResolver();
}
