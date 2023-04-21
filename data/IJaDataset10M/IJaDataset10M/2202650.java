package org.nakedobjects.webapp.view.debug;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.webapp.AbstractElementProcessor;
import org.nakedobjects.webapp.context.RequestContext;
import org.nakedobjects.webapp.processor.Request;

public class Specification extends AbstractElementProcessor {

    public void process(Request request) {
        if (request.isRequested("always") || request.getContext().getDebug() == RequestContext.Debug.ON) {
            request.appendHtml("<div class=\"debug\">");
            request.appendHtml("<pre>");
            String id = request.getOptionalProperty("object");
            NakedObject object = request.getContext().getMappedObjectOrResult(id);
            NakedObjectSpecification specification = object.getSpecification();
            request.appendHtml(specification.getSingularName() + " (" + specification.getFullName() + ") \n");
            NakedObjectAssociation[] fields = specification.getAssociations();
            for (int i = 0; i < fields.length; i++) {
                request.appendHtml("    " + fields[i].getName() + " (" + fields[i].getSpecification().getSingularName() + ") \n");
            }
            request.appendHtml("</pre>");
            request.appendHtml("</div>");
        }
    }

    public String getName() {
        return "specification";
    }
}
