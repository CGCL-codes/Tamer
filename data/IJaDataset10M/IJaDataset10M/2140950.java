package org.axed.user.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import java.util.Vector;
import org.axed.user.client.util.FontSizeListener;
import org.axed.user.client.util.FontSizeWatcher;
import org.timepedia.exporter.client.Exportable;

/**
 * This is the <b>a</b>britrary e<b>x</b>tentable <b>ed</b>itor widget.
 *
 * It handles all browser events and forwards them to its listeners.
 * @gwt.exportPackage axed
 * @gwt.export
 */
public class AxedArea extends CaretWidget implements FontSizeListener, HasFocus, UncaughtExceptionHandler, Exportable {

    /**
	 * The version string.
	 */
    public static final String VERSION = "1.1";

    /**
	 * Various debugging variables.
	 * Set true to see corresponding debug messages.
	 */
    public static final boolean DEBUG_CURSOR_SET = false;

    public static final boolean DEBUG_KEYBOARD_EVENTS = false;

    public static final boolean DEBUG_FOCUS_EVENTS = false;

    /**
	 * Font style enumeration.
	 */
    public static final int FONT_MONOSPACE = 0;

    public static final int FONT_SERIF = 1;

    /**
	 * Default priorities for internal event listeners.
	 */
    public static final int LISTENER_PRIORITY_INPUTHANDLER = 1000;

    public static final int LISTENER_PRIORITY_DOCUMENT = 2000;

    public static final int LISTENER_PRIORITY_CURSOR = 2100;

    public static final int LISTENER_PRIORITY_UNDOMANAGER = 3000;

    public static final int LISTENER_PRIORITY_CARET = 4000;

    public static final int LISTENER_PRIORITY_INPUTCATCHER = 4001;

    public static final int LISTENER_PRIORITY_EXTEND = 5000;

    /**
	 * The next event atomic id that will be generated.
	 */
    private int nextAtomicID = 1;

    /**
	 * If true, Axed will handle no event at all.
	 */
    boolean disable = false;

    /**
	 * The undo manager handles undo/redo lists.
	 */
    UndoManager undoManager;

    /**
	 * The implementation that gets all keystroke events, and sends
	 * the approperiate AxedMessages.
	 */
    public InputCatcher inputCatcher;

    /**
	 * The document that contains and handles all the doc data.
	 */
    public Document document;

    /**
	 * The input handler;
	 */
    InputBase inputHandler;

    /**
	 * The default input handler;
	 */
    InputBase defaultInputHandler;

    /**
	 * true if left mousebutton is down
	 */
    boolean mouseDown = false;

    /**
	 * true if this AxedWidged thinks it has the focus.
	 */
    boolean haveFocus = false;

    /**
	 * A Pool of event objects, so they don't have to be 
	 * created/destroyed all the time, but can be recycled.
	 */
    private AxedEventPool eventPool = new AxedEventPool(this);

    /**
	 * The caret object.
	 */
    Caret caret;

    /**
	 * A label to print some debug messages to.
	 */
    public static Label debugLabel = null;

    /**
	 * All Listeners that want to receive events
	 */
    AxedListenerCollection listeners = new AxedListenerCollection();

    /**	
	 * Constructor.
	 */
    public AxedArea(Element setDiv, int setWidth, Label setDebugLabel) {
        super();
        if (setDiv == null) {
            outDiv = DOM.createDiv();
        } else {
            outDiv = setDiv;
        }
        this.setWidth = setWidth;
        DOM.setStyleAttribute(outDiv, "width", setWidth + "px");
        DOM.setStyleAttribute(outDiv, "height", setHeight + "px");
        DOM.setStyleAttribute(outDiv, "zIndex", "0");
        DOM.setStyleAttribute(outDiv, "cursor", "text");
        DOM.setStyleAttribute(outDiv, "overflow", "hidden");
        DOM.setStyleAttribute(outDiv, "position", "relative");
        setElement(outDiv);
        scrollDiv = DOM.createDiv();
        DOM.setStyleAttribute(scrollDiv, "height", setHeight + "px");
        DOM.setStyleAttribute(scrollDiv, "overflow", "auto");
        DOM.setStyleAttribute(scrollDiv, "backgroundColor", "transparent");
        DOM.setStyleAttribute(scrollDiv, "position", "relative");
        DOM.setStyleAttribute(scrollDiv, "zIndex", "1");
        DOM.setStyleAttribute(scrollDiv, "cursor", "text");
        DOM.setStyleAttribute(scrollDiv, "paddingLeft", borderLeft + "px");
        DOM.setStyleAttribute(scrollDiv, "paddingRight", borderRight + "px");
        DOM.setStyleAttribute(scrollDiv, "paddingRight", borderRight + "px");
        DOM.appendChild(outDiv, scrollDiv);
        document = new Document(this, scrollDiv);
        defaultInputHandler = inputHandler = new UserInput(this);
        addListener(inputHandler, LISTENER_PRIORITY_INPUTHANDLER);
        addListener(inputHandler.cursor, LISTENER_PRIORITY_CURSOR);
        addListener(document, LISTENER_PRIORITY_DOCUMENT);
        undoManager = new UndoManager(this);
        addListener(undoManager, LISTENER_PRIORITY_UNDOMANAGER);
        caret = new Caret(this);
        addListener(caret, LISTENER_PRIORITY_CARET);
        inputCatcher = new InputCatcher(this, caret);
        RootPanel.get().add(inputCatcher);
        addListener(inputCatcher, LISTENER_PRIORITY_INPUTCATCHER);
        if (setDebugLabel != null) {
            debugLabel = setDebugLabel;
        }
        sinkEvents(Event.ONSCROLL);
        sinkEvents(Event.ONCLICK);
        sinkEvents(Event.ONMOUSEDOWN);
        sinkEvents(Event.ONMOUSEUP);
        sinkEvents(Event.ONMOUSEMOVE);
        DOM.setEventListener(outDiv, this);
        FontSizeWatcher.addListener(this);
        registerScroll(this, scrollDiv);
        if (GWT.isScript()) {
            GWT.setUncaughtExceptionHandler(this);
        } else {
        }
        _invalidateIESelect(outDiv);
    }

    /**
	 * Adds a listener interface to receive mouse events.
	 */
    public void addFocusListener(FocusListener listener) {
        inputCatcher.addFocusListener(listener);
    }

    /**
	 * Adds a listener interface to receive keyboard events.
	 */
    public void addKeyboardListener(KeyboardListener listener) {
        inputCatcher.addKeyboardListener(listener);
    }

    /**
	 * Adds an event listener to this widget.
	 *
	 * @param listener the listener to add.
	 * @param priority an index in which order the listeners are to be called.
	 */
    public void addListener(AxedListener listener, int priority) {
        listeners.addListener(listener, priority);
    }

    /**
	 * Clears all text.
	 */
    public void clear(int atomicID) {
        removeText(atomicID, 0, 0, getLineCount() - 1, getLineLength(getLineCount() - 1));
    }

    /**
	 * Removes all event listeners.
	 */
    public void clearListeners() {
        listeners.clear();
    }

    /**
	 * clears the undo stack, edits before this cannot be undone.
	 */
    public void clearUndoRedoStack() {
        AxedEvent e = eventPool.newEvent(createAtomicID(), AxedEvent.CLEARUNDOREDOSTACK);
        e.getClearUndoRedoStack().issue(AxedEvent.CLEARUNDOREDOSTACK);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Returns line if within document, 0 if before, or lineCount if after
	 */
    public int confineLine(int line) {
        if (line < 0) {
            return 0;
        }
        int lc = getLineCount() - 1;
        if (line > lc) {
            return lc;
        }
        return line;
    }

    /**
	 * Creates and returns an atomic event id to chain events.
	 */
    public int createAtomicID() {
        return nextAtomicID++;
    }

    /**
	 * cuts the selection.
	 */
    public void cut() {
        AxedEvent e = eventPool.newEvent(createAtomicID(), AxedEvent.CUT);
        e.getCut().issue(AxedEvent.CUT);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Adds a debug test to the debugLabel if flag = true.
	 */
    public static void debug(boolean flag, String text) {
        if (flag) {
            debugLabel.setText(debugLabel.getText() + text + " ");
        }
    }

    /**
	 * Finds the next occurence of text after startLine and startCol
	 */
    public boolean find(String text, int startLine, int startCol, FindResult fr) {
        return document.find(text, startLine, startCol, Document.OPERATE_WRAP, -1, fr);
    }

    /**
	 * Searches the next occurence of regular expression after startLine and startCol
	 */
    public boolean match(String expr, int startLine, int startCol, FindResult fr, boolean matchStart) {
        return document.match(expr, startLine, startCol, Document.OPERATE_WRAP, -1, fr, matchStart);
    }

    /**
	 * Searches the next occurence of regular expression after startLine and startCol
	 */
    public boolean match(String expr, AxedPointer start, FindResult fr, boolean matchStart) {
        return document.match(expr, start.line, start.col, Document.OPERATE_WRAP, -1, fr, matchStart);
    }

    /**
	 * Replaces all occurences of expr with replacement
	 *
	 * @param global if false replace only the first occurance of each line
	 *
	 * @return the number replacements done
	 */
    public int replaceExpr(int atomicID, String expr, String replacement, AxedInterval iv, boolean global) {
        AxedEvent e = eventPool.newEvent(createAtomicID(), AxedEvent.REPLACE_EXPR);
        e.getReplaceExpr().issue(AxedEvent.REPLACE_EXPR, expr, replacement, iv, global);
        listeners.fireEvent(e);
        int r = e.getReplaceExpr().replaceNum;
        eventPool.releaseEvent(e);
        return r;
    }

    /**
	 * Finds the column from a character in a line given top and left coordinates.
	 * Parameters start and forward give hints to get a faster solution.
	 *
	 * @param line          the line to search in.
	 * @param targetTop     the top coord to find.
	 * @param targetLeft    the left coord to find.
	 * @param start         the column to start looking
	 * @param forward       true: looking forward, false: looking backward
	 */
    public int findColPos(int line, int targetTop, int targetLeft, int start, boolean forward) {
        return document.getLine(line).findColPos(targetTop, targetLeft, start, forward);
    }

    /**
	 * Finds the column from a line-wrap.
	 *
	 * @param line          the line to search in.
	 * @param targetTop     the top coord to find.
	 * @param start         the column to start looking
	 * @param forward       true: looking forward, false: looking backward
	 */
    public int findWrap(int line, int targetTop, int start, boolean forward) {
        return document.getLine(line).findWrap(targetTop, start, forward);
    }

    /**
	 * Returns the coordinates of a character.
	 */
    public void getCoords(int line, int col, Coords coords) {
        document.getLine(line).getCoords(col, coords);
    }

    /**
	 * Returns the coordinates of a character.
	 */
    public void getCoords(AxedPointer p, Coords coords) {
        document.getLine(p.line).getCoords(p.col, coords);
    }

    /**
	 * Return the coordinates of the cursor
	 */
    public void getCursorCoords(Coords coords) {
        inputHandler.getCursorCoords(coords);
    }

    /**
	 * Returns the caret object.
	 * @gwt.noexport
	 */
    public Caret getCaret() {
        return caret;
    }

    /**
 	 * Returns the column the cursor is in. That is elements from Line start.
 	 */
    public int getCursorCol() {
        return inputHandler.getCursorCol();
    }

    /**
	 * Returns the linenumber the cursor is in.
	 */
    public int getCursorLine() {
        return inputHandler.getCursorLine();
    }

    /**
	 * Returns the offset bottom of a line.
	 */
    public int getLineBottom(int linenr) {
        return document.getLine(linenr).getCurses().getBottom();
    }

    /**
	 * Returns the element of a line.
	 */
    public Element getLineElement(int linenr) {
        return document.getLine(linenr).getCurses().getElement();
    }

    /**
	 * Returns the offset btop of a line.
	 */
    public int getLineTop(int linenr) {
        return document.getLine(linenr).getCurses().getTop();
    }

    /**
	 * Returns the length of a line.
	 */
    public int getLineLength(int linenr) {
        return document.getLine(linenr).size();
    }

    /**
	 * Returns the number of lines
	 */
    public int getLineCount() {
        return document.getLineCount();
    }

    /**
	 * Returns the scroll div element.
	 */
    public Element getScrollDiv() {
        return scrollDiv;
    }

    /**
	 * Gets the widget's position in the tab index.
	 */
    public int getTabIndex() {
        return inputCatcher.getTabIndex();
    }

    /**
	 * Returns the character at line/col.
	 */
    public char getChar(int line, int col) {
        return document.getChar(line, col);
    }

    /**
	 * Returns the character at pointer.
	 */
    public char getChar(AxedPointer p) {
        return document.getChar(p.line, p.col);
    }

    /**
	 * Returns the document contents.
	 */
    public String getText1() {
        return document.getText();
    }

    /**
	 * Returns the text of a line.
	 */
    public String getText(int linenr) {
        return document.getLine(linenr).text();
    }

    /**
	 * Returns the text of from a line to a line.
	 */
    public String getText(int line1, int line2) {
        return document.getText(line1, line2);
    }

    /**
	 * Returns the text from line1/col to line2/col2
	 */
    public String getText(int line1, int col1, int line2, int col2) {
        return document.getText(line1, col1, line2, col2);
    }

    /**
	 * Returns the text from pointer to pointer.
     * @gwt.noexport
	 */
    public String getText(AxedPointer p1, AxedPointer p2) {
        return document.getText(p1.line, p1.col, p2.line, p2.col);
    }

    /**
	 * Returns the text from an interval.
     * @gwt.noexport
	 */
    public String getText(AxedInterval p) {
        return document.getText(p.p1.line, p.p1.col, p.p2.line, p.p2.col);
    }

    /**
	 * Retrieves the undo manager.
     * @gwt.noexport
	 */
    public UndoManager getUndoManager() {
        return undoManager;
    }

    /**
	 * Returns the width of the widget.
	 */
    public int getWidth() {
        return DOM.getElementPropertyInt(outDiv, "offsetWidth");
    }

    /**
	 * Inserts the text into the document.
	 *
	 * @param text   the text to insert.
	 */
    public void insertText(int atomicID, int line1, int col1, String text, AxedPointer cp) {
        assert atomicID != 0;
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.INSERT_TEXT);
        e.getInsertText().issue(AxedEvent.INSERT_TEXT, line1, col1, text);
        listeners.fireEvent(e);
        if (cp != null) {
            cp.set(e.getInsertText().iv.p2);
        }
        eventPool.releaseEvent(e);
    }

    /**
	 * Inserts the text into the document.
	 *
	 * @param atomicID the id of this change.
	 * @param p        the point where to insert to.
	 * @param text     the text to insert.
	 * @param cp       reference parameter, if not null set to end of insertion 
	 *
	 */
    public void insertText(int atomicID, AxedPointer p, String text, AxedPointer cp) {
        assert atomicID != 0;
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.INSERT_TEXT);
        e.getInsertText().issue(AxedEvent.INSERT_TEXT, p, text);
        listeners.fireEvent(e);
        if (cp != null) {
            cp.set(e.getInsertText().iv.p2);
        }
        eventPool.releaseEvent(e);
    }

    /**
	 * Inserts a char to the document.
	 */
    public void insertChar(int atomicID, int line, int col, char ch) {
        assert atomicID != 0;
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.INSERT_CHAR);
        e.getInsertChar().issue(AxedEvent.INSERT_CHAR, line, col, ch, 0);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Inserts a char to the document.
	 */
    public void insertChar(int atomicID, AxedPointer p, char ch) {
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.INSERT_CHAR);
        e.getInsertChar().issue(AxedEvent.INSERT_CHAR, p, ch, 0);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Joins a line with its follower
	 */
    public void joinLine(int atomicID, int line) {
        assert atomicID != 0;
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.JOIN_LINE);
        e.getJoinLine().issue(AxedEvent.JOIN_LINE, line, getLineLength(line));
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Returns the line height
	 */
    public int lineHeight() {
        return document.lineHeight;
    }

    /**
	 * Handles a change of line height.
	 */
    public void lineHeightChanged(int line) {
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.LINE_HEIGHT_CHANGED);
        e.getLineHeightChanged().issue(AxedEvent.LINE_HEIGHT_CHANGED, line);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Moves the cursor to the line/col specified.
	 */
    public void moveCursor(int line, int col) {
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.MOVE_CURSOR);
        e.getMoveCursor().issue(AxedEvent.MOVE_CURSOR, line, col, AxedEvent.MOVETYPE_EXTERN);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Moves the cursor to the pointer specified.
	 * @gwt.noexport
	 */
    public void moveCursor(AxedPointer p) {
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.MOVE_CURSOR);
        e.getMoveCursor().issue(AxedEvent.MOVE_CURSOR, p, AxedEvent.MOVETYPE_EXTERN);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Moves the cursor to the line/col specified.
	 * @gwt.noexport
	 */
    public void moveCursor(int line, int col, int movetype) {
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.MOVE_CURSOR);
        e.getMoveCursor().issue(AxedEvent.MOVE_CURSOR, line, col, movetype);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Moves the cursor to the pointer specified.
	 * @gwt.noexport
	 */
    public void moveCursor(AxedPointer p, int movetype) {
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.MOVE_CURSOR);
        e.getMoveCursor().issue(AxedEvent.MOVE_CURSOR, p, movetype);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Create a keypress
	 */
    public void keypress(int key, char ch, boolean shift, boolean ctrl) {
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.KEY_PRESS);
        e.getKeyPress().issue(AxedEvent.KEY_PRESS, key, ch, shift, ctrl);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Handles all Browserevents for the axed widget.
	 */
    public void onBrowserEvent(Event event) {
        if (disable) {
            return;
        }
        switch(DOM.eventGetType(event)) {
            case Event.ONSCROLL:
                return;
            case Event.ONCLICK:
                {
                    AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.CLICK);
                    e.getClick().issue(AxedEvent.CLICK, DOM.eventGetClientX(event), DOM.eventGetClientY(event), DOM.eventGetTarget(event));
                    listeners.fireEvent(e);
                    eventPool.releaseEvent(e);
                    return;
                }
            case Event.ONMOUSEDOWN:
                {
                    int x = DOM.eventGetClientX(event) + Window.getScrollLeft();
                    int y = DOM.eventGetClientY(event) + Window.getScrollTop();
                    Element t = DOM.eventGetTarget(event);
                    setFocus(true);
                    if (t.equals(outDiv) || x >= getAbsoluteInnerRight() || y >= getAbsoluteInnerBottom()) {
                        return;
                    }
                    mouseDown = true;
                    DOM.setCapture(outDiv);
                    DOM.eventPreventDefault(event);
                    AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.MOUSE_DOWN);
                    e.getMouseDown().issue(AxedEvent.MOUSE_DOWN, x, y, t);
                    listeners.fireEvent(e);
                    eventPool.releaseEvent(e);
                    return;
                }
            case Event.ONMOUSEUP:
                {
                    int x = DOM.eventGetClientX(event) + Window.getScrollLeft();
                    int y = DOM.eventGetClientY(event) + Window.getScrollTop();
                    Element t = DOM.eventGetTarget(event);
                    mouseDown = false;
                    setFocus(true);
                    DOM.releaseCapture(outDiv);
                    AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.MOUSE_UP);
                    e.getMouseUp().issue(AxedEvent.MOUSE_UP, x, y, t);
                    listeners.fireEvent(e);
                    eventPool.releaseEvent(e);
                }
                return;
            case Event.ONMOUSEMOVE:
                {
                    if (!mouseDown) {
                        return;
                    }
                    int x = DOM.eventGetClientX(event) + Window.getScrollLeft();
                    int y = DOM.eventGetClientY(event) + Window.getScrollTop();
                    Element t = DOM.eventGetTarget(event);
                    AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.MOUSE_MOVE);
                    e.getMouseMove().issue(AxedEvent.MOUSE_MOVE, x, y, t);
                    listeners.fireEvent(e);
                    eventPool.releaseEvent(e);
                }
                return;
        }
    }

    /**
	 * Fired when a widget receives keyboard focus.
	 */
    public void onFocus(Widget sender) {
        if (disable) {
            return;
        }
        debug(DEBUG_FOCUS_EVENTS, "F");
        if (!haveFocus) {
            AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.FOCUS);
            e.getFocus().issue(AxedEvent.FOCUS);
            listeners.fireEvent(e);
            eventPool.releaseEvent(e);
            haveFocus = true;
        }
    }

    /**
	 * Called by the FontSizeWatcher shortly after the browser changed its fontsize.
	 */
    public void onFontSizeChange(Element testCase) {
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.FONT_SIZE_CHANGED);
        e.getFontChange().issue(AxedEvent.FONT_SIZE_CHANGED, testCase);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Fired when a widget loses keyboard focus.
	 */
    public void onLostFocus(Widget sender) {
        if (disable) {
            return;
        }
        debug(DEBUG_FOCUS_EVENTS, "B");
        if (!mouseDown) {
            haveFocus = false;
            AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.BLUR);
            e.getBlur().issue(AxedEvent.BLUR);
            listeners.fireEvent(e);
            eventPool.releaseEvent(e);
        }
    }

    /**
	 * Called by GWT when something bad happened.
	 */
    public void onUncaughtException(Throwable e) {
        boolean a = Window.confirm("Oh dear! An internal error in Axed has happened :-(\n" + "\n" + e.getMessage() + "\n\n" + "Press OK if you want me to try to ressurect your data. Please try to rescue it to your clipboard\n\n" + "Please send a bug report to http://code.google.com/p/axedarea/issues.\n");
        if (!a) {
            return;
        }
        String ps = "Please send a bug report to <a href=\"http://code.google.com/p/axedarea/issues\">http://code.google.com/p/axedarea/issues</a> including at least<br/>";
        String s = "  (Instructions how to raise this error)\n" + "  Axed-version: " + VERSION + "\n" + "  Error message " + e.getMessage() + "\n" + "\n\n" + "RESCUED TEXT FOLLOWS: \n" + "   ( After you have copied the text to clipboard, you might want to press [Refresh].\n" + "     Many sorries! )\n";
        s += "=============================================================================================\n";
        try {
            int il = document.getLineCount();
            for (int i = 0; i < il; i++) {
                Line l = document.getLine(i);
                s += l.text.text.toString();
                s += "\n";
            }
            s += "=============================================================================================";
            s = SpaceString.stringToHTML(s);
            s = s.replaceAll("\n", "<br/>");
            s = ps + s;
            Element d = RootPanel.get().getElement();
            DOM.setInnerHTML(d, s);
            DOM.setStyleAttribute(d, "backgroundColor", "#FFAAAA");
            disable = true;
        } catch (Throwable ee) {
            s = "Ressurection failed! :-(( Thats what was repaired:\n\n" + s;
            s = SpaceString.stringToHTML(s);
            s = s.replaceAll("\n", "<br/>");
            s += "=============================================================================================";
            s = ps + s;
            Element d = RootPanel.get().getElement();
            DOM.setInnerHTML(d, s);
            DOM.setStyleAttribute(d, "backgroundColor", "#FFAAAA");
            disable = true;
        }
    }

    /**
	 * Paste text
	 *
	 * TODO why not inserttext?
	 */
    public void paste(int atomicID, String text) {
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.PASTE);
        e.getPaste().issue(AxedEvent.PASTE, text);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * redoes a change.
	 */
    public void redo() {
        AxedEvent e = eventPool.newEvent(createAtomicID(), AxedEvent.REDO);
        e.getRedo().issue(AxedEvent.REDO);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Refreshes some changes, like different absolute position
	 */
    public void refresh() {
        AxedEvent e = eventPool.newEvent(createAtomicID(), AxedEvent.REFRESH);
        e.getRefresh().issue(AxedEvent.REFRESH);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Removes a previously added listener interface.
	 */
    public void removeFocusListener(FocusListener listener) {
        inputCatcher.removeFocusListener(listener);
    }

    /**
	 * Removes a previously added listener interface.
	 */
    public void removeKeyboardListener(KeyboardListener listener) {
        inputCatcher.removeKeyboardListener(listener);
    }

    /**
	 * Removes an event listener from this widget.
	 */
    public void removeListener(AxedListener listener) {
        listeners.remove(listener);
    }

    /**
	 * Removes a char from the document.
	 */
    public void removeChar(int atomicID, int line, int col, int tag) {
        assert atomicID != 0;
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.REMOVE_CHAR);
        e.getRemoveChar().issue(AxedEvent.REMOVE_CHAR, line, col, (char) 0, tag);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Removes a char from the document.
	 */
    public void removeChar(int atomicID, AxedPointer p, int tag) {
        assert atomicID != 0;
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.REMOVE_CHAR);
        e.getRemoveChar().issue(AxedEvent.REMOVE_CHAR, p, (char) 0, tag);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Removes text from begin to end column in linenr.
	 */
    public void removeText(int atomicID, int line, int begin, int end) {
        assert atomicID != 0;
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.REMOVE_TEXT);
        e.getRemoveText().issue(AxedEvent.REMOVE_TEXT, line, begin, line, end);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Removes text from begin to end column in linenr.
	 */
    public void removeText(int atomicID, int line1, int begin, int line2, int end) {
        assert atomicID != 0;
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.REMOVE_TEXT);
        e.getRemoveText().issue(AxedEvent.REMOVE_TEXT, line1, begin, line2, end);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Removes text from pointer to pointer.
	 */
    public void removeText(int atomicID, AxedPointer p1, AxedPointer p2) {
        assert atomicID != 0;
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.REMOVE_TEXT);
        e.getRemoveText().issue(AxedEvent.REMOVE_TEXT, p1, p2);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Removes text from interval
	 */
    public void removeText(int atomicID, AxedInterval iv) {
        assert atomicID != 0;
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.REMOVE_TEXT);
        e.getRemoveText().issue(AxedEvent.REMOVE_TEXT, iv);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    public void removeLine(int atomicID, int line) {
        assert atomicID != 0;
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.REMOVE_TEXT);
        e.getRemoveText().issue(AxedEvent.REMOVE_TEXT, line, 0, line + 1, 0);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    public void removeLines(int atomicID, int line, int count) {
        assert atomicID != 0;
        int el = line + count;
        if (el > getLineCount() - 1) {
            el = getLineCount() - 1;
        }
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.REMOVE_TEXT);
        e.getRemoveText().issue(AxedEvent.REMOVE_TEXT, line, 0, el, 0);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * replaces the input handler by a custom one
	 */
    public void replaceInputHandler(InputBase ih) {
        removeListener(inputHandler);
        removeListener(inputHandler.cursor);
        ih.cursor.set(inputHandler.cursor);
        ih.lastAtomicID = inputHandler.lastAtomicID;
        inputHandler = ih;
        addListener(ih, LISTENER_PRIORITY_INPUTHANDLER);
        addListener(ih.cursor, LISTENER_PRIORITY_CURSOR);
    }

    /**
	 * restore the default input handler.
	 */
    public void restoreInputHandler() {
        if (inputHandler == defaultInputHandler) {
            return;
        }
        replaceInputHandler(defaultInputHandler);
    }

    /**
	 * Scroll the axedWidget so that 'line' is on top of the viewarea.
	 */
    public void scrollTopToLine(int line) {
        setScrollTop(document.getLine(line).curs.getTop());
    }

    /**
	 * Sets the widget's 'access key'.
	 */
    public void setAccessKey(char key) {
        inputCatcher.setAccessKey(key);
    }

    /**
	 * shows/hides the caret. This is different than 
	 * setFocus(false) in that the focus is kept,
	 * but just (default) caret set invisible.
	 */
    public void setCaretEnabled(boolean enabled) {
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.SET_CARET_ENABLED);
        e.getSetCaretEnable().issue(AxedEvent.SET_CARET_ENABLED, enabled);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
     * Explicitly focus/unfocus the Axed widget.
	 *
	 * @param focus   true... focus, false... blur
	 */
    public void setFocus(boolean focus) {
        inputCatcher.setFocus(focus);
    }

    /**
	 * Sets the font of the widget.
	 *
	 * @param font A int value of the FONT_* enumaration.
	 */
    public void setFont(int font) {
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.SET_FONT);
        e.getSetFont().issue(AxedEvent.SET_FONT, font);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
        onFontSizeChange(FontSizeWatcher.getTestCase());
    }

    /**
	 * Sets the vertical scrollposition so that the bottom  of the 
	 * viewbox is at scrollBottom.
	 *
	 * @param scrollBottom buttom to scroll the bottom of the viewbox to.
	 */
    public void setScrollBottom(int scrollBottom) {
        super.setScrollBottom(scrollBottom);
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.SCROLL);
        e.getScroll().issue(AxedEvent.SCROLL);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Sets the horizonal scrollposition.
	 *
	 * @param scrollLeft the position to scroll to.
	 */
    public void setScrollLeft(int scrollLeft) {
        super.setScrollLeft(scrollLeft);
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.SCROLL);
        e.getScroll().issue(AxedEvent.SCROLL);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Sets the horizontal scrollposition so that the right end 
	 * of the viewbox is at scrollRight.
	 *
	 * @param scrollRight the position to scroll to.
	 */
    public void setScrollRight(int scrollRight) {
        super.setScrollRight(scrollRight);
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.SCROLL);
        e.getScroll().issue(AxedEvent.SCROLL);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Sets the vertical scrollposition.
	 *
	 * @param scrollTop the position to scroll to.
	 */
    public void setScrollTop(int scrollTop) {
        super.setScrollTop(scrollTop);
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.SCROLL);
        e.getScroll().issue(AxedEvent.SCROLL);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Sets the selection.
	 */
    public void setSelection(int line1, int col1, int line2, int col2) {
        inputHandler.setSelection(line1, col1, line2, col2);
    }

    /**
	 * Sets the selection.
	 */
    public void setSelection(AxedPointer p1, AxedPointer p2) {
        inputHandler.setSelection(p1.line, p1.col, p2.line, p2.col);
    }

    /**
	 * Sets the selection.
	 */
    public void setSelection(AxedInterval p) {
        inputHandler.setSelection(p.p1.line, p.p1.col, p.p2.line, p.p2.col);
    }

    /**
	 * Sets the widget's 'access key'.
	 */
    public void setTabIndex(int index) {
        inputCatcher.setTabIndex(index);
    }

    /**
	 * Sets wrapping true or false.
	 */
    public void setWrap(boolean wrap) {
        document.setWrap(wrap);
    }

    /**
	 * Returns the size of the document in bytes
	 * @return size
	 */
    public int size() {
        return document.size();
    }

    /**
	 * Splits a line and inserts a new one with the remaining text.
	 */
    public void splitLine(int atomicID, int line, int col) {
        assert atomicID != 0;
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.SPLIT_LINE);
        e.getSplitLine().issue(AxedEvent.SPLIT_LINE, line, col);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Splits a line and inserts a new one with the remaining text.
	 */
    public void splitLine(int atomicID, AxedPointer p) {
        assert atomicID != 0;
        AxedEvent e = eventPool.newEvent(atomicID, AxedEvent.SPLIT_LINE);
        e.getSplitLine().issue(AxedEvent.SPLIT_LINE, p);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * undoes a change.
	 */
    public void undo() {
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.UNDO);
        e.getUndo().issue(AxedEvent.UNDO);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    /**
	 * Removes the selection.
	 */
    public void unselect() {
        inputHandler.unselect();
    }

    private static native void _invalidateIESelect(Element e);

    /**
	 * Fires an event.
	 */
    private void fireAndKeepEvent(AxedEvent e) {
        listeners.fireEvent(e);
    }

    /**
	 * Called from JSNI hack, when the edit div is scrolled
	 */
    private void onScroll2() {
        AxedEvent e = eventPool.newEvent(AxedEvent.ATOMIC_NONE, AxedEvent.SCROLL);
        e.getScroll().issue(AxedEvent.SCROLL);
        listeners.fireEvent(e);
        eventPool.releaseEvent(e);
    }

    private static native void registerScroll(AxedArea ax, Element e);

    /**
	 * Releases an event called by fire.
	 */
    private void releaseEvent(AxedEvent e) {
        eventPool.releaseEvent(e);
    }
}
