package org.dcm4chex.archive.web.maverick.ae;

import javax.servlet.http.HttpServletRequest;
import org.dcm4chex.archive.ejb.interfaces.AEDTO;

/**
 * @author umberto.cappellini@tiani.com
 * @author gunter.zeilinger@tiani.com
 * @version $Revision: 11261 $ $Date: 2009-05-08 04:15:19 -0400 (Fri, 08 May 2009) $
 */
public class AEEditSubmitCtrl extends AEFormCtrl {

    protected String perform() throws Exception {
        HttpServletRequest request = getCtx().getRequest();
        AEModel model = AEModel.getModel(request);
        model.clearPopupMsg();
        AEDelegate delegate = lookupAEDelegate();
        if (request.getParameter("update") != null) {
            AEDTO newAE = model.getAE();
            try {
                lookupAEDelegate().updateAE(newAE, model.isCheckHost());
                return SUCCESS;
            } catch (Throwable e) {
                Throwable t = e.getCause();
                if (t == null) t = e;
                model.setPopupMsg("ae.err_chg", t.getMessage());
                return "failed";
            }
        } else if (request.getParameter("echo") != null) {
            model.setPopupMsg("ae.echo", delegate.echo(model.getAE(), 5));
            return SUCCESS;
        } else return SUCCESS;
    }
}
