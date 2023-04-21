package org.pubcurator.core.handlers;

import java.util.List;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.pubcurator.core.dialogs.EditTagsDialog;
import org.pubcurator.core.managers.ServerManager;
import org.pubcurator.core.managers.SqlQueryManager;
import org.pubcurator.core.managers.UIManager;
import org.pubcurator.core.utils.ErrorUtil;
import org.pubcurator.core.views.DocumentsView;
import org.pubcurator.model.document.PubDocument;

public class EditDocumentTagsHandler extends AbstractHandler {

    @SuppressWarnings("unchecked")
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        DocumentsView view = (DocumentsView) UIManager.INSTANCE.getView(DocumentsView.class);
        IStructuredSelection selection = (IStructuredSelection) view.getSite().getSelectionProvider().getSelection();
        List<PubDocument> documents = selection.toList();
        List<String> tags = SqlQueryManager.INSTANCE.getCommonTags(documents);
        EditTagsDialog dialog = new EditTagsDialog(HandlerUtil.getActiveShell(event), tags);
        if (dialog.open() == EditTagsDialog.OK) {
            for (PubDocument document : documents) {
                document.getTags().clear();
                document.getTags().addAll(tags);
            }
            try {
                ServerManager.INSTANCE.getMainTransaction().commit();
            } catch (CommitException e) {
                ErrorUtil.databaseError(e);
            }
        }
        return null;
    }
}
