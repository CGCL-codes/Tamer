package javax.accessibility;

import javax.swing.text.AttributeSet;

/**
 * Objects which present editable textual information on the display should
 * implement this interface.  Accessibility software can use the
 * implementations of this interface to change the content, attributes,
 * and spacial location of the text.
 *
 * <p>The <code>AccessibleContext.getAccessibleEditableText()</code> method
 * should return <code>null</code> if an object does not implement this
 * interface.
 *
 * @author Eric Blake (ebb9@email.byu.edu)
 * @see Accessible
 * @see AccessibleContext
 * @see AccessibleContext#getAccessibleText()
 * @see AccessibleContext#getAccessibleEditableText()
 * @since 1.2
 * @status updated to 1.4, except for javax.swing support
 */
public interface AccessibleEditableText extends AccessibleText {

    void setTextContents(String s);

    void insertTextAtIndex(int index, String s);

    String getTextRange(int start, int end);

    void delete(int start, int end);

    void cut(int start, int end);

    void paste(int start);

    void replaceText(int start, int end, String s);

    void selectText(int start, int stop);

    void setAttributes(int start, int end, AttributeSet s);
}
