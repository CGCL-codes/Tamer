package ti.swing.console;

import ti.exceptions.ProgrammingErrorException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import javax.swing.Timer;

/**
 * Handle keyboard input, and provide a reader interface.... this really 
 * should be thought of as an inner-class of ConsoleTextArea, but was moved 
 * here to preserve sane file sizes...
 * 
 * @author Rob Clark
 * @version 0.1
 */
public class ConsoleKeyListener extends KeyAdapter implements Serializable {

    private static final int RUNNING = 0;

    private static final int KILLED = 1;

    private int state = RUNNING;

    private static final int VK_CONTROL = (System.getProperty("mrj.version") == null) ? KeyEvent.VK_CONTROL : KeyEvent.VK_META;

    /**
   * Modifiers....
   */
    private boolean overwrite = false;

    private boolean control = false;

    private static final int BUFSIZE = 40;

    /**
   * The console we are a member of...  we really should be an inner class
   * of ConsoleTextArea, but that would be too much for one src file.  Instead
   * we manually implement inner-classes by keeping a reference back to our
   * creator.
   */
    private Console console;

    private static final char[] EMPTY_BUF = new char[0];

    /**
   * The buffer of characters typed since last LF
   */
    private char buf[] = EMPTY_BUF;

    private int bufLength = 0;

    /**
   * The cursor into the buf.
   */
    private int cursor = 0;

    /**
   * The "queue" of characters that have been committed (ie. user pressed 
   * <ENTER>), but not yet consumed by reader.
   */
    private char cqueue[] = EMPTY_BUF;

    /**
   * Since cqueue is overwritten, but we still need to synchronize access
   * to it, this object is used:
   */
    private final Object cqueueLock = new String("cqueueLock");

    /**
   * The history buffer.
   */
    private History history = new History();

    /**
   * The tab completion engine, if one is configured.  Tab completion is 
   * disabled if this is <code>null</code>.
   */
    private ConsoleTabCompleter tabCompleter;

    /**
   * Class Constructor.
   * 
   * @param console      the console we belong to
   */
    public ConsoleKeyListener(Console console) {
        this.console = console;
    }

    /**
   * Set the history.  The history object must be an object that was previously
   * returned by {@link #getHistory}, but may be serialized/deserialized.
   * 
   * @param history   a history object, as returned by {@link #getHistory}
   * @see #getHistory
   */
    public void setHistory(Object history) {
        if (history instanceof History) this.history = (History) history;
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
        return history;
    }

    /**
   * Set the {@link ConsoleTabCompleter}
   */
    public void setTabCompleter(ConsoleTabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
    }

    /**
   * Get the Reader for the console.  By using this reader, an application 
   * can use this console for input.
   *
   * @return a reader that can be used to read from the console
   */
    public Reader getReader() {
        return new Reader() {

            /**
         * Tell whether this stream is ready to be read.
         *
         * @return True if the next read() is guaranteed not to block for input,
         * false otherwise.  Note that returning false does not guarantee that the
         * next read will block.
         *
         * @exception  IOException  If an I/O error occurs
         */
            public boolean ready() throws IOException {
                return (state != KILLED) && (cqueue.length > 0);
            }

            /**
         * Read into a portion of an array of characters.  This method will block
         * until some input is available, an I/O error occurs, or the end of the
         * stream is reached, e.g. the outerclass is finalized.
         *
         * @param cbuf        Array of characters to read into
         * @param off         Offset from which to start writing characters 
         * @param len         Number of characters to write
         * @return the number of characters read, or -1 if the end of the stream
         *         has been reached
         * @exception IOException If an I/O error occurs
         */
            public int read(char[] cbuf, int off, int len) throws IOException {
                while (true) {
                    synchronized (cqueueLock) {
                        while ((state != KILLED) && !ready()) {
                            try {
                                cqueueLock.wait();
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                    if (state == KILLED) return -1;
                    synchronized (cqueueLock) {
                        if (cqueue.length != 0) {
                            int i = off;
                            int j = 0;
                            while ((i < off + len) && (j < cqueue.length)) cbuf[i++] = cqueue[j++];
                            if (j < cqueue.length) {
                                char tmp[] = new char[cqueue.length - j];
                                for (i = 0; i < (cqueue.length - j); i++) tmp[i] = cqueue[j + i];
                                cqueue = tmp;
                            } else {
                                cqueue = new char[0];
                            }
                            return j;
                        }
                    }
                }
            }

            /**
         * Don't do anything, since you can't close this!
         *
         * @exception IOException - If an I/O error occurs
         */
            public void close() throws IOException {
            }
        };
    }

    /**
   * This should called when getting rid of this text-area... this will
   * cause any blocking readers to return.
   */
    public void dispose() {
        state = KILLED;
        synchronized (cqueueLock) {
            cqueueLock.notifyAll();
        }
    }

    /**
   * Append char to current active buffer.
   */
    public void insertChar(char c) {
        InputHandler ih = console.getInputHandler();
        if (c == Console.LF) {
            synchronized (cqueueLock) {
                char[] tmp = new char[cqueue.length + bufLength + 1];
                System.arraycopy(cqueue, 0, tmp, 0, cqueue.length);
                System.arraycopy(buf, 0, tmp, cqueue.length, bufLength);
                tmp[tmp.length - 1] = Console.LF;
                history.append(buf, 0, bufLength);
                buf = EMPTY_BUF;
                cursor = bufLength = 0;
                ih.append(tmp, tmp.length - 1, 1);
                cqueue = tmp;
                cqueueLock.notifyAll();
            }
        } else {
            ensureCapacity(1);
            if (!overwrite) {
                ih.zap(bufLength - cursor);
                System.arraycopy(buf, cursor, buf, cursor + 1, bufLength - cursor);
                buf[cursor] = c;
                bufLength++;
                cursor++;
                ih.append(buf, cursor - 1, bufLength - cursor + 1);
            } else {
                throw new ProgrammingErrorException("unimplemented");
            }
        }
    }

    /**
   * Invoked when a key is typed.
   */
    public synchronized void keyTyped(KeyEvent evt) {
        if (!console.isEnabled()) return;
        if (console.getInputMap().get(javax.swing.KeyStroke.getKeyStroke(evt.getKeyCode(), evt.getModifiers())) != null) return;
        InputHandler ih = console.getInputHandler();
        ih.lock();
        char c = evt.getKeyChar();
        int keyCode = (int) c;
        if (c == Console.CR) c = Console.LF;
        switch(keyCode) {
            case KeyEvent.VK_BACK_SPACE:
                if (cursor > 0) {
                    ih.zap(bufLength - cursor + 1);
                    System.arraycopy(buf, cursor, buf, cursor - 1, bufLength - cursor);
                    bufLength--;
                    cursor--;
                    ih.append(buf, cursor, bufLength - cursor);
                }
                break;
            case KeyEvent.VK_DELETE:
                if (cursor < bufLength) {
                    ih.zap(bufLength - cursor);
                    System.arraycopy(buf, cursor + 1, buf, cursor, bufLength - cursor - 1);
                    bufLength--;
                    ih.append(buf, cursor, bufLength - cursor);
                }
                break;
            case KeyEvent.VK_TAB:
                if (tabCompleter != null) {
                    String pre = new String(buf, 0, cursor);
                    String post = new String(buf, cursor, bufLength - cursor);
                    replaceLine(pre + tabCompleter.complete(pre) + post);
                    break;
                }
            default:
                if (!control) insertChar(c);
        }
        ih.unlock();
    }

    /**
   * Keep track of modifiers.
   */
    public void keyPressed(KeyEvent evt) {
        if (!console.isEnabled()) return;
        int keyCode = evt.getKeyCode();
        if (keyCode == VK_CONTROL) keyCode = KeyEvent.VK_CONTROL;
        switch(keyCode) {
            case KeyEvent.VK_CONTROL:
                control = true;
                break;
            case KeyEvent.VK_LEFT:
                while (cursor > 0) {
                    cursor--;
                    if (!control || isSeparator(buf[cursor])) break;
                }
                console.repaint();
                break;
            case KeyEvent.VK_RIGHT:
                while (++cursor < bufLength) if (!control || isSeparator(buf[cursor])) break;
                if (cursor > bufLength) cursor = bufLength;
                console.repaint();
                break;
        }
    }

    /**
   * Certain keys don't generate keyTyped events, so we have to do this.
   */
    public void keyReleased(KeyEvent evt) {
        if (!console.isEnabled()) return;
        int keyCode = evt.getKeyCode();
        if (keyCode == VK_CONTROL) keyCode = KeyEvent.VK_CONTROL;
        int oldCursor = cursor;
        switch(keyCode) {
            case KeyEvent.VK_CONTROL:
                control = false;
                break;
            case KeyEvent.VK_UP:
                replaceLine(history.getPrev(new String(buf, 0, cursor)));
                cursor = Math.min(oldCursor, cursor);
                break;
            case KeyEvent.VK_DOWN:
                replaceLine(history.getNext(new String(buf, 0, cursor)));
                cursor = Math.min(oldCursor, cursor);
                break;
            case KeyEvent.VK_HOME:
                cursor = 0;
                console.repaint();
                break;
            case KeyEvent.VK_END:
                cursor = bufLength;
                console.repaint();
                break;
            case KeyEvent.VK_ESCAPE:
                if (control) replaceLine("");
                break;
        }
    }

    private static final String SEPARATOR_CHARS = " .()[]{}-+*/_";

    private final boolean isSeparator(char c) {
        for (int i = 0; i < SEPARATOR_CHARS.length(); i++) if (c == SEPARATOR_CHARS.charAt(i)) return true;
        return false;
    }

    private final void replaceLine(String str) {
        if (str != null) {
            char[] buf = str.toCharArray();
            InputHandler ih = console.getInputHandler();
            ih.lock();
            ih.zap(bufLength);
            this.buf = buf;
            bufLength = cursor = buf.length;
            ih.append(buf, 0, bufLength);
            ih.unlock();
        }
    }

    private final void ensureCapacity(int cnt) {
        if ((bufLength + cnt) >= buf.length) {
            char[] newBuf = new char[buf.length + BUFSIZE + cnt];
            System.arraycopy(buf, 0, newBuf, 0, buf.length);
            buf = newBuf;
        }
    }

    /**
   * Draw the cursor over the graphics.
   */
    public void paintCursor(Graphics g) {
        InputHandler ih = console.getInputHandler();
        if (!console.locked()) {
            int off = ih.getOffset() - (bufLength - cursor);
            Point p = console.toPoint(off);
            g = g.create();
            g.setColor(Color.white);
            g.setXORMode(Color.blue);
            g.fillRect(p.x, p.y, console.getColumnWidth(), console.getRowHeight());
        }
    }

    /**
   * Class to implement history buffer.
   */
    private static class History implements Serializable {

        private Vector history = new Vector();

        private int idx = 0;

        /**
     * Get the prev entry in the history that begins with <code>current</code>
     * (which is the characters in the current buffer up to the cursor)
     */
        String getPrev(String current) {
            int idx = this.idx;
            char[] tmp;
            while ((idx > 0) && ((tmp = (char[]) (history.get(--idx))).length >= current.length())) if (current.equals(new String(tmp, 0, current.length()))) return new String((char[]) (history.get(this.idx = idx)));
            return null;
        }

        /**
     * Get the next entry in the history that begins with <code>current</code>
     * (which is the characters in the current buffer up to the cursor)
     */
        String getNext(String current) {
            int idx = this.idx;
            while (idx < (history.size() - 1)) {
                char[] tmp = (char[]) (history.get(++idx));
                if (current.equals(new String(tmp, 0, Math.min(tmp.length, current.length())))) {
                    this.idx = idx;
                    return new String(tmp);
                }
            }
            if (idx < history.size()) return "";
            return null;
        }

        void append(char[] buf, int off, int len) {
            idx = history.size();
            if (len <= 0) return;
            char[] line = new char[len];
            System.arraycopy(buf, off, line, 0, len);
            if ((history.size() > 0) && equals(line, (char[]) (history.get(history.size() - 1)))) return;
            history.add(line);
            idx = history.size();
        }

        private static boolean equals(char[] a1, char[] a2) {
            if (a1.length != a2.length) return false;
            for (int i = 0; i < a1.length; i++) if (a1[i] != a2[i]) return false;
            return true;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append('{');
            int i = 0;
            for (; i < history.size(); i++) sb.append("\"" + new String((char[]) (history.get(i))) + "\",");
            if (i > 0) sb.setCharAt(sb.length() - 1, '}'); else sb.append('}');
            return sb.toString();
        }
    }
}
