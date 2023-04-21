package net.hydromatic.clapham.chart;

/**
 * {@link Chart} options. These options affect how the {@link Chart} is
 * displayed.
 * 
 * @author Edgar Espina
 * @version $Id: $
 */
public interface ChartOptions {

    public enum ChartLayout {

        BEST, LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    /**
     * The default arc size
     */
    int ARC_SIZE = 16;

    /**
     * The default component gap height
     */
    int COMPONENT_GAP_HEIGHT = 10;

    /**
     * The default component gap width
     */
    int COMPONENT_GAP_WIDTH = 32;

    /**
     * The default arrow size
     */
    int ARROW_SIZE = 3;

    /**
     * The default initial x
     */
    int INITIAL_X = 35;

    /**
     * The default initial y
     */
    int INITIAL_Y = 20;

    int SYMBOL_GAP_HEIGHT = 4;

    int SYMBOL_GAP_WIDTH = 2;

    /**
     * The font height
     * 
     * @return The font height
     */
    int fontHeight();

    /**
     * How much vertical space is between two symbols
     * 
     * @return How much vertical space is between two symbols
     */
    int componentGapHeight();

    /**
     * How much horizontal space is between two symbols
     * 
     * @return How much horizontal space is between two symbols
     */
    int componentGapWidth();

    /**
     * The string width of the text. That's the string width calculated using
     * the underlying font
     * 
     * @param text
     * @return
     */
    int stringWidth(String text);

    /**
     * The vertical gap between the text of a symbol and the box that contains
     * the symbol
     * 
     * @return The vertical gap between the text of a symbol and the box that
     *         contains the symbol
     */
    int symbolGapHeight();

    /**
     * The horizontal gap between the text of a symbol and the box that contains
     * the symbol
     * 
     * @return The horizontal gap between the text of a symbol and the box that
     *         contains the symbol
     */
    int symbolGapWidth();

    /**
     * The arc size used in optional and iterations node
     * 
     * @return The arc size used in optional and iterations node
     */
    int arcSize();

    /**
     * Draw the borders of each node. Useful for debugging purpose only
     * 
     * @return
     */
    boolean showBorders();

    /**
     * The x coordinate where the graph should start drawing it
     * 
     * @return
     */
    int initialX();

    /**
     * The y coordinate were the graph should start drawing it
     * 
     * @return
     */
    int initialY();

    /**
     * How big is the arrow of each connector
     * 
     * @return
     */
    int arrowSize();

    /**
     * where the drawing starts
     * 
     * @param x
     * @param y
     */
    void withInitialLocation(int x, int y);

    boolean optimize();

    /**
     * Set the component gap height
     * 
     * @param height
     * @return
     */
    ChartOptions withComponentGapHeight(int height);

    /**
     * Set the arc size
     * 
     * @param size
     * @return
     */
    ChartOptions withArcSize(int size);

    ChartOptions withOptimize(boolean optimize);

    /**
     * Indicates if the repeated nodes are drawing from left-to-right or
     * right-to-left.
     * 
     * @return True if the repeated nodes are drawing from right-to-left
     */
    ChartLayout iterationLayout();

    /**
     * Set the drawing order for repeated nodes
     * @param rightToLeft
     * 
     * @return
     */
    ChartOptions withIterationOrder(ChartLayout order);

    boolean showSymbolName();

    ChartOptions withSymbolName(boolean show);
}
