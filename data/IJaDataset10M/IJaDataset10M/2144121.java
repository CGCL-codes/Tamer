package kr.godsoft.egovframe.generatorwebapp.comtneventmanage.service.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.godsoft.egovframe.generatorwebapp.comtneventmanage.service.ComtneventmanageVO;

/**
 * @Class Name : ComtneventmanageDAO.java
 * @Description : Comtneventmanage DAO Class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Repository("comtneventmanageDAO")
public class ComtneventmanageDAO extends EgovAbstractDAO {

    /**
	 * COMTNEVENTMANAGE을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComtneventmanageVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComtneventmanage(ComtneventmanageVO vo) throws Exception {
        return (String) insert("comtneventmanageDAO.insertComtneventmanage_S", vo);
    }

    /**
	 * COMTNEVENTMANAGE을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComtneventmanageVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComtneventmanage(ComtneventmanageVO vo) throws Exception {
        update("comtneventmanageDAO.updateComtneventmanage_S", vo);
    }

    /**
	 * COMTNEVENTMANAGE을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComtneventmanageVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComtneventmanage(ComtneventmanageVO vo) throws Exception {
        delete("comtneventmanageDAO.deleteComtneventmanage_S", vo);
    }

    /**
	 * COMTNEVENTMANAGE을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComtneventmanageVO
	 * @return 조회한 COMTNEVENTMANAGE
	 * @exception Exception
	 */
    public ComtneventmanageVO selectComtneventmanage(ComtneventmanageVO vo) throws Exception {
        return (ComtneventmanageVO) selectByPk("comtneventmanageDAO.selectComtneventmanage_S", vo);
    }

    /**
	 * COMTNEVENTMANAGE 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNEVENTMANAGE 목록
	 * @exception Exception
	 */
    @SuppressWarnings("unchecked")
    public List<EgovMap> selectComtneventmanageList(ComtneventmanageVO vo) throws Exception {
        return list("comtneventmanageDAO.selectComtneventmanageList_D", vo);
    }

    /**
	 * COMTNEVENTMANAGE 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTNEVENTMANAGE 총 갯수
	 * @exception
	 */
    public int selectComtneventmanageListTotCnt(ComtneventmanageVO vo) {
        return (Integer) getSqlMapClientTemplate().queryForObject("comtneventmanageDAO.selectComtneventmanageListTotCnt_S", vo);
    }
}
