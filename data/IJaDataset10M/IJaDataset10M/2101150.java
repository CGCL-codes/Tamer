package glkjni;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.brickshadow.jglk.GlkImageAlign;
import org.brickshadow.jglk.GlkStyle;
import org.brickshadow.jglk.GlkStyleHint;

public abstract class GlkWindow {

    protected GlkWindow() {
    }

    /**
     * Forwarded from {@code glk_window_clear}.
     * <p>
     * Clears the window.
     */
    protected abstract void clear();

    /**
     * Forwarded from {@code glk_window_get_size}.
     * <p>
     * Returns the size of the window in the {@code dim[]} array.
     * <ul>
     * <li><b>{@code dim[0]}</b> - the window width</li>
     * <li><b>{@code dim[1]}</b> - the window height</li>
     * 
     * @param dim
     *           The width and height of the window.<p>
     */
    protected abstract void getSize(int[] dim);

    /**
     * Forwarded from {@code glk_request_mouse_event}.
     * <p>
     * Requests a mouse event for the window.
     */
    protected abstract void requestMouseEvent();

    /**
     * Forwarded from {@code glk_cancel_mouse_event}.
     * <p>
     * Cancels mouse events for the window. This method may be called
     * when there is no pending request for a mouse event.
     */
    protected abstract void cancelMouseEvent();

    /**
     * Forwarded from {@code glk_set_style}.
     * <p>
     * Changes the current output style of the text window.
     * 
     * @param val
     *           One of the constants in {@link GlkStyle}.<p>
     */
    protected abstract void setStyle(int val);

    /**
     * Forwarded from {@code glk_style_measure}.
     * <p>
     * Returns the current value of a style attribute in the text window.
     * 
     * @param styl
     *           One of the constants in {@link GlkStyle}.<p>
     * @param hint
     *           One of the constants in {@link GlkStyleHint}.<p>
     * @throws RuntimeException
     *           if the style attribute cannot be measured.
     * @return
     *           The current value of the style attribute.
     */
    protected abstract int measureStyle(int styl, int hint);

    /**
     * Forwarded from {@code glk_style_distinguish}.
     * <p>
     * Checks if two styles are visually distinguishable in the text
     * window.
     * 
     * @param styl1
     *           One of the constants in {@link GlkStyle}.<p>
     * @param styl2
     * 			 One of the constants in {@link GlkStyle}.<p>
     * @return
     *           True if the styles are visually distingushable.
     */
    protected abstract boolean distinguishStyles(int styl1, int styl2);

    /**
     * Forwarded from the Glk printing functions.
     * <p>
     * Prints a string in the text window.
     * <p>
     * <b>NOTE:</b> GlkJNI buffers text output until the next call to
     * {@code glk_select}, {@code glk_poll}, {@code glk_set_style},
     * {@code glk_set_hyperlink}, {@code glk_request_line_input},
     * {@code glk_window_move_cursor}, or {@code glk_exit}.
     * 
     * @param str
     *           The string to print.<p>
     */
    protected abstract void print(String str);

    /**
     * Forwarded from {@code glk_request_char_event} and
     * {@code glk_request_char_event_uni}.
     * <p>
     * Requests character input in the window. This method will not be
     * called when there is a pending request for character or line input.
     * 
     * @param unicode
     *           True if Unicode input is requested, false for Latin-1.
     */
    protected abstract void requestCharEvent(boolean unicode);

    /**
     * Forwarded from {@code glk_cancel_char_event}.
     * <p>
     * Cancels character input for the text window. This method may be
     * called when there is no pending request for character input.
     */
    protected abstract void cancelCharEvent();

    /**
     * Forwarded from {@code glk_request_line_event}.
     * <p>
     * Requests Latin-1 line input in the text window.
     * 
     * @param buf
     *           A buffer for storing the Latin-1 bytes of the user's
     *           input.<p>
     * @param maxlen
     *           The available length of the buffer.<p>
     * @param initlen
     *           If non-zero, the length of pre-existing data in the
     *           buffer.<p>
     */
    protected abstract void requestLineEvent(ByteBuffer buf, int maxlen, int initlen);

    /**
     * Forwarded from {@code glk_request_line_event_uni}.
     * <p>
     * Requests Unicode line input in the text window.
     * 
     * @param buf
     *           A buffer for storing the Unicode codepoints of the user's
     *           input.<p>
     * @param maxlen
     *           The available length of the buffer.<p>
     * @param initlen
     *           If non-zero, the length of pre-existing data in the
     *           buffer.<p>
     */
    protected abstract void requestLineEventUni(IntBuffer buf, int maxlen, int initlen);

    /**
     * Forwarded from {@code glk_window_set_hyperlink}.
     * <p>
     * Sets the link value for the current text position in the window.
     * 
     * @param val
     *           The link value.<p>
     */
    protected abstract void setLinkValue(int val);

    /**
     * Forwarded from {@code glk_request_hyperlink_event}.
     * <p>
     * Requests hyperlink input in the text window. This method will not
     * be called when there is a pending request for hyperlink input.
     */
    protected abstract void requestLinkEvent();

    /**
     * Forwarded from {@code glk_cancel_hyperlink_event}.
     * <p>
     * Cancels hyperlink input for the text window. This method may be
     * called when there is no pending request for hyperlink input.
     */
    protected abstract void cancelLinkEvent();

    /**
     * Forwarded from {@code glk_cancel_line_event}.
     * <p>
     * Cancels line input for the text window. This method may be called
     * when there is no pending request for line input.
     * 
     * @return
     *           The number of characters, if any, already entered
     *           during an active line input request.
     */
    protected abstract int cancelLineEvent();

    /**
     * Forwarded from {@code glk_window_move_cursor}.
     * <p>
     * Positions the cursor in the text grid window.
     * 
     * @param xpos
     *           The x position of the cursor. If this is past the end of
     *           a line, the cursor moves to the beginning of the next
     *           line.<p>
     * @param ypos
     *           The y position of the cursor. If this is greater than the
     *           height of the window, the cursor goes "off screen" and
     *           further printing has no effect.
     */
    protected abstract void moveCursor(int xpos, int ypos);

    /**
     * Forwarded from {@code glk_window_set_arrangement}.
     * <p>
     * Rearranges the children of the pair window.
     * @param method
     *           The new split method.<p>
     * @param size
     *           The new size constraint.<p>
     * @param key
     *           The new key window.<p>
     */
    protected abstract void setArrangement(int method, int size, GlkWindow key);

    /**
     * Forwarded from {@code glk_image_draw}.
     * <p>
     * Draws an image in the text buffer window.
     * 
     * @param num
     *           The image resource number.<p>
     * @param alignment
     *           One of the constants in {@link GlkImageAlign}.<p>
     * @return
     *           True if the image was drawn.
     */
    protected abstract boolean drawInlineImage(int num, int alignment);

    /**
     * Forwarded from {@code glk_image_draw}.
     * <p>
     * Draws a scaled image in the text buffer window.
     * 
     * @param num
     *           The image resource number.<p>
     * @param alignment
     *           One of the constants in {@link GlkImageAlign}.<p>
     * @param width
     *           The display width of the image.<p>
     * @param height
     *           The display height of the image.<p>
     * @return
     *           True if the image was drawn.
     */
    protected abstract boolean drawInlineImage(int num, int alignment, int width, int height);

    /**
     * Forwarded from {@code glk_window_flow_break}.
     * <p>
     * Breaks the text in the window below a margin image.
     */
    protected abstract void flowBreak();

    /**
     * Forwarded from {@code glk_image_draw}.
     * <p>
     * Draws an image in the window.
     * 
     * @param num
     *           The number of the image resource.<p>
     * @param x
     *           The x coordinate of the image.<p>
     * @param y
     *           The y coordinate of the image.<p>
     * @return
     *           True if the image was drawn.
     */
    protected abstract boolean drawImage(int num, int x, int y);

    /**
     * Forwarded from {@code glk_image_draw}.
     * <p>
     * Draws an image in the window, scaled to a certain size. The x and
     * y coordinate may lie outside the window; width and height will
     * be non-zero and positive.
     * 
     * @param num
     *           The number of the image resource.<p>
     * @param x
     *           The x coordinate of the image.<p>
     * @param y
     *           The y coordinate of the image.<p>
     * @param width
     *           The display width of the image.<p>
     * @param height
     *           The display height of the image.<p>
     * @return
     *           True if the image was drawn.
     */
    protected abstract boolean drawImage(int num, int x, int y, int width, int height);

    /**
     * Forwarded from {@code glk_window_set_background_color}.
     * <p>
     * Sets the background color of the window. This does not take effect
     * until the next clear or redraw.
     * 
     * @param color
     *           The background color.<p>
     */
    protected abstract void setBackgroundColor(int color);

    /**
     * Forwarded from {@code glk_window_erase_rect}.
     * <p>
     * Clears a rectangle with the window's background color. The
     * dimensions of the rectangle might not lie entirely within the
     * window; the width and height will be non-zero and positive.
     * 
     * @param left
     *           The leftmost x coordinate of the rectangle.<p>
     * @param top
     *           The topmost y coordinate of the rectangle.<p>
     * @param width
     *           The width of the rectangle.<p>
     * @param height
     *           The height of the rectangle.<p>
     */
    protected abstract void eraseRect(int left, int top, int width, int height);

    /**
     * Forwarded from {@code glk_window_fill_rect}.
     * <p>
     * Fills a rectangle with a certain color.  The dimensions of the
     * rectangle might not lie entirely within the window; the width and
     * height will be non-zero and positive.
     * 
     * @param color
     *           The fill color.<p>
     * @param left
     *           The leftmost x coordinate of the rectangle.<p>
     * @param top
     *           The topmost y coordinate of the rectangle.<p>
     * @param width
     *           The width of the rectangle.<p>
     * @param height
     *           The height of the rectangle.<p>
     */
    protected abstract void fillRect(int color, int left, int top, int width, int height);
}
