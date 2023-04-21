package com.javaeye.lonlysky.lforum.web.admin.forum;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import com.javaeye.lonlysky.lforum.AdminBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.service.admin.AdminForumManager;

/**
 * 删除版块
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class Forum_delforumsAction extends AdminBaseAction {

    private static final long serialVersionUID = 5231351066559996775L;

    @Autowired
    private AdminForumManager adminForumManager;

    @Override
    public String execute() throws Exception {
        if (!LForumRequest.isPost()) {
            if (adminForumManager.deleteForumsByFid(LForumRequest.getParamIntValue("fid", 0))) {
                adminVistLogManager.insertLog(user, username, usergroup, grouptitle, ip, "删除论坛版块", "删除论坛版块,fid为:" + LForumRequest.getParamIntValue("fid", 0));
                registerStartupScript("", "<script>window.location.href='forum_forumstree.action';</script>");
            } else {
                registerStartupScript("", "<script>alert('对不起,当前节点下面还有子结点,因此不能删除！');window.location.href='forum_forumstree.action';</script>");
            }
        }
        return SUCCESS;
    }
}
