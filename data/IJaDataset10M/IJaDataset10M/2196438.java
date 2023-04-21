package com.laoer.bbscs.web.action;

import java.util.ArrayList;
import java.util.List;
import com.laoer.bbscs.bean.Forum;
import com.laoer.bbscs.comm.BBSCSUtil;
import com.laoer.bbscs.comm.Constant;
import com.laoer.bbscs.exception.BbscsException;
import com.laoer.bbscs.service.ForumService;
import com.laoer.bbscs.service.web.PageList;
import com.laoer.bbscs.service.web.Pages;
import com.laoer.bbscs.web.interceptor.RemoteAddrAware;
import com.laoer.bbscs.web.interceptor.RequestBasePathAware;

public class ForumManage extends BaseBoardAction implements RequestBasePathAware, RemoteAddrAware {

    /**
	 *
	 */
    private static final long serialVersionUID = -268162634056559747L;

    private String basePath;

    private String remoteAddr;

    private ForumService forumService;

    private PageList pageList;

    private List<String> ids;

    public PageList getPageList() {
        return pageList;
    }

    public void setPageList(PageList pageList) {
        this.pageList = pageList;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public ForumService getForumService() {
        return forumService;
    }

    public void setForumService(ForumService forumService) {
        this.forumService = forumService;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    private String forwardUrl;

    public String getForwardUrl() {
        return forwardUrl;
    }

    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    public String m() {
        Pages pages = new Pages();
        pages.setPage(this.getPage());
        pages.setPerPageNum(40);
        pages.setFileName(BBSCSUtil.getActionMappingURLWithoutPrefix("forumManage?action=" + this.getAction() + "&bid=" + this.getBid()));
        if (this.getTotal() > 0) {
            pages.setTotalNum(this.getTotal());
        }
        this.setPageList(this.getForumService().findForumsAllManage(this.getBid(), pages));
        return "forumManage";
    }

    public String a() {
        Pages pages = new Pages();
        pages.setPage(this.getPage());
        pages.setPerPageNum(40);
        pages.setFileName(BBSCSUtil.getActionMappingURLWithoutPrefix("forumManage?action=" + this.getAction() + "&bid=" + this.getBid()));
        if (this.getTotal() > 0) {
            pages.setTotalNum(this.getTotal());
        }
        this.setPageList(this.getForumService().findForumsAuditing(this.getBid(), pages));
        return "forumAuditing";
    }

    public String aa() {
        Pages pages = new Pages();
        pages.setPage(this.getPage());
        pages.setPerPageNum(40);
        pages.setFileName(BBSCSUtil.getActionMappingURLWithoutPrefix("forumManage?action=" + this.getAction() + "&bid=" + this.getBid()));
        if (this.getTotal() > 0) {
            pages.setTotalNum(this.getTotal());
        }
        this.setPageList(this.getForumService().findForumsAuditingAttachFile(this.getBid(), pages));
        return "forumAuditing";
    }

    public String dels() {
        if (this.getIds() == null || this.getIds().isEmpty()) {
            this.addActionError(this.getText("error.select"));
            return ERROR;
        }
        List l = this.getForumService().findForumsInIds(this.getIds());
        List<Forum> fl = new ArrayList<Forum>();
        for (int i = 0; i < l.size(); i++) {
            Forum f = (Forum) l.get(i);
            f.setDelIP(this.getRemoteAddr());
            f.setDelSign(1);
            f.setDelTime(System.currentTimeMillis());
            f.setDelUserID(this.getUserSession().getId());
            f.setDelUserName(this.getUserSession().getUserName());
            if (f.getIndexStatus() == Constant.INDEX_STATUS_NO_INDEX) {
                f.setIndexStatus(Constant.INDEX_STATUS_NO_INDEX_TO_DEL);
            } else {
                f.setIndexStatus(Constant.INDEX_STATUS_NEED_DEL);
            }
            fl.add(f);
        }
        try {
            this.getForumService().delPosts(fl, this.getBoard());
            this.setForwardUrl(this.getBasePath() + BBSCSUtil.getActionMappingURLWithoutPrefix("forumManage?action=m&bid=" + this.getBid() + "&page=" + this.getPage()));
            return SUCCESS;
        } catch (BbscsException ex) {
            this.addActionError(this.getText("error.forummanage.dels"));
            return ERROR;
        }
    }

    public String w() {
        Pages pages = new Pages();
        pages.setPage(this.getPage());
        pages.setPerPageNum(40);
        pages.setFileName(BBSCSUtil.getActionMappingURLWithoutPrefix("forumManage?action=" + this.getAction() + "&bid=" + this.getBid()));
        if (this.getTotal() > 0) {
            pages.setTotalNum(this.getTotal());
        }
        this.setPageList(this.getForumService().findForumsDel(this.getBid(), pages));
        return "forumWaste";
    }

    public String delw() {
        if (this.getIds() == null || this.getIds().isEmpty()) {
            this.addActionError(this.getText("error.select"));
            return ERROR;
        }
        try {
            this.getForumService().delPostsReal(this.getIds());
            this.setForwardUrl(this.getBasePath() + BBSCSUtil.getActionMappingURLWithoutPrefix("forumManage?action=w&bid=" + this.getBid() + "&page=" + this.getPage()));
            return SUCCESS;
        } catch (BbscsException ex1) {
            this.addActionError(this.getText("error.forummanage.dels"));
            return ERROR;
        }
    }

    public String delallw() {
        try {
            this.getForumService().delWastePost(this.getBid());
            this.setForwardUrl(this.getBasePath() + BBSCSUtil.getActionMappingURLWithoutPrefix("forumManage?action=w&bid=" + this.getBid() + "&page=1"));
            return SUCCESS;
        } catch (BbscsException ex2) {
            this.addActionError(this.getText("error.forummanage.dels"));
            return ERROR;
        }
    }

    public String resume() {
        if (this.getIds() == null || this.getIds().isEmpty()) {
            this.addActionError(this.getText("error.select"));
            return ERROR;
        }
        try {
            this.getForumService().saveForumsResume(this.getIds(), this.getBoard());
            this.setForwardUrl(this.getBasePath() + BBSCSUtil.getActionMappingURLWithoutPrefix("forumManage?action=w&bid=" + this.getBid() + "&page=" + this.getPage()));
            return SUCCESS;
        } catch (BbscsException ex3) {
            this.addActionError(this.getText("error.forummanage.resume"));
            return ERROR;
        }
    }

    public String auditing() {
        if (this.getIds() == null || this.getIds().isEmpty()) {
            this.addActionError(this.getText("error.select"));
            return ERROR;
        }
        try {
            this.getForumService().saveForumsAuditing(this.getIds(), this.getBoard());
            this.setForwardUrl(this.getBasePath() + BBSCSUtil.getActionMappingURLWithoutPrefix("forumManage?action=a&bid=" + this.getBid() + "&page=" + this.getPage()));
            return SUCCESS;
        } catch (BbscsException ex4) {
            this.addActionError(this.getText("error.forummanage.auditing"));
            return ERROR;
        }
    }

    public String auditingAttach() {
        if (this.getIds() == null || this.getIds().isEmpty()) {
            this.addActionError(this.getText("error.select"));
            return ERROR;
        }
        try {
            this.getForumService().saveForumsAuditingAttachFile(this.getIds());
            this.setForwardUrl(this.getBasePath() + BBSCSUtil.getActionMappingURLWithoutPrefix("forumManage?action=aa&bid=" + this.getBid() + "&page=" + this.getPage()));
            return SUCCESS;
        } catch (BbscsException ex4) {
            this.addActionError(this.getText("error.forummanage.auditing"));
            return ERROR;
        }
    }

    public String delsnota() {
        if (this.getIds() == null || this.getIds().isEmpty()) {
            this.addActionError(this.getText("error.select"));
            return ERROR;
        }
        List l = this.getForumService().findForumsInIds(this.getBid(), this.getIds());
        List<Forum> fl = new ArrayList<Forum>();
        for (int i = 0; i < l.size(); i++) {
            Forum f = (Forum) l.get(i);
            f.setDelIP(this.getRemoteAddr());
            f.setDelSign(1);
            f.setDelTime(System.currentTimeMillis());
            f.setDelUserID(this.getUserSession().getId());
            f.setDelUserName(this.getUserSession().getUserName());
            fl.add(f);
        }
        try {
            this.getForumService().delPosts(fl, this.getBoard());
            this.setForwardUrl(BBSCSUtil.getActionMappingURLWithoutPrefix("/forumManage?action=a&bid=" + this.getBid() + "&page=" + this.getPage()));
            return SUCCESS;
        } catch (BbscsException ex) {
            this.addActionError(this.getText("error.forummanage.dels"));
            return ERROR;
        }
    }

    public String delsnotaa() {
        if (this.getIds() == null || this.getIds().isEmpty()) {
            this.addActionError(this.getText("error.select"));
            return ERROR;
        }
        try {
            this.getForumService().delForumsNotAuditingAttachFile(this.getIds());
            this.setForwardUrl(BBSCSUtil.getActionMappingURLWithoutPrefix("/forumManage?action=aa&bid=" + this.getBid() + "&page=" + this.getPage()));
            return SUCCESS;
        } catch (BbscsException ex4) {
            this.addActionError(this.getText("error.forummanage.dels"));
            return ERROR;
        }
    }
}
