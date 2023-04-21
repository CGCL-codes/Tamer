package ch.trackedbean.binding.components;

import java.awt.*;
import javax.swing.*;
import ch.trackedbean.binding.mapping.*;
import ch.trackedbean.tracking.*;

/**
 * Label which changes it's color according to a linked bean property status
 * 
 * @author M. Hautle
 */
public class StatusLabel extends JLabel implements IStatusDependent {

    /** Color indicating an technical error. */
    private static final Color TECHNICAL_ERROR = Color.RED.darker();

    /** Color indicating an error. */
    private static final Color ERROR = Color.RED;

    /** Color indicating an changed value. */
    private static final Color CHANGED = Color.BLUE.darker();

    /** The 'normal' tooltip text to show. */
    private String defaultTooltip;

    /**
     * Creates a <code>StatusLabel</code> instance with the specified text, image, and horizontal alignment. The label is centered vertically in its display
     * area. The text is on the trailing edge of the image.
     * 
     * @param text The text to be displayed by the label.
     * @param icon The image to be displayed by the label.
     * @param horizontalAlignment One of the following constants defined in <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>,
     *            <code>RIGHT</code>, <code>LEADING</code> or <code>TRAILING</code>.
     */
    public StatusLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
    }

    /**
     * Creates a <code>StatusLabel</code> instance with the specified text and horizontal alignment. The label is centered vertically in its display area.
     * 
     * @param text The text to be displayed by the label.
     * @param horizontalAlignment One of the following constants defined in <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>,
     *            <code>RIGHT</code>, <code>LEADING</code> or <code>TRAILING</code>.
     */
    public StatusLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
    }

    /**
     * Creates a <code>StatusLabel</code> instance with the specified text. The label is aligned against the leading edge of its display area, and centered
     * vertically.
     * 
     * @param text The text to be displayed by the label.
     */
    public StatusLabel(String text) {
        super(text);
    }

    /**
     * Creates a <code>StatusLabel</code> instance with the specified image and horizontal alignment. The label is centered vertically in its display area.
     * 
     * @param image The image to be displayed by the label.
     * @param horizontalAlignment One of the following constants defined in <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>,
     *            <code>RIGHT</code>, <code>LEADING</code> or <code>TRAILING</code>.
     */
    public StatusLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
    }

    /**
     * Creates a <code>StatusLabel</code> instance with the specified image. The label is centered vertically and horizontally in its display area.
     * 
     * @param image The image to be displayed by the label.
     */
    public StatusLabel(Icon image) {
        super(image);
    }

    /**
     * Creates a <code>StatusLabel</code> instance with no image and with an empty string for the title. The label is centered vertically in its display area.
     * The label's contents, once set, will be displayed on the leading edge of the label's display area.
     */
    public StatusLabel() {
        super();
    }

    /**
     * Creates a {@link StatusLabel} and attaches it to the given {@link MappingGroup}.
     * 
     * @param text The label text
     * @param property The property for which this label is used
     * @param grp The mapping group where to attach this label
     * @return The label
     */
    public static StatusLabel create(final String text, final String property, final MappingGroup grp) {
        final StatusLabel b = new StatusLabel(text);
        grp.createBinding(property, b);
        return b;
    }

    /**
     * Sets the label color according to a bean property status
     * 
     * @param status The status of a bean property
     */
    @Override
    public void setStatus(final Status status) {
        Color c;
        switch(status.getFlag()) {
            case CHANGED:
                c = CHANGED;
                break;
            case ERROR:
                c = ERROR;
                break;
            case TECHNICAL_ERROR:
                c = TECHNICAL_ERROR;
                break;
            default:
                c = Color.BLACK;
        }
        final String tip = status.getMessage();
        super.setToolTipText(tip != null ? tip : defaultTooltip);
        setForeground(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setToolTipText(String text) {
        defaultTooltip = text;
        super.setToolTipText(text);
    }
}
