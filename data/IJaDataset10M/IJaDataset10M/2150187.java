package com.prime.yui4jsf.util;

import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Latest modification by $Author: cagatay_civici $
 * @version $Revision: 1277 $ $Date: 2008-04-20 07:53:46 -0400 (Sun, 20 Apr 2008) $
 */
public class AutoCompletePhaseListener implements PhaseListener {

    Logger log = Logger.getLogger(AutoCompletePhaseListener.class.getName());

    public void afterPhase(PhaseEvent phaseEvent) {
        FacesContext context = phaseEvent.getFacesContext();
        if (context.getViewRoot().getViewId().indexOf(Yui4JSFConstants.YUI4JSF_AUTOCOMPLETE_TOKEN) != -1) {
            handleAutoCompleteResponse(context);
        }
    }

    public void beforePhase(PhaseEvent phaseEvent) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    private void handleAutoCompleteResponse(FacesContext context) {
        String query = (String) context.getExternalContext().getRequestParameterMap().get("query");
        String methodBindingExpr = (String) context.getExternalContext().getRequestParameterMap().get("method");
        String size = (String) context.getExternalContext().getRequestParameterMap().get("size");
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        try {
            MethodBinding mb = null;
            List results = null;
            if (!size.equals("-1")) {
                mb = context.getApplication().createMethodBinding("#{" + methodBindingExpr + "}", new Class[] { String.class, Integer.class });
                results = (List) mb.invoke(context, new Object[] { query, new Integer(size) });
            } else {
                mb = context.getApplication().createMethodBinding("#{" + methodBindingExpr + "}", new Class[] { String.class });
                results = (List) mb.invoke(context, new Object[] { query });
            }
            PrintWriter writer = response.getWriter();
            if (results != null) {
                for (int i = 0; i < results.size(); i++) {
                    writer.write(results.get(i).toString());
                    writer.write("\n");
                }
            }
        } catch (Exception exception) {
            throw new FacesException(exception);
        } finally {
            context.responseComplete();
        }
    }
}
