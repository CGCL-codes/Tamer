package ti.swing;

import ti.swing.console.*;
import ti.exceptions.ProgrammingErrorException;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;

/**
 * ConsoleTextArea is like a regular text area, except that you can
 * <code>getReader()</code>/<code>getWriter()</code> to get a Reader/Writer
 * that can be used for input/output with a non-GUI program. Because of this,
 * ConsoleTextArea can be used as a console for non-GUI programs.
 *
 * @author Rob Clark
 * @version 0.4
 */
public class ConsoleTextArea extends Console implements Serializable {

    /**
   * Handling keyboard input, and providing a Reader interface is done by 
   * the key-listener.
   */
    private ConsoleKeyListener keyListener = null;

    /**
   * Class Constructor
   */
    public ConsoleTextArea() {
        this(25, 80);
    }

    /**
   * Class Constructor.  Create a standard read-write console.
   * 
   * @param rows        the number of rows for the console
   * @param columns     the number of columns for the console
   */
    public ConsoleTextArea(int rows, int columns) {
        this(rows, columns, false);
    }

    private MouseSelectionHandler mouseSelectionHandler;

    /**
   * Class Constructor.  Create a console window.  If <code>ro</code> is
   * true, then the console is "read-only", meaning that the user cannot
   * type input into the console.  
   *<p>
   * You can still get a reader for a "read-only" console, but because the
   * user cannot enter any input there is probably no point in doing so.
   * 
   * @param rows        the number of rows for the console
   * @param columns     the number of columns for the console
   * @param readonly    true if this should be a read-only console
   */
    public ConsoleTextArea(int rows, int columns, boolean readonly) {
        super(rows, columns);
        if (!readonly) {
            keyListener = new ConsoleKeyListener(this);
            addKeyListener(keyListener);
        }
        mouseSelectionHandler = new MouseSelectionHandler();
        addMouseListener(mouseSelectionHandler);
        addMouseMotionListener(mouseSelectionHandler);
        setInputHandler(new DefaultInputAdapter(getInputHandler()));
    }

    /**
   * InputHandler that deals with \r, \t, etc
   */
    private static class DefaultInputAdapter extends InputAdapter {

        /**
     * don't use this constructor... just here to make things serializable
     */
        public DefaultInputAdapter() {
            super(null);
        }

        DefaultInputAdapter(InputHandler ih) {
            super(ih);
        }

        private char[] TAB = new char[] { ' ', ' ', ' ', ' ', ' ' };

        public void append(char[] cbuf, int off, int len) {
            lock();
            int startIdx = 0;
            for (int i = 0; i < len; i++) {
                switch(cbuf[i + off]) {
                    case '\t':
                        super.append(cbuf, off + startIdx, i - startIdx);
                        super.append(TAB, 0, TAB.length);
                        startIdx = i + 1;
                        break;
                    case '\r':
                        super.append(cbuf, off + startIdx, i - startIdx);
                        startIdx = i + 1;
                        break;
                }
            }
            super.append(cbuf, off + startIdx, len - startIdx);
            unlock();
        }
    }

    /**
   * Paint this component.  We overload this so we have a chance to draw
   * a cursor over things.
   */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (keyListener != null) keyListener.paintCursor(g);
    }

    /**
   * Overload so that non-read-only consoles don't let TAB change focus.
   */
    public boolean isManagingFocus() {
        return true;
    }

    /**
   * Scroll to a specified text coordinate.  This method figures out the
   * minimum amount of scrolling needed to make a particular text coordinate
   * visible, and scrolls there, if it is not already visible.
   * 
   * @param p   the position to scroll to
   */
    protected void scrollTo(Point p) {
        throw new ProgrammingErrorException("unimplemented");
    }

    /**
   * Set the history.  The history object must be an object that was previously
   * returned by {@link #getHistory}, but may be serialized/deserialized.
   * 
   * @param history   a history object, as returned by {@link #getHistory}
   * @see #getHistory
   */
    public void setHistory(Object history) {
        if (keyListener != null) keyListener.setHistory(history);
    }

    /**
   * Get the history.  The history is externally treated as opaque, but the
   * get/set methods allow the creator of the {@link Console} to make history
   * persistant.  The history object is {@link java.io.Serializable}
   * 
   * @return a opaque history object, which is serializable
   * @see #setHistory
   */
    public Object getHistory() {
        if (keyListener != null) return keyListener.getHistory();
        return null;
    }

    /**
   * Set the {@link ConsoleTabCompleter}
   */
    public void setTabCompleter(ConsoleTabCompleter ctc) {
        if (keyListener != null) keyListener.setTabCompleter(ctc);
    }

    /**
   * Select the entire buffer.
   */
    public void selectAll() {
        throw new ProgrammingErrorException("unimplemented");
    }

    /**
   * Copy the selected text into the system-wide clipboard.
   */
    public void copy() {
        copy((getToolkit()).getSystemClipboard());
    }

    /**
   * Copy the selected text into the provided clipboard.  If no text is
   * selected, this is a no-op.
   * 
   * @param clipboard   the clipboard to copy into
   */
    public void copy(Clipboard clipboard) {
        int startOffset = mouseSelectionHandler.getSelectedStartOffset();
        int endOffset = mouseSelectionHandler.getSelectedEndOffset();
        String str = new String(getInputHandler().getData(startOffset, endOffset - startOffset));
        clipboard.setContents(new StringSelection(str), null);
    }

    /**
   * Paste the text from the system-wide clipboard into the text area at
   * the specified position.  If the data in the clipboard is not text,
   * then this is a no-op.
   */
    public void paste() {
        paste((getToolkit()).getSystemClipboard());
    }

    /**
   * Paste the text from the specified clipboard into the text area at
   * the specified position.  If the data in the clipboard is not text,
   * then this is a no-op.
   * 
   * @param clipboard   the clipboard to copy from
   */
    public void paste(Clipboard clipboard) {
        if (keyListener != null) {
            Transferable contents = clipboard.getContents(this);
            String str;
            try {
                str = (String) (contents.getTransferData(DataFlavor.stringFlavor));
            } catch (Exception e) {
                str = contents.toString();
            }
            paste(str);
        }
    }

    /**
   * Paste the text into the text area.
   * 
   * @param str         the string to paste.
   */
    public void paste(String str) {
        if (keyListener != null) {
            char data[] = str.toCharArray();
            getInputHandler().lock();
            for (int i = 0; i < data.length; i++) keyListener.insertChar(data[i]);
            getInputHandler().unlock();
        }
    }

    /**
   * This should called when getting rid of this text-area... this will
   * cause any blocking readers to return.
   */
    public void dispose() {
        if (keyListener != null) keyListener.dispose();
    }

    /**
   * Get the Writer for this console.  By using this writer, an
   * application can use this console for output.
   *
   * @return a writer that can be used to write into the console
   */
    public Writer getWriter() {
        return new ConsoleWriter();
    }

    /**
   * A ConsoleWriter is a Writer that sends its output to it's ConsoleTextArea
   * outer class.
   */
    private class ConsoleWriter extends Writer {

        private static final int BUFSIZE = 40;

        private char buf[];

        private int cursor;

        /**
     * Class Constructor
     */
        public ConsoleWriter() {
            super();
            initBuf();
        }

        /**
     * Initialize the buffer
     */
        private void initBuf() {
            buf = new char[BUFSIZE];
            cursor = 0;
        }

        /**
     * Write a portion of an array of characters.
     *
     * @param cbuf        Array of characters 
     * @param off         Offset from which to start writing characters 
     * @param len         Number of characters to write 
     * @exception IOException If an I/O error occurs
     */
        public void write(char[] cbuf, int off, int len) throws IOException {
            synchronized (this) {
                if (len + cursor >= buf.length) {
                    int newsize = buf.length;
                    while (len + cursor >= newsize) newsize += BUFSIZE;
                    char newbuf[] = new char[newsize];
                    for (int i = 0; i < cursor; i++) newbuf[i] = buf[i];
                    buf = newbuf;
                }
                for (int i = off; i < (off + len); i++) {
                    buf[cursor] = cbuf[i];
                    cursor++;
                }
            }
        }

        /**
     * Flush the stream. If the stream has saved any characters from the various
     * write() methods in a buffer, write them immediately to their intended 
     * destination. Then, if that destination is another character or byte stream,
     * flush it. Thus one flush() invocation will flush all the buffers in a chain
     * of Writers and OutputStreams.
     *
     * @exception IOException - If an I/O error occurs
     */
        public void flush() throws IOException {
            char[] buf;
            int cursor;
            synchronized (this) {
                buf = this.buf;
                cursor = this.cursor;
                initBuf();
            }
            ConsoleTextArea.this.write(buf, 0, cursor);
        }

        /**
     * You can't close this!
     *
     * @exception IOException - If an I/O error occurs
     */
        public void close() throws IOException {
            flush();
            getInputHandler().close();
        }
    }

    private final void write(char[] cbuf, int off, int len) {
        InputHandler ih = getInputHandler();
        ih.append(cbuf, off, len);
        waitForRedraw();
    }

    /**
   * Get the Reader for this console.  By using this reader, an application 
   * can use this console for input.
   *
   * @return a reader that can be used to read from the console
   */
    public Reader getReader() {
        if (keyListener != null) return keyListener.getReader(); else throw new ProgrammingErrorException("read-only console");
    }

    /**
   * The mouse-handler keeps track of the selected region of the document,
   * and applies an INVERSE attribute over that region.
   */
    private class MouseSelectionHandler implements MouseMotionListener, MouseListener, Serializable {

        private int startOffset = -1;

        private int endOffset = -1;

        public int getSelectedStartOffset() {
            return Math.min(startOffset, endOffset);
        }

        public int getSelectedEndOffset() {
            return Math.max(startOffset, endOffset);
        }

        public void mouseDragged(MouseEvent evt) {
            endOffset = getEventOffset(evt);
            updateSelectedRegion();
        }

        public void mousePressed(MouseEvent evt) {
            startOffset = endOffset = getEventOffset(evt);
            updateSelectedRegion();
        }

        public void mouseReleased(MouseEvent evt) {
            if (startOffset != endOffset) copy();
        }

        public void mouseClicked(MouseEvent evt) {
            if ((evt.getModifiers() & MouseEvent.BUTTON2_MASK) != 0) paste();
        }

        public void mouseMoved(MouseEvent evt) {
        }

        public void mouseEntered(MouseEvent evt) {
        }

        public void mouseExited(MouseEvent evt) {
        }

        private final int getEventOffset(MouseEvent evt) {
            return toOffset(evt.getPoint());
        }

        private Region selectedRegion = null;

        private final void updateSelectedRegion() {
            if ((startOffset == -1) || (endOffset == -1)) return;
            int actualStartOffset = getSelectedStartOffset();
            int actualEndOffset = getSelectedEndOffset();
            InputHandler ih = getInputHandler();
            ih.lock();
            if (selectedRegion != null) {
                ih.removeRegion(selectedRegion);
                selectedRegion = null;
            }
            if (actualStartOffset != actualEndOffset) {
                selectedRegion = InverseAttribute.INVERSE.getRegion(actualStartOffset, actualEndOffset - actualStartOffset);
                ih.addRegion(selectedRegion);
            }
            ih.unlock();
        }
    }
}
