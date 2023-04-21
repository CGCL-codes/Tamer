package jaron.gui;

/**
 * The <code>Panel</code> class is a helper class for a graphical component that
 * consists of an outer frame and the content where the drawing is done.<br>
 * The panel can furthermore have up to four labels that can be added to the top,
 * the bottom, the left and the right side of the content. Adding those labels is
 * done by setting their height (top and bottom label) or their width respectively
 * (right and left label).
 * 
 * @author      jarontec gmail com
 * @version     1.2
 * @since       1.0
 */
public class Panel {

    /**
   * A <code>Rect</code> that represents the panel's frame.
   */
    public Rect frame;

    /**
   * A <code>Rect</code> that encloses the panel's content.
   */
    public Rect content;

    /**
   * A <code>Rect</code> that encloses the panel's bottom label.
   */
    public Rect labelBottom;

    /**
   * A <code>Rect</code> that encloses the panel's left label.
   */
    public Rect labelLeft;

    /**
   * A <code>Rect</code> that encloses the panel's right label.
   */
    public Rect labelRight;

    /**
   * A <code>Rect</code> that encloses the panel's top label.
   */
    public Rect labelTop;

    /**
   * Creates a new <code>Panel</code> object at a certain position and with a certain
   * width and height.<br>
   * 
   * @param left    the left coordinate
   * @param top     the top coordinate
   * @param width   the width of the panel
   * @param height  the height of the panel
   */
    public Panel(int left, int top, int width, int height) {
        frame = new Rect(left, top, width, height);
        content = new Rect(left, top, width, height);
        labelBottom = new Rect(left, top + height, width, 0);
        labelLeft = new Rect(left, top, 0, height);
        labelRight = new Rect(left + width, top, 0, height);
        labelTop = new Rect(left, top, width, 0);
    }

    /**
   * Returns the panels content <code>Rect</code>.
   * 
   * @return the <code>Rect</code> that encloses the panel's content
   */
    public Rect getContent() {
        return content;
    }

    /**
   * Returns the panels frame <code>Rect</code>.
   * 
   * @return the <code>Rect</code> that represents the panel's frame
   */
    public Rect getFrame() {
        return frame;
    }

    /**
   * Returns the panels bottom label <code>Rect</code>.
   * 
   * @return the <code>Rect</code> that encloses the panel's bottom label
   */
    public Rect getLabelBottom() {
        return labelBottom;
    }

    /**
   * Returns the panels left label <code>Rect</code>.
   * 
   * @return the <code>Rect</code> that encloses the panel's left label
   */
    public Rect getLabelLeft() {
        return labelLeft;
    }

    /**
   * Returns the panels right label <code>Rect</code>.
   * 
   * @return the <code>Rect</code> that encloses the panel's right label
   */
    public Rect getLabelRight() {
        return labelRight;
    }

    /**
   * Returns the panels top label <code>Rect</code>.
   * 
   * @return the <code>Rect</code> that encloses the panel's top label
   */
    public Rect getLabelTop() {
        return labelTop;
    }

    /**
   * Sets a new content <code>Rect</code> for this panel. By altering the
   * content, all the calculations like adding labels is done to the new
   * content <code>Rect</code>.
   * 
   * @param content   the new content <code>Rect</code> to be used
   */
    public void setContent(Rect content) {
        this.content = content;
    }

    /**
   * Sets a new frame <code>Rect</code> for this panel.
   * 
   * @param frame   the new frame <code>Rect</code> to be used
   */
    public void setFrame(Rect frame) {
        this.frame = frame;
    }

    /**
   * Sets the panel's bottom label height. Initially the height is set to 0.
   * 
   * @param   height  the label's new height
   */
    public void setLabelBottomHeight(int height) {
        content.setSize(frame.getWidth() - labelLeft.getWidth() - labelRight.getWidth(), frame.getHeight() - labelTop.getHeight() - height);
        labelBottom = new Rect(content.getLeft(), content.getTop() + content.getHeight(), content.getWidth(), height);
    }

    /**
   * Sets the panel's left label width. Initially the width is set to 0.
   * 
   * @param   width  the label's new width
   */
    public void setLabelLeftWidth(int width) {
        content.setLocation(frame.getLeft() + width, frame.getTop() + labelTop.getHeight());
        content.setSize(frame.getWidth() - labelRight.getWidth() - width, frame.getHeight() - labelTop.getHeight() - labelBottom.getHeight());
        labelLeft = new Rect(content.getLeft() - width, content.getTop(), width, content.getHeight());
    }

    /**
   * Sets the panel's right label width. Initially the width is set to 0.
   * 
   * @param   width  the label's new width
   */
    public void setLabelRightWidth(int width) {
        content.setSize(frame.getWidth() - labelLeft.getWidth() - width, frame.getHeight() - labelTop.getHeight() - labelBottom.getHeight());
        labelRight = new Rect(content.getLeft() + content.getWidth(), content.getTop(), width, content.getHeight());
    }

    /**
   * Sets the panel's top label height. Initially the height is set to 0.
   * 
   * @param   height  the label's new height
   */
    public void setLabelTopHeight(int height) {
        content.setLocation(frame.getLeft() + labelLeft.getWidth(), frame.getTop() + height);
        content.setSize(frame.getWidth() - labelLeft.getWidth() - labelRight.getWidth(), frame.getHeight() - labelBottom.getHeight() - height);
        labelTop = new Rect(content.getLeft(), content.getTop() - height, content.getWidth(), height);
    }
}
