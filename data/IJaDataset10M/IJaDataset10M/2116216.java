package org.nakedobjects.webapp.view.simple;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.webapp.AbstractElementProcessor;
import org.nakedobjects.webapp.context.RequestContext;
import org.nakedobjects.webapp.processor.Request;
import org.nakedobjects.webapp.util.MethodsUtils;

public abstract class AbstractLink extends AbstractElementProcessor {

    public void process(Request request) {
        String object = request.getOptionalProperty(OBJECT);
        RequestContext context = request.getContext();
        String objectId = object != null ? object : (String) context.getVariable(RequestContext.RESULT);
        NakedObject adapter = MethodsUtils.findObject(context, objectId);
        if (valid(request, adapter)) {
            String variable = request.getOptionalProperty("param-name", RequestContext.RESULT);
            String variableSegment = variable + "=" + objectId;
            String view = request.getOptionalProperty(VIEW);
            if (view == null) {
                view = defaultView();
            }
            view = context.fullUriPath(view);
            String additionalSegment = additionalParameters(request);
            additionalSegment = additionalSegment == null ? "" : "&" + additionalSegment;
            request.appendHtml("<a class=\"action\" href=\"" + view + "?" + variableSegment + context.encodedInteractionParameters() + additionalSegment + "\">");
            request.processUtilCloseTag();
            request.appendHtml("</a>");
        } else {
            request.skipUntilClose();
        }
    }

    protected String additionalParameters(Request request) {
        return null;
    }

    protected abstract boolean valid(Request request, NakedObject adapter);

    protected abstract String defaultView();
}
