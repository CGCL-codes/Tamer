package org.systemsEngineering.workbench.core.ui.editors;

import org.eclipse.jface.action.*;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;

/**
 * Manages the installation/deinstallation of global actions for multi-page
 * editors. Responsible for the redirection of global actions to the active
 * editor. Multi-page contributor replaces the contributors for the individual
 * editors in the multi-page editor.
 */
public class MultiPageEditorContributor extends MultiPageEditorActionBarContributor {

    private IEditorPart activeEditorPart;

    private Action addInputValueAction;

    private Action removeInputValueAction;

    private Action addResultValueAction;

    private Action removeResultValueAction;

    /**
         * Creates a multi-page contributor.
         */
    public MultiPageEditorContributor() {
        super();
        createActions();
    }

    /**
         * Returns the action registed with the given text editor.
         * 
         * @return IAction or null if editor is null.
         */
    protected IAction getAction(ITextEditor editor, String actionID) {
        return (editor == null ? null : editor.getAction(actionID));
    }

    public void setActivePage(IEditorPart part) {
        if (activeEditorPart == part) return;
        activeEditorPart = part;
        IActionBars actionBars = getActionBars();
        if (actionBars != null) {
            ITextEditor editor = (part instanceof ITextEditor) ? (ITextEditor) part : null;
            actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), getAction(editor, ITextEditorActionConstants.DELETE));
            actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), getAction(editor, ITextEditorActionConstants.UNDO));
            actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), getAction(editor, ITextEditorActionConstants.REDO));
            actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), getAction(editor, ITextEditorActionConstants.CUT));
            actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), getAction(editor, ITextEditorActionConstants.COPY));
            actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), getAction(editor, ITextEditorActionConstants.PASTE));
            actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), getAction(editor, ITextEditorActionConstants.SELECT_ALL));
            actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), getAction(editor, ITextEditorActionConstants.FIND));
            actionBars.setGlobalActionHandler(IDEActionFactory.BOOKMARK.getId(), getAction(editor, IDEActionFactory.BOOKMARK.getId()));
            actionBars.updateActionBars();
        }
    }

    private void createActions() {
        addInputValueAction = new Action() {

            public void run() {
                WgaTable.addInputValue("New Influence");
            }
        };
        addInputValueAction.setImageDescriptor(ImageDescriptor.createFromImage(new Image(Display.getDefault(), "/home/msteinb/Inferno/ecl-proj/org.systemsEngineering.workbench.core.ui/icons/insertInputValue.gif")));
        removeInputValueAction = new Action() {

            public void run() {
                try {
                    WgaTable.removeInputValue();
                } catch (WgaTableSelectionException wgaTableSelectionException) {
                    wgaTableSelectionException.showMessageDialog();
                }
            }
        };
        removeInputValueAction.setImageDescriptor(ImageDescriptor.createFromImage(new Image(Display.getDefault(), "/home/msteinb/Inferno/ecl-proj/org.systemsEngineering.workbench.core.ui/icons/deleteInputValue.gif")));
        addResultValueAction = new Action() {

            public void run() {
                WgaTable.addResultValue("New Effect");
            }
        };
        addResultValueAction.setImageDescriptor(ImageDescriptor.createFromImage(new Image(Display.getDefault(), "/home/msteinb/Inferno/ecl-proj/org.systemsEngineering.workbench.core.ui/icons/insertResultValue.gif")));
        removeResultValueAction = new Action() {

            public void run() {
                try {
                    WgaTable.removeResultValue();
                } catch (WgaTableSelectionException wgaTableSelectionException) {
                    wgaTableSelectionException.showMessageDialog();
                }
            }
        };
        removeResultValueAction.setImageDescriptor(ImageDescriptor.createFromImage(new Image(Display.getDefault(), "/home/msteinb/Inferno/ecl-proj/org.systemsEngineering.workbench.core.ui/icons/deleteResultValue.gif")));
    }

    public void contributeToMenu(IMenuManager manager) {
    }

    public void contributeToToolBar(IToolBarManager manager) {
        manager.add(new Separator());
        manager.add(addInputValueAction);
        manager.add(removeInputValueAction);
        manager.add(addResultValueAction);
        manager.add(removeResultValueAction);
    }
}
