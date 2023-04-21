package kr.godsoft.egovframe.generatorwebapp.comtcadministcoderecptnlog.web;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtcadministcoderecptnlog.service.ComtcadministcoderecptnlogService;
import kr.godsoft.egovframe.generatorwebapp.comtcadministcoderecptnlog.service.ComtcadministcoderecptnlogDefaultVO;
import kr.godsoft.egovframe.generatorwebapp.comtcadministcoderecptnlog.service.ComtcadministcoderecptnlogVO;
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
 * @Class Name : ComtcadministcoderecptnlogController.java
 * @Description : Comtcadministcoderecptnlog Controller class
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
public class ComtcadministcoderecptnlogController {

    @Resource(name = "comtcadministcoderecptnlogService")
    private ComtcadministcoderecptnlogService comtcadministcoderecptnlogService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * COMTCADMINISTCODERECPTNLOG 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 ComtcadministcoderecptnlogVO
	 * @return "/comtcadministcoderecptnlog/ComtcadministcoderecptnlogList"
	 * @exception Exception
	 */
    @RequestMapping(value = "/comtcadministcoderecptnlog/ComtcadministcoderecptnlogList.do")
    public String selectComtcadministcoderecptnlogList(@ModelAttribute("searchVO") ComtcadministcoderecptnlogVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List<EgovMap> comtcadministcoderecptnlogList = comtcadministcoderecptnlogService.selectComtcadministcoderecptnlogList(searchVO);
        model.addAttribute("resultList", comtcadministcoderecptnlogList);
        int totCnt = comtcadministcoderecptnlogService.selectComtcadministcoderecptnlogListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "/comtcadministcoderecptnlog/ComtcadministcoderecptnlogList";
    }

    @RequestMapping("/comtcadministcoderecptnlog/addComtcadministcoderecptnlogView.do")
    public String addComtcadministcoderecptnlogView(@ModelAttribute("searchVO") ComtcadministcoderecptnlogDefaultVO searchVO, Model model) throws Exception {
        model.addAttribute("comtcadministcoderecptnlogVO", new ComtcadministcoderecptnlogVO());
        return "/comtcadministcoderecptnlog/ComtcadministcoderecptnlogRegister";
    }

    @RequestMapping("/comtcadministcoderecptnlog/addComtcadministcoderecptnlog.do")
    public String addComtcadministcoderecptnlog(ComtcadministcoderecptnlogVO comtcadministcoderecptnlogVO, @ModelAttribute("searchVO") ComtcadministcoderecptnlogDefaultVO searchVO, SessionStatus status) throws Exception {
        comtcadministcoderecptnlogService.insertComtcadministcoderecptnlog(comtcadministcoderecptnlogVO);
        status.setComplete();
        return "forward:/comtcadministcoderecptnlog/ComtcadministcoderecptnlogList.do";
    }

    @RequestMapping("/comtcadministcoderecptnlog/updateComtcadministcoderecptnlogView.do")
    public String updateComtcadministcoderecptnlogView(@RequestParam("occrrncDe") String occrrncDe, @RequestParam("administZoneSe") String administZoneSe, @RequestParam("administZoneCode") String administZoneCode, @RequestParam("opertSn") String opertSn, @ModelAttribute("searchVO") ComtcadministcoderecptnlogDefaultVO searchVO, Model model) throws Exception {
        ComtcadministcoderecptnlogVO comtcadministcoderecptnlogVO = new ComtcadministcoderecptnlogVO();
        comtcadministcoderecptnlogVO.setOccrrncDe(occrrncDe);
        comtcadministcoderecptnlogVO.setAdministZoneSe(administZoneSe);
        comtcadministcoderecptnlogVO.setAdministZoneCode(administZoneCode);
        comtcadministcoderecptnlogVO.setOpertSn(opertSn);
        ;
        model.addAttribute(selectComtcadministcoderecptnlog(comtcadministcoderecptnlogVO, searchVO));
        return "/comtcadministcoderecptnlog/ComtcadministcoderecptnlogRegister";
    }

    @RequestMapping("/comtcadministcoderecptnlog/selectComtcadministcoderecptnlog.do")
    @ModelAttribute("comtcadministcoderecptnlogVO")
    public ComtcadministcoderecptnlogVO selectComtcadministcoderecptnlog(ComtcadministcoderecptnlogVO comtcadministcoderecptnlogVO, @ModelAttribute("searchVO") ComtcadministcoderecptnlogDefaultVO searchVO) throws Exception {
        return comtcadministcoderecptnlogService.selectComtcadministcoderecptnlog(comtcadministcoderecptnlogVO);
    }

    @RequestMapping("/comtcadministcoderecptnlog/updateComtcadministcoderecptnlog.do")
    public String updateComtcadministcoderecptnlog(ComtcadministcoderecptnlogVO comtcadministcoderecptnlogVO, @ModelAttribute("searchVO") ComtcadministcoderecptnlogDefaultVO searchVO, SessionStatus status) throws Exception {
        comtcadministcoderecptnlogService.updateComtcadministcoderecptnlog(comtcadministcoderecptnlogVO);
        status.setComplete();
        return "forward:/comtcadministcoderecptnlog/ComtcadministcoderecptnlogList.do";
    }

    @RequestMapping("/comtcadministcoderecptnlog/deleteComtcadministcoderecptnlog.do")
    public String deleteComtcadministcoderecptnlog(ComtcadministcoderecptnlogVO comtcadministcoderecptnlogVO, @ModelAttribute("searchVO") ComtcadministcoderecptnlogDefaultVO searchVO, SessionStatus status) throws Exception {
        comtcadministcoderecptnlogService.deleteComtcadministcoderecptnlog(comtcadministcoderecptnlogVO);
        status.setComplete();
        return "forward:/comtcadministcoderecptnlog/ComtcadministcoderecptnlogList.do";
    }
}
