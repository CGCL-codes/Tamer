package kr.godsoft.egovframe.generatorwebapp.mgv_all_view_triggers.web;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.mgv_all_view_triggers.service.MgvAllViewTriggersService;
import kr.godsoft.egovframe.generatorwebapp.mgv_all_view_triggers.service.MgvAllViewTriggersDefaultVO;
import kr.godsoft.egovframe.generatorwebapp.mgv_all_view_triggers.service.MgvAllViewTriggersVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : MgvAllViewTriggersController.java
 * @Description : MgvAllViewTriggers Controller class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Controller
public class MgvAllViewTriggersController {

    @Resource(name = "mgvAllViewTriggersService")
    private MgvAllViewTriggersService mgvAllViewTriggersService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * MGV_ALL_VIEW_TRIGGERS 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 MgvAllViewTriggersVO
	 * @return "/mgvAllViewTriggers/MgvAllViewTriggersList"
	 * @exception Exception
	 */
    @RequestMapping(value = "/mgvAllViewTriggers/MgvAllViewTriggersList.do")
    public String selectMgvAllViewTriggersList(@ModelAttribute("searchVO") MgvAllViewTriggersVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List<EgovMap> mgvAllViewTriggersList = mgvAllViewTriggersService.selectMgvAllViewTriggersList(searchVO);
        model.addAttribute("resultList", mgvAllViewTriggersList);
        int totCnt = mgvAllViewTriggersService.selectMgvAllViewTriggersListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "/mgvAllViewTriggers/MgvAllViewTriggersList";
    }

    @RequestMapping("/mgvAllViewTriggers/addMgvAllViewTriggersView.do")
    public String addMgvAllViewTriggersView(@ModelAttribute("searchVO") MgvAllViewTriggersDefaultVO searchVO, Model model) throws Exception {
        model.addAttribute("mgvAllViewTriggersVO", new MgvAllViewTriggersVO());
        return "/mgvAllViewTriggers/MgvAllViewTriggersRegister";
    }

    @RequestMapping("/mgvAllViewTriggers/addMgvAllViewTriggers.do")
    public String addMgvAllViewTriggers(MgvAllViewTriggersVO mgvAllViewTriggersVO, @ModelAttribute("searchVO") MgvAllViewTriggersDefaultVO searchVO, SessionStatus status) throws Exception {
        mgvAllViewTriggersService.insertMgvAllViewTriggers(mgvAllViewTriggersVO);
        status.setComplete();
        return "forward:/mgvAllViewTriggers/MgvAllViewTriggersList.do";
    }

    @RequestMapping("/mgvAllViewTriggers/updateMgvAllViewTriggersView.do")
    public String updateMgvAllViewTriggersView(@ModelAttribute("searchVO") MgvAllViewTriggersDefaultVO searchVO, Model model) throws Exception {
        MgvAllViewTriggersVO mgvAllViewTriggersVO = new MgvAllViewTriggersVO();
        ;
        model.addAttribute(selectMgvAllViewTriggers(mgvAllViewTriggersVO, searchVO));
        return "/mgvAllViewTriggers/MgvAllViewTriggersRegister";
    }

    @RequestMapping("/mgvAllViewTriggers/selectMgvAllViewTriggers.do")
    @ModelAttribute("mgvAllViewTriggersVO")
    public MgvAllViewTriggersVO selectMgvAllViewTriggers(MgvAllViewTriggersVO mgvAllViewTriggersVO, @ModelAttribute("searchVO") MgvAllViewTriggersDefaultVO searchVO) throws Exception {
        return mgvAllViewTriggersService.selectMgvAllViewTriggers(mgvAllViewTriggersVO);
    }

    @RequestMapping("/mgvAllViewTriggers/updateMgvAllViewTriggers.do")
    public String updateMgvAllViewTriggers(MgvAllViewTriggersVO mgvAllViewTriggersVO, @ModelAttribute("searchVO") MgvAllViewTriggersDefaultVO searchVO, SessionStatus status) throws Exception {
        mgvAllViewTriggersService.updateMgvAllViewTriggers(mgvAllViewTriggersVO);
        status.setComplete();
        return "forward:/mgvAllViewTriggers/MgvAllViewTriggersList.do";
    }

    @RequestMapping("/mgvAllViewTriggers/deleteMgvAllViewTriggers.do")
    public String deleteMgvAllViewTriggers(MgvAllViewTriggersVO mgvAllViewTriggersVO, @ModelAttribute("searchVO") MgvAllViewTriggersDefaultVO searchVO, SessionStatus status) throws Exception {
        mgvAllViewTriggersService.deleteMgvAllViewTriggers(mgvAllViewTriggersVO);
        status.setComplete();
        return "forward:/mgvAllViewTriggers/MgvAllViewTriggersList.do";
    }
}
