package edu.cmu.cs.euklas.visitors.target;

import java.util.LinkedList;
import org.eclipse.wst.jsdt.core.dom.ASTVisitor;
import edu.cmu.cs.euklas.dataobjects.SourceCodePosition;
import edu.cmu.cs.euklas.visitors.TargetVisitor;

/**
 * This abstract class is part of the strategy pattern
 * which is used for the implementation of the visitor
 * concept for visiting AST nodes. All specific target 
 * node visitors must be derived from this class.
 * 
 * see: http://www.eclipse.org/articles/article.php?file=Article-JavaCodeManipulation_AST/index.html
 * for additional information.
 * 
 * @author Christian Doerner
 * 
 */
public abstract class AbstractVisitor extends ASTVisitor {

    protected TargetVisitor targetVisitor;

    private LinkedList<SourceCodePosition> sourceCodePositionList;

    /**
	 * Standard constructor
	 *
	 * @param tvis A target visitor	
	 * @param sourceCodePositionList A list of source code positions connected to this target
	 */
    public AbstractVisitor(TargetVisitor tvis, LinkedList<SourceCodePosition> sourceCodePositionList) {
        this.targetVisitor = tvis;
        this.sourceCodePositionList = sourceCodePositionList;
    }

    /**
	 * @return the sourceCodePositionList
	 */
    public LinkedList<SourceCodePosition> getSourceCodePositionList() {
        return sourceCodePositionList;
    }
}
