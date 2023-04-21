package blue.ui.core.orchestra.editor;

import java.util.HashMap;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import org.syntax.jedit.tokenmarker.JavaScriptTokenMarker;
import org.syntax.jedit.tokenmarker.PythonTokenMarker;
import org.syntax.jedit.tokenmarker.TokenMarker;
import skt.swing.SwingUtil;
import blue.BlueSystem;
import blue.actions.RedoAction;
import blue.actions.UndoAction;
import blue.event.SimpleDocumentListener;
import blue.gui.CsoundTokenMarker;
import blue.gui.InfoDialog;
import blue.orchestra.GenericInstrument;
import blue.orchestra.Instrument;
import blue.orchestra.PythonInstrument;
import blue.orchestra.RhinoInstrument;
import blue.orchestra.editor.GenericEditable;
import blue.orchestra.editor.InstrumentEditor;
import blue.ui.core.udo.EmbeddedOpcodeListPanel;
import blue.undo.NoStyleChangeUndoManager;
import blue.undo.TabSelectionWrapper;

/**
 * 
 * @author steven
 */
public class GenericEditor extends InstrumentEditor {

    private static HashMap tokenMarkerTypes = new HashMap();

    static {
        tokenMarkerTypes.put(GenericInstrument.class, new CsoundTokenMarker());
        tokenMarkerTypes.put(PythonInstrument.class, new PythonTokenMarker());
        tokenMarkerTypes.put(RhinoInstrument.class, new JavaScriptTokenMarker());
    }

    GenericEditable instr;

    EmbeddedOpcodeListPanel udoPanel = new EmbeddedOpcodeListPanel();

    UndoManager undo = new NoStyleChangeUndoManager();

    /** Creates new form GenericEditor2 */
    public GenericEditor() {
        initComponents();
        tabs.add(BlueSystem.getString("instrument.udo"), udoPanel);
        globalOrcEditPane.getDocument().addDocumentListener(new SimpleDocumentListener() {

            public void documentChanged(DocumentEvent e) {
                if (instr != null) {
                    instr.setGlobalOrc(globalOrcEditPane.getText());
                }
            }
        });
        globalScoEditPane.getDocument().addDocumentListener(new SimpleDocumentListener() {

            public void documentChanged(DocumentEvent e) {
                if (instr != null) {
                    instr.setGlobalSco(globalScoEditPane.getText());
                }
            }
        });
        textEditPane.getDocument().addDocumentListener(new SimpleDocumentListener() {

            public void documentChanged(DocumentEvent e) {
                if (instr != null) {
                    instr.setText(textEditPane.getText());
                }
            }
        });
        UndoableEditListener ul = new UndoableEditListener() {

            public void undoableEditHappened(UndoableEditEvent e) {
                UndoableEdit event = e.getEdit();
                if (event.getPresentationName().equals("style change")) {
                    undo.addEdit(event);
                } else {
                    TabSelectionWrapper wrapper = new TabSelectionWrapper(event, tabs);
                    undo.addEdit(wrapper);
                }
            }
        };
        textEditPane.getDocument().addUndoableEditListener(ul);
        globalOrcEditPane.getDocument().addUndoableEditListener(ul);
        globalScoEditPane.getDocument().addUndoableEditListener(ul);
        Action[] undoActions = new Action[] { new UndoAction(undo), new RedoAction(undo) };
        SwingUtil.installActions(textEditPane, undoActions);
        SwingUtil.installActions(globalOrcEditPane, undoActions);
        SwingUtil.installActions(globalScoEditPane, undoActions);
        undo.setLimit(1000);
    }

    public final void editInstrument(Instrument instr) {
        if (instr == null) {
            this.instr = null;
            editorLabel.setText(BlueSystem.getString("instrument.noEditorAvailable"));
            textEditPane.setText("Null Instrument");
            textEditPane.setEnabled(false);
            return;
        }
        if (!(instr instanceof GenericEditable)) {
            this.instr = null;
            editorLabel.setText(BlueSystem.getString("instrument.noEditorAvailable"));
            textEditPane.setText("[ERROR] GenericEditor::editInstrument - not instance of GenericEditable");
            textEditPane.setEnabled(false);
            return;
        }
        Object marker = tokenMarkerTypes.get(instr.getClass());
        if (marker != null) {
            textEditPane.setTokenMarker((TokenMarker) marker);
        }
        editorLabel.setText("Generic Editor - Type: " + instr.getClass().getName());
        this.instr = (GenericEditable) instr;
        textEditPane.setText(this.instr.getText());
        textEditPane.setEnabled(true);
        textEditPane.setCaretPosition(0);
        globalOrcEditPane.setText(this.instr.getGlobalOrc());
        globalOrcEditPane.setEnabled(true);
        globalOrcEditPane.setCaretPosition(0);
        globalScoEditPane.setText(this.instr.getGlobalSco());
        globalScoEditPane.setEnabled(true);
        globalScoEditPane.setCaretPosition(0);
        udoPanel.editOpcodeList(this.instr.getOpcodeList());
        undo.discardAllEdits();
    }

    private void initComponents() {
        editorLabel = new javax.swing.JLabel();
        testButton = new javax.swing.JButton();
        tabs = new javax.swing.JTabbedPane();
        textEditPane = new blue.gui.BlueEditorPane();
        globalOrcEditPane = new blue.gui.BlueEditorPane();
        globalScoEditPane = new blue.gui.BlueEditorPane();
        editorLabel.setText("jLabel1");
        testButton.setText(BlueSystem.getString("common.test"));
        testButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testButtonActionPerformed(evt);
            }
        });
        tabs.addTab(BlueSystem.getString("instrument.instrumentText"), textEditPane);
        tabs.addTab(BlueSystem.getString("global.orchestra"), globalOrcEditPane);
        tabs.addTab(BlueSystem.getString("global.score"), globalScoEditPane);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, tabs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE).add(layout.createSequentialGroup().add(editorLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 229, Short.MAX_VALUE).add(testButton))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(testButton).add(editorLabel)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(tabs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE).addContainerGap()));
    }

    private void testButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (this.instr == null) {
            return;
        }
        String instrumentText = ((Instrument) this.instr).generateInstrument();
        InfoDialog.showInformationDialog(SwingUtilities.getRoot(this), instrumentText, BlueSystem.getString("instrument.generatedInstrument"));
    }

    private javax.swing.JLabel editorLabel;

    private blue.gui.BlueEditorPane globalOrcEditPane;

    private blue.gui.BlueEditorPane globalScoEditPane;

    private javax.swing.JTabbedPane tabs;

    private javax.swing.JButton testButton;

    private blue.gui.BlueEditorPane textEditPane;

    @Override
    public Class getInstrumentClass() {
        return GenericEditable.class;
    }
}
