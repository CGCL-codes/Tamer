package com.rome.syncml.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.rome.syncml.bean.CallLogVO;
import com.rome.syncml.domain.User;
import com.rome.syncml.service.CallLogService;
import com.rome.syncml.service.SMSService;

/**
 * 类CallLogController.java的实现描述：TODO 类实现描述 
 * @author afei 2011-11-26 下午02:46:21
 */
@Controller
@RequestMapping("/log")
public class CallLogController {

    @Resource
    private CallLogService callLogService;

    @Resource
    private SMSService smsService;

    private int pageSize = 10;

    @RequestMapping("/list")
    public String list(HttpServletRequest request, @RequestParam(value = "queryString", required = false) String queryString, @RequestParam(value = "currentPage", required = false) Integer currentPage, @RequestParam(value = "type", required = false) Integer type) {
        User user = (User) request.getSession().getAttribute("user");
        long userId = user.getId();
        if (currentPage == null) currentPage = 1;
        if (type == null) type = 0;
        CallLogVO vo = callLogService.queryCallLog(userId, queryString, currentPage, pageSize, type);
        request.setAttribute("vo", vo);
        request.setAttribute("type", type);
        return "log/list";
    }

    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, @RequestParam("msgchk") long[] ids) {
        smsService.delete(ids);
        return list(request, null, 1, 0);
    }
}
