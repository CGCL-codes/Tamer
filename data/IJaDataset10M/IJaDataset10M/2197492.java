package common.web;

import common.CommonConstants;
import common.model.WebUser;
import common.util.EbUtil;
import common.util.FacesUtil;

/**
 *
 * @author shousuke
 */
public class NavigationController {

    private String currentPage = "/public/login.xhtml";

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String updateCurrentPage() {
        setCurrentPage(getPageFromRequest());
        return null;
    }

    private String getPageFromRequest() {
        String reqPage = FacesUtil.getRequestParameter(CommonConstants.REQ_CUR_PAGE);
        String reqUserPage = FacesUtil.getRequestParameter(CommonConstants.REQ_USR_CUR_PAGE);
        if (EbUtil.getUserFromSession() != null && reqUserPage != null) {
            return reqUserPage;
        } else {
            return reqPage;
        }
    }
}
