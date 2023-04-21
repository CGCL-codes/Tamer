package org.jfree.chart.block;

import java.util.List;
import org.jfree.ui.Size2D;

/**
 * Represents the result of a call to one of the arrange() methods in the
 * {@link Block} class.
 */
public class ArrangeResult {

    /** A list of warnings/errors from the layout process. */
    private List messages;

    /** The size of the block. */
    private Size2D size;

    /**
     * Creates a new default instance.
     */
    public ArrangeResult() {
        this(null, null);
    }

    public ArrangeResult(double width, double height, List messages) {
        this.size = new Size2D(width, height);
        this.messages = messages;
    }

    /**
     * Creates a new result.
     * 
     * @param size  the size.
     */
    public ArrangeResult(Size2D size, List messages) {
        this.size = size;
        this.messages = messages;
    }

    public double getWidth() {
        return this.size.getWidth();
    }

    public void setWidth(double width) {
        this.size = new Size2D(width, this.size.getHeight());
    }

    public double getHeight() {
        return this.size.getHeight();
    }

    public void setHeight(double height) {
        this.size = new Size2D(this.size.getWidth(), height);
    }

    /**
     * Returns the block size.
     * 
     * @return The block size.
     */
    public Size2D getSize() {
        return this.size;
    }

    /**
     * Sets the block size.
     * 
     * @param size  the size.
     */
    public void setSize(Size2D size) {
        this.size = size;
    }

    public void setSize(double width, double height) {
        this.size = new Size2D(width, height);
    }

    /**
     * Returns the message list.  This contains errors and/or warnings from
     * the layout process.
     * 
     * @return The message list (possibly <code>null</code>).
     */
    public List getMessages() {
        return this.messages;
    }

    /**
     * Sets the message list.
     * 
     * @param messages  the message list (<code>null</code> permitted)..
     */
    public void setMessages(List messages) {
        this.messages = messages;
    }

    public String toString() {
        return "ArrangeResult[w=" + this.getWidth() + ",h=" + this.getHeight() + "]";
    }
}
