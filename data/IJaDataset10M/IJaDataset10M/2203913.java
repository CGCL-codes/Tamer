package org.jarp.gui.jhotdraw.figures;

import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Interface for XML loading/saving.
 * All components must implement this interface,
 * should they need to be saved in an XML document.
 * The methods present a simple interface:
 * - createNode asks the component to render a Node
 *   containing all relevant information about itself;
 * - parseNode does the exact opposite, where the component
 *   must pick up the relevant information from a XML
 *   document node.
 *
 * Creation date: (8/11/2001 12:23:38)
 * 
 * @version $Revision: 1.1 $
 * @author <a href="mailto:ricardo_padilha@users.sourceforge.net">Ricardo 
 * Sangoi Padilha</a>
 */
public interface XMLStorable {

    /**
	 * Implementors must render a Node containing all relevant information,
	 * according to PNML definiton, if possible.
	 * @param doc
	 * @return Node
	 * @throws IOException if it's not possible the render the Node
	 */
    public Node createNode(Document doc) throws IOException;

    /**
	 * Implementors must retrieve all relevant information from a Node,
	 * according to PNML definiton, if possible.
	 * @param node
	 * @throws IOException if the Node has an invalid syntax
	 */
    public void parseNode(Node node) throws IOException;
}
