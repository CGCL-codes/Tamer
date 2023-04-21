package org.vikamine.kernel.formula;

import java.util.List;
import org.vikamine.kernel.formula.exception.ASTBuildingException;

/**
 * {@link ASTBuilder} is a base class for building ASTs.
 * 
 * @author Tobias Vogele
 */
public interface ASTBuilder {

    /**
     * Creates a abstract syntax tree out of a List of parserElements
     * 
     * @param parserElements
     * @return
     */
    ParserElement buildAST(List parserElements) throws ASTBuildingException;
}
