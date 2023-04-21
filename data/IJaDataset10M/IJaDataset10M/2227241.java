package org.nakedobjects.webapp.view.debug;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.webapp.AbstractElementProcessor;
import org.nakedobjects.webapp.ForbiddenException;
import org.nakedobjects.webapp.ScimpiException;
import org.nakedobjects.webapp.processor.Request;

public class Members extends AbstractElementProcessor {

    public String getName() {
        return "members";
    }

    public void process(Request request) {
        String id = request.getOptionalProperty(OBJECT);
        String fieldName = request.getOptionalProperty(FIELD);
        request.appendHtml("<pre class=\"debug\">");
        try {
            NakedObject object = request.getContext().getMappedObjectOrResult(id);
            NakedObjectAssociation field = null;
            if (fieldName != null) {
                field = object.getSpecification().getAssociation(fieldName);
                if (field.isVisible(NakedObjectsContext.getAuthenticationSession(), object).isVetoed()) {
                    throw new ForbiddenException("Field " + fieldName + " in " + object + " is not visible");
                }
                object = field.get(object);
            }
            request.processUtilCloseTag();
            NakedObjectSpecification specification = field == null ? object.getSpecification() : field.getSpecification();
            request.appendHtml(specification.getSingularName() + " (" + specification.getFullName() + ") \n");
            NakedObjectAssociation[] fields = specification.getAssociations();
            for (NakedObjectAssociation fld : fields) {
                if (!fld.isAlwaysHidden()) {
                    request.appendHtml("   " + fld.getId() + " - '" + fld.getName() + "' -> " + fld.getSpecification().getSingularName() + (fld.isOneToManyAssociation() ? " (collection of)" : "") + "\n");
                }
            }
            request.appendHtml("   --------------\n");
            NakedObjectAction[] actions = specification.getObjectActions(NakedObjectActionType.USER);
            ;
            for (NakedObjectAction action : actions) {
                request.appendHtml("   " + action.getId() + " (");
                boolean first = true;
                for (NakedObjectActionParameter parameter : action.getParameters()) {
                    if (!first) {
                        request.appendHtml(", ");
                    }
                    request.appendHtml(parameter.getSpecification().getSingularName());
                    first = false;
                }
                request.appendHtml(")" + " - '" + action.getName() + "'");
                if (action.getSpecification() != null) {
                    request.appendHtml(" -> " + action.getSpecification().getSingularName() + ")");
                }
                request.appendHtml("\n");
            }
        } catch (ScimpiException e) {
            request.appendHtml("Debug failed: " + e.getMessage());
        }
        request.appendHtml("</pre>");
    }
}
