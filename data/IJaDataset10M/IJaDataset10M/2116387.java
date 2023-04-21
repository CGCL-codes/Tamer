package gtkwire.widget.utils;

/**
*Used for justifying the text inside a GtkLabel widget.
*Documentation for this class is from the gtk+ file that this class binds to. See original file for copyrights.
*/
public class GtkJustification {

    /**
	*The text is placed at the left edge of the label.
	*/
    public static final GtkJustification LEFT = new GtkJustification("GTK_JUSTIFY_LEFT");

    /**
	*The text is placed at the right edge of the label.
	*/
    public static final GtkJustification RIGHT = new GtkJustification("GTK_JUSTIFY_RIGHT");

    /**
	*The text is placed in the center of the label.
	*/
    public static final GtkJustification CENTER = new GtkJustification("GTK_JUSTIFY_CENTER");

    /**
	*The text is placed is distributed across the label.
	*/
    public static final GtkJustification FILL = new GtkJustification("GTK_JUSTIFY_FILL");

    private String mode;

    private GtkJustification(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
