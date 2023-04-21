package com.endigi.frame.base.web;

import java.util.Date;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.endigi.frame.base.domain.FriendLinkService;
import com.endigi.frame.base.domain.entity.FriendLink;
import com.endigi.frame.base.genericdao.search.Search;
import com.endigi.frame.base.genericdao.search.SearchResult;
import com.endigi.frame.base.util.ServiceException;
import com.endigi.frame.base.web.vo.GenericResponse;
import com.endigi.frame.base.web.vo.JqGridRequest;
import com.endigi.frame.base.web.vo.JqGridResponse;

@Controller
@RequestMapping(value = "/friendLink")
public class FriendLinkFacade extends BaseController {

    @Resource
    private FriendLinkService fLinkService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public JqGridResponse searchHistory(JqGridRequest jqGridRequest, Boolean locked) {
        Search search = jqGridRequest.getSearch(FriendLink.class);
        if (locked != null) {
            search.addFilterEqual("locked", locked);
        }
        SearchResult<FriendLink> searchResult = fLinkService.searchFriendLink(search);
        return jqGridRequest.getJqGridResponse(searchResult);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public GenericResponse getFriendLinkById(@PathVariable String id) {
        return this.getSuccessResponse(fLinkService.getFriendLinkById(id));
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse submitFriendLink(FriendLink flink) throws ServiceException {
        flink.setCreateTime(new Date());
        fLinkService.submitFriendLink(flink);
        return getSuccessResponse("提交友情链接成功，请等待管理员审核");
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse saveFriendLink(FriendLink flink) throws ServiceException {
        flink.setCreateTime(new Date());
        fLinkService.submitFriendLink(flink);
        return getSuccessResponse("提交友情链接成功，请等待管理员审核");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public GenericResponse udpateFriendLink(@RequestBody FriendLink flink) throws ServiceException {
        fLinkService.updateFriendLink(flink);
        return getSuccessResponse("更新友情链接成功");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public GenericResponse deleteFriendLink(@PathVariable String id) throws ServiceException {
        fLinkService.deleteFriendLinkById(id);
        return getSuccessResponse("删除友情链接成功");
    }
}
