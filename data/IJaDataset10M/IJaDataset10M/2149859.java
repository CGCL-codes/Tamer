package edu.rice.cs.drjava.model.repl;

import edu.rice.cs.util.UnexpectedException;
import edu.rice.cs.util.text.ConsoleInterface;
import edu.rice.cs.util.text.DocumentEditCondition;
import edu.rice.cs.util.text.DocumentAdapterException;
import edu.rice.cs.util.swing.Utilities;

/** @version $Id: ConsoleDocument.java 3335 2005-06-30 14:27:55Z rcartwright $ */
public class ConsoleDocument implements ConsoleInterface {

    /** Default text style. */
    public static final String DEFAULT_STYLE = "default";

    /** Style for System.out */
    public static final String SYSTEM_OUT_STYLE = "System.out";

    /** Style for System.err */
    public static final String SYSTEM_ERR_STYLE = "System.err";

    /** Style for System.in */
    public static final String SYSTEM_IN_STYLE = "System.in";

    /** The default prompt to use in the console. */
    public static final String DEFAULT_CONSOLE_PROMPT = "";

    /** The document storing the text for this console model. */
    protected final ConsoleInterface _document;

    /** A runnable command to use for a notification beep. */
    protected volatile Runnable _beep;

    /** Index in the document of the first place that is editable. */
    protected volatile int _promptPos;

    /** String to use for the prompt. */
    protected volatile String _prompt;

    /** Whether the document currently has a prompt and is ready to accept input. */
    protected volatile boolean _hasPrompt;

    /** Creates a new ConsoleDocument with the given ConsoleInterface.
   *  @param adapter the ConsoleInterface to use
   */
    public ConsoleDocument(ConsoleInterface adapter) {
        _document = adapter;
        _beep = new Runnable() {

            public void run() {
            }
        };
        _promptPos = 0;
        _prompt = DEFAULT_CONSOLE_PROMPT;
        _hasPrompt = false;
        _document.setEditCondition(new ConsoleEditCondition());
    }

    /** @return true iff this document has a prompt and is ready to accept input. */
    public boolean hasPrompt() {
        return _hasPrompt;
    }

    /** Accessor for the string used for the prompt. */
    public String getPrompt() {
        return _prompt;
    }

    /** Sets the string to use for the prompt.
   *  @param prompt String to use for the prompt.
   */
    public void setPrompt(String prompt) {
        acquireWriteLock();
        _prompt = prompt;
        releaseWriteLock();
    }

    /** Gets the object which determines whether an insert/remove edit should be applied based on the inputs.
   *  @return the DocumentEditCondition to determine legality of inputs
   */
    public DocumentEditCondition getEditCondition() {
        return _document.getEditCondition();
    }

    /** Provides an object which can determine whether an insert or remove edit should be applied, based on 
   *  the inputs.
   *  @param condition Object to determine legality of inputs
   */
    public void setEditCondition(DocumentEditCondition condition) {
        _document.setEditCondition(condition);
    }

    /** Returns the first location in the document where editing is allowed. */
    public int getPromptPos() {
        return _promptPos;
    }

    /** Sets the prompt position.
   *  @param newPos the new position.
   */
    public void setPromptPos(int newPos) {
        acquireReadLock();
        _promptPos = newPos;
        releaseReadLock();
    }

    /** Sets a runnable action to use as a beep.
   *  @param beep Runnable beep command
   */
    public void setBeep(Runnable beep) {
        acquireReadLock();
        _beep = beep;
        releaseReadLock();
    }

    /** Resets the document to a clean state. */
    public void reset() {
        acquireWriteLock();
        try {
            forceRemoveText(0, _document.getLength());
            _promptPos = 0;
        } catch (DocumentAdapterException e) {
            throw new UnexpectedException(e);
        } finally {
            releaseWriteLock();
        }
    }

    /** Prints a prompt for a new input. */
    public void insertPrompt() {
        acquireWriteLock();
        try {
            forceInsertText(_document.getLength(), _prompt, DEFAULT_STYLE);
            _promptPos = _document.getLength();
            _hasPrompt = true;
        } catch (DocumentAdapterException e) {
            throw new UnexpectedException(e);
        } finally {
            releaseWriteLock();
        }
    }

    /** Disables the prompt in this document. */
    public void disablePrompt() {
        acquireWriteLock();
        try {
            _hasPrompt = false;
            _promptPos = _document.getLength();
        } finally {
            releaseWriteLock();
        }
    }

    /** Inserts a new line at the given position.
   *  @param pos Position to insert the new line
   */
    public void insertNewLine(int pos) {
        acquireWriteLock();
        try {
            int len = _document.getLength();
            if (pos > len) pos = len; else if (pos < 0) pos = 0;
            String newLine = System.getProperty("line.separator");
            insertText(pos, newLine, DEFAULT_STYLE);
        } catch (DocumentAdapterException e) {
            throw new UnexpectedException(e);
        } finally {
            releaseWriteLock();
        }
    }

    /** Gets the position immediately before the prompt, or the doc length if there is no prompt. */
    public int getPositionBeforePrompt() {
        acquireReadLock();
        try {
            if (_hasPrompt) return _promptPos - _prompt.length();
            return _document.getLength();
        } finally {
            releaseReadLock();
        }
    }

    /** Inserts the given string with the given attributes just before the most recent prompt.
   *  @param text String to insert
   *  @param style name of style to format the string
   */
    public void insertBeforeLastPrompt(String text, String style) {
        acquireWriteLock();
        try {
            int pos = getPositionBeforePrompt();
            _promptPos += text.length();
            _addToStyleLists(pos, text, style);
            _document.forceInsertText(pos, text, style);
        } catch (DocumentAdapterException ble) {
            throw new UnexpectedException(ble);
        } finally {
            releaseWriteLock();
        }
    }

    /** Inserts a string into the document at the given offset and named style, if the edit condition allows it.
   *  @param offs Offset into the document
   *  @param str String to be inserted
   *  @param style Name of the style to use.  Must have been
   *  added using addStyle.
   *  @throws DocumentAdapterException if the offset is illegal
   */
    public void insertText(int offs, String str, String style) throws DocumentAdapterException {
        acquireWriteLock();
        try {
            if (offs < _promptPos) _beep.run(); else {
                _addToStyleLists(offs, str, style);
                _document.insertText(offs, str, style);
            }
        } finally {
            releaseWriteLock();
        }
    }

    /** Appends a string to in the given named style, if the edit condition allows it.
   *  @param offs Offset into the document
   *  @param str String to be inserted
   *  @param style Name of the style to use.  Must have been
   *  added using addStyle.
   *  @throws DocumentAdapterException if the offset is illegal
   */
    public void append(String str, String style) throws DocumentAdapterException {
        acquireWriteLock();
        try {
            int offs = _document.getLength();
            _addToStyleLists(offs, str, style);
            _document.insertText(offs, str, style);
        } finally {
            releaseWriteLock();
        }
    }

    /** Inserts a string into the document at the given offset and the given named style, regardless of the edit 
   *  condition.
   *  @param offs Offset into the document
   *  @param str String to be inserted
   *  @param style Name of the style to use.  Must have been added using addStyle.
   *  @throws DocumentAdapterException if the offset is illegal
   */
    public void forceInsertText(int offs, String str, String style) throws DocumentAdapterException {
        acquireWriteLock();
        try {
            _addToStyleLists(offs, str, style);
            _document.forceInsertText(offs, str, style);
        } finally {
            releaseWriteLock();
        }
    }

    private void _addToStyleLists(int offs, String str, String style) {
        if (_document instanceof InteractionsDocumentAdapter) ((InteractionsDocumentAdapter) _document).addColoring(offs, offs + str.length(), style);
    }

    /** Removes a portion of the document, if the edit condition allows it.
   *  @param offs Offset to start deleting from
   *  @param len Number of characters to remove
   *  @throws DocumentAdapterException if the offset or length are illegal
   */
    public void removeText(int offs, int len) throws DocumentAdapterException {
        acquireWriteLock();
        try {
            if (offs < _promptPos) _beep.run(); else _document.removeText(offs, len);
        } finally {
            releaseWriteLock();
        }
    }

    /** Removes a portion of the document, regardless of the edit condition.
   *  @param offs Offset to start deleting from
   *  @param len Number of characters to remove
   *  @throws DocumentAdapterException if the offset or length are illegal
   */
    public void forceRemoveText(int offs, int len) throws DocumentAdapterException {
        _document.forceRemoveText(offs, len);
    }

    /** Returns the length of the document. */
    public int getLength() {
        return _document.getLength();
    }

    /** Returns a portion of the document.
   *  @param offs First offset of the desired text
   *  @param len Number of characters to return
   *  @throws DocumentAdapterException if the offset or length are illegal
   */
    public String getDocText(int offs, int len) throws DocumentAdapterException {
        return _document.getDocText(offs, len);
    }

    /** Returns the string that the user has entered at the current prompt.
   *  May contain newline characters.
   */
    public String getCurrentInput() {
        acquireReadLock();
        try {
            try {
                return getDocText(_promptPos, _document.getLength() - _promptPos);
            } catch (DocumentAdapterException e) {
                throw new UnexpectedException(e);
            }
        } finally {
            releaseReadLock();
        }
    }

    /** Clears the current input text. */
    public void clearCurrentInput() {
        _clearCurrentInputText();
    }

    /** Removes the text from the current prompt to the end of the document. */
    protected void _clearCurrentInputText() {
        acquireWriteLock();
        try {
            removeText(_promptPos, _document.getLength() - _promptPos);
        } catch (DocumentAdapterException ble) {
            throw new UnexpectedException(ble);
        } finally {
            releaseWriteLock();
        }
    }

    /** Class ensuring that attempts to edit document lines above the prompt are rejected. */
    class ConsoleEditCondition extends DocumentEditCondition {

        public boolean canInsertText(int offs) {
            return canRemoveText(offs);
        }

        public boolean canRemoveText(int offs) {
            if (offs < _promptPos) {
                _beep.run();
                return false;
            }
            return true;
        }
    }

    /** Swing-style readLock(). */
    public void acquireReadLock() {
        _document.acquireReadLock();
    }

    /** Swing-style readUnlock(). */
    public void releaseReadLock() {
        _document.releaseReadLock();
    }

    /** Swing-style writeLock(). */
    public void acquireWriteLock() {
        _document.acquireWriteLock();
    }

    /** Swing-style writeUnlock(). */
    public void releaseWriteLock() {
        _document.releaseWriteLock();
    }
}
