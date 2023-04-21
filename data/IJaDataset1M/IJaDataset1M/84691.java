package vocal;

import java.awt.Color;
import prefuse.Constants;
import prefuse.util.ColorLib;

/**
 * 
 * Default configuration values for VoCAL
 * 
 * @author <code>fnaufel@gmail.com</code>
 * @version 2007-12-02
 *
 */
public class VocalConfig {

    /**
	 * Arc width of rounded corners of node labels of concept trees
	 */
    public static int nodeLabelRoundedArcWidth = 8;

    /**
	 * Arc height of rounded corners of node labels of concept trees
	 */
    public static int nodeLabelRoundedArcHeight = 8;

    /**
	 * Horizontal alignment of text in node labels of concept trees 
	 */
    public static int nodeLabelHorizontalTextAlignment = prefuse.Constants.LEFT;

    /**
	 * Type of node shape drawn in concept tree: outline or fill (or none or both) 
	 */
    public static int nodeLabelRenderType = prefuse.render.AbstractShapeRenderer.RENDER_TYPE_FILL;

    /**
	 * Horizontal alignment of node labels of concept trees with respect to their x, y coordinates.
	 */
    public static int nodeLabelHorizontalAlignment = prefuse.Constants.LEFT;

    /**
	 * Horizontal padding in pixels between content and border of node labels of concept trees
	 */
    public static int nodeLabelHorizontalPadding = 5;

    /**
	 * Vertical padding in pixels between content and border of node labels of concept trees
	 */
    public static int nodeLabelVerticalPadding = 5;

    /**
	 * Orientation of concept tree
	 */
    public static int treeOrientation = Constants.ORIENT_LEFT_RIGHT;

    /**
	 * Spacing to maintain between depth levels of the tree
	 */
    public static double levelSpacing = 20;

    /**
	 * Spacing to maintain between sibling nodes
	 */
    public static double siblingSpacing = 20;

    /**
	 * Spacing to maintain between neighboring subtrees
	 */
    public static double subtreeSpacing = 5;

    /**
	 * Background color of main panel of UI  
	 */
    public static Color mainPanelBackgroundColor = Color.WHITE;

    /**
	 * Foreground color of main panel of UI  
	 */
    public static Color mainPanelForegroundColor = Color.BLACK;

    /**
	 * Fill color of named class nodes
	 */
    public static int namedClassNodeColor = ColorLib.color(Color.orange);

    /**
	 * Fill color of not nodes
	 */
    public static int notNodeColor = ColorLib.color(Color.pink);

    /**
	 * Fill color of or nodes
	 */
    public static int orNodeColor = ColorLib.color(Color.green);

    /**
	 * Fill color of and nodes
	 */
    public static int andNodeColor = ColorLib.color(Color.lightGray);

    /**
	 * Fill color of only nodes
	 */
    public static int onlyNodeColor = ColorLib.color(Color.yellow);

    /**
	 * Fill color of some nodes
	 */
    public static int someNodeColor = ColorLib.color(Color.cyan);

    /**
	 * Fill color of unknown nodes
	 */
    public static int unknownNodeColor = ColorLib.color(Color.magenta);

    /**
	 * Text color of node labels
	 */
    public static int nodeTextColor = ColorLib.color(Color.BLACK);

    /**
	 * Edge color
	 */
    public static int edgeColor = ColorLib.rgb(200, 200, 200);
}
