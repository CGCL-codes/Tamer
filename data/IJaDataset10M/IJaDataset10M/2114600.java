package com.windsor.node.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.windsor.node.admin.util.AdminConstants;
import com.windsor.node.admin.util.VisitUtils;
import com.windsor.node.common.domain.NodeVisit;
import com.windsor.node.common.service.admin.TransactionService;

public class DocController implements Controller, InitializingBean {

    private static final long serialVersionUID = 1;

    private static final String NO_CACHE = "no-cache";

    protected Logger logger = LoggerFactory.getLogger(DocController.class);

    private TransactionService transactionService;

    public void afterPropertiesSet() throws Exception {
        if (transactionService == null) {
            throw new Exception("transactionService not set");
        }
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        NodeVisit visit = VisitUtils.getVisit(request);
        if (visit == null) {
            logger.debug(AdminConstants.UNAUTHED_CONFIG);
            throw new IllegalArgumentException(AdminConstants.UNAUTHED_CONFIG + ": No visit.");
        }
        String id = ServletRequestUtils.getRequiredStringParameter(request, "id");
        String tid = ServletRequestUtils.getRequiredStringParameter(request, "tid");
        String name = ServletRequestUtils.getRequiredStringParameter(request, "name");
        if (StringUtils.isBlank(id) && StringUtils.isBlank(tid) && StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Invalid transaction or document Id");
        }
        logger.debug("Download document with\nTransaction id: " + tid + "\nDocument Id: " + id + "\nDocument Name: " + name);
        byte[] content = transactionService.downloadContent(tid, id, visit);
        logger.debug("Content length (in bytes): " + content.length);
        response.setHeader("Cache-Control", "must-revalidate");
        response.setBufferSize(content.length);
        response.setContentType("application/zip");
        response.setContentLength(content.length);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
        FileCopyUtils.copy(content, response.getOutputStream());
        return null;
    }

    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
}
