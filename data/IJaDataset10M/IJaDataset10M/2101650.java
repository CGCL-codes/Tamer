package egovframework.com.uss.ion.rss.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springmodules.validation.commons.DefaultBeanValidator;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.uss.ion.rss.service.EgovRssTagManageService;
import egovframework.com.uss.ion.rss.service.RssManage;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * RSS태그관리를 처리하는 Controller Class 구현
 * @author 공통서비스 장동한
 * @since 2010.06.16
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2010.06.16  장동한          최초 생성
 *   2011.8.26	정진오			IncludedInfo annotation 추가
 * 
 * </pre>
 */
@Controller
public class EgovRssTagManageController {

    @Autowired
    private DefaultBeanValidator beanValidator;

    /** EgovMessageSource */
    @Resource(name = "egovMessageSource")
    EgovMessageSource egovMessageSource;

    /** egovOnlinePollService */
    @Resource(name = "egovRssManageService")
    private EgovRssTagManageService egovRssManageService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    @RequestMapping(value = "/uss/ion/rss/listRssTagManageTableColumnList.do")
    public String EgovRssTagManageTableColumnList(Map commandMap, ModelMap model) throws Exception {
        String sDbType = (String) egovMessageSource.getMessage("Globals.DbType");
        String sTableName = commandMap.get("tableName") == null ? "" : (String) commandMap.get("tableName");
        HashMap hmParam = new HashMap();
        hmParam.put("dbType", sDbType);
        hmParam.put("tableName", sTableName);
        ArrayList arrListResult = (ArrayList) egovRssManageService.selectRssTagManageTableColumnList(hmParam);
        model.addAttribute("ColumnList", arrListResult);
        return "egovframework/com/uss/ion/rss/EgovRssTagManageTableColumnList";
    }

    /**
     * RSS태그관리 목록을 조회한다.
     * @param searchVO -검색정보가 담긴 객체 
     * @param commandMap -Request Variable
     * @param rssManage -RSS태그관리 객체
     * @param model -Spring 제공하는 ModelMap
     * @return String -리턴 URL
     * @throws Exception
     */
    @IncludedInfo(name = "RSS태그관리", listUrl = "/uss/ion/rss/listRssTagManage.do", order = 820, gid = 50)
    @RequestMapping(value = "/uss/ion/rss/listRssTagManage.do")
    public String EgovRssTagManageList(@ModelAttribute("searchVO") RssManage searchVO, Map commandMap, RssManage rssManage, ModelMap model) throws Exception {
        String sCmd = commandMap.get("cmd") == null ? "" : (String) commandMap.get("cmd");
        Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
        if (!isAuthenticated) {
            model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
            return "egovframework/com/uat/uia/EgovLoginUsr";
        }
        LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        if (sCmd.equals("del")) {
            if (commandMap.get("checkList") instanceof String) {
                String sCheckList = (String) commandMap.get("checkList");
                rssManage.setFrstRegisterId((String) loginVO.getUniqId());
                rssManage.setLastUpdusrId((String) loginVO.getUniqId());
                rssManage.setRssId(sCheckList);
                egovRssManageService.deleteRssTagManage(rssManage);
            }
            if (commandMap.get("checkList") instanceof String[]) {
                String[] sArrCheckList = (String[]) commandMap.get("checkList");
                for (int i = 0; i < sArrCheckList.length; i++) {
                    rssManage.setFrstRegisterId((String) loginVO.getUniqId());
                    rssManage.setLastUpdusrId((String) loginVO.getUniqId());
                    rssManage.setRssId(sArrCheckList[i]);
                    egovRssManageService.deleteRssTagManage(rssManage);
                }
            }
            searchVO.setPageIndex(1);
        }
        searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
        searchVO.setPageSize(propertiesService.getInt("pageSize"));
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
        paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
        paginationInfo.setPageSize(searchVO.getPageSize());
        searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
        searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
        searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
        List reusltList = egovRssManageService.selectRssTagManageList(searchVO);
        model.addAttribute("resultList", reusltList);
        model.addAttribute("searchKeyword", commandMap.get("searchKeyword") == null ? "" : (String) commandMap.get("searchKeyword"));
        model.addAttribute("searchCondition", commandMap.get("searchCondition") == null ? "" : (String) commandMap.get("searchCondition"));
        int totCnt = (Integer) egovRssManageService.selectRssTagManageListCnt(searchVO);
        paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        return "egovframework/com/uss/ion/rss/EgovRssTagManageList";
    }

    /**
     * RSS태그관리 목록을 상세조회 조회한다.
     * @param searchVO -검색정보가 담긴 객체 
     * @param rssManage -RSS태그관리 객체
     * @param commandMap -Request Variable
     * @param model -Spring 제공하는 ModelMap
     * @return String -리턴 URL
     * @throws Exception
     */
    @RequestMapping(value = "/uss/ion/rss/detailRssTagManage.do")
    public String EgovRssTagManageDetail(@ModelAttribute("searchVO") RssManage searchVO, RssManage rssManage, Map commandMap, ModelMap model) throws Exception {
        String sLocationUrl = "egovframework/com/uss/ion/rss/EgovRssTagManageDetail";
        String sCmd = commandMap.get("cmd") == null ? "" : (String) commandMap.get("cmd");
        if (sCmd.equals("del")) {
            egovRssManageService.deleteRssTagManage(rssManage);
            sLocationUrl = "redirect:/uss/ion/rss/listRssTagManage.do";
        } else {
            RssManage rssManages = (RssManage) egovRssManageService.selectRssTagManageDetail(rssManage);
            model.addAttribute("rssManage", rssManages);
        }
        return sLocationUrl;
    }

    /**
     * RSS태그관리를 수정한다.
     * @param searchVO -검색정보가 담긴 객체
     * @param commandMap -Request Variable
     * @param rssManage -RSS태그관리 객체
     * @param BindingResult	-Validator 하기위한 객체
     * @param model -Spring 제공하는 ModelMap
     * @return String -리턴 URL
     * @throws Exception
     */
    @RequestMapping(value = "/uss/ion/rss/updtRssTagManage.do")
    public String EgovRssTagManageModify(@ModelAttribute("searchVO") RssManage searchVO, Map commandMap, @ModelAttribute("rssManage") RssManage rssManage, BindingResult bindingResult, ModelMap model) throws Exception {
        Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
        if (!isAuthenticated) {
            model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
            return "egovframework/com/uat/uia/EgovLoginUsr";
        }
        LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        String sLocationUrl = "egovframework/com/uss/ion/rss/EgovRssTagManageUpdt";
        String sCmd = commandMap.get("cmd") == null ? "" : (String) commandMap.get("cmd");
        if (sCmd.equals("save")) {
            beanValidator.validate(rssManage, bindingResult);
            if (bindingResult.hasErrors()) {
                return sLocationUrl;
            }
            rssManage.setFrstRegisterId((String) loginVO.getUniqId());
            rssManage.setLastUpdusrId((String) loginVO.getUniqId());
            egovRssManageService.updateRssTagManage(rssManage);
            sLocationUrl = "forward:/uss/ion/rss/listRssTagManage.do";
        } else {
            model.addAttribute("trgetSvcTableList", (List) egovRssManageService.selectRssTagManageTableList());
            RssManage rssManageVO = egovRssManageService.selectRssTagManageDetail(rssManage);
            model.addAttribute("rssManage", rssManageVO);
        }
        return sLocationUrl;
    }

    /**
     * RSS태그관리를 등록한다.
     * @param searchVO -검색정보가 담긴 객체
     * @param commandMap -Request Variable
     * @param rssManage -RSS태그관리 객체
     * @param BindingResult	-Validator 하기위한 객체
     * @param model -Spring 제공하는 ModelMap
     * @return String -리턴 URL
     * @throws Exception
     */
    @RequestMapping(value = "/uss/ion/rss/registRssTagManage.do")
    public String EgovRssTagManageRegist(@ModelAttribute("searchVO") RssManage searchVO, Map commandMap, @ModelAttribute("rssManage") RssManage rssManage, BindingResult bindingResult, ModelMap model) throws Exception {
        Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
        if (!isAuthenticated) {
            model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
            return "egovframework/com/uat/uia/EgovLoginUsr";
        }
        LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        String sLocationUrl = "egovframework/com/uss/ion/rss/EgovRssTagManageRegist";
        String sCmd = commandMap.get("cmd") == null ? "" : (String) commandMap.get("cmd");
        if (sCmd.equals("save")) {
            beanValidator.validate(rssManage, bindingResult);
            if (bindingResult.hasErrors()) {
                return sLocationUrl;
            }
            rssManage.setFrstRegisterId((String) loginVO.getUniqId());
            rssManage.setLastUpdusrId((String) loginVO.getUniqId());
            egovRssManageService.insertRssTagManage(rssManage);
            sLocationUrl = "forward:/uss/ion/rss/listRssTagManage.do";
        } else {
            model.addAttribute("trgetSvcTableList", (List) egovRssManageService.selectRssTagManageTableList());
        }
        return sLocationUrl;
    }
}
