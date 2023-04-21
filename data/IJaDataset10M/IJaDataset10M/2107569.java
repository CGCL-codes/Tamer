package cube42.napkin.sequenceDiagram;

import java.awt.*;

/**
 * This class contains all the constants used for text and color in the
 * sequence diagrams.  If you are to change any of this look and feel change
 * it here.
 *
 * @author Matt Paulin
 * @version 1.0
 * @since JDK 1.2
 */
public class GraphicsConstants {

    /**
  * The font used as a default in most situations.
  */
    public static final Font STANDARD_FONT = new Font("TimesRoman", Font.PLAIN, 12);

    /**
  * The font used when displaying the class titles
  */
    public static final Font CLASS_TITLE = new Font("TimesRoman", Font.BOLD, 20);

    /**
  * The color for the border around the edge of the picture
  */
    public static final Color DEFAULT_BORDER_COLOR = Color.black;

    /**
  * The color the lines use unless its replaced by a different color
  */
    public static final Color DEFAULT_LINE_COLOR = Color.darkGray;

    /**
  * The color used when drawing the box for the class in the header
  */
    public static final Color DEFAULT_BOX_COLOR = new Color(2, 155, 251);

    /**
  * This is the color used to show that something is a thread and not just
  * a ordinary call
  */
    public static final Color THREAD_COLOR = Color.red;

    /**
  * This is the color used inside of the box that represents a macro
  */
    public static final Color MACRO_COLOR = new Color(59, 242, 236);

    /**
  * This color is used to make the background for a the drawing in the viewer
  * and in the printed gif
  */
    public static final Color BACKGROUND_IMAGE_COLOR = Color.white;

    /**
  * The panel is set to this color, even though I don't think that this made
  * a bit of difference
  */
    public static final Color BACKGROUND_PANEL_COLOR = Color.black;

    /**
  * This is the color used to show that something was selected
  */
    public static final Color SELECTED_COLOR = new Color(251, 2, 61);

    /**
  * This is the color used to show that something is being moved.
  */
    public static final Color MOVE_COLOR = new Color(251, 2, 167);

    /**
  * If statements look like a box.  And this is the color that is used when
  * drawing that box.
  */
    public static final Color IF_COLOR = new Color(55, 240, 151);

    /**
  * Else statement are colored in this color.
  */
    public static final Color ELSE_COLOR = new Color(181, 114, 227);

    /**
  * The background for the note is drawn with this color
  */
    public static final Color NOTE_COLOR = new Color(71, 233, 174);

    /**
  * The width of the note is set with this
  */
    public static final int NOTE_WIDTH = 100;

    /**
  * The minimum height of the note is set to this dimension
  */
    public static final int NOTE_HEIGHT = 100;

    /**
  * This is the defalut vertical spacing between the calls
  */
    public static final int VERT_SPACING = 50;

    /**
  * Determines how much room is around the text in the box
  */
    public static final int MARGIN_SIZE = 10;

    /**
  * Determines the space above the images of classes in the header
  */
    public static final int HEADER_TOP_MARGIN_SIZE = 30;

    /**
  * Length of the arrow heads
  */
    public static final int ARROW_LENGTH = 18;

    /**
  * dimentions of the tick used as a return icon
  */
    public static final int TICK_LENGTH = 10;

    /**
  * excess box width that goes beyond the lines from the objects on the sides
  * This is used with the macro
  */
    public static final int MACRO_OFFSET = 20;

    /**
  * thickness of the order on the if and else statements
  */
    public static final int IF_ElSE_BORDER = 20;
}
