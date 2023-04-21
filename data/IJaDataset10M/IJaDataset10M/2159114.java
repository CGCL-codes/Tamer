package kr.godsoft.egovframe.generatorwebapp.comtnfiledetail.web;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtnfiledetail.service.ComtnfiledetailService;
import kr.godsoft.egovframe.generatorwebapp.comtnfiledetail.service.ComtnfiledetailDefaultVO;
import kr.godsoft.egovframe.generatorwebapp.comtnfiledetail.service.ComtnfiledetailVO;
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
 * @Class Name : ComtnfiledetailController.java
 * @Description : Comtnfiledetail Controller class
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
public class ComtnfiledetailController {

    @Resource(name = "comtnfiledetailService")
    private ComtnfiledetailService comtnfiledetailService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * COMTNFILEDETAIL 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 ComtnfiledetailVO
	 * @return "/comtnfiledetail/ComtnfiledetailList"
	 * @exception Exception
	 */
    @RequestMapping(value = "/comtnfiledetail/ComtnfiledetailList.do")
    public String selectComtnfiledetailList(@ModelAttribute("searchVO") ComtnfiledetailVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List<EgovMap> comtnfiledetailList = comtnfiledetailService.selectComtnfiledetailList(searchVO);
        model.addAttribute("resultList", comtnfiledetailList);
        int totCnt = comtnfiledetailService.selectComtnfiledetailListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "/comtnfiledetail/ComtnfiledetailList";
    }

    @RequestMapping("/comtnfiledetail/addComtnfiledetailView.do")
    public String addComtnfiledetailView(@ModelAttribute("searchVO") ComtnfiledetailDefaultVO searchVO, Model model) throws Exception {
        model.addAttribute("comtnfiledetailVO", new ComtnfiledetailVO());
        return "/comtnfiledetail/ComtnfiledetailRegister";
    }

    @RequestMapping("/comtnfiledetail/addComtnfiledetail.do")
    public String addComtnfiledetail(ComtnfiledetailVO comtnfiledetailVO, @ModelAttribute("searchVO") ComtnfiledetailDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnfiledetailService.insertComtnfiledetail(comtnfiledetailVO);
        status.setComplete();
        return "forward:/comtnfiledetail/ComtnfiledetailList.do";
    }

    @RequestMapping("/comtnfiledetail/updateComtnfiledetailView.do")
    public String updateComtnfiledetailView(@RequestParam("atchFileId") String atchFileId, @RequestParam("fileSn") String fileSn, @ModelAttribute("searchVO") ComtnfiledetailDefaultVO searchVO, Model model) throws Exception {
        ComtnfiledetailVO comtnfiledetailVO = new ComtnfiledetailVO();
        comtnfiledetailVO.setAtchFileId(atchFileId);
        comtnfiledetailVO.setFileSn(fileSn);
        ;
        model.addAttribute(selectComtnfiledetail(comtnfiledetailVO, searchVO));
        return "/comtnfiledetail/ComtnfiledetailRegister";
    }

    @RequestMapping("/comtnfiledetail/selectComtnfiledetail.do")
    @ModelAttribute("comtnfiledetailVO")
    public ComtnfiledetailVO selectComtnfiledetail(ComtnfiledetailVO comtnfiledetailVO, @ModelAttribute("searchVO") ComtnfiledetailDefaultVO searchVO) throws Exception {
        return comtnfiledetailService.selectComtnfiledetail(comtnfiledetailVO);
    }

    @RequestMapping("/comtnfiledetail/updateComtnfiledetail.do")
    public String updateComtnfiledetail(ComtnfiledetailVO comtnfiledetailVO, @ModelAttribute("searchVO") ComtnfiledetailDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnfiledetailService.updateComtnfiledetail(comtnfiledetailVO);
        status.setComplete();
        return "forward:/comtnfiledetail/ComtnfiledetailList.do";
    }

    @RequestMapping("/comtnfiledetail/deleteComtnfiledetail.do")
    public String deleteComtnfiledetail(ComtnfiledetailVO comtnfiledetailVO, @ModelAttribute("searchVO") ComtnfiledetailDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnfiledetailService.deleteComtnfiledetail(comtnfiledetailVO);
        status.setComplete();
        return "forward:/comtnfiledetail/ComtnfiledetailList.do";
    }
}
