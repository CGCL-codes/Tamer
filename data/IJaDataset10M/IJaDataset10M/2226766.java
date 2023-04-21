package com.endigi.frame.authority.web;

import java.rmi.ServerException;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.endigi.frame.authority.domian.UserGroupService;
import com.endigi.frame.authority.domian.entity.UserGroup;
import com.endigi.frame.base.genericdao.search.Search;
import com.endigi.frame.base.genericdao.search.SearchResult;
import com.endigi.frame.base.web.BaseController;
import com.endigi.frame.base.web.vo.GenericResponse;
import com.endigi.frame.base.web.vo.JqGridRequest;
import com.endigi.frame.base.web.vo.JqGridResponse;

@Controller
@RequestMapping(value = "/group")
public class GroupCtr extends BaseController {

    private static Logger logger = Logger.getLogger(GroupCtr.class);

    @Resource
    UserGroupService groupService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse saveGroup(UserGroup group) throws ServerException {
        groupService.saveGroup(group);
        return getSuccessResponse("添加用户组成功");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public GenericResponse updateGroup(@RequestBody UserGroup group) throws ServerException {
        groupService.updateGroup(group);
        return getSuccessResponse("更新用户组成功");
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public JqGridResponse searchGroup(JqGridRequest jqGridRequest) throws ServiceException {
        Search search = jqGridRequest.getSearch(UserGroup.class);
        SearchResult<UserGroup> searchResult = groupService.searchGroup(search);
        return jqGridRequest.getJqGridResponse(searchResult);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public GenericResponse deleteGroup(@PathVariable String id) throws ServerException {
        groupService.deleteGroup(id);
        return getSuccessResponse("删除用户组成功");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public GenericResponse getGroupById(@PathVariable String id) throws ServerException {
        return getSuccessResponse(groupService.getGroupById(id));
    }

    @RequestMapping(value = "/json/", method = RequestMethod.GET)
    @ResponseBody
    public List<UserGroup> getAllGroupJson() throws ServiceException {
        Search search = new Search(UserGroup.class);
        return groupService.searchGroupList(search);
    }

    @RequestMapping(value = "/{id}/authority", method = RequestMethod.PUT)
    @ResponseBody
    public GenericResponse updateGroupAuthority(@PathVariable String id, @RequestBody UserGroup group) throws ServerException {
        logger.info("id:" + id);
        UserGroup dbGroup = groupService.getGroupById(id);
        dbGroup.setAuthorities(group.getAuthorities());
        groupService.updateGroup(dbGroup);
        return getSuccessResponse("更新用户组成功");
    }
}
