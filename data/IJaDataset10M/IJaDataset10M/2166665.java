package kr.godsoft.egovframe.generatorwebapp.comtecopseq.web;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtecopseq.service.ComtecopseqService;
import kr.godsoft.egovframe.generatorwebapp.comtecopseq.service.ComtecopseqDefaultVO;
import kr.godsoft.egovframe.generatorwebapp.comtecopseq.service.ComtecopseqVO;
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
 * @Class Name : ComtecopseqController.java
 * @Description : Comtecopseq Controller class
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
public class ComtecopseqController {

    @Resource(name = "comtecopseqService")
    private ComtecopseqService comtecopseqService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * COMTECOPSEQ 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 ComtecopseqVO
	 * @return "/comtecopseq/ComtecopseqList"
	 * @exception Exception
	 */
    @RequestMapping(value = "/comtecopseq/ComtecopseqList.do")
    public String selectComtecopseqList(@ModelAttribute("searchVO") ComtecopseqVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List<EgovMap> comtecopseqList = comtecopseqService.selectComtecopseqList(searchVO);
        model.addAttribute("resultList", comtecopseqList);
        int totCnt = comtecopseqService.selectComtecopseqListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "/comtecopseq/ComtecopseqList";
    }

    @RequestMapping("/comtecopseq/addComtecopseqView.do")
    public String addComtecopseqView(@ModelAttribute("searchVO") ComtecopseqDefaultVO searchVO, Model model) throws Exception {
        model.addAttribute("comtecopseqVO", new ComtecopseqVO());
        return "/comtecopseq/ComtecopseqRegister";
    }

    @RequestMapping("/comtecopseq/addComtecopseq.do")
    public String addComtecopseq(ComtecopseqVO comtecopseqVO, @ModelAttribute("searchVO") ComtecopseqDefaultVO searchVO, SessionStatus status) throws Exception {
        comtecopseqService.insertComtecopseq(comtecopseqVO);
        status.setComplete();
        return "forward:/comtecopseq/ComtecopseqList.do";
    }

    @RequestMapping("/comtecopseq/updateComtecopseqView.do")
    public String updateComtecopseqView(@RequestParam("tableName") String tableName, @ModelAttribute("searchVO") ComtecopseqDefaultVO searchVO, Model model) throws Exception {
        ComtecopseqVO comtecopseqVO = new ComtecopseqVO();
        comtecopseqVO.setTableName(tableName);
        ;
        model.addAttribute(selectComtecopseq(comtecopseqVO, searchVO));
        return "/comtecopseq/ComtecopseqRegister";
    }

    @RequestMapping("/comtecopseq/selectComtecopseq.do")
    @ModelAttribute("comtecopseqVO")
    public ComtecopseqVO selectComtecopseq(ComtecopseqVO comtecopseqVO, @ModelAttribute("searchVO") ComtecopseqDefaultVO searchVO) throws Exception {
        return comtecopseqService.selectComtecopseq(comtecopseqVO);
    }

    @RequestMapping("/comtecopseq/updateComtecopseq.do")
    public String updateComtecopseq(ComtecopseqVO comtecopseqVO, @ModelAttribute("searchVO") ComtecopseqDefaultVO searchVO, SessionStatus status) throws Exception {
        comtecopseqService.updateComtecopseq(comtecopseqVO);
        status.setComplete();
        return "forward:/comtecopseq/ComtecopseqList.do";
    }

    @RequestMapping("/comtecopseq/deleteComtecopseq.do")
    public String deleteComtecopseq(ComtecopseqVO comtecopseqVO, @ModelAttribute("searchVO") ComtecopseqDefaultVO searchVO, SessionStatus status) throws Exception {
        comtecopseqService.deleteComtecopseq(comtecopseqVO);
        status.setComplete();
        return "forward:/comtecopseq/ComtecopseqList.do";
    }
}
