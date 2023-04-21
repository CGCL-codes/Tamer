package org.gjt.sp.jedit;

import org.gjt.sp.jedit.visitors.JEditVisitorAdapter;
import org.gjt.sp.jedit.visitors.JEditVisitor;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.net.Socket;
import java.util.*;
import java.util.List;
import org.gjt.sp.jedit.msg.*;
import org.gjt.sp.jedit.gui.*;
import org.gjt.sp.jedit.textarea.*;
import org.gjt.sp.jedit.input.AbstractInputHandler;
import org.gjt.sp.jedit.input.InputHandlerProvider;
import org.gjt.sp.util.Log;

/**
 * A <code>View</code> is jEdit's top-level frame window.<p>
 *
 * In a BeanShell script, you can obtain the current view instance from the
 * <code>view</code> variable.<p>
 *
 * The largest component it contains is an {@link EditPane} that in turn
 * contains a {@link org.gjt.sp.jedit.textarea.JEditTextArea} that displays a
 * {@link Buffer}.
 * A view can have more than one edit pane in a split window configuration.
 * A view also contains a menu bar, an optional toolbar and other window
 * decorations, as well as docked windows.<p>
 *
 * The <b>View</b> class performs two important operations
 * dealing with plugins: creating plugin menu items, and managing dockable
 * windows.
 *
 * <ul>
 * <li>When a view is being created, its initialization routine
 * iterates through the collection of loaded plugins and constructs the
 * <b>Plugins</b> menu using the properties as specified in the
 * {@link EditPlugin} class.</li>
 * <li>The view also creates and initializes a
 * {@link org.gjt.sp.jedit.gui.DockableWindowManager}
 * object.  This object is
 * responsible for creating, closing and managing dockable windows.</li>
 * </ul>
 *
 * This class does not have a public constructor.
 * Views can be opened and closed using methods in the <code>jEdit</code>
 * class.
 *
 * @see org.gjt.sp.jedit.jEdit#newView(View)
 * @see org.gjt.sp.jedit.jEdit#newView(View,Buffer)
 * @see org.gjt.sp.jedit.jEdit#newView(View,Buffer,boolean)
 * @see org.gjt.sp.jedit.jEdit#closeView(View)
 *
 * @author Slava Pestov
 * @author John Gellene (API documentation)
 * @version $Id: View.java 13142 2008-08-02 11:08:58Z k_satoda $
 */
public class View extends JFrame implements EBComponent, InputHandlerProvider {

    public AbstractInputHandler getInputHandler() {
        return null;
    }

    /**
	 * The group of tool bars above the DockableWindowManager
	 * @see #addToolBar(int,int,java.awt.Component)
	 * @since jEdit 4.0pre7
	 */
    public static final int TOP_GROUP = 0;

    /**
	 * The group of tool bars below the DockableWindowManager
	 * @see #addToolBar(int,int,java.awt.Component)
	 * @since jEdit 4.0pre7
	 */
    public static final int BOTTOM_GROUP = 1;

    public static final int DEFAULT_GROUP = TOP_GROUP;

    /**
	 * The highest possible layer.
	 * @see #addToolBar(int,int,java.awt.Component)
	 * @since jEdit 4.0pre7
	 */
    public static final int TOP_LAYER = Integer.MAX_VALUE;

    /**
	 * The default layer for tool bars with no preference.
	 * @see #addToolBar(int,int,java.awt.Component)
	 * @since jEdit 4.0pre7
	 */
    public static final int DEFAULT_LAYER = 0;

    /**
	 * The lowest possible layer.
	 * @see #addToolBar(int,int,java.awt.Component)
	 * @since jEdit 4.0pre7
	 */
    public static final int BOTTOM_LAYER = Integer.MIN_VALUE;

    /**
	 * Above system tool bar layer.
	 * @see #addToolBar(int,int,java.awt.Component)
	 * @since jEdit 4.0pre7
	 */
    public static final int ABOVE_SYSTEM_BAR_LAYER = 150;

    /**
	 * System tool bar layer.
	 * jEdit uses this for the main tool bar.
	 * @see #addToolBar(int,int,java.awt.Component)
	 * @since jEdit 4.0pre7
	 */
    public static final int SYSTEM_BAR_LAYER = 100;

    /**
	 * Below system tool bar layer.
	 * @see #addToolBar(int,int,java.awt.Component)
	 * @since jEdit 4.0pre7
	 */
    public static final int BELOW_SYSTEM_BAR_LAYER = 75;

    /**
	 * Search bar layer.
	 * @see #addToolBar(int,int,java.awt.Component)
	 * @since jEdit 4.0pre7
	 */
    public static final int SEARCH_BAR_LAYER = 75;

    /**
	 * Below search bar layer.
	 * @see #addToolBar(int,int,java.awt.Component)
	 * @since jEdit 4.0pre7
	 */
    public static final int BELOW_SEARCH_BAR_LAYER = 50;

    /**
	 * @deprecated Status bar no longer added as a tool bar.
	 */
    @Deprecated
    public static final int ABOVE_ACTION_BAR_LAYER = -50;

    /**
	 * Action bar layer.
	 * @see #addToolBar(int,int,java.awt.Component)
	 * @since jEdit 4.2pre1
	 */
    public static final int ACTION_BAR_LAYER = -75;

    /**
	 * Status bar layer.
	 * @see #addToolBar(int,int,java.awt.Component)
	 * @since jEdit 4.2pre1
	 */
    public static final int STATUS_BAR_LAYER = -100;

    /**
	 * Status bar layer.
	 * @see #addToolBar(int,int,java.awt.Component)
	 * @since jEdit 4.2pre1
	 */
    public static final int BELOW_STATUS_BAR_LAYER = -150;

    /**
	 * Returns the view's tool bar.
	 * @since jEdit 4.2pre1
	 */
    public Container getToolBar() {
        return toolBar;
    }

    /**
	 * Adds a tool bar to this view.
	 * @param toolBar The tool bar
	 */
    public void addToolBar(Component toolBar) {
    }

    /**
	 * Adds a tool bar to this view.
	 * @param group The tool bar group to add to
	 * @param toolBar The tool bar
	 * @see org.gjt.sp.jedit.gui.ToolBarManager
	 * @since jEdit 4.0pre7
	 */
    public void addToolBar(int group, Component toolBar) {
    }

    /**
	 * Removes a tool bar from this view.
	 * @param toolBar The tool bar
	 */
    public void removeToolBar(Component toolBar) {
    }

    /**
	 * Shows the wait cursor. This method and
	 * {@link #hideWaitCursor()} are implemented using a reference
	 * count of requests for wait cursors, so that nested calls work
	 * correctly; however, you should be careful to use these methods in
	 * tandem.<p>
	 *
	 * To ensure that {@link #hideWaitCursor()} is always called
	 * after a {@link #showWaitCursor()}, use a
	 * <code>try</code>/<code>finally</code> block, like this:
	 * <pre>try
	 *{
	 *    view.showWaitCursor();
	 *    // ...
	 *}
	 *finally
	 *{
	 *    view.hideWaitCursor();
	 *}</pre>
	 */
    public synchronized void showWaitCursor() {
        if (waitCount++ == 0) {
            Cursor cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
            setCursor(cursor);
            visit(new SetCursorVisitor(cursor));
        }
    }

    /**
	 * Hides the wait cursor.
	 */
    public synchronized void hideWaitCursor() {
        if (waitCount > 0) waitCount--;
        if (waitCount == 0) {
            Cursor cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
            setCursor(cursor);
            cursor = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
            visit(new SetCursorVisitor(cursor));
        }
    }

    /**
	 * Quick search.
	 * @since jEdit 4.0pre3
	 */
    public void quickIncrementalSearch(boolean word) {
    }

    /**
	 * Quick HyperSearch.
	 * @since jEdit 4.0pre3
	 */
    public void quickHyperSearch(boolean word) {
    }

    /**
	 * Shows the action bar if needed, and sends keyboard focus there.
	 * @since jEdit 4.2pre1
	 */
    public void actionBar() {
    }

    /**
	 * Returns the listener that will handle all key events in this
	 * view, if any.
	 * @return the key event interceptor or null
	 */
    public KeyListener getKeyEventInterceptor() {
        return null;
    }

    /**
	 * Sets the listener that will handle all key events in this
	 * view. For example, the complete word command uses this so
	 * that all key events are passed to the word list popup while
	 * it is visible.
	 * @param listener The key event interceptor.
	 */
    public void setKeyEventInterceptor(KeyListener listener) {
    }

    /**
	 * Sets the input handler.
	 * @param inputHandler The new input handler
	 */
    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    /**
	 * Forwards key events directly to the input handler.
	 * This is slightly faster than using a KeyListener
	 * because some Swing overhead is avoided.
	 */
    @Override
    public void processKeyEvent(KeyEvent evt) {
        inputHandler.processKeyEvent(evt, VIEW, false);
        if (!evt.isConsumed()) super.processKeyEvent(evt);
    }

    /**
	 * Forwards key events directly to the input handler.
	 * This is slightly faster than using a KeyListener
	 * because some Swing overhead is avoided.
	 */
    public void processKeyEvent(KeyEvent evt, boolean calledFromTextArea) {
        processKeyEvent(evt, calledFromTextArea ? TEXT_AREA : VIEW);
    }

    public static final int VIEW = 0;

    public static final int TEXT_AREA = 1;

    public static final int ACTION_BAR = 2;

    /**
	 * Forwards key events directly to the input handler.
	 * This is slightly faster than using a KeyListener
	 * because some Swing overhead is avoided.
	 */
    public void processKeyEvent(KeyEvent evt, int from) {
        processKeyEvent(evt, from, false);
    }

    /**
	 * Forwards key events directly to the input handler.
	 * This is slightly faster than using a KeyListener
	 * because some Swing overhead is avoided.
	 * @deprecated do not use, try {@link org.gjt.sp.jedit.gui.InputHandler#processKeyEvent(java.awt.event.KeyEvent, int, boolean)}
	 */
    @Deprecated
    public void processKeyEvent(KeyEvent evt, int from, boolean global) {
        inputHandler.processKeyEvent(evt, from, global);
        if (!evt.isConsumed()) super.processKeyEvent(evt);
    }

    /**
	 * Unsplits the view.
	 * @since jEdit 2.3pre2
	 */
    public void unsplit() {
    }

    /**
	 * Removes the current split.
	 * @since jEdit 2.3pre2
	 */
    public void unsplitCurrent() {
    }

    /**
	 * Restore the split configuration as it was before unsplitting.
	 *
	 * @since jEdit 4.3pre1
	 */
    public void resplit() {
        if (lastSplitConfig == null) getToolkit().beep(); else setSplitConfig(null, lastSplitConfig);
    }

    /**
	 * Moves keyboard focus to the next text area.
	 * @since jEdit 2.7pre4
	 */
    public void nextTextArea() {
    }

    /**
	 * Moves keyboard focus to the previous text area.
	 * @since jEdit 2.7pre4
	 */
    public void prevTextArea() {
    }

    /**
	 * Returns the top-level split pane, if any.
	 * @return the top JSplitPane if any.
	 * @since jEdit 2.3pre2
	 */
    public JSplitPane getSplitPane() {
        return splitPane;
    }

    /**
	 * Returns the current edit pane's buffer.
	 * @return the current edit pane's buffer, it can be null
	 */
    public Buffer getBuffer() {
        return null;
    }

    /**
	 * Sets the current edit pane's buffer.
	 * @param buffer The buffer
	 */
    public void setBuffer(Buffer buffer) {
        setBuffer(buffer, false);
    }

    /**
	 * Sets the current edit pane's buffer.
	 * @param buffer The buffer
	 * @param disableFileStatusCheck Disables file status checking
	 * regardless of the state of the checkFileStatus property
	 */
    public void setBuffer(Buffer buffer, boolean disableFileStatusCheck) {
        setBuffer(buffer, disableFileStatusCheck, true);
    }

    /**
	 * Sets the current edit pane's buffer.
	 * @param buffer The buffer
	 * @param disableFileStatusCheck Disables file status checking
	 * regardless of the state of the checkFileStatus property
	 * @param focus Whether the textarea should request focus
	 * @since jEdit 4.3pre13
	 */
    public void setBuffer(Buffer buffer, boolean disableFileStatusCheck, boolean focus) {
    }

    /**
	 * Returns the current edit pane's text area.
	 * @return the current edit pane's text area, or <b>null</b> if there is no edit pane yet
	 */
    public JEditTextArea getTextArea() {
        return null;
    }

    /**
	 * @return a ViewConfig instance for the current view
	 * @since jEdit 4.2pre1
	 */
    public ViewConfig getViewConfig() {
        ViewConfig config = new ViewConfig();
        config.plainView = isPlainView();
        config.splitConfig = getSplitConfig();
        config.extState = getExtendedState();
        String prefix = config.plainView ? "plain-view" : "view";
        switch(config.extState) {
            case Frame.MAXIMIZED_BOTH:
            case Frame.ICONIFIED:
                config.x = jEdit.getIntegerProperty(prefix + ".x", getX());
                config.y = jEdit.getIntegerProperty(prefix + ".y", getY());
                config.width = jEdit.getIntegerProperty(prefix + ".width", getWidth());
                config.height = jEdit.getIntegerProperty(prefix + ".height", getHeight());
                break;
            case Frame.MAXIMIZED_VERT:
                config.x = getX();
                config.y = jEdit.getIntegerProperty(prefix + ".y", getY());
                config.width = getWidth();
                config.height = jEdit.getIntegerProperty(prefix + ".height", getHeight());
                break;
            case Frame.MAXIMIZED_HORIZ:
                config.x = jEdit.getIntegerProperty(prefix + ".x", getX());
                config.y = getY();
                config.width = jEdit.getIntegerProperty(prefix + ".width", getWidth());
                config.height = getHeight();
                break;
            case Frame.NORMAL:
            default:
                config.x = getX();
                config.y = getY();
                config.width = getWidth();
                config.height = getHeight();
                break;
        }
        return config;
    }

    /**
	 * Returns true if this view has been closed with
	 * {@link jEdit#closeView(View)}.
	 * @return true if the view is closed
	 */
    public boolean isClosed() {
        return closed;
    }

    /**
	 * Returns true if this is an auxilliary view with no dockable windows.
	 * @return true if the view is plain
	 * @since jEdit 4.1pre2
	 */
    public boolean isPlainView() {
        return plainView;
    }

    /**
	 * Returns the next view in the list.
	 * @return the next view
	 */
    public View getNext() {
        return null;
    }

    /**
	 * Returns the previous view in the list.
	 * @return the preview view
	 */
    public View getPrev() {
        return null;
    }

    public void handleMessage(EBMessage msg) {
        if (msg instanceof PropertiesChanged) propertiesChanged(); else if (msg instanceof BufferUpdate) handleBufferUpdate((BufferUpdate) msg);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(0, 0);
    }

    /**
	 * This socket is closed when the buffer is closed.
	 */
    public void setWaitSocket(Socket waitSocket) {
        this.waitSocket = waitSocket;
    }

    @Override
    public String toString() {
        return getClass().getName() + '[' + (jEdit.getActiveView() == this ? "active" : "inactive") + ']';
    }

    /**
	 * Updates the title bar.
	 */
    public void updateTitle() {
    }

    public Component getPrefixFocusOwner() {
        return prefixFocusOwner;
    }

    public void setPrefixFocusOwner(Component prefixFocusOwner) {
        this.prefixFocusOwner = prefixFocusOwner;
    }

    /**
	 * Visit the the editpanes and textareas of the view
	 * @param visitor the visitor
	 * @since jEdit 4.3pre13
	 */
    public void visit(JEditVisitor visitor) {
    }

    void close() {
    }

    private boolean closed;

    private JPanel topToolBars;

    private JPanel bottomToolBars;

    private Container toolBar;

    private JSplitPane splitPane;

    private String lastSplitConfig;

    private InputHandler inputHandler;

    private Component prefixFocusOwner;

    private int waitCount;

    private boolean showFullPath;

    private boolean plainView;

    private Socket waitSocket;

    private String getSplitConfig() {
        StringBuilder splitConfig = new StringBuilder();
        if (splitPane != null) getSplitConfig(splitPane, splitConfig);
        return splitConfig.toString();
    }

    private static void getSplitConfig(JSplitPane splitPane, StringBuilder splitConfig) {
        Component right = splitPane.getRightComponent();
        appendToSplitConfig(splitConfig, right);
        splitConfig.append(' ');
        Component left = splitPane.getLeftComponent();
        appendToSplitConfig(splitConfig, left);
        splitConfig.append(' ');
        splitConfig.append(splitPane.getDividerLocation());
        splitConfig.append(' ');
        splitConfig.append(splitPane.getOrientation() == JSplitPane.VERTICAL_SPLIT ? "vertical" : "horizontal");
    }

    /**
	 * Append the Component to the split config.
	 * The component must be a JSplitPane or an EditPane
	 *
	 * @param splitConfig the split config
	 * @param component the component
	 */
    private static void appendToSplitConfig(StringBuilder splitConfig, Component component) {
        if (component instanceof JSplitPane) {
            getSplitConfig((JSplitPane) component, splitConfig);
        }
    }

    private void setSplitConfig(Buffer buffer, String splitConfig) {
        try {
            Component comp = restoreSplitConfig(buffer, splitConfig);
        } catch (IOException e) {
            throw new InternalError();
        }
    }

    private Component restoreSplitConfig(Buffer buffer, String splitConfig) throws IOException {
        Buffer[] buffers = jEdit.getBuffers();
        Stack stack = new Stack();
        StreamTokenizer st = new StreamTokenizer(new StringReader(splitConfig));
        st.whitespaceChars(0, ' ');
        st.wordChars('#', '~');
        st.commentChar('!');
        st.quoteChar('"');
        st.eolIsSignificant(false);
        boolean continuousLayout = jEdit.getBooleanProperty("appearance.continuousLayout");
        List<Buffer> editPaneBuffers = new ArrayList<Buffer>();
        loop: while (true) {
            switch(st.nextToken()) {
                case StreamTokenizer.TT_EOF:
                    break loop;
                case StreamTokenizer.TT_WORD:
                    if (st.sval.equals("vertical") || st.sval.equals("horizontal")) {
                        int orientation = st.sval.equals("vertical") ? JSplitPane.VERTICAL_SPLIT : JSplitPane.HORIZONTAL_SPLIT;
                        int divider = ((Integer) stack.pop()).intValue();
                        stack.push(splitPane = new JSplitPane(orientation, continuousLayout, (Component) stack.pop(), (Component) stack.pop()));
                        splitPane.setOneTouchExpandable(true);
                        splitPane.setBorder(null);
                        splitPane.setMinimumSize(new Dimension(0, 0));
                        splitPane.setDividerLocation(divider);
                    } else if (st.sval.equals("buffer")) {
                        Object obj = stack.pop();
                        if (obj instanceof Integer) {
                            int index = ((Integer) obj).intValue();
                            if (index >= 0 && index < buffers.length) buffer = buffers[index];
                        } else if (obj instanceof String) {
                            String path = (String) obj;
                            buffer = jEdit.getBuffer(path);
                        }
                        if (buffer == null) buffer = jEdit.getFirstBuffer();
                    } else if (st.sval.equals("buff")) {
                        String path = (String) stack.pop();
                        buffer = jEdit.getBuffer(path);
                        if (buffer == null) {
                            Log.log(Log.WARNING, this, "Error buffer " + path + " doesn't exists");
                        } else {
                            editPaneBuffers.add(buffer);
                        }
                    } else if (st.sval.equals("bufferset")) {
                    }
                    break;
                case StreamTokenizer.TT_NUMBER:
                    stack.push((int) st.nval);
                    break;
                case '"':
                    stack.push(st.sval);
                    break;
            }
        }
        updateGutterBorders();
        return (Component) stack.peek();
    }

    /**
	 * Reloads various settings from the properties.
	 */
    private void propertiesChanged() {
        loadToolBars();
        showFullPath = jEdit.getBooleanProperty("view.showFullPath");
        updateTitle();
        boolean showStatus = plainView ? jEdit.getBooleanProperty("view.status.plainview.visible") : jEdit.getBooleanProperty("view.status.visible");
        if (jEdit.getBooleanProperty("view.toolbar.alternateLayout")) {
            getContentPane().add(BorderLayout.NORTH, topToolBars);
            getContentPane().add(BorderLayout.SOUTH, bottomToolBars);
        }
        getRootPane().revalidate();
    }

    private void loadToolBars() {
    }

    private void handleBufferUpdate(BufferUpdate msg) {
    }

    /**
	 * Updates the borders of all gutters in this view to reflect the
	 * currently focused text area.
	 * @since jEdit 2.6final
	 */
    private void updateGutterBorders() {
    }

    private class CaretHandler implements CaretListener {

        public void caretUpdate(CaretEvent evt) {
        }
    }

    private class FocusHandler extends FocusAdapter {

        @Override
        public void focusGained(FocusEvent evt) {
        }
    }

    private class WindowHandler extends WindowAdapter {

        @Override
        public void windowActivated(WindowEvent evt) {
            boolean editPaneChanged = jEdit.getActiveViewInternal() != View.this;
            jEdit.setActiveView(View.this);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                }
            });
            if (editPaneChanged) {
                EditBus.send(new ViewUpdate(View.this, ViewUpdate.ACTIVATED));
            }
        }

        @Override
        public void windowClosing(WindowEvent evt) {
            jEdit.closeView(View.this);
        }
    }

    public static class ViewConfig {

        public boolean plainView;

        public String splitConfig;

        public int x, y, width, height, extState;

        public String top, left, bottom, right;

        public int topPos, leftPos, bottomPos, rightPos;

        public ViewConfig() {
        }

        public ViewConfig(boolean plainView) {
            this.plainView = plainView;
            String prefix = plainView ? "plain-view" : "view";
            x = jEdit.getIntegerProperty(prefix + ".x", 0);
            y = jEdit.getIntegerProperty(prefix + ".y", 0);
            width = jEdit.getIntegerProperty(prefix + ".width", 0);
            height = jEdit.getIntegerProperty(prefix + ".height", 0);
            extState = jEdit.getIntegerProperty(prefix + ".extendedState", NORMAL);
        }

        public ViewConfig(boolean plainView, String splitConfig, int x, int y, int width, int height, int extState) {
            this.plainView = plainView;
            this.splitConfig = splitConfig;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.extState = extState;
        }
    }

    private static class MyFocusTraversalPolicy extends LayoutFocusTraversalPolicy {

        @Override
        public Component getDefaultComponent(Container focusCycleRoot) {
            return null;
        }
    }

    private static class SetCursorVisitor extends JEditVisitorAdapter {

        private final Cursor cursor;

        SetCursorVisitor(Cursor cursor) {
            this.cursor = cursor;
        }
    }
}
