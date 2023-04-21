package org.coode.oae.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.description.OWLExpressionParserException;
import org.protege.editor.owl.ui.clsdescriptioneditor.AutoCompleterMatcher;
import org.protege.editor.owl.ui.clsdescriptioneditor.AutoCompleterMatcherImpl;
import org.protege.editor.owl.ui.clsdescriptioneditor.OWLExpressionChecker;
import org.semanticweb.owl.model.OWLObject;
import uk.ac.manchester.mae.evaluation.PropertyChainModel;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: May 4, 2006<br>
 * <br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br>
 * <br>
 */
public class PropertyChainAutoCompleter {

    private static Logger logger = Logger.getLogger(PropertyChainAutoCompleter.class);

    public static final int DEFAULT_MAX_ENTRIES = 100;

    private OWLEditorKit owlEditorKit;

    protected JTextComponent textComponent;

    private Set<String> wordDelimeters;

    private AutoCompleterMatcher matcher;

    private JList popupList;

    protected JWindow popupWindow;

    public static final int POPUP_WIDTH = 350;

    public static final int POPUP_HEIGHT = 300;

    private OWLExpressionChecker<PropertyChainModel> checker;

    protected String lastTextUpdate = "*";

    private int maxEntries = DEFAULT_MAX_ENTRIES;

    private KeyListener keyListener = new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent e) {
            processKeyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_DOWN) {
                if (PropertyChainAutoCompleter.this.popupWindow.isVisible() && !PropertyChainAutoCompleter.this.lastTextUpdate.equals(PropertyChainAutoCompleter.this.textComponent.getText())) {
                    PropertyChainAutoCompleter.this.lastTextUpdate = PropertyChainAutoCompleter.this.textComponent.getText();
                    updatePopup(getMatches());
                }
            }
        }
    };

    protected ComponentAdapter componentListener = new ComponentAdapter() {

        @Override
        public void componentHidden(ComponentEvent event) {
            hidePopup();
        }

        @Override
        public void componentResized(ComponentEvent event) {
            hidePopup();
        }

        @Override
        public void componentMoved(ComponentEvent event) {
            hidePopup();
        }
    };

    private HierarchyListener hierarchyListener = new HierarchyListener() {

        /**
		 * Called when the hierarchy has been changed. To discern the actual
		 * type of change, call <code>HierarchyEvent.getChangeFlags()</code>.
		 * 
		 * @see java.awt.event.HierarchyEvent#getChangeFlags()
		 */
        public void hierarchyChanged(HierarchyEvent e) {
            if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0) {
                createPopupWindow();
                Container frame = PropertyChainAutoCompleter.this.textComponent.getTopLevelAncestor();
                if (frame != null) {
                    frame.addComponentListener(PropertyChainAutoCompleter.this.componentListener);
                }
            }
        }
    };

    private MouseListener mouseListener = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                completeWithPopupSelection();
            }
        }
    };

    private FocusListener focusListener = new FocusAdapter() {

        @Override
        public void focusLost(FocusEvent event) {
            hidePopup();
        }
    };

    public PropertyChainAutoCompleter(OWLEditorKit owlEditorKit, JTextComponent tc, OWLExpressionChecker<PropertyChainModel> checker) {
        this.owlEditorKit = owlEditorKit;
        this.checker = checker;
        this.textComponent = tc;
        this.wordDelimeters = new HashSet<String>();
        this.wordDelimeters.add(" ");
        this.wordDelimeters.add("\n");
        this.wordDelimeters.add("[");
        this.wordDelimeters.add("]");
        this.wordDelimeters.add("{");
        this.wordDelimeters.add("}");
        this.wordDelimeters.add("(");
        this.wordDelimeters.add(")");
        this.wordDelimeters.add(",");
        this.wordDelimeters.add("^");
        this.matcher = new AutoCompleterMatcherImpl(owlEditorKit.getModelManager());
        this.popupList = new JList();
        this.popupList.setAutoscrolls(true);
        this.popupList.setCellRenderer(owlEditorKit.getWorkspace().createOWLCellRenderer());
        this.popupList.addMouseListener(this.mouseListener);
        this.popupList.setRequestFocusEnabled(false);
        this.textComponent.addKeyListener(this.keyListener);
        this.textComponent.addHierarchyListener(this.hierarchyListener);
        this.textComponent.addComponentListener(this.componentListener);
        this.textComponent.addFocusListener(this.focusListener);
        createPopupWindow();
    }

    public void cancel() {
        hidePopup();
    }

    protected void processKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && e.isControlDown()) {
            performAutoCompletion();
        } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
            e.consume();
            performAutoCompletion();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (this.popupWindow.isVisible()) {
                e.consume();
                hidePopup();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (this.popupWindow.isVisible()) {
                e.consume();
                completeWithPopupSelection();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (this.popupWindow.isVisible()) {
                e.consume();
                incrementSelection();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (this.popupWindow.isVisible()) {
                e.consume();
                decrementSelection();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            hidePopup();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            hidePopup();
        }
    }

    protected void completeWithPopupSelection() {
        if (this.popupWindow.isVisible()) {
            Object selObject = this.popupList.getSelectedValue();
            if (selObject != null) {
                insertWord(getInsertText(selObject));
                hidePopup();
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected List getMatches() {
        try {
            int wordIndex = getWordIndex();
            String expression = this.textComponent.getDocument().getText(0, wordIndex);
            try {
                this.checker.check(expression);
            } catch (OWLExpressionParserException e) {
                Set<OWLObject> matches = this.matcher.getMatches("", e.isOWLClassExpected(), e.isOWLObjectPropertyExpected(), e.isOWLDataPropertyExpected(), false, false);
                List kwMatches = new ArrayList(matches.size() + 10);
                for (String s : e.getExpectedKeyWords()) {
                    kwMatches.add(s);
                }
                kwMatches.addAll(matches);
                return kwMatches;
            }
        } catch (BadLocationException e) {
            Logger.getLogger(getClass()).warn(e);
        }
        return Collections.EMPTY_LIST;
    }

    protected void createPopupWindow() {
        JScrollPane sp = ComponentFactory.createScrollPane(this.popupList);
        this.popupWindow = new JWindow((Window) SwingUtilities.getAncestorOfClass(Window.class, this.textComponent));
        this.popupWindow.getContentPane().setLayout(new BorderLayout());
        this.popupWindow.getContentPane().add(sp, BorderLayout.CENTER);
        this.popupWindow.setFocusableWindowState(false);
    }

    @SuppressWarnings("unchecked")
    private void performAutoCompletion() {
        List matches = getMatches();
        if (matches.size() == 1) {
            insertWord(getInsertText(matches.iterator().next()));
        } else if (matches.size() > 1) {
            this.lastTextUpdate = this.textComponent.getText();
            showPopup();
            updatePopup(matches);
        }
    }

    private void insertWord(String word) {
        try {
            int selStart = this.textComponent.getSelectionStart();
            int selEnd = this.textComponent.getSelectionEnd();
            int selLen = selEnd - selStart;
            if (selLen > 0) {
                this.textComponent.getDocument().remove(selStart, selLen);
            }
            int index = getWordIndex();
            int caretIndex = this.textComponent.getCaretPosition();
            if (caretIndex > 0 && caretIndex > index) {
                this.textComponent.getDocument().remove(index, caretIndex - index);
            }
            this.textComponent.getDocument().insertString(index, word, null);
        } catch (BadLocationException e) {
            logger.error(e);
        }
    }

    private void showPopup() {
        if (this.popupWindow == null) {
            createPopupWindow();
        }
        if (!this.popupWindow.isVisible()) {
            this.popupWindow.setSize(POPUP_WIDTH, POPUP_HEIGHT);
            try {
                int wordIndex = getWordIndex();
                Point p = new Point(0, 0);
                if (wordIndex > 0) {
                    p = this.textComponent.modelToView(wordIndex).getLocation();
                }
                SwingUtilities.convertPointToScreen(p, this.textComponent);
                p.y = p.y + this.textComponent.getFontMetrics(this.textComponent.getFont()).getHeight();
                this.popupWindow.setLocation(p);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            this.popupWindow.setVisible(true);
        }
    }

    protected void hidePopup() {
        this.popupWindow.setVisible(false);
        this.popupList.setListData(new Object[0]);
    }

    @SuppressWarnings("unchecked")
    protected void updatePopup(List matches) {
        int count = matches.size();
        if (count > this.maxEntries) {
            count = this.maxEntries;
        }
        if (!matches.isEmpty()) {
            this.popupList.setListData(matches.subList(0, count).toArray());
        } else {
            this.popupList.setListData(matches.toArray());
        }
        this.popupList.setSelectedIndex(0);
        this.popupWindow.setSize(POPUP_WIDTH, POPUP_HEIGHT);
    }

    private void incrementSelection() {
        if (this.popupList.getModel().getSize() > 0) {
            int selIndex = this.popupList.getSelectedIndex();
            selIndex++;
            if (selIndex > this.popupList.getModel().getSize() - 1) {
                selIndex = 0;
            }
            this.popupList.setSelectedIndex(selIndex);
            this.popupList.scrollRectToVisible(this.popupList.getCellBounds(selIndex, selIndex));
        }
    }

    private void decrementSelection() {
        if (this.popupList.getModel().getSize() > 0) {
            int selIndex = this.popupList.getSelectedIndex();
            selIndex--;
            if (selIndex < 0) {
                selIndex = this.popupList.getModel().getSize() - 1;
            }
            this.popupList.setSelectedIndex(selIndex);
            this.popupList.scrollRectToVisible(this.popupList.getCellBounds(selIndex, selIndex));
        }
    }

    private int getWordIndex() {
        int index = getEscapedWordIndex();
        if (index == -1) {
            index = getUnbrokenWordIndex();
        }
        return Math.max(0, index);
    }

    private int getEscapedWordIndex() {
        try {
            int caretPos = Math.max(0, getEffectiveCaretPosition() - 1);
            String expression = this.textComponent.getDocument().getText(0, caretPos);
            int escapeEnd = -1;
            do {
                int escapeStart = expression.indexOf("'", escapeEnd + 1);
                if (escapeStart != -1) {
                    escapeEnd = expression.indexOf("'", escapeStart + 1);
                    if (escapeEnd == -1) {
                        return escapeStart;
                    }
                } else {
                    return -1;
                }
            } while (true);
        } catch (BadLocationException e) {
            logger.error(e);
        }
        return -1;
    }

    private int getUnbrokenWordIndex() {
        try {
            int caretPos = Math.max(0, getEffectiveCaretPosition() - 1);
            if (caretPos > 0) {
                for (int index = caretPos; index > -1; index--) {
                    if (this.wordDelimeters.contains(this.textComponent.getDocument().getText(index, 1))) {
                        return index + 1;
                    }
                    if (index == 0) {
                        return 0;
                    }
                }
            }
        } catch (BadLocationException e) {
            logger.error(e);
        }
        return -1;
    }

    private String getInsertText(Object o) {
        if (o instanceof OWLObject) {
            OWLModelManager mngr = this.owlEditorKit.getModelManager();
            return mngr.getRendering((OWLObject) o);
        } else {
            return o.toString();
        }
    }

    private int getEffectiveCaretPosition() {
        int startSel = this.textComponent.getSelectionStart();
        if (startSel >= 0) {
            return startSel;
        }
        return this.textComponent.getCaretPosition();
    }

    public void uninstall() {
        hidePopup();
        this.textComponent.removeKeyListener(this.keyListener);
        this.textComponent.removeComponentListener(this.componentListener);
        this.textComponent.removeFocusListener(this.focusListener);
        this.textComponent.removeHierarchyListener(this.hierarchyListener);
    }
}
