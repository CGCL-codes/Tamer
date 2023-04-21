package com.asoft.common.base.web.service;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.asoft.common.base.manager.Manager;
import com.asoft.common.base.model.BaseObject;

/**
 * 修改实体pri，供排序用
 *
 *  created by amon 2005-9-3 16:19
 */
public class ChangePriService extends TempletService {

    static Logger logger = Logger.getLogger(ChangePriService.class);

    private Manager manager;

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    /** 执行服务 */
    public void executeService(HttpServletRequest request) {
        logger.info("切换pri Service");
        BaseObject sbo = (BaseObject) request.getAttribute("sBO");
        this.iniUserInforIntoOldBO(request, sbo);
        BaseObject destbo = (BaseObject) request.getAttribute("destBO");
        this.iniUserInforIntoOldBO(request, destbo);
        this.manager.changePri(sbo, destbo);
    }

    /** 执行服务前（for 操作日志）*/
    public String getDetailBeforeExe(HttpServletRequest request) {
        BaseObject sbo = (BaseObject) request.getAttribute("sBO");
        BaseObject destbo = (BaseObject) request.getAttribute("destBO");
        StringBuffer sb = new StringBuffer(1000);
        sb.append("<old>").append(sbo.getModelDetailXML()).append(destbo.getModelDetailXML()).append("</old>");
        return sb.toString();
    }

    /** 执行服务后（for 操作日志）*/
    public String getDetailAfterExe(HttpServletRequest request) {
        BaseObject sbo = (BaseObject) request.getAttribute("sBO");
        BaseObject destbo = (BaseObject) request.getAttribute("destBO");
        StringBuffer sb = new StringBuffer(1000);
        sb.append("<new>").append(sbo.getModelDetailXML()).append(destbo.getModelDetailXML()).append("</new>");
        return sb.toString();
    }
}
