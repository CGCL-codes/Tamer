package edu.princeton.wordnet.browser.tree.renderers.domw3c;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.w3c.dom.Node;

/**
 * Decorates text node
 * 
 * @author <a href="mailto:bbou@ac-toulouse.fr">Bernard Bou</a>
 */
public class TextDecorator extends DefaultDecorator {

    /**
	 * Icon
	 */
    private static ImageIcon theIcon;

    /**
	 * Initialize
	 */
    static {
        TextDecorator.theIcon = new ImageIcon(TextDecorator.class.getResource("images/treetext.gif"));
    }

    /**
	 * Constructor
	 * 
	 * @param thisNode
	 *            node to decorate
	 */
    public TextDecorator(final Node thisNode) {
        super(thisNode);
    }

    /**
	 * Get icon for node
	 * 
	 * @return icon
	 */
    @Override
    public Icon getIcon() {
        return TextDecorator.theIcon;
    }
}
