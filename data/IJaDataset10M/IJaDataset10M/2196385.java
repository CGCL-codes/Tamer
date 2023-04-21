package org.akrogen.tkui.css.core.sac;

import java.util.Stack;
import org.w3c.css.sac.DocumentHandler;

/**
 * Extends {@link DocumentHandler} to get the root node.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public interface ExtendedDocumentHandler extends DocumentHandler {

    /**
	 * Return root node.
	 * 
	 * @return
	 */
    public Object getNodeRoot();

    /**
	 * Set node stack.
	 * 
	 * @param statck
	 */
    public void setNodeStack(Stack statck);
}
