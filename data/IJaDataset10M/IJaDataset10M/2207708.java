package edu.iastate.cs.designlab.testutilities;

import AST.Frontend;

/**
 * Create compilers that parse, translate and compile
 * @author Sean Mooney
 *
 */
public interface AbstractCompilerFactory {

    public Frontend makeParser();

    public Frontend makeCompiler();

    public Frontend makeTranslator();

    public Frontend makeTreeDumper();
}
