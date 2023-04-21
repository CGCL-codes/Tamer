package org.paradise.dms.web.action.registerinfo;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.paradise.dms.pojo.RegisterLargeItem;
import org.paradise.dms.services.RegisterLargeItemService;
import org.paradise.dms.web.action.DMSBaseAction;
import org.paradise.dms.web.tools.Pager;
import org.paradise.dms.web.tools.PagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Description: Copyright (c) 2008-2009 Neo. All Rights Reserved.
 * 
 * @version 1.0 Apr 3, 2009 10:16:51 PM 李萌（neolimeng@gmail.com）created
 */
@Service
public class RegisterLargeItemAction extends DMSBaseAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4000611726594825502L;

    private static Logger log = Logger.getLogger(RegisterLargeItemAction.class);

    @Autowired
    @Qualifier("registerLargeItemServiceImpl")
    private RegisterLargeItemService registerLargeItemService;

    private RegisterLargeItem rli;

    private List<RegisterLargeItem> rlil;

    @Autowired
    private PagerService pagerService;

    private Pager pager;

    protected String currentPage;

    protected String totalRows;

    protected String pagerMethod;

    public String listAllRegisterLargeItemIn() {
        try {
            HttpSession session = ServletActionContext.getRequest().getSession();
            String recorderId = (String) session.getAttribute("systemuserid");
            log.info("DMS_info_用户操作，操作者ID为" + recorderId);
            int totalRows = this.registerLargeItemService.getAllRowsIn();
            pager = pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRows);
            this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
            this.setTotalRows(String.valueOf(totalRows));
            rlil = this.registerLargeItemService.listAllRegisterLargeItemIn(pager.getPageSize(), pager.getStartRow());
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return INPUT;
        }
    }

    public String listAllRegisterLargeItemOut() {
        try {
            HttpSession session = ServletActionContext.getRequest().getSession();
            String recorderId = (String) session.getAttribute("systemuserid");
            log.info("DMS_info_用户操作，操作者ID为" + recorderId);
            int totalRows = this.registerLargeItemService.getAllRowsOut();
            pager = pagerService.getPager(this.getCurrentPage(), this.getPagerMethod(), totalRows);
            this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
            this.setTotalRows(String.valueOf(totalRows));
            rlil = this.registerLargeItemService.listAllRegisterLargeItemOut(pager.getPageSize(), pager.getStartRow());
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return INPUT;
        }
    }

    /**
	 * 
	 * Description: Action-插入一条大物品入登记记录
	 * 
	 * @Version1.0 Apr 3, 2009 10:25:23 PM 李萌（neolimeng@gmail.com）创建
	 * @return
	 */
    public String insertReigsterLargeItemIn() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        String recorderId = (String) session.getAttribute("systemuserid");
        String recorderName = (String) session.getAttribute("systemusername");
        log.info("DMS_info_用户操作，操作者ID为" + recorderId);
        rli.setRegisterlargeitemind(1);
        rli.setRegisterlargeitemintime(new Date());
        rli.setRecordby(recorderName);
        rli.setInorout(0);
        if (this.registerLargeItemService.insertRegisterLargeItem(rli)) return SUCCESS; else return INPUT;
    }

    /**
	 * 
	 * Description: Action-更新一条大物品入登记记录
	 * 
	 * @Version1.0 Apr 3, 2009 10:26:53 PM 李萌（neolimeng@gmail.com）创建
	 * @return
	 */
    public String updateRegisterLargeItemIn() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        String recorderId = (String) session.getAttribute("systemuserid");
        String recorderName = (String) session.getAttribute("systemusername");
        log.info("DMS_info_用户操作，操作者ID为" + recorderId);
        rli.setRegisterlargeitemind(1);
        rli.setRegisterlargeitemintime(new Date());
        rli.setRecordby(recorderName);
        rli.setInorout(0);
        if (this.registerLargeItemService.updateRegisterLargeItem(rli)) return SUCCESS; else return INPUT;
    }

    /**
	 * 
	 * Description: Action-删除一条大物品入登记记录
	 * 
	 * @Version1.0 Apr 3, 2009 10:30:19 PM 李萌（neolimeng@gmail.com）创建
	 * @return
	 */
    public String deleteRegisterLargeItemIn() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        String recorderId = (String) session.getAttribute("systemuserid");
        log.info("DMS_info_用户操作，操作者ID为" + recorderId);
        int rliid = Integer.parseInt(ServletActionContext.getRequest().getParameter("registerlargeitemid"));
        if (this.registerLargeItemService.deleteRegisterLargeItem(rliid)) return SUCCESS; else return INPUT;
    }

    /**
	 * 
	 * Description: Action-取得一条大物品入登记记录
	 * 
	 * @Version1.0 Apr 3, 2009 10:35:10 PM 李萌（neolimeng@gmail.com）创建
	 * @return
	 */
    public String getRegisterLargeItemIn() {
        try {
            HttpSession session = ServletActionContext.getRequest().getSession();
            String recorderId = (String) session.getAttribute("systemuserid");
            log.info("DMS_info_用户操作，操作者ID为" + recorderId);
            int registerLargeItemId = Integer.parseInt(ServletActionContext.getRequest().getParameter("registerlargeitemid"));
            rli = this.registerLargeItemService.getRegisterLargeItem(registerLargeItemId);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return INPUT;
        }
    }

    public String setRegisterLargeItemInToOut() {
        try {
            int rliid = Integer.parseInt(ServletActionContext.getRequest().getParameter("registerlargeitemid"));
            this.registerLargeItemService.setRegisterLargeItemInToOut(rliid);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return INPUT;
        }
    }

    public String setRegisterLargeItemOutToIn() {
        try {
            int rliid = Integer.parseInt(ServletActionContext.getRequest().getParameter("registerlargeitemid"));
            this.registerLargeItemService.setRegisterLargeItemOutToIn(rliid);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return INPUT;
        }
    }

    /**
	 * 
	 * Description: Action-取得所有大物品出入登记记录
	 * 
	 * @Version1.0 Apr 3, 2009 10:35:30 PM 李萌（neolimeng@gmail.com）创建
	 * @return
	 */
    public String getAllRegisterLargeItemIn() {
        try {
            HttpSession session = ServletActionContext.getRequest().getSession();
            String recorderId = (String) session.getAttribute("systemuserid");
            log.info("DMS_info_用户操作，操作者ID为" + recorderId);
            rlil = this.registerLargeItemService.getAllRegisterLargeItemIn();
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return INPUT;
        }
    }

    public String getAllRegisterLargeItem() {
        try {
            HttpSession session = ServletActionContext.getRequest().getSession();
            String recorderId = (String) session.getAttribute("systemuserid");
            log.info("DMS_info_用户操作，操作者ID为" + recorderId);
            rlil = this.registerLargeItemService.getAllRegisterLargeItem();
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return INPUT;
        }
    }

    /**
	 * 
	 * Description: Action-插入一条大物品出入登记记录
	 * 
	 * @Version1.0 Apr 3, 2009 10:25:23 PM 李萌（neolimeng@gmail.com）创建
	 * @return
	 */
    public String insertReigsterLargeItemOut() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        String recorderId = (String) session.getAttribute("systemuserid");
        String recorderName = (String) session.getAttribute("systemusername");
        log.info("DMS_info_用户操作，操作者ID为" + recorderId);
        rli.setRegisterlargeitemind(1);
        rli.setRegisterlargeitemouttime(new Date());
        rli.setRecordby(recorderName);
        rli.setInorout(1);
        if (this.registerLargeItemService.insertRegisterLargeItem(rli)) return SUCCESS; else return INPUT;
    }

    /**
	 * 
	 * Description: Action-更新一条大物品出入登记记录
	 * 
	 * @Version1.0 Apr 3, 2009 10:26:53 PM 李萌（neolimeng@gmail.com）创建
	 * @return
	 */
    public String updateRegisterLargeItemOut() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        String recorderId = (String) session.getAttribute("systemuserid");
        String recorderName = (String) session.getAttribute("systemusername");
        log.info("DMS_info_用户操作，操作者ID为" + recorderId);
        rli.setRegisterlargeitemind(1);
        rli.setRegisterlargeitemintime(new Date());
        rli.setRecordby(recorderName);
        rli.setInorout(1);
        if (this.registerLargeItemService.updateRegisterLargeItem(rli)) return SUCCESS; else return INPUT;
    }

    /**
	 * 
	 * Description: Action-删除一条大物品出入登记记录
	 * 
	 * @Version1.0 Apr 3, 2009 10:30:19 PM 李萌（neolimeng@gmail.com）创建
	 * @return
	 */
    public String deleteRegisterLargeItemOut() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        String recorderId = (String) session.getAttribute("systemuserid");
        log.info("DMS_info_用户操作，操作者ID为" + recorderId);
        int rliid = Integer.parseInt(ServletActionContext.getRequest().getParameter("registerlargeitemid"));
        if (this.registerLargeItemService.deleteRegisterLargeItem(rliid)) return SUCCESS; else return INPUT;
    }

    /**
	 * 
	 * Description: Action-取得一条大物品出入登记记录
	 * 
	 * @Version1.0 Apr 3, 2009 10:35:10 PM 李萌（neolimeng@gmail.com）创建
	 * @return
	 */
    public String getRegisterLargeItemOut() {
        try {
            HttpSession session = ServletActionContext.getRequest().getSession();
            String recorderId = (String) session.getAttribute("systemuserid");
            log.info("DMS_info_用户操作，操作者ID为" + recorderId);
            int registerLargeItemId = Integer.parseInt(ServletActionContext.getRequest().getParameter("registerlargeitemid"));
            rli = this.registerLargeItemService.getRegisterLargeItem(registerLargeItemId);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return INPUT;
        }
    }

    /**
	 * 
	 * Description: Action-取得所有大物品出入登记记录
	 * 
	 * @Version1.0 Apr 3, 2009 10:35:30 PM 李萌（neolimeng@gmail.com）创建
	 * @return
	 */
    public String getAllRegisterLargeItemOut() {
        try {
            HttpSession session = ServletActionContext.getRequest().getSession();
            String recorderId = (String) session.getAttribute("systemuserid");
            log.info("DMS_info_用户操作，操作者ID为" + recorderId);
            rlil = this.registerLargeItemService.getAllRegisterLargeItemOut();
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return INPUT;
        }
    }

    /**
	 * @return the registerLargeItemService
	 */
    public RegisterLargeItemService getRegisterLargeItemService() {
        return registerLargeItemService;
    }

    /**
	 * @param registerLargeItemService
	 *            the registerLargeItemService to set
	 */
    public void setRegisterLargeItemService(RegisterLargeItemService registerLargeItemService) {
        this.registerLargeItemService = registerLargeItemService;
    }

    /**
	 * @return the rli
	 */
    public RegisterLargeItem getRli() {
        return rli;
    }

    /**
	 * @param rli
	 *            the rli to set
	 */
    public void setRli(RegisterLargeItem rli) {
        this.rli = rli;
    }

    /**
	 * @return the rlil
	 */
    public List<RegisterLargeItem> getRlil() {
        return rlil;
    }

    /**
	 * @param rlil
	 *            the rlil to set
	 */
    public void setRlil(List<RegisterLargeItem> rlil) {
        this.rlil = rlil;
    }

    public PagerService getPagerService() {
        return pagerService;
    }

    public void setPagerService(PagerService pagerService) {
        this.pagerService = pagerService;
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(String totalRows) {
        this.totalRows = totalRows;
    }

    public String getPagerMethod() {
        return pagerMethod;
    }

    public void setPagerMethod(String pagerMethod) {
        this.pagerMethod = pagerMethod;
    }
}
