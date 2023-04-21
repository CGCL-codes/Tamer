package net.sourceforge.syncyoursecrets.gui.rcp.handler;

import net.sourceforge.syncyoursecrets.gui.rcp.View;
import net.sourceforge.syncyoursecrets.gui.rcp.model.IPWEditor;
import net.sourceforge.syncyoursecrets.gui.rcp.model.entry.PWEditorInput;
import net.sourceforge.syncyoursecrets.gui.rcp.model.entry.PWEntryEditor;
import net.sourceforge.syncyoursecrets.gui.rcp.model.table.PWTableEditor;
import net.sourceforge.syncyoursecrets.gui.rcp.model.table.PWTableInput;
import net.sourceforge.syncyoursecrets.gui.rcp.util.MessageDialog;
import net.sourceforge.syncyoursecrets.gui.rcp.util.SingletonHolder;
import net.sourceforge.syncyoursecrets.model.pw.PWEntry;
import net.sourceforge.syncyoursecrets.model.pw.PWTable;
import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

/**
 * The Class CallEditorHandler handles calls to open an editor. It attempts to
 * reuse already openend editor windows.
 * 
 * 
 * @author Jan Petranek
 */
public class CallEditorHandler extends AbstractHandler implements IHandler {

    /** The Constant logger. */
    static final Logger logger = Logger.getLogger(CallEditorHandler.class);

    /**
	 * ID of the call editor, as defined in plugin xml.
	 */
    public static final String CALL_EDITOR_ID = "SyncYourSecrets.callEditor";

    /**
	 * Opens the selected Item in an editor. Attempts to reuse already opened
	 * editors.
	 * 
	 * @param event
	 *            the event
	 * 
	 * @return null
	 * 
	 * @throws ExecutionException
	 *             the execution exception
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 *      ExecutionEvent)
	 */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        logger.debug("entering execute");
        View view = SingletonHolder.getView();
        IWorkbenchPage page = view.getSite().getPage();
        TableViewer viewer = view.getViewer();
        Object obj = view.getSelectedObject();
        try {
            if (obj != null) {
                if (obj instanceof PWEntry) {
                    logger.debug("Selected PWEntry");
                    PWEntry pwEntry = (PWEntry) obj;
                    IEditorInput input = new PWEditorInput(view, pwEntry);
                    reuseEditor(page, viewer, input, PWEntryEditor.ID);
                } else if (obj instanceof PWTable) {
                    logger.debug("Selected PWTable");
                    PWTable pwTable = (PWTable) obj;
                    PWTableInput input = new PWTableInput(view, pwTable);
                    reuseEditor(page, viewer, input, PWTableEditor.ID);
                } else {
                    String msg = "Selected Element must be one of PWEntry, PWTable";
                    MessageDialog.showUnexpectedErrorMessage(msg, logger);
                    throw new ExecutionException(msg);
                }
            } else {
                closeAllEditors(page);
            }
        } catch (PartInitException e) {
            String msg = "Calling editor failed";
            MessageDialog.showUnexpectedErrorMessage(msg, logger, e);
            throw new ExecutionException(msg, e);
        }
        view.refresh();
        logger.debug("exiting execute");
        return null;
    }

    /**
	 * Attempts to reuse an IPWEditor. If no matching editor is already opened,
	 * a new one is opened.
	 * 
	 * @param page
	 *            the page
	 * @param viewer
	 *            the viewer
	 * @param input
	 *            the input
	 * @param editorId
	 *            the editor id
	 * 
	 * @throws PartInitException
	 *             the part init exception
	 */
    private void reuseEditor(IWorkbenchPage page, TableViewer viewer, IEditorInput input, String editorId) throws PartInitException {
        logger.debug("entering reuseEditor for EditorId " + editorId);
        IEditorReference[] editorReferences = page.getEditorReferences();
        IPWEditor editor = null;
        for (IEditorReference ref : editorReferences) {
            if (ref.getEditor(true) instanceof IPWEditor) {
                IPWEditor pwEditor = (IPWEditor) ref.getEditor(true);
                logger.debug("Editor present: " + pwEditor.getId());
                if (editorId.equals(pwEditor.getId())) {
                    editor = pwEditor;
                } else {
                    logger.debug("Closing editor " + pwEditor.getId());
                    page.closeEditor(pwEditor, false);
                }
            }
        }
        if (editor != null) {
            logger.debug("Reusable Editor found for " + editorId);
            page.reuseEditor(editor, input);
            viewer.setSelection(viewer.getSelection());
        } else {
            logger.debug("No reusable Editor found, creating new one of for " + editorId);
            page.openEditor(input, editorId);
        }
        logger.debug("exiting reuseEditor");
    }

    private void closeAllEditors(IWorkbenchPage page) {
        logger.debug("Closing all editors");
        page.closeAllEditors(false);
    }
}
