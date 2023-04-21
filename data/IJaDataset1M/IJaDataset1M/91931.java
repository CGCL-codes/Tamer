package kr.godsoft.egovframe.generatorwebapp.mgv_all_stored_programs.web;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.mgv_all_stored_programs.service.MgvAllStoredProgramsService;
import kr.godsoft.egovframe.generatorwebapp.mgv_all_stored_programs.service.MgvAllStoredProgramsDefaultVO;
import kr.godsoft.egovframe.generatorwebapp.mgv_all_stored_programs.service.MgvAllStoredProgramsVO;
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
 * @Class Name : MgvAllStoredProgramsController.java
 * @Description : MgvAllStoredPrograms Controller class
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
public class MgvAllStoredProgramsController {

    @Resource(name = "mgvAllStoredProgramsService")
    private MgvAllStoredProgramsService mgvAllStoredProgramsService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * MGV_ALL_STORED_PROGRAMS 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 MgvAllStoredProgramsVO
	 * @return "/mgvAllStoredPrograms/MgvAllStoredProgramsList"
	 * @exception Exception
	 */
    @RequestMapping(value = "/mgvAllStoredPrograms/MgvAllStoredProgramsList.do")
    public String selectMgvAllStoredProgramsList(@ModelAttribute("searchVO") MgvAllStoredProgramsVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List<EgovMap> mgvAllStoredProgramsList = mgvAllStoredProgramsService.selectMgvAllStoredProgramsList(searchVO);
        model.addAttribute("resultList", mgvAllStoredProgramsList);
        int totCnt = mgvAllStoredProgramsService.selectMgvAllStoredProgramsListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "/mgvAllStoredPrograms/MgvAllStoredProgramsList";
    }

    @RequestMapping("/mgvAllStoredPrograms/addMgvAllStoredProgramsView.do")
    public String addMgvAllStoredProgramsView(@ModelAttribute("searchVO") MgvAllStoredProgramsDefaultVO searchVO, Model model) throws Exception {
        model.addAttribute("mgvAllStoredProgramsVO", new MgvAllStoredProgramsVO());
        return "/mgvAllStoredPrograms/MgvAllStoredProgramsRegister";
    }

    @RequestMapping("/mgvAllStoredPrograms/addMgvAllStoredPrograms.do")
    public String addMgvAllStoredPrograms(MgvAllStoredProgramsVO mgvAllStoredProgramsVO, @ModelAttribute("searchVO") MgvAllStoredProgramsDefaultVO searchVO, SessionStatus status) throws Exception {
        mgvAllStoredProgramsService.insertMgvAllStoredPrograms(mgvAllStoredProgramsVO);
        status.setComplete();
        return "forward:/mgvAllStoredPrograms/MgvAllStoredProgramsList.do";
    }

    @RequestMapping("/mgvAllStoredPrograms/updateMgvAllStoredProgramsView.do")
    public String updateMgvAllStoredProgramsView(@ModelAttribute("searchVO") MgvAllStoredProgramsDefaultVO searchVO, Model model) throws Exception {
        MgvAllStoredProgramsVO mgvAllStoredProgramsVO = new MgvAllStoredProgramsVO();
        ;
        model.addAttribute(selectMgvAllStoredPrograms(mgvAllStoredProgramsVO, searchVO));
        return "/mgvAllStoredPrograms/MgvAllStoredProgramsRegister";
    }

    @RequestMapping("/mgvAllStoredPrograms/selectMgvAllStoredPrograms.do")
    @ModelAttribute("mgvAllStoredProgramsVO")
    public MgvAllStoredProgramsVO selectMgvAllStoredPrograms(MgvAllStoredProgramsVO mgvAllStoredProgramsVO, @ModelAttribute("searchVO") MgvAllStoredProgramsDefaultVO searchVO) throws Exception {
        return mgvAllStoredProgramsService.selectMgvAllStoredPrograms(mgvAllStoredProgramsVO);
    }

    @RequestMapping("/mgvAllStoredPrograms/updateMgvAllStoredPrograms.do")
    public String updateMgvAllStoredPrograms(MgvAllStoredProgramsVO mgvAllStoredProgramsVO, @ModelAttribute("searchVO") MgvAllStoredProgramsDefaultVO searchVO, SessionStatus status) throws Exception {
        mgvAllStoredProgramsService.updateMgvAllStoredPrograms(mgvAllStoredProgramsVO);
        status.setComplete();
        return "forward:/mgvAllStoredPrograms/MgvAllStoredProgramsList.do";
    }

    @RequestMapping("/mgvAllStoredPrograms/deleteMgvAllStoredPrograms.do")
    public String deleteMgvAllStoredPrograms(MgvAllStoredProgramsVO mgvAllStoredProgramsVO, @ModelAttribute("searchVO") MgvAllStoredProgramsDefaultVO searchVO, SessionStatus status) throws Exception {
        mgvAllStoredProgramsService.deleteMgvAllStoredPrograms(mgvAllStoredProgramsVO);
        status.setComplete();
        return "forward:/mgvAllStoredPrograms/MgvAllStoredProgramsList.do";
    }
}
