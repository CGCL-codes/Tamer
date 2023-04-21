package kr.godsoft.egovframe.generatorwebapp.comtnwikibkmk.web;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtnwikibkmk.service.ComtnwikibkmkService;
import kr.godsoft.egovframe.generatorwebapp.comtnwikibkmk.service.ComtnwikibkmkDefaultVO;
import kr.godsoft.egovframe.generatorwebapp.comtnwikibkmk.service.ComtnwikibkmkVO;
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
 * @Class Name : ComtnwikibkmkController.java
 * @Description : Comtnwikibkmk Controller class
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
public class ComtnwikibkmkController {

    @Resource(name = "comtnwikibkmkService")
    private ComtnwikibkmkService comtnwikibkmkService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * COMTNWIKIBKMK 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 ComtnwikibkmkVO
	 * @return "/comtnwikibkmk/ComtnwikibkmkList"
	 * @exception Exception
	 */
    @RequestMapping(value = "/comtnwikibkmk/ComtnwikibkmkList.do")
    public String selectComtnwikibkmkList(@ModelAttribute("searchVO") ComtnwikibkmkVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List<EgovMap> comtnwikibkmkList = comtnwikibkmkService.selectComtnwikibkmkList(searchVO);
        model.addAttribute("resultList", comtnwikibkmkList);
        int totCnt = comtnwikibkmkService.selectComtnwikibkmkListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "/comtnwikibkmk/ComtnwikibkmkList";
    }

    @RequestMapping("/comtnwikibkmk/addComtnwikibkmkView.do")
    public String addComtnwikibkmkView(@ModelAttribute("searchVO") ComtnwikibkmkDefaultVO searchVO, Model model) throws Exception {
        model.addAttribute("comtnwikibkmkVO", new ComtnwikibkmkVO());
        return "/comtnwikibkmk/ComtnwikibkmkRegister";
    }

    @RequestMapping("/comtnwikibkmk/addComtnwikibkmk.do")
    public String addComtnwikibkmk(ComtnwikibkmkVO comtnwikibkmkVO, @ModelAttribute("searchVO") ComtnwikibkmkDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnwikibkmkService.insertComtnwikibkmk(comtnwikibkmkVO);
        status.setComplete();
        return "forward:/comtnwikibkmk/ComtnwikibkmkList.do";
    }

    @RequestMapping("/comtnwikibkmk/updateComtnwikibkmkView.do")
    public String updateComtnwikibkmkView(@RequestParam("wikiBkmkId") String wikiBkmkId, @RequestParam("userId") String userId, @ModelAttribute("searchVO") ComtnwikibkmkDefaultVO searchVO, Model model) throws Exception {
        ComtnwikibkmkVO comtnwikibkmkVO = new ComtnwikibkmkVO();
        comtnwikibkmkVO.setWikiBkmkId(wikiBkmkId);
        comtnwikibkmkVO.setUserId(userId);
        ;
        model.addAttribute(selectComtnwikibkmk(comtnwikibkmkVO, searchVO));
        return "/comtnwikibkmk/ComtnwikibkmkRegister";
    }

    @RequestMapping("/comtnwikibkmk/selectComtnwikibkmk.do")
    @ModelAttribute("comtnwikibkmkVO")
    public ComtnwikibkmkVO selectComtnwikibkmk(ComtnwikibkmkVO comtnwikibkmkVO, @ModelAttribute("searchVO") ComtnwikibkmkDefaultVO searchVO) throws Exception {
        return comtnwikibkmkService.selectComtnwikibkmk(comtnwikibkmkVO);
    }

    @RequestMapping("/comtnwikibkmk/updateComtnwikibkmk.do")
    public String updateComtnwikibkmk(ComtnwikibkmkVO comtnwikibkmkVO, @ModelAttribute("searchVO") ComtnwikibkmkDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnwikibkmkService.updateComtnwikibkmk(comtnwikibkmkVO);
        status.setComplete();
        return "forward:/comtnwikibkmk/ComtnwikibkmkList.do";
    }

    @RequestMapping("/comtnwikibkmk/deleteComtnwikibkmk.do")
    public String deleteComtnwikibkmk(ComtnwikibkmkVO comtnwikibkmkVO, @ModelAttribute("searchVO") ComtnwikibkmkDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnwikibkmkService.deleteComtnwikibkmk(comtnwikibkmkVO);
        status.setComplete();
        return "forward:/comtnwikibkmk/ComtnwikibkmkList.do";
    }
}
