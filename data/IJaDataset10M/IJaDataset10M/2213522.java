package org.appfuse.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.service.UserManager;
import org.appfuse.web.util.PagingController;
import org.springframework.web.servlet.ModelAndView;

public class UserPagingController extends PagingController {

    private static Log log = LogFactory.getLog(UserPagingController.class);

    private UserManager mgr = null;

    public void setUserManager(UserManager userManager) {
        this.mgr = userManager;
    }

    protected ModelAndView doHandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getParameter("from") != null) {
            log.info("-----------goto page " + request.getParameter("from"));
            super.goToPage(request, new Integer(request.getParameter("from")).intValue());
        }
        int recordsBeginNo = computeRecordsBeginNo(request);
        int recordsNumberToRead = computeRecordsNumberToRead(request);
        log.info("recordsBeginNo:" + recordsBeginNo + "|recordsNumberToRead:" + recordsNumberToRead);
        return new ModelAndView("testPaging", "users", mgr.getUsersByPage(recordsBeginNo, recordsNumberToRead));
    }

    protected int getTotalRecordsNumber(HttpServletRequest request) {
        return mgr.getUsers().size();
    }
}
