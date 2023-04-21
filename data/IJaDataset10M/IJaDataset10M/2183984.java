package net.sourceforge.squirrel_sql.plugins.syntax.oster;

import java.awt.Font;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseListener;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import javax.swing.undo.UndoManager;
import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.gui.dnd.FileEditorDropTargetListener;
import net.sourceforge.squirrel_sql.client.session.BaseSQLEntryPanel;
import net.sourceforge.squirrel_sql.client.session.ISQLEntryPanel;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.SQLTokenListener;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;
import net.sourceforge.squirrel_sql.plugins.syntax.SyntaxPreferences;

public class OsterSQLEntryPanel extends BaseSQLEntryPanel {

    /** Logger for this class. */
    private static final ILogger s_log = LoggerController.createLogger(OsterSQLEntryPanel.class);

    /** Application API. */
    private IApplication _app;

    /** Text component. */
    private OsterTextControl _textArea;

    @SuppressWarnings("unused")
    private DropTarget dt;

    private ISession _session;

    OsterSQLEntryPanel(ISession session, SyntaxPreferences prefs) {
        super(session.getApplication());
        _session = session;
        if (session == null) {
            throw new IllegalArgumentException("Null ISession passed");
        }
        if (prefs == null) {
            throw new IllegalArgumentException("Null Preferences passed");
        }
        _app = session.getApplication();
        _textArea = new OsterTextControl(session, prefs, getIdentifier());
        dt = new DropTarget(_textArea, new FileEditorDropTargetListener(session));
    }

    public void endColorerThread() {
        _textArea.endColorerThread();
    }

    /**
	 * @see ISQLEntryPanel#gettextComponent()
	 */
    public JTextComponent getTextComponent() {
        return _textArea;
    }

    /**
	 * If the component returned by <TT>getTextComponent</TT> contains
	 * its own scroll bars return <TT>true</TT> other wise this component
	 * will be wrapped in the scroll pane when added to the SQL panel.
	 *
	 * @return	<TT>true</TT> if text component already handles scrolling.
	 */
    public boolean getDoesTextComponentHaveScroller() {
        return false;
    }

    /**
	 * @see ISQLEntryPanel#getText()
	 */
    public String getText() {
        return _textArea.getText();
    }

    public void setFont(Font font) {
        _textArea.setFont(font);
    }

    /**
	 * @see ISQLEntryPanel#getSelectedText()
	 */
    public String getSelectedText() {
        return _textArea.getSelectedText();
    }

    /**
	 * Replace the contents of the SQL entry area with the passed
	 * SQL script without selecting it.
	 *
	 * @param	sqlScript	The script to be placed in the SQL entry area..
	 */
    public void setText(String text) {
        setText(text, true);
    }

    /**
	 * Replace the contents of the SQL entry area with the passed
	 * SQL script and specify whether to select it.
	 *
	 * @param	sqlScript	The script to be placed in the SQL entry area..
	 * @param 	select		If <TT>true</TT> then select the passed script
	 *						in the sql entry area.
	 */
    public void setText(String text, boolean select) {
        _textArea.setText(text);
        if (select) {
            setSelectionEnd(_textArea.getDocument().getLength());
            setSelectionStart(0);
        }
    }

    /**
	 * Append the passed SQL script to the SQL entry area but don't select
	 * it.
	 *
	 * @param	sqlScript	The script to be appended.
	 */
    public void appendText(String sqlScript) {
        appendText(sqlScript, false);
    }

    /**
	 * Append the passed SQL script to the SQL entry area and specify
	 * whether it should be selected.
	 *
	 * @param	sqlScript	The script to be appended.
	 * @param	select		If <TT>true</TT> then select the passed script
	 *						in the sql entry area.
	 */
    public void appendText(String sqlScript, boolean select) {
        Document doc = _textArea.getDocument();
        try {
            int start = 0;
            if (select) {
                start = doc.getLength();
            }
            doc.insertString(doc.getLength(), sqlScript, null);
            if (select) {
                setSelectionEnd(doc.getLength());
                setSelectionStart(start);
            }
        } catch (Exception ex) {
            s_log.error("Error appending text to text area", ex);
        }
    }

    /**
	 * @see ISQLEntryPanel#getCaretPosition()
	 */
    public int getCaretPosition() {
        return _textArea.getCaretPosition();
    }

    public void setCaretPosition(int value) {
        _textArea.setCaretPosition(value);
    }

    /**
	 * @see ISQLEntryPanel#setTabSize(int)
	 */
    public void setTabSize(int tabSize) {
        _textArea.getDocument().putProperty(PlainDocument.tabSizeAttribute, Integer.valueOf(tabSize));
    }

    /**
	 * @see ISQLEntryPanel#getSelectionStart()
	 */
    public int getSelectionStart() {
        return _textArea.getSelectionStart();
    }

    /**
	 * @see ISQLEntryPanel#setSelectionStart(int)
	 */
    public void setSelectionStart(int pos) {
        _textArea.setSelectionStart(pos);
    }

    /**
	 * @see ISQLEntryPanel#getSelectionEnd()
	 */
    public int getSelectionEnd() {
        return _textArea.getSelectionEnd();
    }

    /**
	 * @see ISQLEntryPanel#setSelectionEnd(int)
	 */
    public void setSelectionEnd(int pos) {
        _textArea.setSelectionEnd(pos);
    }

    /**
	 * Replace the currently selected text in the SQL entry area
	 * with the passed text.
	 *
	 * @param	sqlScript	The script to be placed in the SQL entry area.
	 */
    public void replaceSelection(String sqlScript) {
        _textArea.replaceSelection(sqlScript);
    }

    /**
	 * @see ISQLEntryPanel#hasFocus()
	 */
    public boolean hasFocus() {
        return _textArea.hasFocus();
    }

    /**
	 * @see ISQLEntryPanel#requestFocus()
	 */
    public void requestFocus() {
        _textArea.requestFocus();
    }

    /**
	 * @see ISQLEntryPanel#addMouseListener(MouseListener)
	 */
    public void addMouseListener(MouseListener lis) {
        _textArea.addMouseListener(lis);
    }

    /**
	 * @see ISQLEntryPanel#removeMouseListener(MouseListener)
	 */
    public void removeMouseListener(MouseListener lis) {
        _textArea.removeMouseListener(lis);
    }

    public void updateFromPreferences() {
        _textArea.updateFromPreferences();
    }

    /**
	 * @see ISQLEntryPanel#hasOwnUndoableManager()
	 */
    public boolean hasOwnUndoableManager() {
        return false;
    }

    /**
	 * @see ISQLEntryPanel#addUndoableEditListener(UndoableEditListener)
	 */
    public void addUndoableEditListener(UndoableEditListener listener) {
        _textArea.getDocument().addUndoableEditListener(listener);
    }

    /**
	 * @see ISQLEntryPanel#removeUndoableEditListener(UndoableEditListener)
	 */
    public void removeUndoableEditListener(UndoableEditListener listener) {
        _textArea.getDocument().removeUndoableEditListener(listener);
    }

    public void setUndoManager(UndoManager manager) {
    }

    /**
	 * @see ISQLEntryPanel#getCaretLineNumber()
	 */
    public int getCaretLineNumber() {
        final int pos = getCaretPosition();
        final Document doc = _textArea.getStyledDocument();
        final Element docElem = doc.getDefaultRootElement();
        return docElem.getElementIndex(pos);
    }

    /**
	 * @see ISQLEntryPanel#getCaretLinePosition()
	 */
    public int getCaretLinePosition() {
        String textTillCarret = getText().substring(0, getCaretPosition());
        int lineFeedIndex = textTillCarret.lastIndexOf('\n');
        if (-1 == lineFeedIndex) {
            return getCaretPosition();
        } else {
            return getCaretPosition() - lineFeedIndex - 1;
        }
    }

    /**
	 * @see ISQLEntryPanel#addCaretListener(CaretListener)
	 */
    public void addCaretListener(CaretListener lis) {
        _textArea.addCaretListener(lis);
    }

    /**
	 * @see ISQLEntryPanel#removeCaretListener(CaretListener)
	 */
    public void removeCaretListener(CaretListener lis) {
        _textArea.removeCaretListener(lis);
    }

    public void addSQLTokenListener(SQLTokenListener tl) {
        _textArea.addSQLTokenListener(tl);
    }

    public void removeSQLTokenListener(SQLTokenListener tl) {
        _textArea.removeSQLTokenListener(tl);
    }

    public ISession getSession() {
        return _session;
    }
}
