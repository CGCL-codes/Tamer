package com.apachetune.core.ui.editors.impl;

import com.apachetune.core.ResourceManager;
import com.apachetune.core.preferences.Preferences;
import com.apachetune.core.preferences.PreferencesManager;
import com.apachetune.core.ui.CoreUIUtils;
import com.apachetune.core.ui.GenericUIWorkItem;
import com.apachetune.core.ui.MenuBarManager;
import com.apachetune.core.ui.actions.*;
import com.apachetune.core.ui.editors.EditorActionSite;
import com.apachetune.core.ui.editors.EditorInput;
import com.apachetune.core.ui.editors.EditorWorkItem;
import com.apachetune.core.ui.statusbar.StatusBarManager;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import jsyntaxpane.ExtendedSyntaxDocument;
import org.noos.xing.mydoggy.Content;
import org.noos.xing.mydoggy.ToolWindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import static com.apachetune.core.ui.Constants.*;
import static com.apachetune.core.utils.Utils.createRuntimeException;
import static java.awt.Color.RED;
import static java.lang.Math.min;
import static java.text.MessageFormat.format;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;

/**
 * FIXDOC
 *
 * @author <a href="mailto:progmonster@gmail.com">Aleksey V. Katorgin</a>
 * @version 1.0
 */
public class EditorWorkItemImpl extends GenericUIWorkItem implements EditorWorkItem, EditorActionSite, SaveFileActionSite, UndoWorkflowActionSite, PrintDocumentActionSite {

    private static final Logger logger = LoggerFactory.getLogger(EditorWorkItemImpl.class);

    private static final char EDITOR_DIRTY_FLAG = '*';

    private static final Color HIGHLIGHT_ERROR_LINE_COLOR = RED;

    private final ToolWindowManager toolWindowManager;

    private final ActionManager actionManager;

    private final StatusBarManager statusBarManager;

    private final MenuBarManager menuBarManager;

    private final PreferencesManager preferencesManager;

    private final CoreUIUtils coreUIUtils;

    private final JFrame mainFrame;

    private EditorInput editorInput;

    private Content contentPane;

    private JEditorPane editorPane;

    private final EditorPaneFocusListener editorPaneFocusListener = new EditorPaneFocusListener();

    private boolean dirty;

    private boolean canUndo;

    private boolean canRedo;

    private JScrollPane editorScrollPane;

    private ResourceBundle resourceBundle = ResourceManager.getInstance().getResourceBundle(EditorWorkItemImpl.class);

    @Inject
    public EditorWorkItemImpl(@Named(TOOL_WINDOW_MANAGER) ToolWindowManager toolWindowManager, ActionManager actionManager, StatusBarManager statusBarManager, MenuBarManager menuBarManager, PreferencesManager preferencesManager, CoreUIUtils coreUIUtils, JFrame mainFrame) {
        this.toolWindowManager = toolWindowManager;
        this.actionManager = actionManager;
        this.statusBarManager = statusBarManager;
        this.menuBarManager = menuBarManager;
        this.preferencesManager = preferencesManager;
        this.coreUIUtils = coreUIUtils;
        this.mainFrame = mainFrame;
    }

    public void setEditorInput(EditorInput editorInput) {
        notNull(editorInput, "Argument editorInput cannot be a null");
        this.editorInput = editorInput;
        setId(editorInput.getWorkItemId());
    }

    @ActionHandler(EDIT_COPY_ACTION)
    public void onCopy() {
        editorPane.copy();
    }

    @ActionPermission(EDIT_COPY_ACTION)
    public boolean isCopyEnabled() {
        return true;
    }

    @ActionHandler(EDIT_CUT_ACTION)
    public void onCut() {
        editorPane.cut();
    }

    @ActionPermission(EDIT_CUT_ACTION)
    public boolean isCutEnabled() {
        return true;
    }

    @ActionHandler(EDIT_PASTE_ACTION)
    public void onPaste() {
        editorPane.paste();
    }

    @ActionPermission(EDIT_PASTE_ACTION)
    public boolean isPasteEnabled() {
        return true;
    }

    @ActionHandler(EDIT_SELECT_ALL_ACTION)
    public void onSelectAll() {
        editorPane.grabFocus();
        editorPane.selectAll();
    }

    @ActionPermission(EDIT_SELECT_ALL_ACTION)
    public boolean isSelectAllEnabled() {
        return true;
    }

    @ActionHandler(FILE_SAVE_ACTION)
    public void onSaveFile() {
        save();
    }

    @ActionPermission(FILE_SAVE_ACTION)
    public boolean isSaveFileEnabled() {
        return dirty;
    }

    @ActionHandler(EDIT_UNDO_ACTION)
    public void onUndo() {
        getDocument().doUndo();
        updateUndoRedoActions();
    }

    @ActionPermission(EDIT_UNDO_ACTION)
    public boolean isUndoEnabled() {
        return canUndo;
    }

    @ActionHandler(EDIT_REDO_ACTION)
    public void onRedo() {
        getDocument().doRedo();
        updateUndoRedoActions();
    }

    @ActionPermission(EDIT_REDO_ACTION)
    public boolean isRedoEnabled() {
        return canRedo;
    }

    @ActionHandler(FILE_PRINT_ACTION)
    public void onPrintDocument() {
        statusBarManager.addMainStatus(PRINT_DOCUMENT_STATUS, resourceBundle.getString("editorWorkItemImpl.onPrintDocument.statusBarManager.printingStatus"));
        try {
            String content = getContent();
            DefaultStyledDocument document = new DefaultStyledDocument();
            document.replace(0, document.getLength(), content, null);
            JTextPane textPane = new JTextPane(document);
            textPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
            String header = editorInput.getPrintTitle();
            textPane.print(new MessageFormat(header), new MessageFormat(resourceBundle.getString("editorWorkItemImpl.onPrintDocument.pageNumberFormat")), true, null, null, true);
        } catch (PrinterException e) {
            logger.error("Error printing file", e);
            showMessageDialog(mainFrame, resourceBundle.getString("editorWorkItemImpl.onPrintDocument.showMessageDialog.message"), resourceBundle.getString("editorWorkItemImpl.onPrintDocument.showMessageDialog.title"), ERROR_MESSAGE);
        } catch (BadLocationException e) {
            throw createRuntimeException(e);
        } finally {
            statusBarManager.removeMainStatus(PRINT_DOCUMENT_STATUS);
        }
    }

    @ActionPermission(FILE_PRINT_ACTION)
    public boolean isPrintDocumentEnabled() {
        return true;
    }

    public void save() {
        if (!dirty) {
            return;
        }
        statusBarManager.addMainStatus(SAVE_FILE_STATUS, format(resourceBundle.getString("editorWorkItemImpl.save.statusBarManager.savingStatus"), editorInput.getSaveTitle()));
        try {
            editorInput.saveContent(getContent());
            setDirty(false);
        } finally {
            statusBarManager.removeMainStatus(SAVE_FILE_STATUS);
        }
    }

    public boolean isDirty() {
        return dirty;
    }

    protected void doUIInitialize() {
        checkEditorInput();
        createEditorPaneContent();
        editorPane.grabFocus();
    }

    protected void doUIDispose() {
        try {
            storeCaretPosition();
            storeViewPosition();
        } catch (BackingStoreException e) {
            throw createRuntimeException(e);
        }
        editorPane.requestFocus();
        clearEditorPaneUndoManagerState();
        removeEditorPaneContent();
        resetCaretPositionState();
    }

    private void resetCaretPositionState() {
        statusBarManager.setCaretPositionState(null);
    }

    @Override
    protected void doActivation() {
        contentPane.setSelected(true);
        editorPane.grabFocus();
        updateCaretPositionState(editorPane.getCaretPosition());
    }

    protected void doDeactivation() {
        resetCaretPositionState();
    }

    private void updateCaretPositionState(int pos) {
        Element root = getDocument().getDefaultRootElement();
        int line = root.getElementIndex(pos);
        int col = pos - root.getElement(line).getStartOffset();
        statusBarManager.setCaretPositionState(new Point(col + 1, line + 1));
    }

    private void createEditorPaneContent() {
        editorPane = new JEditorPane();
        editorScrollPane = new JScrollPane(editorPane);
        editorPane.setContentType(editorInput.getContentType());
        editorPane.setText(editorInput.loadContent());
        clearEditorPaneUndoManagerState();
        initEditorPaneCaretListener();
        initEditorPaneUndoRedoListener();
        initEditorPaneDocumentListener();
        contentPane = coreUIUtils.addContentToNestedToolWindowManager(editorInput.getContentPaneId(), editorInput.getContentPaneTitle(), editorInput.getContentPaneIcon(), editorScrollPane, editorInput.getSaveTitle());
        editorPane.setAutoscrolls(false);
        restoreCaretPosition();
        restoreViewPosition();
        contentPane.getDockableManager().setPopupMenu(null);
        contentPane.getContentUI().setCloseable(false);
        contentPane.getContentUI().setDetachable(false);
        contentPane.getContentUI().setMinimizable(false);
        editorPane.addFocusListener(editorPaneFocusListener);
        menuBarManager.createAndBindContextMenu(editorPane, this);
    }

    private void restoreCaretPosition() {
        int caretPosition = 0;
        if (hasStoredCaretPosition()) {
            caretPosition = getStoredCaretPosition();
        }
        final int finalCaretPosition = caretPosition;
        editorPane.setCaretPosition(min(finalCaretPosition, getContentLength()));
    }

    private int getContentLength() {
        return editorPane.getDocument().getLength();
    }

    private void restoreViewPosition() {
        final int viewPosition;
        if (hasStoredViewPosition()) {
            viewPosition = min(getStoredViewPosition(), getContentLength());
        } else {
            viewPosition = 0;
        }
        try {
            Point restoredFirstVisiblePoint = editorPane.modelToView(viewPosition).getLocation();
            editorPane.scrollRectToVisible(new Rectangle(restoredFirstVisiblePoint, editorScrollPane.getViewport().getSize()));
        } catch (BadLocationException e) {
            throw createRuntimeException(e);
        }
    }

    private int getStoredViewPosition() {
        return preferencesManager.userNodeForPackage(EditorWorkItemImpl.class).node(VIEW_POSITION_PREFS_NODE_NAME).getInt(getDocumentUri().toASCIIString(), -1);
    }

    private boolean hasStoredViewPosition() {
        int storedPos = getStoredViewPosition();
        return (storedPos != -1);
    }

    private void initEditorPaneDocumentListener() {
        getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                setDirty(true);
            }

            public void removeUpdate(DocumentEvent e) {
                setDirty(true);
            }

            public void changedUpdate(DocumentEvent e) {
                setDirty(true);
            }
        });
    }

    private void initEditorPaneUndoRedoListener() {
        getDocument().addUndoableEditListener(new UndoableEditListener() {

            public void undoableEditHappened(UndoableEditEvent e) {
                updateUndoAndRedoActions(e.getEdit().canUndo(), e.getEdit().canRedo());
            }
        });
    }

    public ExtendedSyntaxDocument getDocument() {
        return (ExtendedSyntaxDocument) editorPane.getDocument();
    }

    public int getCaretPosition() {
        return editorPane.getCaretPosition();
    }

    public void setCaretPosition(int position) {
        editorPane.setCaretPosition(position);
        try {
            Rectangle caretRect = editorPane.modelToView(position);
            Rectangle scrollRect = new Rectangle(caretRect.x, caretRect.y, caretRect.width, editorPane.getVisibleRect().height);
            editorPane.scrollRectToVisible(scrollRect);
        } catch (BadLocationException e) {
            throw createRuntimeException(e);
        }
    }

    public int getLineStartPosition(int lineNum) {
        int lineCount = getDocument().getLineCount();
        isTrue((lineNum >= 1) && (lineNum <= lineCount), "Argument lineNum cannot be less than unity and greater than " + lineCount + "[lineNum = " + lineNum + "; this = " + this + ']');
        return getDocument().getDefaultRootElement().getElement(lineNum - 1).getStartOffset();
    }

    public void highlightLine(int lineNum, Color red) {
        int lineCount = getDocument().getLineCount();
        isTrue((lineNum >= 1) && (lineNum <= lineCount), "Argument lineNum cannot be less than unity and greater than " + lineCount + "[lineNum = " + lineNum + "; this = " + this + ']');
        Element lineElement = getDocument().getDefaultRootElement().getElement(lineNum - 1);
        new SelfReleasingErrorLineHighlighter(editorPane, lineElement.getStartOffset(), lineElement.getEndOffset() - 1);
    }

    private void updateUndoRedoActions() {
        UndoManager undoManager = getDocument().getUndoManager();
        updateUndoAndRedoActions(undoManager.canUndo(), undoManager.canRedo());
    }

    private void updateUndoAndRedoActions(boolean canUndo, boolean canRedo) {
        this.canUndo = canUndo;
        this.canRedo = canRedo;
        actionManager.updateActionSites(this);
    }

    private void removeEditorPaneContent() {
        Content editorPaneContent = toolWindowManager.getContentManager().getContent(getId());
        if (editorPaneContent != null) {
            toolWindowManager.getContentManager().removeContent(editorPaneContent);
        }
        editorPane.removeFocusListener(editorPaneFocusListener);
        editorPane = null;
    }

    private void initEditorPaneCaretListener() {
        editorPane.addCaretListener(new CaretListener() {

            public void caretUpdate(CaretEvent e) {
                updateCaretPositionState(e.getDot());
            }
        });
    }

    private void clearEditorPaneUndoManagerState() {
        getDocument().clearUndos();
        updateUndoAndRedoActions(false, false);
    }

    private void checkEditorInput() {
        notNull(editorInput, "Argument editorInput cannot be a null");
    }

    private String getContent() {
        return editorPane.getText();
    }

    private void setDirty(boolean isDirty) {
        if (dirty == isDirty) {
            return;
        }
        Boolean oldValue = dirty;
        dirty = isDirty;
        contentPane.setTitle(editorInput.getContentPaneTitle() + (isDirty ? EDITOR_DIRTY_FLAG : ""));
        actionManager.updateActionSites(this);
        firePropertyChangeEvent(IS_DIRTY_PROP, oldValue, isDirty);
    }

    private URI getDocumentUri() {
        return editorInput.getDocumentUri();
    }

    private void storeCaretPosition() throws BackingStoreException {
        Preferences caretPrefsNode = preferencesManager.userNodeForPackage(EditorWorkItemImpl.class).node(CARET_POSITION_PREFS_NODE_NAME);
        caretPrefsNode.putInt(getDocumentUri().toASCIIString(), getCaretPosition());
        caretPrefsNode.flush();
    }

    private void storeViewPosition() throws BackingStoreException {
        Preferences caretPrefsNode = preferencesManager.userNodeForPackage(EditorWorkItemImpl.class).node(VIEW_POSITION_PREFS_NODE_NAME);
        caretPrefsNode.putInt(getDocumentUri().toASCIIString(), getViewPosition());
        caretPrefsNode.flush();
    }

    private int getViewPosition() {
        Point firstVisibleLinePoint = editorScrollPane.getViewport().getViewRect().getLocation();
        return editorPane.viewToModel(firstVisibleLinePoint);
    }

    private boolean hasStoredCaretPosition() {
        int storedPos = getStoredCaretPosition();
        return (storedPos != -1);
    }

    private int getStoredCaretPosition() {
        return preferencesManager.userNodeForPackage(EditorWorkItemImpl.class).node(CARET_POSITION_PREFS_NODE_NAME).getInt(getDocumentUri().toASCIIString(), -1);
    }

    private class EditorPaneFocusListener extends FocusAdapter {

        public void focusGained(FocusEvent e) {
            activate();
        }
    }

    private class SelfReleasingErrorLineHighlighter extends DefaultHighlighter.DefaultHighlightPainter implements CaretListener, MouseListener {

        private final JEditorPane editorPane;

        private final Object highlight;

        public SelfReleasingErrorLineHighlighter(JEditorPane editorPane, int startOffset, int endOffset) {
            super(HIGHLIGHT_ERROR_LINE_COLOR);
            this.editorPane = editorPane;
            try {
                highlight = editorPane.getHighlighter().addHighlight(startOffset, endOffset, this);
            } catch (BadLocationException e) {
                throw createRuntimeException(e);
            }
            editorPane.addCaretListener(this);
            editorPane.addMouseListener(this);
        }

        public void caretUpdate(CaretEvent e) {
            releaseHighlighting();
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            releaseHighlighting();
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        private void releaseHighlighting() {
            editorPane.getHighlighter().removeHighlight(highlight);
            editorPane.removeCaretListener(this);
            editorPane.removeMouseListener(this);
        }
    }
}
