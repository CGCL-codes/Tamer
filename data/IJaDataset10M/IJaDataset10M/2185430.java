package Renderer.HTMLDrawer;

import Tree.Node;
import Renderer.*;

/**
 * This class processes all TextFormat tag Nodes.
 * @author Nathan Scully
 * @version FINAL
 */
public class TextFormat extends Drawer {

    /**
     * The Constuctor for this class, which calls the Drawer superclass and
     * passes the node and renderer to it.
     * @param node The Node to be passed to Drawer.
     * @param renderer The Renderer to be passed to Drawer.
     */
    public TextFormat(Node node, HTMLRenderer renderer) {
        super(node, renderer);
    }

    /**
     * The overwritten draw method. This creates a clone of the current TextProperty
     * object and updates it with the new formatting options that were specified
     * by the tag. This new TextProperty object is then pushed onto the Stack.
     */
    public void draw() {
        String token = node.getToken().toLowerCase();
        TextProperty textProperty = (TextProperty) (stack.peek().clone());
        if (token.equals("b")) {
            textProperty.setBold(true);
        } else if (token.equals("i")) {
            textProperty.setItalic(true);
        } else if (token.equals("u")) {
            textProperty.setUnderlined(true);
        }
        stack.Push(textProperty);
    }

    /**
     * The overwritten postProcess method. When a close tag is found, this
     * will remove the TextPropery object that was created in draw() from the
     * Stack, effectively undoing what draw() did.
     */
    public void postProcess() {
        stack.pop();
    }
}
