package org.aigebi.rbac.action;

import java.util.ArrayList;
import java.util.List;
import org.aigebi.rbac.bean.LtokenBean;
import org.aigebi.rbac.to.LtokenTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Ligong Xu
 * @version $Id: LtokenUpdate.java 1 2007-09-22 18:10:03Z ligongx $
 */
public class LtokenUpdate extends BaseLtokenAction {

    private static Log log = LogFactory.getLog(LtokenUpdate.class);

    private String ltokenId;

    public LtokenUpdate() {
        super();
    }

    public String input() throws Exception {
        setLtokenId(retrieveLtokenIdForUpdate());
        String input = super.input();
        LtokenTO ltoken = getTokenManager().getLtokenById(Long.valueOf(getLtokenId()));
        setLtokenBean(new LtokenBean(ltoken));
        return input;
    }

    /**expect request parameter ltokenId */
    private String retrieveLtokenIdForUpdate() {
        String ltokenId = getServletRequest().getParameter("ltokenId");
        return ltokenId;
    }

    public String update() throws Exception {
        List<String> msgArgs = new ArrayList<String>();
        msgArgs.add(getLtokenBean().getName());
        try {
            getTokenManager().updateLtoken(getLtokenBean().getLtokenTO());
            addSessionMessage(getText("ltoken.update.success", msgArgs));
            return SUCCESS;
        } catch (Throwable t) {
            log.error("updae ltoken error", t);
            addActionError(getText("ltoken.create.error", msgArgs));
            return INPUT;
        }
    }

    /** ltokenId for ltoken to be updated  */
    public String getLtokenId() {
        return ltokenId;
    }

    public void setLtokenId(String pLtokenId) {
        ltokenId = pLtokenId;
    }
}
