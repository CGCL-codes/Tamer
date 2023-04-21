package org.aigebi.rbac.action;

import java.util.ArrayList;
import java.util.List;
import org.aigebi.rbac.bean.OtokenBean;
import org.aigebi.rbac.to.OtokenTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Ligong Xu
 * @version $Id: OtokenUpdate.java 1 2007-09-22 18:10:03Z ligongx $
 */
public class OtokenUpdate extends BaseOtokenAction {

    private static Log log = LogFactory.getLog(OtokenUpdate.class);

    private String otokenId;

    public OtokenUpdate() {
        super();
    }

    public String input() throws Exception {
        setOtokenId(retrieveOtokenIdForUpdate());
        String input = super.input();
        OtokenTO otoken = getTokenManager().getOtokenById(Long.valueOf(getOtokenId()));
        setOtokenBean(new OtokenBean(otoken));
        return input;
    }

    /**expect request parameter otokenId */
    private String retrieveOtokenIdForUpdate() {
        String otokenId = getServletRequest().getParameter("otokenId");
        return otokenId;
    }

    public String update() throws Exception {
        List<String> msgArgs = new ArrayList<String>();
        msgArgs.add(getOtokenBean().getName());
        try {
            getTokenManager().updateOtoken(getOtokenBean().getOtokenTO());
            addSessionMessage(getText("otoken.update.success", msgArgs));
            return SUCCESS;
        } catch (Throwable t) {
            log.error(t);
            addActionError(getText("otoken.create.error", msgArgs));
            return INPUT;
        }
    }

    /** otokenId for otoken to be updated  */
    public String getOtokenId() {
        return otokenId;
    }

    public void setOtokenId(String pOtokenId) {
        otokenId = pOtokenId;
    }
}
