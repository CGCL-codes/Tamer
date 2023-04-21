package kr.godsoft.egovframe.generatorwebapp.migr_datatype_transform_rule.web;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.migr_datatype_transform_rule.service.MigrDatatypeTransformRuleService;
import kr.godsoft.egovframe.generatorwebapp.migr_datatype_transform_rule.service.MigrDatatypeTransformRuleDefaultVO;
import kr.godsoft.egovframe.generatorwebapp.migr_datatype_transform_rule.service.MigrDatatypeTransformRuleVO;
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
 * @Class Name : MigrDatatypeTransformRuleController.java
 * @Description : MigrDatatypeTransformRule Controller class
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
public class MigrDatatypeTransformRuleController {

    @Resource(name = "migrDatatypeTransformRuleService")
    private MigrDatatypeTransformRuleService migrDatatypeTransformRuleService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * MIGR_DATATYPE_TRANSFORM_RULE 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 MigrDatatypeTransformRuleVO
	 * @return "/migrDatatypeTransformRule/MigrDatatypeTransformRuleList"
	 * @exception Exception
	 */
    @RequestMapping(value = "/migrDatatypeTransformRule/MigrDatatypeTransformRuleList.do")
    public String selectMigrDatatypeTransformRuleList(@ModelAttribute("searchVO") MigrDatatypeTransformRuleVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List<EgovMap> migrDatatypeTransformRuleList = migrDatatypeTransformRuleService.selectMigrDatatypeTransformRuleList(searchVO);
        model.addAttribute("resultList", migrDatatypeTransformRuleList);
        int totCnt = migrDatatypeTransformRuleService.selectMigrDatatypeTransformRuleListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "/migrDatatypeTransformRule/MigrDatatypeTransformRuleList";
    }

    @RequestMapping("/migrDatatypeTransformRule/addMigrDatatypeTransformRuleView.do")
    public String addMigrDatatypeTransformRuleView(@ModelAttribute("searchVO") MigrDatatypeTransformRuleDefaultVO searchVO, Model model) throws Exception {
        model.addAttribute("migrDatatypeTransformRuleVO", new MigrDatatypeTransformRuleVO());
        return "/migrDatatypeTransformRule/MigrDatatypeTransformRuleRegister";
    }

    @RequestMapping("/migrDatatypeTransformRule/addMigrDatatypeTransformRule.do")
    public String addMigrDatatypeTransformRule(MigrDatatypeTransformRuleVO migrDatatypeTransformRuleVO, @ModelAttribute("searchVO") MigrDatatypeTransformRuleDefaultVO searchVO, SessionStatus status) throws Exception {
        migrDatatypeTransformRuleService.insertMigrDatatypeTransformRule(migrDatatypeTransformRuleVO);
        status.setComplete();
        return "forward:/migrDatatypeTransformRule/MigrDatatypeTransformRuleList.do";
    }

    @RequestMapping("/migrDatatypeTransformRule/updateMigrDatatypeTransformRuleView.do")
    public String updateMigrDatatypeTransformRuleView(@RequestParam("id") String id, @ModelAttribute("searchVO") MigrDatatypeTransformRuleDefaultVO searchVO, Model model) throws Exception {
        MigrDatatypeTransformRuleVO migrDatatypeTransformRuleVO = new MigrDatatypeTransformRuleVO();
        migrDatatypeTransformRuleVO.setId(id);
        ;
        model.addAttribute(selectMigrDatatypeTransformRule(migrDatatypeTransformRuleVO, searchVO));
        return "/migrDatatypeTransformRule/MigrDatatypeTransformRuleRegister";
    }

    @RequestMapping("/migrDatatypeTransformRule/selectMigrDatatypeTransformRule.do")
    @ModelAttribute("migrDatatypeTransformRuleVO")
    public MigrDatatypeTransformRuleVO selectMigrDatatypeTransformRule(MigrDatatypeTransformRuleVO migrDatatypeTransformRuleVO, @ModelAttribute("searchVO") MigrDatatypeTransformRuleDefaultVO searchVO) throws Exception {
        return migrDatatypeTransformRuleService.selectMigrDatatypeTransformRule(migrDatatypeTransformRuleVO);
    }

    @RequestMapping("/migrDatatypeTransformRule/updateMigrDatatypeTransformRule.do")
    public String updateMigrDatatypeTransformRule(MigrDatatypeTransformRuleVO migrDatatypeTransformRuleVO, @ModelAttribute("searchVO") MigrDatatypeTransformRuleDefaultVO searchVO, SessionStatus status) throws Exception {
        migrDatatypeTransformRuleService.updateMigrDatatypeTransformRule(migrDatatypeTransformRuleVO);
        status.setComplete();
        return "forward:/migrDatatypeTransformRule/MigrDatatypeTransformRuleList.do";
    }

    @RequestMapping("/migrDatatypeTransformRule/deleteMigrDatatypeTransformRule.do")
    public String deleteMigrDatatypeTransformRule(MigrDatatypeTransformRuleVO migrDatatypeTransformRuleVO, @ModelAttribute("searchVO") MigrDatatypeTransformRuleDefaultVO searchVO, SessionStatus status) throws Exception {
        migrDatatypeTransformRuleService.deleteMigrDatatypeTransformRule(migrDatatypeTransformRuleVO);
        status.setComplete();
        return "forward:/migrDatatypeTransformRule/MigrDatatypeTransformRuleList.do";
    }
}
