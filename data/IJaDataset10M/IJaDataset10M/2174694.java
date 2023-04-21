package kr.godsoft.egovframe.generatorwebapp.comtnbndtmanage.web;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtnbndtmanage.service.ComtnbndtmanageService;
import kr.godsoft.egovframe.generatorwebapp.comtnbndtmanage.service.ComtnbndtmanageDefaultVO;
import kr.godsoft.egovframe.generatorwebapp.comtnbndtmanage.service.ComtnbndtmanageVO;
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
 * @Class Name : ComtnbndtmanageController.java
 * @Description : Comtnbndtmanage Controller class
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
public class ComtnbndtmanageController {

    @Resource(name = "comtnbndtmanageService")
    private ComtnbndtmanageService comtnbndtmanageService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * COMTNBNDTMANAGE 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 ComtnbndtmanageVO
	 * @return "/comtnbndtmanage/ComtnbndtmanageList"
	 * @exception Exception
	 */
    @RequestMapping(value = "/comtnbndtmanage/ComtnbndtmanageList.do")
    public String selectComtnbndtmanageList(@ModelAttribute("searchVO") ComtnbndtmanageVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List<EgovMap> comtnbndtmanageList = comtnbndtmanageService.selectComtnbndtmanageList(searchVO);
        model.addAttribute("resultList", comtnbndtmanageList);
        int totCnt = comtnbndtmanageService.selectComtnbndtmanageListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "/comtnbndtmanage/ComtnbndtmanageList";
    }

    @RequestMapping("/comtnbndtmanage/addComtnbndtmanageView.do")
    public String addComtnbndtmanageView(@ModelAttribute("searchVO") ComtnbndtmanageDefaultVO searchVO, Model model) throws Exception {
        model.addAttribute("comtnbndtmanageVO", new ComtnbndtmanageVO());
        return "/comtnbndtmanage/ComtnbndtmanageRegister";
    }

    @RequestMapping("/comtnbndtmanage/addComtnbndtmanage.do")
    public String addComtnbndtmanage(ComtnbndtmanageVO comtnbndtmanageVO, @ModelAttribute("searchVO") ComtnbndtmanageDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnbndtmanageService.insertComtnbndtmanage(comtnbndtmanageVO);
        status.setComplete();
        return "forward:/comtnbndtmanage/ComtnbndtmanageList.do";
    }

    @RequestMapping("/comtnbndtmanage/updateComtnbndtmanageView.do")
    public String updateComtnbndtmanageView(@RequestParam("bndtId") String bndtId, @RequestParam("bndtDe") String bndtDe, @ModelAttribute("searchVO") ComtnbndtmanageDefaultVO searchVO, Model model) throws Exception {
        ComtnbndtmanageVO comtnbndtmanageVO = new ComtnbndtmanageVO();
        comtnbndtmanageVO.setBndtId(bndtId);
        comtnbndtmanageVO.setBndtDe(bndtDe);
        ;
        model.addAttribute(selectComtnbndtmanage(comtnbndtmanageVO, searchVO));
        return "/comtnbndtmanage/ComtnbndtmanageRegister";
    }

    @RequestMapping("/comtnbndtmanage/selectComtnbndtmanage.do")
    @ModelAttribute("comtnbndtmanageVO")
    public ComtnbndtmanageVO selectComtnbndtmanage(ComtnbndtmanageVO comtnbndtmanageVO, @ModelAttribute("searchVO") ComtnbndtmanageDefaultVO searchVO) throws Exception {
        return comtnbndtmanageService.selectComtnbndtmanage(comtnbndtmanageVO);
    }

    @RequestMapping("/comtnbndtmanage/updateComtnbndtmanage.do")
    public String updateComtnbndtmanage(ComtnbndtmanageVO comtnbndtmanageVO, @ModelAttribute("searchVO") ComtnbndtmanageDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnbndtmanageService.updateComtnbndtmanage(comtnbndtmanageVO);
        status.setComplete();
        return "forward:/comtnbndtmanage/ComtnbndtmanageList.do";
    }

    @RequestMapping("/comtnbndtmanage/deleteComtnbndtmanage.do")
    public String deleteComtnbndtmanage(ComtnbndtmanageVO comtnbndtmanageVO, @ModelAttribute("searchVO") ComtnbndtmanageDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnbndtmanageService.deleteComtnbndtmanage(comtnbndtmanageVO);
        status.setComplete();
        return "forward:/comtnbndtmanage/ComtnbndtmanageList.do";
    }
}
