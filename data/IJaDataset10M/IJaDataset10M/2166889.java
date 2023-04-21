package org.fenggui.binding.clipboard;

/**
 * Represents a Clipboard implementation. It is possible to read and write a String from and to the clipboard.
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface IClipboard {

    /**
   * Returns the text in the systems clipboard.
   * 
   * @return Text form the clipboard.
   */
    public abstract String getText();

    /**
   * Sets the given text to the systems clipboard.
   * 
   * @param text Text to set to the clipboard.
   */
    public abstract void setText(String text);
}
