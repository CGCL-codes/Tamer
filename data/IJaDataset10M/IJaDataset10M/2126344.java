package kr.godsoft.egovframe.generatorwebapp.comtnintnetsvc.service.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.godsoft.egovframe.generatorwebapp.comtnintnetsvc.service.ComtnintnetsvcVO;

/**
 * @Class Name : ComtnintnetsvcDAO.java
 * @Description : Comtnintnetsvc DAO Class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Repository("comtnintnetsvcDAO")
public class ComtnintnetsvcDAO extends EgovAbstractDAO {

    /**
	 * COMTNINTNETSVC을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtnintnetsvcVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtnintnetsvc(ComtnintnetsvcVO vo) throws Exception {
        return (String) insert("comtnintnetsvcDAO.insertComtnintnetsvc_S", vo);
    }

    /**
	 * COMTNINTNETSVC을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtnintnetsvcVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtnintnetsvc(ComtnintnetsvcVO vo) throws Exception {
        update("comtnintnetsvcDAO.updateComtnintnetsvc_S", vo);
    }

    /**
	 * COMTNINTNETSVC을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtnintnetsvcVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtnintnetsvc(ComtnintnetsvcVO vo) throws Exception {
        delete("comtnintnetsvcDAO.deleteComtnintnetsvc_S", vo);
    }

    /**
	 * COMTNINTNETSVC을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtnintnetsvcVO
	 * @return 조회한 COMTNINTNETSVC
	 * @exception Exception
	 */
    public ComtnintnetsvcVO selectComtnintnetsvc(ComtnintnetsvcVO vo) throws Exception {
        return (ComtnintnetsvcVO) selectByPk("comtnintnetsvcDAO.selectComtnintnetsvc_S", vo);
    }

    /**
	 * COMTNINTNETSVC 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNINTNETSVC 목록
	 * @exception Exception
	 */
    @SuppressWarnings("unchecked")
    public List<EgovMap> selectComtnintnetsvcList(ComtnintnetsvcVO vo) throws Exception {
        return list("comtnintnetsvcDAO.selectComtnintnetsvcList_D", vo);
    }

    /**
	 * COMTNINTNETSVC 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNINTNETSVC 총 갯수
	 * @exception
	 */
    public int selectComtnintnetsvcListTotCnt(ComtnintnetsvcVO vo) {
        return (Integer) getSqlMapClientTemplate().queryForObject("comtnintnetsvcDAO.selectComtnintnetsvcListTotCnt_S", vo);
    }
}
