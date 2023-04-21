package egovframework.rte.rex.brd.web;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import egovframework.rte.rex.brd.service.BoardVO;
import egovframework.rte.rex.brd.service.EgovBoardService;
import egovframework.rte.rex.com.service.SearchVO;

/**
 * 게시글 정보를 관리하는 컨트롤러 클래스를 정의한다.
 * @author 실행환경 개발팀 신혜연
 * @since 2011.07.11
 * @version 1.0
 * @see 
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.07.11  신혜연          최초 생성
 * 
 * </pre>
 */
@Controller
@SessionAttributes(types = BoardVO.class)
public class EgovBoardController {

    /** EgovBoardService */
    @Resource(name = "boardService")
    private EgovBoardService boardService;

    /** 
	 *글목록을 조회한다.(paging)
	 * @param searchVO 검색조건
	 * @param model
	 * @return "brd/egovBoardList"
	 * @throws Exception
	 */
    @RequestMapping(value = "/brd/egovBoardList.do")
    public String selectBoardList(@ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) throws Exception {
        searchVO.setPageUnit(5);
        searchVO.setPageSize(5);
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List boardList = boardService.selectBoardList(searchVO);
        model.addAttribute("resultList", boardList);
        int totCnt = boardService.selectBoardListTotCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "brd/egovBoardList";
    }
}
