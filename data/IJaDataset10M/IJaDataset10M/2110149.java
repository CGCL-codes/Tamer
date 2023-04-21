package com.safi.workshop.edit.policies;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenEditPolicy;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import com.safi.core.actionstep.DBQueryId;
import com.safi.core.actionstep.DBQueryParamId;
import com.safi.core.actionstep.SetQueryParam;
import com.safi.workshop.edit.parts.SetQueryParamEditPart;
import com.safi.workshop.sheet.DBResourceChooser;

public class OpenQueryParamEditPolicy extends OpenEditPolicy {

    public OpenQueryParamEditPolicy() {
    }

    @Override
    protected Command getOpenCommand(Request request) {
        EditPart target = getTargetEditPart(request);
        final SetQueryParam openQuery = (SetQueryParam) ((SetQueryParamEditPart) target).getActionStep();
        DBQueryParamId id = openQuery.getParameter();
        return new ICommandProxy(new OpenQueryParamCommand((GraphicalEditPart) target, id, openQuery));
    }

    class OpenQueryParamCommand extends AbstractTransactionalCommand {

        private final DBQueryParamId id;

        private final SetQueryParam setQueryParam;

        public OpenQueryParamCommand(GraphicalEditPart editPart, DBQueryParamId id, SetQueryParam openQuery) {
            super(editPart.getEditingDomain(), "Open Query", null);
            this.id = id;
            this.setQueryParam = openQuery;
        }

        @Override
        protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
            try {
                DBResourceChooser chooser = new DBResourceChooser(Display.getDefault().getActiveShell());
                chooser.setSelectedId(id);
                chooser.setMode(DBResourceChooser.Mode.QUERY_PARAM);
                DBQueryId did = setQueryParam.getQuery();
                if (did == null) {
                    chooser.setTitleText("No resources available");
                    chooser.setMessageText("You must first select a query");
                    chooser.setDisabled(true);
                } else {
                    String sid = did.getId();
                    chooser.setParentId(sid);
                    chooser.setMessageText("Select a query parameter");
                }
                int result = chooser.open();
                if (result == Window.OK) {
                    Object newVal = chooser.getSelectedId();
                    if (newVal != id) {
                        setQueryParam.setParameter((DBQueryParamId) newVal);
                    }
                }
                return CommandResult.newOKCommandResult();
            } catch (Exception ex) {
                throw new ExecutionException("Can't open diagram", ex);
            }
        }
    }
}
