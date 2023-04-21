package com.ecmdeveloper.plugin.search.actions;

import java.util.HashMap;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import com.ecmdeveloper.plugin.search.model.QueryComponent;
import com.ecmdeveloper.plugin.search.parts.QueryComponentEditPart;

/**
 * @author ricardo.belfor
 *
 */
public class EditQueryComponentAction extends SelectionAction {

    public static final String QUERY_COMPONENT_KEY = "queryComponent";

    public static final String ID = "com.ecmdeveloper.plugin.search.actions.editAction";

    public static final String REQUEST_TYPE = "editQueryComponent";

    private static final String ACTION_NAME = "Edit";

    public EditQueryComponentAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText(ACTION_NAME);
    }

    @Override
    protected boolean calculateEnabled() {
        return isSingleItemSelected() && isQueryComponentSelected();
    }

    private boolean isSingleItemSelected() {
        return getSelectedObjects().size() == 1;
    }

    private boolean isQueryComponentSelected() {
        return (getSelectedObjects().get(0) instanceof QueryComponentEditPart);
    }

    @Override
    public void run() {
        execute(createEditCommand());
    }

    private Command createEditCommand() {
        if (!isSingleItemSelected()) {
            throw new UnsupportedOperationException();
        }
        Request request = new Request(REQUEST_TYPE);
        QueryComponentEditPart part = (QueryComponentEditPart) getSelectedObjects().get(0);
        QueryComponent queryComponent = (QueryComponent) part.getModel();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(QUERY_COMPONENT_KEY, queryComponent);
        request.setExtendedData(map);
        return part.getCommand(request);
    }
}
