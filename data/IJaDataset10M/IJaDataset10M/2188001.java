package kr.godsoft.egovframe.generatorwebapp.md_columns.web;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.md_columns.service.MdColumnsService;
import kr.godsoft.egovframe.generatorwebapp.md_columns.service.MdColumnsDefaultVO;
import kr.godsoft.egovframe.generatorwebapp.md_columns.service.MdColumnsVO;
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
 * @Class Name : MdColumnsController.java
 * @Description : MdColumns Controller class
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
public class MdColumnsController {

    @Resource(name = "mdColumnsService")
    private MdColumnsService mdColumnsService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * MD_COLUMNS 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 MdColumnsVO
	 * @return "/mdColumns/MdColumnsList"
	 * @exception Exception
	 */
    @RequestMapping(value = "/mdColumns/MdColumnsList.do")
    public String selectMdColumnsList(@ModelAttribute("searchVO") MdColumnsVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List<EgovMap> mdColumnsList = mdColumnsService.selectMdColumnsList(searchVO);
        model.addAttribute("resultList", mdColumnsList);
        int totCnt = mdColumnsService.selectMdColumnsListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "/mdColumns/MdColumnsList";
    }

    @RequestMapping("/mdColumns/addMdColumnsView.do")
    public String addMdColumnsView(@ModelAttribute("searchVO") MdColumnsDefaultVO searchVO, Model model) throws Exception {
        model.addAttribute("mdColumnsVO", new MdColumnsVO());
        return "/mdColumns/MdColumnsRegister";
    }

    @RequestMapping("/mdColumns/addMdColumns.do")
    public String addMdColumns(MdColumnsVO mdColumnsVO, @ModelAttribute("searchVO") MdColumnsDefaultVO searchVO, SessionStatus status) throws Exception {
        mdColumnsService.insertMdColumns(mdColumnsVO);
        status.setComplete();
        return "forward:/mdColumns/MdColumnsList.do";
    }

    @RequestMapping("/mdColumns/updateMdColumnsView.do")
    public String updateMdColumnsView(@RequestParam("id") String id, @ModelAttribute("searchVO") MdColumnsDefaultVO searchVO, Model model) throws Exception {
        MdColumnsVO mdColumnsVO = new MdColumnsVO();
        mdColumnsVO.setId(id);
        ;
        model.addAttribute(selectMdColumns(mdColumnsVO, searchVO));
        return "/mdColumns/MdColumnsRegister";
    }

    @RequestMapping("/mdColumns/selectMdColumns.do")
    @ModelAttribute("mdColumnsVO")
    public MdColumnsVO selectMdColumns(MdColumnsVO mdColumnsVO, @ModelAttribute("searchVO") MdColumnsDefaultVO searchVO) throws Exception {
        return mdColumnsService.selectMdColumns(mdColumnsVO);
    }

    @RequestMapping("/mdColumns/updateMdColumns.do")
    public String updateMdColumns(MdColumnsVO mdColumnsVO, @ModelAttribute("searchVO") MdColumnsDefaultVO searchVO, SessionStatus status) throws Exception {
        mdColumnsService.updateMdColumns(mdColumnsVO);
        status.setComplete();
        return "forward:/mdColumns/MdColumnsList.do";
    }

    @RequestMapping("/mdColumns/deleteMdColumns.do")
    public String deleteMdColumns(MdColumnsVO mdColumnsVO, @ModelAttribute("searchVO") MdColumnsDefaultVO searchVO, SessionStatus status) throws Exception {
        mdColumnsService.deleteMdColumns(mdColumnsVO);
        status.setComplete();
        return "forward:/mdColumns/MdColumnsList.do";
    }
}
