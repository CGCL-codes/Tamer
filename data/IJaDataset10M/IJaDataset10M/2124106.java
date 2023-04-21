package kr.godsoft.egovframe.generatorwebapp.comtndampro.web;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtndampro.service.ComtndamproService;
import kr.godsoft.egovframe.generatorwebapp.comtndampro.service.ComtndamproDefaultVO;
import kr.godsoft.egovframe.generatorwebapp.comtndampro.service.ComtndamproVO;
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
 * @Class Name : ComtndamproController.java
 * @Description : Comtndampro Controller class
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
public class ComtndamproController {

    @Resource(name = "comtndamproService")
    private ComtndamproService comtndamproService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * COMTNDAMPRO 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 ComtndamproVO
	 * @return "/comtndampro/ComtndamproList"
	 * @exception Exception
	 */
    @RequestMapping(value = "/comtndampro/ComtndamproList.do")
    public String selectComtndamproList(@ModelAttribute("searchVO") ComtndamproVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List<EgovMap> comtndamproList = comtndamproService.selectComtndamproList(searchVO);
        model.addAttribute("resultList", comtndamproList);
        int totCnt = comtndamproService.selectComtndamproListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "/comtndampro/ComtndamproList";
    }

    @RequestMapping("/comtndampro/addComtndamproView.do")
    public String addComtndamproView(@ModelAttribute("searchVO") ComtndamproDefaultVO searchVO, Model model) throws Exception {
        model.addAttribute("comtndamproVO", new ComtndamproVO());
        return "/comtndampro/ComtndamproRegister";
    }

    @RequestMapping("/comtndampro/addComtndampro.do")
    public String addComtndampro(ComtndamproVO comtndamproVO, @ModelAttribute("searchVO") ComtndamproDefaultVO searchVO, SessionStatus status) throws Exception {
        comtndamproService.insertComtndampro(comtndamproVO);
        status.setComplete();
        return "forward:/comtndampro/ComtndamproList.do";
    }

    @RequestMapping("/comtndampro/updateComtndamproView.do")
    public String updateComtndamproView(@RequestParam("expertId") String expertId, @RequestParam("knwldgTyCode") String knwldgTyCode, @RequestParam("expertGrad") String expertGrad, @ModelAttribute("searchVO") ComtndamproDefaultVO searchVO, Model model) throws Exception {
        ComtndamproVO comtndamproVO = new ComtndamproVO();
        comtndamproVO.setExpertId(expertId);
        comtndamproVO.setKnwldgTyCode(knwldgTyCode);
        comtndamproVO.setExpertGrad(expertGrad);
        ;
        model.addAttribute(selectComtndampro(comtndamproVO, searchVO));
        return "/comtndampro/ComtndamproRegister";
    }

    @RequestMapping("/comtndampro/selectComtndampro.do")
    @ModelAttribute("comtndamproVO")
    public ComtndamproVO selectComtndampro(ComtndamproVO comtndamproVO, @ModelAttribute("searchVO") ComtndamproDefaultVO searchVO) throws Exception {
        return comtndamproService.selectComtndampro(comtndamproVO);
    }

    @RequestMapping("/comtndampro/updateComtndampro.do")
    public String updateComtndampro(ComtndamproVO comtndamproVO, @ModelAttribute("searchVO") ComtndamproDefaultVO searchVO, SessionStatus status) throws Exception {
        comtndamproService.updateComtndampro(comtndamproVO);
        status.setComplete();
        return "forward:/comtndampro/ComtndamproList.do";
    }

    @RequestMapping("/comtndampro/deleteComtndampro.do")
    public String deleteComtndampro(ComtndamproVO comtndamproVO, @ModelAttribute("searchVO") ComtndamproDefaultVO searchVO, SessionStatus status) throws Exception {
        comtndamproService.deleteComtndampro(comtndamproVO);
        status.setComplete();
        return "forward:/comtndampro/ComtndamproList.do";
    }
}
