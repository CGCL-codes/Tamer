package org.nakedobjects.webapp.edit;

import java.io.IOException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.webapp.Action;
import org.nakedobjects.webapp.ForbiddenException;
import org.nakedobjects.webapp.ScimpiException;
import org.nakedobjects.webapp.context.RequestContext;
import org.nakedobjects.webapp.context.RequestContext.Scope;
import org.nakedobjects.webapp.debug.DebugView;

/**
 * Remove an element from a collection. 
 */
public class RemoveAction implements Action {

    public static final String ACTION = "remove";

    public String getName() {
        return ACTION;
    }

    public void process(RequestContext context) throws IOException {
        String parentId = context.getParameter(OBJECT);
        String rowId = context.getParameter(ELEMENT);
        try {
            NakedObject parent = (NakedObject) context.getMappedObject(parentId);
            NakedObject row = (NakedObject) context.getMappedObject(rowId);
            String fieldName = context.getParameter(FIELD);
            NakedObjectAssociation field = parent.getSpecification().getAssociation(fieldName);
            if (field == null) {
                throw new ScimpiException("No field " + fieldName + " in " + parent.getSpecification().getFullName());
            }
            if (field.isVisible(NakedObjectsContext.getAuthenticationSession(), parent).isVetoed()) {
                throw new ForbiddenException("Field " + fieldName + " in " + parent + " is not visible");
            }
            ((OneToManyAssociation) field).removeElement(parent, row);
            String view = context.getParameter(VIEW);
            String override = context.getParameter(RESULT_OVERRIDE);
            String resultName = context.getParameter(RESULT_NAME);
            resultName = resultName == null ? RequestContext.RESULT : resultName;
            String id = context.mapObject(parent, Scope.REQUEST);
            context.addVariable(resultName, id, Scope.REQUEST);
            if (override != null) {
                context.addVariable(resultName, override, Scope.REQUEST);
            }
            int questionMark = view == null ? -1 : view.indexOf("?");
            if (questionMark > -1) {
                String params = view.substring(questionMark + 1);
                int equals = params.indexOf("=");
                context.addVariable(params.substring(0, equals), params.substring(equals + 1), Scope.REQUEST);
                view = view.substring(0, questionMark);
            }
            context.setRequestPath(view);
        } catch (RuntimeException e) {
            NakedObjectsContext.getMessageBroker().getMessages();
            NakedObjectsContext.getMessageBroker().getWarnings();
            NakedObjectsContext.getUpdateNotifier().clear();
            NakedObjectsContext.getUpdateNotifier().clear();
            throw e;
        }
    }

    public void init() {
    }

    public void debug(DebugView view) {
    }
}
