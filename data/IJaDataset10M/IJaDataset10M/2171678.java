package org.makagiga.editors.todo;

import static org.makagiga.commons.UI._;
import java.awt.BorderLayout;
import org.makagiga.Tabs;
import org.makagiga.commons.MColor;
import org.makagiga.commons.MDisposable;
import org.makagiga.commons.TK;
import org.makagiga.commons.html.HTMLBuilder;
import org.makagiga.commons.html.Linkify;
import org.makagiga.commons.html.MHTMLViewer;
import org.makagiga.commons.swing.MButton;
import org.makagiga.commons.swing.MTimer;
import org.makagiga.editors.Designer;
import org.makagiga.editors.Editor;
import org.makagiga.todo.Column;
import org.makagiga.todo.SummaryEditor;
import org.makagiga.todo.Task;

/**
 * @since 3.0
 */
public final class TodoEditorDesigner extends Designer implements MDisposable {

    private boolean hasValidSelection;

    private MButton summaryEditButton;

    private MHTMLViewer summaryViewer;

    private MTimer updateTimer;

    public TodoEditorDesigner() {
        super(new BorderLayout(5, 5));
        setDisplayText(_("Properties"));
        summaryViewer = new MHTMLViewer();
        summaryEditButton = new MButton(_("Edit Summary...")) {

            @Override
            protected void onClick() {
                Editor<?> editor = Tabs.getInstance().getSelectedTab();
                if (editor instanceof TodoEditor) TodoEditor.class.cast(editor).getEditorDesigner().editSummary();
            }
        };
        summaryEditButton.setRequestFocusEnabled(false);
        addCenter(summaryViewer);
        addNorth(summaryEditButton);
    }

    @Override
    public void setLocked(final boolean value) {
        super.setLocked(value);
        summaryEditButton.setEnabled(!value && hasValidSelection);
    }

    /**
	 * @inheritDoc
	 *
	 * @since 4.0
	 */
    @Override
    public void dispose() {
        summaryEditButton = null;
        updateTimer = TK.dispose(updateTimer);
    }

    private void editSummary() {
        TodoEditorCore core = Editor.getCurrentCore(TodoEditorCore.class);
        if (core == null) return;
        int modelRow = core.convertRowIndexToModel(core.getSelectedRow());
        int modelColumn = Column.SUMMARY.ordinal();
        Task task = core.getModel().getRowAt(modelRow);
        String summary = SummaryEditor.edit(getWindowAncestor(), task.getSummary());
        if (summary != null) {
            core.getModel().setValueAt(summary, modelRow, modelColumn);
            updateComponents(core);
        }
    }

    private void updateComponents(final TodoEditorCore core) {
        HTMLBuilder html = new HTMLBuilder();
        html.beginHTML();
        html.beginStyle();
        html.beginRule("a");
        html.addAttr("color", MColor.getLinkForeground(summaryViewer.getBackground()));
        html.endRule();
        html.beginRule("p, pre");
        html.addAttr("margin-left", "0px");
        html.addAttr("margin-right", "0px");
        html.addAttr("margin-top", "5px");
        html.addAttr("margin-bottom", "5px");
        html.addAttr("padding", "0px");
        html.endRule();
        html.endStyle();
        html.beginDoc();
        html.addHeader(2, html.escape(core.getSelectionText()));
        int selectionCount = core.getSelectedRowCount();
        hasValidSelection = selectionCount == 1;
        if (hasValidSelection) {
            summaryEditButton.setEnabled(!isLocked());
            Task task = core.getModel().getRowAt(core.convertRowIndexToModel(core.getSelectedRow()));
            html.singleTag("hr");
            html.appendLine(task.getToolTipTable(false));
            html.singleTag("hr");
            String summary = task.getSummary();
            html.beginTag("p");
            Linkify linkify = Linkify.getInstance();
            StringBuilder s = linkify.applyToHTML(html.escape(summary));
            TK.fastReplace(s, "\n", "<br>");
            html.append(s);
            html.appendLine();
            html.endTag("p");
        } else {
            summaryEditButton.setEnabled(false);
        }
        html.endDoc();
        summaryViewer.setHTML(html);
    }

    void updateComponentsLater(final TodoEditorCore core) {
        if (updateTimer == null) {
            updateTimer = new MTimer(500) {

                @Override
                protected boolean onTimeout() {
                    TodoEditorDesigner.this.updateComponents(core);
                    return false;
                }
            };
        }
        updateTimer.restart();
    }
}
