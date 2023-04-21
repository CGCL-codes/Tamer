package kr.godsoft.egovframe.generatorwebapp.comthemaildsptchmanage.service.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.godsoft.egovframe.generatorwebapp.comthemaildsptchmanage.service.ComthemaildsptchmanageVO;

/**
 * @Class Name : ComthemaildsptchmanageDAO.java
 * @Description : Comthemaildsptchmanage DAO Class
 * @Modification Information
 *
 * @author 이백행
 * @since 2012-03-30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Repository("comthemaildsptchmanageDAO")
public class ComthemaildsptchmanageDAO extends EgovAbstractDAO {

    /**
	 * COMTHEMAILDSPTCHMANAGE을 등록한다.
	 * @param vo - 등록할 정보가 담긴 ComthemaildsptchmanageVO
	 * @return 등록 결과
	 * @exception Exception
	 */
    public String insertComthemaildsptchmanage(ComthemaildsptchmanageVO vo) throws Exception {
        return (String) insert("comthemaildsptchmanageDAO.insertComthemaildsptchmanage_S", vo);
    }

    /**
	 * COMTHEMAILDSPTCHMANAGE을 수정한다.
	 * @param vo - 수정할 정보가 담긴 ComthemaildsptchmanageVO
	 * @return void형
	 * @exception Exception
	 */
    public void updateComthemaildsptchmanage(ComthemaildsptchmanageVO vo) throws Exception {
        update("comthemaildsptchmanageDAO.updateComthemaildsptchmanage_S", vo);
    }

    /**
	 * COMTHEMAILDSPTCHMANAGE을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 ComthemaildsptchmanageVO
	 * @return void형 
	 * @exception Exception
	 */
    public void deleteComthemaildsptchmanage(ComthemaildsptchmanageVO vo) throws Exception {
        delete("comthemaildsptchmanageDAO.deleteComthemaildsptchmanage_S", vo);
    }

    /**
	 * COMTHEMAILDSPTCHMANAGE을 조회한다.
	 * @param vo - 조회할 정보가 담긴 ComthemaildsptchmanageVO
	 * @return 조회한 COMTHEMAILDSPTCHMANAGE
	 * @exception Exception
	 */
    public ComthemaildsptchmanageVO selectComthemaildsptchmanage(ComthemaildsptchmanageVO vo) throws Exception {
        return (ComthemaildsptchmanageVO) selectByPk("comthemaildsptchmanageDAO.selectComthemaildsptchmanage_S", vo);
    }

    /**
	 * COMTHEMAILDSPTCHMANAGE 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTHEMAILDSPTCHMANAGE 목록
	 * @exception Exception
	 */
    @SuppressWarnings("unchecked")
    public List<EgovMap> selectComthemaildsptchmanageList(ComthemaildsptchmanageVO vo) throws Exception {
        return list("comthemaildsptchmanageDAO.selectComthemaildsptchmanageList_D", vo);
    }

    /**
	 * COMTHEMAILDSPTCHMANAGE 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return COMTHEMAILDSPTCHMANAGE 총 갯수
	 * @exception
	 */
    public int selectComthemaildsptchmanageListTotCnt(ComthemaildsptchmanageVO vo) {
        return (Integer) getSqlMapClientTemplate().queryForObject("comthemaildsptchmanageDAO.selectComthemaildsptchmanageListTotCnt_S", vo);
    }
}
