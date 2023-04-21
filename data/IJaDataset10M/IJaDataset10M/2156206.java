package unbbayes.gui.table.extension;

import javax.swing.JPanel;
import unbbayes.prs.INode;
import unbbayes.prs.Node;

/**
 * This is an interface to handle probability distribution's
 * graphical edition for nodes loaded from plugin manager.
 * It carries basically a node (INode) and builds a JPanel
 * to edit its probability function.
 * Plugins must implement this interface in order to 
 * permit probability function's visual edition.
 * 
 * @author Shou Matsumoto
 *
 * @version 2010/01/01 : currently, this class is instantiated when we click the "new plugin node" button
 * (a split button containing currently loaded plugin nodes)
 *
 */
public interface IProbabilityFunctionPanelBuilder {

    /**
	 * Sets the node owning the probability distribution function.
	 * This method is expected to change the JPanel's content
	 * @param node
	 */
    public void setProbabilityFunctionOwner(Node node);

    /**
	 * Gets the node currently owning the probability distribution function
	 * @return
	 */
    public Node getProbabilityFunctionOwner();

    /**
	 * Obtains the JPanel used to edit the node's probability function
	 * graphically.
	 * @return : a JPanel used to edit node's probability function
	 */
    public JPanel buildProbabilityFunctionEditionPanel();
}
