package com.hk.web.laba.action;

import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.web.pub.action.BaseAction;

/**
 * 喇叭的恢复列表,取消使用
 * 
 * @author akwei
 */
@Deprecated
public class ReListAction extends BaseAction {

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        return "/WEB-INF/page/laba/relist.jsp";
    }
}
