package org.nakedobjects.plugins.html.action.edit;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.plugins.html.action.Action;
import org.nakedobjects.plugins.html.action.ActionException;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.ForwardRequest;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.plugins.html.task.EditTask;

public class EditObject implements Action {

    public void execute(final Request request, final Context context, final Page page) {
        final String idString = request.getObjectId();
        if (idString == null) {
            throw new ActionException("Task no longer in progress");
        }
        final NakedObject object = context.getMappedObject(idString);
        if (object.getResolveState() != ResolveState.TRANSIENT) {
            context.setObjectCrumb(object);
        }
        final EditTask editTask = new EditTask(context, object);
        context.addTaskCrumb(editTask);
        request.forward(ForwardRequest.task(editTask));
    }

    public String name() {
        return Request.EDIT_COMMAND;
    }
}
