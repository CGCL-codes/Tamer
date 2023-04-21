package edu.cmu.cs.euklas.problems;

import org.eclipse.wst.jsdt.core.dom.ASTNode;

/**
 * This class represents JavaScript problems that are 
 * related to references that are missing in the 
 * corresponding HTML file, such as IDs and images.
 * 
 * @author Christian Doerner
 *  
 */
public class ReferenceProblem extends JavaScriptProblem {

    private String ID;

    /**
	 * Standard constructor for problems in pure JS files.
	 * 
	 * @param astnode The AST node that this problem should be tied to 
	 * @param target The JavaScript unit that is currently parsed
	 * @param severity The severity of the problem (should be one of the ones defined in the superclass)	 
	 * @param ID The ID related to this problem
	 */
    public ReferenceProblem(ASTNode astnode, String target, int severity, String ID) {
        super(astnode, target, severity);
        this.ID = ID;
    }

    /**
	 * Standard constructor for problems in HTML files. In this
	 * case the offset of the problem's AST node has to be shifted
	 * because it is wrong otherwise.
	 * 
	 * @param astnode The AST node that this problem should be tied to 
	 * @param target The JavaScript unit that is currently parsed
	 * @param severity The severity of the problem (should be one of the ones defined in this class)
	 * @param shiftOffset The amount the offset has to be shifted	 
	 * @param ID The ID related to this problem
	 */
    public ReferenceProblem(ASTNode astnode, String target, int severity, int shiftOffset, String ID) {
        super(astnode, target, severity, shiftOffset);
        this.ID = ID;
    }

    /**
	 * @return the iD
	 */
    public String getID() {
        return ID;
    }

    @Override
    public int getProblemID() {
        return JavaScriptProblem.REFERENCES_PROBLEM_ID;
    }
}
