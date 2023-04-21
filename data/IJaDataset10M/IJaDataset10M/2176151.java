package de.mguennewig.pobjects.metadata;

/** A pseudo form entry to hold a popup-link to another page.
 *
 * @author Michael G�nnewig
 */
public class LinkEntry extends FormEntry {

    private final String label;

    private final int width;

    private final int height;

    /** Creates a new LinkEntry. */
    public LinkEntry(final String name, final int layout, final String label) {
        this(name, layout, label, 600, 600);
    }

    /** Creates a new LinkEntry. */
    public LinkEntry(final String name, final int layout, final String label, final int width, final int height) {
        super(name, null, layout);
        this.label = label;
        this.width = width;
        this.height = height;
    }

    /** Returns the label used for the link. */
    public final String getLabel() {
        return label;
    }

    /** Returns the width of the popup-window. */
    public final int getWidth() {
        return width;
    }

    /** Returns the height of the popup-window. */
    public final int getHeight() {
        return height;
    }
}
