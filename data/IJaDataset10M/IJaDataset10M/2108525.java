package edu.gsbme.msource.Reference.Element;

import edu.gsbme.MMLParser2.MathML.MEE.MathAST.AST;

/**
 * Source term modification data structure
 * @author David
 *
 */
public class SourceMod extends RefObject {

    public enum SourceModType {

        PLUS, MINUS, DIVIDE, TIMES
    }

    public SourceModType modification_type;

    public AST root;

    public SourceMod(String id) {
        super(id);
    }
}
