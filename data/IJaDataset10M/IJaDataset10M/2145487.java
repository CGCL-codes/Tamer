package egovframework.mbl.com.mgr.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import egovframework.mbl.com.mgr.service.MenuVO;
import egovframework.mbl.com.mgr.service.EgovMobileMenuService;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

/**
 * @Class Name : EgovMobileMenuServiceImpl.java
 * @Description : 모바일 템플릿 정보 관리를 위한 구현 클래스
 * @Modification Information
 *
 *    수정일                    수정자              수정내용
 *    -------        -------     -------------------
 *    2011.08.23     김민지              최초생성
 *
 * @author 모바일 개발환경 개발팀 김민지
 * @since 2011.08.23
 * @version 1.0
 *
 */
@Service("EgovMobileMenuService")
public class EgovMobileMenuServiceImpl extends AbstractServiceImpl implements EgovMobileMenuService {

    /** userManageDAO */
    @Resource(name = "MenuDAO")
    private MenuDAO menuDAO;

    /**
     * 메뉴를 삭제한다.
     */
    public void deleteMenu(MenuVO vo) throws Exception {
        menuDAO.deleteMenu(vo);
    }

    /**
     * 메뉴 정보를 입력한다.
     */
    public void insertMenu(MenuVO vo) throws Exception {
        menuDAO.insertMenu(vo);
    }

    /**
     * 상세메뉴를 조회한다.
     */
    public MenuVO selectMenuDetail(MenuVO vo) throws Exception {
        return menuDAO.selectMenuDetail(vo);
    }

    /**
     * 메뉴목록을 조회한다.
     */
    public Map<String, Object> selectMenuList(MenuVO vo) throws Exception {
        List<MenuVO> result = menuDAO.selectMenuList(vo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resultList", result);
        return map;
    }

    /**
     * 메뉴목록을 조회한다.
     */
    public Map<String, Object> selectUpperMenuList(MenuVO vo) throws Exception {
        List<MenuVO> result = menuDAO.selectUpperMenuList(vo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resultList", result);
        return map;
    }

    /**
     * 사용여부가 참인 목록만 조회한다.
     */
    public Map<String, Object> selectUseList(MenuVO vo) throws Exception {
        List<MenuVO> result = menuDAO.selectUseList(vo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resultList", result);
        return map;
    }

    /**
     * 메뉴를 수정한다.
     */
    public void updateMenu(MenuVO vo) throws Exception {
        menuDAO.updateMenu(vo);
    }
}
