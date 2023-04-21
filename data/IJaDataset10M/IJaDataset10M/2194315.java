package de.walware.statet.nico.internal.ui.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;
import de.walware.eclipsecommons.ui.SharedMessages;
import de.walware.eclipsecommons.ui.util.DNDUtil;
import de.walware.statet.nico.ui.views.HistoryView;

public class HistoryCopyAction extends BaseSelectionListenerAction {

    private final HistoryView fView;

    public HistoryCopyAction(final HistoryView view) {
        super(SharedMessages.CopyAction_name);
        setToolTipText(SharedMessages.CopyAction_tooltip);
        setId(ActionFactory.COPY.getId());
        setActionDefinitionId(IWorkbenchActionDefinitionIds.COPY);
        fView = view;
        view.getTableViewer().addSelectionChangedListener(this);
    }

    @Override
    protected boolean updateSelection(final IStructuredSelection selection) {
        return (selection.size() > 0);
    }

    @Override
    public void run() {
        final String text = HistoryView.createTextBlock(getStructuredSelection());
        DNDUtil.setContent(fView.getClipboard(), new String[] { text }, new Transfer[] { TextTransfer.getInstance() });
    }
}
