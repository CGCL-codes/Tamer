package kr.godsoft.egovframe.generatorwebapp.comtnannvrsrymanage.web;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtnannvrsrymanage.service.ComtnannvrsrymanageService;
import kr.godsoft.egovframe.generatorwebapp.comtnannvrsrymanage.service.ComtnannvrsrymanageDefaultVO;
import kr.godsoft.egovframe.generatorwebapp.comtnannvrsrymanage.service.ComtnannvrsrymanageVO;
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
 * @Class Name : ComtnannvrsrymanageController.java
 * @Description : Comtnannvrsrymanage Controller class
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
public class ComtnannvrsrymanageController {

    @Resource(name = "comtnannvrsrymanageService")
    private ComtnannvrsrymanageService comtnannvrsrymanageService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * COMTNANNVRSRYMANAGE 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 ComtnannvrsrymanageVO
	 * @return "/comtnannvrsrymanage/ComtnannvrsrymanageList"
	 * @exception Exception
	 */
    @RequestMapping(value = "/comtnannvrsrymanage/ComtnannvrsrymanageList.do")
    public String selectComtnannvrsrymanageList(@ModelAttribute("searchVO") ComtnannvrsrymanageVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List<EgovMap> comtnannvrsrymanageList = comtnannvrsrymanageService.selectComtnannvrsrymanageList(searchVO);
        model.addAttribute("resultList", comtnannvrsrymanageList);
        int totCnt = comtnannvrsrymanageService.selectComtnannvrsrymanageListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "/comtnannvrsrymanage/ComtnannvrsrymanageList";
    }

    @RequestMapping("/comtnannvrsrymanage/addComtnannvrsrymanageView.do")
    public String addComtnannvrsrymanageView(@ModelAttribute("searchVO") ComtnannvrsrymanageDefaultVO searchVO, Model model) throws Exception {
        model.addAttribute("comtnannvrsrymanageVO", new ComtnannvrsrymanageVO());
        return "/comtnannvrsrymanage/ComtnannvrsrymanageRegister";
    }

    @RequestMapping("/comtnannvrsrymanage/addComtnannvrsrymanage.do")
    public String addComtnannvrsrymanage(ComtnannvrsrymanageVO comtnannvrsrymanageVO, @ModelAttribute("searchVO") ComtnannvrsrymanageDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnannvrsrymanageService.insertComtnannvrsrymanage(comtnannvrsrymanageVO);
        status.setComplete();
        return "forward:/comtnannvrsrymanage/ComtnannvrsrymanageList.do";
    }

    @RequestMapping("/comtnannvrsrymanage/updateComtnannvrsrymanageView.do")
    public String updateComtnannvrsrymanageView(@RequestParam("annvrsryId") String annvrsryId, @ModelAttribute("searchVO") ComtnannvrsrymanageDefaultVO searchVO, Model model) throws Exception {
        ComtnannvrsrymanageVO comtnannvrsrymanageVO = new ComtnannvrsrymanageVO();
        comtnannvrsrymanageVO.setAnnvrsryId(annvrsryId);
        ;
        model.addAttribute(selectComtnannvrsrymanage(comtnannvrsrymanageVO, searchVO));
        return "/comtnannvrsrymanage/ComtnannvrsrymanageRegister";
    }

    @RequestMapping("/comtnannvrsrymanage/selectComtnannvrsrymanage.do")
    @ModelAttribute("comtnannvrsrymanageVO")
    public ComtnannvrsrymanageVO selectComtnannvrsrymanage(ComtnannvrsrymanageVO comtnannvrsrymanageVO, @ModelAttribute("searchVO") ComtnannvrsrymanageDefaultVO searchVO) throws Exception {
        return comtnannvrsrymanageService.selectComtnannvrsrymanage(comtnannvrsrymanageVO);
    }

    @RequestMapping("/comtnannvrsrymanage/updateComtnannvrsrymanage.do")
    public String updateComtnannvrsrymanage(ComtnannvrsrymanageVO comtnannvrsrymanageVO, @ModelAttribute("searchVO") ComtnannvrsrymanageDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnannvrsrymanageService.updateComtnannvrsrymanage(comtnannvrsrymanageVO);
        status.setComplete();
        return "forward:/comtnannvrsrymanage/ComtnannvrsrymanageList.do";
    }

    @RequestMapping("/comtnannvrsrymanage/deleteComtnannvrsrymanage.do")
    public String deleteComtnannvrsrymanage(ComtnannvrsrymanageVO comtnannvrsrymanageVO, @ModelAttribute("searchVO") ComtnannvrsrymanageDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnannvrsrymanageService.deleteComtnannvrsrymanage(comtnannvrsrymanageVO);
        status.setComplete();
        return "forward:/comtnannvrsrymanage/ComtnannvrsrymanageList.do";
    }
}
