package kr.godsoft.egovframe.generatorwebapp.comtnuserlog.web;

import java.util.List;
import javax.annotation.Resource;
import kr.godsoft.egovframe.generatorwebapp.comtnuserlog.service.ComtnuserlogService;
import kr.godsoft.egovframe.generatorwebapp.comtnuserlog.service.ComtnuserlogDefaultVO;
import kr.godsoft.egovframe.generatorwebapp.comtnuserlog.service.ComtnuserlogVO;
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
 * @Class Name : ComtnuserlogController.java
 * @Description : Comtnuserlog Controller class
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
public class ComtnuserlogController {

    @Resource(name = "comtnuserlogService")
    private ComtnuserlogService comtnuserlogService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /**
	 * COMTNUSERLOG 목록을 조회한다. (pageing)
	 * @param searchVO - 조회할 정보가 담긴 ComtnuserlogVO
	 * @return "/comtnuserlog/ComtnuserlogList"
	 * @exception Exception
	 */
    @RequestMapping(value = "/comtnuserlog/ComtnuserlogList.do")
    public String selectComtnuserlogList(@ModelAttribute("searchVO") ComtnuserlogVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List<EgovMap> comtnuserlogList = comtnuserlogService.selectComtnuserlogList(searchVO);
        model.addAttribute("resultList", comtnuserlogList);
        int totCnt = comtnuserlogService.selectComtnuserlogListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "/comtnuserlog/ComtnuserlogList";
    }

    @RequestMapping("/comtnuserlog/addComtnuserlogView.do")
    public String addComtnuserlogView(@ModelAttribute("searchVO") ComtnuserlogDefaultVO searchVO, Model model) throws Exception {
        model.addAttribute("comtnuserlogVO", new ComtnuserlogVO());
        return "/comtnuserlog/ComtnuserlogRegister";
    }

    @RequestMapping("/comtnuserlog/addComtnuserlog.do")
    public String addComtnuserlog(ComtnuserlogVO comtnuserlogVO, @ModelAttribute("searchVO") ComtnuserlogDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnuserlogService.insertComtnuserlog(comtnuserlogVO);
        status.setComplete();
        return "forward:/comtnuserlog/ComtnuserlogList.do";
    }

    @RequestMapping("/comtnuserlog/updateComtnuserlogView.do")
    public String updateComtnuserlogView(@RequestParam("occrrncDe") String occrrncDe, @RequestParam("rqesterId") String rqesterId, @RequestParam("svcNm") String svcNm, @RequestParam("methodNm") String methodNm, @ModelAttribute("searchVO") ComtnuserlogDefaultVO searchVO, Model model) throws Exception {
        ComtnuserlogVO comtnuserlogVO = new ComtnuserlogVO();
        comtnuserlogVO.setOccrrncDe(occrrncDe);
        comtnuserlogVO.setRqesterId(rqesterId);
        comtnuserlogVO.setSvcNm(svcNm);
        comtnuserlogVO.setMethodNm(methodNm);
        ;
        model.addAttribute(selectComtnuserlog(comtnuserlogVO, searchVO));
        return "/comtnuserlog/ComtnuserlogRegister";
    }

    @RequestMapping("/comtnuserlog/selectComtnuserlog.do")
    @ModelAttribute("comtnuserlogVO")
    public ComtnuserlogVO selectComtnuserlog(ComtnuserlogVO comtnuserlogVO, @ModelAttribute("searchVO") ComtnuserlogDefaultVO searchVO) throws Exception {
        return comtnuserlogService.selectComtnuserlog(comtnuserlogVO);
    }

    @RequestMapping("/comtnuserlog/updateComtnuserlog.do")
    public String updateComtnuserlog(ComtnuserlogVO comtnuserlogVO, @ModelAttribute("searchVO") ComtnuserlogDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnuserlogService.updateComtnuserlog(comtnuserlogVO);
        status.setComplete();
        return "forward:/comtnuserlog/ComtnuserlogList.do";
    }

    @RequestMapping("/comtnuserlog/deleteComtnuserlog.do")
    public String deleteComtnuserlog(ComtnuserlogVO comtnuserlogVO, @ModelAttribute("searchVO") ComtnuserlogDefaultVO searchVO, SessionStatus status) throws Exception {
        comtnuserlogService.deleteComtnuserlog(comtnuserlogVO);
        status.setComplete();
        return "forward:/comtnuserlog/ComtnuserlogList.do";
    }
}
