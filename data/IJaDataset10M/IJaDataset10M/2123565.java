package com.air.admin.controller.imp;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.air.admin.controller.IComponentManagerController;
import com.air.admin.mo.AdminComponent;
import com.air.admin.service.IAdminComponentService;
import com.air.admin.vo.DataGridRequestVO;
import com.air.common.controller.imp.BaseControllerImp;
import com.air.common.util.BasicAppConstant;
import com.air.common.util.QueryCondition;
import com.air.common.util.QueryExpression;
import com.air.common.vo.PageResultListVO;
import com.air.common.vo.ResponseVO;

@Controller
@RequestMapping("/admin/componentManager")
public class ComponentManagerControllerImp extends BaseControllerImp implements IComponentManagerController {

    @Autowired
    IAdminComponentService adminComponentService;

    @Override
    @RequestMapping("add")
    public ModelAndView addComponent(HttpServletRequest request, HttpServletResponse response, AdminComponent component) throws Exception {
        component = adminComponentService.insert(component);
        ResponseVO responseVO = new ResponseVO();
        responseVO.addObject("component", component);
        return parse2Mav(request, responseVO);
    }

    @Override
    @RequestMapping("list")
    public ModelAndView listComponent(HttpServletRequest request, HttpServletResponse response, DataGridRequestVO queryRequestVO) throws Exception {
        QueryCondition condition = new QueryCondition();
        if (queryRequestVO.getSort() != null) {
            condition.addOrderExpression(queryRequestVO.getSort(), queryRequestVO.getOrder());
        }
        PageResultListVO pageResult = adminComponentService.queryByCondition(condition, queryRequestVO.getPage() - 1, queryRequestVO.getRows());
        ModelAndView mav = new ModelAndView();
        mav.addObject(BasicAppConstant._KEY_RESPONSE_VO, JSONObject.fromObject(pageResult).toString());
        mav.setViewName(BasicAppConstant._VIEW_COMMON_JSON);
        return mav;
    }

    @Override
    @RequestMapping("modify")
    public ModelAndView modifyComponent(HttpServletRequest request, HttpServletResponse response, AdminComponent component) throws Exception {
        this.adminComponentService.updateById(component);
        ResponseVO responseVO = new ResponseVO();
        responseVO.addObject("component", component);
        return parse2Mav(request, responseVO);
    }

    @Override
    @RequestMapping("remove")
    public ModelAndView removeComponents(HttpServletRequest request, HttpServletResponse response, String idList) throws Exception {
        List<String> componentIdList = Arrays.asList(idList.split(","));
        QueryCondition condition = new QueryCondition();
        condition.addQueryCondition("id", componentIdList, QueryExpression.IN);
        this.adminComponentService.deleteByCondition(condition);
        ResponseVO responseVO = new ResponseVO();
        return parse2Mav(request, responseVO);
    }
}
