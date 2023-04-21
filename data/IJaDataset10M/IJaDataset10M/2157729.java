package kr.godsoft.egovframe.generatorwebapp.comthhttpmonloginfo.web;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comthhttpmonloginfo.service.ComthhttpmonloginfoService;
import kr.godsoft.egovframe.generatorwebapp.comthhttpmonloginfo.service.ComthhttpmonloginfoDefaultVO;
import kr.godsoft.egovframe.generatorwebapp.comthhttpmonloginfo.service.ComthhttpmonloginfoVO;
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
 * @Class Name : ComthhttpmonloginfoController.java
 * @Description : Comthhttpmonloginfo Controller class
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
public class ComthhttpmonloginfoController {

    @Resource(name = "comthhttpmonloginfoService")
    private ComthhttpmonloginfoService comthhttpmonloginfoService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * COMTHHTTPMONLOGINFO 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 ComthhttpmonloginfoVO
	 * @return "/comthhttpmonloginfo/ComthhttpmonloginfoList"
	 * @exception Exception
	 */
    @RequestMapping(value = "/comthhttpmonloginfo/ComthhttpmonloginfoList.do")
    public String selectComthhttpmonloginfoList(@ModelAttribute("searchVO") ComthhttpmonloginfoVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List<EgovMap> comthhttpmonloginfoList = comthhttpmonloginfoService.selectComthhttpmonloginfoList(searchVO);
        model.addAttribute("resultList", comthhttpmonloginfoList);
        int totCnt = comthhttpmonloginfoService.selectComthhttpmonloginfoListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "/comthhttpmonloginfo/ComthhttpmonloginfoList";
    }

    @RequestMapping("/comthhttpmonloginfo/addComthhttpmonloginfoView.do")
    public String addComthhttpmonloginfoView(@ModelAttribute("searchVO") ComthhttpmonloginfoDefaultVO searchVO, Model model) throws Exception {
        model.addAttribute("comthhttpmonloginfoVO", new ComthhttpmonloginfoVO());
        return "/comthhttpmonloginfo/ComthhttpmonloginfoRegister";
    }

    @RequestMapping("/comthhttpmonloginfo/addComthhttpmonloginfo.do")
    public String addComthhttpmonloginfo(ComthhttpmonloginfoVO comthhttpmonloginfoVO, @ModelAttribute("searchVO") ComthhttpmonloginfoDefaultVO searchVO, SessionStatus status) throws Exception {
        comthhttpmonloginfoService.insertComthhttpmonloginfo(comthhttpmonloginfoVO);
        status.setComplete();
        return "forward:/comthhttpmonloginfo/ComthhttpmonloginfoList.do";
    }

    @RequestMapping("/comthhttpmonloginfo/updateComthhttpmonloginfoView.do")
    public String updateComthhttpmonloginfoView(@RequestParam("sysId") String sysId, @RequestParam("logId") String logId, @ModelAttribute("searchVO") ComthhttpmonloginfoDefaultVO searchVO, Model model) throws Exception {
        ComthhttpmonloginfoVO comthhttpmonloginfoVO = new ComthhttpmonloginfoVO();
        comthhttpmonloginfoVO.setSysId(sysId);
        comthhttpmonloginfoVO.setLogId(logId);
        ;
        model.addAttribute(selectComthhttpmonloginfo(comthhttpmonloginfoVO, searchVO));
        return "/comthhttpmonloginfo/ComthhttpmonloginfoRegister";
    }

    @RequestMapping("/comthhttpmonloginfo/selectComthhttpmonloginfo.do")
    @ModelAttribute("comthhttpmonloginfoVO")
    public ComthhttpmonloginfoVO selectComthhttpmonloginfo(ComthhttpmonloginfoVO comthhttpmonloginfoVO, @ModelAttribute("searchVO") ComthhttpmonloginfoDefaultVO searchVO) throws Exception {
        return comthhttpmonloginfoService.selectComthhttpmonloginfo(comthhttpmonloginfoVO);
    }

    @RequestMapping("/comthhttpmonloginfo/updateComthhttpmonloginfo.do")
    public String updateComthhttpmonloginfo(ComthhttpmonloginfoVO comthhttpmonloginfoVO, @ModelAttribute("searchVO") ComthhttpmonloginfoDefaultVO searchVO, SessionStatus status) throws Exception {
        comthhttpmonloginfoService.updateComthhttpmonloginfo(comthhttpmonloginfoVO);
        status.setComplete();
        return "forward:/comthhttpmonloginfo/ComthhttpmonloginfoList.do";
    }

    @RequestMapping("/comthhttpmonloginfo/deleteComthhttpmonloginfo.do")
    public String deleteComthhttpmonloginfo(ComthhttpmonloginfoVO comthhttpmonloginfoVO, @ModelAttribute("searchVO") ComthhttpmonloginfoDefaultVO searchVO, SessionStatus status) throws Exception {
        comthhttpmonloginfoService.deleteComthhttpmonloginfo(comthhttpmonloginfoVO);
        status.setComplete();
        return "forward:/comthhttpmonloginfo/ComthhttpmonloginfoList.do";
    }
}
