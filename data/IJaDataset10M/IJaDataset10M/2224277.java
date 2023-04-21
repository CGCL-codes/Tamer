package sg.edu.nus.comp.simTL.engine.tracing;

import sg.edu.nus.comp.simTL.engine.exceptions.InterpreterException;
import sg.edu.nus.comp.simTL.engine.exceptions.SimTLException;

/**
 * Connects two corresponding model elements preferably from two different models
 * @author Marcel Boehme
 */
public class TraceLink {

    private final StaticContext staticContext;

    private final TraceLinkNode leftSideTraceLinkNode;

    private final TraceLinkNode rightSideTraceLinkNode;

    public TraceLink(StaticContext staticContext, TraceLinkNode leftSideTraceLinkNode, TraceLinkNode rightSideTraceLinkNode) throws SimTLException {
        this.leftSideTraceLinkNode = leftSideTraceLinkNode;
        this.rightSideTraceLinkNode = rightSideTraceLinkNode;
        this.staticContext = staticContext;
        if (leftSideTraceLinkNode == null) throw new InterpreterException("leftSideTraceLinkNode is null. TraceLink pointing to nowhere.");
        if (rightSideTraceLinkNode == null) throw new InterpreterException("rightSideTraceLinkNode is null. TraceLink pointing to nowhere.");
        leftSideTraceLinkNode.setPartOfTraceLink(this);
        rightSideTraceLinkNode.setPartOfTraceLink(this);
    }

    /**
	 * Getter<br>
	 * TraceLinks are theoratically bidirectional actually
	 * @return Returns the traceLinkNode of <b>one side</b> of the traceLink(-edge)
	 */
    public TraceLinkNode getLeftSideTraceLinkNode() {
        return leftSideTraceLinkNode;
    }

    /**
	 * Getter<br>
	 * TraceLinks are theoratically bidirectional actually
	 * @return Returns the traceLinkNode of <b>the other side</b> of the traceLink(-edge)
	 */
    public TraceLinkNode getRightSideTraceLinkNode() {
        return rightSideTraceLinkNode;
    }

    /**
	 * Getter<br>
	 * The TraceLinks connects an element in the patternVersion
	 * of the template and another element<br>
	 * The context helps to find the respective patternVersion
	 * @return Returns the staticContext
	 */
    public StaticContext getStaticContext() {
        return staticContext;
    }
}
