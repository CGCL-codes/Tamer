package com.isfasiel.main.comment.web;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Provider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.isfasiel.main.comment.service.CommentService;
import com.isfasiel.main.content.web.ContentController;
import com.isfasiel.main.domain.User;
import com.isfasiel.main.user.web.LoginInfo;
import com.isfasiel.util.data.Data;

@Controller
@RequestMapping(value = "/comment")
public class CommentController extends ContentController {

    private int pageSize = 20;

    private String path = "content/xml";

    @Resource(name = "commentService")
    CommentService commentService;

    @RequestMapping(value = "/list/{contentId}/{pageNumber}")
    public String getList(@PathVariable long contentId, @PathVariable int pageNumber, Model model) throws Exception {
        Data param = getPageParam(pageNumber, pageSize);
        param.add(0, "contentId", contentId);
        Data result = commentService.list(param);
        addXML(model, "result", result, "commment");
        param = null;
        return path;
    }

    @RequestMapping(value = "/subList/{contentId}/{commentId}/{pageNumber}")
    public String getSubList(@PathVariable long contentId, @PathVariable long commentId, @PathVariable int pageNumber, Model model) throws Exception {
        Data param = getPageParam(pageNumber, pageSize);
        param.add(0, "contentId", contentId);
        param.add(0, "parentCommentId", commentId);
        Data result = commentService.subList(param);
        addXML(model, "result", result, "comment");
        param = null;
        return path;
    }

    @RequestMapping(value = "/create.do")
    public String createComment(Model model) throws Exception {
        Data result = new Data();
        User user = getUser();
        if (user != null) {
            try {
                Data param = getParam();
                param.add(0, "userIdx", user.getId());
                param.add(0, "ipAddr", getRemoteIP());
                commentService.craete(param);
                result.add(0, "result", "OK");
                param = null;
            } catch (Exception e) {
                e.printStackTrace();
                result.add(0, "result", "NO");
            }
        } else {
            result.add(0, "result", "NO");
        }
        addXML(model, "result", result, "comment");
        return path;
    }

    @RequestMapping(value = "/update.do")
    public String updateComment(Model model) throws Exception {
        Data result = new Data();
        User user = getUser();
        if (user != null) {
            try {
                Data param = getParam();
                param.add(0, "userIdx", user.getId());
                commentService.update(param);
                result.add(0, "result", "OK");
                param = null;
            } catch (Exception e) {
                e.printStackTrace();
                result.add(0, "result", "NO");
            }
        } else {
            result.add(0, "result", "NO");
        }
        addXML(model, "result", result, "comment");
        return path;
    }

    @RequestMapping(value = "/delete.do")
    public String deleteComment(Model model) throws Exception {
        Data result = new Data();
        User user = getUser();
        if (user != null) {
            try {
                Data param = getParam();
                param.add(0, "userIdx", user.getId());
                commentService.delete(param);
                result.add(0, "result", "OK");
                param = null;
            } catch (Exception e) {
                e.printStackTrace();
                result.add(0, "result", "NO");
            }
        } else {
            result.add(0, "result", "NO");
        }
        addXML(model, "result", result, "comment");
        return path;
    }

    @RequestMapping(value = "/test")
    public String test(Model model) throws Exception {
        return "comment/test";
    }
}
