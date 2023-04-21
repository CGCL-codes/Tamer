package kr.godsoft.egovframe.generatorwebapp.comtnonlinepolliem.web;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtnonlinepolliem.service.ComtnonlinepolliemService;
import kr.godsoft.egovframe.generatorwebapp.comtnonlinepolliem.service.ComtnonlinepolliemDefaultVO;
import kr.godsoft.egovframe.generatorwebapp.comtnonlinepolliem.service.ComtnonlinepolliemVO;
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
 * @Class Name : ComtnonlinepolliemController.java
 * @Description : Comtnonlinepolliem Controller class
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
public class ComtnonlinepolliemController {

    @Resource(name = "comtnonlinepolliemService")
    private ComtnonlinepolliemService comtnonlinepolliemService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * COMTNONLINEPOLLIEM 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 ComtnonlinepolliemVO
	 * @return "/comtnonlinepolliem/ComtnonlinepolliemList"
	 * @exception Exception
	 */
    @RequestMapping(value = "/comtnonlinepolliem/ComtnonlinepolliemList.do")
    public String selectComtnonlinepolliemList(@ModelAttribute("searchVO") ComtnonlinepolliemVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List<EgovMap> comtnonlinepolliemList = comtnonlinepolliemService.selectComtnonlinepolliemList(searchVO);
        model.addAttribute("resultList", comtnonlinepolliemList);
        int totCnt = comtnonlinepolliemService.selectComtnonlinepolliemListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "/comtnonlinepolliem/ComtnonlinepolliemList";
    }

    @RequestMapping("/comtnonlinepolliem/addComtnonlinepolliemView.do")
    public String addComtnonlinepolliemView(@ModelAttribute("searchVO") ComtnonlinepolliemDefaultVO searchVO, Model model) throws Exception {
        model.addAttribute("comtnonlinepolliemVO", new ComtnonlinepolliemVO());
        return "/comtnonlinepolliem/ComtnonlinepolliemRegister";
    }

    @RequestMapping("/comtnonlinepolliem/addComtnonlinepolliem.do")
    public String addComtnonlinepolliem(ComtnonlinepolliemVO comtnonlinepolliemVO, @ModelAttribute("searchVO") ComtnonlinepolliemDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnonlinepolliemService.insertComtnonlinepolliem(comtnonlinepolliemVO);
        status.setComplete();
        return "forward:/comtnonlinepolliem/ComtnonlinepolliemList.do";
    }

    @RequestMapping("/comtnonlinepolliem/updateComtnonlinepolliemView.do")
    public String updateComtnonlinepolliemView(@RequestParam("pollIemId") String pollIemId, @RequestParam("pollId") String pollId, @ModelAttribute("searchVO") ComtnonlinepolliemDefaultVO searchVO, Model model) throws Exception {
        ComtnonlinepolliemVO comtnonlinepolliemVO = new ComtnonlinepolliemVO();
        comtnonlinepolliemVO.setPollIemId(pollIemId);
        comtnonlinepolliemVO.setPollId(pollId);
        ;
        model.addAttribute(selectComtnonlinepolliem(comtnonlinepolliemVO, searchVO));
        return "/comtnonlinepolliem/ComtnonlinepolliemRegister";
    }

    @RequestMapping("/comtnonlinepolliem/selectComtnonlinepolliem.do")
    @ModelAttribute("comtnonlinepolliemVO")
    public ComtnonlinepolliemVO selectComtnonlinepolliem(ComtnonlinepolliemVO comtnonlinepolliemVO, @ModelAttribute("searchVO") ComtnonlinepolliemDefaultVO searchVO) throws Exception {
        return comtnonlinepolliemService.selectComtnonlinepolliem(comtnonlinepolliemVO);
    }

    @RequestMapping("/comtnonlinepolliem/updateComtnonlinepolliem.do")
    public String updateComtnonlinepolliem(ComtnonlinepolliemVO comtnonlinepolliemVO, @ModelAttribute("searchVO") ComtnonlinepolliemDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnonlinepolliemService.updateComtnonlinepolliem(comtnonlinepolliemVO);
        status.setComplete();
        return "forward:/comtnonlinepolliem/ComtnonlinepolliemList.do";
    }

    @RequestMapping("/comtnonlinepolliem/deleteComtnonlinepolliem.do")
    public String deleteComtnonlinepolliem(ComtnonlinepolliemVO comtnonlinepolliemVO, @ModelAttribute("searchVO") ComtnonlinepolliemDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnonlinepolliemService.deleteComtnonlinepolliem(comtnonlinepolliemVO);
        status.setComplete();
        return "forward:/comtnonlinepolliem/ComtnonlinepolliemList.do";
    }
}
